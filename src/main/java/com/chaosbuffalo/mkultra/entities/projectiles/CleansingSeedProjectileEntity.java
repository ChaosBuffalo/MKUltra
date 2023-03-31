package com.chaosbuffalo.mkultra.entities.projectiles;

import com.chaosbuffalo.mkcore.MKCore;
import com.chaosbuffalo.mkcore.core.damage.MKDamageSource;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.fx.particles.ParticleAnimationManager;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.network.MKParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.abilities.green_knight.CleansingSeedAbility;
import com.chaosbuffalo.mkultra.effects.CureEffect;
import com.chaosbuffalo.mkultra.entities.IMKRenderAsItem;
import com.chaosbuffalo.mkultra.init.MKUAbilities;
import com.chaosbuffalo.mkultra.init.MKUItems;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.Targeting;
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
import net.minecraftforge.registries.ObjectHolder;


/**
 * Created by Jacob on 7/28/2018.
 */
public class CleansingSeedProjectileEntity extends TrailProjectileEntity implements IMKRenderAsItem {
    public static final ResourceLocation TRAIL_PARTICLES = new ResourceLocation(MKUltra.MODID, "cleansing_seed_trail");
    public static final ResourceLocation DETONATE_PARTICLES = new ResourceLocation(MKUltra.MODID, "cleansing_seed_detonate");


    public CleansingSeedProjectileEntity(EntityType<? extends Projectile> entityTypeIn,
                                         Level worldIn) {
        super(entityTypeIn, worldIn);
        setDeathTime(40);
        setTrailAnimation(ParticleAnimationManager.ANIMATIONS.get(TRAIL_PARTICLES));
    }

    @Override
    protected boolean onImpact(Entity caster, HitResult trace, int amplifier) {
        if (level.isClientSide) {
            // No client code
            return false;
        }

        SoundSource cat = caster instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE;
        SoundUtils.serverPlaySoundAtEntity(this, ModSounds.spell_water_6, cat);
        if (caster instanceof LivingEntity && trace.getType() == HitResult.Type.ENTITY) {
            LivingEntity casterLiving = (LivingEntity) caster;
            EntityHitResult entityTrace = (EntityHitResult) trace;
            if (entityTrace.getEntity() instanceof LivingEntity) {
                LivingEntity target = (LivingEntity) entityTrace.getEntity();
                Targeting.TargetRelation relation = Targeting.getTargetRelation(caster, target);
                CleansingSeedAbility ability = MKUAbilities.CLEANSING_SEED.get();
                switch (relation) {
                    case FRIEND: {
                        MKEffectBuilder<?> cure = CureEffect.from(casterLiving)
                                .ability(ability)
                                .directEntity(this)
                                .skillLevel(getSkillLevel())
                                .amplify(amplifier);

                        MKCore.getEntityData(target).ifPresent(targetData -> targetData.getEffects().addEffect(cure));

                        SoundUtils.serverPlaySoundAtEntity(target, ModSounds.spell_water_2, cat);
                        break;
                    }
                    case ENEMY: {
                        target.hurt(MKDamageSource.causeAbilityDamage(CoreDamageTypes.NatureDamage,
                                        ability.getAbilityId(), this, caster,
                                        ability.getModifierScaling()),
                                ability.getDamageForLevel(getSkillLevel()));
                        SoundUtils.serverPlaySoundAtEntity(target, ModSounds.spell_water_8, cat);
                        break;
                    }
                }
            }
        }

        PacketHandler.sendToTrackingAndSelf(new MKParticleEffectSpawnPacket(
                new Vec3(0.0, 0.0, 0.0), DETONATE_PARTICLES, getId()), this);

        return true;
    }

    @Override
    protected TargetingContext getTargetContext() {
        return TargetingContexts.ALL;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(MKUItems.cleansingSeedProjectileItem);
    }
}
