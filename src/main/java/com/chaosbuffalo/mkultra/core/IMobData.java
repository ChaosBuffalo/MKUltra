package com.chaosbuffalo.mkultra.core;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.HashSet;

public interface IMobData {

    boolean isMKSpawned();

    boolean hasAbilities();

    void onTick();

    void setMKSpawned(boolean isSpawned);

    void serialize(NBTTagCompound tag);

    void deserialize(NBTTagCompound tag);

    int getMobLevel();

    void setMobLevel(int levelIn);

    HashSet<MobAbilityTracker> getAbilityTrackers();

    void addAbility(MobAbility abilityIn);

    double getAggroRange();

    void setAggroRange(double value);

    EntityLivingBase getEntity();

    ResourceLocation getMobFaction();

    void setMobFaction(ResourceLocation factionName);

    boolean hasFaction();

    boolean isSameFaction(EntityLivingBase other);
}
