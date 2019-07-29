package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.event.ItemRestrictionHandler;
import com.chaosbuffalo.mkultra.item.ItemHelper;
import com.chaosbuffalo.mkultra.item.ManaRegenIdol;
import com.chaosbuffalo.mkultra.item.interfaces.IClassProvider;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.network.packets.PlayerSyncRequestPacket;
import com.chaosbuffalo.mkultra.network.packets.AbilityUpdatePacket;
import com.chaosbuffalo.mkultra.network.packets.ClassUpdatePacket;
import com.google.common.collect.Lists;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

public class PlayerData implements IPlayerData {

    private final static DataParameter<Integer> MANA = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.VARINT);
    private final static DataParameter<Integer> LEVEL = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.VARINT);
    private final static DataParameter<Integer> UNSPENT_POINTS = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.VARINT);
    private final static DataParameter<String> CLASS_ID = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.STRING);
    private final static DataParameter<String>[] ACTION_BAR_ABILITY_ID;
    private final static DataParameter<Integer>[] ACTION_BAR_ABILITY_RANK;

    static {
        ACTION_BAR_ABILITY_ID = new DataParameter[GameConstants.ACTION_BAR_SIZE];
        for (int i = 0; i < GameConstants.ACTION_BAR_SIZE; i++) {
            ACTION_BAR_ABILITY_ID[i] = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.STRING);
        }
        ACTION_BAR_ABILITY_RANK = new DataParameter[GameConstants.ACTION_BAR_SIZE];
        for (int i = 0; i < GameConstants.ACTION_BAR_SIZE; i++) {
            ACTION_BAR_ABILITY_RANK[i] = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.VARINT);
        }
    }


    private final EntityPlayer player;
    private final EntityDataManager privateData;
    private float regenTime;
    private float healthRegenTime;
    private AbilityTracker abilityTracker;
    private Map<ResourceLocation, PlayerClassInfo> knownClasses = new HashMap<>();
    private Map<ResourceLocation, PlayerAbilityInfo> abilityInfoMap = new HashMap<>(5);
    private Set<ItemArmor.ArmorMaterial> alwaysAllowedArmorMaterials = new HashSet<>();

    public PlayerData(EntityPlayer player) {
        this.player = player;
        regenTime = 0;
        healthRegenTime = 0;
        abilityTracker = AbilityTracker.getTracker(player);
        privateData = player.getDataManager();
        setupWatcher();

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
    }

    private void setupWatcher() {

        privateData.register(MANA, 0);
        privateData.register(UNSPENT_POINTS, 0);
        privateData.register(CLASS_ID, MKURegistry.INVALID_CLASS.toString());
        privateData.register(LEVEL, 0);
        for (int i = 0; i < GameConstants.ACTION_BAR_SIZE; i++) {
            privateData.register(ACTION_BAR_ABILITY_ID[i], MKURegistry.INVALID_ABILITY.toString());
            privateData.register(ACTION_BAR_ABILITY_RANK[i], GameConstants.ABILITY_INVALID_RANK);
        }
    }

    private void markEntityDataDirty() {
        privateData.setDirty(MANA);
        privateData.setDirty(UNSPENT_POINTS);
        privateData.setDirty(CLASS_ID);
        privateData.setDirty(LEVEL);
        for (int i = 0; i < GameConstants.ACTION_BAR_SIZE; i++) {
            privateData.setDirty(ACTION_BAR_ABILITY_ID[i]);
            privateData.setDirty(ACTION_BAR_ABILITY_RANK[i]);
        }
    }

    private PlayerClassInfo getActiveClass() {
        return knownClasses.get(getClassId());
    }

    private ResourceLocation getLastUpgradedAbility() {
        PlayerClassInfo cinfo = getActiveClass();
        if (cinfo != null) {
            return cinfo.getLastUpgradedAbility();
        }
        return MKURegistry.INVALID_ABILITY;
    }

    private void updatePlayerStats() {
        if (!hasChosenClass()) {
            setMana(0);
            setTotalMana(0);
            setManaRegen(0);
            setHealthRegen(0);
            setHealth(Math.min(20, this.player.getHealth()));
            setTotalHealth(20);
        } else {
            PlayerClass playerClass = MKURegistry.getClass(getClassId());
            if (playerClass == null)
                return;

            int level = getLevel();
            int newTotalMana = playerClass.getBaseMana() + (level * playerClass.getManaPerLevel());
            int newTotalHealth = playerClass.getBaseHealth() + (level * playerClass.getHealthPerLevel());
            float newManaRegen = playerClass.getBaseManaRegen() + (level * playerClass.getManaRegenPerLevel());
            setTotalMana(newTotalMana);
            setMana(Math.min(newTotalMana, getMana()));
            setTotalHealth(newTotalHealth);
            setHealth(Math.min(newTotalHealth, this.player.getHealth()));
            setManaRegen(newManaRegen);
            setHealthRegen(0);
        }
    }

    @Override
    public void setTotalHealth(float maxHealth) {
        this.player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(maxHealth);
    }

    @Override
    public float getTotalHealth() {
        return player.getMaxHealth();
    }

    @Override
    public void setHealth(float health) {
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
    public void setSpellCritChance(float critChance) {
        player.getEntityAttribute(PlayerAttributes.SPELL_CRIT).setBaseValue(critChance);
    }

    @Override
    public float getSpellCritDamage() {
        return (float) player.getEntityAttribute(PlayerAttributes.SPELL_CRITICAL_DAMAGE).getAttributeValue();
    }

    @Override
    public void setSpellCritDamage(float critDamage) {
        player.getEntityAttribute(PlayerAttributes.SPELL_CRITICAL_DAMAGE).setBaseValue(critDamage);
    }

    @Override
    public void setMeleeCritChance(float critChance) {
        player.getEntityAttribute(PlayerAttributes.MELEE_CRIT).setBaseValue(critChance);
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
    public boolean hasChosenClass() {
        return this.getClassId().compareTo(MKURegistry.INVALID_CLASS) != 0;
    }

    @Override
    public int getUnspentPoints() {
        return privateData.get(UNSPENT_POINTS);
    }

    private void setUnspentPoints(int unspentPoints) {
        // You shouldn't have more unspent points than your levels
        if (unspentPoints > getLevel())
            return;
        privateData.set(UNSPENT_POINTS, unspentPoints);
    }

    private void setClassId(ResourceLocation classId) {
        privateData.set(CLASS_ID, classId.toString());
    }

    @Override
    public ResourceLocation getClassId() {
        return new ResourceLocation(privateData.get(CLASS_ID));
    }

    @Override
    public int getLevel() {
        return privateData.get(LEVEL);
    }

    @Override
    public boolean canLevelUp() {
        return (this.player.experienceLevel >= (this.getLevel() + 1)
                && this.hasChosenClass()
                && this.getLevel() < GameConstants.MAX_CLASS_LEVEL);
    }

    @Override
    public void levelUp() {
        if (canLevelUp()) {
            int newLevel = this.getLevel() + 1;
            this.setLevel(newLevel);
            this.setUnspentPoints(this.getUnspentPoints() + 1);
            this.player.addExperienceLevel(-newLevel);
        }
    }

    private void setLevel(int level) {
        privateData.set(LEVEL, level);
        updatePlayerStats();
    }

    private void setActiveAbilities(ResourceLocation[] abilities) {
        int max = Math.min(abilities.length, GameConstants.ACTION_BAR_SIZE);
        for (int i = 0; i < max; i++) {
            setAbilityInSlot(i, abilities[i]);
        }
        updateActiveAbilities();
    }

    private ResourceLocation[] getActiveAbilities() {
        ResourceLocation[] actives = new ResourceLocation[GameConstants.ACTION_BAR_SIZE];
        for (int i = 0; i < GameConstants.ACTION_BAR_SIZE; i++) {
            actives[i] = getAbilityInSlot(i);
        }
        return actives;
    }

    private void setAbilityInSlot(int slotIndex, ResourceLocation abilityId) {
        privateData.set(ACTION_BAR_ABILITY_ID[slotIndex], abilityId.toString());
    }

    private int getCurrentSlotForAbility(ResourceLocation abilityId) {
        for (int i = 0; i < GameConstants.ACTION_BAR_SIZE; i++) {
            if (getAbilityInSlot(i).compareTo(abilityId) == 0) {
                return i;
            }
        }
        return GameConstants.ACTION_BAR_INVALID_SLOT;
    }

    private int getFirstFreeAbilitySlot() {
        return getCurrentSlotForAbility(MKURegistry.INVALID_ABILITY);
    }

    @Override
    public ResourceLocation getAbilityInSlot(int index) {
        if (index < ACTION_BAR_ABILITY_ID.length) {
            return new ResourceLocation(privateData.get(ACTION_BAR_ABILITY_ID[index]));
        }
        return MKURegistry.INVALID_ABILITY;
    }

    @Override
    public int getCurrentAbilityCooldown(ResourceLocation abilityId) {
        PlayerAbilityInfo abilityInfo = getAbilityInfo(abilityId);
        return abilityInfo != null ? abilityTracker.getCooldownTicks(abilityInfo) : GameConstants.ACTION_BAR_INVALID_COOLDOWN;
    }

    @Override
    public int getAbilityCooldown(PlayerAbility ability) {
        return PlayerFormulas.applyCooldownReduction(this, ability.getCooldownTicks(getAbilityRank(ability.getAbilityId())));
    }

    @Override
    public int getAbilityRank(ResourceLocation abilityId) {
        PlayerAbilityInfo abilityInfo = getAbilityInfo(abilityId);
        return abilityInfo != null ? abilityInfo.getRank() : GameConstants.ABILITY_INVALID_RANK;
    }

    @Override
    public boolean learnAbility(ResourceLocation abilityId, boolean consumePoint) {
        PlayerClassInfo classInfo = getActiveClass();

        // Can't learn an ability without a class
        if (classInfo == null) {
            return false;
        }

        PlayerAbilityInfo info = getAbilityInfo(abilityId);
        if (info == null) {
            info = new PlayerAbilityInfo(abilityId);
        }

        if (consumePoint) {
            int curUnspent = getUnspentPoints();
            if (curUnspent > 0) {
                setUnspentPoints(curUnspent - 1);
            } else {
                return false;
            }
        }

        info.upgrade();
        classInfo.addToSpendOrder(abilityId);

        if (abilityTracker.hasCooldown(info)) {
            PlayerAbility ability = MKURegistry.getAbility(abilityId);
            int newMaxCooldown = getAbilityCooldown(ability);
            int current = abilityTracker.getCooldownTicks(info);
            setCooldown(info.getId(), Math.min(current, newMaxCooldown));
        }

        abilityInfoMap.put(abilityId, info);
        updateToggleAbility(info);
        sendSingleAbilityUpdate(info);

        int slot = getCurrentSlotForAbility(abilityId);
        if (slot == GameConstants.ACTION_BAR_INVALID_SLOT) {
            // Skill was just learned so let's try to put it on the bar
            slot = getFirstFreeAbilitySlot();
            if (slot != GameConstants.ACTION_BAR_INVALID_SLOT) {
                setAbilityInSlot(slot, abilityId);
            }
        }

        if (slot != GameConstants.ACTION_BAR_INVALID_SLOT) {
            updateActiveAbilitySlot(slot);
        }

        return true;
    }

    public boolean unlearnAbility(ResourceLocation abilityId, boolean refundPoint, boolean allRanks) {
        PlayerAbilityInfo info = getAbilityInfo(abilityId);
        if (info == null || !info.isCurrentlyKnown()) {
            // We never knew it or it exists but is currently unlearned
            return false;
        }

        int ranks = 0;
        if (allRanks) {
            while (info.isCurrentlyKnown())
                if (info.downgrade())
                    ranks += 1;
        }
        else {
            if (info.isCurrentlyKnown())
                if (info.downgrade())
                    ranks += 1;
        }

        if (refundPoint) {
            int curUnspent = getUnspentPoints();
            setUnspentPoints(curUnspent + ranks);
        }

        updateToggleAbility(info);
        sendSingleAbilityUpdate(info);

        int slot = getCurrentSlotForAbility(abilityId);
        if (slot != GameConstants.ACTION_BAR_INVALID_SLOT) {
            updateActiveAbilitySlot(slot);
        }

        return true;
    }

    private void updateToggleAbility(PlayerAbilityInfo info) {
        PlayerAbility ability = MKURegistry.getAbility(info.getId());
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

    @Override
    public boolean executeHotBarAbility(int slotIndex) {
        ResourceLocation abilityId = getAbilityInSlot(slotIndex);
        if (abilityId.compareTo(MKURegistry.INVALID_ABILITY) == 0)
            return false;

        if (getCurrentAbilityCooldown(abilityId) == 0) {

            PlayerAbility ability = MKURegistry.getAbility(abilityId);
            if (ability != null && ability.meetsRequirements(this)) {
                ability.execute(player, this, player.getEntityWorld());
                return true;
            }
        }

        return false;
    }

    @Override
    public void startAbility(PlayerAbility ability) {
        PlayerAbilityInfo info = getAbilityInfo(ability.getAbilityId());
        if (info == null || !info.isCurrentlyKnown())
            return;
        ItemStack heldItem = this.player.getHeldItem(EnumHand.OFF_HAND);
        if (heldItem.getItem() instanceof ManaRegenIdol) {
            ItemHelper.damageStack(player, heldItem, 1);
        }
        ItemStack mainHandItem = this.player.getHeldItem(EnumHand.MAIN_HAND);
        if (mainHandItem.getItem() instanceof ManaRegenIdol) {
            ItemHelper.damageStack(player, mainHandItem, 1);
        }
        int manaCost = ability.getManaCost(info.getRank());
        manaCost = PlayerFormulas.applyManaCostReduction(this, manaCost);
        setMana(getMana() - manaCost);

        int cooldown = ability.getCooldownTicks(info.getRank());
        cooldown = PlayerFormulas.applyCooldownReduction(this, cooldown);
        setCooldown(info.getId(), cooldown);
    }

    private PlayerAbilityInfo getAbilityInfo(ResourceLocation abilityId) {
        return abilityInfoMap.get(abilityId);
    }

    private void updateActiveAbilitySlot(int index) {
        ResourceLocation abilityId = getAbilityInSlot(index);
        PlayerAbilityInfo abilityInfo = getAbilityInfo(abilityId);

        boolean valid = abilityInfo != null && abilityInfo.isCurrentlyKnown();
        ResourceLocation id = valid ? abilityInfo.getId() : MKURegistry.INVALID_ABILITY;
        int rank = valid ? abilityInfo.getRank() : GameConstants.ABILITY_INVALID_RANK;

        setAbilityInSlot(index, id);
        privateData.set(ACTION_BAR_ABILITY_RANK[index], rank);

        if (abilityTracker.hasCooldown(abilityInfo)) {
            int cd = abilityTracker.getCooldownTicks(abilityInfo);
            setCooldown(abilityId, cd);
        }
    }

    private void updateActiveAbilities() {
        for (int i = 0; i < GameConstants.ACTION_BAR_SIZE; i++) {
            updateActiveAbilitySlot(i);
        }
    }

    @Override
    public void setManaRegen(float manaRegenRate) {
        player.getEntityAttribute(PlayerAttributes.MANA_REGEN).setBaseValue(manaRegenRate);
    }

    @Override
    public void setHealthRegen(float healthRegenRate) {
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

    private float getBaseHealthRegenRate() {
        return (float) player.getEntityAttribute(PlayerAttributes.HEALTH_REGEN).getBaseValue();
    }

    private float getBaseManaRegenRate() {
        return (float) player.getEntityAttribute(PlayerAttributes.MANA_REGEN).getBaseValue();
    }

    @Override
    public void setTotalMana(int totalMana) {
        player.getEntityAttribute(PlayerAttributes.MAX_MANA).setBaseValue(totalMana);
    }

    @Override
    public int getTotalMana() {
        return (int) player.getEntityAttribute(PlayerAttributes.MAX_MANA).getAttributeValue();
    }

    private int getBaseTotalMana() {
        return (int) player.getEntityAttribute(PlayerAttributes.MAX_MANA).getBaseValue();
    }

    @Override
    public void setMana(int mana) {
        privateData.set(MANA, mana);
    }

    @Override
    public int getMana() {
        return privateData.get(MANA);
    }

    private void updateMana() {
        if (this.getManaRegenRate() == 0.0f){
            return;
        }
        regenTime += 1. / 20.;
        float i_regen = 3.0f / this.getManaRegenRate();
        if (regenTime >= i_regen) {
            if (this.getMana() < this.getTotalMana()) {
                this.setMana(this.getMana() + 1);
            }
            regenTime -= i_regen;
        }
    }

    private void updateHealth() {
        if (this.getHealthRegenRate() == 0.0f){
            return;
        }
        healthRegenTime += 1. / 20.;
        float i_regen = 3.0f / this.getHealthRegenRate();
        if (healthRegenTime >= i_regen) {
            if (this.getHealth() < this.getTotalHealth()) {
                this.setHealth(this.getHealth() + 1);
            }
            healthRegenTime -= i_regen;
        }
    }

    private boolean isServerSide() {
        return this.player instanceof EntityPlayerMP;
    }

    public void forceUpdate() {
        markEntityDataDirty();
        sendBulkAbilityUpdate();
        sendBulkClassUpdate();
        updateActiveAbilities();
    }

    public void onRespawn() {
    }

    public void onJoinWorld() {
        Log.trace("PlayerData@onJoinWorld\n");

        if (isServerSide()) {
            updatePlayerStats();
        } else {
            Log.trace("PlayerData@onJoinWorld - Client sending sync req\n");
            MKUltra.packetHandler.sendToServer(new PlayerSyncRequestPacket());
        }

    }

    public void onTick() {
        abilityTracker.tick();

        if (!isServerSide())
            return;

        updateMana();
        updateHealth();
    }

    private void sendSingleAbilityUpdate(PlayerAbilityInfo info) {
        if (isServerSide()) {
            MKUltra.packetHandler.sendTo(new AbilityUpdatePacket(info), (EntityPlayerMP) player);
        }
    }

    private void sendBulkAbilityUpdate() {
        if (isServerSide()) {
            MKUltra.packetHandler.sendTo(new AbilityUpdatePacket(abilityInfoMap.values()), (EntityPlayerMP) player);
        }
    }

    private void sendBulkClassUpdate() {
        if (isServerSide()) {
            MKUltra.packetHandler.sendTo(new ClassUpdatePacket(knownClasses.values()), (EntityPlayerMP) player);
        }
    }

    @SideOnly(Side.CLIENT)
    public void clientSkillListUpdate(PlayerAbilityInfo info) {
        if (!info.isCurrentlyKnown()) {
            abilityInfoMap.remove(info.getId());
        } else {
            abilityInfoMap.put(info.getId(), info);
        }
    }

    @SideOnly(Side.CLIENT)
    public void clientBulkKnownClassUpdate(List<PlayerClassInfo> info) {
        knownClasses.clear();
        info.forEach(ci -> knownClasses.put(ci.classId, ci));
    }

    private void serializeSkills(NBTTagCompound tag) {

        NBTTagList allSkills = new NBTTagList();
        for (PlayerAbilityInfo info : abilityInfoMap.values()) {
            NBTTagCompound sk = new NBTTagCompound();
            info.setCooldownTicks(abilityTracker.getCooldownTicks(info));
            info.serialize(sk);
            allSkills.appendTag(sk);
        }
        tag.setTag("allSkills", allSkills);
    }

    private void deserializeSkills(NBTTagCompound tag) {
        if (tag.hasKey("allSkills")) {
            NBTTagList skills = tag.getTagList("allSkills", Constants.NBT.TAG_COMPOUND);
            abilityInfoMap = new HashMap<>(skills.tagCount());
            for (int i = 0; i < skills.tagCount(); i++) {
                NBTTagCompound sk = skills.getCompoundTagAt(i);
                PlayerAbilityInfo info = new PlayerAbilityInfo(new ResourceLocation(sk.getString("id")));
                info.deserialize(sk);

                abilityTracker.setCooldown(info, info.getCooldown());

                abilityInfoMap.put(info.getId(), info);
            }

            sendBulkAbilityUpdate();
        }
    }

    private void serializeClasses(NBTTagCompound tag) {
        saveCurrentClass();

        NBTTagList classes = new NBTTagList();
        for (PlayerClassInfo info : knownClasses.values()) {
            NBTTagCompound sk = new NBTTagCompound();
            info.serialize(sk);
            classes.appendTag(sk);
        }
        tag.setTag("classes", classes);

        tag.setString("activeClassId", getClassId().toString());
    }

    private void deserializeClasses(NBTTagCompound tag) {
        if (tag.hasKey("classes")) {
            NBTTagList classes = tag.getTagList("classes", Constants.NBT.TAG_COMPOUND);
            knownClasses = new HashMap<>(classes.tagCount());

            for (int i = 0; i < classes.tagCount(); i++) {
                NBTTagCompound cls = classes.getCompoundTagAt(i);
                PlayerClassInfo info = new PlayerClassInfo(new ResourceLocation(cls.getString("id")));
                info.deserialize(cls);

                knownClasses.put(info.classId, info);
            }
        }

        if (tag.hasKey("activeClassId", Constants.NBT.TAG_STRING)) {
            ResourceLocation classId = new ResourceLocation(tag.getString("activeClassId"));
            // If the character was saved with a class that doesn't exist anymore (say from a plugin),
            // reset the character to have no class
            if (MKURegistry.getClass(classId) == null)
                classId = MKURegistry.INVALID_CLASS;

            activateClass(classId);
        } else {
            activateClass(MKURegistry.INVALID_CLASS);
        }
    }

    @Override
    public void serialize(NBTTagCompound nbt) {
        nbt.setInteger("mana", getMana());
        nbt.setFloat("manaRegenRate", getBaseManaRegenRate());
        nbt.setInteger("totalMana", getBaseTotalMana());
        nbt.setFloat("healthRegenRate", getBaseHealthRegenRate());
        serializeSkills(nbt);
        serializeClasses(nbt);
    }

    @Override
    public void deserialize(NBTTagCompound nbt) {
        if (nbt.hasKey("mana", 3)) {
            setMana(nbt.getInteger("mana"));
        }
        if (nbt.hasKey("manaRegenRate", 3)) {
            setManaRegen(nbt.getFloat("manaRegenRate"));
        }
        if (nbt.hasKey("totalMana", 3)) {
            setTotalMana(nbt.getInteger("totalMana"));
        }
        if (nbt.hasKey("healthRegenRate", 3)){
            setHealthRegen(nbt.getFloat("healthRegenRate"));
        }

        deserializeSkills(nbt);
        deserializeClasses(nbt);
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
        int totalPoints = getUnspentPoints();
        for (int i = 0; i < GameConstants.ACTION_BAR_SIZE; i++) {
            ResourceLocation abilityId = getAbilityInSlot(i);

            if (abilityId.compareTo(MKURegistry.INVALID_ABILITY) == 0)
                continue;

            PlayerAbilityInfo info = getAbilityInfo(abilityId);
            if (info == null)
                continue;

            if (info.isCurrentlyKnown()) {
                totalPoints += info.getRank();
            }
        }

        Log.info("validateAbilityPoints: %s expected %d calculated %d", player.getName(), getLevel(), totalPoints);
    }

    public void doDeath() {
        validateAbilityPoints();
        if (getLevel() > 1) {
            int curUnspent = getUnspentPoints();
            if (curUnspent > 0) {
                setUnspentPoints(curUnspent - 1);
            } else {
                ResourceLocation lastAbility = getLastUpgradedAbility();
                if (lastAbility.compareTo(MKURegistry.INVALID_ABILITY) != 0) {
                    unlearnAbility(lastAbility, false, false);
                }
            }

            // Check to see if de-leveling will make us lower than the required level for some spells.
            // If so, unlearn the spell and refund the point.
            int newLevel = getLevel() - 1;
            Arrays.stream(getActiveAbilities())
                    .filter(r -> MKURegistry.getAbility(r) != null)
                    .map(MKURegistry::getAbility)
                    .filter(a -> {
                        // Subtract 1 because getRequiredLevel is a little weird. It actually tells you the required
                        // level to go up a rank, not the required level for the current rank
                        int newRank = getAbilityRank(a.getAbilityId()) - 1;
                        int reqLevel = a.getRequiredLevel(newRank);
                        reqLevel = Math.max(1, reqLevel);
                        return reqLevel > newLevel;
                    })
                    .forEach(a -> unlearnAbility(a.getAbilityId(), true, false));

            setLevel(newLevel);
        }
    }

    @Override
    public boolean learnClass(IClassProvider provider, ResourceLocation classId) {
        return learnClass(provider, classId, true);
    }

    public boolean learnClass(IClassProvider provider, ResourceLocation classId, boolean enforceChecks) {
        if (isClassKnown(classId)) {
            // Class was already known
            return true;
        }

        PlayerClass playerClass = MKURegistry.getClass(classId);
        if (playerClass == null)
            return false;

        if (enforceChecks && !provider.teachesClass(playerClass))
            return false;

        PlayerClassInfo info = new PlayerClassInfo(classId);
        knownClasses.put(classId, info);
        sendBulkClassUpdate();

        // Learned class
        return true;
    }

    public void unlearnClass(ResourceLocation classId) {
        if (isClassKnown(classId)) {
            activateClass(MKURegistry.INVALID_CLASS);
            knownClasses.remove(classId);

            // Unlearn all abilities offered by this class
            PlayerClass bc = MKURegistry.getClass(classId);
            if (bc != null) {
                bc.getAbilities().forEach(a -> unlearnAbility(a.getAbilityId(), false, true));
            }

            sendBulkClassUpdate();
        }
    }

    private boolean isClassKnown(ResourceLocation classId) {
        return knownClasses.containsKey(classId);
    }

    private void saveCurrentClass() {
        if (!hasChosenClass()) {
            return;
        }

        PlayerClassInfo cinfo = getActiveClass();
        if (cinfo == null) {
            return;
        }

        // save current class data
        cinfo.level = getLevel();
        cinfo.unspentPoints = getUnspentPoints();
        cinfo.setActiveAbilities(getActiveAbilities());
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

    @Override
    public void activateClass(ResourceLocation classId) {

        int level;
        int unspent;
        ResourceLocation[] hotbar;

        saveCurrentClass();
        deactivateCurrentToggleAbilities();

        if (classId.compareTo(MKURegistry.INVALID_CLASS) == 0 || !isClassKnown(classId)) {
            // Switching to no class

            classId = MKURegistry.INVALID_CLASS;
            level = 1;
            unspent = 1;
            hotbar = new ResourceLocation[GameConstants.ACTION_BAR_SIZE];
            Arrays.fill(hotbar, MKURegistry.INVALID_ABILITY);
        } else {
            PlayerClassInfo info = knownClasses.get(classId);
            level = info.level;
            unspent = info.unspentPoints;
            hotbar = info.getActiveAbilities();
        }

        setClassId(classId);
        setLevel(level);
        setUnspentPoints(unspent);
        setActiveAbilities(hotbar);
        ItemRestrictionHandler.checkEquipment(player);
    }

    @Override
    public List<ResourceLocation> getKnownClasses() {
        return Lists.newArrayList(knownClasses.keySet());
    }

    public boolean canWearArmorMaterial(ItemArmor.ArmorMaterial material) {

        PlayerClass currentClass = MKURegistry.getClass(getClassId());
        // If no class, default to vanilla behaviour of wearing anything
        // Then check the current class if it's allowed
        // Then check for special exceptions granted by other means
        return currentClass == null ||
                currentClass.getArmorClass().canWear(material) ||
                alwaysAllowedArmorMaterials.contains(material);

    }

    @Override
    public boolean setCooldown(ResourceLocation abilityId, int cooldownTicks) {
        PlayerAbilityInfo info = getAbilityInfo(abilityId);
        if (info == null)
            return false;

        if (cooldownTicks > 0) {
            abilityTracker.setCooldown(info, cooldownTicks);
        } else {
            abilityTracker.removeCooldown(info);
        }
        return true;
    }

    @Override
    public float getCooldownPercent(PlayerAbility ability, float partialTicks) {
        PlayerAbilityInfo info = getAbilityInfo(ability.getAbilityId());
        return info != null ? abilityTracker.getCooldown(info, partialTicks) : 0.0f;
    }

    public void debugResetAllCooldowns() {
        for (PlayerAbilityInfo info : abilityInfoMap.values()) {
            setCooldown(info.getId(), 0);
        }

        updateActiveAbilities();
    }

    public void debugDumpAllAbilities(ICommandSender sender) {

        String msg = "All Abilities:";
        sender.sendMessage(new TextComponentString(msg));
        for (PlayerAbilityInfo info : abilityInfoMap.values()) {
            PlayerAbility ability = MKURegistry.getAbility(info.getId());

            msg = String.format("%s: %d / %d", ability.getAbilityName(), abilityTracker.getCooldownTicks(info), getAbilityCooldown(ability));
            sender.sendMessage(new TextComponentString(msg));
        }
    }
}
