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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;


/**
 * Created by Jacob on 7/28/2018.
 */
public class CleansingSeedProjectileEntity extends TrailProjectileEntity implements IMKRenderAsItem {
    public static final ResourceLocation TRAIL_PARTICLES = new ResourceLocation(MKUltra.MODID, "cleansing_seed_trail");
    public static final ResourceLocation DETONATE_PARTICLES = new ResourceLocation(MKUltra.MODID, "cleansing_seed_detonate");

    @ObjectHolder(MKUltra.MODID + ":cleansing_seed_projectile")
    public static EntityType<CleansingSeedProjectileEntity> TYPE;

    public CleansingSeedProjectileEntity(EntityType<? extends ProjectileEntity> entityTypeIn,
                                         World worldIn) {
        super(entityTypeIn, worldIn);
        setDeathTime(40);
        setTrailAnimation(ParticleAnimationManager.ANIMATIONS.get(TRAIL_PARTICLES));
    }

    public CleansingSeedProjectileEntity(World world) {
        this(TYPE, world);
    }

    @Override
    protected boolean onImpact(Entity caster, RayTraceResult trace, int amplifier) {
        if (world.isRemote) {
            // No client code
            return false;
        }

        SoundCategory cat = caster instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
        SoundUtils.serverPlaySoundAtEntity(this, ModSounds.spell_water_6, cat);
        if (caster instanceof LivingEntity && trace.getType() == RayTraceResult.Type.ENTITY) {
            LivingEntity casterLiving = (LivingEntity) caster;
            EntityRayTraceResult entityTrace = (EntityRayTraceResult) trace;
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
                        target.attackEntityFrom(MKDamageSource.causeAbilityDamage(CoreDamageTypes.NatureDamage,
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
                new Vector3d(0.0, 0.0, 0.0), DETONATE_PARTICLES, getEntityId()), this);

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
