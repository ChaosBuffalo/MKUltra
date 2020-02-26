package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.client.audio.MovingSoundCasting;
import com.chaosbuffalo.mkultra.core.abilities.cast_states.CastState;
import com.chaosbuffalo.mkultra.core.events.PlayerAbilityEvent;
import com.chaosbuffalo.mkultra.core.events.PlayerClassEvent;
import com.chaosbuffalo.mkultra.core.talents.PassiveAbilityTalent;
import com.chaosbuffalo.mkultra.core.talents.RangedAttributeTalent;
import com.chaosbuffalo.mkultra.core.talents.TalentTreeRecord;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.mkultra.effects.passives.PassiveAbilityPotionBase;
import com.chaosbuffalo.mkultra.effects.spells.ArmorTrainingPotion;
import com.chaosbuffalo.mkultra.event.ItemEventHandler;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.network.packets.*;
import com.chaosbuffalo.mkultra.utils.AbilityUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;

public class PlayerData implements IPlayerData {

    private final EntityPlayer player;
    private float regenTime;
    private float healthRegenTime;
    private AbilityTracker abilityTracker;
    private Map<ResourceLocation, PlayerClassInfo> knownClasses = new HashMap<>();
    private Map<ResourceLocation, PlayerToggleAbility> activeToggleMap = new HashMap<>();
    private boolean needPassiveTalentRefresh;
    private boolean talentPassivesUnlocked;
    private EnumHandSide originalMainHand;
    private boolean isDualWielding;
    private int ticksSinceLastSwing;
    private final static int DUAL_WIELD_TIMEOUT = 25;
    private Set<String> activeSpellTriggers;
    private boolean dirty;
    private float mana;
    private PlayerCastingState currentCast;
    private ResourceLocation activeClassId;


    public PlayerData(EntityPlayer player) {
        this.player = player;
        regenTime = 0;
        healthRegenTime = 0;
        mana = 0f;
        ticksSinceLastSwing = 0;
        isDualWielding = false;
        originalMainHand = player.getPrimaryHand();
        abilityTracker = AbilityTracker.getTracker(player);
        activeSpellTriggers = new HashSet<>();
        currentCast = null;
        activeClassId = MKURegistry.INVALID_CLASS;
        registerAttributes();
    }

    private void registerAttributes() {
        player.getAttributeMap().registerAttribute(PlayerAttributes.MAX_MANA);
        player.getAttributeMap().registerAttribute(PlayerAttributes.MANA_REGEN);
        player.getAttributeMap().registerAttribute(PlayerAttributes.MAGIC_ATTACK_DAMAGE);
        player.getAttributeMap().registerAttribute(PlayerAttributes.MAGIC_ARMOR);
        player.getAttributeMap().registerAttribute(PlayerAttributes.COOLDOWN);
        player.getAttributeMap().registerAttribute(PlayerAttributes.MELEE_CRIT);
        player.getAttributeMap().registerAttribute(PlayerAttributes.SPELL_CRIT);
        player.getAttributeMap().registerAttribute(PlayerAttributes.SPELL_CRITICAL_DAMAGE);
        player.getAttributeMap().registerAttribute(PlayerAttributes.HEALTH_REGEN);
        player.getAttributeMap().registerAttribute(PlayerAttributes.HEAL_BONUS);
        player.getAttributeMap().registerAttribute(PlayerAttributes.MELEE_CRITICAL_DAMAGE);
        player.getAttributeMap().registerAttribute(PlayerAttributes.BUFF_DURATION);
    }

    private void markDirty() {
        dirty = true;
    }

    private IMessage getUpdateMessage() {
        if (dirty) {
            dirty = false;
            return new PlayerDataSyncPacket(this, player.getUniqueID());
        }
        return null;
    }

    @Nullable
    private PlayerClassInfo getActiveClass() {
        return knownClasses.get(getClassId());
    }

    public boolean spendTalentPoint(ResourceLocation talentTree, String line, int index) {
        PlayerClassInfo classInfo = getActiveClass();
        if (classInfo == null)
            return false;

        if (isServerSide()) {
            if (classInfo.getUnspentTalentPoints() > 0) {
                boolean didSpend = classInfo.spendTalentPoint(player, talentTree, line, index);
                if (didSpend) {
                    updateTalents();
                }
                return didSpend;
            }
        } else {
            if (classInfo.canIncrementPointInTree(talentTree, line, index)) {
                MKUltra.packetHandler.sendToServer(new AddRemoveTalentPointPacket(talentTree, line, index, AddRemoveTalentPointPacket.Mode.SPEND));
                return true;
            }
        }

        return false;
    }

    public boolean refundTalentPoint(ResourceLocation talentTree, String line, int index) {
        PlayerClassInfo classInfo = getActiveClass();
        if (classInfo == null)
            return false;

        if (isServerSide()) {
            boolean didSpend = classInfo.refundTalentPoint(player, talentTree, line, index);
            if (didSpend) {
                updateTalents();
            }
            return didSpend;
        } else {
            if (classInfo.canDecrementPointInTree(talentTree, line, index)) {
                MKUltra.packetHandler.sendToServer(new AddRemoveTalentPointPacket(talentTree, line, index, AddRemoveTalentPointPacket.Mode.REFUND));
                return true;
            }
        }
        return false;
    }

    public void gainTalentPoint() {
        PlayerClassInfo classInfo = getActiveClass();
        if (classInfo == null || classInfo.isAtTalentPointLimit())
            return;

        if (player.experienceLevel >= classInfo.getTotalTalentPoints()) {
            player.addExperienceLevel(-classInfo.getTotalTalentPoints());
            classInfo.addTalentPoints(1);
        }
    }

    @Override
    public int getTotalTalentPoints() {
        PlayerClassInfo classInfo = getActiveClass();
        return classInfo != null ? classInfo.getTotalTalentPoints() : 0;
    }

    private boolean checkTalentTotals() {
        PlayerClassInfo classInfo = getActiveClass();
        return classInfo != null && classInfo.checkTalentTotals();
    }

    @Override
    public int getUnspentTalentPoints() {
        PlayerClassInfo classInfo = getActiveClass();
        return classInfo != null ? classInfo.getUnspentTalentPoints() : 0;
    }

    @Override
    public TalentTreeRecord getTalentTree(ResourceLocation loc) {
        PlayerClassInfo classInfo = getActiveClass();
        if (classInfo == null) {
            return null;
        }
        return classInfo.getTalentTree(loc);
    }

    @Override
    @Nullable
    public List<ResourceLocation> getActivePassives() {
        PlayerClassInfo activeClass = getActiveClass();
        return activeClass != null ? activeClass.getActivePassives() : null;
    }

    @Nullable
    @Override
    public List<ResourceLocation> getActiveUltimates() {
        PlayerClassInfo activeClass = getActiveClass();
        return activeClass != null ? activeClass.getActiveUltimates() : null;
    }

    @Override
    @Nullable
    public Set<PlayerPassiveAbility> getLearnedPassives() {
        PlayerClassInfo activeClass = getActiveClass();
        return activeClass != null ? activeClass.getPassiveAbilitiesFromTalents() : null;
    }

    @Nullable
    @Override
    public Set<PlayerAbility> getLearnedUltimates() {
        PlayerClassInfo activeClass = getActiveClass();
        return activeClass != null ? activeClass.getUltimateAbilitiesFromTalents() : null;
    }

    public boolean activatePassive(ResourceLocation loc, int slotIndex) {
        PlayerClassInfo activeClass = getActiveClass();
        if (activeClass == null)
            return false;

        if (isServerSide()) {
            boolean didWork = activeClass.addPassiveToSlot(loc, slotIndex);
            setRefreshPassiveTalents();
            return didWork;
        } else {
            if (activeClass.canAddPassiveToSlot(loc, slotIndex)) {
                MKUltra.packetHandler.sendToServer(new ActivatePassivePacket(loc, slotIndex));
                return true;
            }
        }
        return false;
    }

    public void clearPassive(ResourceLocation abilityId) {
        PlayerClassInfo classInfo = getActiveClass();
        if (classInfo == null)
            return;

        int slot = classInfo.getPassiveSlot(abilityId);
        if (slot != GameConstants.PASSIVE_INVALID_SLOT) {
            classInfo.clearPassiveSlot(slot);
        }
    }

    public boolean activateUltimate(ResourceLocation loc, int slotIndex) {
        PlayerClassInfo activeClass = getActiveClass();
        if (activeClass == null)
            return false;

        if (isServerSide()) {
            ResourceLocation currentAbility = activeClass.getUltimateForSlot(slotIndex);
            if (loc.equals(MKURegistry.INVALID_ABILITY) && !currentAbility.equals(MKURegistry.INVALID_ABILITY)) {
                unlearnAbility(currentAbility, false, true);
                activeClass.clearUltimateSlot(slotIndex);
                return true;
            } else {
                if (!currentAbility.equals(MKURegistry.INVALID_ABILITY)) {
                    activeClass.clearUltimateSlot(slotIndex);
                    unlearnAbility(currentAbility, false, true);
                }
                boolean didWork = activeClass.addUltimateToSlot(loc, slotIndex);
                if (didWork) {
                    learnAbility(loc, false);
                }
                return didWork;
            }
        } else {
            if (activeClass.canAddUltimateToSlot(loc, slotIndex)) {
                MKUltra.packetHandler.sendToServer(new ActivateUltimatePacket(loc, slotIndex));
                return true;
            }
        }
        return false;
    }

    public void clearUltimate(ResourceLocation abilityId) {
        PlayerClassInfo classInfo = getActiveClass();
        if (classInfo == null)
            return;

        int slot = classInfo.getUltimateSlot(abilityId);
        if (slot != GameConstants.ULTIMATE_INVALID_SLOT) {
            classInfo.clearUltimateSlot(slot);
            unlearnAbility(abilityId, false, true);
        }
    }

    @Override
    public boolean hasUltimates() {
        PlayerClassInfo activeClass = getActiveClass();
        if (activeClass != null) {
            return activeClass.hasUltimate();
        }
        return false;
    }

    @Override
    public int getActionBarSize() {
        ResourceLocation loc = getAbilityInSlot(GameConstants.ACTION_BAR_SIZE - 1);
        return hasUltimates() || !loc.equals(MKURegistry.INVALID_ABILITY) ?
                GameConstants.ACTION_BAR_SIZE : GameConstants.CLASS_ACTION_BAR_SIZE;
    }

    private void swapHands() {
        player.setPrimaryHand(player.getPrimaryHand().opposite());
        ItemStack mainHand = player.getHeldItemMainhand();
        player.setHeldItem(EnumHand.MAIN_HAND, player.getHeldItem(EnumHand.OFF_HAND));
        player.setHeldItem(EnumHand.OFF_HAND, mainHand);
    }

    public void performDualWieldSequence() {
        if (!isDualWielding) {
            isDualWielding = true;
            originalMainHand = player.getPrimaryHand();
        } else {
            swapHands();
        }
        ticksSinceLastSwing = 0;
    }

    public void endDualWieldSequence() {
        if (isDualWielding) {
            if (player.getPrimaryHand() != originalMainHand) {
                swapHands();
            }
            isDualWielding = false;
        }
    }

    @Override
    public boolean isDualWielding() {
        return isDualWielding;
    }

    @Override
    public void setTimer(ResourceLocation loc, int cooldown) {
        if (cooldown > 0) {
            abilityTracker.setCooldown(loc, cooldown);
        } else {
            abilityTracker.removeCooldown(loc);
        }
    }

    @Override
    public int getTimer(ResourceLocation loc) {
        return abilityTracker.getCooldownTicks(loc);
    }

    private void updateTalents() {
        removeTalents();
        if (!hasChosenClass()) {
            return;
        }
        applyTalents();
    }

    private void applyTalents() {
        PlayerClassInfo activeClass = getActiveClass();
        if (activeClass == null) {
            return;
        }
        activeClass.applyAttributesModifiersToPlayer(player);
        // Since this can be called early, don't try to apply potions before being added to the world
        if (player.isAddedToWorld()) {
            refreshPassiveTalents(activeClass);
        } else {
            setRefreshPassiveTalents();
        }
    }

    private void refreshPassiveTalents(PlayerClassInfo activeClass) {
        removeAllPassiveTalents(player);
        activeClass.applyPassives(player, this, player.getEntityWorld());
        ItemEventHandler.checkEquipment(player);
    }

    private void removeTalents() {
        removeAllAttributeTalents(player);
        removeAllPassiveTalents(player);
    }

    private void removeAllAttributeTalents(EntityPlayer player) {
        AbstractAttributeMap attributeMap = player.getAttributeMap();
        for (RangedAttributeTalent entry : MKURegistry.getAllAttributeTalents()) {
            IAttributeInstance instance = attributeMap.getAttributeInstance(entry.getAttribute());
            if (instance != null) {
                instance.removeModifier(entry.getUUID());
            }
        }
    }

    private void removeAllPassiveTalents(EntityPlayer player) {
        for (PassiveAbilityTalent talent : MKURegistry.getAllPassiveTalents()) {
            if (player.isPotionActive(talent.getAbility().getPassiveEffect())) {
                talent.getAbility().removeEffect(player, this, player.world);
            }
        }
    }

    public void setRefreshPassiveTalents() {
        needPassiveTalentRefresh = true;
    }

    public boolean getPassiveTalentsUnlocked() {
        return talentPassivesUnlocked;
    }

    void removePassiveEffect(PassiveAbilityPotionBase passiveEffect) {
        talentPassivesUnlocked = true;
        player.removePotionEffect(passiveEffect);
        talentPassivesUnlocked = false;
    }

    private void updatePlayerStats(boolean doTalents) {
        if (!hasChosenClass()) {
            setMana(0);
            setTotalManaBase(0);
            setManaRegen(0);
            setHealthRegen(0);
            setTotalHealthBase((int) SharedMonsterAttributes.MAX_HEALTH.getDefaultValue());
            if (doTalents) {
                updateTalents();
            }
        } else {
            PlayerClass playerClass = MKURegistry.getClass(getClassId());
            if (playerClass == null)
                return;

            int level = getLevel();
            int newTotalMana = playerClass.getBaseMana() + (level * playerClass.getManaPerLevel());
            int newTotalHealth = playerClass.getBaseHealth() + (level * playerClass.getHealthPerLevel());
            float newManaRegen = playerClass.getBaseManaRegen() + (level * playerClass.getManaRegenPerLevel());
            setTotalManaBase(newTotalMana);
            setTotalHealthBase(newTotalHealth);
            setManaRegen(newManaRegen);
            setHealthRegen(0);
            if (doTalents) {
                updateTalents();
                checkTalentTotals();
            }
        }
    }

    private void setTotalHealthBase(float maxHealth) {
        this.player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(maxHealth);
        setHealth(getHealth()); // Refresh the health to account for the updated maximum
    }

    @Override
    public float getTotalHealth() {
        return player.getMaxHealth();
    }

    @Override
    public void setHealth(float health) {
        health = MathHelper.clamp(health, 0, getTotalHealth());
        this.player.setHealth(health);
    }

    @Override
    public float getHealth() {
        return player.getHealth();
    }

    @Override
    public float getMeleeCritChance() {
        return (float) player.getEntityAttribute(PlayerAttributes.MELEE_CRIT).getAttributeValue();
    }

    @Override
    public float getSpellCritChance() {
        return (float) player.getEntityAttribute(PlayerAttributes.SPELL_CRIT).getAttributeValue();
    }

    @Override
    public float getSpellCritDamage() {
        return (float) player.getEntityAttribute(PlayerAttributes.SPELL_CRITICAL_DAMAGE).getAttributeValue();
    }

    @Override
    public float getMeleeCritDamage() {
        return (float) player.getEntityAttribute(PlayerAttributes.MELEE_CRITICAL_DAMAGE).getAttributeValue();
    }

    @Override
    public float getCooldownProgressSpeed() {
        return (float) player.getEntityAttribute(PlayerAttributes.COOLDOWN).getAttributeValue();
    }

    @Override
    public float getMagicDamageBonus() {
        return (float) player.getEntityAttribute(PlayerAttributes.MAGIC_ATTACK_DAMAGE).getAttributeValue();
    }

    @Override
    public float getMagicArmor() {
        return (float) player.getEntityAttribute(PlayerAttributes.MAGIC_ARMOR).getAttributeValue();
    }

    @Override
    public float getHealBonus() {
        return (float) player.getEntityAttribute(PlayerAttributes.HEAL_BONUS).getAttributeValue();
    }

    @Override
    public float getBuffDurationBonus() {
        return (float) player.getEntityAttribute(PlayerAttributes.BUFF_DURATION).getAttributeValue();
    }

    @Override
    public boolean hasChosenClass() {
        return !getClassId().equals(MKURegistry.INVALID_CLASS);
    }

    @Override
    public int getUnspentPoints() {
        PlayerClassInfo classInfo = getActiveClass();
        if (classInfo != null) {
            return classInfo.getUnspentPoints();
        }
        return 0;
    }

    private void setClassId(ResourceLocation classId) {
        activeClassId = classId;
        markDirty();
    }

    @Override
    public ResourceLocation getClassId() {
        return activeClassId;
    }

    @Override
    public int getLevel() {
        PlayerClassInfo classInfo = getActiveClass();
        return classInfo != null ? classInfo.getLevel() : 0;
    }

    @Override
    public boolean canLevelUp() {
        PlayerClassInfo classInfo = getActiveClass();
        if (classInfo == null)
            return false;

        return player.experienceLevel >= (classInfo.getLevel() + 1) &&
                classInfo.getLevel() < GameConstants.MAX_CLASS_LEVEL;
    }

    @Override
    public void levelUp() {
        PlayerClassInfo classInfo = getActiveClass();
        if (classInfo == null)
            return;

        if (canLevelUp()) {
            int newLevel = classInfo.getLevel() + 1;
            setLevel(newLevel);
            classInfo.setUnspentPoints(classInfo.getUnspentPoints() + 1);
            player.addExperienceLevel(-newLevel);
        }
    }

    private void setLevel(int level) {
        PlayerClassInfo classInfo = getActiveClass();
        if (classInfo == null)
            return;

        int currentLevel = classInfo.getLevel();
        classInfo.setLevel(level);
        updatePlayerStats(false);
        if (level != currentLevel) {
            MinecraftForge.EVENT_BUS.post(new PlayerClassEvent.LevelChanged(player, this, currentLevel, level));
        }
    }

    @Override
    public ResourceLocation getAbilityInSlot(int index) {
        PlayerClassInfo classInfo = getActiveClass();
        return classInfo != null ? classInfo.getAbilityInSlot(index) : MKURegistry.INVALID_ABILITY;
    }

    private int getFirstFreeAbilitySlot(PlayerClassInfo classInfo) {
        return classInfo.getSlotForAbility(MKURegistry.INVALID_ABILITY);
    }

    @Override
    public int getCurrentAbilityCooldown(ResourceLocation abilityId) {
        PlayerAbilityInfo abilityInfo = getAbilityInfo(abilityId);
        return abilityInfo != null ? abilityTracker.getCooldownTicks(abilityId) : GameConstants.ACTION_BAR_INVALID_COOLDOWN;
    }

    @Override
    public int getAbilityCooldown(PlayerAbility ability) {
        return PlayerFormulas.applyCooldownReduction(this, ability.getCooldownTicks(getAbilityRank(ability.getAbilityId())));
    }

    @Override
    public int getCooldownForLevel(PlayerAbility ability, int level) {
        return ability.getCooldownTicks(level);
    }

    @Override
    public int getAbilityRank(ResourceLocation abilityId) {
        PlayerAbilityInfo abilityInfo = getAbilityInfo(abilityId);
        return abilityInfo != null ? abilityInfo.getRank() : GameConstants.ABILITY_INVALID_RANK;
    }

    private int getAbilityLearnIndex() {
        return getLevel() - getUnspentPoints();
    }

    @Override
    public boolean learnAbility(ResourceLocation abilityId, boolean consumePoint) {
        // Can't learn an ability without a class
        PlayerClassInfo classInfo = getActiveClass();
        if (classInfo == null)
            return false;

        PlayerAbility ability = MKURegistry.getAbility(abilityId);
        if (ability == null) {
            return false;
        }
        PlayerAbilityInfo info = classInfo.getAbilityInfo(abilityId);
        if (info == null) {
            info = ability.createAbilityInfo();
        }

        if (consumePoint && getUnspentPoints() == 0)
            return false;

        if (!info.upgrade())
            return false;

        if (consumePoint) {
            int curUnspent = classInfo.getUnspentPoints();
            if (curUnspent > 0) {
                classInfo.setUnspentPoints(curUnspent - 1);
            } else {
                return false;
            }
            classInfo.setAbilitySpendOrder(abilityId, getAbilityLearnIndex());
        }

        if (abilityTracker.hasCooldown(abilityId)) {
            int newMaxCooldown = getAbilityCooldown(ability);
            int current = abilityTracker.getCooldownTicks(abilityId);
            setCooldown(info.getId(), Math.min(current, newMaxCooldown));
        }

        classInfo.putInfo(abilityId, info);
        updateToggleAbility(info);
        sendSingleAbilityUpdate(info);

        int slot = classInfo.getSlotForAbility(abilityId);
        if (slot == GameConstants.ACTION_BAR_INVALID_SLOT) {
            // Skill was just learned so let's try to put it on the bar
            slot = getFirstFreeAbilitySlot(classInfo);
            if (slot != GameConstants.ACTION_BAR_INVALID_SLOT) {
                classInfo.setAbilityInSlot(slot, abilityId);
            }
        }

        if (slot != GameConstants.ACTION_BAR_INVALID_SLOT) {
            updateActiveAbilitySlot(classInfo, slot);
        }

        return true;
    }

    public boolean unlearnAbility(ResourceLocation abilityId, boolean refundPoint, boolean allRanks) {
        PlayerClassInfo classInfo = getActiveClass();
        if (classInfo == null)
            return false;

        PlayerAbilityInfo info = classInfo.getAbilityInfo(abilityId);
        if (info == null || !info.isCurrentlyKnown()) {
            // We never knew it or it exists but is currently unlearned
            return false;
        }

        int ranks = 0;
        if (allRanks) {
            while (info.isCurrentlyKnown())
                if (info.downgrade())
                    ranks += 1;
        } else {
            if (info.downgrade())
                ranks += 1;
        }

        if (refundPoint) {
            int curUnspent = classInfo.getUnspentPoints();
            classInfo.setUnspentPoints(curUnspent + ranks);
        }

        updateToggleAbility(info);
        sendSingleAbilityUpdate(info);

        int slot = classInfo.getSlotForAbility(abilityId);
        if (slot != GameConstants.ACTION_BAR_INVALID_SLOT) {
            updateActiveAbilitySlot(classInfo, slot);
        }

        return true;
    }

    private void updateToggleAbility(PlayerAbilityInfo info) {
        PlayerAbility ability = info.getAbility();
        if (ability instanceof PlayerToggleAbility && player != null) {
            PlayerToggleAbility toggle = (PlayerToggleAbility) ability;

            if (info.isCurrentlyKnown()) {
                // If this is a toggle ability we must re-apply the effect to make sure it's working at the proper rank
                if (player.isPotionActive(toggle.getToggleEffect())) {
                    toggle.removeEffect(player, this, player.getEntityWorld());
                    toggle.applyEffect(player, this, player.getEntityWorld());
                }
            } else {
                // Unlearning, remove the effect
                toggle.removeEffect(player, this, player.getEntityWorld());
            }
        }
    }

    public void clearToggleGroupAbility(ResourceLocation groupId) {
        activeToggleMap.remove(groupId);
    }

    public void setToggleGroupAbility(ResourceLocation groupId, PlayerToggleAbility ability) {
        PlayerToggleAbility current = activeToggleMap.get(ability.getToggleGroupId());
        // This can also be called when rebuilding the activeToggleMap after transferring dimensions and in that case
        // ability will be the same as current
        if (current != null && current != ability) {
            current.removeEffect(player, this, player.getEntityWorld());
            setCooldown(current.getAbilityId(), getAbilityCooldown(current));
        }
        activeToggleMap.put(groupId, ability);
    }

    @Override
    public boolean isCasting() {
        return currentCast != null;
    }

    @Override
    public int getCastTicks() {
        return currentCast != null ? currentCast.getCastTicks() : 0;
    }

    @Override
    public ResourceLocation getCastingAbility() {
        return currentCast != null ? currentCast.getAbilityId() : MKURegistry.INVALID_ABILITY;
    }

    private void clearCastingAbility() {
        currentCast = null;
    }

    static abstract class PlayerCastingState {
        boolean started = false;
        int castTicks;
        PlayerAbility ability;
        PlayerData playerData;

        public PlayerCastingState(PlayerData playerData, PlayerAbility ability, int castTicks) {
            this.playerData = playerData;
            this.ability = ability;
            this.castTicks = castTicks;
        }

        public int getCastTicks() {
            return castTicks;
        }

        public ResourceLocation getAbilityId() {
            return ability.getAbilityId();
        }

        public boolean tick() {
            if (castTicks <= 0)
                return false;

            if (!started) {
                begin();
                started = true;
            }

            activeTick();
            castTicks--;
            boolean active = castTicks > 0;
            if (!active) {
                finish();
            }
            return active;
        }

        void begin() {

        }

        abstract void activeTick();

        abstract void finish();
    }

    static class ServerCastingState extends PlayerCastingState {
        PlayerAbilityInfo info;
        CastState abilityCastState;

        public ServerCastingState(PlayerData playerData, PlayerAbilityInfo ability, int castTicks) {
            super(playerData, ability.getAbility(), castTicks);
            this.info = ability;
            abilityCastState = ability.getAbility().createCastState(castTicks);
        }

        public CastState getAbilityCastState() {
            return abilityCastState;
        }

        @Override
        void activeTick() {
            ability.continueCast(playerData.player, playerData, playerData.player.getEntityWorld(), castTicks, abilityCastState);
        }

        @Override
        void finish() {
            ability.endCast(playerData.player, playerData, playerData.player.getEntityWorld(), abilityCastState);
            playerData.completeAbility(ability, info);
        }
    }

    static class ClientCastingState extends PlayerCastingState {
        MovingSoundCasting sound;
        boolean playing = false;

        public ClientCastingState(PlayerData player, PlayerAbility ability, int castTicks) {
            super(player, ability, castTicks);
        }

        @Override
        void begin() {
            SoundEvent event = ability.getCastingSoundEvent();
            if (event != null) {
                sound = new MovingSoundCasting(playerData.player, event, SoundCategory.PLAYERS, castTicks);
                Minecraft.getMinecraft().getSoundHandler().playSound(sound);
                playing = true;
            }
        }

        @Override
        void activeTick() {
            ability.continueCastClient(playerData.player, playerData, playerData.player.getEntityWorld(), castTicks);
        }

        @Override
        void finish() {
            if (playing && sound != null) {
                Minecraft.getMinecraft().getSoundHandler().stopSound(sound);
                playing = false;
            }
        }
    }


    private CastState startCast(PlayerAbilityInfo abilityInfo, int castTime) {
        ServerCastingState serverCastingState = new ServerCastingState(this, abilityInfo, castTime);
        currentCast = serverCastingState;
        if (isServerSide()) {
            IMessage packet = new PlayerStartCastPacket(abilityInfo.getId(), castTime);
            MKUltra.packetHandler.sendToAllTrackingAndSelf(packet, (EntityPlayerMP) player);
        }

        return serverCastingState.getAbilityCastState();
    }

    @SideOnly(Side.CLIENT)
    public void startCastClient(ResourceLocation abilityId, int castTicks) {
        PlayerAbility ability = MKURegistry.getAbility(abilityId);
        if (ability != null) {
            currentCast = new ClientCastingState(this, ability, castTicks);
        } else {
            clearCastingAbility();
        }
    }

    void updateCurrentCast() {
        if (!isCasting())
            return;

        if (!currentCast.tick()) {
            clearCastingAbility();
        }
    }

    @Override
    public boolean executeHotBarAbility(int slotIndex) {
        ResourceLocation abilityId = getAbilityInSlot(slotIndex);
        if (abilityId.equals(MKURegistry.INVALID_ABILITY))
            return false;

        PlayerAbilityInfo info = getAbilityInfo(abilityId);
        if (info == null || !info.isCurrentlyKnown())
            return false;

        if (getCurrentAbilityCooldown(abilityId) == 0) {

            PlayerAbility ability = info.getAbility();
            if (ability != null &&
                    ability.meetsRequirements(this) &&
                    !MinecraftForge.EVENT_BUS.post(new PlayerAbilityEvent.StartCasting(player, this, info))) {
                ability.execute(player, this, player.getEntityWorld());
                return true;
            }
        }

        return false;
    }


    private void completeAbility(PlayerAbility ability, PlayerAbilityInfo info) {
        int cooldown = ability.getCooldownTicks(info.getRank());
        cooldown = PlayerFormulas.applyCooldownReduction(this, cooldown);
        setCooldown(info.getId(), cooldown);
        SoundEvent sound = ability.getSpellCompleteSoundEvent();
        if (sound != null) {
            AbilityUtils.playSoundAtServerEntity(player, sound, SoundCategory.PLAYERS);
        }
        clearCastingAbility();
        MinecraftForge.EVENT_BUS.post(new PlayerAbilityEvent.Completed(player, this, info));
    }

    @Nullable
    @Override
    public CastState startAbility(PlayerAbility ability) {
        PlayerAbilityInfo info = getAbilityInfo(ability.getAbilityId());
        if (info == null || !info.isCurrentlyKnown() || isCasting())
            return null;

        float manaCost = getAbilityManaCost(ability.getAbilityId());
        setMana(getMana() - manaCost);

        int castTime = ability.getCastTime(info.getRank());
        if (castTime > 0) {
            return startCast(info, castTime);
        } else {
            completeAbility(ability, info);
        }
        return null;
    }


    @Override
    @Nullable
    public PlayerAbilityInfo getAbilityInfo(ResourceLocation abilityId) {
        PlayerClassInfo info = getActiveClass();
        return info != null ? info.getAbilityInfo(abilityId) : null;
    }

    private void updateActiveAbilitySlot(PlayerClassInfo classInfo, int index) {
        // This is mostly to get the abilityTracker to send an AbilityCooldown packet to the client
        // so the AbilityBar can shade based on cooldowns
        // FIXME: make this less awkward
        ResourceLocation abilityId = classInfo.getAbilityInSlot(index);
        if (abilityTracker.hasCooldown(abilityId)) {
            int cd = abilityTracker.getCooldownTicks(abilityId);
            setCooldown(abilityId, cd);
        }
    }

    private void updateActiveAbilities() {
        PlayerClassInfo classInfo = getActiveClass();
        if (classInfo == null)
            return;

        for (int i = 0; i < GameConstants.ACTION_BAR_SIZE; i++) {
            updateActiveAbilitySlot(classInfo, i);
        }
    }

    private void setManaRegen(float manaRegenRate) {
        player.getEntityAttribute(PlayerAttributes.MANA_REGEN).setBaseValue(manaRegenRate);
    }

    private void setHealthRegen(float healthRegenRate) {
        player.getEntityAttribute(PlayerAttributes.HEALTH_REGEN).setBaseValue(healthRegenRate);
    }

    @Override
    public float getHealthRegenRate() {
        return (float) player.getEntityAttribute(PlayerAttributes.HEALTH_REGEN).getAttributeValue();
    }

    @Override
    public float getManaRegenRate() {
        return (float) player.getEntityAttribute(PlayerAttributes.MANA_REGEN).getAttributeValue();
    }

    private void setTotalManaBase(float totalMana) {
        player.getEntityAttribute(PlayerAttributes.MAX_MANA).setBaseValue(totalMana);
        setMana(getMana()); // Refresh the mana to account for the updated maximum
    }

    @Override
    public float getTotalMana() {
        return (float) player.getEntityAttribute(PlayerAttributes.MAX_MANA).getAttributeValue();
    }

    @Override
    public void setMana(float mana) {
        this.mana = MathHelper.clamp(mana, 0, getTotalMana());
        markDirty();
    }

    @Override
    public float getMana() {
        return mana;
    }

    private void updateMana() {
        if (getMana() > getTotalMana())
            setMana(getTotalMana());

        if (this.getManaRegenRate() == 0.0f) {
            return;
        }
        regenTime += 1. / 20.;
        float i_regen = 3.0f / this.getManaRegenRate();
        if (regenTime >= i_regen) {
            if (this.getMana() < this.getTotalMana()) {
                addMana(1);
            }
            regenTime -= i_regen;
        }
    }

    private void updateHealth() {
        if (this.getHealthRegenRate() == 0.0f) {
            return;
        }
        healthRegenTime += 1. / 20.;
        float i_regen = 3.0f / this.getHealthRegenRate();
        if (healthRegenTime >= i_regen) {
            if (this.getHealth() > 0 && this.getHealth() < this.getTotalHealth()) {
                this.setHealth(this.getHealth() + 1);
            }
            healthRegenTime -= i_regen;
        }
    }

    private boolean isServerSide() {
        return this.player instanceof EntityPlayerMP;
    }

    public void forceUpdate() {
        markDirty();
        sendBulkAbilityUpdate();
        sendBulkClassUpdate();
        updateActiveAbilities();
    }

    public void onJoinWorld() {
        Log.trace("PlayerData@onJoinWorld\n");

        if (isServerSide()) {
            checkPassiveEffects();
            rebuildActiveToggleMap();
            updatePlayerStats(true);
        } else {
            Log.trace("PlayerData@onJoinWorld - Client sending sync req\n");
            MKUltra.packetHandler.sendToServer(new PlayerSyncRequestPacket());
        }

    }


    private void updateDualWielding() {
        if (isDualWielding) {
            if (ticksSinceLastSwing > DUAL_WIELD_TIMEOUT) {
                endDualWieldSequence();
            } else {
                ticksSinceLastSwing++;
            }
        }
    }

    public void onTick() {
        abilityTracker.tick();
        updateCurrentCast();
        if (!isServerSide()) {
            return;
        }
        if (needPassiveTalentRefresh) {
            PlayerClassInfo info = getActiveClass();
            if (info != null) {
                refreshPassiveTalents(info);
            }
            needPassiveTalentRefresh = false;
        }
        updateMana();
        updateHealth();
        updateDualWielding();
        syncState();
    }

    private void syncState() {
        PlayerClassInfo activeClass = getActiveClass();
        if (activeClass != null) {
            IMessage message = activeClass.getUpdateMessage();
            if (message != null) {
                MinecraftForge.EVENT_BUS.post(new PlayerClassEvent.Updated(player, this, activeClass.getClassId()));
                MKUltra.packetHandler.sendTo(message, (EntityPlayerMP) player);
            }
        }

        IMessage updateMessage = getUpdateMessage();
        if (updateMessage != null) {
            MKUltra.packetHandler.sendToAllTrackingAndSelf(updateMessage, (EntityPlayerMP) player);
        }
    }

    private void sendSingleAbilityUpdate(PlayerAbilityInfo info) {
        if (isServerSide()) {
            MKUltra.packetHandler.sendTo(new AbilityUpdatePacket(info), (EntityPlayerMP) player);
        }
    }

    private void sendBulkAbilityUpdate() {
        if (isServerSide()) {
            PlayerClassInfo classInfo = getActiveClass();
            if (classInfo != null) {
                MKUltra.packetHandler.sendTo(new AbilityUpdatePacket(classInfo.getAbilityInfos()),
                        (EntityPlayerMP) player);
            }
        }
    }

    private void sendBulkClassUpdate() {
        if (isServerSide()) {
            MKUltra.packetHandler.sendTo(new ClassUpdatePacket(knownClasses.values()), (EntityPlayerMP) player);
        }
    }

    @SideOnly(Side.CLIENT)
    public void clientAbilityUpdate(PlayerAbilityInfo info) {
        PlayerClassInfo classInfo = getActiveClass();
        if (classInfo != null) {
            classInfo.putInfo(info.getId(), info);
        }
    }

    @SideOnly(Side.CLIENT)
    public void clientBulkKnownClassUpdate(Collection<PlayerClassInfo> info, boolean isFullUpdate) {
        if (isFullUpdate) {
            knownClasses.clear();
        }
        info.forEach(classInfo -> {
            knownClasses.put(classInfo.getClassId(), classInfo);
            MinecraftForge.EVENT_BUS.post(new PlayerClassEvent.Updated(player, this, classInfo.getClassId()));
        });
    }


    private void serializeClasses(NBTTagCompound tag) {
        NBTTagList classes = new NBTTagList();
        for (PlayerClassInfo info : knownClasses.values()) {
            NBTTagCompound sk = new NBTTagCompound();
            info.serialize(sk);
            classes.appendTag(sk);
        }
        tag.setTag("classes", classes);
    }

    private void deserializeClasses(NBTTagCompound tag) {
        if (tag.hasKey("classes")) {
            NBTTagList classes = tag.getTagList("classes", Constants.NBT.TAG_COMPOUND);
            knownClasses = new HashMap<>(classes.tagCount());

            for (int i = 0; i < classes.tagCount(); i++) {
                NBTTagCompound cls = classes.getCompoundTagAt(i);
                ResourceLocation classId = new ResourceLocation(cls.getString("id"));
                PlayerClass playerClass = MKURegistry.getClass(classId);
                if (playerClass != null) {
                    PlayerClassInfo info = playerClass.createClassInfo();
                    info.deserialize(cls);
                    knownClasses.put(info.getClassId(), info);
                }
            }
        }
    }

    @Override
    public void serialize(NBTTagCompound nbt) {
        serializeUpdate(nbt);
        serializeClasses(nbt);
        abilityTracker.serialize(nbt);
    }

    @Override
    public void deserialize(NBTTagCompound nbt) {
        abilityTracker.deserialize(nbt);
        deserializeClasses(nbt);
        deserializeUpdate(nbt);
    }

    private void serializeUpdate(NBTTagCompound tag) {
        tag.setString("activeClassId", getClassId().toString());
        tag.setFloat("mana", getMana());
    }

    private void deserializeUpdate(NBTTagCompound tag) {
        if (tag.hasKey("activeClassId", Constants.NBT.TAG_STRING)) {
            ResourceLocation classId = new ResourceLocation(tag.getString("activeClassId"));
            // If the character was saved with a class that doesn't exist anymore (say from a plugin),
            // reset the character to have no class
            if (MKURegistry.getClass(classId) == null)
                classId = MKURegistry.INVALID_CLASS;

            activateClass(classId);
            sendBulkAbilityUpdate();
        } else {
            activateClass(MKURegistry.INVALID_CLASS);
        }
        if (tag.hasKey("mana")) {
            setMana(tag.getFloat("mana"));
        }
    }

    public void serializeClientUpdate(NBTTagCompound tag) {
        tag.setString("activeClassId", getClassId().toString());
        tag.setFloat("mana", getMana());
    }

    @SideOnly(Side.CLIENT)
    public void deserializeClientUpdate(NBTTagCompound tag) {
        if (tag.hasKey("activeClassId")) {
            activeClassId = new ResourceLocation(tag.getString("activeClassId"));
        }
        if (tag.hasKey("mana")) {
            mana = tag.getFloat("mana");
        }
    }

    public void clone(EntityPlayer previous) {
        PlayerData prevData = (PlayerData) MKUPlayerData.get(previous);
        if (prevData == null)
            return;

        NBTTagCompound tag = new NBTTagCompound();
        prevData.serialize(tag);
        deserialize(tag);
        updateActiveAbilities();
    }

    private void validateAbilityPoints() {
        PlayerClassInfo classInfo = getActiveClass();
        if (classInfo == null)
            return;

        PlayerClass playerClass = MKURegistry.getClass(getClassId());
        if (playerClass == null)
            return;

        int totalPoints = classInfo.getUnspentPoints();
        for (int i = 0; i < GameConstants.CLASS_ACTION_BAR_SIZE; i++) {
            PlayerAbility ability = playerClass.getOfferedAbilityBySlot(i);
            if (ability == null)
                continue;

            PlayerAbilityInfo info = classInfo.getAbilityInfo(ability.getAbilityId());
            if (info == null || !info.isCurrentlyKnown())
                continue;

            totalPoints += info.getRank();
        }

        Log.info("validateAbilityPoints: %s expected %d calculated %d", player.getName(), classInfo.getLevel(), totalPoints);
        if (totalPoints != classInfo.getLevel()) {
            resetAbilities(false);
            player.sendMessage(new TextComponentString("Your abilities have been reset and points refunded. Sorry for the inconvenience"));
        }
    }

    public void doDeath() {
        PlayerClassInfo classInfo = getActiveClass();
        if (classInfo == null)
            return;

        int oldLevel = classInfo.getLevel();

        if (oldLevel > 1) {
            int curUnspent = classInfo.getUnspentPoints();
            if (curUnspent > 0) {
                classInfo.setUnspentPoints(curUnspent - 1);
            } else {
                ResourceLocation lastAbility = classInfo.getAbilitySpendOrder(getAbilityLearnIndex());
                if (!lastAbility.equals(MKURegistry.INVALID_ABILITY)) {
                    unlearnAbility(lastAbility, false, false);
                }
            }

            // Check to see if de-leveling will make us lower than the required level for some spells.
            // If so, unlearn the spell and refund the point.
            int newLevel = oldLevel - 1;
            classInfo.getActiveAbilities().stream()
                    .map(MKURegistry::getAbility)
                    .filter(Objects::nonNull)
                    .filter(ability -> ability.getType() == PlayerAbility.AbilityType.Active)
                    .filter(ability -> {
                        int currentRank = getAbilityRank(ability.getAbilityId());
                        // Subtract 1 because getRequiredLevel is a little weird. It actually tells you the required
                        // level to go up a rank, not the required level for the current rank
                        int newRank = currentRank - 1;
                        int reqLevel = ability.getRequiredLevel(newRank);
                        reqLevel = Math.max(1, reqLevel);
                        return reqLevel > newLevel;
                    })
                    .forEach(a -> unlearnAbility(a.getAbilityId(), true, false));

            setLevel(newLevel);
            validateAbilityPoints();
        }
    }

    @Override
    public boolean learnClass(IClassProvider provider, ResourceLocation classId) {
        if (knowsClass(classId)) {
            // Class was already known
            return true;
        }

        PlayerClass playerClass = MKURegistry.getClass(classId);
        if (playerClass == null)
            return false;

        if (!provider.teachesClass(playerClass))
            return false;

        PlayerClassInfo info = playerClass.createClassInfo();
        knownClasses.put(classId, info);
        sendBulkClassUpdate();
        MinecraftForge.EVENT_BUS.post(new PlayerClassEvent.Learned(player, this));

        // Learned class
        return true;
    }

    public void unlearnClass(ResourceLocation classId) {
        if (!knowsClass(classId)) {
            return;
        }

        // If it's the active class, switch to no class first
        if (getClassId().equals(classId))
            activateClass(MKURegistry.INVALID_CLASS);

        PlayerClassInfo info = knownClasses.remove(classId);

        // Unlearn all abilities offered by this class
        PlayerClass bc = MKURegistry.getClass(classId);
        if (bc != null) {
            bc.getAbilities().forEach(a -> unlearnAbility(a.getAbilityId(), false, true));
        }

        sendBulkClassUpdate();
        MinecraftForge.EVENT_BUS.post(new PlayerClassEvent.Removed(player, this));
    }

    private void deactivateCurrentToggleAbilities() {
        for (int i = 0; i < GameConstants.ACTION_BAR_SIZE; i++) {
            ResourceLocation abilityId = getAbilityInSlot(i);
            PlayerAbility ability = MKURegistry.getAbility(abilityId);
            if (ability instanceof PlayerToggleAbility && player != null) {
                PlayerToggleAbility toggle = (PlayerToggleAbility) ability;
                toggle.removeEffect(player, this, player.getEntityWorld());
            }
        }
    }

    private void rebuildActiveToggleMap() {
        for (int i = 0; i < GameConstants.ACTION_BAR_SIZE; i++) {
            ResourceLocation abilityId = getAbilityInSlot(i);
            PlayerAbility ability = MKURegistry.getAbility(abilityId);
            if (ability instanceof PlayerToggleAbility && player != null) {
                PlayerToggleAbility toggle = (PlayerToggleAbility) ability;
                if (player.isPotionActive(toggle.getToggleEffect()))
                    setToggleGroupAbility(toggle.getToggleGroupId(), toggle);
            }
        }
    }

    private void checkPassiveEffects() {
        player.getActivePotionMap().forEach((p, e) -> {
            if (p instanceof SpellPotionBase) {
                SpellPotionBase sp = (SpellPotionBase) p;
                if (!sp.canPersistAcrossSessions()) {
                    SpellCast cast = sp.createReapplicationCast(player);
                    if (cast != null) {
                        // Force PotionEffect combination so it will call add/remove of potion attributes
                        player.addPotionEffect(cast.toPotionEffect(e.getDuration(), e.getAmplifier()));
                    }
                }
            }
        });
    }

    @Override
    public void activateClass(ResourceLocation classId) {
        ResourceLocation oldClassId = getClassId();
        if (oldClassId == classId)
            return;

        deactivateCurrentToggleAbilities();

        if (classId.equals(MKURegistry.INVALID_CLASS) || !knowsClass(classId)) {
            // Switching to no class
            classId = MKURegistry.INVALID_CLASS;
        }

        setClassId(classId);
        updatePlayerStats(true);
        updateActiveAbilities();
        checkTalentTotals();
        validateAbilityPoints();
        ItemEventHandler.checkEquipment(player);

        if (!classId.equals(oldClassId)) {
            MinecraftForge.EVENT_BUS.post(new PlayerClassEvent.ClassChanged(player, this, oldClassId, classId));
        }
    }

    @Override
    public Collection<ResourceLocation> getKnownClasses() {
        return Collections.unmodifiableSet(knownClasses.keySet());
    }

    @Override
    public ArmorClass getArmorClass() {
        PlayerClass currentClass = MKURegistry.getClass(getClassId());
        if (currentClass == null)
            return ArmorClass.ALL;
        ArmorClass ac = currentClass.getArmorClass();
        return player.isPotionActive(ArmorTrainingPotion.INSTANCE) ? ac.getSuccessor() : ac;
    }

    @Override
    public boolean canWearArmor(ItemArmor item) {
        ArmorClass effective = getArmorClass();
        // If no class, default to vanilla behaviour of wearing anything
        // Then check the current class if it's allowed
        // Then check for special exceptions granted by other means
        return effective == null || effective.canWear(item);
    }

    @Override
    public boolean setCooldown(ResourceLocation abilityId, int cooldownTicks) {
        PlayerAbilityInfo info = getAbilityInfo(abilityId);
        if (info == null)
            return false;

        if (cooldownTicks > 0) {
            abilityTracker.setCooldown(info.getId(), cooldownTicks);
        } else {
            abilityTracker.removeCooldown(info.getId());
        }
        return true;
    }

    @Override
    public void addToAllCooldowns(int cooldownTicks) {
        abilityTracker.iterateActive((loc, ticks) -> abilityTracker.setCooldown(loc, ticks + cooldownTicks));
    }

    @Override
    public float getCooldownPercent(PlayerAbilityInfo abilityInfo, float partialTicks) {
        return abilityInfo != null ? abilityTracker.getCooldown(abilityInfo.getId(), partialTicks) : 0.0f;
    }

    @Override
    public float getAbilityManaCost(ResourceLocation abilityId) {
        PlayerAbilityInfo abilityInfo = getAbilityInfo(abilityId);
        if (abilityInfo == null) {
            return 0.0f;
        }
        return PlayerFormulas.applyManaCostReduction(this, abilityInfo.getAbility().getManaCost(abilityInfo.getRank()));
    }

    public void debugResetAllCooldowns() {
        abilityTracker.removeAll();
        updateActiveAbilities();
    }

    public void debugDumpAllAbilities(ICommandSender sender) {
        String msg = "All active cooldowns:";
        sender.sendMessage(new TextComponentString(msg));
        abilityTracker.iterateActive((abilityId, current) -> {
            String name;
            int max;
            PlayerAbility ability = MKURegistry.getAbility(abilityId);
            if (ability != null) {
                name = ability.getTranslationKey();
                max = getAbilityCooldown(ability);
            } else {
                name = abilityId.toString();
                max = abilityTracker.getMaxCooldownTicks(abilityId);
            }
            ITextComponent line = new TextComponentTranslation(name).appendText(String.format(": %d / %d", current, max));
            sender.sendMessage(line);
        });
    }

    public void setInSpellTriggerCallback(String tag, boolean enable) {
        if (enable) {
            activeSpellTriggers.add(tag);
        } else {
            activeSpellTriggers.remove(tag);
        }
    }

    public boolean isInSpellTriggerCallback(String tag) {
        return activeSpellTriggers.contains(tag);
    }

    public boolean resetAbilities(boolean includeTalents) {
        PlayerClassInfo classInfo = getActiveClass();
        if (classInfo == null)
            return false;

        PlayerClass playerClass = MKURegistry.getClass(classInfo.getClassId());
        if (playerClass == null)
            return false;

        for (int i = 0; i < GameConstants.CLASS_ACTION_BAR_SIZE; i++) {
            PlayerAbility ability = playerClass.getOfferedAbilityBySlot(i);
            if (ability == null)
                continue;
            unlearnAbility(ability.getAbilityId(), false, true);
        }

        classInfo.clearAbilitySpendOrder();
        classInfo.setUnspentPoints(classInfo.getLevel());

        return true;
    }
}
