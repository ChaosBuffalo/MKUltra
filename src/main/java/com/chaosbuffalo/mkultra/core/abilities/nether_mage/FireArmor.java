package com.chaosbuffalo.mkultra.core.abilities.nether_mage;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.abilities.cast_states.CastState;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.PlayerFormulas;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.spells.ParticlePotion;
import com.chaosbuffalo.mkultra.effects.spells.SoundPotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FireArmor extends PlayerAbility {

    public static int BASE_DURATION = 60;
    public static int DURATION_SCALE = 30;

    public FireArmor() {
        super(MKUltra.MODID, "ability.fire_armor");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 150 - currentRank * 15;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.FRIENDLY;
    }

    @Override
    public float getManaCost(int currentRank) {
        return 16 - currentRank * 4;
    }

    @Override
    public float getDistance(int currentRank) {
        return 10.0f + 2.0f * currentRank;
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return 4 + currentRank * 2;
    }

    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_buff_5;
    }

    @Override
    public int getCastTime(int currentRank) {
        return GameConstants.TICKS_PER_SECOND - 5 * (currentRank - 1);
    }

    @Override
    public void endCast(EntityPlayer entity, IPlayerData data, World theWorld, CastState state) {
        super.endCast(entity, data, theWorld, state);
        int level = data.getAbilityRank(getAbilityId());

        // What to do for each target hit
        int duration = (BASE_DURATION + DURATION_SCALE * level) * GameConstants.TICKS_PER_SECOND;
        duration = PlayerFormulas.applyBuffDurationBonus(data, duration);

        PotionEffect absorbEffect = new PotionEffect(MobEffects.ABSORPTION,
                duration, level + 1, false, true);

        PotionEffect fireResistanceEffect = new PotionEffect(MobEffects.FIRE_RESISTANCE,
                duration, level, false, true);

        SpellCast particlePotion = ParticlePotion.Create(entity,
                EnumParticleTypes.FLAME.getParticleID(),
                ParticleEffects.CIRCLE_PILLAR_MOTION, false,
                new Vec3d(1.0, 1.0, 1.0),
                new Vec3d(0.0, 1.0, 0.0),
                40, 5, .1f);

        AreaEffectBuilder.Create(entity, entity)
                .effect(absorbEffect, getTargetType())
                .effect(fireResistanceEffect, getTargetType())
                .spellCast(particlePotion, level, getTargetType())
                .spellCast(SoundPotion.Create(entity, ModSounds.spell_fire_2, SoundCategory.PLAYERS),
                        1, getTargetType())
                .instant()
                .particle(EnumParticleTypes.DRIP_LAVA)
                .color(16762905).radius(getDistance(level), true)
                .spawn();

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.FLAME.getParticleID(),
                        ParticleEffects.CIRCLE_MOTION, 50, 0,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, .1f,
                        lookVec),
                entity, 50.0f);
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);


    }
}
