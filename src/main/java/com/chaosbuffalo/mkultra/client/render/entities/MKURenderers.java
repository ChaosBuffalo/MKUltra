package com.chaosbuffalo.mkultra.client.render.entities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.entities.projectiles.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;

/**
 * Created by Jacob on 7/15/2016.
 */
@Mod.EventBusSubscriber(modid = MKUltra.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MKURenderers {

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent evt) {
        RenderingRegistry.registerEntityRenderingHandler(CleansingSeedProjectileEntity.TYPE,
            (manager) -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer()));
        RenderingRegistry.registerEntityRenderingHandler(SpiritBombProjectileEntity.TYPE,
            (manager) -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer()));
    }
}
