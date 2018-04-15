package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.api.Targeting;
import com.chaosbuffalo.mkultra.core.BaseAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

public class GoldenOpportunity extends BaseAbility {
    private static final int MIN_LEVEL_FOR_IRON = 2;

    public GoldenOpportunity() {
        super(MKUltra.MODID, "ability.golden_opportunity");
    }

    @Override
    public String getAbilityName() {
        return "Golden Opportunity";
    }

    @Override
    public String getAbilityDescription() {
        return "Creates a Gold/Iron Pickaxe out of the nearby materials lying around.";
    }

    @Override
    public String getAbilityType() {
        return "Summon";
    }

    @Override
    public int getCooldown(int currentLevel){
        return 30;
    }

    @Override
    public ResourceLocation getAbilityIcon(){
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/goldenopportunity.png");
    }


    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.SELF;
    }

    @Override
    public int getManaCost(int currentLevel){
        return 10;
    }

    @Override
    public int getRequiredLevel(int currentLevel) {
        return currentLevel * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);

        int level = pData.getLevelForAbility(getAbilityId());
        Item pick;
        if (level < MIN_LEVEL_FOR_IRON) {
            pick = Items.GOLDEN_PICKAXE;
        } else {
            pick = Items.IRON_PICKAXE;
        }

        ItemStack stack = new ItemStack(pick);

        ItemHandlerHelper.giveItemToPlayer(entity, stack);
        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.WATER_BUBBLE.getParticleID(),
                        ParticleEffects.CIRCLE_MOTION, 40, 10,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, 1.0,
                        lookVec), entity, 50.0f);
    }
}
