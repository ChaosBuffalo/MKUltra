package com.chaosbuffalo.mkultra.abilities.green_knight;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.abilities.*;
import com.chaosbuffalo.mkcore.abilities.attributes.FloatAttribute;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.entities.projectiles.SpiritBombProjectileEntity;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.Set;

@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpiritBombAbility extends MKAbility {
    public static final SpiritBombAbility INSTANCE = new SpiritBombAbility();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<MKAbility> event) {
        event.getRegistry().register(INSTANCE);
    }

    protected final FloatAttribute baseDamage = new FloatAttribute("baseDamage", 4.0f);
    protected final FloatAttribute scaleDamage = new FloatAttribute("scaleDamage", 4.0f);
    protected final FloatAttribute projectileSpeed = new FloatAttribute("projectileSpeed", 1.25f);
    protected final FloatAttribute projectileInaccuracy = new FloatAttribute("projectileInaccuracy", 0.2f);
    protected final FloatAttribute modifierScaling = new FloatAttribute("modifierScaling", 1.0f);

    private SpiritBombAbility() {
        super(MKUltra.MODID, "ability.spirit_bomb");
        setCooldownSeconds(16);
        setCastTime(GameConstants.TICKS_PER_SECOND + (GameConstants.TICKS_PER_SECOND / 4));
        setManaCost(4);
        addAttributes(baseDamage, scaleDamage, projectileSpeed, projectileInaccuracy, modifierScaling);
        addSkillAttribute(MKAttributes.EVOCATION);
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
    public SoundEvent getCastingSoundEvent() {
        return ModSounds.casting_holy;
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_magic_whoosh_1;
    }

    public float getBaseDamage() {
        return baseDamage.getValue();
    }

    public float getScaleDamage() {
        return scaleDamage.getValue();
    }

    public float getModifierScaling() {
        return modifierScaling.getValue();
    }

    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {
        ITextComponent damageStr = getDamageDescription(entityData, CoreDamageTypes.NatureDamage, baseDamage.getValue(),
                scaleDamage.getValue(), getSkillLevel(entityData.getEntity(), MKAttributes.EVOCATION),
                modifierScaling.getValue());
        return new TranslationTextComponent(getDescriptionTranslationKey(), damageStr);
    }

    @Override
    public void endCast(LivingEntity entity, IMKEntityData data, AbilityContext context) {
        super.endCast(entity, data, context);
        int level = getSkillLevel(entity, MKAttributes.EVOCATION);
        SpiritBombProjectileEntity proj = new SpiritBombProjectileEntity(entity.world);
        proj.setShooter(entity);
        proj.setAmplifier(level);
        shootProjectile(proj, projectileSpeed.getValue(), projectileInaccuracy.getValue(), entity, context);
        entity.world.addEntity(proj);
    }
}
