package com.chaosbuffalo.mkultra.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.lang.ref.WeakReference;
import java.util.UUID;

public class SpellCast {

    private SpellPotionBase potion;
    private WeakReference<Entity> applier;
    private WeakReference<Entity> caster;
    private NBTTagCompound data;
    private UUID casterUUID;
    private UUID applierUUID;

    public SpellCast(SpellPotionBase potion, Entity caster) {
        this.potion = potion;
        this.applier = new WeakReference<>(caster);
        this.caster = new WeakReference<>(caster);
        this.data = new NBTTagCompound();
        if (caster instanceof EntityPlayer) {
            this.casterUUID = caster.getUniqueID();
            this.applierUUID = caster.getUniqueID();
        }
    }

    public SpellPotionBase getPotion() {
        return potion;
    }

    void updateRefs(World world) {
        if (casterUUID != null) {
            caster = new WeakReference<>(world.getPlayerEntityByUUID(casterUUID));
        }
        if (applierUUID != null) {
            applier = new WeakReference<>(world.getPlayerEntityByUUID(applierUUID));
        }
    }

    public Entity getApplier() {
        return applier.get();
    }

    public Entity getCaster() {
        return caster.get();
    }

    public SpellCast setTarget(EntityLivingBase target) {
        SpellManager.registerTarget(this, target);
        return this;
    }

    public SpellCast setScalingParameters(float base, float scale) {
        setFloat("damageBase", base);
        setFloat("damageScale", scale);
        return this;
    }

    public SpellCast setFloat(String name, float value) {
        data.setFloat(name, value);
        return this;
    }

    public float getFloat(String name) {
        return data.getFloat(name);
    }

    public float getFloat(String name, float defaultValue) {
        if (data.hasKey(name)) {
            return data.getFloat(name);
        }
        return defaultValue;
    }

    public SpellCast setDouble(String name, double value) {
        data.setDouble(name, value);
        return this;
    }

    public double getDouble(String name) {
        return data.getDouble(name);
    }

    public SpellCast setInt(String name, int value) {
        data.setInteger(name, value);
        return this;
    }

    public int getInt(String name) {
        return data.getInteger(name);
    }

    public SpellCast setBoolean(String name, boolean value) {
        data.setBoolean(name, value);
        return this;
    }

    public SpellCast setResourceLocation(String name, ResourceLocation loc){
        data.setString(name, loc.toString());
        return this;
    }

    @Nullable
    public ResourceLocation getResourceLocation(String name){
        return new ResourceLocation(data.getString(name));
    }

    public boolean getBoolean(String name) {
        return data.getBoolean(name);
    }

    public float getBaseValue() {
        return getFloat("damageBase");
    }

    public float getScaleValue() {
        return getFloat("damageScale");
    }

    public float getScaledValue(int amplifier) {
        return getBaseValue() + (getScaleValue() * amplifier);
    }

    public PotionEffect toPotionEffect(int amplifier) {
        return potion.toPotionEffect(amplifier);
    }

    public PotionEffect toPotionEffect(int duration, int amplifier) {
        return potion.toPotionEffect(duration, amplifier);
    }

    @Override
    public String toString() {
        return String.format("Cast[%s, %s]", potion.getName(), caster);
    }
}
