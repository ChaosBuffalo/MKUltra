package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.event.ItemRestrictionHandler;
import com.chaosbuffalo.mkultra.item.ItemHelper;
import com.chaosbuffalo.mkultra.item.ManaRegenIdol;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.network.packets.client.PlayerSyncRequestPacket;
import com.chaosbuffalo.mkultra.network.packets.server.AbilityUpdatePacket;
import com.chaosbuffalo.mkultra.network.packets.server.ClassUpdatePacket;
import com.google.common.collect.Lists;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
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
    private final static DataParameter<Integer>[] ACTION_BAR_ABILITY_LEVEL;

    static {
        ACTION_BAR_ABILITY_ID = new DataParameter[GameConstants.ACTION_BAR_SIZE];
        for (int i = 0; i < GameConstants.ACTION_BAR_SIZE; i++) {
            ACTION_BAR_ABILITY_ID[i] = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.STRING);
        }
        ACTION_BAR_ABILITY_LEVEL = new DataParameter[GameConstants.ACTION_BAR_SIZE];
        for (int i = 0; i < GameConstants.ACTION_BAR_SIZE; i++) {
            ACTION_BAR_ABILITY_LEVEL[i] = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.VARINT);
        }
    }


    private final EntityPlayer player;
    private final EntityDataManager privateData;
    private float regenTime;
    private AbilityTracker abilityTracker;
    private Map<ResourceLocation, PlayerClassInfo> knownClasses = new HashMap<>();
    private Map<ResourceLocation, PlayerAbilityInfo> abilityInfoMap = new HashMap<>(5);
    private Set<ItemArmor.ArmorMaterial> alwaysAllowedArmorMaterials = new HashSet<>();

    public PlayerData(EntityPlayer player) {
        this.player = player;
        regenTime = 0;
        if (isServerSide()) {
            abilityTracker = new AbilityTrackerServer((EntityPlayerMP) player);
        } else {
            abilityTracker = new AbilityTracker();
        }
        privateData = player.getDataManager();
        setupWatcher();

        player.getAttributeMap().registerAttribute(PlayerAttributes.MAX_MANA);
        player.getAttributeMap().registerAttribute(PlayerAttributes.MANA_REGEN);
        player.getAttributeMap().registerAttribute(PlayerAttributes.MAGIC_ATTACK_DAMAGE);
        player.getAttributeMap().registerAttribute(PlayerAttributes.MAGIC_ARMOR);
        player.getAttributeMap().registerAttribute(PlayerAttributes.COOLDOWN);
    }

    private void setupWatcher() {

        privateData.register(MANA, 0);
        privateData.register(UNSPENT_POINTS, 0);
        privateData.register(CLASS_ID, ClassData.INVALID_CLASS.toString());
        privateData.register(LEVEL, 0);
        for (int i = 0; i < GameConstants.ACTION_BAR_SIZE; i++) {
            privateData.register(ACTION_BAR_ABILITY_ID[i], ClassData.INVALID_ABILITY.toString());
            privateData.register(ACTION_BAR_ABILITY_LEVEL[i], GameConstants.ACTION_BAR_INVALID_LEVEL);
        }
    }

    private void markEntityDataDirty() {
        privateData.setDirty(MANA);
        privateData.setDirty(UNSPENT_POINTS);
        privateData.setDirty(CLASS_ID);
        privateData.setDirty(LEVEL);
        for (int i = 0; i < GameConstants.ACTION_BAR_SIZE; i++) {
            privateData.setDirty(ACTION_BAR_ABILITY_ID[i]);
            privateData.setDirty(ACTION_BAR_ABILITY_LEVEL[i]);
        }
    }

    private PlayerClassInfo getActiveClass() {
        return knownClasses.get(getClassId());
    }

    private ResourceLocation getLastLeveledAbility() {
        PlayerClassInfo cinfo = getActiveClass();
        if (cinfo != null) {
            return cinfo.getLastLeveledAbility();
        }
        return ClassData.INVALID_ABILITY;
    }

    private void updatePlayerStats() {
        if (!hasChosenClass()) {
            setMana(0);
            setTotalMana(0);
            setManaRegen(0);
            setHealth(Math.min(20, this.player.getHealth()));
            setTotalHealth(20);
        } else {
            BaseClass playerClass = ClassData.getClass(getClassId());
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
    public boolean hasChosenClass() {
        return this.getClassId().compareTo(ClassData.INVALID_CLASS) != 0;
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
        return getCurrentSlotForAbility(ClassData.INVALID_ABILITY);
    }

    @Override
    public ResourceLocation getAbilityInSlot(int index) {
        if (index < ACTION_BAR_ABILITY_ID.length) {
            return new ResourceLocation(privateData.get(ACTION_BAR_ABILITY_ID[index]));
        }
        return ClassData.INVALID_ABILITY;
    }

    @Override
    public int getCurrentAbilityCooldown(ResourceLocation abilityId) {
        PlayerAbilityInfo abilityInfo = getAbilityInfo(abilityId);
        return abilityInfo != null ? abilityTracker.getCooldownTicks(abilityInfo) : GameConstants.ACTION_BAR_INVALID_COOLDOWN;
    }

    @Override
    public int getAbilityCooldown(BaseAbility ability) {
        return PlayerFormulas.applyCooldownReduction(this, ability.getCooldownTicks(getLevelForAbility(ability.getAbilityId())));
    }

    @Override
    public int getLevelForAbility(ResourceLocation abilityId) {
        PlayerAbilityInfo abilityInfo = getAbilityInfo(abilityId);
        return abilityInfo != null ? abilityInfo.level : GameConstants.ACTION_BAR_INVALID_LEVEL;
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

        info.level += 1;
        classInfo.addToSpendOrder(abilityId);

        if (abilityTracker.hasCooldown(info)) {
            BaseAbility ability = ClassData.getAbility(abilityId);
            int newMaxCooldown = getAbilityCooldown(ability);
            int current = abilityTracker.getCooldownTicks(info);
            setCooldown(info.id, Math.min(current, newMaxCooldown));
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

    @Override
    public boolean unlearnAbility(ResourceLocation abilityId, boolean refundPoint) {
        PlayerAbilityInfo info = getAbilityInfo(abilityId);
        if (info == null || info.level == GameConstants.ACTION_BAR_INVALID_LEVEL) {
            // We never knew it or it exists but is currently unlearned
            return false;
        }

        info.level -= 1;

        if (refundPoint) {
            int curUnspent = getUnspentPoints();
            setUnspentPoints(curUnspent + 1);
        }

        if (info.level <= GameConstants.ACTION_BAR_INVALID_LEVEL) {
            // Unlearning completely. Keep the PlayerAbilityInfo around so we can continue to track cooldowns
            info.level = GameConstants.ACTION_BAR_INVALID_LEVEL;
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
        BaseAbility ability = ClassData.getAbility(info.id);
        if (ability instanceof BaseToggleAbility && player != null) {
            BaseToggleAbility toggle = (BaseToggleAbility) ability;

            if (info.level > GameConstants.ACTION_BAR_INVALID_LEVEL) {
                // If this is a toggle ability we must re-apply the effect to make sure it's working at the proper level
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
        if (abilityId.compareTo(ClassData.INVALID_ABILITY) == 0)
            return false;

        if (getCurrentAbilityCooldown(abilityId) == 0) {

            BaseAbility ability = ClassData.getAbility(abilityId);
            if (ability != null && ability.meetsRequirements(this)) {
                ability.execute(player, this, player.getEntityWorld());
                return true;
            }
        }

        return false;
    }

    @Override
    public void startAbility(BaseAbility ability) {
        PlayerAbilityInfo info = getAbilityInfo(ability.getAbilityId());
        if (info == null || info.level == GameConstants.ACTION_BAR_INVALID_LEVEL)
            return;
        ItemStack heldItem = this.player.getHeldItem(EnumHand.OFF_HAND);
        if (heldItem.getItem() instanceof ManaRegenIdol) {
            ItemHelper.damageStack(player, heldItem, 1);
        }
        ItemStack mainHandItem = this.player.getHeldItem(EnumHand.MAIN_HAND);
        if (mainHandItem.getItem() instanceof ManaRegenIdol) {
            ItemHelper.damageStack(player, mainHandItem, 1);
        }
        int manaCost = ability.getManaCost(info.level);
        manaCost = PlayerFormulas.applyManaCostReduction(this, manaCost);
        setMana(getMana() - manaCost);

        int cooldown = ability.getCooldownTicks(info.level);
        cooldown = PlayerFormulas.applyCooldownReduction(this, cooldown);
        setCooldown(info.id, cooldown);
    }

    private PlayerAbilityInfo getAbilityInfo(ResourceLocation abilityId) {
        return abilityInfoMap.get(abilityId);
    }

    private void updateActiveAbilitySlot(int index) {
        ResourceLocation abilityId = getAbilityInSlot(index);
        PlayerAbilityInfo abilityInfo = getAbilityInfo(abilityId);

        boolean valid = abilityInfo != null && abilityInfo.level != GameConstants.ACTION_BAR_INVALID_LEVEL;
        ResourceLocation id = valid ? abilityInfo.id : ClassData.INVALID_ABILITY;
        int level = valid ? abilityInfo.level : GameConstants.ACTION_BAR_INVALID_LEVEL;

        setAbilityInSlot(index, id);
        privateData.set(ACTION_BAR_ABILITY_LEVEL[index], level);

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
    public float getManaRegenRate() {
        return (float) player.getEntityAttribute(PlayerAttributes.MANA_REGEN).getAttributeValue();
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
        regenTime += 1. / 20.;
        float i_regen = 3.0f / this.getManaRegenRate();
        if (regenTime >= i_regen) {
            if (this.getMana() < this.getTotalMana()) {
                this.setMana(this.getMana() + 1);
            }

            regenTime -= i_regen;
        }
    }

    private boolean isServerSide() {
        return this.player instanceof EntityPlayerMP;
    }

    public void forceUpdate() {
        markEntityDataDirty();
        sendBulkAbilityUpdate(abilityInfoMap.values());
        sendBulkClassUpdate(knownClasses.values());
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
    }

    private void sendSingleAbilityUpdate(PlayerAbilityInfo info) {
        if (isServerSide()) {
            boolean removed = info.level == GameConstants.ACTION_BAR_INVALID_LEVEL;
            MKUltra.packetHandler.sendTo(new AbilityUpdatePacket(info, removed), (EntityPlayerMP) player);
        }
    }

    private void sendBulkAbilityUpdate(Collection<PlayerAbilityInfo> updated) {
        if (isServerSide()) {
            MKUltra.packetHandler.sendTo(new AbilityUpdatePacket(updated), (EntityPlayerMP) player);
        }
    }

    private void sendBulkClassUpdate(Collection<PlayerClassInfo> updated) {
        if (isServerSide()) {
            MKUltra.packetHandler.sendTo(new ClassUpdatePacket(updated), (EntityPlayerMP) player);
        }
    }

    @SideOnly(Side.CLIENT)
    public void clientSkillListUpdate(PlayerAbilityInfo info, boolean removed) {
        if (removed) {
            abilityInfoMap.remove(info.id);
        } else {
            abilityInfoMap.put(info.id, info);
        }
    }

    @SideOnly(Side.CLIENT)
    public void clientKnownClassUpdate(PlayerClassInfo info) {
        knownClasses.put(info.classId, info);
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

                abilityInfoMap.put(info.id, info);
            }

            sendBulkAbilityUpdate(abilityInfoMap.values());
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
            if (ClassData.getClass(classId) == null)
                classId = ClassData.INVALID_CLASS;

            activateClass(classId);
        } else {
            activateClass(ClassData.INVALID_CLASS);
        }
    }

    @Override
    public void serialize(NBTTagCompound nbt) {
        nbt.setInteger("mana", getMana());
        nbt.setFloat("manaRegenRate", getBaseManaRegenRate());
        nbt.setInteger("totalMana", getBaseTotalMana());
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

            if (abilityId.compareTo(ClassData.INVALID_ABILITY) == 0)
                continue;

            PlayerAbilityInfo info = getAbilityInfo(abilityId);
            if (info == null)
                continue;

            if (info.level > 0) {
                totalPoints += info.level;
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
                ResourceLocation lastAbility = getLastLeveledAbility();
                if (lastAbility.compareTo(ClassData.INVALID_ABILITY) != 0) {
                    unlearnAbility(lastAbility, false);
                }
            }

            // Check to see if de-leveling will make us lower than the required level for some spells.
            // If so, unlearn the spell and refund the point.
            int newLevel = getLevel() - 1;
            Arrays.stream(getActiveAbilities())
                    .filter(r -> ClassData.getAbility(r) != null)
                    .map(ClassData::getAbility)
                    .filter(a -> {
                        // Subtract 1 because getRequiredLevel is a little weird. It actually tells you the required
                        // level to go up a rank, not the required level for the current rank
                        int newAbilityLevel = getLevelForAbility(a.getAbilityId()) - 1;
                        int reqLevel = a.getRequiredLevel(newAbilityLevel);
                        reqLevel = Math.max(1, reqLevel);
                        return reqLevel > newLevel;
                    })
                    .forEach(a -> unlearnAbility(a.getAbilityId(), true));

            setLevel(newLevel);
        }
    }


    @Override
    public boolean learnClass(ResourceLocation classId) {
        if (!isClassKnown(classId)) {
            PlayerClassInfo info = new PlayerClassInfo(classId);
            knownClasses.put(classId, info);

            if (isServerSide()) {
                MKUltra.packetHandler.sendTo(new ClassUpdatePacket(knownClasses.values()), (EntityPlayerMP) player);
            }

            // Changed
            return true;
        }
        // No change
        return false;
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
            BaseAbility ability = ClassData.getAbility(abilityId);
            if (ability instanceof BaseToggleAbility && player != null) {
                BaseToggleAbility toggle = (BaseToggleAbility) ability;
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

        if (classId.compareTo(ClassData.INVALID_CLASS) == 0 || !isClassKnown(classId)) {
            // Switching to no class

            classId = ClassData.INVALID_CLASS;
            level = 1;
            unspent = 1;
            hotbar = new ResourceLocation[GameConstants.ACTION_BAR_SIZE];
            Arrays.fill(hotbar, ClassData.INVALID_ABILITY);
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
        checkClassFixes();
    }

    private void checkClassFixes() {
        if (getClassId().compareTo(new ResourceLocation(MKUltra.MODID, "class.druid")) == 0) {
            ResourceLocation eagleAspect = new ResourceLocation(MKUltra.MODID, "ability.eagle_aspect");

            PlayerAbilityInfo info = getAbilityInfo(eagleAspect);
            if (info != null) {
                if (info.level > GameConstants.ACTION_BAR_INVALID_LEVEL) {
                    Log.info("Found 'Eagle Aspect', refunding %d points", info.level);
                    setUnspentPoints(getUnspentPoints() + info.level);
                    info.level = 0;
                    sendSingleAbilityUpdate(info);
                }
            }
        }
    }

    @Override
    public List<ResourceLocation> getKnownClasses() {
        return Lists.newArrayList(knownClasses.keySet());
    }

    public boolean canWearArmorMaterial(ItemArmor.ArmorMaterial material) {

        BaseClass currentClass = ClassData.getClass(getClassId());
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
    public float getCooldownPercent(BaseAbility ability, float partialTicks) {
        PlayerAbilityInfo info = getAbilityInfo(ability.getAbilityId());
        return abilityTracker.getCooldown(info, partialTicks);
    }

    public void debugResetAllCooldowns() {
        for (PlayerAbilityInfo info : abilityInfoMap.values()) {
            setCooldown(info.id, 0);
        }

        updateActiveAbilities();
    }

    public void debugDumpAllAbilities(ICommandSender sender) {

        String msg = "All Abilities:";
        sender.sendMessage(new TextComponentString(msg));
        for (PlayerAbilityInfo info : abilityInfoMap.values()) {
            BaseAbility ability = ClassData.getAbility(info.id);

            msg = String.format("%s: %d / %d", ability.getAbilityName(), abilityTracker.getCooldownTicks(info), getAbilityCooldown(ability));
            sender.sendMessage(new TextComponentString(msg));
        }
    }
}
