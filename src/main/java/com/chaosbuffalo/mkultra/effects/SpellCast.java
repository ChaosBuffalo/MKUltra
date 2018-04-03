package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkultra.log.Log;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;

import java.util.HashMap;
import java.util.Map;

public class SpellCast {

    private static Map<EntityLivingBase, Map<SpellPotionBase, SpellCast>> allCasts =
            new HashMap<>(new HashMap<EntityLivingBase, Map<SpellPotionBase, SpellCast>>());

    private SpellPotionBase potion;
    private Entity applier;
    private Entity caster;
    private NBTTagCompound data;

    public SpellCast(SpellPotionBase potion, Entity caster) {
        this.potion = potion;
        this.applier = caster;
        this.caster = caster;
        this.data = new NBTTagCompound();
    }

    public static SpellCast create(SpellPotionBase potion, Entity caster) {
        return new SpellCast(potion, caster);
    }

    public static SpellCast get(EntityLivingBase target, SpellPotionBase potion) {

        Map<SpellPotionBase, SpellCast> targetSpells = allCasts.get(target);
        if (targetSpells == null) {
            Log.warn("Tried to get a spell on an unregistered target!");
            return null;
        }

        return targetSpells.get(potion);
    }

    public static void registerTarget(SpellCast cast, EntityLivingBase target) {

        Map<SpellPotionBase, SpellCast> targetSpells = allCasts.get(target);
        if (targetSpells == null) {
            allCasts.put(target, new HashMap<>());
            targetSpells = allCasts.get(target);
        }

        targetSpells.put(cast.potion, cast);
    }

    public Entity getApplier() {
        return applier;
    }

    public Entity getCaster() {
        return caster;
    }

    public SpellCast setTarget(EntityLivingBase target) {
        registerTarget(this, target);
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

}
