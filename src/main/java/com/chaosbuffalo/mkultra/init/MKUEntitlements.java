package com.chaosbuffalo.mkultra.init;


import com.chaosbuffalo.mkcore.MKCoreRegistry;
import com.chaosbuffalo.mkcore.core.entitlements.MKEntitlement;
import com.chaosbuffalo.mkcore.core.entitlements.SimpleEntitlement;
import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


public class MKUEntitlements {

    public static final DeferredRegister<MKEntitlement> REGISTRY = DeferredRegister.create(MKCoreRegistry.ENTITLEMENT_REGISTRY_NAME, MKUltra.MODID);

    public static RegistryObject<MKEntitlement> GreenKnightTier1 = REGISTRY.register("green_knight.tier_1",
            () -> new SimpleEntitlement(1));
    public static RegistryObject<MKEntitlement> GreenKnightTier2 = REGISTRY.register("green_knight.tier_2",
            () -> new SimpleEntitlement(1));
    public static RegistryObject<MKEntitlement> GreenKnightTier3 = REGISTRY.register("green_knight.tier_3",
            () -> new SimpleEntitlement(1));
    public static RegistryObject<MKEntitlement> ClericTier1 = REGISTRY.register("cleric.tier_1",
            () -> new SimpleEntitlement(1));
    public static RegistryObject<MKEntitlement> ClericTier2 = REGISTRY.register("cleric.tier_2",
            () -> new SimpleEntitlement(1));
    public static RegistryObject<MKEntitlement> ClericTier3 = REGISTRY.register("cleric.tier_3",
            () -> new SimpleEntitlement(1));
    public static RegistryObject<MKEntitlement> IntroClericTier1 = REGISTRY.register("cleric.intro.tier_1",
            () -> new SimpleEntitlement(1));
    public static RegistryObject<MKEntitlement> NetherMageTier1 = REGISTRY.register("nether_mage.tier_1",
            () -> new SimpleEntitlement(1));
    public static RegistryObject<MKEntitlement> NetherMageTier2 = REGISTRY.register("nether_mage.tier_2",
            () -> new SimpleEntitlement(1));
    public static RegistryObject<MKEntitlement> NetherMageTier3 = REGISTRY.register("nether_mage.tier_3",
            () -> new SimpleEntitlement(1));
    public static RegistryObject<MKEntitlement> IntroNetherMageTier1 = REGISTRY.register("nether_mage.intro.tier_1",
            () -> new SimpleEntitlement(1));


    public static void register(IEventBus modBus) {
        REGISTRY.register(modBus);
    }
}
