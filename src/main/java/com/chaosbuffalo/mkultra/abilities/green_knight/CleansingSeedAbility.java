package com.chaosbuffalo.mkultra.abilities.green_knight;

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
import com.chaosbuffalo.mkultra.entities.projectiles.CleansingSeedProjectileEntity;
import com.chaosbuffalo.mkultra.init.MKUEntities;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import javax.annotation.Nullable;

public class CleansingSeedAbility extends MKAbility {
    public static final ResourceLocation CASTING_PARTICLES = new ResourceLocation(MKUltra.MODID, "cleansing_seed_casting");
    protected final FloatAttribute baseDamage = new FloatAttribute("baseDamage", 4.0f);
    protected final FloatAttribute scaleDamage = new FloatAttribute("scaleDamage", 4.0f);
    protected final FloatAttribute projectileSpeed = new FloatAttribute("projectileSpeed", 1.25f);
    protected final FloatAttribute projectileInaccuracy = new FloatAttribute("projectileInaccuracy", 0.2f);
    protected final FloatAttribute modifierScaling = new FloatAttribute("modifierScaling", 1.0f);

    public CleansingSeedAbility() {
        super();
        setCooldownSeconds(8);
        setManaCost(4);
        setCastTime(GameConstants.TICKS_PER_SECOND - 5);
        addAttributes(baseDamage, scaleDamage, projectileSpeed, projectileInaccuracy, modifierScaling);
        addSkillAttribute(MKAttributes.RESTORATION);
        casting_particles.setDefaultValue(CASTING_PARTICLES);
    }

    public float getDamageForLevel(float level) {
        return baseDamage.value() + scaleDamage.value() * level;
    }

    @Override
    protected Component getAbilityDescription(IMKEntityData entityData) {
        Component damageStr = getDamageDescription(entityData, CoreDamageTypes.NatureDamage, baseDamage.value(),
                scaleDamage.value(), getSkillLevel(entityData.getEntity(), MKAttributes.RESTORATION),
                modifierScaling.value());
        return new TranslatableComponent(getDescriptionTranslationKey(), damageStr);
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_cast_6.get();
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.ALL;
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
        return ModSounds.casting_water.get();
    }

    @Override
    public float getDistance(LivingEntity entity) {
        return 50.0f;
    }

    @Override
    public void endCast(LivingEntity entity, IMKEntityData data, AbilityContext context) {
        super.endCast(entity, data, context);

        float level = getSkillLevel(entity, MKAttributes.RESTORATION);
        CleansingSeedProjectileEntity proj = new CleansingSeedProjectileEntity(MKUEntities.CLEANSING_SEED_TYPE.get(), entity.level);
        proj.setOwner(entity);
        proj.setSkillLevel(level);
        shootProjectile(proj, projectileSpeed.value(), projectileInaccuracy.value(), entity, context);
        entity.level.addFreshEntity(proj);
    }
}
