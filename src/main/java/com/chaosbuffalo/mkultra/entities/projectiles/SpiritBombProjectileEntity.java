package com.chaosbuffalo.mkultra.entities.projectiles;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.effects.instant.MKAbilityDamageEffect;
import com.chaosbuffalo.mkcore.fx.particles.ParticleAnimationManager;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.network.MKParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.abilities.green_knight.SpiritBombAbility;
import com.chaosbuffalo.mkultra.entities.IMKRenderAsItem;
import com.chaosbuffalo.mkultra.init.MKUItems;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.Targeting;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
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
public class SpiritBombProjectileEntity extends TrailProjectileEntity implements IMKRenderAsItem {

    public static final ResourceLocation TRAIL_PARTICLES = new ResourceLocation(MKUltra.MODID, "spirit_bomb_trail");
    public static final ResourceLocation DETONATE_PARTICLES = new ResourceLocation(MKUltra.MODID, "spirit_bomb_detonate");

    @ObjectHolder(MKUltra.MODID + ":spirit_bomb_projectile")
    public static EntityType<SpiritBombProjectileEntity> TYPE;


    public SpiritBombProjectileEntity(EntityType<? extends ProjectileEntity> entityTypeIn,
                                      World worldIn) {
        super(entityTypeIn, worldIn);
        setDeathTime(60);
        setAirProcTime(GameConstants.TICKS_PER_SECOND);
        setDoAirProc(true);
        setDoGroundProc(true);
        setGroundProcTime(GameConstants.TICKS_PER_SECOND);
        setTrailAnimation(ParticleAnimationManager.ANIMATIONS.get(TRAIL_PARTICLES));
    }

    public SpiritBombProjectileEntity(World world) {
        this(TYPE, world);
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(MKUItems.spiritBombProjectileItem);
    }

    @Override
    protected TargetingContext getTargetContext() {
        return TargetingContexts.ENEMY;
    }

    @Override
    protected boolean onImpact(Entity caster, RayTraceResult result, int amplifier) {
        if (!this.world.isRemote && caster != null) {
            SoundCategory cat = caster.getSoundCategory();
            SoundUtils.serverPlaySoundAtEntity(this, ModSounds.spell_thunder_3, cat);
            switch (result.getType()) {
                case BLOCK:
                    break;
                case ENTITY:
                    EntityRayTraceResult entityTrace = (EntityRayTraceResult) result;
                    if (entityTrace.getEntity() instanceof LivingEntity) {
                        LivingEntity target = (LivingEntity) entityTrace.getEntity();
                        if (Targeting.isValidTarget(getTargetContext(), caster, target)) {
                            this.setMotion(0.0, 0.0, 0.0);
                        }
                    }
                    break;
                case MISS:
                    break;
            }
        }
        return false;
    }


    @Override
    public float getGravityVelocity() {
        return 0.00F;
    }

    @Override
    protected boolean onAirProc(Entity caster, int amplifier) {
        return doEffect(caster, amplifier);
    }

    private boolean doEffect(Entity caster, int amplifier) {
        if (!this.world.isRemote && caster instanceof LivingEntity) {
            MKEffectBuilder<?> damage = MKAbilityDamageEffect.from(caster, CoreDamageTypes.NatureDamage,
                            SpiritBombAbility.INSTANCE.getBaseDamage(),
                            SpiritBombAbility.INSTANCE.getScaleDamage(),
                            SpiritBombAbility.INSTANCE.getModifierScaling())
                    .ability(SpiritBombAbility.INSTANCE)
                    .amplify(amplifier);

            AreaEffectBuilder.Create((LivingEntity) caster, this)
                    .effect(damage, getTargetContext())
                    .instant()
                    .color(65535)
                    .radius(4.0f, true)
                    .disableParticle()
                    .spawn();
            SoundCategory cat = caster.getSoundCategory();
            SoundUtils.serverPlaySoundAtEntity(this, ModSounds.spell_magic_explosion, cat);
            PacketHandler.sendToTrackingAndSelf(
                    new MKParticleEffectSpawnPacket(new Vector3d(0.0, 0.0, 0.0), DETONATE_PARTICLES, getEntityId()), this);
            return true;
        }
        return false;
    }

    @Override
    protected boolean onGroundProc(Entity caster, int amplifier) {
        return doEffect(caster, amplifier);
    }
}
