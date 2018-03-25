package com.chaosbuffalo.mkultra.client.render.entities;

import com.chaosbuffalo.mkultra.entities.EntityMKAreaEffect;
import com.chaosbuffalo.mkultra.entities.projectiles.EntityBallLightningProjectile;
import com.chaosbuffalo.mkultra.entities.projectiles.EntityDrownProjectile;
import com.chaosbuffalo.mkultra.entities.projectiles.EntityDualityRuneProjectile;
import com.chaosbuffalo.mkultra.entities.projectiles.EntityGeyserProjectile;
import com.chaosbuffalo.mkultra.init.ModItems;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

/**
 * Created by Jacob on 7/15/2016.
 */
public class EntityRenderRegister {

    public static void registerEntityRenderers(){
        RenderingRegistry.registerEntityRenderingHandler(EntityMKAreaEffect.class, new RenderMKAreaEffect());
        RenderingRegistry.registerEntityRenderingHandler(EntityDrownProjectile.class,
                new RenderProjectile(ModItems.drownProjectile, 1.0f));
        RenderingRegistry.registerEntityRenderingHandler(EntityGeyserProjectile.class,
                new RenderProjectile(ModItems.geyserProjectile, 1.0f));
        RenderingRegistry.registerEntityRenderingHandler(EntityBallLightningProjectile.class,
                new RenderProjectile(ModItems.ballLightning, 1.0f));
        RenderingRegistry.registerEntityRenderingHandler(EntityDualityRuneProjectile.class,
                new RenderProjectile(ModItems.duality_rune_projectile, 1.0f));
    }
}
