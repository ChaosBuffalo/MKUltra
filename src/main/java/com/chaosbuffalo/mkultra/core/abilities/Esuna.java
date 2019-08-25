package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.PlayerFormulas;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.spells.CurePotion;
import com.chaosbuffalo.mkultra.effects.spells.EsunaPotion;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Esuna extends PlayerAbility {

    public Esuna() {
        super(MKUltra.MODID, "ability.esuna");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 25 - currentRank * 5;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.FRIENDLY;
    }

    @Override
    public float getManaCost(int currentRank) {
        return 12 - currentRank * 3;
    }


    @Override
    public float getDistance(int currentRank) {
        return 10.0f;
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return 4 + currentRank * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);

        int level = pData.getAbilityRank(getAbilityId());

        // What to do for each target hit
        SpellCast esuna = EsunaPotion.Create(entity);
        SpellCast cure = CurePotion.Create(entity);

        int esunaDuration = PlayerFormulas.applyBuffDurationBonus(pData, 4 * GameConstants.TICKS_PER_SECOND);
        AreaEffectBuilder.Create(entity, entity)
                .spellCast(esuna, esunaDuration, level, getTargetType())
                .spellCast(cure, level, getTargetType())
                .instant()
                .color(65480).radius(getDistance(level), true)
                .spawn();

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.DRIP_WATER.getParticleID(),
                        ParticleEffects.SPHERE_MOTION, 50, 6,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, 1.5,
                        lookVec),
                entity, 50.0f);
    }
}
