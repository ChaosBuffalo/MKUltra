package com.chaosbuffalo.mkultra.core.abilities.brawler;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.PlayerFormulas;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.spells.CriticalChancePotion;
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

import javax.annotation.Nullable;

public class Yaup extends PlayerAbility {

    public Yaup() {
        super(MKUltra.MODID, "ability.yaup");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 45;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.FRIENDLY;
    }

    @Override
    public float getManaCost(int currentRank) {
        return 2 + currentRank * 2;
    }

    @Override
    public float getDistance(int currentRank) {
        return 10.0f + (currentRank * 5.0f);
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_holy_1;
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return 4 + currentRank * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);

        int level = pData.getAbilityRank(getAbilityId());

        int duration = 15 + (level * 15);
        duration *= GameConstants.TICKS_PER_SECOND;
        duration = PlayerFormulas.applyBuffDurationBonus(pData, duration);

        PotionEffect hasteEffect = new PotionEffect(MobEffects.HASTE, duration, level - 1, false, true);
        PotionEffect damageEffect = new PotionEffect(MobEffects.STRENGTH, duration, level - 1, false, true);
        SpellCast particlePotion = ParticlePotion.Create(entity,
                EnumParticleTypes.CRIT.getParticleID(),
                ParticleEffects.SPHERE_MOTION, false, new Vec3d(1.0, 1.0, 1.0),
                new Vec3d(0.0, 1.0, 0.0), 50, 5, 0.5);

        AreaEffectBuilder.Create(entity, entity)
                .effect(hasteEffect, getTargetType())
                .effect(damageEffect, getTargetType())
                .spellCast(SoundPotion.Create(entity, ModSounds.spell_buff_attack_4, SoundCategory.PLAYERS),
                        1, getTargetType())
                .spellCast(CriticalChancePotion.Create(entity), level + 1, Targeting.TargetType.SELF)
                .spellCast(particlePotion, 0, getTargetType())
                .instant().color(16751360).radius(getDistance(level), true)
                .spawn();

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.CRIT.getParticleID(),
                        ParticleEffects.SPHERE_MOTION, 50, 5,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, 0.5,
                        lookVec), entity, 50.0f);
    }
}