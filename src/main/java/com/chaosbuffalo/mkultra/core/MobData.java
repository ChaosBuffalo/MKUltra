package com.chaosbuffalo.mkultra.core;
import com.chaosbuffalo.mkultra.GameConstants;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.HashSet;

public class MobData implements IMobData {
    private int level;
    private final EntityLivingBase entity;
    private boolean isMKSpawned;
    private final HashSet<MobAbilityTracker> trackers;
    private boolean hasAbilities;
    private double aggroRange;
    private ResourceLocation factionName;
    private BlockPos spawnPoint;
    private int timeBetweenCasts;
    private int maxTimeBetweenCasts;
    private ResourceLocation additionalLootTable;
    private boolean isBoss;
    private ResourceLocation mobDefinition;


    public MobData(EntityLivingBase entity) {
        this.entity = entity;
        this.trackers = new HashSet<>();
        hasAbilities = false;
        aggroRange = 10.0;
        timeBetweenCasts = 0;
        isBoss = false;
        factionName = MKURegistry.INVALID_FACTION;
        maxTimeBetweenCasts = 10 * GameConstants.TICKS_PER_SECOND;
        mobDefinition = MKURegistry.INVALID_MOB;

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
            if (timeBetweenCasts > 0){
                timeBetweenCasts--;
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
        tag.setBoolean("isBoss", isBoss());
        tag.setInteger("level", level);
        if (hasAdditionalLootTable()){
            tag.setString("additionalLootTable", additionalLootTable.toString());
        }
        if (!mobDefinition.equals(MKURegistry.INVALID_MOB)){
            tag.setString("mobDefinition", mobDefinition.toString());
        }
        if (!factionName.equals(MKURegistry.INVALID_FACTION)){
            tag.setString("mobFaction", factionName.toString());
        }
    }

    @Override
    public void deserialize(NBTTagCompound tag) {
        if (tag.hasKey("isMKSpawned")) {
            setMKSpawned(tag.getBoolean("isMKSpawned"));
        }
        if (tag.hasKey("additionalLootTable")){
            setAdditionalLootTable(new ResourceLocation(tag.getString("additionalLootTable")));
        }
        if (tag.hasKey("isBoss")){
            setIsBoss(tag.getBoolean("isBoss"));
        }
        if (tag.hasKey("mobDefinition")){
            setMobDefinition(new ResourceLocation(tag.getString("mobDefinition")));
        }
        if (tag.hasKey("level")){
            setMobLevel(tag.getInteger("level"));
        }
        if (tag.hasKey("mobFaction")){
            setMobFaction(new ResourceLocation(tag.getString("mobFaction")));
        }
    }

    @Override
    public boolean isBoss() {
        return isBoss;
    }

    @Override
    public void setIsBoss(boolean in) {
        isBoss = in;
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

    @Override
    public void setTimeBetweenCasts(int value) {
        timeBetweenCasts = value;
    }

    @Override
    public int getTimeBetweenCasts() {
        return timeBetweenCasts;
    }

    @Override
    public boolean isOnCastCooldown() {
        return timeBetweenCasts > 0;
    }

    @Override
    public int getMaxTimeBetweenCasts() {
        return maxTimeBetweenCasts;
    }

    @Override
    public void setMaxTimeBetweenCasts(int value) {
        maxTimeBetweenCasts = value;
    }

    @Override
    public boolean hasAdditionalLootTable() {
        return additionalLootTable != null;
    }

    @Override
    public ResourceLocation getAdditionalLootTable() {
        return additionalLootTable;
    }

    @Override
    public void setAdditionalLootTable(ResourceLocation table) {
        additionalLootTable = table;
    }

    @Override
    public ResourceLocation getMobDefinition() {
        return mobDefinition;
    }

    @Override
    public void setMobDefinition(ResourceLocation definition) {
        mobDefinition = definition;
    }
}
