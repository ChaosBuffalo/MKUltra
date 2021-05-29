package com.chaosbuffalo.mkultra.abilities.misc;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.abilities.AbilityContext;
import com.chaosbuffalo.mkcore.abilities.MKAbility;
import com.chaosbuffalo.mkcore.abilities.attributes.FloatAttribute;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.entities.projectiles.FireballProjectileEntity;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;


@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FireballAbility extends MKAbility {
    public static final FireballAbility INSTANCE = new FireballAbility();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<MKAbility> event) {
        event.getRegistry().register(INSTANCE);
    }

    protected final FloatAttribute baseDamage = new FloatAttribute("baseDamage", 6.0f);
    protected final FloatAttribute scaleDamage = new FloatAttribute("scaleDamage", 2.0f);
    protected final FloatAttribute projectileSpeed = new FloatAttribute("projectileSpeed", 1.25f);
    protected final FloatAttribute projectileInaccuracy = new FloatAttribute("projectileInaccuracy", 0.2f);
    protected final FloatAttribute modifierScaling = new FloatAttribute("modifierScaling", 1.0f);
    protected final FloatAttribute radius = new FloatAttribute("explosionRadius", 2.0f);

    private FireballAbility() {
        super(MKUltra.MODID, "ability.fireball");
        setCooldownSeconds(6);
        setManaCost(5);
        setCastTime(GameConstants.TICKS_PER_SECOND);
        addAttributes(baseDamage, scaleDamage, projectileSpeed, projectileInaccuracy,
                modifierScaling, radius);
        addSkillAttribute(MKAttributes.EVOCATION);
    }

    public float getBaseDamage(){
        return baseDamage.getValue();
    }

    public float getScaleDamage(){
        return scaleDamage.getValue();
    }

    public float getExplosionRadius(){
        return radius.getValue();
    }

    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {
        int skillLevel = getSkillLevel(entityData.getEntity(), MKAttributes.EVOCATION);
        ITextComponent damageStr = getDamageDescription(entityData, CoreDamageTypes.FireDamage, baseDamage.getValue(),
                scaleDamage.getValue(), skillLevel,
                modifierScaling.getValue());
        return new TranslationTextComponent(getDescriptionTranslationKey(), damageStr, getExplosionRadius(),
                (skillLevel + 1) * .1f * 100.0f, skillLevel + 1);
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_fire_2;
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.ENEMY;
    }

    public float getModifierScaling() {
        return modifierScaling.getValue();
    }

    @Override
    public SoundEvent getCastingSoundEvent() {
        return ModSounds.hostile_casting_fire;
    }


    @Override
    public void endCast(LivingEntity entity, IMKEntityData data, AbilityContext context) {
        super.endCast(entity, data, context);
        int level = getSkillLevel(entity, MKAttributes.EVOCATION);
        FireballProjectileEntity proj = new FireballProjectileEntity(entity.world);
        proj.setShooter(entity);
        proj.setAmplifier(level);
        shootProjectile(proj, projectileSpeed.getValue(), projectileInaccuracy.getValue(), entity, context);
        entity.world.addEntity(proj);
    }

}
