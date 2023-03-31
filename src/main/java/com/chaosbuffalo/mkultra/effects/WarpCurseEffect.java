package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkcore.core.CastInterruptReason;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.damage.MKDamageSource;
import com.chaosbuffalo.mkcore.effects.MKActiveEffect;
import com.chaosbuffalo.mkcore.effects.MKEffect;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.effects.ScalingValueEffectState;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.utils.EntityUtils;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.init.ModSounds;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

public class WarpCurseEffect extends MKEffect {
    public static final int DEFAULT_PERIOD = 40;

    public static final WarpCurseEffect INSTANCE = new WarpCurseEffect();

    public static MKEffectBuilder<?> from(LivingEntity source, float base, float scaling, float modifier, ResourceLocation castParticles) {
        return INSTANCE.builder(source).state(s -> {
                    s.setEffectParticles(castParticles);
                    s.setScalingParameters(base, scaling, modifier);
                })
                .periodic(DEFAULT_PERIOD);
    }

    private WarpCurseEffect() {
        super(MobEffectCategory.HARMFUL);
        setRegistryName(MKUltra.MODID, "effect.warp_curse");
    }

    @Override
    public State makeState() {
        return new State();
    }

    @Override
    public MKEffectBuilder<State> builder(UUID sourceId) {
        return new MKEffectBuilder<>(this, sourceId, this::makeState);
    }

    @Override
    public MKEffectBuilder<State> builder(LivingEntity sourceEntity) {
        return new MKEffectBuilder<>(this, sourceEntity, this::makeState);
    }

    public static class State extends ScalingValueEffectState {

        @Override
        public boolean performEffect(IMKEntityData targetData, MKActiveEffect activeEffect) {
            LivingEntity target = targetData.getEntity();

            float damage = getScaledValue(activeEffect.getStackCount(), activeEffect.getSkillLevel());
            target.hurt(MKDamageSource.causeAbilityDamage(CoreDamageTypes.ShadowDamage,
                    activeEffect.getAbilityId(), activeEffect.getDirectEntity(), activeEffect.getSourceEntity(),
                    getModifierScale()), damage);

            SoundUtils.serverPlaySoundAtEntity(target, ModSounds.spell_fire_5, target.getSoundSource());
            boolean hasTeleported = false;
            int attempts = 5;
            while (!hasTeleported && attempts > 0){
                Vec3 targetOrigin = target.position();
                double nextX = targetOrigin.x + (target.getRandom().nextInt(8) - target.getRandom().nextInt(8));
                double nextY = targetOrigin.y + 1.0;
                double nextZ = targetOrigin.z + (target.getRandom().nextInt(8) - target.getRandom().nextInt(8));
                hasTeleported = EntityUtils.safeTeleportEntity(target, new Vec3(nextX, nextY, nextZ));
                attempts--;
            }
            targetData.getAbilityExecutor().interruptCast(CastInterruptReason.Teleport);
            sendEffectParticles(target);
            return true;
        }
    }

    @SuppressWarnings("unused")
    @Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    private static class RegisterMe {
        @SubscribeEvent
        public static void register(RegistryEvent.Register<MKEffect> event) {
            event.getRegistry().register(INSTANCE);
        }
    }
}
