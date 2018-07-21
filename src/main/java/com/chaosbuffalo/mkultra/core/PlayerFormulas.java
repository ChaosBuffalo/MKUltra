package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.utils.ItemUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class PlayerFormulas {

    public static float scaleMagicDamage(IPlayerData playerData, float originalDamage) {
        float mod = playerData.getMagicDamageBonus();
        return originalDamage + mod;
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

    public static int applyManaCostReduction(IPlayerData playerData, int originalCost) {
        return originalCost;
    }

    public static float getMeleeCritChanceForItem(IPlayerData data, EntityPlayerMP player, ItemStack item) {
        return data.getMeleeCritChance() + ItemUtils.getCritChanceForItem(item);
    }
}
