package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.utils.RayTraceUtils;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.regex.Pattern;

public abstract class BaseAbility extends IForgeRegistryEntry.Impl<BaseAbility> {

    public static final int ACTIVE_ABILITY = 0;
    public static final int TOGGLE_ABILITY = 1;

    private ResourceLocation abilityId;

    public BaseAbility(String domain, String id) {
        this(new ResourceLocation(domain, id));
    }

    public BaseAbility(ResourceLocation abilityId) {
        this.abilityId = abilityId;
    }


    public ResourceLocation getAbilityId() {
        return abilityId;
    }

    public String getAbilityName()
    {
        return I18n.format(String.format("%s.%s.name", abilityId.getResourceDomain(), abilityId.getResourcePath()));
    }

    public String getAbilityDescription()
    {
        return I18n.format(String.format("%s.%s.description", abilityId.getResourceDomain(), abilityId.getResourcePath()));
    }


    public ResourceLocation getAbilityIcon()
    {
        return new ResourceLocation(abilityId.getResourceDomain(), String.format("textures/class/abilities/%s.png", abilityId.getResourcePath().split(Pattern.quote("."))[1]));
    }


    public float getDistance(int currentLevel) {
        return 1.0f;
    }

    public abstract int getCooldown(int currentLevel);

    public int getCooldownTicks(int currentLevel) {
        return getCooldown(currentLevel) * GameConstants.TICKS_PER_SECOND;
    }

    public int getType() {
        return ACTIVE_ABILITY;
    }

    public abstract Targeting.TargetType getTargetType();

    public boolean canSelfCast() {
        return false;
    }

    protected boolean isValidTarget(EntityLivingBase caster, EntityLivingBase target) {
        return Targeting.isValidTarget(getTargetType(), caster, target, !canSelfCast());
    }

    public abstract int getManaCost(int currentLevel);

    public abstract int getRequiredLevel(int currentLevel);

    public int getMaxLevel() {
        return GameConstants.MAX_ABILITY_LEVEL;
    }

    public boolean meetsRequirements(IPlayerData player) {
        return player.getMana() >= getManaCost(player.getLevelForAbility(abilityId)) &&
                player.getCurrentAbilityCooldown(abilityId) == 0;
    }

    public abstract void execute(EntityPlayer entity, IPlayerData data, World theWorld);

    protected EntityLivingBase getSingleLivingTarget(EntityLivingBase caster, float distance) {
        return getSingleLivingTarget(caster, distance, true);
    }

    protected List<Entity> getTargetsInLine(EntityLivingBase caster, Vec3d from, Vec3d to, boolean checkValid) {
        return RayTraceUtils.getEntitiesInLine(caster, from, to, new Vec3d(0.0f, 0.0f, 0.0f), .25f,
                e -> !checkValid || (e instanceof EntityLivingBase && isValidTarget(caster, (EntityLivingBase) e)));
    }

    protected EntityLivingBase getSingleLivingTarget(EntityLivingBase caster, float distance, boolean checkValid) {
        RayTraceResult lookingAt = RayTraceUtils.getLookingAt(caster, distance,
                e -> !checkValid || (e instanceof EntityLivingBase && isValidTarget(caster, (EntityLivingBase) e)));

        if (lookingAt != null && lookingAt.entityHit instanceof EntityLivingBase) {

            if (checkValid && !isValidTarget(caster, (EntityLivingBase) lookingAt.entityHit)) {
                return null;
            }

            return (EntityLivingBase) lookingAt.entityHit;
        }

        return null;
    }

    @Nonnull
    protected EntityLivingBase getSingleLivingTargetOrSelf(EntityLivingBase caster, float distance, boolean checkValid) {
        EntityLivingBase target = getSingleLivingTarget(caster, distance, checkValid);
        return target != null ? target : caster;
    }
}
