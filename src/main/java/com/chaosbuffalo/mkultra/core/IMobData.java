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

    void setISMKSpawning(boolean ismkSpawning);

    boolean isMKSpawning();

    void serialize(NBTTagCompound tag);

    void deserialize(NBTTagCompound tag);

    boolean isBoss();

    void setIsBoss(boolean in);

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

    boolean isFactionPlayerFriendly();

    void setTimeBetweenCasts(int value);

    int getTimeBetweenCasts();

    boolean isOnCastCooldown();

    int getBonusExperience();

    void setBonusExperience(int value);

    int getMaxTimeBetweenCasts();

    void setMaxTimeBetweenCasts(int value);

    boolean hasAdditionalLootTable();

    ResourceLocation getAdditionalLootTable();

    void setAdditionalLootTable(ResourceLocation table);

    ResourceLocation getMobDefinition();

    void setMobDefinition(ResourceLocation definition);

}
