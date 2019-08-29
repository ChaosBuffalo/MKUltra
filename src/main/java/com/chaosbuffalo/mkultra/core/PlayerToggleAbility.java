package com.chaosbuffalo.mkultra.core;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Map;

/**
 * Created by Jacob on 3/24/2018.
 */
public abstract class PlayerToggleAbility extends PlayerAbility {

    private static final Map<Potion, ResourceLocation> toggleAbilityMap = Maps.newHashMap();

    public static ResourceLocation getToggleAbilityIdForPotion(Potion potion) {
        return toggleAbilityMap.get(potion);
    }

    public PlayerToggleAbility(String domain, String id) {
        this(new ResourceLocation(domain, id));
    }

    public PlayerToggleAbility(ResourceLocation abilityId) {
        super(abilityId);
        toggleAbilityMap.put(getToggleEffect(), abilityId);
    }

    public ResourceLocation getToggleGroupId() {
        return getAbilityId();
    }

    public abstract Potion getToggleEffect();

    @Override
    public AbilityType getType() {
        return AbilityType.Toggle;
    }

    public void applyEffect(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.setToggleGroupAbility(getToggleGroupId(), this);
    }

    public void removeEffect(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.clearToggleGroupAbility(getToggleGroupId());
        entity.removePotionEffect(getToggleEffect());
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);
        if (entity.getActivePotionEffect(getToggleEffect()) != null) {
            removeEffect(entity, pData, theWorld);
        } else {
            applyEffect(entity, pData, theWorld);
        }
    }

}
