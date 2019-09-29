package com.chaosbuffalo.mkultra.core.abilities.ranger;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKDamageSource;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.PlayerFormulas;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.spells.ParticlePotion;
import com.chaosbuffalo.mkultra.effects.spells.SlayingEdgePotion;
import com.chaosbuffalo.mkultra.effects.spells.SoundPotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkultra.utils.AbilityUtils;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Jacob on 6/23/2018.
 */
public class SlayingEdge extends PlayerAbility {

    public static float BASE_DAMAGE = 10.0f;
    public static float DAMAGE_SCALE = 5.0f;

    public SlayingEdge() {
        super(MKUltra.MODID, "ability.slaying_edge");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 10 - 2 * currentRank;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public float getManaCost(int currentRank) {
        return 4 + currentRank * 2;
    }

    @Override
    public float getDistance(int currentRank) {
        return 5.0f + 3.0f * currentRank;
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return 6 + currentRank * 2;
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_shadow_8;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        int level = pData.getAbilityRank(getAbilityId());

        EntityLivingBase targetEntity = getSingleLivingTarget(entity, getDistance(level));
        if (targetEntity != null) {
            pData.startAbility(this);
            targetEntity.attackEntityFrom(
                    MKDamageSource.fromMeleeSkill(getAbilityId(), entity, entity),
                    BASE_DAMAGE + DAMAGE_SCALE * level);
            AbilityUtils.playSoundAtServerEntity(targetEntity, ModSounds.spell_shadow_8, SoundCategory.PLAYERS);
            if (!targetEntity.isEntityAlive()) {
                SpellCast slaying_edge = SlayingEdgePotion.Create(entity);
                SpellCast particlePotion = ParticlePotion.Create(entity,
                        EnumParticleTypes.SPELL_MOB.getParticleID(),
                        ParticleEffects.SPHERE_MOTION, false, new Vec3d(1.0, 1.0, 1.0),
                        new Vec3d(0.0, 1.0, 0.0), 20, 5, 0.5);

                int duration = 10 * GameConstants.TICKS_PER_SECOND;
                duration = PlayerFormulas.applyBuffDurationBonus(pData, duration);
                AreaEffectBuilder.Create(entity, entity)
                        .spellCast(slaying_edge, duration, level, Targeting.TargetType.FRIENDLY)
                        .spellCast(particlePotion, 0, Targeting.TargetType.FRIENDLY)
                        .spellCast(SoundPotion.Create(entity, ModSounds.spell_shadow_8, SoundCategory.PLAYERS),
                                1, Targeting.TargetType.FRIENDLY)
                        .instant().color(10158335).radius(getDistance(level), true)
                        .spawn();
            }
            Vec3d lookVec = entity.getLookVec();
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.SPELL_MOB.getParticleID(),
                            ParticleEffects.SPHERE_MOTION, 30, 10,
                            targetEntity.posX, targetEntity.posY + 1.0,
                            targetEntity.posZ, 1.0, 1.0, 1.0, 1.0,
                            lookVec),
                    entity.dimension, targetEntity.posX,
                    targetEntity.posY, targetEntity.posZ, 50.0f);
        }
    }
}

