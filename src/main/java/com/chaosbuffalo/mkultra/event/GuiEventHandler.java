package com.chaosbuffalo.mkultra.event;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.ArmorClass;
import com.chaosbuffalo.mkultra.utils.ItemUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Jacob on 7/21/2018.
 */
@Mod.EventBusSubscriber
public class GuiEventHandler {


    private static void doArmorClassTooltip(ItemTooltipEvent event){
        ArmorClass armorClass = ArmorClass.getArmorClassForArmorMat(
                ((ItemArmor) event.getItemStack().getItem()).getArmorMaterial());
        event.getToolTip().add(armorClass.getName());
    }

    private static void doCritTooltip(ItemTooltipEvent event){
        ItemStack inHand = event.getItemStack();
        float crit_multiplier = ItemUtils.getCritDamageForItem(inHand);
        float crit_chance = ItemUtils.getCritChanceForItem(inHand);
        String tooltip = String.format(
                "%s: %s%%, %s: %sx",
                I18n.format(String.format("%s.%s.name", MKUltra.MODID, "item_stats.crit_chance")),
                Integer.toString((int)(crit_chance*100)),
                I18n.format(String.format("%s.%s.name", MKUltra.MODID, "item_stats.crit_damage")),
                Float.toString(crit_multiplier)
                );
        event.getToolTip().add(tooltip);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void onItemTooltipEvent(ItemTooltipEvent event){
        if (event.getItemStack().getItem() instanceof ItemArmor) {
            doArmorClassTooltip(event);
        }
        if (ItemUtils.itemHasCriticalChance(event.getItemStack().getItem())){
            doCritTooltip(event);
        }
    }
}
