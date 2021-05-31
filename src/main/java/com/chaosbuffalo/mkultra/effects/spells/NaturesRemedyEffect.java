package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkcore.core.healing.MKHealSource;
import com.chaosbuffalo.mkcore.core.healing.MKHealing;
import com.chaosbuffalo.mkcore.effects.SpellCast;
import com.chaosbuffalo.mkcore.effects.SpellPeriodicEffectBase;
import com.chaosbuffalo.mkcore.fx.ParticleEffects;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.network.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.abilities.green_knight.NaturesRemedyAbility;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Created by Jacob on 7/28/2018.
 */
@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NaturesRemedyEffect extends SpellPeriodicEffectBase {

    private static final int DEFAULT_PERIOD = 20;

    public static final NaturesRemedyEffect INSTANCE = new NaturesRemedyEffect();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Effect> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static SpellCast Create(Entity source, LivingEntity target, float base, float scaling) {
        return Create(source, base, scaling).setTarget(target);
    }

    public static SpellCast Create(Entity source, float base, float scaling) {
        return INSTANCE.newSpellCast(source)
                .setScalingParameters(base, scaling);
    }

    private NaturesRemedyEffect() {
        // boolean isBadEffectIn, int liquidColorIn
        super(DEFAULT_PERIOD, EffectType.BENEFICIAL, 4393481);
        setRegistryName(MKUltra.MODID, "effect.natures_remedy");
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.FRIENDLY;
    }


    @Override
    public void doEffect(Entity applier, Entity caster, LivingEntity target, int amplifier, SpellCast cast) {

        float value = cast.getScaledValue(amplifier);
        MKHealing.healEntityFrom(target, value,
                MKHealSource.getNatureHeal(NaturesRemedyAbility.INSTANCE.getAbilityId(), applier, caster,
                        NaturesRemedyAbility.INSTANCE.getModifierScaling()));

        PacketHandler.sendToTrackingAndSelf(
                new ParticleEffectSpawnPacket(
                        ParticleTypes.ITEM_SLIME,
                        ParticleEffects.SPHERE_MOTION, 30, 10,
                        target.getPosX(), target.getPosY() + 1.0f,
                        target.getPosZ(), 1.0, 1.0, 1.0, .5,
                        target.getLookVec()), target);
    }
}
