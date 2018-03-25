package com.chaosbuffalo.mkultra.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Created by Jacob on 3/24/2018.
 */
public abstract class BaseToggleAbility extends BaseAbility {

    public BaseToggleAbility(String domain, String id) {
        this(new ResourceLocation(domain, id));
    }

    public BaseToggleAbility(ResourceLocation abilityId) {
        super(abilityId);
    }

    public abstract Potion getToggleEffect();

    @Override
    public int getType() {
        return TOGGLE_ABILITY;
    }

    public abstract void applyEffect(EntityPlayer entity, IPlayerData pData, World theWorld);

    public void removeEffect(EntityPlayer entity, IPlayerData pData, World theWorld){
        entity.removePotionEffect(getToggleEffect());
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);
        if (entity.getActivePotionEffect(getToggleEffect()) != null){
            removeEffect(entity, pData, theWorld);
        } else {
            applyEffect(entity, pData, theWorld);
        }
    }

}
