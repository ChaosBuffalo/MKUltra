package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.MKConfig;
import com.chaosbuffalo.mkultra.core.classes.*;
import com.google.common.collect.Lists;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

@Mod.EventBusSubscriber
public class CorePlugin {
    private static List<PlayerClass> BUILTIN_CLASSES = Lists.newArrayList();

    static {
        BUILTIN_CLASSES.add(new Archer());
        BUILTIN_CLASSES.add(new Brawler());
        BUILTIN_CLASSES.add(new Digger());
        BUILTIN_CLASSES.add(new Cleric());
        BUILTIN_CLASSES.add(new Skald());
        BUILTIN_CLASSES.add(new NetherMage());
        BUILTIN_CLASSES.add(new WetWizard());
        BUILTIN_CLASSES.add(new Druid());
        BUILTIN_CLASSES.add(new MoonKnight());
        BUILTIN_CLASSES.add(new WaveKnight());
        BUILTIN_CLASSES.add(new Ranger());
        BUILTIN_CLASSES.add(new GreenKnight());
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerClasses(RegistryEvent.Register<PlayerClass> event) {
        BUILTIN_CLASSES.forEach(c -> event.getRegistry().register(c.setRegistryName(c.getClassId())));
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerAbilities(RegistryEvent.Register<PlayerAbility> event) {
        BUILTIN_CLASSES.stream()
                .flatMap(pc -> pc.getAbilities().stream())
                .forEach(a -> event.getRegistry().register(a.setRegistryName(a.getAbilityId())));
    }
}
