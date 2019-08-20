package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.effects.passives.PassiveAbilityPotionBase;
import com.chaosbuffalo.targeting_api.Targeting;
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

    public PlayerPassiveAbility finish() {
        // This method is called from the registry callback, so our mod is the active mod
        // The single parameter overload of setRegistryName uses the domain of the active mod automatically
        setRegistryName(getAbilityId());
        return this;
    }

    @Override
    public int getType() {
        return PASSIVE_ABILITY;
    }

    @Override
    public int getCooldown(int currentRank) {
        return 0;
    }

    @Override
    public float getManaCost(int currentRank) {
        return 0;
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return 0;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.SELF;
    }

    public abstract void applyEffect(EntityPlayer entity, IPlayerData pData, World theWorld);

    public void removeEffect(EntityPlayer entity, IPlayerData pData, World theWorld) {
        ((PlayerData) pData).removePassiveEffect(getPassiveEffect());
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        if (entity.getActivePotionEffect(getPassiveEffect()) == null) {
            applyEffect(entity, pData, theWorld);
        }
    }
}
