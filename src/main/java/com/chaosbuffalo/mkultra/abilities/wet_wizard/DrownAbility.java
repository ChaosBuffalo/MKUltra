package com.chaosbuffalo.mkultra.abilities.wet_wizard;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.abilities.*;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.serialization.attributes.FloatAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.IntAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.ResourceLocationAttribute;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.abilities.nether_mage.EmberAbility;
import com.chaosbuffalo.mkultra.effects.DrownEffect;
import com.chaosbuffalo.mkultra.entities.projectiles.DrownProjectileEntity;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class DrownAbility extends MKAbility {
    public static final ResourceLocation CASTING_PARTICLES = new ResourceLocation(MKUltra.MODID, "ember_casting");
    public static final ResourceLocation TICK_PARTICLES = new ResourceLocation(MKUltra.MODID, "drown_effect");
    public static final DrownAbility INSTANCE = new DrownAbility();

    protected final FloatAttribute baseDot = new FloatAttribute("baseBurnDamage", 4.0f);
    protected final FloatAttribute scaleDot = new FloatAttribute("scaleBurnDamage", 2.0f);
    protected final IntAttribute baseDuration = new IntAttribute("baseDuration", 10);
    protected final IntAttribute scaleDuration = new IntAttribute("scaleDuration", 2);
    protected final FloatAttribute projectileSpeed = new FloatAttribute("projectileSpeed", 0.9f);
    protected final FloatAttribute projectileInaccuracy = new FloatAttribute("projectileInaccuracy", 0.2f);
    protected final FloatAttribute dotModifierScaling = new FloatAttribute("dotModifierScaling", 0.2f);
    protected final ResourceLocationAttribute tick_particles = new ResourceLocationAttribute("burn_cast_particles", TICK_PARTICLES);


    public DrownAbility() {
        super(MKUltra.MODID, "ability.drown");
        setCooldownSeconds(10);
        setManaCost(5);
        setCastTime(GameConstants.TICKS_PER_SECOND);
        addAttributes(baseDuration, scaleDuration, baseDot, scaleDot, dotModifierScaling, tick_particles);
        addSkillAttribute(MKAttributes.CONJURATION);
        casting_particles.setDefaultValue(CASTING_PARTICLES);
    }

    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {
        float level = getSkillLevel(entityData.getEntity(), MKAttributes.CONJURATION);
        ITextComponent dotStr = getDamageDescription(entityData,
                CoreDamageTypes.NatureDamage, baseDot.value(), scaleDot.value(), level, dotModifierScaling.value());
        return new TranslationTextComponent(getDescriptionTranslationKey(),
                NUMBER_FORMATTER.format(convertDurationToSeconds(getBuffDuration(entityData, level, baseDuration.value(), scaleDuration.value()))),
                dotStr, NUMBER_FORMATTER.format(convertDurationToSeconds(DrownEffect.DEFAULT_PERIOD)));
    }

    public MKEffectBuilder<?> getDotEffect(IMKEntityData casterData, float level) {
        int durTicks = getBuffDuration(casterData, level, baseDuration.value(), scaleDuration.value());
        return DrownEffect.from(casterData.getEntity(), baseDot.value(), scaleDot.value(),
                dotModifierScaling.value(), tick_particles.getValue())
                .ability(this)
                .skillLevel(level)
                .timed(durTicks);
    }

    @Override
    public float getDistance(LivingEntity entity) {
        return 50.0f;
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.ENEMY;
    }

    @Override
    public AbilityTargetSelector getTargetSelector() {
        return AbilityTargeting.PROJECTILE;
    }

    @Override
    public SoundEvent getCastingSoundEvent() {
        return ModSounds.casting_water;
    }

    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_water_7;
    }

    @Override
    public void endCast(LivingEntity entity, IMKEntityData data, AbilityContext context) {
        super.endCast(entity, data, context);
        float level = getSkillLevel(entity, MKAttributes.CONJURATION);
        DrownProjectileEntity proj = new DrownProjectileEntity(entity.world);
        proj.setShooter(entity);
        proj.setSkillLevel(level);
        shootProjectile(proj, projectileSpeed.value(), projectileInaccuracy.value(), entity, context);
        entity.world.addEntity(proj);
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
