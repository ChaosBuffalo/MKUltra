package com.chaosbuffalo.mkultra.init;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.registry.CrusherRecipeRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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

        addRecipe(event, new ItemStack(ModItems.chainmailChestplate),
                "l l", "ili", "lil", 'i', Items.IRON_INGOT, 'l', Items.LEATHER);
        addRecipe(event, new ItemStack(ModItems.chainmailLeggings),
                "lil", "i i", "l l", 'i', Items.IRON_INGOT, 'l', Items.LEATHER);
        addRecipe(event, new ItemStack(ModItems.chainmailHelmet),
                "lil", "i i", 'i', Items.IRON_INGOT, 'l', Items.LEATHER);
        addRecipe(event, new ItemStack(ModItems.chainmailBoots),
                "i i", "l l", 'i', Items.IRON_INGOT, 'l', Items.LEATHER);

        addRecipe(event, new ItemStack(ModItems.obsidian_chain_chestplate),
                "l l", "ili", "lil", 'i',
                com.mcmoddev.basemetals.init.Materials.getMaterialByName(MaterialNames.OBSIDIAN).getItem(Names.INGOT),
                'l', Items.LEATHER);
        addRecipe(event, new ItemStack(ModItems.obsidian_chain_leggings),
                "lil", "i i", "l l", 'i',
                com.mcmoddev.basemetals.init.Materials.getMaterialByName(MaterialNames.OBSIDIAN).getItem(Names.INGOT),
                'l', Items.LEATHER);
        addRecipe(event, new ItemStack(ModItems.obsidian_chain_helmet),
                "lil", "i i", 'i',
                com.mcmoddev.basemetals.init.Materials.getMaterialByName(MaterialNames.OBSIDIAN).getItem(Names.INGOT),
                'l', Items.LEATHER);
        addRecipe(event, new ItemStack(ModItems.obsidian_chain_boots),
                "i i", "l l", 'i',
                com.mcmoddev.basemetals.init.Materials.getMaterialByName(MaterialNames.OBSIDIAN).getItem(Names.INGOT),
                'l', Items.LEATHER);

        addRecipe(event, new ItemStack(ModItems.diamond_dusted_invar_chestplate),
                "i i", "idi", "iii", 'i',
                com.mcmoddev.basemetals.init.Materials.getMaterialByName(MaterialNames.INVAR).getItem(Names.INGOT),
                'd', ModItems.diamond_dust);
        addRecipe(event, new ItemStack(ModItems.diamond_dusted_invar_leggings),
                "idi", "i i", "i i", 'i',
                com.mcmoddev.basemetals.init.Materials.getMaterialByName(MaterialNames.INVAR).getItem(Names.INGOT),
                'd', ModItems.diamond_dust);
        addRecipe(event, new ItemStack(ModItems.diamond_dusted_invar_helmet),
                "idi", "i i", 'i',
                com.mcmoddev.basemetals.init.Materials.getMaterialByName(MaterialNames.INVAR).getItem(Names.INGOT),
                'd', ModItems.diamond_dust);
        addRecipe(event, new ItemStack(ModItems.diamond_dusted_invar_boots),
                "d d", "i i", 'i',
                com.mcmoddev.basemetals.init.Materials.getMaterialByName(MaterialNames.INVAR).getItem(Names.INGOT),
                'd', ModItems.diamond_dust);
        addRecipe(event, new ItemStack(Item.getItemFromBlock(ModBlocks.xpTableBlock)),
                "www", "wiw", "www", 'i', Item.getItemFromBlock(Blocks.IRON_BLOCK),
                'w', Blocks.PLANKS);
        addRecipe(event, new ItemStack(Item.getItemFromBlock(ModBlocks.portalBlock), 4),
                "ooo", "odo", "ooo", 'o', Item.getItemFromBlock(Blocks.OBSIDIAN),
                'd', Items.DIAMOND);
        addRecipe(event, new ItemStack(ModItems.forgetfulnessBread, 4),
                "brb", 'b', Items.BREAD, 'r', Items.ROTTEN_FLESH);
        addRecipe(event, new ItemStack(ModItems.hempSeeds),
                "ss", "ss", 's', Items.WHEAT_SEEDS);
        CrusherRecipeRegistry.addNewCrusherRecipe(Items.DIAMOND, new ItemStack(ModItems.diamond_dust, 4));
        CrusherRecipeRegistry.addNewCrusherRecipe(ModItems.hempLeaves,
                new ItemStack(ModItems.hempFibers, 2));
        addRecipe(event, new ItemStack(ModItems.gold_threaded_cloth, 9),
                "fff", "fgf", "fff", 'f', ModItems.hempFibers, 'g', Items.GOLD_INGOT);
        addRecipe(event, new ItemStack(ModItems.gold_threaded_boots),
                "c c", "c c", 'c', ModItems.gold_threaded_cloth);
        addRecipe(event, new ItemStack(ModItems.gold_threaded_helmet),
                "ccc", "c c", 'c', ModItems.gold_threaded_cloth);
        addRecipe(event, new ItemStack(ModItems.gold_threaded_chestplate),
                "c c", "ccc", "ccc", 'c', ModItems.gold_threaded_cloth);
        addRecipe(event, new ItemStack(ModItems.gold_threaded_leggings),
                "ccc", "c c", "c c", 'c', ModItems.gold_threaded_cloth);

        addRecipe(event, new ItemStack(ModItems.bonedLeather, 5),
                "bbb", "blb", 'b', Items.BONE, 'l', Items.LEATHER);
        addRecipe(event, new ItemStack(ModItems.bonedLeatherBoots),
                "c c", "c c", 'c', ModItems.bonedLeather);
        addRecipe(event, new ItemStack(ModItems.bonedLeatherHelmet),
                "ccc", "c c", 'c', ModItems.bonedLeather);
        addRecipe(event, new ItemStack(ModItems.bonedLeatherChestplate),
                "c c", "ccc", "ccc", 'c', ModItems.bonedLeather);
        addRecipe(event, new ItemStack(ModItems.bonedLeatherLeggings),
                "ccc", "c c", "c c", 'c', ModItems.bonedLeather);

        addRecipe(event, new ItemStack(ModItems.hempSeedBread),
                "whw", 'w', Items.WHEAT, 'h', ModItems.hempSeeds);
        addRecipe(event, new ItemStack(Item.getItemFromBlock(ModBlocks.ropeBlock)),
                "f", "f", 'f', ModItems.hempFibers);

        addRecipe(event, new ItemStack(ModItems.fire_extinguisher_flask, 8),
                "bbb", "bwb", "bbb", 'b', Items.GLASS_BOTTLE, 'w', Items.WATER_BUCKET);

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
        addRecipe(event, new ItemStack(Items.DIAMOND), "dd", "dd", 'd', ModItems.diamond_dust);
        CrusherRecipeRegistry.addNewCrusherRecipe(Blocks.OBSIDIAN,
                new ItemStack(com.mcmoddev.basemetals.init.Materials.getMaterialByName(MaterialNames.OBSIDIAN).getItem(Names.POWDER), 4));
    }

}
