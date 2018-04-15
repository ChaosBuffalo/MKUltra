package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.mkultra.api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class ClericHealPotion extends SpellPotionBase {

    public static final ClericHealPotion INSTANCE = new ClericHealPotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static SpellCast Create(Entity source, EntityLivingBase target, float base, float scaling) {
        return Create(source, base, scaling).setTarget(target);
    }

    public static SpellCast Create(Entity source, float base, float scaling) {
        return INSTANCE.newSpellCast(source)
                .setScalingParameters(base, scaling);
    }

    private ClericHealPotion() {
        // boolean isBadEffectIn, int liquidColorIn
        super(false, 4393481);
        register(MKUltra.MODID, "effect.cleric_heal");
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.FRIENDLY;
    }

    @Override
    public boolean isValidTarget(Targeting.TargetType targetType, Entity caster, EntityLivingBase target, boolean excludeCaster) {
        return super.isValidTarget(targetType, caster, target, excludeCaster) || target.isEntityUndead();
    }

    @Override
    public boolean canSelfCast() {
        return true;
    }

    @Override
    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {

        float value = cast.getScaledValue(amplifier);

        if (target.isEntityUndead()) {
            target.attackEntityFrom(DamageSource.causeIndirectMagicDamage(applier, caster), 2.0f * value);
        } else {
            target.heal(value);
        }
    }
}