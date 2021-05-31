//package com.chaosbuffalo.mkultra.entities.projectiles;
//
//import com.chaosbuffalo.mkultra.MKUltra;
//import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
//import com.chaosbuffalo.mkultra.effects.SpellCast;
//import com.chaosbuffalo.mkultra.effects.spells.AbilityMagicDamage;
//import com.chaosbuffalo.mkultra.effects.spells.ClericHealPotion;
//import com.chaosbuffalo.mkultra.effects.spells.SoundPotion;
//import com.chaosbuffalo.mkultra.fx.ParticleEffects;
//import com.chaosbuffalo.mkultra.init.ModSounds;
//import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
//import com.chaosbuffalo.targeting_api.Targeting;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.util.EnumParticleTypes;
//import net.minecraft.util.SoundCategory;
//import net.minecraft.util.math.Vec3d;
//import net.minecraft.world.World;
//
///**
// * Created by Jacob on 3/24/2018.
// */
//public class EntityDualityRuneProjectile extends EntityBaseProjectile {
//
//    private final static float BASE = 4.0f;
//    private final static float SCALE = 4.0f;
//
//    public EntityDualityRuneProjectile(World worldIn) {
//        super(worldIn);
//    }
//
//    public EntityDualityRuneProjectile(World worldIn, EntityLivingBase throwerIn) {
//        super(worldIn, throwerIn);
//
//        this.setDeathTime(60);
//        this.setDoGroundProc(true);
//        this.setGroundProcTime(30);
//    }
//
//    @Override
//    protected Targeting.TargetType getTargetType() {
//        return Targeting.TargetType.ALL;
//    }
//
//    @Override
//    protected boolean shouldExcludeCaster() {
//        return false;
//    }
//
//    @Override
//    protected boolean onGroundProc(EntityLivingBase caster, int amplifier) {
//        if (!this.world.isRemote && caster != null) {
//            SpellCast heal = ClericHealPotion.Create(caster, BASE, SCALE);
//            SpellCast damage = AbilityMagicDamage.Create(caster, BASE, SCALE);
//
//            AreaEffectBuilder.Create(caster, this)
//                    .spellCast(heal, amplifier, Targeting.TargetType.FRIENDLY)
//                    .spellCast(damage, amplifier, Targeting.TargetType.ENEMY)
//                    .spellCast(SoundPotion.Create(caster, ModSounds.spell_shadow_6, SoundCategory.PLAYERS), 1,
//                            Targeting.TargetType.ALL)
//                    .instant()
//                    .color(39935).radius(4.0f, true)
//                    .spawn();
//            MKUltra.packetHandler.sendToAllAround(
//                    new ParticleEffectSpawnPacket(
//                            EnumParticleTypes.SPELL_WITCH.getParticleID(),
//                            ParticleEffects.DIRECTED_SPOUT, 100, 1,
//                            this.posX, this.posY + 1.0,
//                            this.posZ, 1.5, 2.0, 1.5, 1.0,
//                            new Vec3d(0., 1.0, 0.0)),
//                    this.dimension, this.posX, this.posY, this.posZ, 50.0f);
//            return true;
//        }
//
//        return false;
//    }
//
//}
