package com.chaosbuffalo.mkultra.init;

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
        addRecipe(event, new ItemStack(ModItems.sunicon),
                " s ", "sis", " s ",
                'i',
                com.mcmoddev.basemetals.init.Items.getItemByName("brass_ingot"),
                's',
                com.mcmoddev.basemetals.init.Items.getItemByName("bronze_ingot"));
        addRecipe(event, new ItemStack(ModItems.ironSpear),
                "  s", " s ", "i  ", 'i', Items.IRON_INGOT, 's', Items.STICK);
        addRecipe(event, new ItemStack(ModItems.woodSpear),
                "  s", " s ", "i  ", 'i', Blocks.PLANKS, 's', Items.STICK);
        addRecipe(event, new ItemStack(ModItems.stoneSpear),
                "  s", " s ", "i  ", 'i', Blocks.COBBLESTONE, 's', Items.STICK);
        addRecipe(event, new ItemStack(ModItems.goldSpear),
                "  s", " s ", "i  ", 'i', Items.GOLD_INGOT, 's', Items.STICK);
        addRecipe(event, new ItemStack(ModItems.diamondSpear),
                "  s", " s ", "i  ", 'i', Items.DIAMOND, 's', Items.STICK);
        addRecipe(event, new ItemStack(ModItems.tinSpear),
                "  s", " s ", "i  ",
                'i',
                com.mcmoddev.basemetals.init.Items.getItemByName("tin_ingot"),
                's', Items.STICK);
        addRecipe(event, new ItemStack(ModItems.silverSpear),
                "  s", " s ", "i  ",
                'i',
                com.mcmoddev.basemetals.init.Items.getItemByName("silver_ingot"),
                's', Items.STICK);
        addRecipe(event, new ItemStack(ModItems.steelSpear),
                "  s", " s ", "i  ",
                'i', com.mcmoddev.basemetals.init.Items.getItemByName("steel_ingot"),
                's', Items.STICK);
        addRecipe(event, new ItemStack(ModItems.starsteelSpear),
                "  s", " s ", "i  ",
                'i', com.mcmoddev.basemetals.init.Items.getItemByName("starsteel_ingot"),
                's', Items.STICK);
        addRecipe(event, new ItemStack(ModItems.nickelSpear),
                "  s", " s ", "i  ",
                'i', com.mcmoddev.basemetals.init.Items.getItemByName("nickel_ingot"),
                's', Items.STICK);
        addRecipe(event, new ItemStack(ModItems.mithrilSpear),
                "  s", " s ", "i  ",
                'i', com.mcmoddev.basemetals.init.Items.getItemByName("mithril_ingot"),
                's', Items.STICK);
        addRecipe(event, new ItemStack(ModItems.leadSpear),
                "  s", " s ", "i  ",
                'i', com.mcmoddev.basemetals.init.Items.getItemByName("lead_ingot"),
                's', Items.STICK);
        addRecipe(event, new ItemStack(ModItems.invarSpear),
                "  s", " s ", "i  ",
                'i', com.mcmoddev.basemetals.init.Items.getItemByName("invar_ingot"),
                's', Items.STICK);
        addRecipe(event, new ItemStack(ModItems.electrumSpear),
                "  s", " s ", "i  ", 'i', com.mcmoddev.basemetals.init.Items.getItemByName("electrum_ingot"),
                's', Items.STICK);
        addRecipe(event, new ItemStack(ModItems.coldironSpear),
                "  s", " s ", "i  ", 'i', com.mcmoddev.basemetals.init.Items.getItemByName("coldiron_ingot"),
                's', Items.STICK);
        addRecipe(event, new ItemStack(ModItems.bronzeSpear),
                "  s", " s ", "i  ", 'i', com.mcmoddev.basemetals.init.Items.getItemByName("bronze_ingot"),
                's', Items.STICK);
        addRecipe(event, new ItemStack(ModItems.brassSpear),
                "  s", " s ", "i  ", 'i', com.mcmoddev.basemetals.init.Items.getItemByName("brass_ingot"),
                's', Items.STICK);
        addRecipe(event, new ItemStack(ModItems.aquariumSpear),
                "  s", " s ", "i  ", 'i', com.mcmoddev.basemetals.init.Items.getItemByName("aquarium_ingot"),
                's', Items.STICK);
        addRecipe(event, new ItemStack(ModItems.adamantineSpear),
                "  s", " s ", "i  ", 'i', com.mcmoddev.basemetals.init.Items.getItemByName("adamantine_ingot"),
                's', Items.STICK);
        addRecipe(event, new ItemStack(ModItems.copperSpear),
                "  s", " s ", "i  ", 'i', com.mcmoddev.basemetals.init.Items.getItemByName("copper_ingot"),
                's', Items.STICK);
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
        CrusherRecipeRegistry.addNewCrusherRecipe(Items.DIAMOND, new ItemStack(ModItems.angelDust, 4));
        CrusherRecipeRegistry.addNewCrusherRecipe(ModItems.hempLeaves,
                new ItemStack(ModItems.hempFibers, 2));
        addRecipe(event, new ItemStack(ModItems.gold_threaded_cloth, 4),
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
    }

}
