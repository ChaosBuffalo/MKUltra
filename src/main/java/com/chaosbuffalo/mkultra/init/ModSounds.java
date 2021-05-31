package com.chaosbuffalo.mkultra.init;

import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(MKUltra.MODID)
public class ModSounds {

    @ObjectHolder("casting_general")
    public static SoundEvent casting_general;
    @ObjectHolder("casting_fire")
    public static SoundEvent casting_fire;
    @ObjectHolder("casting_shadow")
    public static SoundEvent casting_shadow;
    @ObjectHolder("casting_holy")
    public static SoundEvent casting_holy;
    @ObjectHolder("casting_water")
    public static SoundEvent casting_water;
    @ObjectHolder("casting_rain")
    public static SoundEvent casting_rain;
    @ObjectHolder("hostile_casting_general")
    public static SoundEvent hostile_casting_general;
    @ObjectHolder("hostile_casting_fire")
    public static SoundEvent hostile_casting_fire;
    @ObjectHolder("hostile_casting_shadow")
    public static SoundEvent hostile_casting_shadow;
    @ObjectHolder("hostile_casting_holy")
    public static SoundEvent hostile_casting_holy;
    @ObjectHolder("hostile_casting_water")
    public static SoundEvent hostile_casting_water;
    @ObjectHolder("spell_cast_2")
    public static SoundEvent spell_cast_2;
    @ObjectHolder("spell_cast_3")
    public static SoundEvent spell_cast_3;
    @ObjectHolder("spell_cast_5")
    public static SoundEvent spell_cast_5;
    @ObjectHolder("spell_cast_6")
    public static SoundEvent spell_cast_6;
    @ObjectHolder("spell_cast_7")
    public static SoundEvent spell_cast_7;
    @ObjectHolder("spell_cast_10")
    public static SoundEvent spell_cast_10;
    @ObjectHolder("spell_cast_11")
    public static SoundEvent spell_cast_11;
    @ObjectHolder("spell_cast_12")
    public static SoundEvent spell_cast_12;
    @ObjectHolder("spell_fire_3")
    public static SoundEvent spell_fire_3;
    @ObjectHolder("spell_fire_7")
    public static SoundEvent spell_fire_7;
    @ObjectHolder("spell_thunder_1")
    public static SoundEvent spell_thunder_1;
    @ObjectHolder("spell_thunder_3")
    public static SoundEvent spell_thunder_3;
    @ObjectHolder("spell_thunder_8")
    public static SoundEvent spell_thunder_8;
    @ObjectHolder("spell_heal_1")
    public static SoundEvent spell_heal_1;
    @ObjectHolder("spell_heal_2")
    public static SoundEvent spell_heal_2;
    @ObjectHolder("spell_heal_3")
    public static SoundEvent spell_heal_3;
    @ObjectHolder("spell_heal_7")
    public static SoundEvent spell_heal_7;
    @ObjectHolder("spell_heal_8")
    public static SoundEvent spell_heal_8;
    @ObjectHolder("spell_heal_9")
    public static SoundEvent spell_heal_9;
    @ObjectHolder("spell_buff_4")
    public static SoundEvent spell_buff_4;
    @ObjectHolder("spell_wind_4")
    public static SoundEvent spell_wind_4;
    @ObjectHolder("spell_wind_5")
    public static SoundEvent spell_wind_5;
    @ObjectHolder("spell_holy_1")
    public static SoundEvent spell_holy_1;
    @ObjectHolder("spell_holy_2")
    public static SoundEvent spell_holy_2;
    @ObjectHolder("spell_holy_3")
    public static SoundEvent spell_holy_3;
    @ObjectHolder("spell_holy_4")
    public static SoundEvent spell_holy_4;
    @ObjectHolder("spell_holy_5")
    public static SoundEvent spell_holy_5;
    @ObjectHolder("spell_holy_8")
    public static SoundEvent spell_holy_8;
    @ObjectHolder("spell_holy_9")
    public static SoundEvent spell_holy_9;
    @ObjectHolder("spell_buff_shield_3")
    public static SoundEvent spell_buff_shield_3;
    @ObjectHolder("spell_buff_shield_4")
    public static SoundEvent spell_buff_shield_4;
    @ObjectHolder("spell_buff_1")
    public static SoundEvent spell_buff_1;
    @ObjectHolder("spell_buff_3")
    public static SoundEvent spell_buff_3;
    @ObjectHolder("spell_buff_5")
    public static SoundEvent spell_buff_5;
    @ObjectHolder("spell_buff_6")
    public static SoundEvent spell_buff_6;
    @ObjectHolder("spell_buff_7")
    public static SoundEvent spell_buff_7;
    @ObjectHolder("spell_buff_8")
    public static SoundEvent spell_buff_8;
    @ObjectHolder("bow_arrow_1")
    public static SoundEvent bow_arrow_1;
    @ObjectHolder("bow_arrow_2")
    public static SoundEvent bow_arrow_2;
    @ObjectHolder("bow_arrow_3")
    public static SoundEvent bow_arrow_3;
    @ObjectHolder("spell_fire_1")
    public static SoundEvent spell_fire_1;
    @ObjectHolder("spell_fire_2")
    public static SoundEvent spell_fire_2;
    @ObjectHolder("spell_fire_4")
    public static SoundEvent spell_fire_4;
    @ObjectHolder("spell_fire_5")
    public static SoundEvent spell_fire_5;
    @ObjectHolder("spell_fire_6")
    public static SoundEvent spell_fire_6;
    @ObjectHolder("spell_fire_8")
    public static SoundEvent spell_fire_8;
    @ObjectHolder("spell_negative_effect_1")
    public static SoundEvent spell_negative_effect_1;
    @ObjectHolder("spell_negative_effect_2")
    public static SoundEvent spell_negative_effect_2;
    @ObjectHolder("spell_negative_effect_7")
    public static SoundEvent spell_negative_effect_7;
    @ObjectHolder("spell_punch_6")
    public static SoundEvent spell_punch_6;
    @ObjectHolder("spell_shadow_2")
    public static SoundEvent spell_shadow_2;
    @ObjectHolder("spell_shadow_3")
    public static SoundEvent spell_shadow_3;
    @ObjectHolder("spell_shadow_5")
    public static SoundEvent spell_shadow_5;
    @ObjectHolder("spell_shadow_6")
    public static SoundEvent spell_shadow_6;
    @ObjectHolder("spell_shadow_8")
    public static SoundEvent spell_shadow_8;
    @ObjectHolder("spell_shadow_9")
    public static SoundEvent spell_shadow_9;
    @ObjectHolder("spell_shadow_10")
    public static SoundEvent spell_shadow_10;
    @ObjectHolder("spell_shadow_11")
    public static SoundEvent spell_shadow_11;
    @ObjectHolder("spell_magic_whoosh_1")
    public static SoundEvent spell_magic_whoosh_1;
    @ObjectHolder("spell_magic_whoosh_2")
    public static SoundEvent spell_magic_whoosh_2;
    @ObjectHolder("spell_magic_whoosh_3")
    public static SoundEvent spell_magic_whoosh_3;
    @ObjectHolder("spell_magic_whoosh_4")
    public static SoundEvent spell_magic_whoosh_4;
    @ObjectHolder("spell_shout_1")
    public static SoundEvent spell_shout_1;
    @ObjectHolder("spell_whirlwind_1")
    public static SoundEvent spell_whirlwind_1;
    @ObjectHolder("spell_grab_1")
    public static SoundEvent spell_grab_1;
    @ObjectHolder("spell_grab_2")
    public static SoundEvent spell_grab_2;
    @ObjectHolder("spell_buff_attack_2")
    public static SoundEvent spell_buff_attack_2;
    @ObjectHolder("spell_buff_attack_3")
    public static SoundEvent spell_buff_attack_3;
    @ObjectHolder("spell_buff_attack_4")
    public static SoundEvent spell_buff_attack_4;
    @ObjectHolder("spell_earth_1")
    public static SoundEvent spell_earth_1;
    @ObjectHolder("spell_earth_6")
    public static SoundEvent spell_earth_6;
    @ObjectHolder("spell_earth_7")
    public static SoundEvent spell_earth_7;
    @ObjectHolder("spell_earth_8")
    public static SoundEvent spell_earth_8;
    @ObjectHolder("spell_magic_explosion")
    public static SoundEvent spell_magic_explosion;
    @ObjectHolder("spell_water_1")
    public static SoundEvent spell_water_1;
    @ObjectHolder("spell_water_2")
    public static SoundEvent spell_water_2;
    @ObjectHolder("spell_water_4")
    public static SoundEvent spell_water_4;
    @ObjectHolder("spell_water_5")
    public static SoundEvent spell_water_5;
    @ObjectHolder("spell_water_6")
    public static SoundEvent spell_water_6;
    @ObjectHolder("spell_water_7")
    public static SoundEvent spell_water_7;
    @ObjectHolder("spell_water_8")
    public static SoundEvent spell_water_8;
    @ObjectHolder("spell_water_9")
    public static SoundEvent spell_water_9;
    @ObjectHolder("spell_dark_1")
    public static SoundEvent spell_dark_1;
    @ObjectHolder("spell_dark_3")
    public static SoundEvent spell_dark_3;
    @ObjectHolder("spell_dark_4")
    public static SoundEvent spell_dark_4;
    @ObjectHolder("spell_dark_5")
    public static SoundEvent spell_dark_5;
    @ObjectHolder("spell_dark_7")
    public static SoundEvent spell_dark_7;
    @ObjectHolder("spell_dark_8")
    public static SoundEvent spell_dark_8;
    @ObjectHolder("spell_dark_9")
    public static SoundEvent spell_dark_9;
    @ObjectHolder("spell_dark_11")
    public static SoundEvent spell_dark_11;
    @ObjectHolder("spell_dark_13")
    public static SoundEvent spell_dark_13;
    @ObjectHolder("spell_dark_14")
    public static SoundEvent spell_dark_14;
    @ObjectHolder("spell_dark_15")
    public static SoundEvent spell_dark_15;
    @ObjectHolder("spell_dark_16")
    public static SoundEvent spell_dark_16;
    @ObjectHolder("spell_debuff_1")
    public static SoundEvent spell_debuff_1;
    @ObjectHolder("spell_debuff_2")
    public static SoundEvent spell_debuff_2;
    @ObjectHolder("spell_debuff_4")
    public static SoundEvent spell_debuff_4;
    @ObjectHolder("spell_scream_1")
    public static SoundEvent spell_scream_1;

    public static void registerSound(RegistryEvent.Register<SoundEvent> evt, SoundEvent event) {
        evt.getRegistry().register(event);
    }

    public static SoundEvent createSound(String name) {
        ResourceLocation r_name = new ResourceLocation(MKUltra.MODID, name);
        SoundEvent event = new SoundEvent(r_name);
        event.setRegistryName(r_name);
        return event;
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> evt) {
        registerSound(evt, createSound("casting_general"));
        registerSound(evt, createSound("casting_fire"));
        registerSound(evt, createSound("casting_water"));
        registerSound(evt, createSound("casting_shadow"));
        registerSound(evt, createSound("casting_holy"));
        registerSound(evt, createSound("casting_rain"));
        registerSound(evt, createSound("hostile_casting_general"));
        registerSound(evt, createSound("hostile_casting_fire"));
        registerSound(evt, createSound("hostile_casting_water"));
        registerSound(evt, createSound("hostile_casting_shadow"));
        registerSound(evt, createSound("hostile_casting_holy"));
        registerSound(evt, createSound("spell_cast_2"));
        registerSound(evt, createSound("spell_cast_3"));
        registerSound(evt, createSound("spell_cast_5"));
        registerSound(evt, createSound("spell_cast_6"));
        registerSound(evt, createSound("spell_cast_7"));
        registerSound(evt, createSound("spell_cast_10"));
        registerSound(evt, createSound("spell_cast_11"));
        registerSound(evt, createSound("spell_cast_12"));
        registerSound(evt, createSound("spell_fire_1"));
        registerSound(evt, createSound("spell_fire_3"));
        registerSound(evt, createSound("spell_fire_4"));
        registerSound(evt, createSound("spell_fire_6"));
        registerSound(evt, createSound("spell_fire_7"));
        registerSound(evt, createSound("spell_fire_8"));
        registerSound(evt, createSound("spell_thunder_1"));
        registerSound(evt, createSound("spell_thunder_3"));
        registerSound(evt, createSound("spell_thunder_8"));
        registerSound(evt, createSound("spell_heal_1"));
        registerSound(evt, createSound("spell_heal_2"));
        registerSound(evt, createSound("spell_heal_3"));
        registerSound(evt, createSound("spell_heal_7"));
        registerSound(evt, createSound("spell_heal_8"));
        registerSound(evt, createSound("spell_heal_9"));
        registerSound(evt, createSound("spell_buff_4"));
        registerSound(evt, createSound("spell_buff_6"));
        registerSound(evt, createSound("spell_buff_7"));
        registerSound(evt, createSound("spell_wind_4"));
        registerSound(evt, createSound("spell_wind_5"));
        registerSound(evt, createSound("spell_holy_1"));
        registerSound(evt, createSound("spell_holy_2"));
        registerSound(evt, createSound("spell_holy_4"));
        registerSound(evt, createSound("spell_holy_5"));
        registerSound(evt, createSound("spell_holy_8"));
        registerSound(evt, createSound("spell_holy_9"));
        registerSound(evt, createSound("spell_buff_shield_3"));
        registerSound(evt, createSound("spell_buff_shield_4"));
        registerSound(evt, createSound("spell_buff_1"));
        registerSound(evt, createSound("spell_buff_3"));
        registerSound(evt, createSound("spell_buff_5"));
        registerSound(evt, createSound("spell_buff_8"));
        registerSound(evt, createSound("bow_arrow_1"));
        registerSound(evt, createSound("bow_arrow_2"));
        registerSound(evt, createSound("bow_arrow_3"));
        registerSound(evt, createSound("spell_fire_2"));
        registerSound(evt, createSound("spell_fire_5"));
        registerSound(evt, createSound("spell_negative_effect_1"));
        registerSound(evt, createSound("spell_negative_effect_2"));
        registerSound(evt, createSound("spell_negative_effect_7"));
        registerSound(evt, createSound("spell_punch_6"));
        registerSound(evt, createSound("spell_shadow_2"));
        registerSound(evt, createSound("spell_shadow_3"));
        registerSound(evt, createSound("spell_shadow_5"));
        registerSound(evt, createSound("spell_shadow_6"));
        registerSound(evt, createSound("spell_shadow_8"));
        registerSound(evt, createSound("spell_shadow_9"));
        registerSound(evt, createSound("spell_shadow_10"));
        registerSound(evt, createSound("spell_shadow_11"));
        registerSound(evt, createSound("spell_magic_whoosh_1"));
        registerSound(evt, createSound("spell_magic_whoosh_2"));
        registerSound(evt, createSound("spell_magic_whoosh_3"));
        registerSound(evt, createSound("spell_magic_whoosh_4"));
        registerSound(evt, createSound("spell_shout_1"));
        registerSound(evt, createSound("spell_whirlwind_1"));
        registerSound(evt, createSound("spell_grab_1"));
        registerSound(evt, createSound("spell_grab_2"));
        registerSound(evt, createSound("spell_buff_attack_2"));
        registerSound(evt, createSound("spell_buff_attack_3"));
        registerSound(evt, createSound("spell_buff_attack_4"));
        registerSound(evt, createSound("spell_earth_1"));
        registerSound(evt, createSound("spell_earth_6"));
        registerSound(evt, createSound("spell_earth_7"));
        registerSound(evt, createSound("spell_earth_8"));
        registerSound(evt, createSound("spell_magic_explosion"));
        registerSound(evt, createSound("spell_water_1"));
        registerSound(evt, createSound("spell_water_2"));
        registerSound(evt, createSound("spell_water_4"));
        registerSound(evt, createSound("spell_water_5"));
        registerSound(evt, createSound("spell_water_6"));
        registerSound(evt, createSound("spell_water_7"));
        registerSound(evt, createSound("spell_water_8"));
        registerSound(evt, createSound("spell_water_9"));
        registerSound(evt, createSound("spell_dark_1"));
        registerSound(evt, createSound("spell_dark_3"));
        registerSound(evt, createSound("spell_dark_4"));
        registerSound(evt, createSound("spell_dark_5"));
        registerSound(evt, createSound("spell_dark_7"));
        registerSound(evt, createSound("spell_dark_8"));
        registerSound(evt, createSound("spell_dark_9"));
        registerSound(evt, createSound("spell_dark_11"));
        registerSound(evt, createSound("spell_dark_13"));
        registerSound(evt, createSound("spell_dark_14"));
        registerSound(evt, createSound("spell_dark_15"));
        registerSound(evt, createSound("spell_dark_16"));
        registerSound(evt, createSound("spell_debuff_1"));
        registerSound(evt, createSound("spell_debuff_2"));
        registerSound(evt, createSound("spell_debuff_4"));
        registerSound(evt, createSound("spell_scream_1"));
    }
}
