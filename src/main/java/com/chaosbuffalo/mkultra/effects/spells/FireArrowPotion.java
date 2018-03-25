package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.*;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class FireArrowPotion extends SpellPotionBase {

    public static final FireArrowPotion INSTANCE = new FireArrowPotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static SpellCast Create(Entity source, float baseDamage, float scaling, float range) {
        return INSTANCE.newSpellCast(source)
                .setScalingParameters(baseDamage, scaling)
                .setFloat("range", range);
    }

    private FireArrowPotion() {
        // boolean isBadEffectIn, int liquidColorIn
        super(true, 4393423);
        SpellPotionBase.register("effect.fire_arrow", this);
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public void doEffect(Entity source, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {
        if (target.isBurning()) {
            SpellCast wave = FlameWavePotion.Create(caster, cast.getBaseValue(), cast.getScaleValue());
            SpellCast particle = ParticlePotion.Create(caster,
                    EnumParticleTypes.LAVA.getParticleID(),
                    ParticleEffects.SPHERE_MOTION, false, new Vec3d(1.0, 1.0, 1.0),
                    new Vec3d(0.0, 1.0, 0.0), 40, 5, 1.0);

            AreaEffectBuilder.Create((EntityLivingBase) caster, target)
                    .spellCast(wave, amplifier, getTargetType())
                    .spellCast(particle, amplifier, getTargetType())
                    .duration(6).waitTime(0)
                    .particle(EnumParticleTypes.LAVA)
                    .color(16737305).radius(cast.getFloat("range"), true)
                    .spawn();

        } else {
            target.setFire(6 * 20);
        }

    }
}