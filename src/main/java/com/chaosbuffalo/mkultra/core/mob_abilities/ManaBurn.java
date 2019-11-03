package com.chaosbuffalo.mkultra.core.mob_abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MobAbility;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.spells.ManaBurnPotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkultra.utils.AbilityUtils;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ManaBurn extends MobAbility {

    public static final ResourceLocation MANA_BURN_ID = new ResourceLocation(
            MKUltra.MODID, "mob_ability.mana_burn");


    public ManaBurn() {
        super(MANA_BURN_ID);
    }

    @Override
    public int getCooldown() {
        return 20 * GameConstants.TICKS_PER_SECOND;
    }

    @Override
    public AbilityType getAbilityType() {
        return AbilityType.ATTACK;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public float getDistance() {
        return 15.0f;
    }

    @Override
    public SoundEvent getCastingSoundEvent() {
        return ModSounds.hostile_casting_shadow;
    }

    @Nullable
    @Override
    public SoundEvent getCastingCompleteEvent() {
        return ModSounds.spell_shadow_9;
    }

    @Override
    public Potion getEffectPotion() {
        return ManaBurnPotion.INSTANCE;
    }

    @Override
    public void execute(EntityLivingBase entity, IMobData data, EntityLivingBase target, World theWorld) {
        SpellCast manaBurn = ManaBurnPotion.Create(entity, 2.0f, 0.0f).setTarget(target);
        target.addPotionEffect(manaBurn.toPotionEffect(
                GameConstants.TICKS_PER_SECOND * (2 + data.getMobLevel()), 1));
        AbilityUtils.playSoundAtServerEntity(target, ModSounds.spell_dark_9, SoundCategory.HOSTILE);
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID(),
                        ParticleEffects.CIRCLE_MOTION, 50, 10,
                        entity.posX, entity.posY + 1.0f,
                        entity.posZ, .75, 1.0, .75, 1.5,
                        entity.getLookVec()),
                entity.dimension, entity.posX,
                entity.posY, entity.posZ, 50.0f);
    }
}