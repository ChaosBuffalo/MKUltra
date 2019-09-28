package com.chaosbuffalo.mkultra.core.abilities.ranger;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.PlayerFormulas;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by Jacob on 5/20/2018.
 */
public class PracticedHunter extends PlayerAbility {
    public static int BASE_DURATION = 15;
    public static int DURATION_SCALE = 15;
    public static int FOOD_AMOUNT = 12;
    public static int FOOD_SCALE = 4;
    public static float FOOD_SATURATION = 6.0f;
    public static float FOOD_SATURATION_SCALE = 4.0f;

    public PracticedHunter() {
        super(MKUltra.MODID, "ability.practiced_hunter");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 60 - currentRank * DURATION_SCALE;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.SELF;
    }

    @Override
    public float getManaCost(int currentRank) {
        return 2 * currentRank;
    }

    @Override
    public float getDistance(int currentRank) {
        return 1.0f;
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return currentRank * 2;
    }

    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_cast_2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        int level = pData.getAbilityRank(getAbilityId());
        pData.startAbility(this);
        int duration = (BASE_DURATION + DURATION_SCALE * level) * GameConstants.TICKS_PER_SECOND;
        duration = PlayerFormulas.applyBuffDurationBonus(pData, duration);
        entity.addPotionEffect(new PotionEffect(
                MobEffects.NIGHT_VISION,
                duration,
                0, false, false)
        );
        entity.getFoodStats().addStats(FOOD_AMOUNT + level * FOOD_SCALE,
                FOOD_SATURATION + level * FOOD_SATURATION_SCALE);
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
