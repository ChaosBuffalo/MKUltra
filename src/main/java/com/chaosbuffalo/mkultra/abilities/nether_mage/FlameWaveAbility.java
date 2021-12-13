package com.chaosbuffalo.mkultra.abilities.nether_mage;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.abilities.AbilityContext;
import com.chaosbuffalo.mkcore.abilities.AbilityTargetSelector;
import com.chaosbuffalo.mkcore.abilities.AbilityTargeting;
import com.chaosbuffalo.mkcore.abilities.MKAbility;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkcore.effects.MKParticleEffect;
import com.chaosbuffalo.mkcore.effects.SpellCast;
import com.chaosbuffalo.mkcore.effects.instant.SoundEffect;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.network.MKParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.serialization.attributes.FloatAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.IntAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.ResourceLocationAttribute;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.spells.FlameWaveEffect;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FlameWaveAbility extends MKAbility {
    public static final ResourceLocation CASTING_PARTICLES = new ResourceLocation(MKUltra.MODID, "flame_wave_casting");
    public static final ResourceLocation CAST_1_PARTICLES = new ResourceLocation(MKUltra.MODID, "flame_wave_cast_1");
    public static final ResourceLocation CAST_2_PARTICLES = new ResourceLocation(MKUltra.MODID, "flame_wave_cast_2");
    public static final FlameWaveAbility INSTANCE = new FlameWaveAbility();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<MKAbility> event) {
        event.getRegistry().register(INSTANCE);
    }

    protected final FloatAttribute base = new FloatAttribute("base", 6.0f);
    protected final FloatAttribute scale = new FloatAttribute("scale", 3.0f);
    protected final FloatAttribute modifierScaling = new FloatAttribute("modifierScaling", 1.0f);
    protected final IntAttribute baseDuration = new IntAttribute("baseDuration", 3);
    protected final IntAttribute scaleDuration = new IntAttribute("scaleDuration", 1);
    protected final FloatAttribute damageBoost = new FloatAttribute("damageBoost", 1.5f);
    protected final ResourceLocationAttribute cast_1_particles = new ResourceLocationAttribute("cast_1_particles", CAST_1_PARTICLES);
    protected final ResourceLocationAttribute cast_2_particles = new ResourceLocationAttribute("cast_2_particles", CAST_2_PARTICLES);


    public FlameWaveAbility(){
        super(MKUltra.MODID, "ability.flame_wave");
        setCooldownSeconds(20);
        setManaCost(8);
        setCastTime(GameConstants.TICKS_PER_SECOND / 2);
        addAttributes(base, scale, modifierScaling, baseDuration, scaleDuration, damageBoost, cast_1_particles, cast_2_particles);
        addSkillAttribute(MKAttributes.EVOCATION);
        casting_particles.setDefaultValue(CASTING_PARTICLES);
    }

    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {
        int level = getSkillLevel(entityData.getEntity(), MKAttributes.EVOCATION);
        ITextComponent dmgStr = getDamageDescription(entityData, CoreDamageTypes.FireDamage, base.getValue(), scale.getValue(),
                level, modifierScaling.getValue());
        int dur = baseDuration.getValue() + scaleDuration.getValue() * level;
        float mult = damageBoost.getValue() * 100.0f;
        return new TranslationTextComponent(getDescriptionTranslationKey(), dmgStr, mult, dur);
    }

    @Override
    public float getDistance(LivingEntity entity) {
        return 5.0f + 2.0f * getSkillLevel(entity, MKAttributes.EVOCATION);
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.ENEMY;
    }


    @Override
    public AbilityTargetSelector getTargetSelector() {
        return AbilityTargeting.PBAOE;
    }

    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_fire_7;
    }

    @Nullable
    @Override
    public SoundEvent getCastingSoundEvent() {
        return ModSounds.casting_fire;
    }

    @Override
    public void endCast(LivingEntity entity, IMKEntityData data, AbilityContext context) {
        super.endCast(entity, data, context);
        int level = getSkillLevel(entity, MKAttributes.EVOCATION);

        SpellCast flames = FlameWaveEffect.Create(entity, base.getValue(), scale.getValue(), modifierScaling.getValue(),
                baseDuration.getValue(), scaleDuration.getValue(), damageBoost.getValue());

        SpellCast particles = MKParticleEffect.Create(entity, cast_2_particles.getValue(),
                false, new Vector3d(0.0, 1.0, 0.0));

        AreaEffectBuilder.Create(entity, entity)
                .spellCast(flames, level, getTargetContext())
                .spellCast(particles, level, getTargetContext())
                .spellCast(SoundEffect.Create(entity, ModSounds.spell_fire_1, entity.getSoundCategory()),
                        1, getTargetContext())
                .instant().color(16737305).radius(getDistance(entity), true)
                .particle(ParticleTypes.LAVA)
                .spawn();

        PacketHandler.sendToTrackingAndSelf(new MKParticleEffectSpawnPacket(
                        new Vector3d(0.0, 1.0, 0.0), cast_1_particles.getValue(), entity.getEntityId()),
                entity);
    }
}