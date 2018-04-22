package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.BaseToggleSetAbility;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.mkultra.effects.spells.MileysInspiringBangerzSongPotion;
import com.chaosbuffalo.mkultra.effects.spells.NotoriousDOTSongPotion;
import com.chaosbuffalo.mkultra.effects.spells.ParticlePotion;
import com.chaosbuffalo.mkultra.core.BaseAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.effects.spells.SwiftsRodeoHBSongPotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MileysInspiringBangerz extends BaseToggleSetAbility {
    public static int BASE_DURATION = 32767;
    public static final Set<SpellPotionBase> TOGGLE_GROUP = new HashSet<>(
            Arrays.asList(SwiftsRodeoHBSongPotion.INSTANCE,
                    NotoriousDOTSongPotion.INSTANCE));

    public MileysInspiringBangerz() {
        super(MKUltra.MODID, "ability.mileys_bangerz");
    }


    @Override
    public int getCooldown(int currentLevel) {
        return 5;
    }

    @Override
    public Potion getToggleEffect() {
        return MileysInspiringBangerzSongPotion.INSTANCE;
    }

    @Override
    public int getType() {
        return TOGGLE_ABILITY;
    }

    @Override
    public void applyEffect(EntityPlayer entity, IPlayerData pData, World theWorld) {
        int level = pData.getLevelForAbility(getAbilityId());
        entity.addPotionEffect(MileysInspiringBangerzSongPotion.Create(entity).setTarget(entity)
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

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.SELF;
    }

    @Override
    public int getManaCost(int currentLevel) {
        return 2 * currentLevel;
    }

    @Override
    public float getDistance(int currentLevel) {
        return 2.0f + currentLevel * 4.0f;
    }

    @Override
    public int getRequiredLevel(int currentLevel) {
        return 6 + currentLevel * 2;
    }

    @Override
    public Set<SpellPotionBase> getToggleGroup() {
        return TOGGLE_GROUP;
    }
}