//package com.chaosbuffalo.mkultra.abilities.wet_wizard;
//
//import com.chaosbuffalo.mkultra.GameConstants;
//import com.chaosbuffalo.mkultra.MKUltra;
//import com.chaosbuffalo.mkultra.abilities.cast_states.CastState;
//import com.chaosbuffalo.mkultra.core.IPlayerData;
//import com.chaosbuffalo.mkultra.core.PlayerAbility;
//import com.chaosbuffalo.mkultra.core.PlayerFormulas;
//import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
//import com.chaosbuffalo.mkultra.effects.SpellCast;
//import com.chaosbuffalo.mkultra.effects.spells.ParticlePotion;
//import com.chaosbuffalo.mkultra.effects.spells.SoundPotion;
//import com.chaosbuffalo.mkultra.effects.spells.UndertowPotion;
//import com.chaosbuffalo.mkultra.fx.ParticleEffects;
//import com.chaosbuffalo.mkultra.init.ModSounds;
//import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
//import com.chaosbuffalo.targeting_api.Targeting;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.util.EnumParticleTypes;
//import net.minecraft.util.SoundCategory;
//import net.minecraft.util.SoundEvent;
//import net.minecraft.util.math.Vec3d;
//import net.minecraft.world.World;
//import net.minecraftforge.event.RegistryEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//
//import javax.annotation.Nullable;
//
//@Mod.EventBusSubscriber(modid = MKUltra.MODID)
//public class Undertow extends PlayerAbility {
//    public static final Undertow INSTANCE = new Undertow();
//
//    @SubscribeEvent
//    public static void register(RegistryEvent.Register<PlayerAbility> event) {
//        event.getRegistry().register(INSTANCE.setRegistryName(INSTANCE.getAbilityId()));
//    }
//
//    public static int BASE_DURATION = 15;
//    public static int DURATION_SCALE = 15;
//
//    private Undertow() {
//        super(MKUltra.MODID, "ability.undertow");
//    }
//
//    @Override
//    public int getCooldown(int currentRank) {
//        return 90 - currentRank * 15;
//    }
//
//    @Override
//    public Targeting.TargetType getTargetType() {
//        return Targeting.TargetType.FRIENDLY;
//    }
//
//    @Override
//    public float getManaCost(int currentRank) {
//        return 4 + currentRank * 2;
//    }
//
//    @Override
//    public float getDistance(int currentRank) {
//        return 10.0f + 2.0f * currentRank;
//    }
//
//    @Override
//    public int getRequiredLevel(int currentRank) {
//        return 4 + currentRank * 2;
//    }
//
//    @Override
//    public SoundEvent getCastingSoundEvent() {
//        return ModSounds.casting_water;
//    }
//
//    @Nullable
//    @Override
//    public SoundEvent getSpellCompleteSoundEvent() {
//        return ModSounds.spell_buff_attack_4;
//    }
//
//    @Override
//    public int getCastTime(int currentRank) {
//        return GameConstants.TICKS_PER_SECOND / currentRank;
//    }
//
//    @Override
//    public void endCast(EntityPlayer entity, IPlayerData data, World theWorld, CastState state) {
//        super.endCast(entity, data, theWorld, state);
//        int level = data.getAbilityRank(getAbilityId());
//
//        int duration = (BASE_DURATION + DURATION_SCALE * level) * GameConstants.TICKS_PER_SECOND;
//        duration = PlayerFormulas.applyBuffDurationBonus(data, duration);
//        SpellCast undertow = UndertowPotion.Create(entity);
//        SpellCast particlePotion = ParticlePotion.Create(entity,
//                EnumParticleTypes.WATER_BUBBLE.getParticleID(),
//                ParticleEffects.DIRECTED_SPOUT, false,
//                new Vec3d(1.0, 1.5, 1.0),
//                new Vec3d(0.0, 1.0, 0.0),
//                40, 5, 1.0);
//
//        AreaEffectBuilder.Create(entity, entity)
//                .spellCast(undertow, duration, level, getTargetType())
//                .spellCast(particlePotion, level, getTargetType())
//                .spellCast(SoundPotion.Create(entity, ModSounds.spell_water_4, SoundCategory.PLAYERS),
//                        1, getTargetType())
//                .instant()
//                .particle(EnumParticleTypes.DRIP_WATER)
//                .color(65480).radius(getDistance(level), true)
//                .spawn();
//
//        Vec3d lookVec = entity.getLookVec();
//        MKUltra.packetHandler.sendToAllAround(
//                new ParticleEffectSpawnPacket(
//                        EnumParticleTypes.DRIP_WATER.getParticleID(),
//                        ParticleEffects.CIRCLE_MOTION, 50, 0,
//                        entity.posX, entity.posY + 1.5,
//                        entity.posZ, 1.0, 1.0, 1.0, 1.0f,
//                        lookVec),
//                entity, 50.0f);
//    }
//
//    @Override
//    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
//        pData.startAbility(this);
//    }
//}
