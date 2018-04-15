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
public class SmitePotion extends SpellPotionBase {

    public static final SmitePotion INSTANCE = new SmitePotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static SpellCast Create(Entity source, EntityLivingBase target, float damageBase, float damageScale) {
        return INSTANCE.newSpellCast(source).setTarget(target).setScalingParameters(damageBase, damageScale);
    }

    private SmitePotion() {
        // boolean isBadEffectIn, int liquidColorIn
        super(true, 4393481);
        register(MKUltra.MODID, "effect.smite");
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {

        float damage = cast.getScaledValue(amplifier);
        if (target.isEntityUndead()) {
            target.attackEntityFrom(DamageSource.causeIndirectMagicDamage(applier, caster), 2.0f * damage);
        } else {
            target.attackEntityFrom(DamageSource.causeIndirectMagicDamage(applier, caster), damage);
        }
    }
}
