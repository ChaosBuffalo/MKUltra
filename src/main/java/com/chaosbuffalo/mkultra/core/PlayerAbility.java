package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.utils.RayTraceUtils;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.regex.Pattern;

public abstract class  PlayerAbility extends IForgeRegistryEntry.Impl<PlayerAbility> {

    public static final int ACTIVE_ABILITY = 0;
    public static final int TOGGLE_ABILITY = 1;
    public static final int PASSIVE_ABILITY = 2;

    private ResourceLocation abilityId;

    public PlayerAbility(String domain, String id) {
        this(new ResourceLocation(domain, id));
    }

    public PlayerAbility(ResourceLocation abilityId) {
        this.abilityId = abilityId;
    }


    public ResourceLocation getAbilityId() {
        return abilityId;
    }

    @SideOnly(Side.CLIENT)
    public String getAbilityName()
    {
        return I18n.format(String.format("%s.%s.name", abilityId.getNamespace(), abilityId.getPath()));
    }

    @SideOnly(Side.CLIENT)
    public String getAbilityDescription()
    {
        return I18n.format(String.format("%s.%s.description", abilityId.getNamespace(), abilityId.getPath()));
    }


    public ResourceLocation getAbilityIcon()
    {
        return new ResourceLocation(abilityId.getNamespace(), String.format("textures/class/abilities/%s.png", abilityId.getPath().split(Pattern.quote("."))[1]));
    }


    public float getDistance(int currentRank) {
        return 1.0f;
    }

    public abstract int getCooldown(int currentRank);

    public int getCooldownTicks(int currentRank) {
        return getCooldown(currentRank) * GameConstants.TICKS_PER_SECOND;
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

    public abstract float getManaCost(int currentRank);

    public abstract int getRequiredLevel(int currentRank);

    public int getMaxRank() {
        return GameConstants.MAX_ABILITY_RANK;
    }

    public boolean meetsRequirements(IPlayerData player) {
        return player.getMana() >=
                PlayerFormulas.applyManaCostReduction(player, getManaCost(player.getAbilityRank(abilityId))) &&
                player.getCurrentAbilityCooldown(abilityId) == 0;
    }

    public abstract void execute(EntityPlayer entity, IPlayerData data, World theWorld);

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
