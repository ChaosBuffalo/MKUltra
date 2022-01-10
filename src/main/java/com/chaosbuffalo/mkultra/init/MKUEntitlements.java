package com.chaosbuffalo.mkultra.init;


import com.chaosbuffalo.mkcore.core.entitlements.MKEntitlement;
import com.chaosbuffalo.mkcore.core.entitlements.SimpleEntitlement;
import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;


@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(MKUltra.MODID)
public class MKUEntitlements {

    @ObjectHolder("green_knight.tier_1")
    public static MKEntitlement GreenKnightTier1;

    @ObjectHolder("green_knight.tier_2")
    public static MKEntitlement GreenKnightTier2;

    @ObjectHolder("green_knight.tier_3")
    public static MKEntitlement GreenKnightTier3;


    @SubscribeEvent
    public static void registerEntitlements(RegistryEvent.Register<MKEntitlement> evt) {
        evt.getRegistry().register(new SimpleEntitlement(new ResourceLocation(MKUltra.MODID, "green_knight.tier_1"), 1));
        evt.getRegistry().register(new SimpleEntitlement(new ResourceLocation(MKUltra.MODID, "green_knight.tier_2"), 1));
        evt.getRegistry().register(new SimpleEntitlement(new ResourceLocation(MKUltra.MODID, "green_knight.tier_3"), 1));
    }
}
