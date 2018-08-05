package com.chaosbuffalo.mkultra;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Configuration;

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

}
