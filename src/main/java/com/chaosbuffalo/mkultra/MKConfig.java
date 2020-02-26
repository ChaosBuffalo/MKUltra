package com.chaosbuffalo.mkultra;

import com.chaosbuffalo.mkultra.core.ArmorClass;
import com.chaosbuffalo.mkultra.core.ClassLists;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.utils.EntityUtils;
import com.chaosbuffalo.mkultra.utils.MobUtils;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;


@Config(modid = MKUltra.MODID, category = "gameplay")
public class MKConfig {

    @Config.Name("Armor")
    @Config.Comment("Armor Configuration")
    public static Armor armor = new Armor();

    @Config.Name("Cheats")
    public static Cheats cheats = new Cheats();

    @Config.Name("Display")
    @Config.Comment("Display Options")
    public static Display display = new Display();

    @Config.Name("Gameplay")
    @Config.Comment("Gameplay Options")
    public static Gameplay gameplay = new Gameplay();

    @Config.Name("Classes")
    @Config.Comment("Class Configuration - Format <identity>=<classId1>,<classId2>")
    public static ClassConfiguration classConfig = new ClassConfiguration();

    public static class Armor {

        @Config.Comment("Armor Materials for Robes Armor Class")
        public String[] ROBES_ARMOR = {
                "mkultra:iron_threaded",
                "mkultra:gold_threaded",
                "mkultrax:copper_threaded",
                "iceandfire:earplugs",
                "iceandfire:sheep_disguise",
                "iceandfire:blindfold",
                "betterwithmods:wool",
                "starsteel",
                "as.imbuedleather",
                "rubber",
                "bl_cloth",
                "lurker_skin",
                "VOIDROBE",
                "SPECIAL",
        };

        @Config.Comment("Armor item names to add to the Robes Armor Class")
        public String[] ROBES_ITEM_OVERRIDES = {};

        @Config.Comment("Armor Materials for Light Armor Class")
        public String[] LIGHT_ARMOR = {
                "leather",
                "mkultra:boned_leather",
                "iceandfire:troll_frost",
                "iceandfire:troll_forest",
                "iceandfire:troll_mountain",
                "betterwithmods:leather_tanned",
                "tin",
                "mithril",
                "pewter",
                "aquarium",
                "bismuth",
                "mkultrax:steel_infused_bone",
                "slimy_bone",
                "THAUMIUM"
        };

        @Config.Comment("Armor item names to add to the Light Armor Class")
        public String[] LIGHT_ITEM_OVERRIDES = {};


        @Config.Comment("Armor Materials for Medium Armor Class")
        public String[] MEDIUM_ARMOR = {
                "chainmail",
                "gold",
                "mkultra:chainmail",
                "iceandfire:red_deathworm",
                "iceandfire:white_deathworm",
                "iceandfire:yellow_deathworm",
                "iceandfire:armor_silver_metal",
                "copper",
                "brass",
                "silver",
                "nickel",
                "quartz",
                "cupronickel",
                "antimony",
                "electrum",
                "mkultrax:obsidian_chain",
                "syrmorite",
                "VOID"
        };

        @Config.Comment("Armor item names to add to the Medium Armor Class")
        public String[] MEDIUM_ITEM_OVERRIDES = {};

        @Config.Comment("Armor Materials for Heavy Armor Class")
        public String[] HEAVY_ARMOR = {
                "iron",
                "iceandfire:armor_dragon_scales1",
                "iceandfire:armor_dragon_scales2",
                "iceandfire:armor_dragon_scales3",
                "iceandfire:armor_dragon_scales4",
                "iceandfire:armor_dragon_scales5",
                "iceandfire:armor_dragon_scales6",
                "iceandfire:armor_dragon_scales7",
                "iceandfire:armor_dragon_scales8",
                "betterwithmods:steel",
                "steel",
                "invar",
                "coldiron",
                "adamantine",
                "zinc",
                "emerald",
                "platinum",
                "bronze",
                "lead",
                "mkultrax:diamond_dusted_invar",
                "valonite",
                "legend",
                "FORTRESS",
        };

        @Config.Comment("Armor item names to add to the Heavy Armor Class")
        public String[] HEAVY_ITEM_OVERRIDES = {};
    }

    public static class Cheats {
        @Config.Name("Pepsi Blue Mode")
        @Config.Comment("If enabled you won't lose levels on death.")
        public boolean PEPSI_BLUE_MODE = false;

        @Config.Name("Bypass armor restrictions")
        @Config.Comment("If enabled armor restrictions won't take effect.")
        public boolean TOUGH_GUY_MODE = false;

        @Config.Name("Bypass shield restrictions")
        @Config.Comment("If enabled, shield restrictions won't take effect.")
        public boolean BIG_HANDS_MODE = false;
    }

    public static class Display {
        @Config.Name("Show Armor Materials")
        @Config.Comment("Show armor material names in tooltip")
        public boolean SHOW_ARMOR_MAT = false;

        @Config.Name("Show my crits")
        @Config.Comment("Show my crit messages")
        public boolean SHOW_MY_CRITS = true;

        @Config.Name("Show others crits")
        @Config.Comment("Show other's crit messages")
        public boolean SHOW_OTHER_CRITS = true;
    }

    public static class Gameplay {

        @Config.Name("Healing hurts Undead")
        @Config.Comment("Set to true if you want heals to damage undead")
        public boolean HEALS_DAMAGE_UNDEAD = true;

        @Config.Name("Spawn Replacement")
        @Config.Comment("Set to false if you don't want the MK mobs to replace vanilla spawns")
        public boolean SPAWN_REPLACEMENT = true;

        @Config.Name("Undead healing damage multiplier")
        @Config.Comment("Multiplier to scale healing damage to undead by if HEALS_DAMAGE_UNDEAD is true")
        public float HEAL_DAMAGE_MULTIPLIER = 2.0f;

        @Config.Name("XP Share Distance")
        @Config.Comment("Maximum distance away to share xp to other players in your party")
        public float XP_SHARE_DISTANCE = 100.0f;

        @Config.Name("Maximum Entity Health")
        @Config.Comment("Adjusts the default maximum health in minecraft for entities")
        public double MAX_ENTITY_HEALTH = 4096.0;

        @Config.Name("NPC Attack Speed Recipients")
        @Config.Comment("These mobs will have attack speed added to their mob states, for use with the mob definition system.")
        public String[] ATTACK_SPEED_ON_MOBS = {
                "minecraft:skeleton",
                "minecraft:zombie"
        };

        @Config.Name("Disabled Entities")
        @Config.Comment("Prevents the listed entities from spawning, useful for working with buggy mods.")
        public String[] DISABLED_ENTITIES = {

        };

        @Config.Name("Maximum Talent Points Per Class")
        @Config.Comment("Limits the talent points players can spend in a single class (-1 for unlimited, 0 disables talents)")
        public int MAX_TALENT_POINTS_PER_CLASS = -1;
    }

    public static class ClassConfiguration {
        @Config.Name("Classes")
        @Config.Comment("Controls what class providers teach which classes")
        public String[] CLASS_MAP = {
                ClassLists.classesToString(new ResourceLocation(MKUltra.MODID, "provider.sun_icon"),
                        new ResourceLocation(MKUltra.MODID, "class.archer"),
                        new ResourceLocation(MKUltra.MODID, "class.brawler"),
                        new ResourceLocation(MKUltra.MODID, "class.cleric"),
                        new ResourceLocation(MKUltra.MODID, "class.digger"),
                        new ResourceLocation(MKUltra.MODID, "class.druid"),
                        new ResourceLocation(MKUltra.MODID, "class.nether_mage"),
                        new ResourceLocation(MKUltra.MODID, "class.skald"),
                        new ResourceLocation(MKUltra.MODID, "class.wet_wizard")),
                ClassLists.classesToString(new ResourceLocation(MKUltra.MODID, "provider.moon_icon"),
                        new ResourceLocation(MKUltra.MODID, "class.wave_knight"),
                        new ResourceLocation(MKUltra.MODID, "class.moon_knight")),
                ClassLists.classesToString(new ResourceLocation(MKUltra.MODID, "provider.desperate_icon"),
                        new ResourceLocation(MKUltra.MODID, "class.green_knight")),
                ClassLists.classesToString(new ResourceLocation(MKUltra.MODID, "provider.ranger"),
                        new ResourceLocation(MKUltra.MODID, "class.ranger")),
        };

        @Config.Name("Banned Classes")
        @Config.Comment("These classes are not allowed to be used")
        @Config.RequiresMcRestart
        public String[] BANNED_CLASSES = new String[0];
    }

    public static void init(File configFile) {
        Configuration config = new Configuration(configFile);

        try {
            config.load();
        } catch (Exception e) {
            Log.info("Error loading config, returning to default variables.");
        } finally {
            Log.info("Big hands mode is: %b", cheats.BIG_HANDS_MODE);
            Log.info("Pepsi blue mode is: %b", cheats.PEPSI_BLUE_MODE);
            Log.info("Tough guy mode is: %b", cheats.TOUGH_GUY_MODE);
            Log.info("Show Armor Mat is: %b", display.SHOW_ARMOR_MAT);
            if (config.hasChanged())
                config.save();
        }
    }


    public static boolean isClassEnabled(ResourceLocation classId) {
        return Arrays.stream(classConfig.BANNED_CLASSES).noneMatch(s -> s.equalsIgnoreCase(classId.toString()));
    }

    public static ItemArmor.ArmorMaterial findArmorMaterial(String armorMat) {
        if (armorMat == null)
            return null;
        for (ItemArmor.ArmorMaterial material : ItemArmor.ArmorMaterial.values()) {
            String armorName = getArmorName(material);
            if (armorName == null) {
                Log.error("Failed to obtain armor material name for ArmorMaterial %s", material);
                continue;
            }
            if (armorName.equals(armorMat))
                return material;
        }
        return null;
    }

    public static void registerArmorFromName(String name, ArmorClass armorclass) {
        ItemArmor.ArmorMaterial mat = findArmorMaterial(name);
        if (mat != null) {
            Log.info("Registering %s for Armor Class: %s", name,
                    armorclass.getLocation().toString());
            armorclass.register(mat);
        } else {
            Log.debug("Failed to find armor material from config, %s", name);
        }
    }

    public static void registerArmorFromItemName(String name, ArmorClass armorClass) {
        ResourceLocation itemName = new ResourceLocation(name);
        Item item = Item.REGISTRY.getObject(itemName);
        if (item instanceof ItemArmor) {
            Log.info("Registering Item: %s for Armor Class: %s", name,
                    armorClass.getLocation().toString());
            armorClass.registerItem((ItemArmor) item);
        } else {
            Log.debug("Failed to find armor with id or the found item is not an ItemArmor, %s", name);
        }

    }

    private static String getArmorName(ItemArmor.ArmorMaterial mat) {
        return ObfuscationReflectionHelper.getPrivateValue(ItemArmor.ArmorMaterial.class, mat, "field_179243_f"); // name
    }

    public static void registerArmors() {
        Arrays.stream(armor.ROBES_ARMOR).forEach((x) -> registerArmorFromName(x, ArmorClass.ROBES));
        Arrays.stream(armor.ROBES_ITEM_OVERRIDES).forEach((x) -> registerArmorFromItemName(x, ArmorClass.ROBES));
        Arrays.stream(armor.LIGHT_ARMOR).forEach((x) -> registerArmorFromName(x, ArmorClass.LIGHT));
        Arrays.stream(armor.LIGHT_ITEM_OVERRIDES).forEach((x) -> registerArmorFromItemName(x, ArmorClass.LIGHT));
        Arrays.stream(armor.MEDIUM_ARMOR).forEach((x) -> registerArmorFromName(x, ArmorClass.MEDIUM));
        Arrays.stream(armor.MEDIUM_ITEM_OVERRIDES).forEach((x) -> registerArmorFromItemName(x, ArmorClass.MEDIUM));
        Arrays.stream(armor.HEAVY_ARMOR).forEach((x) -> registerArmorFromName(x, ArmorClass.HEAVY));
        Arrays.stream(armor.HEAVY_ITEM_OVERRIDES).forEach((x) -> registerArmorFromItemName(x, ArmorClass.HEAVY));
    }

    public static void registerDisabledMobs(){
        Arrays.stream(gameplay.DISABLED_ENTITIES).forEach((x) -> EntityUtils.addDisabledMob(new ResourceLocation(x)));
    }

    public static void setupAttackSpeedWhitelist() {
        MobUtils.mobsToAddAttackSpeed.clear();
        for (String name : gameplay.ATTACK_SPEED_ON_MOBS) {
            MobUtils.mobsToAddAttackSpeed.add(new ResourceLocation(name));
        }
    }


    public static void setMaxHealthMax() {
        RangedAttribute attr = (RangedAttribute) SharedMonsterAttributes.MAX_HEALTH;
        double max = gameplay.MAX_ENTITY_HEALTH;
        Log.info("Setting max health to %f", max);
        try {
            ObfuscationReflectionHelper.setPrivateValue(RangedAttribute.class, attr, max, "field_111118_b");
        } catch (ReflectionHelper.UnableToAccessFieldException e) {
            e.printStackTrace();
        }
    }
}
