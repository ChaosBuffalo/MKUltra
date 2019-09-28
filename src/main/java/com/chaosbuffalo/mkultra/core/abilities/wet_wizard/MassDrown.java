package com.chaosbuffalo.mkultra.core.abilities.wet_wizard;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.spells.DrownPotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MassDrown extends PlayerAbility {

    public MassDrown() {
        super(MKUltra.MODID, "ability.mass_drown");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 30 - 10 * currentRank;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public float getManaCost(int currentRank) {
        return 8 + currentRank * 4;
    }

    @Override
    public float getDistance(int currentRank) {
        return 10.0f + 5.0f * currentRank;
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return 6 + currentRank * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        int level = pData.getAbilityRank(getAbilityId());

        EntityLivingBase targetEntity = getSingleLivingTarget(entity, getDistance(level));
        if (targetEntity != null) {

            // If the target does not have Drown already, this is an AoE of level 1 drown.
            // If the target does have Drown then this is an AoE Drown equal to the level of MassDrown
            // + 1
            int effectiveLevel = 1;
            if (targetEntity.isPotionActive(DrownPotion.INSTANCE)) {
                effectiveLevel = level + 1;
            }

            pData.startAbility(this);
            SpellCast drown = DrownPotion.Create(entity);

            AreaEffectBuilder.Create(entity, targetEntity)
                    .instant()
                    .spellCast(drown, 20 * 3, effectiveLevel, getTargetType())
                    .color(65480).radius(5.0f + 2.0f * level, true)
                    .spawn();
            Vec3d lookVec = entity.getLookVec();
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.WATER_DROP.getParticleID(),
                            ParticleEffects.DIRECTED_SPOUT, 60, 10,
                            targetEntity.posX, targetEntity.posY + 1.0,
                            targetEntity.posZ, 1.0, 1.0, 1.0, 1.0,
                            lookVec),
                    entity.dimension, targetEntity.posX,
                    targetEntity.posY, targetEntity.posZ, 50.0f);

        }
    }
}
