package com.chaosbuffalo.mkultra.abilities.green_knight;

import com.chaosbuffalo.mkcore.abilities.AbilityTargetSelector;
import com.chaosbuffalo.mkcore.abilities.AbilityTargeting;
import com.chaosbuffalo.mkcore.abilities.MKAbility;
import com.chaosbuffalo.mkcore.abilities.MKToggleAbility;
import com.chaosbuffalo.mkcore.abilities.ai.conditions.NeedsBuffCondition;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.MKEffect;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.network.MKParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.serialization.attributes.ResourceLocationAttribute;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.SkinLikeWoodEffect;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

public class SkinLikeWoodAbility extends MKToggleAbility {
    public static final ResourceLocation CASTING_PARTICLES = new ResourceLocation(MKUltra.MODID, "skin_like_wood_casting");
    public static final ResourceLocation CAST_PARTICLES = new ResourceLocation(MKUltra.MODID, "skin_like_wood_cast");
    public static final SkinLikeWoodAbility INSTANCE = new SkinLikeWoodAbility();

    protected final ResourceLocationAttribute cast_particles = new ResourceLocationAttribute("cast_particles", CAST_PARTICLES);

    private SkinLikeWoodAbility() {
        super(new ResourceLocation(MKUltra.MODID, "ability.skin_like_wood"));
        setCooldownSeconds(3);
        setManaCost(2);
        addAttributes(cast_particles);
        addSkillAttribute(MKAttributes.ABJURATION);
        setUseCondition(new NeedsBuffCondition(this, SkinLikeWoodEffect.INSTANCE).setSelfOnly(true));
        casting_particles.setDefaultValue(CASTING_PARTICLES);
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.SELF;
    }

    @Override
    public AbilityTargetSelector getTargetSelector() {
        return AbilityTargeting.SELF;
    }

    @Override
    public MKEffect getToggleEffect() {
        return SkinLikeWoodEffect.INSTANCE;
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return null;
    }

    @Override
    public void applyEffect(LivingEntity entity, IMKEntityData entityData) {
        super.applyEffect(entity, entityData);
        float level = getSkillLevel(entity, MKAttributes.ABJURATION);
        SoundUtils.serverPlaySoundAtEntity(entity, ModSounds.spell_earth_7, entity.getSoundCategory());

        MKEffectBuilder<?> instance = getToggleEffect().builder(entity)
                .ability(this)
                .skillLevel(level)
                .infinite();
        entityData.getEffects().addEffect(instance);

        PacketHandler.sendToTrackingAndSelf(new MKParticleEffectSpawnPacket(
                        new Vector3d(0.0, 1.0, 0.0), cast_particles.getValue(),
                        entity.getEntityId()), entity);
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
