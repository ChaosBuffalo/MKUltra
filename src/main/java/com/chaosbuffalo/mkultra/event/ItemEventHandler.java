package com.chaosbuffalo.mkultra.event;

import com.chaosbuffalo.mkultra.MKConfig;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.effects.SpellTriggers;
import com.chaosbuffalo.mkultra.item.ItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Collections;

@Mod.EventBusSubscriber
public class ItemEventHandler {

    private static final ArrayList<ShieldRestrictionEntry> NO_SHIELD_ITEMS = new ArrayList<>();

    static {
    }

    public static void addShieldRestrictedItem(Class<? extends Item> itemClass, int priority) {
        NO_SHIELD_ITEMS.add(new ShieldRestrictionEntry(itemClass, priority));
        Collections.sort(NO_SHIELD_ITEMS);
    }

    private static void checkBlockedArmor(EntityPlayerMP player, ItemStack armor, IPlayerData playerClass, EntityEquipmentSlot slot) {
        if (MKConfig.cheats.TOUGH_GUY_MODE)
            return;

        if (!(armor.getItem() instanceof ItemArmor))
            return;

        ItemArmor armorItem = (ItemArmor) armor.getItem();
        if (!playerClass.canWearArmor(armorItem)) {
            ItemHelper.unequip(player, slot);
        }
    }

    public static boolean isNoShieldItem(Item item) {
        for (ShieldRestrictionEntry entry : NO_SHIELD_ITEMS) {
            if (entry.matches(item)) {
                return true;
            }
        }
        return false;
    }

    private static void checkShieldRestriction(EntityPlayerMP player) {
        if (MKConfig.cheats.BIG_HANDS_MODE)
            return;

        ItemStack main = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
        ItemStack off = player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);

        if (isNoShieldItem(main.getItem()) && off.getItem() instanceof ItemShield) {
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

        checkShieldRestriction(player);

        IPlayerData playerData = MKUPlayerData.get(player);
        if (playerData == null)
            return;

        SpellTriggers.PLAYER_EQUIPMENT_CHANGE.onEquipmentChange(event, playerData, player);

        if (event.getSlot().getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
            checkBlockedArmor(player, event.getTo(), playerData, event.getSlot());
        }
    }

    private static class ShieldRestrictionEntry implements Comparable<ShieldRestrictionEntry> {
        public final Class<? extends Item> item;

        public final int priority;

        public ShieldRestrictionEntry(Class<? extends Item> itemClass, int priorityIn) {
            item = itemClass;
            priority = priorityIn;
        }

        @Override
        public int compareTo(ShieldRestrictionEntry o) {
            return o.priority - priority;
        }

        private boolean matches(Item obj) {
            return item.isInstance(obj);
        }

    }
}

