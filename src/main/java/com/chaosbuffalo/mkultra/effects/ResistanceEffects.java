package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.MKEffect;
import com.chaosbuffalo.mkcore.effects.status.MKResistance;
import com.chaosbuffalo.mkcore.effects.status.MKResistanceNew;
import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ResistanceEffects {

    private static final UUID FIRE_RESISTANCE_UUID = UUID.fromString("e39ad714-1726-417f-a6b2-21a956fdba79");

    private static final UUID BREAK_FIRE_UUID = UUID.fromString("b610e5c3-089d-474a-9240-18074f225f6d");


    public static final MKResistanceNew FIRE_ARMOR = new MKResistanceNew(new ResourceLocation(MKUltra.MODID, "effect.fire_armor"),
            MKAttributes.FIRE_RESISTANCE, FIRE_RESISTANCE_UUID, 0.2f);
    public static final MKResistanceNew BREAK_FIRE = new MKResistanceNew(new ResourceLocation(MKUltra.MODID, "effect.break_fire"),
            MKAttributes.FIRE_RESISTANCE, BREAK_FIRE_UUID, -0.1f);

    @SubscribeEvent
    public static void registerMK(RegistryEvent.Register<MKEffect> event) {
        event.getRegistry().register(FIRE_ARMOR);
        event.getRegistry().register(BREAK_FIRE);
    }
}
