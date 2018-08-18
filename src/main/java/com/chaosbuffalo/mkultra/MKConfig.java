package com.chaosbuffalo.mkultra;

import com.chaosbuffalo.mkultra.core.ArmorClass;
import com.chaosbuffalo.mkultra.init.ModItems;
import com.chaosbuffalo.mkultra.log.Log;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;

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

    @Config.Comment("Armor Materials for Robes Armor Class")
    public static String[] ROBES_ARMOR = {
            ModItems.IRON_THREADED_MAT.getName(),
            ModItems.ROBEMAT.getName()
    };

    @Config.Comment("Armor Materials for Light Armor Class")
    public static String[] LIGHT_ARMOR = {
            ItemArmor.ArmorMaterial.LEATHER.getName(),
            ModItems.BONED_LEATHER_MAT.getName()
    };

    @Config.Comment("Armor Materials for Medium Armor Class")
    public static String[] MEDIUM_ARMOR = {
            ItemArmor.ArmorMaterial.CHAIN.getName(),
            ModItems.CHAINMAT.getName()
    };

    @Config.Comment("Armor Materials for Heavy Armor Class")
    public static String[] HEAVY_ARMOR = {
            ItemArmor.ArmorMaterial.IRON.getName()
    };

    public static void init(File configFile) {
        Configuration config = new Configuration(configFile);

        try {
            config.load();
        } catch (Exception e) {
            System.out.println("Error loading config, returning to default variables.");
        } finally {
            if (config.hasChanged())
                config.save();
        }

    }

    public static boolean isClassEnabled(ResourceLocation classId) {
        return Arrays.stream(BANNED_CLASSES).noneMatch(s -> s.equalsIgnoreCase(classId.toString()));
    }

    public static ItemArmor.ArmorMaterial findArmorMat(String armorMat){
        for (ItemArmor.ArmorMaterial material : ItemArmor.ArmorMaterial.values()){
            if (material.getName().equals(armorMat)){
                return material;
            }
        }
        return null;
    }

    public static void registerArmorFromName(String name, ArmorClass armorclass){
        ItemArmor.ArmorMaterial mat = findArmorMat(name);
        if (mat != null) {
            Log.info("Registering %s for Armor Class: %s", name, armorclass.getName());
            armorclass.register(mat);
        } else {
            Log.info("Failed to find armor material from config, %s", name);
        }
    }

    public static void registerArmors(){
        Arrays.stream(ROBES_ARMOR).forEach((x) -> registerArmorFromName(x, ArmorClass.ROBES));
        Arrays.stream(LIGHT_ARMOR).forEach((x) -> registerArmorFromName(x, ArmorClass.LIGHT));
        Arrays.stream(MEDIUM_ARMOR).forEach((x) -> registerArmorFromName(x, ArmorClass.MEDIUM));
        Arrays.stream(HEAVY_ARMOR).forEach((x) -> registerArmorFromName(x, ArmorClass.HEAVY));
    }

}
