package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
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
public class MobHealPotion extends SpellPotionBase {

    public static final MobHealPotion INSTANCE = new MobHealPotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source, EntityLivingBase target, float base, float scaling) {
        return Create(source, base, scaling).setTarget(target);
    }

    public static SpellCast Create(Entity source, float base, float scaling) {
        return INSTANCE.newSpellCast(source)
                .setScalingParameters(base, scaling);
    }

    private MobHealPotion() {
        // boolean isBadEffectIn, int liquidColorIn
        super(false, 4393481);
        setPotionName("effect.mob_heal");
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.FRIENDLY;
    }

    @Override
    public boolean isValidTarget(Targeting.TargetType targetType, Entity caster,
                                 EntityLivingBase target, boolean excludeCaster) {
        return super.isValidTarget(targetType, caster, target, excludeCaster);
    }

    @Override
    public boolean canSelfCast() {
        return true;
    }

    @Override
    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {
        target.heal(cast.getScaledValue(amplifier));
    }
}