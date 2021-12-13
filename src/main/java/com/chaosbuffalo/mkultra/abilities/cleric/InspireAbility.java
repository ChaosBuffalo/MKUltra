package com.chaosbuffalo.mkultra.abilities.cleric;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.abilities.*;
import com.chaosbuffalo.mkcore.effects.MKParticleEffect;
import com.chaosbuffalo.mkcore.effects.SpellCast;
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

import java.util.Set;

@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class InspireAbility extends MKAbility {
    public static final ResourceLocation CASTING_PARTICLES = new ResourceLocation(MKUltra.MODID, "inspire_casting");
    public static final ResourceLocation CAST_PARTICLES = new ResourceLocation(MKUltra.MODID, "inspire_cast");
    public static final InspireAbility INSTANCE = new InspireAbility();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<MKAbility> event) {
        event.getRegistry().register(INSTANCE);
    }

    protected final IntAttribute base = new IntAttribute("baseDuration", 8);
    protected final IntAttribute scale = new IntAttribute("scaleDuration", 2);
    protected final ResourceLocationAttribute cast_particles = new ResourceLocationAttribute("cast_particles", CAST_PARTICLES);

    public InspireAbility(){
        super(MKUltra.MODID, "ability.inspire");
        setCooldownSeconds(35);
        setManaCost(8);
        setCastTime(GameConstants.TICKS_PER_SECOND * 2);
        addAttributes(base, scale, cast_particles);
        addSkillAttribute(MKAttributes.ALTERATON);
        casting_particles.setDefaultValue(CASTING_PARTICLES);

    }

    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {
        int level = getSkillLevel(entityData.getEntity(), MKAttributes.ALTERATON);
        int duration = getBuffDuration(entityData, level, base.getValue(), scale.getValue()) / GameConstants.TICKS_PER_SECOND;
        return new TranslationTextComponent(getDescriptionTranslationKey(), duration);
    }

    @Override
    public float getDistance(LivingEntity entity) {
        return 20.0f;
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
    public SoundEvent getCastingSoundEvent() {
        return ModSounds.casting_holy;
    }

    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_cast_12;
    }

    @Override
    public AbilityType getType() {
        return AbilityType.PooledUltimate;
    }

    @Override
    public void endCast(LivingEntity entity, IMKEntityData data, AbilityContext context) {
        super.endCast(entity, data, context);
        int level = getSkillLevel(entity, MKAttributes.ALTERATON);
        int duration = getBuffDuration(data, level, base.getValue(), scale.getValue());

        EffectInstance hasteEffect = new EffectInstance(Effects.HASTE, duration, level, false, false);
        EffectInstance regenEffect = new EffectInstance(Effects.REGENERATION, duration, level, false, false);
        SpellCast particles = MKParticleEffect.Create(entity, cast_particles.getValue(),
                true, new Vector3d(0.0, 1.0, 0.0));

        AreaEffectBuilder.Create(entity, entity)
                .effect(hasteEffect, getTargetContext())
                .effect(regenEffect, getTargetContext())
                .spellCast(SoundEffect.Create(entity, ModSounds.spell_holy_8, entity.getSoundCategory()),
                        1, getTargetContext())
                .spellCast(particles, level, getTargetContext())
                .instant().color(1034415).radius(getDistance(entity), true)
                .disableParticle()
                .spawn();
    }
}
