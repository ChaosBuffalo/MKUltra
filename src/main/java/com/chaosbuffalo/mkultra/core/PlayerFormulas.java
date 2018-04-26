package com.chaosbuffalo.mkultra.core;

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
}
