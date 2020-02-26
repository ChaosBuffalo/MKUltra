package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.utils.EntityUtils;
import com.chaosbuffalo.mkultra.utils.ItemUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class PlayerFormulas {

    public static float scaleMagicDamage(IPlayerData playerData, float originalDamage, float modifierScaling) {
        float mod = playerData.getMagicDamageBonus();
        return originalDamage + mod * modifierScaling;
    }

    public static float applyMagicArmor(IPlayerData playerData, float originalDamage) {
        float mod = playerData.getMagicArmor();
        return originalDamage - mod;
    }

    public static int applyCooldownReduction(IPlayerData playerData, int originalCooldownTicks) {
        float mod = 2.0f - playerData.getCooldownProgressSpeed();
        float newTicks = mod * originalCooldownTicks;
        return (int) newTicks;
    }

    public static float applyManaCostReduction(IPlayerData playerData, float originalCost) {
        return originalCost;
    }

    public static float applyHealBonus(IPlayerData playerData, float amount) {
        float mod = playerData.getHealBonus();
        return amount * mod;
    }

    public static float getMeleeCritChanceForItem(IPlayerData data, EntityPlayerMP player, ItemStack item) {
        return data.getMeleeCritChance() + ItemUtils.getCritChanceForItem(item);
    }

    public static float getRangedCritChanceForEntity(IPlayerData data, EntityPlayerMP player, Entity entity) {
        return EntityUtils.ENTITY_CRIT.getChance(entity);
    }

    public static int applyBuffDurationBonus(IPlayerData data, int duration) {
        return (int) (duration * data.getBuffDurationBonus());
    }

    public static boolean tryGainTalentPoint(IPlayerData data, PlayerClassInfo classInfo,  EntityPlayer player, boolean sim) {
        if (classInfo.isAtTalentPointLimit())
            return false;
        if (player.experienceLevel >= classInfo.getTotalTalentPoints()) {
            if (!sim) {
                player.addExperienceLevel(-classInfo.getTotalTalentPoints());
            }
            return true;
        }
        return false;
    }

    public static boolean tryGainLevel(IPlayerData data, PlayerClassInfo classInfo, EntityPlayer player, boolean sim) {
        int nextLevel = classInfo.getLevel() + 1;
        boolean xpCheck = player.experienceLevel >= nextLevel;
        boolean levelCheck = classInfo.getLevel() < GameConstants.MAX_CLASS_LEVEL;

        if (xpCheck && levelCheck) {
            if (!sim) {
                player.addExperienceLevel(-nextLevel);
            }
            return true;
        }

        return false;
    }
}
