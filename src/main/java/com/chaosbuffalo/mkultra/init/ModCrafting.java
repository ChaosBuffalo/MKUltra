package com.chaosbuffalo.mkultra.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;


@SuppressWarnings("unused")
@Mod.EventBusSubscriber
public final class ModCrafting {


    private static void addRecipe(RegistryEvent.Register<IRecipe> event, ItemStack stack, Object... recipe) {
        ResourceLocation name = stack.getItem().getRegistryName();
        if (name != null) {
            event.getRegistry().register(new ShapedOreRecipe(name, stack, recipe).setRegistryName(name));
        }
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void initCrafting(RegistryEvent.Register<IRecipe> event) {

        GameRegistry.addSmelting(
                Items.DIAMOND_CHESTPLATE,
                new ItemStack(ModItems.diamond_dust, 20),
                3.0f
        );
        GameRegistry.addSmelting(
                Items.DIAMOND_LEGGINGS,
                new ItemStack(ModItems.diamond_dust, 16),
                3.0f
        );
        GameRegistry.addSmelting(
                Items.DIAMOND_BOOTS,
                new ItemStack(ModItems.diamond_dust, 8),
                3.0f
        );
        GameRegistry.addSmelting(
                Items.DIAMOND_HELMET,
                new ItemStack(ModItems.diamond_dust, 12),
                3.0f
        );
    }

}
