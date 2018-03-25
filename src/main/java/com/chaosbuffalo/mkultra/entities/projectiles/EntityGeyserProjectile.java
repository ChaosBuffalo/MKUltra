package com.chaosbuffalo.mkultra.entities.projectiles;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.Targeting;
import com.chaosbuffalo.mkultra.effects.spells.GeyserPotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by Jacob on 7/15/2016.
 */
public class EntityGeyserProjectile extends EntityBaseProjectile {
        public EntityGeyserProjectile(World worldIn)
        {
            super(worldIn);
        }

        public EntityGeyserProjectile(World worldIn, EntityLivingBase throwerIn)
        {
            super(worldIn, throwerIn);

            this.setDeathTime(1200);
            this.setDoGroundProc(true);
            this.setGroundProcTime(50);
        }

        public EntityGeyserProjectile(World worldIn, double x, double y, double z)
        {
            super(worldIn, x, y, z);
        }


        @Override
        protected boolean onGroundProc(EntityLivingBase caster, int amplifier) {
            if (!this.world.isRemote && caster != null)
            {
                SpellCast geyser = GeyserPotion.Create(caster, 0.0f, 10.0f);

                AreaEffectBuilder.Create(caster, this)
                        .spellCast(geyser, amplifier, Targeting.TargetType.ALL)
                        .duration(6).waitTime(0)
                        .color(39935).radius(4.0f, true)
                        .spawn();
                MKUltra.packetHandler.sendToAllAround(
                        new ParticleEffectSpawnPacket(
                                EnumParticleTypes.DRIP_WATER.getParticleID(),
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


