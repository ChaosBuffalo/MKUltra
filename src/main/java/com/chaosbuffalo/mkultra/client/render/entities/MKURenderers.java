package com.chaosbuffalo.mkultra.client.render.entities;

import com.chaosbuffalo.mknpc.client.render.renderers.SkeletalGroupRenderer;
import com.chaosbuffalo.mknpc.client.render.renderers.ZombifiedPiglinGroupRenderer;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.client.render.entities.humans.HumanGroupRenderer;
import com.chaosbuffalo.mkultra.client.render.entities.orcs.OrcGroupRenderer;
import com.chaosbuffalo.mkultra.client.render.styling.MKUPiglins;
import com.chaosbuffalo.mkultra.client.render.styling.MKUSkeletons;
import com.chaosbuffalo.mkultra.entities.projectiles.*;
import com.chaosbuffalo.mkultra.init.MKUEntities;
import net.minecraft.client.Minecraft;
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
            (manager) -> new SpriteProjectileRenderer<>(manager, Minecraft.getInstance().getItemRenderer(), 1.0f, true));
        RenderingRegistry.registerEntityRenderingHandler(SpiritBombProjectileEntity.TYPE,
            (manager) -> new SpriteProjectileRenderer<>(manager, Minecraft.getInstance().getItemRenderer(), 1.0f, true));
        RenderingRegistry.registerEntityRenderingHandler(FireballProjectileEntity.TYPE,
                (manager) -> new SpriteProjectileRenderer<>(manager, Minecraft.getInstance().getItemRenderer(), 1.0f, true));
        RenderingRegistry.registerEntityRenderingHandler(ShadowBoltProjectileEntity.TYPE,
                (manager) -> new SpriteProjectileRenderer<>(manager, Minecraft.getInstance().getItemRenderer(), 1.0f, true));
        RenderingRegistry.registerEntityRenderingHandler(MKUEntities.ORC_TYPE, OrcGroupRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(MKUEntities.HYBOREAN_SKELETON_TYPE, (manager) ->
                new SkeletalGroupRenderer(manager, MKUSkeletons.SKELETON_STYLES));
        RenderingRegistry.registerEntityRenderingHandler(MKUEntities.ZOMBIFIED_PIGLIN_TYPE, (renderManager)->
                new ZombifiedPiglinGroupRenderer(renderManager, MKUPiglins.ZOMBIE_PIGLIN_STYLES));
        RenderingRegistry.registerEntityRenderingHandler(MKUEntities.HUMAN_TYPE, HumanGroupRenderer::new);
    }
}
