package com.chaosbuffalo.mkultra.core.abilities.digger;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.abilities.cast_states.CastState;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class PierceTheHeavens extends PlayerAbility {
    public static PierceTheHeavens INSTANCE = new PierceTheHeavens();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<PlayerAbility> event) {
        event.getRegistry().register(INSTANCE.setRegistryName(INSTANCE.getAbilityId()));
    }

    private static final int COUNT_PER_LEVEL = 4;

    private PierceTheHeavens() {
        super(MKUltra.MODID, "ability.pierce_the_heavens");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 30;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.SELF;
    }

    @Override
    public float getManaCost(int currentRank) {
        return 4 + 3 * currentRank;
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return 4 + currentRank * 2;
    }

    @Override
    public int getCastTime(int currentRank) {
        return GameConstants.TICKS_PER_SECOND * (6 - currentRank);
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_earth_7;
    }

    @Override
    public void endCast(EntityPlayer entity, IPlayerData data, World theWorld, CastState state) {
        super.endCast(entity, data, theWorld, state);
        int level = data.getAbilityRank(getAbilityId());
        int count = level * COUNT_PER_LEVEL;

        ItemStack stack = new ItemStack(Blocks.LADDER, count);
        ItemHandlerHelper.giveItemToPlayer(entity, stack);

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.SPELL.getParticleID(),
                        ParticleEffects.CIRCLE_MOTION, 40, 10,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, 1.0,
                        lookVec),
                entity, 50.0f);
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);
    }
}
