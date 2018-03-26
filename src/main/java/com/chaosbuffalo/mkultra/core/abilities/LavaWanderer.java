package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.Targeting;
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

public class LavaWanderer extends BaseAbility {
    private static final int DURATION_PER_LEVEL = 20;

    public LavaWanderer() {
        super(MKUltra.MODID, "ability.lava_wanderer");
    }

    @Override
    public String getAbilityName() {
        return "Lava Wanderer";
    }

    @Override
    public String getAbilityDescription() {
        return "Grants fire resistance and haste to allies";
    }

    @Override
    public String getAbilityType() {
        return "Group Buff";
    }

    @Override
    public int getCooldown(int currentLevel){
        return 30;
    }

    @Override
    public ResourceLocation getAbilityIcon(){
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/lavawanderer.png");
    }


    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.FRIENDLY;
    }

    @Override
    public int getManaCost(int currentLevel){
        return 5 + 5 * currentLevel;
    }

    @Override
    public int getIconU() {
        return 198;
    }

    @Override
    public int getIconV() {
        return 0;
    }

    @Override
    public float getDistance(int currentLevel) {
        return 5.0f + 5.0f * currentLevel;
    }

    @Override
    public int getRequiredLevel(int currentLevel) {
        return 6 + currentLevel * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);

        int level = pData.getLevelForAbility(getAbilityId());

        PotionEffect fireResist = new PotionEffect(MobEffects.FIRE_RESISTANCE,
                20 * DURATION_PER_LEVEL * level,
                level, false, true);
        PotionEffect speed = new PotionEffect(MobEffects.HASTE,
                20 * DURATION_PER_LEVEL * level,
                level, false, true);

        SpellCast particle = ParticlePotion.Create(entity,
                EnumParticleTypes.DRIP_LAVA.getParticleID(),
                ParticleEffects.CIRCLE_MOTION, false, new Vec3d(1.0, 1.0, 1.0),
                new Vec3d(0.0, 1.0, 0.0), 40, 5, 1.0);

        AreaEffectBuilder.Create(entity, entity)
                .effect(speed, getTargetType())
                .effect(fireResist, getTargetType())
                .spellCast(particle, level, getTargetType())
                .instant()
                .particle(EnumParticleTypes.DRIP_LAVA)
                .color(16762880).radius(getDistance(level), true)
                .spawn();

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.FLAME.getParticleID(),
                        ParticleEffects.SPHERE_MOTION, 40, 10,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, 1.0,
                        lookVec),
                entity, 50.0f);
    }
}
