package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkcore.MKConfig;
import com.chaosbuffalo.mkcore.core.healing.MKHealSource;
import com.chaosbuffalo.mkcore.core.healing.MKHealing;
import com.chaosbuffalo.mkcore.effects.SpellCast;
import com.chaosbuffalo.mkcore.effects.SpellEffectBase;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.abilities.cleric.HealAbility;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;



@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClericHealEffect extends SpellEffectBase {

    public static final ClericHealEffect INSTANCE = new ClericHealEffect();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Effect> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static SpellCast Create(Entity source, LivingEntity target, float base, float scaling) {
        return Create(source, base, scaling).setTarget(target);
    }

    public static SpellCast Create(Entity source, float base, float scaling) {
        return INSTANCE.newSpellCast(source).setScalingParameters(base, scaling);
    }

    private ClericHealEffect() {
        // boolean isBadEffectIn, int liquidColorIn
        super(EffectType.BENEFICIAL, 4393481);
        setRegistryName(MKUltra.MODID, "effect.cleric_heal");
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.FRIENDLY;
    }

    @Override
    public boolean isValidTarget(TargetingContext targetContext, Entity caster, LivingEntity target) {
        return super.isValidTarget(targetContext, caster, target) ||
                (MKConfig.SERVER.healsDamageUndead.get() && target.isEntityUndead() &&
                        MKHealing.isEnemyUndead(caster, target));
    }


    @Override
    public void doEffect(Entity applier, Entity caster, LivingEntity target, int amplifier, SpellCast cast) {
        float value = cast.getScaledValue(amplifier);
        MKHealing.healEntityFrom(target, value,
                MKHealSource.getHolyHeal(HealAbility.INSTANCE.getAbilityId(), applier, caster,
                        HealAbility.INSTANCE.getModifierScaling().getValue()));
    }


}