package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.mkultra.effects.Targeting;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class PoisonArrowPotion extends SpellPotionBase {

    public static final PoisonArrowPotion INSTANCE = new PoisonArrowPotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static SpellCast Create(Entity source, float range) {
        return INSTANCE.newSpellCast(source).setFloat("range", range);
    }

    private PoisonArrowPotion() {
        // boolean isBadEffectIn, int liquidColorIn
        super(true, 4393423);
        register(MKUltra.MODID, "effect.poison_arrow");
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public void doEffect(Entity source, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {

        SpellCast particlePotion = ParticlePotion.Create(caster,
                EnumParticleTypes.SLIME.getParticleID(),
                ParticleEffects.SPHERE_MOTION, false, new Vec3d(1.0, 1.0, 1.0),
                new Vec3d(0.0, 1.0, 0.0), 40, 5, 1.0);

        AreaEffectBuilder.Create((EntityLivingBase) caster, target)
                .effect(new PotionEffect(MobEffects.POISON, 9 * 20, amplifier, false, true), getTargetType())
                .effect(new PotionEffect(MobEffects.SLOWNESS, 9 * 20, amplifier + 2, false, true), getTargetType())
                .spellCast(particlePotion, amplifier, getTargetType())
                .instant()
                .particle(EnumParticleTypes.SLIME)
                .color(16737305).radius(cast.getFloat("range"), true)
                .spawn();
    }
}