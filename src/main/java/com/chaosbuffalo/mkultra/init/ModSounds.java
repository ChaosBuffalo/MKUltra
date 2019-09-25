package com.chaosbuffalo.mkultra.init;

import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.HashMap;


@Mod.EventBusSubscriber
@GameRegistry.ObjectHolder(MKUltra.MODID)
public class ModSounds {

    @GameRegistry.ObjectHolder("casting_general")
    public static SoundEvent casting_general;
    @GameRegistry.ObjectHolder("casting_fire_loop_2")
    public static SoundEvent casting_fire_loop_2;
    @GameRegistry.ObjectHolder("spell_cast_3")
    public static SoundEvent spell_cast_3;
    @GameRegistry.ObjectHolder("spell_fire_3")
    public static SoundEvent spell_fire_3;
    @GameRegistry.ObjectHolder("spell_fire_7")
    public static SoundEvent spell_fire_7;
    @GameRegistry.ObjectHolder("spell_thunder_1")
    public static SoundEvent spell_thunder_1;
    @GameRegistry.ObjectHolder("spell_thunder_3")
    public static SoundEvent spell_thunder_3;
    @GameRegistry.ObjectHolder("spell_thunder_8")
    public static SoundEvent spell_thunder_8;

    private static HashMap<ResourceLocation, SoundEvent> locToSoundMap = new HashMap<>();

    public static void addToSoundMap(SoundEvent sound){
        locToSoundMap.put(sound.getRegistryName(), sound);
    }

    @Nullable
    public static SoundEvent getFromSoundMap(ResourceLocation loc){
        return locToSoundMap.get(loc);
    }

    public static void registerSound(RegistryEvent.Register<SoundEvent> evt, SoundEvent event){
        event.setRegistryName(event.getSoundName());
        addToSoundMap(event);
        evt.getRegistry().register(event);
    }

    public static SoundEvent createSound(String name){
        return new SoundEvent(new ResourceLocation(MKUltra.MODID, name));
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> evt){
        registerSound(evt, createSound("casting_general"));
        registerSound(evt, createSound("casting_fire_loop_2"));
        registerSound(evt, createSound("spell_cast_3"));
        registerSound(evt, createSound("spell_fire_3"));
        registerSound(evt, createSound("spell_fire_7"));
        registerSound(evt, createSound("spell_thunder_1"));
        registerSound(evt, createSound("spell_thunder_3"));
        registerSound(evt, createSound("spell_thunder_8"));
    }
}
