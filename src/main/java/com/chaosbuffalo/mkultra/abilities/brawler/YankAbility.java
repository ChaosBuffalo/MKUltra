package com.chaosbuffalo.mkultra.abilities.brawler;

import com.chaosbuffalo.mkcore.MKCore;
import com.chaosbuffalo.mkcore.abilities.*;
import com.chaosbuffalo.mkcore.abilities.ai.conditions.MeleeSpellInterruptUseCondition;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.network.MKParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.serialization.attributes.FloatAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.ResourceLocationAttribute;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.YankEffect;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class YankAbility extends MKAbility {
    public static final ResourceLocation CAST_PARTICLES = new ResourceLocation(MKUltra.MODID, "yank_cast");
    protected final FloatAttribute base = new FloatAttribute("base", 1.0f);
    protected final FloatAttribute scale = new FloatAttribute("scale", 0.75f);
    protected final ResourceLocationAttribute cast_particles = new ResourceLocationAttribute("cast_particles", CAST_PARTICLES);


    public YankAbility() {
        super();
        setCooldownSeconds(5);
        setManaCost(4);
        addAttributes(base, scale, cast_particles);
        addSkillAttribute(MKAttributes.PANKRATION);
        setUseCondition(new MeleeSpellInterruptUseCondition(this));
    }

    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {
        float level = getSkillLevel(entityData.getEntity(), MKAttributes.PANKRATION);
        String value = NUMBER_FORMATTER.format(base.value() + scale.value() * level);
        return new TranslationTextComponent(getDescriptionTranslationKey(), value);
    }

    @Override
    public float getDistance(LivingEntity entity) {
        return getMeleeReach(entity) * 1.5f;
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.ALL;
    }

    @Override
    public AbilityTargetSelector getTargetSelector() {
        return AbilityTargeting.SINGLE_TARGET;
    }

    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_grab_2;
    }

    @Override
    public void endCast(LivingEntity entity, IMKEntityData data, AbilityContext context) {
        super.endCast(entity, data, context);
        float level = getSkillLevel(entity, MKAttributes.PANKRATION);
        context.getMemory(MKAbilityMemories.ABILITY_TARGET).ifPresent(targetEntity -> {
            Vector3d yankPos = new Vector3d(entity.getPosX(), entity.getPosYHeight(0.6), entity.getPosZ());
            MKEffectBuilder<?> yank = YankEffect.from(entity, base.value(), scale.value(), yankPos)
                    .ability(this)
                    .skillLevel(level);

            MKCore.getEntityData(targetEntity).ifPresent(targetData -> {
                targetData.getEffects().addEffect(yank);
            });

            MKParticleEffectSpawnPacket spawn = new MKParticleEffectSpawnPacket(yankPos, CAST_PARTICLES);
            Vector3d targetPos = new Vector3d(targetEntity.getPosX(), targetEntity.getPosYHeight(0.6f), targetEntity.getPosZ());
            spawn.addLoc(targetPos);
            PacketHandler.sendToTrackingAndSelf(spawn, entity);
        });
    }
}