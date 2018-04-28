package com.chaosbuffalo.mkultra.core;

import net.minecraft.item.ItemArmor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public interface IPlayerData {

    ResourceLocation getClassId();

    boolean hasChosenClass();

    int getUnspentPoints();

    boolean learnAbility(ResourceLocation abilityId, boolean consumePoint);

    boolean unlearnAbility(ResourceLocation abilityId, boolean refundPoint);

    boolean executeHotBarAbility(int abilitySlot);

    boolean canLevelUp();

    void levelUp();

    int getLevel();

    ResourceLocation getAbilityInSlot(int index);

    int getLevelForAbility(ResourceLocation abilityId);

    int getCurrentAbilityCooldown(ResourceLocation abilityId);

    int getAbilityCooldown(BaseAbility ability);

    boolean setCooldown(ResourceLocation abilityId, int cooldownTicks);

    float getCooldownPercent(BaseAbility ability, float partialTicks);

    void startAbility(BaseAbility ability);

    void setManaRegen(float manaRegenRate);

    float getManaRegenRate();

    void setMana(int mana);

    int getMana();

    void setTotalMana(int totalMana);

    int getTotalMana();

    void setTotalHealth(float totalHealth);

    float getTotalHealth();

    void setHealth(float health);

    float getHealth();

    float getCooldownProgressSpeed();

    float getMagicDamageBonus();

    float getMagicArmor();

    boolean learnClass(ResourceLocation classId);

    void activateClass(ResourceLocation classId);

    List<ResourceLocation> getKnownClasses();

    void serialize(NBTTagCompound tag);

    void deserialize(NBTTagCompound tag);

    boolean canWearArmorMaterial(ItemArmor.ArmorMaterial material);
}
