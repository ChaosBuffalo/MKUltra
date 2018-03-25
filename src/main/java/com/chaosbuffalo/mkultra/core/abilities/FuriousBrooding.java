package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.Targeting;
import com.chaosbuffalo.mkultra.core.BaseAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FuriousBrooding extends BaseAbility {

    public FuriousBrooding() {
        super(MKUltra.MODID, "ability.furious_brooding");
    }

    @Override
    public String getAbilityName() {
        return "Furious Brooding";
    }

    @Override
    public String getAbilityDescription() {
        return "Regenerates health while slowing the caster for a short period.";
    }

    @Override
    public String getAbilityType() {
        return "Self";
    }

    @Override
    public int getIconU() {
        return 36;
    }

    @Override
    public ResourceLocation getAbilityIcon(){
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/furiousbrooding.png");
    }


    @Override
    public int getIconV() {
        return 18;
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 30;
    }

    @Override
    public int getType() {
        return ACTIVE_ABILITY;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.SELF;
    }

    @Override
    public int getManaCost(int currentLevel) {
        return 4 + 3 * currentLevel;
    }

    @Override
    public float getDistance(int currentLevel) {
        return 1.0f;
    }

    @Override
    public int getRequiredLevel(int currentLevel) {
        return currentLevel * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {

        int level = pData.getLevelForAbility(getAbilityId());

        pData.startAbility(this);

        entity.addPotionEffect(
                new PotionEffect(MobEffects.REGENERATION, (10 + 5 * level) * 20, level, false, true));
        entity.addPotionEffect(
                new PotionEffect(MobEffects.SLOWNESS, (10 + 5 * level) * 20, level - 1, false, true));

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.VILLAGER_ANGRY.getParticleID(),
                        ParticleEffects.DIRECTED_SPOUT, 60, 1,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, 1.0,
                        lookVec),
                entity.dimension, entity.posX,
                entity.posY, entity.posZ, 50.0f);
    }
}



