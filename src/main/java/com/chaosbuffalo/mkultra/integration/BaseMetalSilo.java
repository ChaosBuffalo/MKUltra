package com.chaosbuffalo.mkultra.integration;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.ArmorClass;
import com.chaosbuffalo.mkultra.init.ModItems;
import com.chaosbuffalo.mkultra.item.*;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.registry.CrusherRecipeRegistry;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mod.EventBusSubscriber
public class BaseMetalSilo {

    static UUID chestUUID = UUID.fromString("77ab4b54-5885-4f7f-ab41-71af536309d1");
    static UUID leggingsUUID = UUID.fromString("8d827c58-8f61-4c77-8dcd-f62f0e69121b");
    static UUID helmetUUID = UUID.fromString("fb16408c-0421-4138-a283-8da7038e5970");
    static UUID feetUUID = UUID.fromString("fb16408c-0421-4138-a283-8da7038e5972");

    public static Item obsidian_chain_leggings;
    public static Item obsidian_chain_chestplate;
    public static Item obsidian_chain_helmet;
    public static Item obsidian_chain_boots;

    public static Item copper_threaded_cloth;
    public static Item copper_threaded_leggings;
    public static Item copper_threaded_chestplate;
    public static Item copper_threaded_helmet;
    public static Item copper_threaded_boots;

    public static Item diamond_dusted_invar_leggings;
    public static Item diamond_dusted_invar_chestplate;
    public static Item diamond_dusted_invar_helmet;
    public static Item diamond_dusted_invar_boots;


    public static Item steel_infused_bone_leather;
    public static Item steel_infused_bone_leggings;
    public static Item steel_infused_bone_chestplate;
    public static Item steel_infused_bone_helmet;
    public static Item steel_infused_bone_boots;



    public static Item manaRegenIdolCopper;
    public static Item manaRegenIdolSilver;
    public static Item manaRegenIdolBrass;
    public static Item manaRegenIdolBronze;

    public static ItemArmor.ArmorMaterial OBSIDIAN_CHAIN = EnumHelper.addArmorMaterial(
            "mkultra_obsidian_chain",
            "mkultra:obsidian_chain", 50,
            new int[]{2, 5, 5, 3}, 20, null, 0);

    public static ItemArmor.ArmorMaterial DIAMOND_DUSTED_INVAR_MAT = EnumHelper.addArmorMaterial(
            "mkultra_diamond_dusted_invar",
            "mkultra:diamond_dusted_invar", 50,
            new int[]{3, 6, 6, 3}, 24, null, 0);

    public static ItemArmor.ArmorMaterial STEEL_INFUSED_BONE_MAT = EnumHelper.addArmorMaterial(
            "mkultra_steel_infused_bone",
            "mkultra:steel_infused_bone", 40,
            new int[]{2, 3, 3, 2}, 12, null, 0);

    public static ItemArmor.ArmorMaterial COPPER_THREADED_MAT = EnumHelper.addArmorMaterial(
            "mkultra_copper_threaded",
            "mkultra:copper_threaded", 35,
            new int[]{0, 1, 1, 0}, 5, null, 0);


    // can't be public because this is an ObjectHolder
    private static final Set<Item> ALL_ITEMS = new HashSet<>();

    private static void regInternal(Item item, String pathName) {
        item.setUnlocalizedName(pathName);
        item.setRegistryName("mkultrax", pathName);
        ALL_ITEMS.add(item);
    }

    private static void regInternal(Item item) {
        // skip 'item.'
        regInternal(item, item.getUnlocalizedName().substring(5));
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        ALL_ITEMS.forEach(event.getRegistry()::register);
    }

    @SuppressWarnings("unused")
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ALL_ITEMS.stream()
                .filter(item -> item.getRegistryName() != null)
                .forEach(item ->
                        ModelLoader.setCustomModelResourceLocation(item, 0,
                                new ModelResourceLocation(item.getRegistryName(), "inventory")));
    }

    public static void initItems() {
        OBSIDIAN_CHAIN.setRepairItem(new ItemStack(
                Materials.getMaterialByName(MaterialNames.OBSIDIAN).getItem(Names.INGOT)));
        regInternal(obsidian_chain_boots = new ItemSpeedArmor(
                "obsidian_chain_boots", OBSIDIAN_CHAIN, 2, EntityEquipmentSlot.FEET, .1f, feetUUID)
                .setCreativeTab(MKUltra.MKULTRA_TAB));
        regInternal(obsidian_chain_chestplate = new ItemCDRArmor(
                "obsidian_chain_chestplate", OBSIDIAN_CHAIN, 1, EntityEquipmentSlot.CHEST, .1f, chestUUID)
                .setCreativeTab(MKUltra.MKULTRA_TAB));
        regInternal(obsidian_chain_leggings = new ItemHealthArmor(
                "obsidian_chain_leggings", OBSIDIAN_CHAIN, 2, EntityEquipmentSlot.LEGS, 5, leggingsUUID)
                .setCreativeTab(MKUltra.MKULTRA_TAB));
        regInternal(obsidian_chain_helmet = new ItemManaRegenArmor(
                "obsidian_chain_helmet", OBSIDIAN_CHAIN, 1, EntityEquipmentSlot.HEAD, 1.0f, helmetUUID)
                .setCreativeTab(MKUltra.MKULTRA_TAB));

        regInternal(diamond_dusted_invar_chestplate = new ItemHealthArmor(
                "diamond_dusted_invar_chestplate",
                DIAMOND_DUSTED_INVAR_MAT, 1,
                EntityEquipmentSlot.CHEST, 5.0f, chestUUID).setCreativeTab(MKUltra.MKULTRA_TAB));
        regInternal(diamond_dusted_invar_helmet = new ItemManaRegenArmor(
                "diamond_dusted_invar_helmet",
                DIAMOND_DUSTED_INVAR_MAT, 1,
                EntityEquipmentSlot.HEAD, 0.75f, helmetUUID).setCreativeTab(MKUltra.MKULTRA_TAB));
        regInternal(diamond_dusted_invar_leggings = new ItemManaArmor(
                "diamond_dusted_invar_leggings",
                DIAMOND_DUSTED_INVAR_MAT, 2,
                EntityEquipmentSlot.LEGS, 5, leggingsUUID).setCreativeTab(MKUltra.MKULTRA_TAB));
        regInternal(diamond_dusted_invar_boots = new ItemCDRArmor(
                "diamond_dusted_invar_boots",
                DIAMOND_DUSTED_INVAR_MAT, 2,
                EntityEquipmentSlot.FEET, .1f, feetUUID).setCreativeTab(MKUltra.MKULTRA_TAB));

        regInternal(manaRegenIdolCopper = new ManaRegenIdol(
                "mana_regen_idol_copper", .5f, 0, 0, 0, 250)
                .setCreativeTab(MKUltra.MKULTRA_TAB));
        regInternal(manaRegenIdolSilver = new ManaRegenIdol(
                "mana_regen_idol_silver", .75f, 5, 2, 4, 400)
                .setCreativeTab(MKUltra.MKULTRA_TAB));
        regInternal(manaRegenIdolBrass = new ManaRegenIdol(
                "mana_regen_idol_brass", 0.25f, 5, 1, 0, 200)
                .setCreativeTab(MKUltra.MKULTRA_TAB));
        regInternal(manaRegenIdolBronze = new ManaRegenIdol(
                "mana_regen_idol_bronze", .5f, 5, 0, 0, 300)
                .setCreativeTab(MKUltra.MKULTRA_TAB));

        regInternal(steel_infused_bone_leather = new Item().setCreativeTab(MKUltra.MKULTRA_TAB),
                "steel_infused_bone_leather");
        STEEL_INFUSED_BONE_MAT.setRepairItem(new ItemStack(steel_infused_bone_leather));
        regInternal(steel_infused_bone_chestplate = new ItemMagicDamageArmor(
                "steel_infused_bone_chestplate",
                STEEL_INFUSED_BONE_MAT, 1,
                EntityEquipmentSlot.CHEST, 1.0f, chestUUID).setCreativeTab(MKUltra.MKULTRA_TAB));
        regInternal(steel_infused_bone_helmet = new ItemManaRegenArmor(
                "steel_infused_bone_helmet", STEEL_INFUSED_BONE_MAT, 1,
                EntityEquipmentSlot.HEAD, 0.5f, helmetUUID).setCreativeTab(MKUltra.MKULTRA_TAB));
        regInternal(steel_infused_bone_leggings = new ItemManaArmor(
                "steel_infused_bone_leggings", STEEL_INFUSED_BONE_MAT, 2,
                EntityEquipmentSlot.LEGS, 2, leggingsUUID).setCreativeTab(MKUltra.MKULTRA_TAB));
        regInternal(steel_infused_bone_boots = new ItemManaArmor(
                "steel_infused_bone_boots", STEEL_INFUSED_BONE_MAT, 2,
                EntityEquipmentSlot.FEET, 2, feetUUID).setCreativeTab(MKUltra.MKULTRA_TAB));

        regInternal(copper_threaded_cloth = new Item().setCreativeTab(MKUltra.MKULTRA_TAB),
                "copper_threaded_cloth");
        COPPER_THREADED_MAT.setRepairItem(new ItemStack(copper_threaded_cloth));
        regInternal(copper_threaded_chestplate = new ItemManaArmor(
                "copper_threaded_chestplate", COPPER_THREADED_MAT, 1,
                EntityEquipmentSlot.CHEST, 2, chestUUID).setCreativeTab(MKUltra.MKULTRA_TAB));
        regInternal(copper_threaded_helmet = new ItemManaRegenArmor(
                "copper_threaded_helmet", COPPER_THREADED_MAT, 1,
                EntityEquipmentSlot.HEAD, 0.5f, helmetUUID).setCreativeTab(MKUltra.MKULTRA_TAB));
        regInternal(copper_threaded_leggings = new ItemManaArmor(
                "copper_threaded_leggings", COPPER_THREADED_MAT, 2,
                EntityEquipmentSlot.LEGS, 2, leggingsUUID).setCreativeTab(MKUltra.MKULTRA_TAB));
        regInternal(copper_threaded_boots = new ItemManaArmor(
                "copper_threaded_boots", COPPER_THREADED_MAT, 2,
                EntityEquipmentSlot.FEET, 1, feetUUID).setCreativeTab(MKUltra.MKULTRA_TAB));

    }

    private static void addRecipe(RegistryEvent.Register<IRecipe> event, ItemStack stack, Object... recipe) {
        ResourceLocation name = stack.getItem().getRegistryName();
        if (name != null) {
            event.getRegistry().register(new ShapedOreRecipe(name, stack, recipe).setRegistryName(name));
        }
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void initCrafting(RegistryEvent.Register<IRecipe> event) {
        addRecipe(event, new ItemStack(obsidian_chain_chestplate),
                "l l", "ili", "lil", 'i',
                com.mcmoddev.basemetals.init.Materials.getMaterialByName(MaterialNames.OBSIDIAN).getItem(Names.INGOT),
                'l', Items.LEATHER);
        addRecipe(event, new ItemStack(obsidian_chain_leggings),
                "lil", "i i", "l l", 'i',
                com.mcmoddev.basemetals.init.Materials.getMaterialByName(MaterialNames.OBSIDIAN).getItem(Names.INGOT),
                'l', Items.LEATHER);
        addRecipe(event, new ItemStack(obsidian_chain_helmet),
                "lil", "i i", 'i',
                com.mcmoddev.basemetals.init.Materials.getMaterialByName(MaterialNames.OBSIDIAN).getItem(Names.INGOT),
                'l', Items.LEATHER);
        addRecipe(event, new ItemStack(obsidian_chain_boots),
                "i i", "l l", 'i',
                com.mcmoddev.basemetals.init.Materials.getMaterialByName(MaterialNames.OBSIDIAN).getItem(Names.INGOT),
                'l', Items.LEATHER);


        addRecipe(event, new ItemStack(diamond_dusted_invar_chestplate),
                "i i", "idi", "iii", 'i',
                com.mcmoddev.basemetals.init.Materials.getMaterialByName(MaterialNames.INVAR).getItem(Names.INGOT),
                'd', ModItems.diamond_dust);
        addRecipe(event, new ItemStack(diamond_dusted_invar_leggings),
                "idi", "i i", "i i", 'i',
                com.mcmoddev.basemetals.init.Materials.getMaterialByName(MaterialNames.INVAR).getItem(Names.INGOT),
                'd', ModItems.diamond_dust);
        addRecipe(event, new ItemStack(diamond_dusted_invar_helmet),
                "idi", "i i", 'i',
                com.mcmoddev.basemetals.init.Materials.getMaterialByName(MaterialNames.INVAR).getItem(Names.INGOT),
                'd', ModItems.diamond_dust);
        addRecipe(event, new ItemStack(diamond_dusted_invar_boots),
                "d d", "i i", 'i',
                com.mcmoddev.basemetals.init.Materials.getMaterialByName(MaterialNames.INVAR).getItem(Names.INGOT),
                'd', ModItems.diamond_dust);


        CrusherRecipeRegistry.addNewCrusherRecipe(Items.DIAMOND, new ItemStack(ModItems.diamond_dust, 4));
        CrusherRecipeRegistry.addNewCrusherRecipe(ModItems.hempLeaves,
                new ItemStack(ModItems.hempFibers, 2));
        CrusherRecipeRegistry.addNewCrusherRecipe(Blocks.OBSIDIAN,
                new ItemStack(com.mcmoddev.basemetals.init.Materials.getMaterialByName(MaterialNames.OBSIDIAN).getItem(Names.POWDER), 4));
    }

    public static void init() {
        ArmorClass.ROBES
                .register(COPPER_THREADED_MAT);
        ArmorClass.LIGHT
                .register(STEEL_INFUSED_BONE_MAT);
        ArmorClass.MEDIUM
                .register(OBSIDIAN_CHAIN);
        ArmorClass.HEAVY
                .register(DIAMOND_DUSTED_INVAR_MAT);

        ArmorClass.ROBES
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.STARSTEEL)));

        ArmorClass.LIGHT
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.AQUARIUM)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.TIN)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.MITHRIL)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.PEWTER)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.BISMUTH)));

        ArmorClass.MEDIUM
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.COPPER)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.BRASS)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.SILVER)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.ELECTRUM)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.NICKEL)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.QUARTZ)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.CUPRONICKEL)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.ANTIMONY)));

        ArmorClass.HEAVY
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.LEAD)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.STEEL)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.INVAR)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.COLDIRON)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.ADAMANTINE)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.OBSIDIAN)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.ZINC)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.EMERALD)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.PLATINUM)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.BRONZE)));
    }
}
