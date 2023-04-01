package com.chaosbuffalo.mkultra.init;


import com.chaosbuffalo.mkcore.core.entitlements.MKEntitlement;
import com.chaosbuffalo.mkcore.core.entitlements.SimpleEntitlement;
import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ObjectHolder;


@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(MKUltra.MODID)
public class MKUEntitlements {

    public static final DeferredRegister<MKEntitlement> REGISTRY = DeferredRegister.create(MKEntitlement.class, MKUltra.MODID);

    @ObjectHolder("green_knight.tier_1")
    public static MKEntitlement GreenKnightTier1;

    @ObjectHolder("green_knight.tier_2")
    public static MKEntitlement GreenKnightTier2;

    @ObjectHolder("green_knight.tier_3")
    public static MKEntitlement GreenKnightTier3;

    @ObjectHolder("cleric.tier_1")
    public static MKEntitlement ClericTier1;

    @ObjectHolder("cleric.intro.tier_1")
    public static MKEntitlement IntroClericTier1;

    @ObjectHolder("cleric.tier_2")
    public static MKEntitlement ClericTier2;

    @ObjectHolder("cleric.tier_3")
    public static MKEntitlement ClericTier3;

    @ObjectHolder("nether_mage.tier_1")
    public static MKEntitlement NetherMageTier1;

    @ObjectHolder("nether_mage.tier_2")
    public static MKEntitlement NetherMageTier2;

    @ObjectHolder("nether_mage.tier_3")
    public static MKEntitlement NetherMageTier3;

    @ObjectHolder("nether_mage.intro.tier_1")
    public static MKEntitlement IntroNetherMageTier1;


    @SubscribeEvent
    public static void registerEntitlements(RegistryEvent.Register<MKEntitlement> evt) {
        evt.getRegistry().register(new SimpleEntitlement(new ResourceLocation(MKUltra.MODID, "green_knight.tier_1"), 1));
        evt.getRegistry().register(new SimpleEntitlement(new ResourceLocation(MKUltra.MODID, "green_knight.tier_2"), 1));
        evt.getRegistry().register(new SimpleEntitlement(new ResourceLocation(MKUltra.MODID, "green_knight.tier_3"), 1));
        evt.getRegistry().register(new SimpleEntitlement(new ResourceLocation(MKUltra.MODID, "cleric.tier_1"), 1));
        evt.getRegistry().register(new SimpleEntitlement(new ResourceLocation(MKUltra.MODID, "cleric.intro.tier_1"), 1));
        evt.getRegistry().register(new SimpleEntitlement(new ResourceLocation(MKUltra.MODID, "cleric.tier_2"), 1));
        evt.getRegistry().register(new SimpleEntitlement(new ResourceLocation(MKUltra.MODID, "cleric.tier_3"), 1));
        evt.getRegistry().register(new SimpleEntitlement(new ResourceLocation(MKUltra.MODID, "nether_mage.tier_1"), 1));
        evt.getRegistry().register(new SimpleEntitlement(new ResourceLocation(MKUltra.MODID, "nether_mage.intro.tier_1"), 1));
        evt.getRegistry().register(new SimpleEntitlement(new ResourceLocation(MKUltra.MODID, "nether_mage.tier_2"), 1));
        evt.getRegistry().register(new SimpleEntitlement(new ResourceLocation(MKUltra.MODID, "nether_mage.tier_3"), 1));

    }
}
