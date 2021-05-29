package com.chaosbuffalo.mkultra.entities.projectiles;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkcore.effects.SpellCast;
import com.chaosbuffalo.mkcore.effects.instant.MKAbilityDamageEffect;
import com.chaosbuffalo.mkcore.entities.BaseProjectileEntity;
import com.chaosbuffalo.mkcore.fx.ParticleEffects;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.network.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.abilities.green_knight.SpiritBombAbility;
import com.chaosbuffalo.mkultra.init.MKUItems;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.Targeting;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Created by Jacob on 7/28/2018.
 */
public class SpiritBombProjectileEntity extends BaseProjectileEntity implements IRendersAsItem {

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
        MKUltra.LOGGER.info("Spawning cleansing seed");
    }

    public SpiritBombProjectileEntity(World world){
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
            SoundCategory cat = caster instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
            SoundUtils.playSoundAtEntity(this, ModSounds.spell_thunder_3, cat);
            PacketHandler.sendToTrackingAndSelf(
                    new ParticleEffectSpawnPacket(
                            ParticleTypes.AMBIENT_ENTITY_EFFECT,
                            ParticleEffects.SPHERE_MOTION, 30, 10,
                            result.getHitVec().x, result.getHitVec().y + 1.0,
                            result.getHitVec().z, 1.0, 1.0, 1.0, 1.0,
                            new Vector3d(0., 1.0, 0.0)),
                    this);
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
            SpellCast damage = MKAbilityDamageEffect.Create(caster, CoreDamageTypes.NatureDamage,
                    SpiritBombAbility.INSTANCE,
                    SpiritBombAbility.INSTANCE.getBaseDamage(),
                    SpiritBombAbility.INSTANCE.getScaleDamage(),
                    SpiritBombAbility.INSTANCE.getModifierScaling());
            AreaEffectBuilder.Create((LivingEntity) caster, this)
                    .spellCast(damage, amplifier, getTargetContext())
                    .instant()
                    .color(65535).radius(4.0f, true)
                    .spawn();
            SoundCategory cat = caster instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
            SoundUtils.playSoundAtEntity(this, ModSounds.spell_magic_explosion, cat);
            PacketHandler.sendToTrackingAndSelf(
                    new ParticleEffectSpawnPacket(
                            ParticleTypes.ITEM_SLIME,
                            ParticleEffects.DIRECTED_SPOUT, 60, 1,
                            this.getPosX(), this.getPosY() + 1.0,
                            this.getPosZ(), 1.5, 2.0, 1.5, 1.0,
                            new Vector3d(0., 1.0, 0.0)),
                    this);
            return true;
        }
        return false;
    }

    @Override
    protected boolean onGroundProc(Entity caster, int amplifier) {
        return doEffect(caster, amplifier);
    }

}
