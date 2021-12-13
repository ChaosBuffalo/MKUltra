package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkcore.MKCore;
import com.chaosbuffalo.mkcore.core.damage.MKDamageSource;
import com.chaosbuffalo.mkcore.effects.SpellCast;
import com.chaosbuffalo.mkcore.effects.SpellEffectBase;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.abilities.nether_mage.EmberAbility;
import com.chaosbuffalo.mkultra.abilities.nether_mage.IgniteAbility;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IgniteEffect extends SpellEffectBase {
    public static final String SCALING_CONTRIBUTION = "ignite_scaling";

    public static final IgniteEffect INSTANCE = new IgniteEffect();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Effect> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static SpellCast Create(Entity source, float baseDamage, float scaling, float modifierScaling) {
        return INSTANCE.newSpellCast(source).setScalingParameters(baseDamage, scaling)
                .setFloat(SCALING_CONTRIBUTION, modifierScaling);
    }

    private IgniteEffect() {
        super(EffectType.HARMFUL, 123);
        setRegistryName(MKUltra.MODID, "effect.ignite");
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.ENEMY;
    }

    @Override
    public void doEffect(Entity applier, Entity caster, LivingEntity target, int i, SpellCast spellCast) {
        float damage = spellCast.getScaledValue(i);
        float scaling = spellCast.getFloat(SCALING_CONTRIBUTION);
        target.attackEntityFrom(MKDamageSource.causeAbilityDamage(CoreDamageTypes.FireDamage,
                IgniteAbility.INSTANCE.getAbilityId(), applier, caster, scaling), damage);
        if (caster instanceof LivingEntity){
            MKCore.getEntityData(caster).ifPresent(data -> {
                EffectInstance burn = EmberAbility.INSTANCE.getBurnCast((LivingEntity) caster, target, data, i);
                target.addPotionEffect(burn);
            });
        }
    }
}
