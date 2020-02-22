package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.core.abilities.cast_states.CastState;
import com.chaosbuffalo.mkultra.core.talents.TalentTreeRecord;
import net.minecraft.item.ItemArmor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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

    float getCooldownPercent(PlayerAbilityInfo abilityInfo, float partialTicks);

    float getAbilityManaCost(ResourceLocation abilityId);

    @Nullable
    CastState startAbility(PlayerAbility ability);

    PlayerAbilityInfo getAbilityInfo(ResourceLocation abilityId);

    float getHealthRegenRate();

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

    Collection<ResourceLocation> getKnownClasses();

    default boolean knowsClass(ResourceLocation classId) {
        return getKnownClasses().contains(classId);
    }

    void serialize(NBTTagCompound tag);

    void deserialize(NBTTagCompound tag);

    boolean canWearArmor(ItemArmor item);

    int getTotalTalentPoints();

    int getUnspentTalentPoints();

    TalentTreeRecord getTalentTree(ResourceLocation loc);

    @Nullable
    List<ResourceLocation> getActivePassives();

    @Nullable
    List<ResourceLocation> getActiveUltimates();

    @Nullable
    Set<PlayerPassiveAbility> getLearnedPassives();

    @Nullable
    Set<PlayerAbility> getLearnedUltimates();

    boolean hasUltimates();

    int getActionBarSize();

    boolean isDualWielding();

    void setTimer(ResourceLocation loc, int cooldown);

    int getTimer(ResourceLocation loc);

    default boolean hasActiveTimer(ResourceLocation loc) {
        return getTimer(loc) > 0;
    }

    ArmorClass getArmorClass();

    boolean isCasting();

    int getCastTicks();

    ResourceLocation getCastingAbility();
}
