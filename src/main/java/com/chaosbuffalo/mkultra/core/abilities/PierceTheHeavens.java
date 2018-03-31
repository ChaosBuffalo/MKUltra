package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.Targeting;
import com.chaosbuffalo.mkultra.core.BaseAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

public class PierceTheHeavens extends BaseAbility {
    private static final int COUNT_PER_LEVEL = 4;

    public PierceTheHeavens() {
        super(MKUltra.MODID, "ability.pierce_the_heavens");
    }

    @Override
    public String getAbilityName() {
        return "Pierce the Heavens";
    }

    @Override
    public String getAbilityDescription() {
        return "Creates some ladders so that you make an escape from the doldrums.";
    }

    @Override
    public String getAbilityType() {
        return "Summon";
    }

    @Override
    public ResourceLocation getAbilityIcon(){
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/piercetheheavens.png");
    }


    @Override
    public int getCooldown(int currentLevel){
        return 30;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.SELF;
    }

    @Override
    public int getManaCost(int currentLevel){
        return 4 + 3 * currentLevel;
    }

    @Override
    public int getIconU() {
        return 162;
    }

    @Override
    public int getIconV() {
        return 0;
    }

    @Override
    public int getRequiredLevel(int currentLevel) {
        return 4 + currentLevel * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);

        int level = pData.getLevelForAbility(getAbilityId());
        int count = level * COUNT_PER_LEVEL;

        ItemStack stack = new ItemStack(Blocks.LADDER, count);
        ItemHandlerHelper.giveItemToPlayer(entity, stack);

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.BLOCK_DUST.getParticleID(),
                        ParticleEffects.CIRCLE_MOTION, 40, 10,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, 1.0,
                        lookVec),
                entity, 50.0f);
    }
}
