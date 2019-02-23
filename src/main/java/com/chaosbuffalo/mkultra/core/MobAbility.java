package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.utils.RayTraceUtils;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class MobAbility extends IForgeRegistryEntry.Impl<MobAbility> {


    public MobAbility(String domain, String id) {
        this(new ResourceLocation(domain, id));
    }

    public ResourceLocation getAbilityId() {
        return getRegistryName();
    }

    public MobAbility(ResourceLocation abilityId) {
        setRegistryName(abilityId);
    }

    public float getDistance() {
        return 1.0f;
    }

    public abstract int getCooldown();

    public int getCastTime(){
        return GameConstants.TICKS_PER_SECOND;
    }

    @Nullable
    public Potion getEffectPotion(){
        return null;
    }

    public boolean shouldCast(EntityLivingBase caster, EntityLivingBase target){
        if (!isInRange(caster, target)){
            return false;
        }
        Potion effect = getEffectPotion();
        if (effect != null){
            if (target.isPotionActive(effect)){
                return false;
            }
        }
        return true;
    }

    public boolean isInRange(EntityLivingBase caster, EntityLivingBase target){
        double dist = getDistance();
        return caster.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ) <= dist * dist;
    }

    public enum AbilityType{
        ATTACK,
        HEAL,
        BUFF
    }

    public abstract AbilityType getAbilityType();

    public boolean canSelfCast() {
        return false;
    }

    public abstract Targeting.TargetType getTargetType();

    protected boolean isValidTarget(EntityLivingBase caster, EntityLivingBase target) {
        return Targeting.isValidTarget(getTargetType(), caster, target, !canSelfCast());
    }

    public abstract void execute(EntityLivingBase entity, IMobData data, EntityLivingBase target, World theWorld);

    protected EntityLivingBase getSingleLivingTarget(EntityLivingBase caster, float distance) {
        return getSingleLivingTarget(caster, distance, true);
    }

    protected List<EntityLivingBase> getTargetsInLine(EntityLivingBase caster, Vec3d from, Vec3d to, boolean checkValid, float growth) {
        return RayTraceUtils.getEntitiesInLine(EntityLivingBase.class, caster, from, to, Vec3d.ZERO, growth,
                e -> !checkValid || (e != null && isValidTarget(caster, e)));
    }

    protected EntityLivingBase getSingleLivingTarget(EntityLivingBase caster, float distance, boolean checkValid) {
        return getSingleLivingTarget(EntityLivingBase.class, caster, distance, checkValid);
    }


    protected <E extends EntityLivingBase> E getSingleLivingTarget(Class<E> clazz, EntityLivingBase caster,
                                                                   float distance, boolean checkValid) {
        RayTraceResult lookingAt = RayTraceUtils.getLookingAt(clazz, caster, distance,
                e -> !checkValid || (e != null && isValidTarget(caster, e)));

        if (lookingAt != null && lookingAt.entityHit instanceof EntityLivingBase) {

            if (checkValid && !isValidTarget(caster, (EntityLivingBase) lookingAt.entityHit)) {
                return null;
            }

            return (E) lookingAt.entityHit;
        }

        return null;
    }

    @Nonnull
    protected EntityLivingBase getSingleLivingTargetOrSelf(EntityLivingBase caster, float distance, boolean checkValid) {
        EntityLivingBase target = getSingleLivingTarget(caster, distance, checkValid);
        return target != null ? target : caster;
    }
}
