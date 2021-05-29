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
import com.chaosbuffalo.mkultra.abilities.misc.FireballAbility;
import com.chaosbuffalo.mkultra.effects.spells.ResistanceEffects;
import com.chaosbuffalo.mkultra.init.MKUItems;
import com.chaosbuffalo.mkultra.init.ModSounds;
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
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

public class FireballProjectileEntity extends BaseProjectileEntity implements IRendersAsItem {
    @ObjectHolder(MKUltra.MODID + ":fireball_projectile")
    public static EntityType<FireballProjectileEntity> TYPE;

    public FireballProjectileEntity(EntityType<? extends ProjectileEntity> entityTypeIn,
                                         World worldIn) {
        super(entityTypeIn, worldIn);
        setDeathTime(GameConstants.TICKS_PER_SECOND * 5);
    }

    public FireballProjectileEntity(World world){
        this(TYPE, world);
    }

    @Override
    protected boolean onImpact(Entity caster, RayTraceResult result, int amplifier) {
        if (!this.world.isRemote && caster instanceof LivingEntity) {
            SoundCategory cat = caster instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
            SoundUtils.playSoundAtEntity(this, ModSounds.spell_fire_4, cat);
            PacketHandler.sendToTrackingAndSelf(
                    new ParticleEffectSpawnPacket(
                            ParticleTypes.DRIPPING_LAVA,
                            ParticleEffects.SPHERE_MOTION, 20, 4,
                            result.getHitVec().x, result.getHitVec().y + 1.0,
                            result.getHitVec().z, 0.25, 0.25, 0.25, 0.25,
                            new Vector3d(0., 1.0, 0.0)),
                    this);
            SpellCast damage = MKAbilityDamageEffect.Create(caster, CoreDamageTypes.FireDamage,
                    FireballAbility.INSTANCE,
                    FireballAbility.INSTANCE.getBaseDamage(),
                    FireballAbility.INSTANCE.getScaleDamage(),
                    FireballAbility.INSTANCE.getModifierScaling());
            SpellCast fireBreak = ResistanceEffects.BREAK_FIRE.newSpellCast(caster);
            AreaEffectBuilder.Create((LivingEntity) caster, this)
                    .spellCast(damage, amplifier, getTargetContext())
                    .spellCast(fireBreak, (amplifier + 1) * GameConstants.TICKS_PER_SECOND, amplifier, getTargetContext())
                    .instant()
                    .color(16737330).radius(FireballAbility.INSTANCE.getExplosionRadius(), true)
                    .spawn();
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
        return new ItemStack(MKUItems.fireballProjectileItem);
    }
}
