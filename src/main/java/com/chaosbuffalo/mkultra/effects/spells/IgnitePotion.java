package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.mkultra.effects.Targeting;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class IgnitePotion extends SpellPotionBase {

    public static final IgnitePotion INSTANCE = new IgnitePotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static SpellCast Create(Entity source, float baseDamage, float scaling) {
        return INSTANCE.newSpellCast(source).setScalingParameters(baseDamage, scaling);
    }

    private IgnitePotion() {
        super(true, 123);
        SpellPotionBase.register("effect.ignite", this);
    }


    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {

        float damage = cast.getScaledValue(amplifier);
        target.setFire(amplifier * 4);
        target.attackEntityFrom(DamageSource.causeIndirectMagicDamage(applier, caster), damage);
        Vec3d lookVec = target.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.FLAME.getParticleID(),
                        ParticleEffects.CIRCLE_PILLAR_MOTION, 60, 10,
                        target.posX, target.posY + 1.0,
                        target.posZ, 1.0, 1.0, 1.0, 1.0,
                        lookVec),
                target.dimension, target.posX,
                target.posY, target.posZ, 50.0f);
    }
}
