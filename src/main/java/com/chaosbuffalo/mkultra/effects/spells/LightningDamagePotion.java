package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.MKDamageSource;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class LightningDamagePotion extends SpellPotionBase {

    public static ResourceLocation INDIRECT_LIGHTNING_DMG_ABILITY_ID = new ResourceLocation(
            MKUltra.MODID, "ability.instant_indirect_lightning_damage");

    public static final LightningDamagePotion INSTANCE = new LightningDamagePotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static SpellCast Create(Entity source, float baseDamage, float scaling) {
        return INSTANCE.newSpellCast(source).setScalingParameters(baseDamage, scaling);
    }

    private LightningDamagePotion() {
        super(true, 123);
        register(MKUltra.MODID, "effect.instant_lightning_damage");
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {

        float damage = cast.getScaledValue(amplifier);
        target.attackEntityFrom(MKDamageSource.causeIndirectMagicDamage(
                INDIRECT_LIGHTNING_DMG_ABILITY_ID, applier, caster), damage);
    }
}
