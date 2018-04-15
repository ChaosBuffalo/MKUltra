package com.chaosbuffalo.mkultra.init;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.item.*;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.lib.data.Names;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mod.EventBusSubscriber
@GameRegistry.ObjectHolder(MKUltra.MODID)
public final class ModItems {

    public static Item diamond_dust;
    public static Item sunicon;
    public static Item moon_icon;
    public static Item manaRegenIdolCopper;
    public static Item manaRegenIdolIron;
    public static Item manaRegenIdolGold;
    public static Item manaRegenIdolWood;
    public static Item manaRegenIdolSilver;
    public static Item manaRegenIdolBrass;
    public static Item manaRegenIdolBronze;
    public static Item chainmailChestplate;
    public static Item chainmailHelmet;
    public static Item chainmailBoots;
    public static Item chainmailLeggings;
    public static Item gold_threaded_helmet;
    public static Item gold_threaded_boots;
    public static Item gold_threaded_chestplate;
    public static Item gold_threaded_leggings;
    public static Item woodSpear;
    public static Item ironSpear;
    public static Item stoneSpear;
    public static Item goldSpear;
    public static Item diamondSpear;
    public static Item emerald_spear;
    public static Item obsidian_spear;
    public static Item forgetfulnessBread;
    public static Item hempSeeds;
    public static Item hempLeaves;
    public static Item hempFibers;
    public static Item gold_threaded_cloth;
    public static Item hempSeedBread;
    public static Item pipe;
    public static Item copperSpear;
    public static Item tinSpear;
    public static Item silverSpear;
    public static Item steelSpear;
    public static Item starsteelSpear;
    public static Item nickelSpear;
    public static Item mithrilSpear;
    public static Item leadSpear;
    public static Item invarSpear;
    public static Item electrumSpear;
    public static Item coldironSpear;
    public static Item bronzeSpear;
    public static Item brassSpear;
    public static Item aquariumSpear;
    public static Item adamantineSpear;
    public static Item drownProjectile;
    public static Item geyserProjectile;
    public static Item ballLightning;
    public static Item bonedLeather;
    public static Item bonedLeatherLeggings;
    public static Item bonedLeatherChestplate;
    public static Item bonedLeatherHelmet;
    public static Item bonedLeatherBoots;
    public static Item copper_threaded_cloth;
    public static Item copper_threaded_leggings;
    public static Item copper_threaded_chestplate;
    public static Item copper_threaded_helmet;
    public static Item copper_threaded_boots;
    public static Item iron_threaded_cloth;
    public static Item iron_threaded_leggings;
    public static Item iron_threaded_chestplate;
    public static Item iron_threaded_helmet;
    public static Item iron_threaded_boots;
    public static Item duality_rune_projectile;
    public static Item whirlpool_projectile;
    public static Item phoenix_dust;
    public static Item steel_infused_bone_leather;
    public static Item steel_infused_bone_leggings;
    public static Item steel_infused_bone_chestplate;
    public static Item steel_infused_bone_helmet;
    public static Item steel_infused_bone_boots;

    public static Item obsidian_chain_leggings;
    public static Item obsidian_chain_chestplate;
    public static Item obsidian_chain_helmet;
    public static Item obsidian_chain_boots;

    public static Item diamond_dusted_invar_leggings;
    public static Item diamond_dusted_invar_chestplate;
    public static Item diamond_dusted_invar_helmet;
    public static Item diamond_dusted_invar_boots;


    public static ItemArmor.ArmorMaterial OBSIDIAN_CHAIN = EnumHelper.addArmorMaterial("mkultra_obsidian_chain",
            "mkultra:obsidian_chain", 15,
            new int[]{2, 5, 5, 3}, 20, null, 0);
    public static ItemArmor.ArmorMaterial CHAINMAT = EnumHelper.addArmorMaterial("mkultra_chain",
            "mkultra:chainmail", 15,
            new int[]{1, 4, 5, 2}, 12, null, 0);
    public static ItemArmor.ArmorMaterial ROBEMAT = EnumHelper.addArmorMaterial("mkultra_gold_threaded",
            "mkultra:gold_threaded", 20,
            new int[]{1, 1, 1, 1}, 35, null, 0);
    public static ItemArmor.ArmorMaterial BONEDLEATHERMAT = EnumHelper.addArmorMaterial("mkultra_boned_leather",
            "mkultra:bone", 24,
            new int[]{2, 3, 2, 1}, 2, null, 0);

    public static ItemArmor.ArmorMaterial COPPER_THREADED_MAT = EnumHelper.addArmorMaterial(
            "mkultra_copper_threaded",
            "mkultra:copper_threaded", 15,
            new int[]{0, 1, 1, 0}, 5, null, 0);

    public static ItemArmor.ArmorMaterial IRON_THREADED_MAT = EnumHelper.addArmorMaterial(
            "mkultra_iron_threaded",
            "mkultra:iron_threaded", 28,
            new int[]{1, 2, 2, 1}, 20, null, 0);

    public static ItemArmor.ArmorMaterial STEEL_INFUSED_BONE_MAT = EnumHelper.addArmorMaterial("mkultra_steel_infused_bone",
            "mkultra:steel_infused_bone", 30,
            new int[]{2, 3, 3, 2}, 12, null, 0);

    public static ItemArmor.ArmorMaterial DIAMOND_DUSTED_INVAR_MAT = EnumHelper.addArmorMaterial("mkultra_diamond_dusted_invar",
            "mkultra:diamond_dusted_invar", 35,
            new int[]{3, 6, 6, 3}, 24, null, 0);

    // can't be public because this is an ObjectHolder
    private static final Set<Item> ALL_ITEMS = new HashSet<>();

    private static void regInternal(Item item, String pathName) {
        item.setUnlocalizedName(pathName);
        item.setRegistryName(MKUltra.MODID, pathName);
        ALL_ITEMS.add(item);
    }

    private static void regInternal(Item item) {
        // skip 'item.'
        regInternal(item, item.getUnlocalizedName().substring(5));
    }

    public static void initItems() {
        // Class-related
        regInternal(diamond_dust = new AngelDust("diamond_dust"));
        regInternal(phoenix_dust = new PhoenixDust("phoenix_dust"));
        regInternal(sunicon = new ClassIcon("sunicon", "The Sun God will bestow on you great powers. Choose your class: ", 8,
                new ResourceLocation(MKUltra.MODID, "textures/items/sunicon.png"), "Give your Brouzoufs to Solarius." +
                "  Receive his blessings.",
                new ResourceLocation(MKUltra.MODID, "textures/gui/xp_table_background.png")
                , 38600), "sunicon");
        regInternal(moon_icon = new ClassIcon("moon_icon", "The Mysterious Moon Goddess offers her arts to you. Choose your class: ", 1,
                new ResourceLocation(MKUltra.MODID, "textures/items/moon_icon.png"),
                "Thalassa, Goddess of the Moon, demands brouzouf in exchange for her powers. ",
                new ResourceLocation(MKUltra.MODID, "textures/gui/xp_table_background_moon.png")
                , 4404838), "moon_icon");
        regInternal(forgetfulnessBread = new ForgetfulnessBread(8, 1.0f, false), "forgetfulnessBread");


        //Projectile items
        regInternal(drownProjectile = new Item().setCreativeTab(CreativeTabs.MATERIALS), "drownProjectile");
        regInternal(geyserProjectile = new Item().setCreativeTab(CreativeTabs.MATERIALS), "geyserProjectile");
        regInternal(duality_rune_projectile = new Item().setCreativeTab(CreativeTabs.MATERIALS), "duality_rune_projectile");
        regInternal(ballLightning = new Item().setCreativeTab(CreativeTabs.MATERIALS), "ballLightning");
        regInternal(whirlpool_projectile = new Item().setCreativeTab(CreativeTabs.MATERIALS), "whirlpool_projectile");

        regInternal(manaRegenIdolCopper = new ManaRegenIdol("mana_regen_idol_copper", .5f, 0, 0, 0, 125));
        regInternal(manaRegenIdolIron = new ManaRegenIdol("mana_regen_idol_iron", .5f, 0, 2, 0, 175));
        regInternal(manaRegenIdolWood = new ManaRegenIdol("mana_regen_idol_wood", .25f, 5, 0, 1, 50));
        regInternal(manaRegenIdolBrass = new ManaRegenIdol("mana_regen_idol_brass", 0.25f, 5, 1, 0, 75));
        regInternal(manaRegenIdolBronze = new ManaRegenIdol("mana_regen_idol_bronze", .5f, 5, 0, 0, 150));
        regInternal(manaRegenIdolGold = new ManaRegenIdol("mana_regen_idol_gold", 1.0f, 5, 0, 4, 125));
        regInternal(manaRegenIdolSilver = new ManaRegenIdol("mana_regen_idol_silver", .75f, 5, 2, 4, 150));

        UUID chestUUID = UUID.fromString("77ab4b54-5885-4f7f-ab41-71af536309d1");
        UUID leggingsUUID = UUID.fromString("8d827c58-8f61-4c77-8dcd-f62f0e69121b");
        UUID helmetUUID = UUID.fromString("fb16408c-0421-4138-a283-8da7038e5970");
        UUID feetUUID = UUID.fromString("fb16408c-0421-4138-a283-8da7038e5972");

        regInternal(copper_threaded_cloth = new Item().setCreativeTab(CreativeTabs.MATERIALS), "copper_threaded_cloth");
        COPPER_THREADED_MAT.setRepairItem(new ItemStack(copper_threaded_cloth));
        regInternal(copper_threaded_chestplate = new ItemManaArmor("copper_threaded_chestplate", COPPER_THREADED_MAT, 1,
                EntityEquipmentSlot.CHEST, 2, chestUUID));
        regInternal(copper_threaded_helmet = new ItemManaRegenArmor("copper_threaded_helmet", COPPER_THREADED_MAT, 1,
                EntityEquipmentSlot.HEAD, 0.5f, helmetUUID));
        regInternal(copper_threaded_leggings = new ItemManaArmor("copper_threaded_leggings", COPPER_THREADED_MAT, 2,
                EntityEquipmentSlot.LEGS, 2, leggingsUUID));
        regInternal(copper_threaded_boots = new ItemManaArmor("copper_threaded_boots", COPPER_THREADED_MAT, 2,
                EntityEquipmentSlot.FEET, 1, feetUUID));

        regInternal(iron_threaded_cloth = new Item().setCreativeTab(CreativeTabs.MATERIALS), "iron_threaded_cloth");
        IRON_THREADED_MAT.setRepairItem(new ItemStack(iron_threaded_cloth));
        regInternal(iron_threaded_chestplate = new ItemMagicDamageArmor("iron_threaded_chestplate", IRON_THREADED_MAT, 1,
                EntityEquipmentSlot.CHEST, 1.0f, chestUUID));
        regInternal(iron_threaded_helmet = new ItemManaRegenArmor("iron_threaded_helmet", IRON_THREADED_MAT, 1,
                EntityEquipmentSlot.HEAD, 1.0f, helmetUUID));
        regInternal(iron_threaded_leggings = new ItemManaArmor("iron_threaded_leggings", IRON_THREADED_MAT, 2,
                EntityEquipmentSlot.LEGS, 3, leggingsUUID));
        regInternal(iron_threaded_boots = new ItemManaArmor("iron_threaded_boots", IRON_THREADED_MAT, 2,
                EntityEquipmentSlot.FEET, 2, feetUUID));

        regInternal(steel_infused_bone_leather = new Item().setCreativeTab(CreativeTabs.MATERIALS), "steel_infused_bone_leather");
        STEEL_INFUSED_BONE_MAT.setRepairItem(new ItemStack(steel_infused_bone_leather));
        regInternal(steel_infused_bone_chestplate = new ItemMagicDamageArmor("steel_infused_bone_chestplate",
                STEEL_INFUSED_BONE_MAT, 1,
                EntityEquipmentSlot.CHEST, 1.0f, chestUUID));
        regInternal(steel_infused_bone_helmet = new ItemManaRegenArmor("steel_infused_bone_helmet", STEEL_INFUSED_BONE_MAT, 1,
                EntityEquipmentSlot.HEAD, 0.5f, helmetUUID));
        regInternal(steel_infused_bone_leggings = new ItemManaArmor("steel_infused_bone_leggings", STEEL_INFUSED_BONE_MAT, 2,
                EntityEquipmentSlot.LEGS, 2, leggingsUUID));
        regInternal(steel_infused_bone_boots = new ItemManaArmor("steel_infused_bone_boots", STEEL_INFUSED_BONE_MAT, 2,
                EntityEquipmentSlot.FEET, 2, feetUUID));

        regInternal(diamond_dusted_invar_chestplate = new ItemHealthArmor("diamond_dusted_invar_chestplate",
                DIAMOND_DUSTED_INVAR_MAT, 1,
                EntityEquipmentSlot.CHEST, 5.0f, chestUUID));
        regInternal(diamond_dusted_invar_helmet = new ItemManaRegenArmor("diamond_dusted_invar_helmet",
                DIAMOND_DUSTED_INVAR_MAT, 1,
                EntityEquipmentSlot.HEAD, 0.75f, helmetUUID));
        regInternal(diamond_dusted_invar_leggings = new ItemManaArmor("diamond_dusted_invar_leggings",
                DIAMOND_DUSTED_INVAR_MAT, 2,
                EntityEquipmentSlot.LEGS, 5, leggingsUUID));
        regInternal(diamond_dusted_invar_boots = new ItemCDRArmor("diamond_dusted_invar_boots",
                DIAMOND_DUSTED_INVAR_MAT, 2,
                EntityEquipmentSlot.FEET, .1f, feetUUID));


        CHAINMAT.setRepairItem(new ItemStack(Items.IRON_INGOT));
        regInternal(chainmailChestplate = new ItemModArmor("chainmail_chestplate", CHAINMAT, 1, EntityEquipmentSlot.CHEST));
        regInternal(chainmailHelmet = new ItemModArmor("chainmail_helmet", CHAINMAT, 1, EntityEquipmentSlot.HEAD));
        regInternal(chainmailLeggings = new ItemModArmor("chainmail_leggings", CHAINMAT, 2, EntityEquipmentSlot.LEGS));
        regInternal(chainmailBoots = new ItemModArmor("chainmail_boots", CHAINMAT, 2, EntityEquipmentSlot.FEET));

        regInternal(gold_threaded_cloth = new Item().setCreativeTab(CreativeTabs.MATERIALS), "gold_threaded_cloth");
        ROBEMAT.setRepairItem(new ItemStack(ModItems.gold_threaded_cloth));
        regInternal(gold_threaded_chestplate = new ItemMagicDamageArmor("gold_threaded_chestplate", ROBEMAT, 1, EntityEquipmentSlot.CHEST,
                2.0f, chestUUID));
        regInternal(gold_threaded_helmet = new ItemManaRegenArmor("gold_threaded_helmet", ROBEMAT, 1, EntityEquipmentSlot.HEAD,
                1.5f, helmetUUID));
        regInternal(gold_threaded_leggings = new ItemManaArmor("gold_threaded_leggings", ROBEMAT, 2, EntityEquipmentSlot.LEGS,
                5, leggingsUUID));
        regInternal(gold_threaded_boots = new ItemManaArmor("gold_threaded_boots", ROBEMAT, 2, EntityEquipmentSlot.FEET,
                5, feetUUID));

        OBSIDIAN_CHAIN.setRepairItem(new ItemStack(Materials.getMaterialByName(MaterialNames.OBSIDIAN).getItem(Names.INGOT)));
        regInternal(obsidian_chain_boots = new ItemSpeedArmor(
                "obsidian_chain_boots", OBSIDIAN_CHAIN, 2, EntityEquipmentSlot.FEET, .1f, feetUUID));
        regInternal(obsidian_chain_chestplate = new ItemCDRArmor(
                "obsidian_chain_chestplate", OBSIDIAN_CHAIN, 1, EntityEquipmentSlot.CHEST, .1f, chestUUID));
        regInternal(obsidian_chain_leggings = new ItemHealthArmor(
                "obsidian_chain_leggings", OBSIDIAN_CHAIN, 2, EntityEquipmentSlot.LEGS, 5, leggingsUUID));
        regInternal(obsidian_chain_helmet = new ItemManaRegenArmor(
                "obsidian_chain_helmet", OBSIDIAN_CHAIN, 1, EntityEquipmentSlot.HEAD, 1.0f, helmetUUID));


        regInternal(bonedLeather = new Item().setCreativeTab(CreativeTabs.MATERIALS), "bonedLeather");
        BONEDLEATHERMAT.setRepairItem(new ItemStack(ModItems.bonedLeather));
        regInternal(bonedLeatherChestplate = new ItemModArmor("bonedLeatherChestplate", BONEDLEATHERMAT, 1, EntityEquipmentSlot.CHEST));
        regInternal(bonedLeatherHelmet = new ItemModArmor("bonedLeatherHelmet", BONEDLEATHERMAT, 1, EntityEquipmentSlot.HEAD));
        regInternal(bonedLeatherLeggings = new ItemModArmor("bonedLeatherLeggings", BONEDLEATHERMAT, 2, EntityEquipmentSlot.LEGS));
        regInternal(bonedLeatherBoots = new ItemModArmor("bonedLeatherBoots", BONEDLEATHERMAT, 2, EntityEquipmentSlot.FEET));


        // All things hemp
        regInternal(hempSeeds = new ItemSeeds(ModBlocks.hempBlock, Blocks.FARMLAND), "hempSeeds");
        regInternal(hempLeaves = new Item().setCreativeTab(CreativeTabs.MATERIALS), "hempLeaves");
        regInternal(hempFibers = new Item().setCreativeTab(CreativeTabs.MATERIALS), "hempFibers");
        regInternal(hempSeedBread = new ItemFood(7, 8.0f, false), "hempSeedBread");
        regInternal(pipe = new Pipe("hemp_pipe"), "hemp_pipe");


        // so many spears
        float spearRange = 4.75f;
        regInternal(ironSpear = new ItemRangeSword("ironSpear", Materials.getMaterialByName(MaterialNames.IRON), spearRange));
        regInternal(woodSpear = new ItemRangeSword("woodSpear", Materials.getMaterialByName(MaterialNames.WOOD), spearRange));
        regInternal(stoneSpear = new ItemRangeSword("stoneSpear", Materials.getMaterialByName(MaterialNames.STONE), spearRange));
        regInternal(goldSpear = new ItemRangeSword("goldSpear", Materials.getMaterialByName(MaterialNames.GOLD), spearRange));
        regInternal(diamondSpear = new ItemRangeSword("diamondSpear", Materials.getMaterialByName(MaterialNames.DIAMOND), spearRange));
        regInternal(copperSpear = new ItemRangeSword("copperSpear", Materials.getMaterialByName(MaterialNames.COPPER), spearRange));
        regInternal(tinSpear = new ItemRangeSword("tinSpear", Materials.getMaterialByName(MaterialNames.TIN), spearRange));
        regInternal(silverSpear = new ItemRangeSword("silverSpear", Materials.getMaterialByName(MaterialNames.SILVER), spearRange));
        regInternal(steelSpear = new ItemRangeSword("steelSpear", Materials.getMaterialByName(MaterialNames.STEEL), spearRange));
        regInternal(starsteelSpear = new ItemRangeSword("starsteelSpear", Materials.getMaterialByName(MaterialNames.STARSTEEL), spearRange));
        regInternal(nickelSpear = new ItemRangeSword("nickelSpear", Materials.getMaterialByName(MaterialNames.NICKEL), spearRange));
        regInternal(mithrilSpear = new ItemRangeSword("mithrilSpear", Materials.getMaterialByName(MaterialNames.MITHRIL), spearRange));
        regInternal(leadSpear = new ItemRangeSword("leadSpear", Materials.getMaterialByName(MaterialNames.LEAD), spearRange));
        regInternal(invarSpear = new ItemRangeSword("invarSpear", Materials.getMaterialByName(MaterialNames.INVAR), spearRange));
        regInternal(electrumSpear = new ItemRangeSword("electrumSpear", Materials.getMaterialByName(MaterialNames.ELECTRUM), spearRange));
        regInternal(coldironSpear = new ItemRangeSword("coldironSpear", Materials.getMaterialByName(MaterialNames.COLDIRON), spearRange));
        regInternal(bronzeSpear = new ItemRangeSword("bronzeSpear", Materials.getMaterialByName(MaterialNames.BRONZE), spearRange));
        regInternal(brassSpear = new ItemRangeSword("brassSpear", Materials.getMaterialByName(MaterialNames.BRASS), spearRange));
        regInternal(aquariumSpear = new ItemRangeSword("aquariumSpear", Materials.getMaterialByName(MaterialNames.AQUARIUM), spearRange));
        regInternal(adamantineSpear = new ItemRangeSword("adamantineSpear", Materials.getMaterialByName(MaterialNames.ADAMANTINE), spearRange));
        regInternal(emerald_spear = new ItemRangeSword("emerald_spear", Materials.getMaterialByName(MaterialNames.EMERALD), spearRange));
        regInternal(obsidian_spear = new ItemRangeSword("obsidian_spear", Materials.getMaterialByName(MaterialNames.OBSIDIAN), spearRange));

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
}
