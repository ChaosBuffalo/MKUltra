package com.chaosbuffalo.mkultra.core.mob_abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MobAbility;
import com.chaosbuffalo.mkultra.effects.spells.FireArrowPotion;
import com.chaosbuffalo.mkultra.entities.projectiles.SpellCastArrow;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FireArrow extends MobAbility{
    private static float BASE_ARROW_DAMAGE = 2.0f;
    private static float SCALE_ARROW_DAMAGE = .5f;
    private static float BASE_DAMAGE = 3.0f;
    private static float DAMAGE_SCALE = .5f;


    public FireArrow() {
        super(MKUltra.MODID, "mob_ability.fire_arrow");
    }

    @Override
    public int getCooldown() {
        return 7 * GameConstants.TICKS_PER_SECOND;
    }

    @Override
    public int getCastTime(){
        return GameConstants.TICKS_PER_SECOND / 2;
    }

    @Override
    public MobAbility.AbilityType getAbilityType() {
        return MobAbility.AbilityType.ATTACK;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public float getDistance(){
        return 20.0f;
    }

    @Override
    public void execute(EntityLivingBase entity, IMobData data, EntityLivingBase target, World theWorld) {
        int level = data.getMobLevel();
        SpellCastArrow arrow = new SpellCastArrow(theWorld, entity);
        arrow.shoot(entity, entity.rotationPitch, entity.rotationYaw, 0.0F, 3.0F, 1.0F);
        arrow.setDamage(BASE_ARROW_DAMAGE + level * SCALE_ARROW_DAMAGE);
        arrow.addSpellCast(FireArrowPotion.Create(entity, BASE_DAMAGE, DAMAGE_SCALE, 10.0f), 1);
        arrow.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
        theWorld.spawnEntity(arrow);

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
}
