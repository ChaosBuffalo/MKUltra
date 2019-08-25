package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.spells.YankPotion;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Yank extends PlayerAbility {

    public static float BASE_FORCE = 1.0f;
    public static float FORCE_SCALE = .75f;

    public Yank() {
        super(MKUltra.MODID, "ability.yank");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 5;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ALL;
    }

    @Override
    public int getManaCost(int currentRank) {
        return 4 - currentRank;
    }

    @Override
    public float getDistance(int currentRank) {
        return 5.0f + 2 * 5.0f;
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return currentRank * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        int level = pData.getAbilityRank(getAbilityId());

        EntityLivingBase targetEntity = getSingleLivingTarget(entity, getDistance(level));
        if (targetEntity != null) {
            pData.startAbility(this);

            targetEntity.addPotionEffect(YankPotion.Create(entity, targetEntity).toPotionEffect(level));

            Vec3d partHeading = targetEntity.getPositionVector()
                    .add(new Vec3d(0.0, 1.0, 0.0))
                    .subtract(entity.getPositionVector())
                    .normalize();
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.SPELL_INSTANT.getParticleID(),
                            ParticleEffects.DIRECTED_SPOUT, 50, 1,
                            entity.posX, entity.posY + 1.0,
                            entity.posZ, .25, .25, .25, 5.0,
                            partHeading),
                    entity, 50.0f);
        }
    }
}
