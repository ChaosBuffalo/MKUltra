package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.Targeting;
import com.chaosbuffalo.mkultra.core.BaseAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

public class FlintHound extends BaseAbility {

    private static final int COUNT_PER_LEVEL = 8;

    public FlintHound() {
        super(MKUltra.MODID, "ability.flint_hound");
    }

    @Override
    public String getAbilityName() {
        return "Flint Hound";
    }

    @Override
    public String getAbilityDescription() {
        return "Summons flint and feathers for the making of arrows.";
    }

    @Override
    public String getAbilityType() {
        return "Summon";
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 600;
    }

    @Override
    public ResourceLocation getAbilityIcon(){
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/flinthound.png");
    }


    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.SELF;
    }

    @Override
    public int getManaCost(int currentLevel) {
        return 10;
    }

    @Override
    public int getRequiredLevel(int currentLevel) {
        return 1;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);

        int level = pData.getLevelForAbility(getAbilityId());
        int count = level * COUNT_PER_LEVEL;

        ItemStack stack = new ItemStack(Items.FLINT, count);
        ItemStack feathers = new ItemStack(Items.FEATHER, count);
        ItemHandlerHelper.giveItemToPlayer(entity, stack);
        ItemHandlerHelper.giveItemToPlayer(entity, feathers);

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.FIREWORKS_SPARK.getParticleID(),
                        ParticleEffects.DIRECTED_SPOUT, 40, 10,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, 1.0,
                        lookVec),
                entity, 50.0f);
    }
}


