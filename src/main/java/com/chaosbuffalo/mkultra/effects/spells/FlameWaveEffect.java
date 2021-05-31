package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.core.damage.MKDamageSource;
import com.chaosbuffalo.mkcore.effects.SpellCast;
import com.chaosbuffalo.mkcore.effects.SpellEffectBase;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.abilities.MKUAbilityUtils;
import com.chaosbuffalo.mkultra.abilities.nether_mage.FlameWaveAbility;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class FlameWaveEffect extends SpellEffectBase {
    public static final String SCALING_CONTRIBUTION = "modifier_scaling";
    public static final String WITHER_DUR_BASE = "wither_dur_base";
    public static final String WITHER_DUR_SCALE = "wither_dur_scale";
    public static final String DAMAGE_BOOST = "damage_boost";

    public static final FlameWaveEffect INSTANCE = new FlameWaveEffect();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Effect> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static SpellCast Create(Entity source, float baseDamage, float scaling, float modifierScaling,
                                   int witherBase, int witherScale, float damageMultiplier) {
        return INSTANCE.newSpellCast(source).setScalingParameters(baseDamage, scaling)
                .setFloat(SCALING_CONTRIBUTION, modifierScaling).setInt(WITHER_DUR_BASE, witherBase)
                .setInt(WITHER_DUR_SCALE, witherScale).setFloat(DAMAGE_BOOST, damageMultiplier);
    }

    private FlameWaveEffect() {
        super(EffectType.HARMFUL, 123);
        setRegistryName(MKUltra.MODID, "effect.flame_wave");
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.ENEMY;
    }

    @Override
    public void doEffect(Entity applier, Entity caster, LivingEntity target, int amplifier, SpellCast spellCast) {
        float damage = spellCast.getScaledValue(amplifier);
        if (MKUAbilityUtils.isBurning(target)){
            int dur = spellCast.getInt(WITHER_DUR_BASE) + amplifier * spellCast.getInt(WITHER_DUR_SCALE);
            EffectInstance witherEffect = new EffectInstance(Effects.WITHER,
                    dur * GameConstants.TICKS_PER_SECOND, 0);
            damage *= spellCast.getFloat(DAMAGE_BOOST);
            target.addPotionEffect(witherEffect);
        }
        target.attackEntityFrom(MKDamageSource.causeAbilityDamage(CoreDamageTypes.FireDamage,
                FlameWaveAbility.INSTANCE.getAbilityId(), applier, caster,
                spellCast.getFloat(SCALING_CONTRIBUTION)), damage);
    }
}