package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerFormulas;
import com.chaosbuffalo.mkultra.effects.spells.ShieldingPotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by Jacob on 6/23/2018.
 */
public class DesperateSurge extends PlayerAbility {

    public static int BASE_DURATION = 6;
    public static int DURATION_SCALE = 2;
    public static int BASE_SHIELDING = 0;
    public static int SHIELDING_SCALE = 2;
    public static int FOOD_COST = 2;
    public static int FOOD_SCALE = 2;

    public DesperateSurge() {
        super(MKUltra.MODID, "ability.desperate_surge");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 16 - 2 * currentRank;
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
    public int getManaCost(int currentRank) {
        return 4 + 2 * currentRank;
    }

    @Override
    public float getDistance(int currentRank) {
        return 1.0f;
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return 2 + currentRank * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        int level = pData.getAbilityRank(getAbilityId());
        if (entity.getFoodStats().getFoodLevel() >= FOOD_COST + level * FOOD_SCALE){
            pData.startAbility(this);
            entity.getFoodStats().setFoodLevel(entity.getFoodStats().getFoodLevel() - (FOOD_COST + level * FOOD_SCALE));
            int duration = (BASE_DURATION + DURATION_SCALE * level) * GameConstants.TICKS_PER_SECOND;
            duration = PlayerFormulas.applyBuffDurationBonus(pData, duration);
            entity.addPotionEffect(ShieldingPotion.Create(entity).setTarget(entity).toPotionEffect(
                            duration, BASE_SHIELDING + level * SHIELDING_SCALE));
            entity.addPotionEffect(new PotionEffect(MobEffects.SPEED, duration * 2, 2 + level));
            float healAmount = PlayerFormulas.applyHealBonus(pData, 2.0f * level);
            entity.heal(healAmount);
            Vec3d lookVec = entity.getLookVec();
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.SMOKE_NORMAL.getParticleID(),
                            ParticleEffects.CIRCLE_MOTION, 25, 0,
                            entity.posX, entity.posY + 1.5,
                            entity.posZ, 1.0, 1.0, 1.0, 1.0f,
                            lookVec),
                    entity, 50.0f);
        }
        // What to do for each target hit

    }
}

