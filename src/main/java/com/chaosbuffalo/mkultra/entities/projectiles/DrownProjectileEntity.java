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
import com.chaosbuffalo.mkultra.abilities.wet_wizard.DrownAbility;
import com.chaosbuffalo.mkultra.entities.IMKRenderAsItem;
import com.chaosbuffalo.mkultra.init.MKUAbilities;
import com.chaosbuffalo.mkultra.init.MKUItems;
import com.chaosbuffalo.mkultra.init.ModSounds;
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

public class DrownProjectileEntity extends TrailProjectileEntity implements IMKRenderAsItem {

    public static final ResourceLocation TRAIL_PARTICLES = new ResourceLocation(MKUltra.MODID, "drown_trail");
    public static final ResourceLocation DETONATE_PARTICLES = new ResourceLocation(MKUltra.MODID, "drown_detonate");
    private static ItemStack projectileItem;

    @ObjectHolder(MKUltra.MODID + ":drown_projectile")
    public static EntityType<DrownProjectileEntity> TYPE;

    public DrownProjectileEntity(EntityType<? extends ProjectileEntity> entityTypeIn,
                                 World worldIn) {
        super(entityTypeIn, worldIn);
        setDeathTime(GameConstants.TICKS_PER_SECOND * 3);
        setTrailAnimation(ParticleAnimationManager.ANIMATIONS.get(TRAIL_PARTICLES));
    }

    public DrownProjectileEntity(World world) {
        this(TYPE, world);
    }

    @Override
    protected boolean onImpact(Entity caster, RayTraceResult result, int amplifier) {
        if (!this.world.isRemote && caster instanceof LivingEntity) {
            SoundCategory cat = caster instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
            SoundUtils.serverPlaySoundAtEntity(this, ModSounds.spell_water_5, cat);
            PacketHandler.sendToTrackingAndSelf(new MKParticleEffectSpawnPacket(
                    new Vector3d(0.0, 0.0, 0.0), DETONATE_PARTICLES, getEntityId()), this);

            if (result.getType().equals(RayTraceResult.Type.ENTITY)) {
                EntityRayTraceResult entityTrace = (EntityRayTraceResult) result;
                MKCore.getEntityData(caster).ifPresent(casterData -> {
                    MKEffectBuilder<?> damage = MKUAbilities.DROWN.get().getDotEffect(casterData, getSkillLevel());
                    MKCore.getEntityData(entityTrace.getEntity()).ifPresent(x -> {
                        x.getEffects().addEffect(damage);
                    });
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
            projectileItem = new ItemStack(MKUItems.drownProjectileItem);
        }
        return projectileItem;
    }
}

