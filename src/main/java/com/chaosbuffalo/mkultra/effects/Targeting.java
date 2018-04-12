package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkultra.log.Log;
import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Team;

import java.util.Set;

public class Targeting {

    public enum TargetType {
        ALL,
        ENEMY,
        FRIENDLY,
        PLAYERS,
        SELF,
    }

    private static Set<String> friendlyEntityTypes = Sets.newHashSet();

    public static void registerFriendlyEntity(String className) {
        friendlyEntityTypes.add(className);
    }

    public static boolean isValidTarget(TargetType type, Entity caster, EntityLivingBase target, boolean excludeCaster) {

        if (excludeCaster && caster.isEntityEqual(target)) {
            return false;
        }

        switch (type) {
            case ALL:
                return true;
            case SELF:
                return caster.isEntityEqual(target);
            case PLAYERS:
                return target instanceof EntityPlayer;
            case FRIENDLY:
                return isValidFriendly(caster, target);
            case ENEMY:
                return isValidEnemy(caster, target);
        }
        return false;
    }

    private static boolean checkFriendlyLiving(EntityLiving target) {
        return target instanceof EntityVillager ||
                target instanceof EntityIronGolem;
    }


    private static boolean isRegisteredFriendly(Entity living) {
        return friendlyEntityTypes.contains(living.getClass().getName());
    }

    private static boolean isValidFriendlyCreature(Entity caster, EntityLivingBase target) {

        if (target instanceof EntityLiving) {
            EntityLiving otherMob = (EntityLiving) target;
            return checkFriendlyLiving(otherMob) || isRegisteredFriendly(target);
        }

        return false;
    }

    private static boolean isSameTeam(Entity caster, Entity target) {
        Team myTeam = caster.getTeam();
        Team otherTeam = target.getTeam();
        return myTeam != null && otherTeam != null && myTeam.isSameTeam(otherTeam);
    }

    private static boolean isFriendlyPlayer(Entity caster, Entity target) {

        if (caster.isEntityEqual(target))
            return true;

        // Currently the only friendly player check is if the caster and target are on the same team
        if (isSameTeam(caster, target))
            return true;

        return false;
    }

    private static boolean isFriendlyPlayerControlled(Entity caster, Entity target) {
        Entity controller = target.getControllingPassenger();
        if (controller instanceof EntityPlayer) {
            return isValidFriendly(caster, controller);
        }

        if (target instanceof IEntityOwnable) {
            Entity owner = ((IEntityOwnable)target).getOwner();

            if (owner != null) {
                return isValidFriendly(caster, owner);
            }
            else {
                Log.info("isFriendlyPlayerControlled: owner was null!");
            }
        }

        return false;
    }


    private static boolean isValidFriendly(Entity caster, Entity target) {

        // Always friendly with ourselves
        if (caster.isEntityEqual(target)) {
            return true;
        }

        // Always friendly with whitelisted entities
        if (isRegisteredFriendly(target)) {
            return true;
        }

        // Always friendly with entities on the same team
        if (isSameTeam(caster, target)) {
            return true;
        }

        if (isFriendlyPlayer(caster, target))
            return true;

        if (isFriendlyPlayerControlled(caster, target))
            return true;

        if (target instanceof EntityLivingBase) {
            EntityLivingBase targetLiving = (EntityLivingBase) target;
            if (isValidFriendlyCreature(caster, targetLiving))
                return true;
        }

        return false;
    }

    private static boolean isValidEnemy(Entity caster, EntityLivingBase target) {
        return !isValidFriendly(caster, target);
    }

}
