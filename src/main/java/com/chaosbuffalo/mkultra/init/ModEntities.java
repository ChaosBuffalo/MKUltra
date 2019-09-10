package com.chaosbuffalo.mkultra.init;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.entities.EntityMKAreaEffect;
import com.chaosbuffalo.mkultra.entities.mobs.EntityOrbMother;
import com.chaosbuffalo.mkultra.entities.mobs.EntityRanger;
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
        register(evt, new ResourceLocation(MKUltra.MODID, "mk_area_effect"),
                EntityMKAreaEffect.class, "EntityMKAreaEffect", 1,
                64, 1, true);
        register(evt, new ResourceLocation(MKUltra.MODID, "drown_projectile"),
                EntityDrownProjectile.class, "EntityDrownProjectile", 2,
                64, 1, true);
        register(evt, new ResourceLocation(MKUltra.MODID, "geyser_projectile"),
                EntityGeyserProjectile.class, "EntityGeyserProjectile", 3,
                64, 1, true);
        register(evt, new ResourceLocation(MKUltra.MODID, "ball_lightning_projectile"),
                EntityBallLightningProjectile.class, "EntityBallLightningProjectile", 4,
                64, 1, true);
        register(evt, new ResourceLocation(MKUltra.MODID, "duality_rune_projectile"),
                EntityDualityRuneProjectile.class, "EntityDualityRuneProjectile", 5,
                64, 1, true);
        register(evt, new ResourceLocation(MKUltra.MODID, "whirlpool_projectile"),
                EntityWhirlpoolProjectile.class, "EntityWhirlpoolProjectile", 6,
                64, 1, true);
        register(evt, new ResourceLocation(MKUltra.MODID, "flame_blade_projectile"),
                EntityFlameBladeProjectile.class, "EntityFlameBladeProjectile", 7,
                64, 1, true);
        register(evt, new ResourceLocation(MKUltra.MODID, "fairy_fire_projectile"),
                EntityFairyFireProjectile.class, "EntityFairyFireProjectile", 8,
                64, 1, true);
        register(evt, new ResourceLocation(MKUltra.MODID, "cleansing_seed_projectile"),
                EntityCleansingSeedProjectile.class, "EntityCleansingSeedProjectile", 9,
                64, 1, true);
        register(evt, new ResourceLocation(MKUltra.MODID, "spirit_bomb_projectile"),
                EntitySpiritBombProjectile.class, "EntitySpiritBombProjectile", 10,
                64, 1, true);
        register(evt, new ResourceLocation(MKUltra.MODID, "mob_fireball_projectile"),
                EntityMobFireballProjectile.class, "EntityMobFireballProjectile", 11,
                64, 1, true);
        register(evt, new ResourceLocation(MKUltra.MODID, "grasping_roots_projectile"),
                EntityGraspingRootsProjectile.class, "EntityGraspingRootsProjectile", 12,
                64, 1, true);
        register(evt, new ResourceLocation(MKUltra.MODID, "ranger"),
                EntityRanger.class, "EntityRanger", 13, 64,
                1, true);
        register(evt, new ResourceLocation(MKUltra.MODID, "orb_mother"),
                EntityOrbMother.class, "EntityOrbMother", 14, 64,
                1, true);
        register(evt, new ResourceLocation(MKUltra.MODID, "meteor_projectile"),
                EntityMeteorProjectile.class, "EntityMeteorProjectile", 15, 64,
                1, true);
    }
}
