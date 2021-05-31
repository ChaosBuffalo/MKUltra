//package com.chaosbuffalo.mkultra.client.render.entities;
//
//
//import com.chaosbuffalo.mkultra.MKUltra;
//import com.chaosbuffalo.mkultra.client.model.ModelHumanoidBase;
//import com.chaosbuffalo.mkultra.client.model.ModelMeteor;
//import com.chaosbuffalo.mkultra.entities.mobs.EntityRanger;
//import com.chaosbuffalo.mkultra.entities.projectiles.EntityMeteorProjectile;
//import net.minecraft.client.renderer.GlStateManager;
//import net.minecraft.client.renderer.entity.Render;
//import net.minecraft.client.renderer.entity.RenderManager;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.fml.client.registry.IRenderFactory;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//public class RenderMeteor implements IRenderFactory<EntityMeteorProjectile> {
//
//    RenderMeteor() {
//    }
//
//    @Override
//    public Render<EntityMeteorProjectile> createRenderFor(RenderManager renderManager) {
//        return new Renderer(renderManager);
//    }
//
//    private static class Renderer extends RenderMKEffectEntity<EntityMeteorProjectile> {
//        private static final ResourceLocation TEXTURE = new ResourceLocation(MKUltra.MODID, "textures/entity/spell/meteor.png");
//
//        public Renderer(RenderManager renderManager) {
//            super(renderManager, new ModelMeteor());
//        }
//
//        @Override
//        protected ResourceLocation getEntityTexture(EntityMeteorProjectile entity) {
//            return TEXTURE;
//        }
//    }
//}
