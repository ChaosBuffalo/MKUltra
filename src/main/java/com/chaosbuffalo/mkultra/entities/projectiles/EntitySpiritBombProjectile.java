package com.chaosbuffalo.mkultra.entities.projectiles;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.abilities.SpiritBomb;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.spells.AbilityMagicDamage;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by Jacob on 7/28/2018.
 */
public class EntitySpiritBombProjectile extends EntityBaseProjectile {



    public EntitySpiritBombProjectile(World worldIn) {
        super(worldIn);
    }

    public EntitySpiritBombProjectile(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);

        this.setDeathTime(60);
        this.setAirProcTime(40);
        this.setDoAirProc(true);
        this.setDoGroundProc(true);
        this.setGroundProcTime(40);
    }

    @Override
    protected boolean onAirProc(EntityLivingBase caster, int amplifier) {
        return doEffect(caster, amplifier);
    }

    public EntitySpiritBombProjectile(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    private boolean doEffect(EntityLivingBase caster, int amplifier){
        if (!this.world.isRemote && caster != null) {
            SpellCast damage = AbilityMagicDamage.Create(caster, SpiritBomb.BASE, SpiritBomb.SCALE);
            AreaEffectBuilder.Create(caster, this)
                    .spellCast(damage, amplifier, Targeting.TargetType.ENEMY)
                    .instant()
                    .color(65535).radius(4.0f, true)
                    .spawn();
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.SLIME.getParticleID(),
                            ParticleEffects.DIRECTED_SPOUT, 100, 1,
                            this.posX, this.posY + 1.0,
                            this.posZ, 1.5, 2.0, 1.5, 1.0,
                            new Vec3d(0., 1.0, 0.0)),
                    this.dimension, this.posX, this.posY, this.posZ, 50.0f);
            return true;
        }
        return false;
    }


    @Override
    protected boolean onGroundProc(EntityLivingBase caster, int amplifier) {
        return doEffect(caster, amplifier);
    }
}
