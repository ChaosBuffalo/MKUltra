package com.chaosbuffalo.mkultra.core.abilities.skald;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerToggleAbility;
import com.chaosbuffalo.mkultra.core.classes.Skald;
import com.chaosbuffalo.mkultra.effects.spells.SwiftsRodeoHBSongPotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkultra.utils.AbilityUtils;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SwiftsRodeoHeartbreak extends PlayerToggleAbility {
    public static int BASE_DURATION = 32767;

    public SwiftsRodeoHeartbreak() {
        super(MKUltra.MODID, "ability.swifts_rodeo_heartbreak");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 5;
    }

    @Override
    public Potion getToggleEffect() {
        return SwiftsRodeoHBSongPotion.INSTANCE;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.SELF;
    }

    @Override
    public float getManaCost(int currentRank) {
        return currentRank;
    }

    @Override
    public float getDistance(int currentRank) {
        return 2.0f + currentRank * 4.0f;
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return 4 + currentRank * 2;
    }

    @Override
    public ResourceLocation getToggleGroupId() {
        return Skald.TOGGLE_GROUP;
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
        entity.addPotionEffect(SwiftsRodeoHBSongPotion.Create(entity).setTarget(entity)
                .toPotionEffect(BASE_DURATION, level));
        AbilityUtils.playSoundAtServerEntity(entity, ModSounds.spell_buff_4, SoundCategory.PLAYERS);
        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.NOTE.getParticleID(),
                        ParticleEffects.SPHERE_MOTION, 50, 6,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, 1.0f,
                        lookVec),
                entity, 50.0f);
    }
}
