package com.chaosbuffalo.mkultra.core.abilities.cleric;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.PlayerFormulas;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.spells.CurePotion;
import com.chaosbuffalo.mkultra.effects.spells.SoundPotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkultra.utils.AbilityUtils;
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

public class Galvanize extends PlayerAbility {

    public Galvanize() {
        super(MKUltra.MODID, "ability.galvanize");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 25;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.FRIENDLY;
    }

    @Override
    public float getManaCost(int currentRank) {
        return 10 - 2 * currentRank;
    }

    @Override
    public float getDistance(int currentRank) {
        return 10.0f;
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return 4 + currentRank * 2;
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_heal_1;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {

        pData.startAbility(this);

        int level = pData.getAbilityRank(getAbilityId());

        int duration = 100 + (50 * level);
        duration = PlayerFormulas.applyBuffDurationBonus(pData, duration);
        PotionEffect jump = new PotionEffect(MobEffects.JUMP_BOOST, duration, level - 1, false, true);
        SpellCast cure = CurePotion.Create(entity);
        AreaEffectBuilder.Create(entity, entity)
                .spellCast(cure, level, getTargetType())
                .effect(jump, getTargetType())
                .spellCast(SoundPotion.Create(entity, ModSounds.spell_buff_5, SoundCategory.PLAYERS),
                        1, getTargetType())
                .instant().color(1048370).radius(getDistance(level), true)
                .spawn();

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.VILLAGER_HAPPY.getParticleID(),
                        ParticleEffects.SPHERE_MOTION, 50, 5,
                        entity.posX, entity.posY + 0.05,
                        entity.posZ, 0.75, 0.75, 0.75, 1.5,
                        lookVec),
                entity, 50.0f);
    }
}
