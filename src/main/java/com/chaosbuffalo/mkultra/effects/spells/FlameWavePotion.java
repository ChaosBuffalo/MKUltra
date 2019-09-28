package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.MKDamageSource;
import com.chaosbuffalo.mkultra.core.abilities.nether_mage.FlameWave;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class FlameWavePotion extends SpellPotionBase {

    public static final FlameWavePotion INSTANCE = new FlameWavePotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source, float baseDamage, float scaling) {
        return INSTANCE.newSpellCast(source).setScalingParameters(baseDamage, scaling);
    }

    private FlameWavePotion() {
        super(true, 123);
        setPotionName("effect.flame_wave");
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {

        float damage = cast.getScaledValue(amplifier);
        if (target.isBurning()) {
            target.addPotionEffect(new PotionEffect(MobEffects.WITHER, GameConstants.TICKS_PER_SECOND * 4));
            damage = damage * 1.5f;
        }
        target.attackEntityFrom(MKDamageSource.causeIndirectMagicDamage(
                new FlameWave().getAbilityId(), applier, caster), damage);
    }
}