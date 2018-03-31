package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.Targeting;
import com.chaosbuffalo.mkultra.effects.spells.DrownPotion;
import com.chaosbuffalo.mkultra.core.BaseAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MassDrown extends BaseAbility {

    public MassDrown() {
        super(MKUltra.MODID, "ability.mass_drown");
    }

    @Override
    public String getAbilityName() {
        return "Mass Drown";
    }

    @Override
    public String getAbilityDescription() {
        return "Spreads drown from the target to surrounding targets.";
    }

    @Override
    public String getAbilityType() {
        return "Single Target";
    }

    @Override
    public ResourceLocation getAbilityIcon(){
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/massdrown.png");
    }


    @Override
    public int getIconU() {
        return 162;
    }

    @Override
    public int getIconV() {
        return 36;
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 30 - 10 * currentLevel;
    }

    @Override
    public int getType() {
        return ACTIVE_ABILITY;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public int getManaCost(int currentLevel) {
        return 8 + currentLevel * 4;
    }

    @Override
    public float getDistance(int currentLevel) {
        return 10.0f + 5.0f * currentLevel;
    }

    @Override
    public int getRequiredLevel(int currentLevel) {
        return 6 + currentLevel * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        int level = pData.getLevelForAbility(getAbilityId());

        EntityLivingBase targetEntity = getSingleLivingTarget(entity, getDistance(level));
        if (targetEntity != null) {

            // If the target does not have Drown already, this is an AoE of level 1 drown.
            // If the target does have Drown then this is an AoE Drown equal to the level of MassDrown
            int effectiveLevel = 1;
            if (targetEntity.isPotionActive(DrownPotion.INSTANCE)) {
                effectiveLevel = level;
            }

            pData.startAbility(this);
            SpellCast drown = DrownPotion.Create(entity);

            AreaEffectBuilder.Create(entity, targetEntity)
                    .instant()
                    .spellCast(drown, 20 * 3, effectiveLevel, getTargetType())
                    .color(65480).radius(5.0f + 2.0f * effectiveLevel, true)
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
