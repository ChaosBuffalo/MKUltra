package com.chaosbuffalo.mkultra.entities.projectiles;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.MKCore;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.effects.instant.MKAbilityDamageEffect;
import com.chaosbuffalo.mkcore.fx.particles.ParticleAnimationManager;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.network.MKParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.abilities.necromancer.ShadowBoltAbility;
import com.chaosbuffalo.mkultra.entities.IMKRenderAsItem;
import com.chaosbuffalo.mkultra.init.MKUAbilities;
import com.chaosbuffalo.mkultra.init.MKUItems;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

public class ShadowBoltProjectileEntity extends TrailProjectileEntity implements IMKRenderAsItem {

    public static final ResourceLocation TRAIL_PARTICLES = new ResourceLocation(MKUltra.MODID, "shadow_bolt_trail");
    public static final ResourceLocation DETONATE_PARTICLES = new ResourceLocation(MKUltra.MODID, "shadow_bolt_detonate");
    private static ItemStack projectileItem;


    public ShadowBoltProjectileEntity(EntityType<? extends Projectile> entityTypeIn,
                                      Level worldIn) {
        super(entityTypeIn, worldIn);
        setDeathTime(GameConstants.TICKS_PER_SECOND * 6);
        setTrailAnimation(ParticleAnimationManager.ANIMATIONS.get(TRAIL_PARTICLES));
    }


    @Override
    protected boolean onImpact(Entity caster, HitResult result, int amplifier) {
        if (!this.level.isClientSide && caster instanceof LivingEntity) {
            LivingEntity casterLiving = (LivingEntity) caster;
            SoundSource cat = caster instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE;
            SoundUtils.serverPlaySoundAtEntity(this, ModSounds.spell_dark_8, cat);
            PacketHandler.sendToTrackingAndSelf(new MKParticleEffectSpawnPacket(
                            new Vec3(0.0, 0.0, 0.0), DETONATE_PARTICLES, getId()), this);

            if (result.getType().equals(HitResult.Type.ENTITY)) {
                EntityHitResult entityTrace = (EntityHitResult) result;
                ShadowBoltAbility ability = MKUAbilities.SHADOW_BOLT.get();
                MKEffectBuilder<?> damage = MKAbilityDamageEffect.from(casterLiving, CoreDamageTypes.ShadowDamage,
                        ability.getBaseDamage(),
                        ability.getScaleDamage(),
                        ability.getModifierScaling())
                        .ability(ability)
                        .directEntity(this)
                        .skillLevel(getSkillLevel())
                        .amplify(amplifier);
                MKCore.getEntityData(entityTrace.getEntity()).ifPresent(x -> {
                    x.getEffects().addEffect(damage);
                });
            }
            return true;
        }
        return false;
    }

    @Override
    public float getGravityVelocity() {
        return 0.0f;
    }

    @Override
    protected TargetingContext getTargetContext() {
        return TargetingContexts.ENEMY;
    }

    @Override
    public ItemStack getItem() {
        if (projectileItem == null) {
            projectileItem = new ItemStack(MKUItems.shadowBoltProjectileItem);
        }
        return projectileItem;
    }
}
