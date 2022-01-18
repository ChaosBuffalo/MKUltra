package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkcore.core.CastInterruptReason;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.effects.MKActiveEffect;
import com.chaosbuffalo.mkcore.effects.MKEffect;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.effects.ScalingValueEffectState;
import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.potion.EffectType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

public class YankEffect extends MKEffect {

    public static final YankEffect INSTANCE = new YankEffect();

    private YankEffect() {
        super(EffectType.NEUTRAL);
        setRegistryName(MKUltra.MODID, "effect.yank");
    }

    public static MKEffectBuilder<?> from(LivingEntity source, float base, float scale, Vector3d sourcePos) {
        return INSTANCE.builder(source).state(s -> {
            s.setScalingParameters(base, scale, 0.0f);
            s.setPos(sourcePos);
        });
    }

    @Override
    public MKEffectBuilder<State> builder(UUID sourceId) {
        return new MKEffectBuilder<>(this, sourceId, this::makeState);
    }

    @Override
    public MKEffectBuilder<State> builder(LivingEntity sourceEntity) {
        return new MKEffectBuilder<>(this, sourceEntity, this::makeState);
    }

    @Override
    public State makeState() {
        return new State();
    }

    public static class State extends ScalingValueEffectState {
        protected Vector3d pos;

        @Override
        public boolean performEffect(IMKEntityData targetData, MKActiveEffect activeEffect) {
            LivingEntity target = targetData.getEntity();
            Vector3d targetPos = new Vector3d(target.getPosX(), target.getPosYHeight(0.6f), target.getPosZ());
            float force = getScaledValue(activeEffect.getStackCount(), activeEffect.getSkillLevel());
            float heightRatio = 1.0f;
            if (activeEffect.getSourceEntity() != null){
                heightRatio = activeEffect.getSourceEntity().getHeight() / target.getHeight();
            }
            force *= heightRatio;
            targetData.getAbilityExecutor().interruptCast(CastInterruptReason.Jump);
            Vector3d awayFrom = pos.subtract(targetPos).normalize().scale(force);
            target.addVelocity(awayFrom.getX(), awayFrom.getY(), awayFrom.getZ());
            if (target instanceof ServerPlayerEntity){
                ((ServerPlayerEntity) target).connection.sendPacket(new SEntityVelocityPacket(target));
            }
            return true;
        }

        public void setPos(Vector3d pos) {
            this.pos = pos;
        }

        public Vector3d getPos() {
            return pos;
        }

        @Override
        public void deserializeStorage(CompoundNBT stateTag) {
            super.deserializeStorage(stateTag);
            pos = new Vector3d(stateTag.getDouble("posX"), stateTag.getDouble("posY"), stateTag.getDouble("posZ"));
        }

        @Override
        public void serializeStorage(CompoundNBT stateTag) {
            super.serializeStorage(stateTag);
            stateTag.putDouble("posX", pos.getX());
            stateTag.putDouble("posY", pos.getY());
            stateTag.putDouble("posZ", pos.getZ());
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