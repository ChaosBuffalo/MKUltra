package com.chaosbuffalo.mkultra.init;

import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;


@Mod.EventBusSubscriber
@GameRegistry.ObjectHolder(MKUltra.MODID)
public class ModSounds {

    @GameRegistry.ObjectHolder("casting_general")
    public static SoundEvent casting_general;
    @GameRegistry.ObjectHolder("casting_fire")
    public static SoundEvent casting_fire;
    @GameRegistry.ObjectHolder("casting_shadow")
    public static SoundEvent casting_shadow;
    @GameRegistry.ObjectHolder("casting_holy")
    public static SoundEvent casting_holy;
    @GameRegistry.ObjectHolder("casting_water")
    public static SoundEvent casting_water;
    @GameRegistry.ObjectHolder("spell_cast_3")
    public static SoundEvent spell_cast_3;
    @GameRegistry.ObjectHolder("spell_cast_12")
    public static SoundEvent spell_cast_12;
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
    @GameRegistry.ObjectHolder("spell_heal_1")
    public static SoundEvent spell_heal_1;
    @GameRegistry.ObjectHolder("spell_heal_3")
    public static SoundEvent spell_heal_3;
    @GameRegistry.ObjectHolder("spell_heal_7")
    public static SoundEvent spell_heal_7;
    @GameRegistry.ObjectHolder("spell_buff_4")
    public static SoundEvent spell_buff_4;
    @GameRegistry.ObjectHolder("spell_wind_4")
    public static SoundEvent spell_wind_4;
    @GameRegistry.ObjectHolder("spell_wind_5")
    public static SoundEvent spell_wind_5;
    @GameRegistry.ObjectHolder("spell_holy_1")
    public static SoundEvent spell_holy_1;
    @GameRegistry.ObjectHolder("spell_holy_2")
    public static SoundEvent spell_holy_2;
    @GameRegistry.ObjectHolder("spell_holy_4")
    public static SoundEvent spell_holy_4;
    @GameRegistry.ObjectHolder("spell_holy_5")
    public static SoundEvent spell_holy_5;
    @GameRegistry.ObjectHolder("spell_holy_8")
    public static SoundEvent spell_holy_8;
    @GameRegistry.ObjectHolder("spell_buff_5")
    public static SoundEvent spell_buff_5;
    @GameRegistry.ObjectHolder("spell_buff_8")
    public static SoundEvent spell_buff_8;
    @GameRegistry.ObjectHolder("bow_arrow_1")
    public static SoundEvent bow_arrow_1;
    @GameRegistry.ObjectHolder("bow_arrow_2")
    public static SoundEvent bow_arrow_2;
    @GameRegistry.ObjectHolder("bow_arrow_3")
    public static SoundEvent bow_arrow_3;
    @GameRegistry.ObjectHolder("spell_fire_5")
    public static SoundEvent spell_fire_5;
    @GameRegistry.ObjectHolder("spell_negative_effect_2")
    public static SoundEvent spell_negative_effect_2;
    @GameRegistry.ObjectHolder("spell_negative_effect_7")
    public static SoundEvent spell_negative_effect_7;
    @GameRegistry.ObjectHolder("spell_punch_6")
    public static SoundEvent spell_punch_6;
    @GameRegistry.ObjectHolder("spell_shadow_2")
    public static SoundEvent spell_shadow_2;
    @GameRegistry.ObjectHolder("spell_shadow_3")
    public static SoundEvent spell_shadow_3;
    @GameRegistry.ObjectHolder("spell_buff_6")
    public static SoundEvent spell_buff_6;
    @GameRegistry.ObjectHolder("spell_magic_whoosh_2")
    public static SoundEvent spell_magic_whoosh_2;
    @GameRegistry.ObjectHolder("spell_magic_whoosh_3")
    public static SoundEvent spell_magic_whoosh_3;
    @GameRegistry.ObjectHolder("spell_magic_whoosh_4")
    public static SoundEvent spell_magic_whoosh_4;
    @GameRegistry.ObjectHolder("spell_shout_1")
    public static SoundEvent spell_shout_1;
    @GameRegistry.ObjectHolder("spell_whirldwind_1")
    public static SoundEvent spell_whirlwind_1;
    @GameRegistry.ObjectHolder("spell_grab_1")
    public static SoundEvent spell_grab_1;
    @GameRegistry.ObjectHolder("spell_grab_2")
    public static SoundEvent spell_grab_2;
    @GameRegistry.ObjectHolder("spell_buff_attack_4")
    public static SoundEvent spell_buff_attack_4;



    public static void registerSound(RegistryEvent.Register<SoundEvent> evt, SoundEvent event){
        event.setRegistryName(event.getSoundName());
        evt.getRegistry().register(event);
    }

    public static SoundEvent createSound(String name){
        return new SoundEvent(new ResourceLocation(MKUltra.MODID, name));
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> evt){
        registerSound(evt, createSound("casting_general"));
        registerSound(evt, createSound("casting_fire"));
        registerSound(evt, createSound("casting_water"));
        registerSound(evt, createSound("casting_shadow"));
        registerSound(evt, createSound("casting_holy"));
        registerSound(evt, createSound("spell_cast_3"));
        registerSound(evt, createSound("spell_cast_12"));
        registerSound(evt, createSound("spell_fire_3"));
        registerSound(evt, createSound("spell_fire_7"));
        registerSound(evt, createSound("spell_thunder_1"));
        registerSound(evt, createSound("spell_thunder_3"));
        registerSound(evt, createSound("spell_thunder_8"));
        registerSound(evt, createSound("spell_heal_1"));
        registerSound(evt, createSound("spell_heal_3"));
        registerSound(evt, createSound("spell_heal_7"));
        registerSound(evt, createSound("spell_buff_4"));
        registerSound(evt, createSound("spell_buff_6"));
        registerSound(evt, createSound("spell_wind_4"));
        registerSound(evt, createSound("spell_wind_5"));
        registerSound(evt, createSound("spell_holy_1"));
        registerSound(evt, createSound("spell_holy_2"));
        registerSound(evt, createSound("spell_holy_4"));
        registerSound(evt, createSound("spell_holy_5"));
        registerSound(evt, createSound("spell_holy_8"));
        registerSound(evt, createSound("spell_buff_5"));
        registerSound(evt, createSound("spell_buff_8"));
        registerSound(evt, createSound("bow_arrow_1"));
        registerSound(evt, createSound("bow_arrow_2"));
        registerSound(evt, createSound("bow_arrow_3"));
        registerSound(evt, createSound("spell_fire_5"));
        registerSound(evt, createSound("spell_negative_effect_2"));
        registerSound(evt, createSound("spell_negative_effect_7"));
        registerSound(evt, createSound("spell_punch_6"));
        registerSound(evt, createSound("spell_shadow_2"));
        registerSound(evt, createSound("spell_shadow_3"));
        registerSound(evt, createSound("spell_magic_whoosh_2"));
        registerSound(evt, createSound("spell_magic_whoosh_3"));
        registerSound(evt, createSound("spell_magic_whoosh_4"));
        registerSound(evt, createSound("spell_shout_1"));
        registerSound(evt, createSound("spell_whirlwind_1"));
        registerSound(evt, createSound("spell_grab_1"));
        registerSound(evt, createSound("spell_grab_2"));
        registerSound(evt, createSound("spell_buff_attack_4"));
}
}
