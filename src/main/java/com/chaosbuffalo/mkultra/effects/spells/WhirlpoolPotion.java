package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.MKDamageSource;
import com.chaosbuffalo.mkultra.core.abilities.wave_knight.Whirlpool;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPeriodicPotionBase;
import com.chaosbuffalo.mkultra.effects.SpellTriggers;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Jacob on 3/25/2018.
 */
@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class WhirlpoolPotion extends SpellPeriodicPotionBase {

    private static final int DEFAULT_PERIOD = 20;

    public static final WhirlpoolPotion INSTANCE = new WhirlpoolPotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private WhirlpoolPotion() {
        super(DEFAULT_PERIOD, true, 1665535);
        setPotionName("effect.whirlpool");
        SpellTriggers.FALL.register(this::onFall);
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/whirlpool.png");
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public void doEffect(Entity source, Entity indirectSource, EntityLivingBase target, int amplifier, SpellCast cast) {
        target.attackEntityFrom(MKDamageSource.causeIndirectMagicDamage(new Whirlpool().getAbilityId(),
                source, indirectSource), amplifier * 2.0f);
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.WATER_SPLASH.getParticleID(),
                        ParticleEffects.CIRCLE_MOTION, 25, 10,
                        target.posX, target.posY + 1.0f,
                        target.posZ, 1.0, 1.0, 1.0, 2.5,
                        target.getLookVec()),
                target.dimension, target.posX,
                target.posY, target.posZ, 50.0f);
    }

    private void onFall(LivingHurtEvent event, DamageSource source, EntityLivingBase entity) {
        if (entity.isPotionActive(WhirlpoolPotion.INSTANCE)) {
            PotionEffect potion = entity.getActivePotionEffect(WhirlpoolPotion.INSTANCE);
            entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(source.getImmediateSource(),
                    source.getTrueSource()), 8.0f * potion.getAmplifier());
        }
    }
}

