package com.chaosbuffalo.mkultra.event;

import com.chaosbuffalo.mkultra.MKConfig;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.ArmorClass;
import com.chaosbuffalo.mkultra.core.ClassLists;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ConfigHandler {

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(MKUltra.MODID)) {
            ConfigManager.sync(MKUltra.MODID, Config.Type.INSTANCE);
            ArmorClass.clearArmorClasses();
            MKConfig.registerArmors();
            MKConfig.setMaxHealthMax();
            ClassLists.initFromConfig();
            MKConfig.setupAttackSpeedWhitelist();
        }
    }
}
