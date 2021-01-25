//package com.chaosbuffalo.mkultra.client.render.entities;
//
//import com.chaosbuffalo.mkultra.entities.projectiles.EntityBaseProjectile;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.GlStateManager;
//import net.minecraft.client.renderer.RenderHelper;
//import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
//import net.minecraft.client.renderer.entity.Render;
//import net.minecraft.client.renderer.entity.RenderManager;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.fml.client.registry.IRenderFactory;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
///**
// * Created by Jacob on 7/15/2016.
// */
//@SideOnly(Side.CLIENT)
//public class RenderProjectile implements IRenderFactory<EntityBaseProjectile> {
//    public Item itemModel;
//    public float scale;
//
//    RenderProjectile(Item itemModel, float scale) {
//        this.itemModel = itemModel;
//        this.scale = scale;
//    }
//
//    @Override
//    public Render<EntityBaseProjectile> createRenderFor(RenderManager manager) {
//        return new Renderer(manager, this.itemModel, this.scale);
//    }
//
//    private static class Renderer extends Render<EntityBaseProjectile> {
//        public Item itemModel;
//        public float scale;
//
//        Renderer(RenderManager renderManager, Item itemModel, float scale) {
//            super(renderManager);
//            this.shadowSize = 0.15f;
//            this.shadowOpaque = 0.75f;
//            this.itemModel = itemModel;
//            this.scale = scale;
//        }
//
//        public void doRender(EntityBaseProjectile entity, double x, double y, double z, float entityYaw, float partialTicks) {
//            boolean flag = false;
//            if (this.bindEntityTexture(entity)) {
//                this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity)).setBlurMipmap(true, true);
//                flag = true;
//            }
//            GlStateManager.enableRescaleNormal();
//            GlStateManager.alphaFunc(516, 0.1F);
//            GlStateManager.enableBlend();
//            RenderHelper.enableStandardItemLighting();
//            GlStateManager.tryBlendFuncSeparate(
//                    GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
//                    GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
//            );
//            Minecraft.getMinecraft().getRenderItem()
//                    .renderItem(new ItemStack(this.itemModel), ItemCameraTransforms.TransformType.GROUND);
//            if (this.renderOutlines) {
//                GlStateManager.enableColorMaterial();
//                GlStateManager.enableOutlineMode(this.getTeamColor(entity));
//            }
//            GlStateManager.pushMatrix();
//            GlStateManager.translate((float) x, (float) y + .33f, (float) z);
//            GlStateManager.scale(this.scale, this.scale, this.scale);
//            GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
//            GlStateManager.rotate((float) (this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
//            Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(this.itemModel), ItemCameraTransforms.TransformType.GROUND);
//            GlStateManager.popMatrix();
//            if (this.renderOutlines) {
//                GlStateManager.disableOutlineMode();
//                GlStateManager.disableColorMaterial();
//            }
//            GlStateManager.disableRescaleNormal();
//            GlStateManager.disableBlend();
//            this.bindEntityTexture(entity);
//
//            if (flag) {
//                this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity)).restoreLastBlurMipmap();
//            }
//            super.doRender(entity, x, y, z, entityYaw, partialTicks);
//        }
//
//
//        protected ResourceLocation getEntityTexture(EntityBaseProjectile entity) {
//            return null;
//        }
//    }
//}
//
