package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.BaseToggleAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.effects.Targeting;
import com.chaosbuffalo.mkultra.effects.spells.VampiricReverePotion;
import com.chaosbuffalo.mkultra.effects.spells.WaveBreakPotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by Jacob on 3/25/2018.
 */
public class WaveBreak extends BaseToggleAbility {

    public static int BASE_DURATION = 32767;
    public static int DURATION_SCALE = 0;

    public WaveBreak() {
        super(MKUltra.MODID, "ability.wave_break");
    }

    @Override
    public String getAbilityName() {
        return "Wave Break";
    }

    @Override
    public String getAbilityDescription() {
        return "Toggle ability that gives you a large buff to health, armor, and magic armor and causes you to" +
                "absorb most of your teammates damage.";
    }

    @Override
    public ResourceLocation getAbilityIcon(){
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/wave_break.png");
    }

    @Override
    public String getAbilityType() {
        return "Self";
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 4 - currentLevel;
    }

    @Override
    public int getType() {
        return TOGGLE_ABILITY;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.SELF;
    }

    @Override
    public int getManaCost(int currentLevel) {
        return 3 - currentLevel;
    }

    @Override
    public float getDistance(int currentLevel) {
        return 1.0f;
    }

    @Override
    public Potion getToggleEffect() { return WaveBreakPotion.INSTANCE; }

    @Override
    public int getRequiredLevel(int currentLevel) {
        return 6 + currentLevel * 2;
    }

    @Override
    public void applyEffect(EntityPlayer entity, IPlayerData pData, World theWorld) {

        int level = pData.getLevelForAbility(getAbilityId());

        // What to do for each target hit
        entity.addPotionEffect(WaveBreakPotion.Create(entity).setTarget(entity).toPotionEffect(BASE_DURATION, level));
        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.WATER_DROP.getParticleID(),
                        ParticleEffects.CIRCLE_MOTION, 50, 0,
                        entity.posX, entity.posY + 1.5,
                        entity.posZ, 1.0, 1.0, 1.0, 1.0f,
                        lookVec),
                entity, 50.0f);

    }
}
