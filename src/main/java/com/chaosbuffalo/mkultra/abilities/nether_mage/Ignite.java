//package com.chaosbuffalo.mkultra.abilities.nether_mage;
//
//import com.chaosbuffalo.mkultra.MKUltra;
//import com.chaosbuffalo.mkultra.core.IPlayerData;
//import com.chaosbuffalo.mkultra.core.MKDamageSource;
//import com.chaosbuffalo.mkultra.core.PlayerAbility;
//import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
//import com.chaosbuffalo.mkultra.effects.SpellCast;
//import com.chaosbuffalo.mkultra.effects.spells.IgnitePotion;
//import com.chaosbuffalo.mkultra.effects.spells.ParticlePotion;
//import com.chaosbuffalo.mkultra.effects.spells.SoundPotion;
//import com.chaosbuffalo.mkultra.fx.ParticleEffects;
//import com.chaosbuffalo.mkultra.init.ModSounds;
//import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
//import com.chaosbuffalo.mkultra.utils.AbilityUtils;
//import com.chaosbuffalo.targeting_api.Targeting;
//import net.minecraft.entity.EntityLivingBase;
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
//public class Ignite extends PlayerAbility {
//    public static final Ignite INSTANCE = new Ignite();
//
//    @SubscribeEvent
//    public static void register(RegistryEvent.Register<PlayerAbility> event) {
//        event.getRegistry().register(INSTANCE.setRegistryName(INSTANCE.getAbilityId()));
//    }
//
//    public static float BASE_DAMAGE = 2.0f;
//    public static float DAMAGE_SCALE = 4.0f;
//
//    private Ignite() {
//        super(MKUltra.MODID, "ability.ignite");
//    }
//
//    @Override
//    public int getCooldown(int currentRank) {
//        return 17 - 2 * currentRank;
//    }
//
//    @Override
//    public Targeting.TargetType getTargetType() {
//        return Targeting.TargetType.ENEMY;
//    }
//
//    @Override
//    public float getManaCost(int currentRank) {
//        return 8 + currentRank * 2;
//    }
//
//    @Override
//    public float getDistance(int currentRank) {
//        return 15.0f + 5.0f * currentRank;
//    }
//
//    @Override
//    public int getRequiredLevel(int currentRank) {
//        return 6 + currentRank * 2;
//    }
//
//    @Nullable
//    @Override
//    public SoundEvent getSpellCompleteSoundEvent() {
//        return ModSounds.spell_dark_13;
//    }
//
//    @Override
//    public SoundEvent getCastingSoundEvent() {
//        return ModSounds.casting_fire;
//    }
//
//    @Override
//    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
//        int level = pData.getAbilityRank(getAbilityId());
//
//        EntityLivingBase targetEntity = getSingleLivingTarget(entity, getDistance(level) * 2);
//        if (targetEntity != null) {
//            pData.startAbility(this);
//
//            targetEntity.attackEntityFrom(MKDamageSource.causeIndirectMagicDamage(getAbilityId(), entity, entity), BASE_DAMAGE + level * DAMAGE_SCALE);
//            AbilityUtils.playSoundAtServerEntity(targetEntity, ModSounds.spell_fire_4, SoundCategory.PLAYERS);
//            if (targetEntity.isBurning()) {
//                SpellCast ignite = IgnitePotion.Create(entity, BASE_DAMAGE, DAMAGE_SCALE);
//                SpellCast particle = ParticlePotion.Create(entity,
//                        EnumParticleTypes.FLAME.getParticleID(),
//                        ParticleEffects.SPHERE_MOTION, false, new Vec3d(1.0, 1.0, 1.0),
//                        new Vec3d(0.0, 1.0, 0.0), 40, 5, 1.0);
//
//                AreaEffectBuilder.Create(entity, entity)
//                        .spellCast(ignite, level, getTargetType())
//                        .spellCast(particle, level, getTargetType())
//                        .spellCast(SoundPotion.Create(entity, ModSounds.spell_fire_8, SoundCategory.PLAYERS),
//                                1, getTargetType())
//                        .instant()
//                        .color(16737305).radius(getDistance(level), true)
//                        .particle(EnumParticleTypes.FLAME)
//                        .spawn();
//            }
//
//            Vec3d lookVec = entity.getLookVec();
//            MKUltra.packetHandler.sendToAllAround(
//                    new ParticleEffectSpawnPacket(
//                            EnumParticleTypes.FLAME.getParticleID(),
//                            ParticleEffects.CIRCLE_MOTION, 40, 10,
//                            targetEntity.posX, targetEntity.posY + 1.0,
//                            targetEntity.posZ, 1.0, 1.0, 1.0, 1.5,
//                            lookVec),
//                    entity.dimension, targetEntity.posX,
//                    targetEntity.posY, targetEntity.posZ, 50.0f);
//        }
//    }
//}
