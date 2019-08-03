package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.client.gui.lib.MKScreen;
import com.chaosbuffalo.mkultra.core.talents.TalentTreeRecord;
import net.minecraft.item.ItemArmor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public interface IPlayerData {

    ResourceLocation getClassId();

    boolean hasChosenClass();

    int getUnspentPoints();

    boolean learnAbility(ResourceLocation abilityId, boolean consumePoint);

    boolean executeHotBarAbility(int abilitySlot);

    boolean canLevelUp();

    void levelUp();

    int getLevel();

    ResourceLocation getAbilityInSlot(int index);

    int getAbilityRank(ResourceLocation abilityId);

    int getCurrentAbilityCooldown(ResourceLocation abilityId);

    int getAbilityCooldown(PlayerAbility ability);

    boolean setCooldown(ResourceLocation abilityId, int cooldownTicks);

    float getCooldownPercent(PlayerAbility ability, float partialTicks);

    void startAbility(PlayerAbility ability);

    void setManaRegen(float manaRegenRate);

    float getHealthRegenRate();

    void setHealthRegen(float healthRegenRate);

    float getManaRegenRate();

    void setMana(int mana);

    int getMana();

    default boolean consumeMana(int amount) {
        if (getMana() >= amount) {
            setMana(getMana() - amount);
            return true;
        }
        return false;
    }

    void setTotalMana(int totalMana);

    int getTotalMana();

    void setTotalHealth(float totalHealth);

    float getTotalHealth();

    void setHealth(float health);

    float getHealth();

    float getMeleeCritChance();

    float getSpellCritChance();

    void setSpellCritChance(float critChance);

    float getSpellCritDamage();

    void setSpellCritDamage(float critDamage);

    void setMeleeCritChance(float critChance);

    float getCooldownProgressSpeed();

    float getMagicDamageBonus();

    float getMagicArmor();

    float getHealBonus();

    boolean learnClass(IClassProvider provider, ResourceLocation classId);

    void activateClass(ResourceLocation classId);

    List<ResourceLocation> getKnownClasses();

    default boolean knowsClass(ResourceLocation classId) {
        return getKnownClasses().contains(classId);
    }

    void serialize(NBTTagCompound tag);

    void deserialize(NBTTagCompound tag);

    boolean canWearArmorMaterial(ItemArmor.ArmorMaterial material);

    boolean spendTalentPoint(ResourceLocation talentTree, String line, int index);

    boolean refundTalentPoint(ResourceLocation talentTree, String line, int index);

    boolean canSpendTalentPoint(ResourceLocation talentTree, String line, int index);

    boolean canRefundTalentPoint(ResourceLocation talentTree, String line, int index);

    void gainTalentPoint();

    int getTotalTalentPoints();

    int getUnspentTalentPoints();

    TalentTreeRecord getTalentTree(ResourceLocation loc);

    void subscribeGuiToClassUpdates(Runnable cb);

    void unsubscribeGuiToClassUpdates(Runnable cb);
}
