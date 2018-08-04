package com.chaosbuffalo.mkultra;

import com.chaosbuffalo.mkultra.core.BaseClass;
import com.chaosbuffalo.mkultra.core.CorePlugin;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Jacob on 8/4/2018.
 */
public class MKConfig {
    public static Configuration config;
    public static boolean PEPSI_BLUE_MODE = false;
    public static boolean TOUGH_GUY_MODE = false;
    public static boolean BIG_HANDS_MODE = false;
    public static HashMap<ResourceLocation, Boolean> ENABLED_CLASSES = new HashMap<>();

    public static void init(File config_file) {
        config = new Configuration(config_file);

        try{
            config.load();
            // module on/off states \\
            PEPSI_BLUE_MODE = config.getBoolean("PepsiBlueMode","Gameplay",false,
                    "If enabled you won't lose levels on death.");
            TOUGH_GUY_MODE = config.getBoolean("ToughGuyMode", "Gameplay", false,
                    "If enabled armor restrictions won't take effect.");
            BIG_HANDS_MODE = config.getBoolean("BigHandsMode", "Gameplay", false,
                    "If enabled, shield restrictions won't take effect.");
            for (BaseClass baseClass : CorePlugin.BUILTIN_CLASSES){
                boolean classEnablaed = config.getBoolean(baseClass.getClassId().toString() + "_enabled",
                        "Gameplay",
                        true,
                        String.format("Determines whether %s will be present in your game", baseClass.getClassName()));
                ENABLED_CLASSES.put(baseClass.getClassId(), classEnablaed);
            }

        }catch(Exception e){
            System.out.println("Error loading config, returning to default variables.");
        }finally {
            config.save();
        }
    }

}