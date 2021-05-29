package com.chaosbuffalo.mkultra.abilities.nether_mage;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.abilities.*;
import com.chaosbuffalo.mkcore.abilities.attributes.IntAttribute;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkcore.effects.ParticleEffect;
import com.chaosbuffalo.mkcore.effects.SpellCast;
import com.chaosbuffalo.mkcore.effects.instant.SoundEffect;
import com.chaosbuffalo.mkcore.fx.ParticleEffects;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.network.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.spells.ResistanceEffects;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FireArmorAbility extends MKAbility {
    public static final FireArmorAbility INSTANCE = new FireArmorAbility();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<MKAbility> event) {
        event.getRegistry().register(INSTANCE);
    }

    protected final IntAttribute baseDuration = new IntAttribute("baseDuration", 60);
    protected final IntAttribute scaleDuration = new IntAttribute("scaleDuration", 15);

    private FireArmorAbility() {
        super(MKUltra.MODID, "ability.fire_armor");
        setCooldownSeconds(150);
        setManaCost(12);
        setCastTime(GameConstants.TICKS_PER_SECOND);
        addSkillAttribute(MKAttributes.ABJURATION);
        addAttributes(baseDuration, scaleDuration);
    }

    @Override
    public float getDistance(LivingEntity entity) {
        return 10.0f;
    }

    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {
        int level = getSkillLevel(entityData.getEntity(), MKAttributes.ABJURATION);
        float amount = ResistanceEffects.FIRE_ARMOR.getPerLevel() * (level + 1) * 100.0f;
        int duration = getBuffDuration(entityData, level, baseDuration.getValue(), scaleDuration.getValue()) / GameConstants.TICKS_PER_SECOND;
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
        int level = getSkillLevel(entity, MKAttributes.ABJURATION);
        int duration = getBuffDuration(data, level, baseDuration.getValue(), scaleDuration.getValue());

        EffectInstance fireResist = new EffectInstance(Effects.FIRE_RESISTANCE, duration, level, false, true);
        EffectInstance absorb = new EffectInstance(Effects.ABSORPTION, duration, level, false, true);

        SpellCast particlePotion = ParticleEffect.Create(entity,
                ParticleTypes.FLAME,
                ParticleEffects.CIRCLE_PILLAR_MOTION, false,
                new Vector3d(1.0, 1.0, 1.0),
                new Vector3d(0.0, 1.0, 0.0),
                40, 5, .1f);

        SpellCast fireArmor = ResistanceEffects.FIRE_ARMOR.newSpellCast(entity);

        AreaEffectBuilder.Create(entity, entity)
                .effect(fireResist, getTargetContext())
                .effect(absorb, getTargetContext())
                .spellCast(SoundEffect.Create(entity, ModSounds.spell_fire_2, entity.getSoundCategory()),
                        1, getTargetContext())
                .spellCast(fireArmor, duration, level, getTargetContext())
                .spellCast(particlePotion, level, getTargetContext())
                .instant().color(16762905).radius(getDistance(entity), true)
                .particle(ParticleTypes.DRIPPING_LAVA)
                .spawn();

        Vector3d lookVec = entity.getLookVec();
        PacketHandler.sendToTrackingAndSelf(
                new ParticleEffectSpawnPacket(
                        ParticleTypes.FLAME,
                        ParticleEffects.CIRCLE_MOTION, 50, 0,
                        entity.getPosX(), entity.getPosY() + 1.0f,
                        entity.getPosZ(), 1.0, 1.0, 1.0, 0.1,
                        lookVec), entity);
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.FRIENDLY;
    }


    @Override
    public AbilityTargetSelector getTargetSelector() {
        return AbilityTargeting.PBAOE;
    }
}
