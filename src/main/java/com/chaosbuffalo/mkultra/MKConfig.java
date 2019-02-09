package com.chaosbuffalo.mkultra;

import com.chaosbuffalo.mkultra.core.ArmorClass;
import com.chaosbuffalo.mkultra.log.Log;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.io.File;
import java.util.Arrays;


@Config(modid = MKUltra.MODID, category = "gameplay")
public class MKConfig {

    @Config.Comment("If enabled you won't lose levels on death.")
    public static boolean PEPSI_BLUE_MODE = false;

    @Config.Comment("If enabled armor restrictions won't take effect.")
    public static boolean TOUGH_GUY_MODE = false;

    @Config.Comment("If enabled, shield restrictions won't take effect.")
    public static boolean BIG_HANDS_MODE = false;

    @Config.Comment("These classes are not allowed to be used")
    public static String[] BANNED_CLASSES = new String[0];

    @Config.Comment("Show armor material names in tooltip")
    public static boolean SHOW_ARMOR_MAT = false;


    @Config.Comment("Armor Materials for Robes Armor Class")
    public static String[] ROBES_ARMOR = {
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

    @Config.Comment("Armor Materials for Light Armor Class")
    public static String[] LIGHT_ARMOR = {
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


    @Config.Comment("Armor Materials for Medium Armor Class")
    public static String[] MEDIUM_ARMOR = {
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

    @Config.Comment("Armor Materials for Heavy Armor Class")
    public static String[] HEAVY_ARMOR = {
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

    public static void init(File configFile) {
        Configuration config = new Configuration(configFile);

        try {
            config.load();
        } catch (Exception e) {
            Log.info("Error loading config, returning to default variables.");
        } finally {
            Log.info("Big hands mode is: %b", BIG_HANDS_MODE);
            Log.info("Pepsi blue mode is: %b", PEPSI_BLUE_MODE);
            Log.info("Tough guy mode is: %b", TOUGH_GUY_MODE);
            Log.info("Show Armor Mat is: %b", SHOW_ARMOR_MAT);
            if (config.hasChanged())
                config.save();
        }

    }



    public static boolean isClassEnabled(ResourceLocation classId) {
        return Arrays.stream(BANNED_CLASSES).noneMatch(s -> s.equalsIgnoreCase(classId.toString()));
    }

    public static ItemArmor.ArmorMaterial findArmorMat(String armorMat){
        for (ItemArmor.ArmorMaterial material : ItemArmor.ArmorMaterial.values()){
            if (getArmorName(material).equals(armorMat)){
                return material;
            }
        }
        return null;
    }

    public static void registerArmorFromName(String name, ArmorClass armorclass){
        ItemArmor.ArmorMaterial mat = findArmorMat(name);
        if (mat != null) {
            Log.info("Registering %s for Armor Class: %s", name,
                    armorclass.getLocation().toString());
            armorclass.register(mat);
        } else {
            Log.info("Failed to find armor material from config, %s", name);
        }
    }

    private static String getArmorName(ItemArmor.ArmorMaterial mat) {
        return ReflectionHelper.getPrivateValue(ItemArmor.ArmorMaterial.class, mat, "name", "field_179243_f", "f");
    }

    public static void registerArmors(){
        Arrays.stream(ROBES_ARMOR).forEach((x) -> registerArmorFromName(x, ArmorClass.ROBES));
        Arrays.stream(LIGHT_ARMOR).forEach((x) -> registerArmorFromName(x, ArmorClass.LIGHT));
        Arrays.stream(MEDIUM_ARMOR).forEach((x) -> registerArmorFromName(x, ArmorClass.MEDIUM));
        Arrays.stream(HEAVY_ARMOR).forEach((x) -> registerArmorFromName(x, ArmorClass.HEAVY));
    }

}
