package com.chaosbuffalo.mkultra.init;

import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.item.MKUArmorMaterial;
import com.chaosbuffalo.mkweapons.items.armor.MKArmorItem;
import com.chaosbuffalo.mkweapons.items.effects.armor.ArmorModifierEffect;
import com.chaosbuffalo.mkweapons.items.randomization.options.AttributeOptionEntry;
import com.google.common.collect.Lists;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.*;

@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class MKUItems {

    @ObjectHolder(MKUltra.MODID + ":cleansing_seed_projectile")
    public static Item cleansingSeedProjectileItem;

    @ObjectHolder(MKUltra.MODID + ":spirit_bomb_projectile")
    public static Item spiritBombProjectileItem;

    @ObjectHolder(MKUltra.MODID + ":fireball_projectile")
    public static Item fireballProjectileItem;

    @ObjectHolder(MKUltra.MODID + ":green_knight_helmet")
    public static Item greenKnightHelmet;

    @ObjectHolder(MKUltra.MODID + ":green_knight_leggings")
    public static Item greenKnightLeggings;

    @ObjectHolder(MKUltra.MODID + ":green_knight_chestplate")
    public static Item greenKnightChestplate;

    @ObjectHolder(MKUltra.MODID + ":green_knight_boots")
    public static Item greenKnightBoots;

    @ObjectHolder(MKUltra.MODID + ":corrupted_pig_iron_plate")
    public static Item corruptedPigIronPlate;

    @ObjectHolder(MKUltra.MODID + ":trooper_knight_helmet")
    public static Item trooperKnightHelmet;

    @ObjectHolder(MKUltra.MODID + ":trooper_knight_leggings")
    public static Item trooperKnightLeggings;

    @ObjectHolder(MKUltra.MODID + ":trooper_knight_chestplate")
    public static Item trooperKnightChestplate;

    @ObjectHolder(MKUltra.MODID + ":trooper_knight_boots")
    public static Item trooperKnightBoots;

    @ObjectHolder(MKUltra.MODID + ":destroyed_trooper_helmet")
    public static Item destroyedTrooperHelmet;

    @ObjectHolder(MKUltra.MODID + ":destroyed_trooper_leggings")
    public static Item destroyedTrooperLeggings;

    @ObjectHolder(MKUltra.MODID + ":destroyed_trooper_chestplate")
    public static Item destroyedTrooperChestplate;

    @ObjectHolder(MKUltra.MODID + ":destroyed_trooper_boots")
    public static Item destroyedTrooperBoots;

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        //projectiles
        event.getRegistry().register(new Item(new Item.Properties())
                .setRegistryName(MKUltra.MODID, "cleansing_seed_projectile"));
        event.getRegistry().register(new Item(new Item.Properties())
                .setRegistryName(MKUltra.MODID, "spirit_bomb_projectile"));
        event.getRegistry().register(new Item(new Item.Properties())
                .setRegistryName(MKUltra.MODID, "fireball_projectile"));

        //quest items
        Item pigIronPlate = new Item(new Item.Properties().group(ItemGroup.MISC))
                .setRegistryName(new ResourceLocation(MKUltra.MODID, "corrupted_pig_iron_plate"));
        event.getRegistry().register(pigIronPlate);
        Item destroyedTrooperHelmet = new Item(new Item.Properties().group(ItemGroup.MISC))
                .setRegistryName(MKUltra.MODID, "destroyed_trooper_helmet");
        Item destroyedTrooperChestplate = new Item(new Item.Properties().group(ItemGroup.MISC))
                .setRegistryName(MKUltra.MODID, "destroyed_trooper_chestplate");
        Item destroyedTrooperBoots = new Item(new Item.Properties().group(ItemGroup.MISC))
                .setRegistryName(MKUltra.MODID, "destroyed_trooper_boots");
        Item destroyedTrooperLeggings = new Item(new Item.Properties().group(ItemGroup.MISC))
                .setRegistryName(MKUltra.MODID, "destroyed_trooper_leggings");
        event.getRegistry().registerAll(destroyedTrooperBoots, destroyedTrooperHelmet,
                destroyedTrooperChestplate, destroyedTrooperLeggings);

        // green knight armor
        List<AttributeOptionEntry> gkHelmetAttrs = Lists.newArrayList(
                new AttributeOptionEntry(MKAttributes.COOLDOWN,
                        new AttributeModifier(UUID.fromString("2013a410-ca6d-48a9-a12d-a70a65ec8190"),
                        "Bonus", 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL)));

        List<AttributeOptionEntry> gkLegsAttrs = Lists.newArrayList(
                new AttributeOptionEntry(MKAttributes.MAX_MANA,
                        new AttributeModifier(UUID.fromString("9b184106-1a7b-444c-8bbe-538bff1f66cd"),
                                "Bonus", 6, AttributeModifier.Operation.ADDITION)),
                new AttributeOptionEntry(MKAttributes.MANA_REGEN,
                        new AttributeModifier(UUID.fromString("25f12c51-a841-4ac9-8fbb-02000a19e563"),
                                "Bonus", 1.0, AttributeModifier.Operation.ADDITION)));

        List<AttributeOptionEntry> gkChestAttrs = Lists.newArrayList(
                new AttributeOptionEntry(Attributes.MAX_HEALTH,
                        new AttributeModifier(UUID.fromString("ea84d132-3e14-40d7-acda-2f8ab0d5f3ad"),
                                "Bonus", 10, AttributeModifier.Operation.ADDITION)),
                new AttributeOptionEntry(MKAttributes.HEAL_BONUS,
                        new AttributeModifier(UUID.fromString("c6359e08-8e0c-4721-b8aa-d55d978f4798"),
                                "Bonus", 2, AttributeModifier.Operation.ADDITION)));

        List<AttributeOptionEntry> gkBootsAttrs = Lists.newArrayList(
                new AttributeOptionEntry(Attributes.ATTACK_SPEED,
                        new AttributeModifier(UUID.fromString("f0d94451-5a80-4669-954d-bc6f6c39ccd0"),
                                "Bonus", 0.10, AttributeModifier.Operation.MULTIPLY_TOTAL)));

        Item gkHelmet = new MKArmorItem(MKUArmorMaterial.GREEN_KNIGHT_ARMOR, EquipmentSlotType.HEAD,
                (new Item.Properties()).group(ItemGroup.COMBAT), new ArmorModifierEffect(gkHelmetAttrs))
                .setRegistryName(MKUltra.MODID, "green_knight_helmet");
        Item gkLeggings = new MKArmorItem(MKUArmorMaterial.GREEN_KNIGHT_ARMOR, EquipmentSlotType.LEGS,
                (new Item.Properties()).group(ItemGroup.COMBAT), new ArmorModifierEffect(gkLegsAttrs))
                .setRegistryName(MKUltra.MODID, "green_knight_leggings");
        Item gkChestplate = new MKArmorItem(MKUArmorMaterial.GREEN_KNIGHT_ARMOR, EquipmentSlotType.CHEST,
                (new Item.Properties()).group(ItemGroup.COMBAT), new ArmorModifierEffect(gkChestAttrs))
                .setRegistryName(MKUltra.MODID, "green_knight_chestplate");
        Item gkBoots = new MKArmorItem(MKUArmorMaterial.GREEN_KNIGHT_ARMOR, EquipmentSlotType.FEET,
                (new Item.Properties()).group(ItemGroup.COMBAT), new ArmorModifierEffect(gkBootsAttrs))
                .setRegistryName(MKUltra.MODID, "green_knight_boots");
        event.getRegistry().registerAll(gkChestplate, gkHelmet, gkBoots, gkLeggings);

        // trooper knight armor
        Item tkHelmet = new MKArmorItem(MKUArmorMaterial.TROOPER_KNIGHT_ARMOR, EquipmentSlotType.HEAD,
                (new Item.Properties()).group(ItemGroup.COMBAT)).setRegistryName(MKUltra.MODID, "trooper_knight_helmet");
        Item tkLeggings = new MKArmorItem(MKUArmorMaterial.TROOPER_KNIGHT_ARMOR, EquipmentSlotType.LEGS,
                (new Item.Properties()).group(ItemGroup.COMBAT)).setRegistryName(MKUltra.MODID, "trooper_knight_leggings");
        Item tkChestplate = new MKArmorItem(MKUArmorMaterial.TROOPER_KNIGHT_ARMOR, EquipmentSlotType.CHEST,
                (new Item.Properties()).group(ItemGroup.COMBAT)).setRegistryName(MKUltra.MODID, "trooper_knight_chestplate");
        Item tkBoots = new MKArmorItem(MKUArmorMaterial.TROOPER_KNIGHT_ARMOR, EquipmentSlotType.FEET,
                (new Item.Properties()).group(ItemGroup.COMBAT)).setRegistryName(MKUltra.MODID, "trooper_knight_boots");
        event.getRegistry().registerAll(tkBoots, tkChestplate, tkHelmet, tkLeggings);


    }

}
