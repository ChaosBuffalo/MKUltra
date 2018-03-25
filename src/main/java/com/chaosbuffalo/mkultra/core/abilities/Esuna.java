package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.Targeting;
import com.chaosbuffalo.mkultra.effects.spells.CurePotion;
import com.chaosbuffalo.mkultra.effects.spells.EsunaPotion;
import com.chaosbuffalo.mkultra.core.BaseAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Esuna extends BaseAbility {

    public Esuna() {
        super(MKUltra.MODID, "ability.esuna");
    }

    @Override
    public String getAbilityName() {
        return "Esuna";
    }

    @Override
    public String getAbilityDescription() {
        return "Restores mana and cures nearby allies";
    }

    @Override
    public String getAbilityType() {
        return "AOE";
    }

    @Override
    public ResourceLocation getAbilityIcon(){
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/esuna.png");
    }


    @Override
    public int getIconU() {
        return 126;
    }

    @Override
    public int getIconV() {
        return 36;
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 25 - currentLevel * 5;
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
        return 12 - currentLevel * 3;
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

        // What to do for each target hit
        SpellCast esuna = EsunaPotion.Create(entity);
        SpellCast cure = CurePotion.Create(entity);

        AreaEffectBuilder.Create(entity, entity)
                .spellCast(esuna, 4 * GameConstants.TICKS_PER_SECOND, level, getTargetType())
                .spellCast(cure, level, getTargetType())
                .instant()
                .color(65480).radius(getDistance(level), true)
                .spawn();

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.DRIP_WATER.getParticleID(),
                        ParticleEffects.SPHERE_MOTION, 50, 6,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, 1.5,
                        lookVec),
                entity, 50.0f);
    }
}
