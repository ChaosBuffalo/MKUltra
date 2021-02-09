package com.chaosbuffalo.mkultra.abilities.green_knight;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.abilities.*;
import com.chaosbuffalo.mkcore.abilities.attributes.FloatAttribute;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.core.damage.MKDamageSource;
import com.chaosbuffalo.mkcore.fx.ParticleEffects;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.network.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.utils.RayTraceUtils;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.spells.CureEffect;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.Targeting;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ExplosiveGrowthAbility extends MKAbility {
    public static final ExplosiveGrowthAbility INSTANCE = new ExplosiveGrowthAbility();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<MKAbility> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static float BASE_DAMAGE = 10.0f;
    public static float DAMAGE_SCALE = 5.0f;
    protected final FloatAttribute baseDamage = new FloatAttribute("baseDamage", 10.0f);
    protected final FloatAttribute scaleDamage = new FloatAttribute("scaleDamage", 5.0f);

    private ExplosiveGrowthAbility() {
        super(MKUltra.MODID, "ability.explosive_growth");
        setCooldownSeconds(35);
        setManaCost(6);
        setCastTime(GameConstants.TICKS_PER_SECOND / 4);
        addAttributes(baseDamage, scaleDamage);
        addSkillAttribute(MKAttributes.RESTORATION);
        addSkillAttribute(MKAttributes.PANKRATION);
    }

    @Override
    public AbilityType getType() {
        return AbilityType.PooledUltimate;
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.ALL;
    }


    @Override
    public float getDistance(LivingEntity entity) {
        return 8.0f;
    }

    @Override
    public SoundEvent getCastingSoundEvent() {
        return ModSounds.casting_shadow;
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_earth_8;
    }

    @Override
    public void endCast(LivingEntity entity, IMKEntityData data, AbilityContext context) {
        super.endCast(entity, data, context);
        int restoLevel = getSkillLevel(entity, MKAttributes.RESTORATION);
        int pankrationLevel = getSkillLevel(entity, MKAttributes.PANKRATION);
        Vector3d look = entity.getLookVec().scale(getDistance(entity));
        Vector3d from = entity.getPositionVec().add(0, entity.getEyeHeight(), 0);
        Vector3d to = from.add(look);
        Vector3d lookVec = entity.getLookVec();
        List<LivingEntity> entityHit = getTargetsInLine(entity, from, to, true, 1.0f);
        SoundCategory cat = entity instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
        float damage = baseDamage.getValue() + scaleDamage.getValue() * pankrationLevel;
        for (LivingEntity entHit : entityHit) {

            if (Targeting.isValidEnemy(entity, entHit)) {
                entHit.attackEntityFrom(MKDamageSource.causeMeleeDamage(getAbilityId(), entity, entity), damage);
                SoundUtils.playSoundAtEntity(entHit, ModSounds.spell_earth_1, cat);
            } else if (!entHit.equals(entity) && Targeting.isValidFriendly(entity, entHit)) {
                entHit.addPotionEffect(CureEffect.Create(entity).setTarget(entHit).toPotionEffect(restoLevel));
                NaturesRemedyAbility.INSTANCE.castNaturesRemedyOnTarget(entHit, data, restoLevel);
                SoundUtils.playSoundAtEntity(entHit, ModSounds.spell_earth_6, cat);
            }
            PacketHandler.sendToTrackingAndSelf(
                    new ParticleEffectSpawnPacket(
                            ParticleTypes.INSTANT_EFFECT,
                            ParticleEffects.CIRCLE_MOTION, 20, 10,
                            entHit.getPosX(), entHit.getPosY() + 1.0,
                            entHit.getPosZ(), 1.0, 1.0, 1.0, 2.0,
                            lookVec),
                    entity);
        }
        RayTraceResult blockHit = RayTraceUtils.rayTraceBlocks(entity, from, to, false);
        if (blockHit != null && blockHit.getType() == RayTraceResult.Type.BLOCK) {
            to = blockHit.getHitVec();
        }
        entity.addPotionEffect(CureEffect.Create(entity).setTarget(entity).toPotionEffect(restoLevel));
        NaturesRemedyAbility.INSTANCE.castNaturesRemedyOnTarget(entity, data, restoLevel);
        entity.setPositionAndUpdate(to.x, to.y, to.z);
        PacketHandler.sendToTrackingAndSelf(
                new ParticleEffectSpawnPacket(
                        ParticleTypes.AMBIENT_ENTITY_EFFECT,
                        ParticleEffects.SPHERE_MOTION, 30, 10,
                        entity.getPosX(), entity.getPosY() + 1.0,
                        entity.getPosZ(), 1.0, 1.0, 1.0, 2.0,
                        lookVec),
                entity);
    }

}
