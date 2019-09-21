package com.chaosbuffalo.mkultra.entities.projectiles;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.abilities.Meteor;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.spells.AIStunPotion;
import com.chaosbuffalo.mkultra.effects.spells.MeteorEffectPotion;
import com.chaosbuffalo.mkultra.effects.spells.MobFireballEffectPotion;
import com.chaosbuffalo.mkultra.effects.spells.ParticlePotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityMeteorProjectile extends EntityBaseProjectile {

    public EntityMeteorProjectile(World worldIn) {
        super(worldIn);
    }

    private boolean shouldPassThrough;

    @Override
    public void setup() {
        super.setup();
        this.setDeathTime(GameConstants.TICKS_PER_SECOND * 5);
        this.setSize(0.3f, 0.3f);
        setGraphicalEffectTickInterval(1);
        shouldPassThrough = true;
        this.setDoAirProc(true);
        this.setDoGroundProc(true);
        this.setGroundProcTime(GameConstants.TICKS_PER_SECOND / 2);
        this.setAirProcTime(GameConstants.TICKS_PER_SECOND / 2);
    }

    @Override
    protected boolean onAirProc(EntityLivingBase caster, int amplifier) {
        shouldPassThrough = false;
        setDoAirProc(false);
        return super.onAirProc(caster, amplifier);
    }

    public EntityMeteorProjectile(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }

    public EntityMeteorProjectile(World worldIn, EntityLivingBase throwerIn, double offset) {
        super(worldIn, throwerIn, offset);
    }

    @Override
    protected boolean canPassThroughBlock(Block block) {
        return shouldPassThrough || super.canPassThroughBlock(block);
    }

    @Override
    public void clientGraphicalUpdate() {
        super.clientGraphicalUpdate();
        Vec3d motion = new Vec3d(0, 0, 0);
        ParticleEffects.spawnParticle(EnumParticleTypes.FLAME.ordinal(), 0.0, getPositionVector(), motion, world);
        ParticleEffects.spawnParticle(EnumParticleTypes.SMOKE_LARGE.ordinal(), 0.0, getPositionVector(), motion, world);
    }

    @Override
    protected boolean onGroundProc(EntityLivingBase caster, int amplifier) {
        doEffect(caster, amplifier);
        return true;
    }

    private void doEffect(EntityLivingBase caster, int level){
        SpellCast damage = MeteorEffectPotion.Create(caster, Meteor.BASE_DAMAGE, Meteor.DAMAGE_SCALE);
        SpellCast stunPotion = AIStunPotion.Create(caster);
        SpellCast particlePotion = ParticlePotion.Create(caster,
                EnumParticleTypes.LAVA.getParticleID(),
                ParticleEffects.SPHERE_MOTION, false,
                new Vec3d(1.0, 1.0, 1.0),
                new Vec3d(0.0, 1.0, 0.0),
                8, 4, 0.1);


        AreaEffectBuilder.Create(caster, this)
                .spellCast(damage, level, getTargetType())
                .spellCast(particlePotion, level, getTargetType())
                .spellCast(stunPotion, GameConstants.TICKS_PER_SECOND * level,
                        1, getTargetType())
                .instant()
                .disableParticle()
                .radius(4, true)
                .spawn();
    }

    @Override
    protected boolean onImpact(EntityLivingBase entity, RayTraceResult result, int level) {

        if (world.isRemote) {
            // No client code
            return false;
        }
        if (result.typeOfHit == RayTraceResult.Type.ENTITY){
            doEffect(entity, level);
            return true;
        }


        return false;
    }

    @Override
    protected Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    protected boolean shouldExcludeCaster() {
        return true;
    }
}
