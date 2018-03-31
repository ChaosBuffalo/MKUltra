package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.Targeting;
import com.chaosbuffalo.mkultra.effects.spells.YankPotion;
import com.chaosbuffalo.mkultra.core.BaseAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Yank extends BaseAbility {

    public static float BASE_FORCE = 1.0f;
    public static float FORCE_SCALE = 1.0f;

    public Yank() {
        super(MKUltra.MODID, "ability.yank");
    }

    @Override
    public String getAbilityName() {
        return "Yank";
    }

    @Override
    public String getAbilityDescription() {
        return "Pulls an enemy or ally towards you.";
    }

    @Override
    public ResourceLocation getAbilityIcon(){
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/yank.png");
    }

    @Override
    public String getAbilityType() {
        return "Target";
    }

    @Override
    public int getIconU() {
        return 216;
    }

    @Override
    public int getIconV() {
        return 0;
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 5;
    }

    @Override
    public int getType() {
        return ACTIVE_ABILITY;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ALL;
    }

    @Override
    public int getManaCost(int currentLevel) {
        return 4 - currentLevel;
    }

    @Override
    public float getDistance(int currentLevel) {
        return 10.0f;
    }

    @Override
    public int getRequiredLevel(int currentLevel) {
        return currentLevel * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        int level = pData.getLevelForAbility(getAbilityId());

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
