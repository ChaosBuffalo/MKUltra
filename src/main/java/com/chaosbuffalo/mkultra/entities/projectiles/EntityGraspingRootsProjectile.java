package com.chaosbuffalo.mkultra.entities.projectiles;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.spells.AbilityMagicDamage;
import com.chaosbuffalo.mkultra.effects.spells.GraspingRootsPotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityGraspingRootsProjectile extends EntityBaseProjectile {

    private final static float BASE = 1.0f;
    private final static float SCALE = 0.5f;

    public EntityGraspingRootsProjectile(World worldIn) {
        super(worldIn);
    }

    public EntityGraspingRootsProjectile(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
        this.setDeathTime(85);
        this.setDoGroundProc(true);
        this.setGroundProcTime(20);
    }

    @Override
    protected Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    public EntityGraspingRootsProjectile(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }


    @Override
    protected boolean onGroundProc(EntityLivingBase caster, int amplifier) {
        if (!this.world.isRemote && caster != null) {
            SpellCast grasping_roots = GraspingRootsPotion.Create(caster);
            SpellCast damage = AbilityMagicDamage.Create(caster, BASE, SCALE);

            AreaEffectBuilder.Create(caster, this)
                    .effect(grasping_roots.toPotionEffect(15, amplifier), Targeting.TargetType.ENEMY)
                    .spellCast(damage, amplifier, Targeting.TargetType.ENEMY)
                    .instant()
                    .color(3669880).radius(3.0f, true)
                    .spawn();
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID(),
                            ParticleEffects.DIRECTED_SPOUT, 100, 1,
                            this.posX, this.posY + 1.0,
                            this.posZ, 1.5, 2.0, 1.5, 1.0,
                            new Vec3d(0., 1.0, 0.0)),
                    this.dimension, this.posX, this.posY, this.posZ, 50.0f);
            return true;
        }

        return false;
    }

}
