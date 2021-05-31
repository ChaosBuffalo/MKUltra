package com.chaosbuffalo.mkultra.abilities.green_knight;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.abilities.*;
import com.chaosbuffalo.mkcore.abilities.attributes.FloatAttribute;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.entities.projectiles.CleansingSeedProjectileEntity;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.Set;

@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CleansingSeedAbility extends MKAbility {
    public static final CleansingSeedAbility INSTANCE = new CleansingSeedAbility();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<MKAbility> event) {
        event.getRegistry().register(INSTANCE);
    }

    protected final FloatAttribute baseDamage = new FloatAttribute("baseDamage", 4.0f);
    protected final FloatAttribute scaleDamage = new FloatAttribute("scaleDamage", 4.0f);
    protected final FloatAttribute projectileSpeed = new FloatAttribute("projectileSpeed", 1.25f);
    protected final FloatAttribute projectileInaccuracy = new FloatAttribute("projectileInaccuracy", 0.2f);
    protected final FloatAttribute modifierScaling = new FloatAttribute("modifierScaling", 1.0f);

    private CleansingSeedAbility() {
        super(MKUltra.MODID, "ability.cleansing_seed");
        setCooldownSeconds(8);
        setManaCost(4);
        setCastTime(GameConstants.TICKS_PER_SECOND - 5);
        addAttributes(baseDamage, scaleDamage, projectileSpeed, projectileInaccuracy, modifierScaling);
        addSkillAttribute(MKAttributes.RESTORATION);
    }

    public float getDamageForLevel(int level){
        return baseDamage.getValue() + scaleDamage.getValue() * level;
    }

    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {
        ITextComponent damageStr = getDamageDescription(entityData, CoreDamageTypes.NatureDamage, baseDamage.getValue(),
                scaleDamage.getValue(), getSkillLevel(entityData.getEntity(), MKAttributes.RESTORATION),
                modifierScaling.getValue());
        return new TranslationTextComponent(getDescriptionTranslationKey(), damageStr);
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_cast_6;
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.ALL;
    }

    public float getModifierScaling() {
        return modifierScaling.getValue();
    }

    @Override
    public SoundEvent getCastingSoundEvent() {
        return ModSounds.casting_water;
    }

    @Override
    public float getDistance(LivingEntity entity) {
        return 50.0f;
    }


    @Override
    public void endCast(LivingEntity entity, IMKEntityData data, AbilityContext context) {
        super.endCast(entity, data, context);
        int level = getSkillLevel(entity, MKAttributes.RESTORATION);
        CleansingSeedProjectileEntity proj = new CleansingSeedProjectileEntity(entity.world);
        proj.setShooter(entity);
        proj.setAmplifier(level);
        shootProjectile(proj, projectileSpeed.getValue(), projectileInaccuracy.getValue(), entity, context);
        entity.world.addEntity(proj);
    }

}


