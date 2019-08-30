package com.chaosbuffalo.mkultra.core.mob_abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MobAbility;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.spells.FlameBladePotion;
import com.chaosbuffalo.mkultra.effects.spells.ParticlePotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FlameBlade extends MobAbility {


    public FlameBlade() {
        super(MKUltra.MODID, "mob_ability.flame_blade");
    }

    @Override
    public int getCooldown() {
        return 30 * GameConstants.TICKS_PER_SECOND;
    }

    @Override
    public AbilityType getAbilityType() {
        return AbilityType.BUFF;
    }

    @Override
    public boolean canSelfCast() {
        return true;
    }

    @Override
    public float getDistance() {
        return 10.0f;
    }

    @Override
    public int getCastTime() {
        return GameConstants.TICKS_PER_SECOND / 2;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.FRIENDLY;
    }

    public int getDuration() {
        return 20 * GameConstants.TICKS_PER_SECOND;
    }

    @Override
    public Potion getEffectPotion() {
        return FlameBladePotion.INSTANCE;
    }

    @Override
    public boolean shouldCast(EntityLivingBase caster, EntityLivingBase target) {
        if (caster instanceof EntityCreature) {
            EntityCreature creature = (EntityCreature) caster;
            if (creature.getAttackTarget() == null) {
                return false;
            }
        }
        return super.shouldCast(caster, target);
    }

    @Override
    public void execute(EntityLivingBase entity, IMobData data, EntityLivingBase target, World theWorld) {
        int duration = getDuration();
        SpellCast effect = FlameBladePotion.Create(entity);
        SpellCast particlePotion = ParticlePotion.Create(entity,
                EnumParticleTypes.DRIP_LAVA.getParticleID(),
                ParticleEffects.DIRECTED_SPOUT, false,
                new Vec3d(1.0, 1.5, 1.0),
                new Vec3d(0.0, 1.0, 0.0),
                40, 5, 1.0);

        AreaEffectBuilder.Create(entity, entity)
                .spellCast(effect, duration, data.getMobLevel() / 2, getTargetType())
                .spellCast(particlePotion, data.getMobLevel() / 2, getTargetType())
                .instant()
                .particle(EnumParticleTypes.FIREWORKS_SPARK)
                .color(16737330).radius(getDistance(), true)
                .spawn();

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.DRIP_LAVA.getParticleID(),
                        ParticleEffects.CIRCLE_MOTION, 50, 0,
                        entity.posX, entity.posY + 1.5,
                        entity.posZ, 1.0, 1.0, 1.0, 1.0f,
                        lookVec),
                entity, 50.0f);
    }
}





