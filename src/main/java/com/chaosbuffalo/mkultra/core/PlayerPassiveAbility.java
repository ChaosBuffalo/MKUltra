package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.effects.PassiveAbilityPotionBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;


public abstract class PlayerPassiveAbility  extends PlayerAbility {

    public PlayerPassiveAbility(String domain, String id) {
        this(new ResourceLocation(domain, id));
    }

    public PlayerPassiveAbility(ResourceLocation abilityId) {
        super(abilityId);
    }

    public abstract PassiveAbilityPotionBase getPassiveEffect();

    @Override
    public int getType() {
        return PASSIVE_ABILITY;
    }

    @Override
    public int getCooldown(int currentRank) {
        return 0;
    }

    public abstract void applyEffect(EntityPlayer entity, IPlayerData pData, World theWorld);

    public void removeEffect(EntityPlayer entity, IPlayerData pData, World theWorld) {
        entity.removePotionEffect(getPassiveEffect());
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        if (entity.getActivePotionEffect(getPassiveEffect()) == null) {
            applyEffect(entity, pData, theWorld);
        }
    }
}
