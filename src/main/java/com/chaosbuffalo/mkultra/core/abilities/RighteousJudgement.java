package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.CastState;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.PlayerFormulas;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.spells.AIStunPotion;
import com.chaosbuffalo.mkultra.effects.spells.BolsteringRoarPotion;
import com.chaosbuffalo.mkultra.effects.spells.ParticlePotion;
import com.chaosbuffalo.mkultra.effects.spells.RighteousJudgementPotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class RighteousJudgement extends PlayerAbility {

    public static final RighteousJudgement INSTANCE = new RighteousJudgement();

    public RighteousJudgement() {
        super(MKUltra.MODID, "ability.righteous_judgement");
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<PlayerAbility> event) {
        INSTANCE.setRegistryName(INSTANCE.getAbilityId());
        event.getRegistry().register(INSTANCE);
    }

    @Override
    public int getCooldown(int currentRank) {
        return 40;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public float getManaCost(int currentRank) {
        return 15 + 2 * currentRank;
    }

    @Override
    public float getDistance(int currentRank) {
        return 8.0f;
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return 1;
    }

    @Override
    public int getCastTime(int currentRank) {
        return GameConstants.TICKS_PER_SECOND * 2 * currentRank;
    }

    @Override
    public void endCast(EntityPlayer entity, IPlayerData data, World theWorld, CastState state) {
        super.endCast(entity, data, theWorld, state);
        int level = data.getAbilityRank(getAbilityId());

        int duration = 20;
        duration *= GameConstants.TICKS_PER_SECOND;

        SpellCast judgementPotion = RighteousJudgementPotion.Create(entity);
        SpellCast particlePotion = ParticlePotion.Create(entity,
                EnumParticleTypes.FLAME.getParticleID(),
                ParticleEffects.CIRCLE_PILLAR_MOTION, false,
                new Vec3d(1.0, 1.0, 1.0),
                new Vec3d(0.0, 1.0, 0.0),
                50, 5, 0.25);
        SpellCast stunPotion = AIStunPotion.Create(entity);

        AreaEffectBuilder.Create(entity, entity)
                .spellCast(judgementPotion, duration, level, getTargetType())
                .spellCast(particlePotion, 0, getTargetType())
                .spellCast(stunPotion, GameConstants.TICKS_PER_SECOND * 2, 1, getTargetType())
                .instant()
                .disableParticle()
                .radius(getDistance(level), true)
                .spawn();

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.FLAME.getParticleID(),
                        ParticleEffects.SPHERE_MOTION, 50, 5,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, 0.1,
                        lookVec), entity, 50.0f);
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);
    }
}

