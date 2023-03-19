package com.chaosbuffalo.mkultra.abilities.necromancer;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.abilities.AbilityContext;
import com.chaosbuffalo.mkcore.abilities.AbilityTargetSelector;
import com.chaosbuffalo.mkcore.abilities.AbilityTargeting;
import com.chaosbuffalo.mkcore.abilities.MKAbility;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.serialization.attributes.FloatAttribute;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.ShadowbringerEffect;
import com.chaosbuffalo.mkultra.entities.projectiles.ShadowBoltProjectileEntity;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class ShadowBoltAbility extends MKAbility {
    public static final ResourceLocation CASTING_PARTICLES = new ResourceLocation(MKUltra.MODID, "shadow_bolt_casting");
    protected final FloatAttribute baseDamage = new FloatAttribute("baseDamage", 8.0f);
    protected final FloatAttribute scaleDamage = new FloatAttribute("scaleDamage", 4.0f);
    protected final FloatAttribute projectileSpeed = new FloatAttribute("projectileSpeed", 1.25f);
    protected final FloatAttribute projectileInaccuracy = new FloatAttribute("projectileInaccuracy", 0.2f);
    protected final FloatAttribute modifierScaling = new FloatAttribute("modifierScaling", 1.0f);

    public ShadowBoltAbility() {
        super();
        setCooldownSeconds(8);
        setManaCost(7);
        setCastTime(GameConstants.TICKS_PER_SECOND);
        addAttributes(baseDamage, scaleDamage, projectileSpeed, projectileInaccuracy,
                modifierScaling);
        addSkillAttribute(MKAttributes.EVOCATION);
        casting_particles.setDefaultValue(CASTING_PARTICLES);
    }

    public float getBaseDamage() {
        return baseDamage.value();
    }

    public float getScaleDamage() {
        return scaleDamage.value();
    }

    @Override
    public int getCastTime(IMKEntityData casterData) {
        return casterData.getEffects().isEffectActive(ShadowbringerEffect.INSTANCE) ? 0 : super.getCastTime(casterData);
    }

    @Override
    public float getManaCost(IMKEntityData casterData) {
        float cost = super.getManaCost(casterData);
        return casterData.getEffects().isEffectActive(ShadowbringerEffect.INSTANCE) ? cost / 2.0f : cost;
    }

    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {
        float skillLevel = getSkillLevel(entityData.getEntity(), MKAttributes.EVOCATION);
        ITextComponent damageStr = getDamageDescription(entityData, CoreDamageTypes.ShadowDamage, baseDamage.value(),
                scaleDamage.value(), skillLevel,
                modifierScaling.value());
        return new TranslationTextComponent(getDescriptionTranslationKey(), damageStr);
    }

    @Override
    public float getDistance(LivingEntity entity) {
        return 50.0f;
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_dark_3;
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.ENEMY;
    }

    @Override
    public AbilityTargetSelector getTargetSelector() {
        return AbilityTargeting.PROJECTILE;
    }

    public float getModifierScaling() {
        return modifierScaling.value();
    }

    @Override
    public SoundEvent getCastingSoundEvent() {
        return ModSounds.casting_shadow;
    }

    @Override
    public void endCast(LivingEntity entity, IMKEntityData data, AbilityContext context) {
        super.endCast(entity, data, context);
        float level = getSkillLevel(entity, MKAttributes.EVOCATION);
        if (data.getEffects().isEffectActive(ShadowbringerEffect.INSTANCE)) {
            data.getEffects().removeEffect(ShadowbringerEffect.INSTANCE);
        }
        ShadowBoltProjectileEntity proj = new ShadowBoltProjectileEntity(entity.world);
        proj.setShooter(entity);
        proj.setSkillLevel(level);
        shootProjectile(proj, projectileSpeed.value(), projectileInaccuracy.value(), entity, context);
        entity.world.addEntity(proj);
    }
}
