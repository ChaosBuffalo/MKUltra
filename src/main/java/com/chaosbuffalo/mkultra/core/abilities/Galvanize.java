package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.api.Targeting;
import com.chaosbuffalo.mkultra.effects.spells.CurePotion;
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

public class Galvanize extends BaseAbility {

    public Galvanize() {
        super(MKUltra.MODID, "ability.galvanize");
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 25;
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
        return 10;
    }

    @Override
    public float getDistance(int currentLevel) {
        return 10.0f;
    }

    @Override
    public int getRequiredLevel(int currentLevel) {
        return 4 + currentLevel * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {

        pData.startAbility(this);

        int level = pData.getLevelForAbility(getAbilityId());

        PotionEffect jump = new PotionEffect(MobEffects.JUMP_BOOST, 100 + (50 * level), level - 1, false, true);
        SpellCast cure = CurePotion.Create(entity);

        AreaEffectBuilder.Create(entity, entity)
                .spellCast(cure, level, getTargetType())
                .effect(jump, getTargetType())
                .instant().color(1048370).radius(getDistance(level), true)
                .spawn();

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.VILLAGER_HAPPY.getParticleID(),
                        ParticleEffects.SPHERE_MOTION, 50, 5,
                        entity.posX, entity.posY + 0.05,
                        entity.posZ, 0.75, 0.75, 0.75, 1.5,
                        lookVec),
                entity, 50.0f);
    }
}
