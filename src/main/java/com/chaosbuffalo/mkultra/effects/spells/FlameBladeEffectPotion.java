package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.MKDamageSource;
import com.chaosbuffalo.mkultra.core.abilities.druid.FlameBlade;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class FlameBladeEffectPotion extends SpellPotionBase {

    public static final FlameBladeEffectPotion INSTANCE = new FlameBladeEffectPotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source, float baseDamage, float scaling) {
        return INSTANCE.newSpellCast(source).setScalingParameters(baseDamage, scaling);
    }

    private FlameBladeEffectPotion() {
        super(true, 123);
        setPotionName("effect.flame_blade_effect");
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {

        float damage = cast.getScaledValue(amplifier);
        if (target.isBurning()) {
            damage = damage * 2.0f;
        }

        target.attackEntityFrom(MKDamageSource.causeIndirectMagicDamage(
                new FlameBlade().getAbilityId(), applier, caster), damage);
    }
}