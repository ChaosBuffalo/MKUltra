package com.chaosbuffalo.mkultra.entities.projectiles;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.Targeting;
import com.chaosbuffalo.mkultra.effects.spells.ClericHealPotion;
import com.chaosbuffalo.mkultra.effects.spells.InstantIndirectMagicDamagePotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by Jacob on 3/24/2018.
 */
public class EntityDualityRuneProjectile extends EntityBaseProjectile {

        private final static float BASE = 5.0f;
        private final static float SCALE = 5.0f;

        public EntityDualityRuneProjectile(World worldIn)
        {
            super(worldIn);
        }

        public EntityDualityRuneProjectile(World worldIn, EntityLivingBase throwerIn)
        {
            super(worldIn, throwerIn);

            this.setDeathTime(60);
            this.setDoSecondary(true);
            this.setSecondaryProcTime(30);
        }

        public EntityDualityRuneProjectile(World worldIn, double x, double y, double z)
        {
            super(worldIn, x, y, z);
        }


        @Override
        protected void onImpact(EntityLivingBase entity, RayTraceResult result, int level) {


        }

        @Override
        protected void onSecondaryProc(EntityLivingBase caster, int amplifier) {
            if (!this.world.isRemote && caster != null)
            {
                SpellCast heal = ClericHealPotion.Create(caster, BASE, SCALE);
                SpellCast damage = InstantIndirectMagicDamagePotion.Create(caster, BASE, SCALE);

                AreaEffectBuilder.Create(caster, this)
                        .spellCast(heal, amplifier, Targeting.TargetType.FRIENDLY)
                        .spellCast(damage, amplifier, Targeting.TargetType.ENEMY)
                        .duration(6).waitTime(0)
                        .color(39935).radius(4.0f, true)
                        .spawn();
                MKUltra.packetHandler.sendToAllAround(
                        new ParticleEffectSpawnPacket(
                                EnumParticleTypes.SPELL_WITCH.getParticleID(),
                                ParticleEffects.DIRECTED_SPOUT, 100, 1,
                                this.posX, this.posY + 1.0,
                                this.posZ, 1.5, 2.0, 1.5, 1.0,
                                new Vec3d(0., 1.0, 0.0)),
                        this.dimension, this.posX, this.posY, this.posZ, 50.0f);
                this.setDead();
            }


        }

        @Override
        protected void onAirProc(EntityLivingBase caster, int amplifier) {

        }
    }
