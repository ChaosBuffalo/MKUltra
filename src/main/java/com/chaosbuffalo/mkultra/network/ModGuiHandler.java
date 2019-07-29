package com.chaosbuffalo.mkultra.network;

import com.chaosbuffalo.mkultra.client.gui.*;
import com.chaosbuffalo.mkultra.init.ModItems;
import com.chaosbuffalo.mkultra.item.interfaces.IClassProvider;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.tiles.TileEntityMKSpawner;
import com.chaosbuffalo.mkultra.tiles.TileEntityNPCSpawner;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * Created by Jacob on 3/15/2016.
 */
public class ModGuiHandler implements IGuiHandler {

    public static final int CLASS_DATA_SCREEN = 0;
    public static final int LEARN_CLASS_SCREEN = 1;
    public static final int XP_TABLE_SCREEN = 2;
    public static final int CHANGE_CLASS_SCREEN = 3;
    public static final int PARTY_INVITE_SCREEN = 4;
    public static final int PIPE_CONTAINER_SCREEN = 5;
    public static final int MK_SPAWNER_SCREEN = 6;
    public static final int LEARN_CLASS_SCREEN_ADMIN = 7;
    public static final int CHANGE_CLASS_SCREEN_ADMIN = 8;
    public static final int NPC_SPAWNER_EQUIPMENT_SCREEN = 9;
    public static final int LEARN_CLASS_FROM_TILE_ENTITY_SCREEN = 10;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == PIPE_CONTAINER_SCREEN){
            ItemStack main_hand = player.getHeldItemMainhand();
            ItemStack off_hand = player.getHeldItemOffhand();
            ItemStack selected = null;
            if (main_hand.getItem() == ModItems.pipe){
                selected = main_hand;
            } else if (off_hand.getItem() == ModItems.pipe){
                selected = off_hand;
            }
            if (selected != null){
                IItemHandler itemHandler = selected.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                if (itemHandler != null){
                    return new PipeContainer(itemHandler, player);
                }
            }
        } else if (ID == NPC_SPAWNER_EQUIPMENT_SCREEN){
            BlockPos pos = new BlockPos(x, y, z);
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileEntityNPCSpawner) {
                return new NPCEquipmentContainer((TileEntityNPCSpawner) te, player);
            }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == CLASS_DATA_SCREEN) {
            return new PlayerClassScreen();
        } else if (ID == LEARN_CLASS_SCREEN) {
            return new ChooseClassScreen.FromItem(true, true);
        } else if (ID == XP_TABLE_SCREEN) {
            return new XpTableScreen();
        } else if (ID == CHANGE_CLASS_SCREEN) {
            return new ChooseClassScreen.FromItem(false, true);
        } else if (ID == CHANGE_CLASS_SCREEN_ADMIN) {
            return new ChooseClassScreen.FromItem(false, false);
        } else if (ID == LEARN_CLASS_SCREEN_ADMIN) {
            return new ChooseClassScreen.FromItem(true, false);
        } else if (ID == PARTY_INVITE_SCREEN) {
            return new PartyInviteScreen();
        } else if (ID == PIPE_CONTAINER_SCREEN){
            ItemStack main_hand = player.getHeldItemMainhand();
            ItemStack off_hand = player.getHeldItemOffhand();
            ItemStack selected = null;
            if (main_hand.getItem() == ModItems.pipe){
                selected = main_hand;
            } else if (off_hand.getItem() == ModItems.pipe){
                selected = off_hand;
            }
            if (selected != null){
                IItemHandler itemHandler = selected.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                if (itemHandler != null){
                    return new PipeGui(itemHandler, player);
                }
            }
        }  else if (ID == MK_SPAWNER_SCREEN){
            TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
            if (entity instanceof TileEntityMKSpawner){
                TileEntityMKSpawner mkSpawner = (TileEntityMKSpawner)entity;
                return new MKSpawnerGui(mkSpawner);
            }
        } else if (ID == NPC_SPAWNER_EQUIPMENT_SCREEN){
            BlockPos pos = new BlockPos(x, y, z);
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileEntityNPCSpawner) {
                TileEntityNPCSpawner containerTileEntity = (TileEntityNPCSpawner) te;
                return new NPCSpawnerEquipmentGUI(containerTileEntity, new NPCEquipmentContainer(containerTileEntity, player));
            }
        } else if (ID == LEARN_CLASS_FROM_TILE_ENTITY_SCREEN){
            BlockPos pos = new BlockPos(x, y, z);
            TileEntity te = world.getTileEntity(pos);
            Log.info("Trying to open class screen");
            if (te instanceof IClassProvider){
                Log.info("tile entity is a class provider");
                return new ChooseClassScreen.FromTE(te, true, true);
            }
        }

        return null;
    }
}