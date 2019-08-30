package com.chaosbuffalo.mkultra.event;

import com.chaosbuffalo.mkultra.MKConfig;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.ArmorClass;
import com.chaosbuffalo.mkultra.utils.ItemUtils;
import com.chaosbuffalo.mkultra.utils.SmokeUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Jacob on 7/21/2018.
 */
@Mod.EventBusSubscriber(Side.CLIENT)
public class GuiEventHandler {


    private static void doArmorClassTooltip(ItemTooltipEvent event) {
        ArmorClass armorClass = ArmorClass.getArmorClassForArmorMat(
                ((ItemArmor) event.getItemStack().getItem()).getArmorMaterial());
        event.getToolTip().add(String.format("%s: %s", I18n.format("mkultra.ui_msg.armor_class"),
                armorClass.getName()));
        if (MKConfig.display.SHOW_ARMOR_MAT) {
            event.getToolTip().add(String.format("Armor Material: %s",
                    ((ItemArmor) event.getItemStack().getItem()).getArmorMaterial().getName()));
        }

    }

    private static void doSmokeableTooltip(ItemTooltipEvent event) {
        event.getToolTip().add("Smokeable");
    }

    private static void doCritTooltip(ItemTooltipEvent event) {
        ItemStack inHand = event.getItemStack();
        float crit_multiplier = ItemUtils.getCritDamageForItem(inHand);
        float crit_chance = ItemUtils.getCritChanceForItem(inHand);
        String tooltip = String.format(
                "%s: %s%%, %s: %sx",
                I18n.format(String.format("%s.%s.name", MKUltra.MODID, "item_stats.crit_chance")),
                Integer.toString((int) (crit_chance * 100)),
                I18n.format(String.format("%s.%s.name", MKUltra.MODID, "item_stats.crit_damage")),
                Float.toString(crit_multiplier)
        );
        event.getToolTip().add(tooltip);
    }

    private static void doNoShieldTooltip(ItemTooltipEvent event) {
        event.getToolTip().add("Cannot Equip Shield");
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void onItemTooltipEvent(ItemTooltipEvent event) {
        if (!MKConfig.cheats.TOUGH_GUY_MODE && event.getItemStack().getItem() instanceof ItemArmor) {
            doArmorClassTooltip(event);
        }
        if (ItemUtils.CRIT.hasChance(event.getItemStack().getItem())) {
            doCritTooltip(event);
        }
        if (!MKConfig.cheats.BIG_HANDS_MODE && ItemEventHandler.isNoShieldItem(event.getItemStack().getItem())) {
            doNoShieldTooltip(event);
        }
        if (SmokeUtils.isSmokeable(event.getItemStack())) {
            doSmokeableTooltip(event);
        }
    }
}
