package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.core.damage.MKDamageSource;
import com.chaosbuffalo.mkcore.effects.SpellCast;
import com.chaosbuffalo.mkcore.effects.SpellPeriodicEffectBase;
import com.chaosbuffalo.mkcore.fx.ParticleEffects;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.network.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.abilities.nether_mage.IgniteAbility;
import com.chaosbuffalo.mkweapons.MKWeapons;
import com.chaosbuffalo.mkweapons.init.MKWeaponsParticles;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;


@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SeverTendonEffect extends SpellPeriodicEffectBase {
    public static final String SCALING_CONTRIBUTION = "bleed.scaling_contribution";

    private static final UUID modUUID = UUID.fromString("bde03af5-32ed-4f6b-9f2c-c23296d60fa8");
    public static final SeverTendonEffect INSTANCE = new SeverTendonEffect();

    protected SeverTendonEffect() {
        super(GameConstants.TICKS_PER_SECOND * 2, EffectType.HARMFUL, 123);
        setRegistryName(MKUltra.MODID, "effect.sever_tendon");
        addAttributesModifier(Attributes.MOVEMENT_SPEED, modUUID.toString(), -0.05, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Effect> event) {
        event.getRegistry().register(INSTANCE);
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.ENEMY;
    }

    public static SpellCast Create(Entity source, float baseDamage, float scaling) {
        return Create(source, baseDamage, scaling, 1.0f);
    }

    public static SpellCast Create(Entity source, float baseDamage, float scaling, float modifierScaling) {
        return INSTANCE.newSpellCast(source).setScalingParameters(baseDamage, scaling)
                .setFloat(SCALING_CONTRIBUTION, modifierScaling);
    }

    @Override
    public void doEffect(Entity applier, Entity caster, LivingEntity target, int i, SpellCast spellCast) {
        float damage = spellCast.getScaledValue(i);
        target.attackEntityFrom(MKDamageSource.causeAbilityDamage(CoreDamageTypes.BleedDamage, IgniteAbility.INSTANCE.getAbilityId(),
                applier, caster,  spellCast.getFloat(SCALING_CONTRIBUTION)), damage);
        PacketHandler.sendToTrackingAndSelf(
                new ParticleEffectSpawnPacket(
                        MKWeaponsParticles.DRIPPING_BLOOD,
                        ParticleEffects.DIRECTED_SPOUT, 8, 1,
                        target.getPosX(), target.getPosY() + target.getHeight() * .75,
                        target.getPosZ(), target.getWidth() / 2.0, 0.5, target.getWidth() / 2.0, 3,
                        target.getUpVector(0)), target);
    }
}