package com.chaosbuffalo.mkultra.blocks;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.MKURegistry;
import com.chaosbuffalo.mkultra.network.ModGuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Jacob on 3/21/2016.
 */
public class XpTableBlock extends Block {

    public XpTableBlock(String translationKey, Material material, float hardness, float resistance) {
        super(material);
        this.setTranslationKey(translationKey);
        this.setCreativeTab(MKUltra.MKULTRA_TAB);
        this.setHardness(hardness);
        this.setResistance(resistance);
        setRegistryName(MKUltra.MODID, "xp_table");
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
                                    EntityPlayer player, EnumHand hand,
                                    EnumFacing side,
                                    float hitX, float hitY, float hitZ) {
        IPlayerData pData = MKUPlayerData.get(player);
        if (pData == null || MKURegistry.getClass(pData.getClassId()) == null)
            return true;

        player.openGui(MKUltra.INSTANCE, ModGuiHandler.XP_TABLE_SCREEN, player.world,
                (int) player.posX, (int) player.posY, (int) player.posZ);
        return true;
    }

}
