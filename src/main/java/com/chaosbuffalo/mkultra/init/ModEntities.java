package com.chaosbuffalo.mkultra.init;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.entities.EntityMKAreaEffect;
import com.chaosbuffalo.mkultra.entities.projectiles.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/**
 * Created by Jacob on 7/15/2016.
 */
public class ModEntities {

    public static void registerEntities() {
        EntityRegistry.registerModEntity(new ResourceLocation(MKUltra.MODID, "EntityMKAreaEffect"),
                EntityMKAreaEffect.class, "EntityMKAreaEffect", 1,
                MKUltra.INSTANCE, 64, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation(MKUltra.MODID, "EntityDrownProjectile"),
                EntityDrownProjectile.class, "EntityDrownProjectile", 2,
                MKUltra.INSTANCE, 64, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation(MKUltra.MODID, "EntityGeyserProjectile"),
                EntityGeyserProjectile.class, "EntityGeyserProjectile", 3,
                MKUltra.INSTANCE, 64, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation(MKUltra.MODID, "EntityBallLightningProjectile"),
                EntityBallLightningProjectile.class, "EntityBallLightningProjectile", 4,
                MKUltra.INSTANCE, 64, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation(MKUltra.MODID, "DualityRuneProjectile"),
                EntityDualityRuneProjectile.class, "EntityDualityRuneProjectile", 5,
                MKUltra.INSTANCE, 64, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation(MKUltra.MODID, "WhirlpoolProjectile"),
                EntityWhirlpoolProjectile.class, "EntityWhirlpoolProjectile", 6,
                MKUltra.INSTANCE, 64, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation(MKUltra.MODID, "FlameBladeProjectile"),
                EntityFlameBladeProjectile.class, "EntityFlameBladeProjectile", 7,
                MKUltra.INSTANCE, 64, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation(MKUltra.MODID, "FairyFireProjectile"),
                EntityFairyFireProjectile.class, "EntityFairyFireProjectile", 8,
                MKUltra.INSTANCE, 64, 1, true);
    }
}
