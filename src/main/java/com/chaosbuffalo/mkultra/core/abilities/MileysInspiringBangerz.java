package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.api.Targeting;
import com.chaosbuffalo.mkultra.effects.spells.ParticlePotion;
import com.chaosbuffalo.mkultra.core.BaseAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MileysInspiringBangerz extends BaseAbility {

    public MileysInspiringBangerz() {
        super(MKUltra.MODID, "ability.mileys_inspiring_bangerz");
    }

    @Override
    public String getAbilityName() {
        return "Miley's Bangerz";
    }

    @Override
    public String getAbilityDescription() {
        return "Gives all your allies absorption and buffs their damage.";
    }

    @Override
    public String getAbilityType() {
        return "Group Buff";
    }

    @Override
    public ResourceLocation getAbilityIcon(){
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/mileysbangerz.png");
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 14 + currentLevel * 6;
    }

    @Override
    public int getType() {
        return ACTIVE_ABILITY;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.FRIENDLY;
    }

    @Override
    public int getManaCost(int currentLevel) {
        return 14 + 2 * currentLevel;
    }

    @Override
    public float getDistance(int currentLevel) {
        return 2.0f + currentLevel * 4.0f;
    }

    @Override
    public int getRequiredLevel(int currentLevel) {
        return 6 + currentLevel * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);

        int level = pData.getLevelForAbility(getAbilityId());

        int duration = getCooldownTicks(level);

        PotionEffect absorb = new PotionEffect(MobEffects.ABSORPTION, duration, level + 2);
        PotionEffect damage = new PotionEffect(MobEffects.REGENERATION, duration, level);
        PotionEffect resistance = new PotionEffect(MobEffects.RESISTANCE, duration, level);


        SpellCast particle = ParticlePotion.Create(entity,
                EnumParticleTypes.NOTE.getParticleID(),
                ParticleEffects.SPHERE_MOTION, false, new Vec3d(1.0, 1.0, 1.0),
                new Vec3d(0.0, 1.0, 0.0), 40, 5, 1.0);

        AreaEffectBuilder.Create(entity, entity)
                .effect(absorb, getTargetType())
                .effect(damage, getTargetType())
                .effect(resistance, getTargetType())
                .spellCast(particle, level, getTargetType())
                .instant()
                .color(16762880).radius(getDistance(level), true)
                .particle(EnumParticleTypes.NOTE)
                .spawn();

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.NOTE.getParticleID(),
                        ParticleEffects.SPHERE_MOTION, 50, 5,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, 1.0f,
                        lookVec),
                entity, 50.0f);
    }
}