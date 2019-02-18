package com.chaosbuffalo.mkultra.core;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

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

    void setSpawnPoint(BlockPos value);

    BlockPos getSpawnPoint();

    boolean hasSpawnPoint();

    void setAggroRange(double value);

    EntityLivingBase getEntity();

    ResourceLocation getMobFaction();

    void setMobFaction(ResourceLocation factionName);

    boolean hasFaction();

    boolean isSameFaction(EntityLivingBase other);
}
