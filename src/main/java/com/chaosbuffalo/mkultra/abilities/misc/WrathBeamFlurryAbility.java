package com.chaosbuffalo.mkultra.abilities.misc;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.abilities.AbilityContext;
import com.chaosbuffalo.mkcore.abilities.AbilityTargetSelector;
import com.chaosbuffalo.mkcore.abilities.AbilityTargeting;
import com.chaosbuffalo.mkcore.abilities.MKAbility;
import com.chaosbuffalo.mkcore.abilities.ai.conditions.MeleeUseCondition;
import com.chaosbuffalo.mkcore.core.AbilityType;
import com.chaosbuffalo.mkcore.core.CastInterruptReason;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.serialization.attributes.IntAttribute;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.Targeting;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;

public class WrathBeamFlurryAbility extends MKAbility {
    public static final ResourceLocation CASTING_PARTICLES = new ResourceLocation(MKUltra.MODID, "flame_wave_casting");
    public static WrathBeamFlurryAbility INSTANCE = new WrathBeamFlurryAbility();
    protected final IntAttribute tickRate = new IntAttribute("tickRate", GameConstants.TICKS_PER_SECOND / 2);

    private WrathBeamFlurryAbility() {
        super(MKUltra.MODID, "ability.wrath_beam_flurry");
        setCastTime(GameConstants.TICKS_PER_SECOND * 2);
        addAttributes(tickRate);
        setCooldownSeconds(5);
        setManaCost(10);
        addSkillAttribute(MKAttributes.EVOCATION);
        setUseCondition(new MeleeUseCondition(this));
        casting_particles.setDefaultValue(CASTING_PARTICLES);
    }

    @Override
    public AbilityType getType() {
        return AbilityType.Ultimate;
    }

    @Override
    public boolean canApplyCastingSpeedModifier() {
        return false;
    }

    @Override
    public boolean isInterruptedBy(IMKEntityData targetData, CastInterruptReason reason) {
        return false;
    }

    @Override
    public AbilityTargetSelector getTargetSelector() {
        return AbilityTargeting.PBAOE;
    }

    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {

        return new TranslationTextComponent(getDescriptionTranslationKey(),
                NUMBER_FORMATTER.format(getDistance(entityData.getEntity())),
                NUMBER_FORMATTER.format(convertDurationToSeconds(tickRate.value())));
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.ENEMY;
    }

    @Override
    public float getDistance(LivingEntity entity) {
        return 10.0f;
    }

    @Nullable
    @Override
    public SoundEvent getCastingSoundEvent() {
        return ModSounds.hostile_casting_fire;
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return null;
    }

    @Override
    public void continueCast(LivingEntity castingEntity, IMKEntityData casterData, int castTimeLeft, AbilityContext context) {
        super.continueCast(castingEntity, casterData, castTimeLeft, context);
        if (castTimeLeft % tickRate.value() == 0) {
            float dist = getDistance(castingEntity);
            Vector3d minBound = castingEntity.getPositionVec().subtract(dist, 1.0, dist);
            Vector3d maxBound = castingEntity.getPositionVec().add(dist, 4.0, dist);
            List<LivingEntity> entities = castingEntity.getEntityWorld().getEntitiesWithinAABB(LivingEntity.class,
                    new AxisAlignedBB(minBound, maxBound));
            for (LivingEntity ent : entities){
                if (Targeting.isValidTarget(getTargetContext(), castingEntity, ent)){
                    WrathBeamAbility.INSTANCE.castWrathBeam(castingEntity, ent.getPositionVec());
                }
            }
        }
    }

    @SuppressWarnings("unused")
    @Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    private static class RegisterMe {
        @SubscribeEvent
        public static void register(RegistryEvent.Register<MKAbility> event) {
            event.getRegistry().register(INSTANCE);
        }
    }
}
