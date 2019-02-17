package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.BaseToggleSetAbility;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.mkultra.effects.spells.*;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class NotoriousDOT extends BaseToggleSetAbility {
    public static float BASE_DAMAGE = 1.0f;
    public static float DAMAGE_SCALE = 2.0f;
    public static int BASE_DURATION = 32767;
    public static final Set<SpellPotionBase> TOGGLE_GROUP = new HashSet<>(Arrays.asList(SwiftsRodeoHBSongPotion.INSTANCE,
            MileysInspiringBangerzSongPotion.INSTANCE));

    public NotoriousDOT() {
        super(MKUltra.MODID, "ability.notorious_dot");
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 5;
    }

    @Override
    public Potion getToggleEffect() {
        return NotoriousDOTSongPotion.INSTANCE;
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
        return currentLevel;
    }

    @Override
    public float getDistance(int currentLevel) {
        return 3.0f + currentLevel * 3.0f;
    }

    @Override
    public int getRequiredLevel(int currentLevel) {
        return currentLevel * 2;
    }

    @Override
    public Set<SpellPotionBase> getToggleGroup() {
        return TOGGLE_GROUP;
    }

    @Override
    public void applyEffect(EntityPlayer entity, IPlayerData pData, World theWorld) {

        int level = pData.getLevelForAbility(getAbilityId());
        entity.addPotionEffect(NotoriousDOTSongPotion.Create(entity).setTarget(entity)
                .toPotionEffect(BASE_DURATION, level));
        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.NOTE.getParticleID(),
                        ParticleEffects.SPHERE_MOTION, 50, 5,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, 1.0f,
                        lookVec),
                entity, 50.0f);
    }
}
