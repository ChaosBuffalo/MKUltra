package com.chaosbuffalo.mkultra.core.abilities.ultimates;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.PlayerFormulas;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.spells.BolsteringRoarPotion;
import com.chaosbuffalo.mkultra.effects.spells.ParticlePotion;
import com.chaosbuffalo.mkultra.effects.spells.SoundPotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class BolsteringRoar extends PlayerAbility {

    public static final BolsteringRoar INSTANCE = new BolsteringRoar();

    public BolsteringRoar() {
        super(MKUltra.MODID, "ability.bolstering_roar");
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<PlayerAbility> event) {
        INSTANCE.setRegistryName(INSTANCE.getAbilityId());
        event.getRegistry().register(INSTANCE);
    }

    public AbilityType getType() {
        return AbilityType.Ultimate;
    }

    @Override
    public int getCooldown(int currentRank) {
        return 75;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.FRIENDLY;
    }

    @Override
    public float getManaCost(int currentRank) {
        return 12 + 2 * currentRank;
    }

    @Override
    public float getDistance(int currentRank) {
        return 10.0f + (currentRank * 5.0f);
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return 1;
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_buff_attack_3;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);

        int level = pData.getAbilityRank(getAbilityId());

        int duration = 20;
        duration *= GameConstants.TICKS_PER_SECOND;
        duration = PlayerFormulas.applyBuffDurationBonus(pData, duration);

        SpellCast roar = BolsteringRoarPotion.Create(entity);
        SpellCast particlePotion = ParticlePotion.Create(entity,
                EnumParticleTypes.CRIT.getParticleID(),
                ParticleEffects.SPHERE_MOTION, false, new Vec3d(1.0, 1.0, 1.0),
                new Vec3d(0.0, 1.0, 0.0), 50, 5, 0.1);

        AreaEffectBuilder.Create(entity, entity)
                .spellCast(roar, duration, level, getTargetType())
                .spellCast(particlePotion, 0, getTargetType())
                .spellCast(SoundPotion.Create(entity, ModSounds.spell_buff_7, SoundCategory.PLAYERS),
                        1, getTargetType())
                .instant()
                .disableParticle()
                .radius(getDistance(level), true)
                .spawn();

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.CRIT.getParticleID(),
                        ParticleEffects.SPHERE_MOTION, 50, 5,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, 0.1,
                        lookVec), entity, 50.0f);
    }
}
