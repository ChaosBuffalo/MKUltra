package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkcore.core.damage.MKDamageSource;
import com.chaosbuffalo.mkcore.effects.SpellCast;
import com.chaosbuffalo.mkcore.effects.SpellPeriodicEffectBase;
import com.chaosbuffalo.mkcore.fx.ParticleEffects;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.network.MKParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.network.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.utils.EntityUtils;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.abilities.nether_mage.WarpCurseAbility;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WarpCurseEffect extends SpellPeriodicEffectBase {
    public static final String SCALING_CONTRIBUTION = "warp_curse_scaling";
    public static final String WARP_CURSE_PARTICLES = "warp_curse_particles";
    private static final int DEFAULT_PERIOD = 40;

    public static final WarpCurseEffect INSTANCE = new WarpCurseEffect();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Effect> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static SpellCast Create(Entity source, float base, float scaling, float modifier, ResourceLocation castParticles) {
        return INSTANCE.newSpellCast(source).setScalingParameters(base, scaling)
                .setFloat(SCALING_CONTRIBUTION, modifier)
                .setResourceLocation(WARP_CURSE_PARTICLES, castParticles);
    }

    private WarpCurseEffect() {
        super(DEFAULT_PERIOD, EffectType.HARMFUL, 4393423);
        setRegistryName(MKUltra.MODID, "effect.warp_curse");
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.ENEMY;
    }

    @Override
    public void doEffect(Entity applier, Entity caster, LivingEntity target, int amplifier, SpellCast spellCast) {
        Vector3d targetOrigin = target.getPositionVec();
        target.attackEntityFrom(MKDamageSource.causeAbilityDamage(CoreDamageTypes.ShadowDamage,
                WarpCurseAbility.INSTANCE.getAbilityId(), applier, caster,
                spellCast.getFloat(SCALING_CONTRIBUTION)), spellCast.getScaledValue(amplifier));
        SoundUtils.serverPlaySoundAtEntity(target, ModSounds.spell_fire_5, target.getSoundCategory());
        if (EntityUtils.canTeleportEntity(target)){
            double nextX = targetOrigin.x + (target.getRNG().nextInt(8) - target.getRNG().nextInt(8));
            double nextY = targetOrigin.y + 5.0;
            double nextZ = targetOrigin.z + (target.getRNG().nextInt(8) - target.getRNG().nextInt(8));
            EntityUtils.safeTeleportEntity(target, new Vector3d(nextX, nextY, nextZ));
        }
        PacketHandler.sendToTrackingAndSelf(new MKParticleEffectSpawnPacket(
                        new Vector3d(0.0, 1.0, 0.0),
                        spellCast.getResourceLocation(WARP_CURSE_PARTICLES), target.getEntityId()),
                target);
    }
}
