package com.chaosbuffalo.mkultra.client.render.entities;

import com.chaosbuffalo.mkultra.entities.EntityMKAreaEffect;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMKAreaEffect implements IRenderFactory<EntityMKAreaEffect>
{
    @Override
    public Render<EntityMKAreaEffect> createRenderFor(RenderManager manager) {
        return new Renderer(manager);
    }

    private static class Renderer extends Render<EntityMKAreaEffect> {
        Renderer(RenderManager renderManager) {
            super(renderManager);
        }

        public void doRender(EntityMKAreaEffect entity, double x, double y, double z, float entityYaw, float partialTicks)
        {
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }

        protected ResourceLocation getEntityTexture(EntityMKAreaEffect entity)
        {
            return null;
        }
    }
}