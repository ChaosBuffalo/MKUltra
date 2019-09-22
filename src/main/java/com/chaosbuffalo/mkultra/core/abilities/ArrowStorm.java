package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.abilities.cast_states.CastState;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.item.RangedWeaponry;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ArrowStorm extends PlayerAbility {

    public static float BASE_DAMAGE = 4.0f;
    public static float DAMAGE_SCALE = 2.0f;
    public static int ARROW_PER_LEVEL = 4;

    public ArrowStorm() {
        super(MKUltra.MODID, "ability.arrow_storm");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 40 - 10 * currentRank;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public float getManaCost(int currentRank) {
        return 14 - currentRank * 2;
    }

    @Override
    public int getCastTime(int currentRank) {
        return GameConstants.TICKS_PER_SECOND / 2;
    }

    @Override
    public void endCast(EntityPlayer entity, IPlayerData data, World theWorld, CastState state) {
        super.endCast(entity, data, theWorld, state);
        ItemStack mainHand = entity.getHeldItemMainhand();
        if (mainHand.isEmpty())
            return;

        RangedWeaponry.IRangedWeapon weapon = RangedWeaponry.findWeapon(mainHand);
        if (weapon == null)
            return;

        ItemStack ammo = weapon.findAmmo(entity);
        if (ammo.isEmpty())
            return;

        int level = data.getAbilityRank(getAbilityId());
        int shootCount = ARROW_PER_LEVEL * level;
        for (int i = 0; i < shootCount; i++) {
            EntityArrow entityarrow = weapon.createAmmoEntity(theWorld, ammo, entity);
            weapon.applyEffects(entityarrow, mainHand, ammo);
            entityarrow.shoot(entity, entity.rotationPitch, entity.rotationYaw,
                    0.0F, 4.0F, 10.0F);
            entityarrow.setDamage(entityarrow.getDamage() + BASE_DAMAGE + level * DAMAGE_SCALE);
            entityarrow.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
            theWorld.spawnEntity(entityarrow);
        }

        weapon.consumeAmmo(entity, ammo, 1);

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.CRIT.getParticleID(),
                        ParticleEffects.DIRECTED_SPOUT, 20, 5,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, 1.5,
                        lookVec),
                entity.dimension, entity.posX,
                entity.posY, entity.posZ, 50.0f);
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return 6 + currentRank * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        ItemStack mainHand = entity.getHeldItemMainhand();
        if (mainHand.isEmpty())
            return;

        RangedWeaponry.IRangedWeapon weapon = RangedWeaponry.findWeapon(mainHand);
        if (weapon == null)
            return;

        ItemStack ammo = weapon.findAmmo(entity);
        if (ammo.isEmpty())
            return;

        pData.startAbility(this);
    }
}
