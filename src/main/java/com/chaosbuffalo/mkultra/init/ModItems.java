package com.chaosbuffalo.mkultra.init;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import com.chaosbuffalo.mkultra.item.*;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
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

@Mod.EventBusSubscriber
@GameRegistry.ObjectHolder(MKUltra.MODID)
public final class ModItems {
    public static Item diamond_dust;
    public static Item sun_icon;
    public static Item moon_icon;
    public static Item desperate_icon;
    public static Item manaRegenIdolIron;
    public static Item manaRegenIdolGold;
    public static Item manaRegenIdolWood;
    public static Item chainmailChestplate;
    public static Item chainmailHelmet;
    public static Item chainmailBoots;
    public static Item chainmailLeggings;
    public static Item gold_threaded_helmet;
    public static Item gold_threaded_boots;
    public static Item gold_threaded_chestplate;
    public static Item gold_threaded_leggings;
    public static Item forgetfulnessBread;
    public static Item gold_threaded_cloth;
    public static Item pipe;

    public static Item bonedLeather;
    public static Item bonedLeatherLeggings;
    public static Item bonedLeatherChestplate;
    public static Item bonedLeatherHelmet;
    public static Item bonedLeatherBoots;

    public static Item iron_threaded_cloth;
    public static Item iron_threaded_leggings;
    public static Item iron_threaded_chestplate;
    public static Item iron_threaded_helmet;
    public static Item iron_threaded_boots;

    public static Item drownProjectile;
    public static Item geyserProjectile;
    public static Item ballLightning;
    public static Item duality_rune_projectile;
    public static Item whirlpool_projectile;
    public static Item flame_blade_projectile;
    public static Item fairy_fire_projectile;
    public static Item cleansing_seed_projectile;
    public static Item spirit_bomb_projectile;
    public static Item mob_fireball_projectile;
    public static Item grasping_roots_projectile;

    public static Item fire_extinguisher_flask;
    @GameRegistry.ObjectHolder("ranger_icon")
    public static Item rangerIcon;




    public static ItemArmor.ArmorMaterial CHAINMAT = EnumHelper.addArmorMaterial(
            "mkultra_chain",
            "mkultra:chainmail", 30,
            new int[]{1, 4, 5, 2}, 12, null, 0);
    public static ItemArmor.ArmorMaterial ROBEMAT = EnumHelper.addArmorMaterial(
            "mkultra_gold_threaded",
            "mkultra:gold_threaded", 65,
            new int[]{1, 1, 1, 1}, 35, null, 0);
    public static ItemArmor.ArmorMaterial BONED_LEATHER_MAT = EnumHelper.addArmorMaterial(
            "mkultra_boned_leather",
            "mkultra:boned_leather", 35,
            new int[]{2, 3, 2, 1}, 2, null, 0);



    public static ItemArmor.ArmorMaterial IRON_THREADED_MAT = EnumHelper.addArmorMaterial(
            "mkultra_iron_threaded",
            "mkultra:iron_threaded", 45,
            new int[]{1, 2, 2, 1}, 20, null, 0);





    // can't be public because this is an ObjectHolder
    private static final Set<Item> ALL_ITEMS = new HashSet<>();

    private static void regInternal(Item item, String pathName) {
        item.setTranslationKey(pathName);
        item.setRegistryName(MKUltra.MODID, pathName);
        ALL_ITEMS.add(item);
    }

    private static void regInternal(Item item) {
        // skip 'item.'
        regInternal(item, item.getTranslationKey().substring(5));
    }

    public static void initItems() {
        // Class-related
        regInternal(diamond_dust = new DiamondDust("diamond_dust")
                .setCreativeTab(MKUltra.MKULTRA_TAB));

        regInternal(sun_icon = new ClassIcon("sun_icon",
                "The Sun God will bestow on you great powers. Choose your class: ", 8,
                new ResourceLocation(MKUltra.MODID, "textures/class/icons/sun.png")
        ).setCreativeTab(MKUltra.MKULTRA_TAB), "sun_icon");
        regInternal(moon_icon = new ClassIcon("moon_icon",
                "The Mysterious Moon Goddess offers her arts to you. Choose your class: ", 1,
                new ResourceLocation(MKUltra.MODID, "textures/class/icons/moon.png")
        ).setCreativeTab(MKUltra.MKULTRA_TAB), "moon_icon");
        regInternal(desperate_icon = new ClassIcon("desperate_icon",
                "The Enigmatic Wood Spirit offers her power to you. Choose your class: ",
                1, new ResourceLocation(MKUltra.MODID, "textures/class/icons/desperate.png")
        ).setCreativeTab(MKUltra.MKULTRA_TAB), "desperate_icon");
        regInternal(forgetfulnessBread = new ForgetfulnessBread(8, 1.0f, false)
                .setCreativeTab(MKUltra.MKULTRA_TAB), "forgetfulness_bread");


        //Projectile items
        regInternal(drownProjectile = new Item().setCreativeTab(MKUltra.MKULTRA_TAB),
                "drown_projectile");
        regInternal(geyserProjectile = new Item().setCreativeTab(MKUltra.MKULTRA_TAB),
                "geyser_projectile");
        regInternal(duality_rune_projectile = new Item().setCreativeTab(MKUltra.MKULTRA_TAB),
                "duality_rune_projectile");
        regInternal(ballLightning = new Item().setCreativeTab(MKUltra.MKULTRA_TAB),
                "ballLightning");
        regInternal(whirlpool_projectile = new Item().setCreativeTab(MKUltra.MKULTRA_TAB),
                "whirlpool_projectile");
        regInternal(flame_blade_projectile = new Item().setCreativeTab(MKUltra.MKULTRA_TAB),
                "flame_blade_projectile");
        regInternal(fairy_fire_projectile = new Item().setCreativeTab(MKUltra.MKULTRA_TAB),
                "fairy_fire_projectile");
        regInternal(cleansing_seed_projectile = new Item().setCreativeTab(MKUltra.MKULTRA_TAB),
                "cleansing_seed_projectile");
        regInternal(spirit_bomb_projectile = new Item().setCreativeTab(MKUltra.MKULTRA_TAB),
                "spirit_bomb_projectile");
        regInternal(mob_fireball_projectile = new Item().setCreativeTab(MKUltra.MKULTRA_TAB),
                "mob_fireball_projectile");
        regInternal(grasping_roots_projectile = new Item().setCreativeTab(MKUltra.MKULTRA_TAB),
                "grasping_roots_projectile");


        regInternal(manaRegenIdolIron = new ManaRegenIdol(
                "mana_regen_idol_iron", .5f, 0, 2, 0, 350)
                .setCreativeTab(MKUltra.MKULTRA_TAB));
        regInternal(manaRegenIdolWood = new ManaRegenIdol(
                "mana_regen_idol_wood", .25f, 5, 0, 1, 150)
                .setCreativeTab(MKUltra.MKULTRA_TAB));
        regInternal(manaRegenIdolGold = new ManaRegenIdol(
                "mana_regen_idol_gold", 1.0f, 5, 0, 4, 500)
                .setCreativeTab(MKUltra.MKULTRA_TAB));


        regInternal(iron_threaded_cloth = new Item().setCreativeTab(MKUltra.MKULTRA_TAB),
                "iron_threaded_cloth");
        IRON_THREADED_MAT.setRepairItem(new ItemStack(iron_threaded_cloth));
        regInternal(iron_threaded_chestplate = new ItemAttributeArmor(
                "iron_threaded_chestplate", IRON_THREADED_MAT, 1,
                EntityEquipmentSlot.CHEST,
                new ItemAttributeEntry(1.0, PlayerAttributes.OP_INCREMENT, PlayerAttributes.MAGIC_ATTACK_DAMAGE))
                .setCreativeTab(MKUltra.MKULTRA_TAB));
        regInternal(iron_threaded_helmet = new ItemAttributeArmor(
                "iron_threaded_helmet", IRON_THREADED_MAT, 1,
                EntityEquipmentSlot.HEAD,
                new ItemAttributeEntry(1.0, PlayerAttributes.OP_INCREMENT, PlayerAttributes.MANA_REGEN))
                .setCreativeTab(MKUltra.MKULTRA_TAB));
        regInternal(iron_threaded_leggings = new ItemAttributeArmor(
                "iron_threaded_leggings", IRON_THREADED_MAT, 2,
                EntityEquipmentSlot.LEGS,
                new ItemAttributeEntry(3, PlayerAttributes.OP_INCREMENT, PlayerAttributes.MAX_MANA))
                .setCreativeTab(MKUltra.MKULTRA_TAB));
        regInternal(iron_threaded_boots = new ItemAttributeArmor(
                "iron_threaded_boots", IRON_THREADED_MAT, 2,
                EntityEquipmentSlot.FEET,
                new ItemAttributeEntry(2, PlayerAttributes.OP_INCREMENT, PlayerAttributes.MAX_MANA))
                .setCreativeTab(MKUltra.MKULTRA_TAB));


        CHAINMAT.setRepairItem(new ItemStack(Items.IRON_INGOT));
        regInternal(chainmailChestplate = new ItemModArmor(
                "chainmail_chestplate", CHAINMAT, 1, EntityEquipmentSlot.CHEST)
                .setCreativeTab(MKUltra.MKULTRA_TAB));
        regInternal(chainmailHelmet = new ItemModArmor(
                "chainmail_helmet", CHAINMAT, 1, EntityEquipmentSlot.HEAD)
                .setCreativeTab(MKUltra.MKULTRA_TAB));
        regInternal(chainmailLeggings = new ItemModArmor(
                "chainmail_leggings", CHAINMAT, 2, EntityEquipmentSlot.LEGS)
                .setCreativeTab(MKUltra.MKULTRA_TAB));
        regInternal(chainmailBoots = new ItemModArmor(
                "chainmail_boots", CHAINMAT, 2, EntityEquipmentSlot.FEET)
                .setCreativeTab(MKUltra.MKULTRA_TAB));

        regInternal(gold_threaded_cloth = new Item()
                .setCreativeTab(MKUltra.MKULTRA_TAB), "gold_threaded_cloth");
        ROBEMAT.setRepairItem(new ItemStack(ModItems.gold_threaded_cloth));

        regInternal(gold_threaded_chestplate = new ItemAttributeArmor(
                "gold_threaded_chestplate", ROBEMAT, 1, EntityEquipmentSlot.CHEST,
                new ItemAttributeEntry(2.0, PlayerAttributes.OP_INCREMENT, PlayerAttributes.MAGIC_ATTACK_DAMAGE))
                .setCreativeTab(MKUltra.MKULTRA_TAB));
        regInternal(gold_threaded_helmet = new ItemAttributeArmor(
                "gold_threaded_helmet", ROBEMAT, 1, EntityEquipmentSlot.HEAD,
                new ItemAttributeEntry(1.5, PlayerAttributes.OP_INCREMENT, PlayerAttributes.MANA_REGEN))
                .setCreativeTab(MKUltra.MKULTRA_TAB));
        regInternal(gold_threaded_leggings = new ItemAttributeArmor(
                "gold_threaded_leggings", ROBEMAT, 2, EntityEquipmentSlot.LEGS,
                new ItemAttributeEntry(5.0, PlayerAttributes.OP_INCREMENT, PlayerAttributes.MAX_MANA))
                .setCreativeTab(MKUltra.MKULTRA_TAB));
        regInternal(gold_threaded_boots = new ItemAttributeArmor(
                "gold_threaded_boots", ROBEMAT, 2, EntityEquipmentSlot.FEET,
                new ItemAttributeEntry(5.0, PlayerAttributes.OP_INCREMENT, PlayerAttributes.MAX_MANA))
                .setCreativeTab(MKUltra.MKULTRA_TAB));


        regInternal(bonedLeather = new Item().setCreativeTab(MKUltra.MKULTRA_TAB), "boned_leather");
        BONED_LEATHER_MAT.setRepairItem(new ItemStack(ModItems.bonedLeather));
        regInternal(bonedLeatherChestplate = new ItemModArmor(
                "boned_leather_chestplate", BONED_LEATHER_MAT, 1, EntityEquipmentSlot.CHEST)
                .setCreativeTab(MKUltra.MKULTRA_TAB));
        regInternal(bonedLeatherHelmet = new ItemModArmor(
                "boned_leather_helmet", BONED_LEATHER_MAT, 1, EntityEquipmentSlot.HEAD)
                .setCreativeTab(MKUltra.MKULTRA_TAB));
        regInternal(bonedLeatherLeggings = new ItemModArmor(
                "boned_leather_leggings", BONED_LEATHER_MAT, 2, EntityEquipmentSlot.LEGS)
                .setCreativeTab(MKUltra.MKULTRA_TAB));
        regInternal(bonedLeatherBoots = new ItemModArmor(
                "boned_leather_boots", BONED_LEATHER_MAT, 2, EntityEquipmentSlot.FEET)
                .setCreativeTab(MKUltra.MKULTRA_TAB));

        regInternal(pipe = new Pipe("hemp_pipe").setCreativeTab(MKUltra.MKULTRA_TAB), "hemp_pipe");
        regInternal(fire_extinguisher_flask = new FireExtinguisherFlask(), "fire_extinguisher_flask");
        regInternal(new NPCSpawnerIcon("ranger_icon"));
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
