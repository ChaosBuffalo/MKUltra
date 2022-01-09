package com.chaosbuffalo.mkultra.abilities.cleric;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.MKCore;
import com.chaosbuffalo.mkcore.abilities.*;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.network.MKParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.serialization.attributes.IntAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.ResourceLocationAttribute;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.WarpTargetEffect;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.Targeting;
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

public class PowerWordSummonAbility extends MKAbility {

    public static final PowerWordSummonAbility INSTANCE = new PowerWordSummonAbility();

    protected final ResourceLocation CASTING_PARTICLES = new ResourceLocation(MKUltra.MODID, "power_word_summon_casting");
    protected final ResourceLocation CAST_PARTICLES = new ResourceLocation(MKUltra.MODID, "power_word_summon_cast");
    protected final IntAttribute base = new IntAttribute("baseDuration", 4);
    protected final IntAttribute scale = new IntAttribute("scaleDuration", 1);
    protected final ResourceLocationAttribute cast_particles = new ResourceLocationAttribute("cast_particles", CAST_PARTICLES);

    public PowerWordSummonAbility() {
        super(MKUltra.MODID, "ability.power_word_summon");
        setCooldownSeconds(16);
        setCastTime(GameConstants.TICKS_PER_SECOND);
        setManaCost(6);
        addAttributes(base, scale, cast_particles);
        addSkillAttribute(MKAttributes.CONJURATION);
        casting_particles.setDefaultValue(CASTING_PARTICLES);
    }

    @Override
    public float getDistance(LivingEntity entity) {
        return 30.0f;
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.ALL;
    }

    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {
        int level = getSkillLevel(entityData.getEntity(), MKAttributes.CONJURATION);
        int duration = getBuffDuration(entityData, level, base.value(), scale.value()) / GameConstants.TICKS_PER_SECOND;
        return new TranslationTextComponent(getDescriptionTranslationKey(), duration);
    }

    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_magic_whoosh_3;
    }

    @Override
    public AbilityTargetSelector getTargetSelector() {
        return AbilityTargeting.SINGLE_TARGET;
    }

    @Override
    public void endCast(LivingEntity castingEntity, IMKEntityData casterData, AbilityContext context) {
        super.endCast(castingEntity, casterData, context);
        int level = getSkillLevel(castingEntity, MKAttributes.CONJURATION);

        context.getMemory(MKAbilityMemories.ABILITY_TARGET).ifPresent(targetEntity -> {
            MKEffectBuilder<?> warp = WarpTargetEffect.INSTANCE.builder(castingEntity.getUniqueID())
                    .ability(this)
                    .amplify(level);

            MKCore.getEntityData(targetEntity).ifPresent(targetData -> targetData.getEffects().addEffect(warp));

            if (Targeting.isValidEnemy(castingEntity, targetEntity)) {
                int duration = getBuffDuration(casterData, level, base.value(), scale.value());
                targetEntity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, duration, 100, false, false));
            }

            SoundUtils.serverPlaySoundAtEntity(targetEntity, ModSounds.spell_magic_whoosh_4, targetEntity.getSoundCategory());
            PacketHandler.sendToTrackingAndSelf(new MKParticleEffectSpawnPacket(
                    new Vector3d(0.0, 1.0, 0.0), cast_particles.getValue(), targetEntity.getEntityId()), targetEntity);
        });
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
