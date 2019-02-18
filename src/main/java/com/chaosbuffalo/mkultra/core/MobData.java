package com.chaosbuffalo.mkultra.core;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.HashSet;

public class MobData implements IMobData {
    private int level;
    private final EntityLivingBase entity;
    private  boolean isMKSpawned;
    private final HashSet<MobAbilityTracker> trackers;
    private boolean hasAbilities;
    private double aggroRange;
    private ResourceLocation factionName;
    private BlockPos spawnPoint;


    public MobData(EntityLivingBase entity) {
        this.entity = entity;
        this.trackers = new HashSet<>();
        hasAbilities = false;
        aggroRange = 10.0;
        factionName = MKURegistry.INVALID_FACTION;
    }

    @Override
    public boolean isMKSpawned() {
        return isMKSpawned;
    }

    @Override
    public boolean hasAbilities() {
        return hasAbilities;
    }

    @Override
    public void onTick() {
        if (hasAbilities){
            for (MobAbilityTracker tracker : trackers){
                tracker.update();
            }
        }
    }

    @Override
    public void setMKSpawned(boolean isSpawned) {
        isMKSpawned = isSpawned;
    }

    @Override
    public void serialize(NBTTagCompound tag) {
        tag.setBoolean("isMKSpawned", isMKSpawned());
    }

    @Override
    public void deserialize(NBTTagCompound tag) {
        if (tag.hasKey("isMKSpawned")) {
            setMKSpawned(tag.getBoolean("isMKSpawned"));
        }
    }

    @Override
    public int getMobLevel() {
        return level;
    }

    @Override
    public void setMobLevel(int levelIn) {
        level = levelIn;
    }

    @Override
    public HashSet<MobAbilityTracker> getAbilityTrackers() {
        return trackers;
    }

    @Override
    public void addAbility(MobAbility abilityIn) {
        trackers.add(new MobAbilityTracker(abilityIn, this));
        hasAbilities = true;
    }

    @Override
    public double getAggroRange() {
        return aggroRange;
    }

    @Override
    public void setSpawnPoint(BlockPos value) {
        spawnPoint = value;
    }

    @Override
    public BlockPos getSpawnPoint() {
        return spawnPoint;
    }

    @Override
    public boolean hasSpawnPoint() {
        return spawnPoint != null;
    }

    @Override
    public void setAggroRange(double value) {
        aggroRange = value;
    }

    @Override
    public EntityLivingBase getEntity() {
        return entity;
    }

    @Override
    public ResourceLocation getMobFaction() {
        return factionName;
    }

    @Override
    public void setMobFaction(ResourceLocation factionName) {
        this.factionName = factionName;
    }

    @Override
    public boolean hasFaction() {
        return !factionName.equals(MKURegistry.INVALID_FACTION);
    }

    @Override
    public boolean isSameFaction(EntityLivingBase other) {
        IMobData otherData = MKUMobData.get(other);
        if (otherData == null || factionName.equals(MKURegistry.INVALID_FACTION)){
            return false;
        }
        return factionName.equals(otherData.getMobFaction());
    }
}
