package com.chaosbuffalo.mkultra.abilities.nether_mage;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.abilities.*;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.*;
import com.chaosbuffalo.mkcore.effects.utility.MKParticleEffect;
import com.chaosbuffalo.mkcore.effects.utility.SoundEffect;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.serialization.attributes.IntAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.ResourceLocationAttribute;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.ResistanceEffects;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

public class FireArmorAbility extends MKAbility {
    public static final ResourceLocation CASTING_PARTICLES = new ResourceLocation(MKUltra.MODID, "fire_armor_casting");
    public static final ResourceLocation CAST_PARTICLES = new ResourceLocation(MKUltra.MODID, "fire_armor_cast");
    public static final FireArmorAbility INSTANCE = new FireArmorAbility();

    protected final IntAttribute baseDuration = new IntAttribute("baseDuration", 60);
    protected final IntAttribute scaleDuration = new IntAttribute("scaleDuration", 15);
    protected final ResourceLocationAttribute cast_particles = new ResourceLocationAttribute("cast_particles", CAST_PARTICLES);

    private FireArmorAbility() {
        super(MKUltra.MODID, "ability.fire_armor");
        setCooldownSeconds(150);
        setManaCost(12);
        setCastTime(GameConstants.TICKS_PER_SECOND);
        addSkillAttribute(MKAttributes.ABJURATION);
        addAttributes(baseDuration, scaleDuration, cast_particles);
        casting_particles.setDefaultValue(CASTING_PARTICLES);
    }

    @Override
    public float getDistance(LivingEntity entity) {
        return 10.0f;
    }

    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {
        float level = getSkillLevel(entityData.getEntity(), MKAttributes.ABJURATION);
        float amount = ResistanceEffects.FIRE_ARMOR.getPerLevel() * (level + 1) * 100.0f;
        int duration = getBuffDuration(entityData, level, baseDuration.value(), scaleDuration.value()) / GameConstants.TICKS_PER_SECOND;
        return new TranslationTextComponent(getDescriptionTranslationKey(), amount,
                CoreDamageTypes.FireDamage.getDisplayName().mergeStyle(CoreDamageTypes.FireDamage.getFormatting()), duration);
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_buff_5;
    }

    @Override
    public void endCast(LivingEntity entity, IMKEntityData data, AbilityContext context) {
        super.endCast(entity, data, context);
        float level = getSkillLevel(entity, MKAttributes.ABJURATION);
        int duration = getBuffDuration(data, level, baseDuration.value(), scaleDuration.value());

        int oldAmp = Math.round(level);
        EffectInstance fireResist = new EffectInstance(Effects.FIRE_RESISTANCE, duration, oldAmp, false, false, true, null);
        EffectInstance absorb = new EffectInstance(Effects.ABSORPTION, duration, oldAmp, false, false, true, null);

        MKEffectBuilder<?> particles = MKParticleEffect.from(entity, cast_particles.getValue(), true, new Vector3d(0.0, 1.0, 0.0))
                .ability(this);

        MKEffectBuilder<?> sound = SoundEffect.from(entity, ModSounds.spell_fire_2, entity.getSoundCategory())
                .ability(this);

        MKEffectBuilder<?> fireArmor = ResistanceEffects.FIRE_ARMOR.builder(entity.getUniqueID())
                .ability(this)
                .timed(duration)
                .skillLevel(level);

        AreaEffectBuilder.createOnCaster(entity)
                .effect(fireResist, getTargetContext())
                .effect(absorb, getTargetContext())
                .effect(sound, getTargetContext())
                .effect(fireArmor, getTargetContext())
                .effect(particles, getTargetContext())
                .disableParticle()
                .instant()
                .color(16762905)
                .radius(getDistance(entity), true)
                .spawn();
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.FRIENDLY;
    }

    @Override
    public AbilityTargetSelector getTargetSelector() {
        return AbilityTargeting.PBAOE;
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
