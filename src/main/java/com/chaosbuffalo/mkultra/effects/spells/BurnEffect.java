package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkcore.effects.SpellCast;
import com.chaosbuffalo.mkcore.effects.status.DamageTypeDotEffect;
import com.chaosbuffalo.mkcore.fx.ParticleEffects;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.network.MKParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.network.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.abilities.nether_mage.EmberAbility;
import com.chaosbuffalo.mkultra.init.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BurnEffect extends DamageTypeDotEffect {


    public static final BurnEffect INSTANCE = new BurnEffect(new ResourceLocation(MKUltra.MODID, "effect.burn"));
    public static final String BURN_PARTICLES = "burn_particles";

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Effect> event) {
        event.getRegistry().register(INSTANCE);
    }


    public BurnEffect(ResourceLocation loc){
        super(() -> CoreDamageTypes.FireDamage, 40, 0xffff0000);
        setRegistryName(loc);
    }

    public static SpellCast Create(Entity source, float base, float scaling, float modifierScaling,
                                   ResourceLocation castParticles) {
        return DamageTypeDotEffect.Create(INSTANCE, source, base, scaling, modifierScaling)
                .setResourceLocation(BURN_PARTICLES, castParticles);
    }

    @Override
    public void doEffect(Entity applier, Entity caster, LivingEntity target, int i, SpellCast spellCast) {
        super.doEffect(applier, caster, target, i, spellCast);
        SoundUtils.serverPlaySoundAtEntity(target, ModSounds.spell_fire_6, target.getSoundCategory());
        PacketHandler.sendToTrackingAndSelf(new MKParticleEffectSpawnPacket(
                        new Vector3d(0.0, 1.0, 0.0),
                        spellCast.getResourceLocation(BURN_PARTICLES), target.getEntityId()),
                target);
    }
}
