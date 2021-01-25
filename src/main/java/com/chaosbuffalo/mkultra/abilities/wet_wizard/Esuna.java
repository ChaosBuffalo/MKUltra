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
//import com.chaosbuffalo.mkultra.effects.spells.CurePotion;
//import com.chaosbuffalo.mkultra.effects.spells.EsunaPotion;
//import com.chaosbuffalo.mkultra.effects.spells.SoundPotion;
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
//public class Esuna extends PlayerAbility {
//    public static final Esuna INSTANCE = new Esuna();
//
//    @SubscribeEvent
//    public static void register(RegistryEvent.Register<PlayerAbility> event) {
//        event.getRegistry().register(INSTANCE.setRegistryName(INSTANCE.getAbilityId()));
//    }
//
//    private Esuna() {
//        super(MKUltra.MODID, "ability.esuna");
//    }
//
//    @Override
//    public int getCooldown(int currentRank) {
//        return 25 - currentRank * 5;
//    }
//
//    @Override
//    public Targeting.TargetType getTargetType() {
//        return Targeting.TargetType.FRIENDLY;
//    }
//
//    @Override
//    public float getManaCost(int currentRank) {
//        return 12 - currentRank * 3;
//    }
//
//
//    @Override
//    public float getDistance(int currentRank) {
//        return 10.0f;
//    }
//
//    @Override
//    public int getRequiredLevel(int currentRank) {
//        return 4 + currentRank * 2;
//    }
//
//    @Override
//    public int getCastTime(int currentRank) {
//        return GameConstants.TICKS_PER_SECOND / currentRank;
//    }
//
//    @Override
//    public SoundEvent getCastingSoundEvent() {
//        return ModSounds.casting_holy;
//    }
//
//    @Nullable
//    @Override
//    public SoundEvent getSpellCompleteSoundEvent() {
//        return ModSounds.spell_buff_1;
//    }
//
//    @Override
//    public void endCast(EntityPlayer entity, IPlayerData data, World theWorld, CastState state) {
//        super.endCast(entity, data, theWorld, state);
//        int level = data.getAbilityRank(getAbilityId());
//
//        SpellCast esuna = EsunaPotion.Create(entity);
//        SpellCast cure = CurePotion.Create(entity);
//
//        int esunaDuration = PlayerFormulas.applyBuffDurationBonus(data,
//                4 * GameConstants.TICKS_PER_SECOND);
//        AreaEffectBuilder.Create(entity, entity)
//                .spellCast(esuna, esunaDuration, level, getTargetType())
//                .spellCast(cure, level, getTargetType())
//                .spellCast(SoundPotion.Create(entity, ModSounds.spell_heal_2, SoundCategory.PLAYERS),
//                        1, getTargetType())
//                .instant()
//                .color(65480).radius(getDistance(level), true)
//                .spawn();
//
//        Vec3d lookVec = entity.getLookVec();
//        MKUltra.packetHandler.sendToAllAround(
//                new ParticleEffectSpawnPacket(
//                        EnumParticleTypes.DRIP_WATER.getParticleID(),
//                        ParticleEffects.SPHERE_MOTION, 50, 6,
//                        entity.posX, entity.posY + 1.0,
//                        entity.posZ, 1.0, 1.0, 1.0, 1.5,
//                        lookVec),
//                entity, 50.0f);
//    }
//
//    @Override
//    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
//        pData.startAbility(this);
//    }
//}
