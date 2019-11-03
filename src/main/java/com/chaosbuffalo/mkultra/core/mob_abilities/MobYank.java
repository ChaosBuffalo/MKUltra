package com.chaosbuffalo.mkultra.core.mob_abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MobAbility;
import com.chaosbuffalo.mkultra.effects.spells.YankPotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkultra.utils.AbilityUtils;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class MobYank extends MobAbility {

    float BASE_DAMAGE = 1.0f;
    float DAMAGE_SCALE = .25f;

    public MobYank() {
        super(MKUltra.MODID, "mob_ability.yank");
    }

    @Override
    public int getCooldown() {
        return 30 * GameConstants.TICKS_PER_SECOND;
    }

    @Override
    public MobAbility.AbilityType getAbilityType() {
        return MobAbility.AbilityType.ATTACK;
    }

    @Override
    public float getDistance() {
        return 4.0f;
    }

    @Override
    public int getCastTime() {
        return GameConstants.TICKS_PER_SECOND;
    }

    @Override
    public SoundEvent getCastingSoundEvent() {
        return ModSounds.hostile_casting_shadow;
    }

    @Nullable
    @Override
    public SoundEvent getCastingCompleteEvent() {
        return ModSounds.spell_grab_2;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public void execute(EntityLivingBase entity, IMobData data, EntityLivingBase target, World theWorld) {
        if (target != null) {
            int level = data.getMobLevel() > 5 ? 2 : 1;
            target.addPotionEffect(YankPotion.Create(entity, target).toPotionEffect(level));
            AbilityUtils.playSoundAtServerEntity(target, ModSounds.spell_grab_1, SoundCategory.HOSTILE);
            Vec3d partHeading = target.getPositionVector()
                    .add(new Vec3d(0.0, 1.0, 0.0))
                    .subtract(entity.getPositionVector())
                    .normalize();
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.SPELL_INSTANT.getParticleID(),
                            ParticleEffects.DIRECTED_SPOUT, 50, 1,
                            entity.posX, entity.posY + 1.0,
                            entity.posZ, .25, .25, .25, 5.0,
                            partHeading),
                    entity, 50.0f);
        }
    }
}
