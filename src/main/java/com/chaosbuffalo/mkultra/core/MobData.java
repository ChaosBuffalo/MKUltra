package com.chaosbuffalo.mkultra.core;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashSet;

public class MobData implements IMobData {
    private int level;
    private final EntityLivingBase entity;
    private  boolean isMKSpawned;
    private final HashSet<MobAbilityTracker> trackers;
    private boolean hasAbilities;
    private double aggroRange;


    public MobData(EntityLivingBase entity) {
        this.entity = entity;
        this.trackers = new HashSet<>();
        hasAbilities = false;
        aggroRange = 10.0;
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
    public void addAbility(BaseMobAbility abilityIn) {
        trackers.add(new MobAbilityTracker(abilityIn, this));
        hasAbilities = true;
    }

    @Override
    public double getAggroRange() {
        return aggroRange;
    }

    @Override
    public void setAggroRange(double value) {
        aggroRange = value;
    }

    @Override
    public EntityLivingBase getEntity() {
        return entity;
    }
}
