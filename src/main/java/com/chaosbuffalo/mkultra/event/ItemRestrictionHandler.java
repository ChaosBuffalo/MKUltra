package com.chaosbuffalo.mkultra.event;

import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.item.ItemHelper;
import com.chaosbuffalo.mkultra.item.interfaces.IExtendedReach;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ItemRestrictionHandler {

    private static void checkBlockedArmor(EntityPlayerMP player, ItemStack armor, IPlayerData playerClass, EntityEquipmentSlot slot) {
        if (!(armor.getItem() instanceof ItemArmor))
            return;

        ItemArmor armorItem = (ItemArmor) armor.getItem();
        if (!playerClass.canWearArmorMaterial(armorItem.getArmorMaterial())) {
            ItemHelper.unequip(player, slot);
        }
    }

    private static void checkShieldWithSpear(EntityPlayerMP player) {
        ItemStack main = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
        ItemStack off = player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);

        if (main.getItem() instanceof IExtendedReach && off.getItem() instanceof ItemShield) {
            ItemHelper.unequip(player, EntityEquipmentSlot.OFFHAND);
        }
    }

    private static void checkEquipmentSlot(EntityPlayer player, IPlayerData data, EntityEquipmentSlot slot) {
        ItemStack stack = player.getItemStackFromSlot(slot);
        if (!stack.isEmpty()) {
            checkBlockedArmor((EntityPlayerMP) player, stack, data, slot);
        }
    }

    public static void checkEquipment(EntityPlayer player) {
        if (!(player instanceof EntityPlayerMP))
            return;

        IPlayerData playerData = MKUPlayerData.get(player);
        if (playerData == null)
            return;

        checkEquipmentSlot(player, playerData, EntityEquipmentSlot.HEAD);
        checkEquipmentSlot(player, playerData, EntityEquipmentSlot.CHEST);
        checkEquipmentSlot(player, playerData, EntityEquipmentSlot.LEGS);
        checkEquipmentSlot(player, playerData, EntityEquipmentSlot.FEET);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayerMP))
            return;

        EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();

        checkShieldWithSpear(player);

        IPlayerData playerData = MKUPlayerData.get(player);
        if (playerData == null)
            return;

        if (event.getSlot().getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
            checkBlockedArmor(player, event.getTo(), playerData, event.getSlot());
        }
    }
}

