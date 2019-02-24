package com.chaosbuffalo.mkultra.init;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.entities.EntityMKAreaEffect;
import com.chaosbuffalo.mkultra.entities.projectiles.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;


@Mod.EventBusSubscriber
public class ModEntities {

    private static void register(RegistryEvent.Register<EntityEntry> evt, ResourceLocation registryName,
                                 Class<? extends Entity> entityClass, String entityName, int id,
                                 int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
        evt.getRegistry().register(EntityEntryBuilder.create()
                .entity(entityClass)
                .id(registryName, id)
                .name(entityName)
                .tracker(trackingRange, updateFrequency, sendsVelocityUpdates).build());
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> evt) {
        register(evt, new ResourceLocation(MKUltra.MODID, "EntityMKAreaEffect"),
                EntityMKAreaEffect.class, "EntityMKAreaEffect", 1,
                64, 1, true);
        register(evt, new ResourceLocation(MKUltra.MODID, "EntityDrownProjectile"),
                EntityDrownProjectile.class, "EntityDrownProjectile", 2,
                64, 1, true);
        register(evt, new ResourceLocation(MKUltra.MODID, "EntityGeyserProjectile"),
                EntityGeyserProjectile.class, "EntityGeyserProjectile", 3,
                64, 1, true);
        register(evt, new ResourceLocation(MKUltra.MODID, "EntityBallLightningProjectile"),
                EntityBallLightningProjectile.class, "EntityBallLightningProjectile", 4,
                64, 1, true);
        register(evt, new ResourceLocation(MKUltra.MODID, "DualityRuneProjectile"),
                EntityDualityRuneProjectile.class, "EntityDualityRuneProjectile", 5,
                64, 1, true);
        register(evt, new ResourceLocation(MKUltra.MODID, "WhirlpoolProjectile"),
                EntityWhirlpoolProjectile.class, "EntityWhirlpoolProjectile", 6,
                64, 1, true);
        register(evt, new ResourceLocation(MKUltra.MODID, "FlameBladeProjectile"),
                EntityFlameBladeProjectile.class, "EntityFlameBladeProjectile", 7,
                64, 1, true);
        register(evt, new ResourceLocation(MKUltra.MODID, "FairyFireProjectile"),
                EntityFairyFireProjectile.class, "EntityFairyFireProjectile", 8,
                64, 1, true);
        register(evt, new ResourceLocation(MKUltra.MODID, "CleansingSeedProjectile"),
                EntityCleansingSeedProjectile.class, "EntityCleansingSeedProjectile", 9,
                64, 1, true);
        register(evt, new ResourceLocation(MKUltra.MODID, "SpiritBombProjectile"),
                EntitySpiritBombProjectile.class, "EntitySpiritBombProjectile", 10,
                64, 1, true);
        register(evt, new ResourceLocation(MKUltra.MODID, "MobFireballProjectile"),
                EntityMobFireballProjectile.class, "EntityMobFireballProjectile", 11,
                64, 1, true);
    }
}
