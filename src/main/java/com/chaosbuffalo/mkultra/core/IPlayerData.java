package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.core.talents.TalentTreeRecord;
import net.minecraft.item.ItemArmor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashSet;
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

    void addToAllCooldowns(int cooldownTicks);

    float getCooldownPercent(PlayerAbility ability, float partialTicks);

    @Nullable
    CastState startAbility(PlayerAbility ability);

    PlayerAbilityInfo getAbilityInfo(ResourceLocation abilityId);

    void setManaRegen(float manaRegenRate);

    float getHealthRegenRate();

    void setHealthRegen(float healthRegenRate);

    float getManaRegenRate();

    void setMana(float mana);

    float getMana();

    default void addMana(float amount) {
        setMana(getMana() + amount);
    }

    default boolean consumeMana(float amount) {
        if (getMana() >= amount) {
            setMana(getMana() - amount);
            return true;
        }
        return false;
    }

    float getTotalMana();

    float getTotalHealth();

    void setHealth(float health);

    float getHealth();

    float getMeleeCritChance();

    float getSpellCritChance();

    float getSpellCritDamage();

    float getMeleeCritDamage();

    float getCooldownProgressSpeed();

    float getMagicDamageBonus();

    float getMagicArmor();

    float getHealBonus();

    float getBuffDurationBonus();

    int getCooldownForLevel(PlayerAbility ability, int level);

    boolean learnClass(IClassProvider provider, ResourceLocation classId);

    void activateClass(ResourceLocation classId);

    List<ResourceLocation> getKnownClasses();

    default boolean knowsClass(ResourceLocation classId) {
        return getKnownClasses().contains(classId);
    }

    void serialize(NBTTagCompound tag);

    void deserialize(NBTTagCompound tag);

    boolean canWearArmor(ItemArmor item);

    boolean spendTalentPoint(ResourceLocation talentTree, String line, int index);

    boolean refundTalentPoint(ResourceLocation talentTree, String line, int index);

    boolean canSpendTalentPoint(ResourceLocation talentTree, String line, int index);

    boolean canRefundTalentPoint(ResourceLocation talentTree, String line, int index);

    void gainTalentPoint();

    int getTotalTalentPoints();

    int getUnspentTalentPoints();

    TalentTreeRecord getTalentTree(ResourceLocation loc);

    @Nullable
    List<ResourceLocation> getActivePassives();

    @Nullable
    List<ResourceLocation> getActiveUltimates();

    @Nullable
    HashSet<PlayerPassiveAbility> getLearnedPassives();

    @Nullable
    HashSet<PlayerAbility> getLearnedUltimates();

    boolean canActivatePassiveForSlot(ResourceLocation loc, int slotIndex);

    boolean activatePassiveForSlot(ResourceLocation loc, int slotIndex);

    boolean canActivateUltimateForSlot(ResourceLocation loc, int slotIndex);

    boolean activateUltimateForSlot(ResourceLocation loc, int slotIndex);

    boolean hasUltimates();

    boolean isDualWielding();

    void setArbitraryCooldown(ResourceLocation loc, int cooldown);

    int getArbitraryCooldown(ResourceLocation loc);

    default boolean isArbitraryOnCooldown(ResourceLocation loc) {
        return getArbitraryCooldown(loc) > 0;
    }

    ArmorClass getArmorClass();

    PlayerToggleAbility getActiveToggleGroupAbility(ResourceLocation groupId);

    void clearToggleGroupAbility(ResourceLocation groupId);

    void setToggleGroupAbility(ResourceLocation groupId, PlayerToggleAbility ability);

    boolean isCasting();

    int getCastTicks();

    void setCastTicks(int value);

    ResourceLocation getCastingAbility();
}
