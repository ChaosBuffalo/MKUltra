package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.CastState;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.effects.spells.FireArrowPotion;
import com.chaosbuffalo.mkultra.entities.projectiles.SpellCastArrow;
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

public class FireArrow extends PlayerAbility {

    public static float BASE_ARROW_DAMAGE = 2.0f;
    public static float SCALE_ARROW_DAMAGE = 2.0f;
    public static float BASE_DAMAGE = 1.0f;
    public static float DAMAGE_SCALE = 1.0f;

    public FireArrow() {
        super(MKUltra.MODID, "ability.fire_arrow");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 5 - currentRank;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public float getManaCost(int currentRank) {
        return 5 - currentRank;
    }


    @Override
    public int getRequiredLevel(int currentRank) {
        return currentRank * 2;
    }

    @Override
    public int getCastTime(int currentRank) {
        return GameConstants.TICKS_PER_SECOND / 4;
    }

    @Override
    public void endCast(EntityPlayer entity, IPlayerData data, World theWorld, CastState state) {
        super.endCast(entity, data, theWorld, state);
        int level = data.getAbilityRank(getAbilityId());
        ItemStack mainHand = entity.getHeldItemMainhand();
        if (mainHand.isEmpty())
            return;

        RangedWeaponry.IRangedWeapon weapon = RangedWeaponry.findWeapon(mainHand);
        if (weapon == null)
            return;

        ItemStack ammo = weapon.findAmmo(entity);
        if (ammo.isEmpty())
            return;
        EntityArrow entityarrow = weapon.createAmmoEntity(theWorld, ammo, entity);
        SpellCastArrow arrow = new SpellCastArrow(theWorld, entity);
        weapon.applyEffects(arrow, mainHand, ammo);
        arrow.shoot(entity, entity.rotationPitch, entity.rotationYaw, 0.0F,
                3.0F, 1.0F);
        arrow.setDamage(entityarrow.getDamage() + BASE_ARROW_DAMAGE + level * SCALE_ARROW_DAMAGE);
        arrow.addSpellCast(FireArrowPotion.Create(entity, BASE_DAMAGE, DAMAGE_SCALE, 10.0f), 1);
        arrow.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
        theWorld.spawnEntity(arrow);

        weapon.consumeAmmo(entity, ammo, 1);

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.LAVA.getParticleID(),
                        ParticleEffects.CIRCLE_PILLAR_MOTION, 40, 10,
                        entity.posX, entity.posY + 0.05,
                        entity.posZ, 1.0, 1.0, 1.0, 1.5,
                        lookVec),
                entity.dimension, entity.posX,
                entity.posY, entity.posZ, 50.0f);

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
