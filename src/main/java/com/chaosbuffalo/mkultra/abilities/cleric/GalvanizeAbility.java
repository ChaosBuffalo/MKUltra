package com.chaosbuffalo.mkultra.abilities.cleric;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.abilities.*;
import com.chaosbuffalo.mkcore.effects.MKParticleEffect;
import com.chaosbuffalo.mkcore.effects.SpellCast;
import com.chaosbuffalo.mkcore.network.MKParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.serialization.attributes.IntAttribute;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkcore.effects.instant.SoundEffect;
import com.chaosbuffalo.mkcore.fx.ParticleEffects;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.network.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.serialization.attributes.ResourceLocationAttribute;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.spells.CureEffect;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
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

@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GalvanizeAbility extends MKAbility {
    public static final ResourceLocation CASTING_PARTICLES = new ResourceLocation(MKUltra.MODID, "galvanize_casting");
    public static final ResourceLocation CAST_1_PARTICLES = new ResourceLocation(MKUltra.MODID, "galvanize_cast_1");
    public static final ResourceLocation CAST_2_PARTICLES = new ResourceLocation(MKUltra.MODID, "galvanize_cast_2");
    public static final GalvanizeAbility INSTANCE = new GalvanizeAbility();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<MKAbility> event) {
        event.getRegistry().register(INSTANCE);
    }

    protected final IntAttribute base = new IntAttribute("baseDuration", 5);
    protected final IntAttribute scale = new IntAttribute("scaleDuration", 2);
    protected final ResourceLocationAttribute cast_1_particles = new ResourceLocationAttribute("cast_1_particles", CAST_1_PARTICLES);
    protected final ResourceLocationAttribute cast_2_particles = new ResourceLocationAttribute("cast_2_particles", CAST_2_PARTICLES);

    public GalvanizeAbility(){
        super(MKUltra.MODID, "ability.galvanize");
        setCooldownSeconds(25);
        setManaCost(8);
        setCastTime(GameConstants.TICKS_PER_SECOND / 4);
        addAttributes(base, scale, cast_1_particles, cast_2_particles);
        addSkillAttribute(MKAttributes.ABJURATION);
        casting_particles.setDefaultValue(CASTING_PARTICLES);
    }

    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {
        int level = getSkillLevel(entityData.getEntity(), MKAttributes.ABJURATION);
        int duration = getBuffDuration(entityData, level, base.getValue(), scale.getValue()) / GameConstants.TICKS_PER_SECOND;
        return new TranslationTextComponent(getDescriptionTranslationKey(), duration);
    }

    @Override
    public float getDistance(LivingEntity entity) {
        return 10.0f;
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.FRIENDLY;
    }


    @Override
    public AbilityTargetSelector getTargetSelector() {
        return AbilityTargeting.PBAOE;
    }

    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_heal_1;
    }

    @Override
    public void endCast(LivingEntity entity, IMKEntityData data, AbilityContext context) {
        super.endCast(entity, data, context);
        int level = getSkillLevel(entity, MKAttributes.ABJURATION);
        int duration = getBuffDuration(data, level, base.getValue(), scale.getValue());

        EffectInstance jump = new EffectInstance(Effects.JUMP_BOOST, duration, level, false, false);
        SpellCast particles = MKParticleEffect.Create(entity, cast_2_particles.getValue(),
                false, new Vector3d(0.0, 1.0, 0.0));

        AreaEffectBuilder.Create(entity, entity)
                .effect(jump, getTargetContext())
                .spellCast(CureEffect.Create(entity), level, getTargetContext())
                .spellCast(SoundEffect.Create(entity, ModSounds.spell_buff_5, entity.getSoundCategory()),
                        1, getTargetContext())
                .spellCast(particles, level, getTargetContext())
                .instant().color(1048370).radius(getDistance(entity), true)
                .disableParticle()
                .spawn();

        PacketHandler.sendToTrackingAndSelf(new MKParticleEffectSpawnPacket(
                        new Vector3d(0.0, 1.0, 0.0), cast_1_particles.getValue(), entity.getEntityId()),
                entity);
    }
}