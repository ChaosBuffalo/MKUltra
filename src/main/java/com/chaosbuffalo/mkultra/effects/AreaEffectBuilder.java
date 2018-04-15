package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkultra.api.Targeting;
import com.chaosbuffalo.mkultra.entities.EntityMKAreaEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;


public class AreaEffectBuilder {

    private EntityMKAreaEffect areaEffectCloud;

    private AreaEffectBuilder(EntityLivingBase caster, Entity center) {
        areaEffectCloud = new EntityMKAreaEffect(center.getEntityWorld(), center.posX, center.posY, center.posZ);
        areaEffectCloud.setOwner(caster);
    }

    public static AreaEffectBuilder Create(EntityLivingBase caster, Entity center) {
        return new AreaEffectBuilder(caster, center);
    }

    public AreaEffectBuilder instant() {
        return duration(6).waitTime(0);
    }

    public AreaEffectBuilder duration(int duration) {
        areaEffectCloud.setDuration(duration);
        return this;
    }

    public AreaEffectBuilder waitTime(int waitTime) {
        areaEffectCloud.setWaitTime(waitTime);
        return this;
    }

    public AreaEffectBuilder spellCast(SpellCast cast, int amplifier, Targeting.TargetType targetType) {
        return spellCast(cast, cast.toPotionEffect(amplifier), targetType, false);
    }

    public AreaEffectBuilder spellCast(SpellCast cast, int duration, int amplifier, Targeting.TargetType targetType) {
        return spellCast(cast, cast.toPotionEffect(duration, amplifier), targetType, false);
    }

    public AreaEffectBuilder spellCast(SpellCast cast, PotionEffect effect, Targeting.TargetType targetType) {
        return spellCast(cast, effect, targetType, false);
    }

    public AreaEffectBuilder spellCast(SpellCast cast, PotionEffect effect, Targeting.TargetType targetType, boolean excludeCaster) {
        areaEffectCloud.addSpellCast(cast, effect, targetType, excludeCaster);
        return this;
    }

    public AreaEffectBuilder effect(PotionEffect effect, Targeting.TargetType targetType) {
        return effect(effect, targetType, false);
    }

    private AreaEffectBuilder effect(PotionEffect effect, Targeting.TargetType targetType, boolean excludeCaster) {
        areaEffectCloud.addEffect(effect, targetType, excludeCaster);
        return this;
    }

    public AreaEffectBuilder radius(float radius) {
        return radius(radius, false);
    }

    public AreaEffectBuilder radius(float radius, boolean makeCube) {
        areaEffectCloud.setRadius(radius);

        // setRadius calls setSize which changes the bounding box according to the width and height
        // but the default height of an AreaEffect is just 0.5
        if (makeCube) {
            AxisAlignedBB bb = areaEffectCloud.getEntityBoundingBox();
            bb = bb.expand(0, radius, 0);
            areaEffectCloud.setEntityBoundingBox(bb);
        }
        return this;
    }

    public AreaEffectBuilder color(int color) {
        areaEffectCloud.setColor(color);
        return this;
    }

    public AreaEffectBuilder particle(EnumParticleTypes particleType) {
        areaEffectCloud.setParticle(particleType);
        return this;
    }

    public AreaEffectBuilder setReapplicationDelay(int ticksBetweenApplication) {
        areaEffectCloud.setReapplicationDelay(ticksBetweenApplication);
        return this;
    }

    public void spawn() {
        areaEffectCloud.getOwner().world.spawnEntity(areaEffectCloud);
    }
}
