package com.chaosbuffalo.mkultra.core.abilities.green_knight;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerToggleAbility;
import com.chaosbuffalo.mkultra.effects.spells.SkinLikeWoodPotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkultra.utils.AbilityUtils;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Jacob on 7/28/2018.
 */
public class SkinLikeWood extends PlayerToggleAbility {

    public static int BASE_DURATION = 32767;
    public static int DURATION_SCALE = 0;

    public SkinLikeWood() {
        super(MKUltra.MODID, "ability.skin_like_wood");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 4 - currentRank;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.SELF;
    }

    @Override
    public float getManaCost(int currentRank) {
        return 3 - currentRank;
    }

    @Override
    public float getDistance(int currentRank) {
        return 1.0f;
    }

    @Override
    public Potion getToggleEffect() {
        return SkinLikeWoodPotion.INSTANCE;
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return currentRank * 2;
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return null;
    }

    @Override
    public void applyEffect(EntityPlayer entity, IPlayerData pData, World theWorld) {
        super.applyEffect(entity, pData, theWorld);
        int level = pData.getAbilityRank(getAbilityId());
        AbilityUtils.playSoundAtServerEntity(entity, ModSounds.spell_earth_7, SoundCategory.PLAYERS);
        // What to do for each target hit
        entity.addPotionEffect(SkinLikeWoodPotion.Create(entity).setTarget(entity).toPotionEffect(BASE_DURATION, level));
        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.SLIME.getParticleID(),
                        ParticleEffects.CIRCLE_MOTION, 30, 0,
                        entity.posX, entity.posY + .5,
                        entity.posZ, 1.0, 1.0, 1.0, 1.0f,
                        lookVec),
                entity, 50.0f);

    }
}
