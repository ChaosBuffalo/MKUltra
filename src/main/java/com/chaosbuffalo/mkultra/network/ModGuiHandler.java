package com.chaosbuffalo.mkultra.network;

import com.chaosbuffalo.mkultra.client.gui.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.items.CapabilityItemHandler;

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

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == PIPE_CONTAINER_SCREEN){
            return new PipeContainer(player.getHeldItemMainhand().getCapability(
                    CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), player);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == CLASS_DATA_SCREEN) {
            return new PlayerClassScreen();
        } else if (ID == LEARN_CLASS_SCREEN) {
            return new ChooseClassScreen(true);
        } else if (ID == XP_TABLE_SCREEN) {
            return new XpTableScreen();
        } else if (ID == CHANGE_CLASS_SCREEN) {
            return new ChooseClassScreen(false);
        } else if (ID == PARTY_INVITE_SCREEN) {
            return new PartyInviteScreen();
        } else if (ID == PIPE_CONTAINER_SCREEN){
            return new PipeGui(player.getHeldItemMainhand().getCapability(
                    CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), player);
        }

        return null;
    }
}