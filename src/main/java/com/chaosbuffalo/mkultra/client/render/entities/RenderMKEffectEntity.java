//package com.chaosbuffalo.mkultra.client.render.entities;
//
//import net.minecraft.client.model.ModelBase;
//import net.minecraft.client.renderer.GlStateManager;
//import net.minecraft.client.renderer.entity.Render;
//import net.minecraft.client.renderer.entity.RenderManager;
//import net.minecraft.entity.Entity;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//import javax.annotation.ParametersAreNonnullByDefault;
//
//@SideOnly(Side.CLIENT)
//public abstract class RenderMKEffectEntity<T extends Entity> extends Render<T> {
//    private final ModelBase mainModel;
//
//    public RenderMKEffectEntity(RenderManager manager, ModelBase model) {
//        super(manager);
//        this.mainModel = model;
//    }
//
//    private float getRenderYaw(float prevYaw, float yaw, float dt) {
//        float dYaw = yaw - prevYaw;
//        while (dYaw < -180.0F) {
//            dYaw += 360.0F;
//        }
//
//        while(dYaw >= 180.0F) {
//            dYaw -= 360.0F;
//        }
//
//        return prevYaw + dt * dYaw;
//    }
//
//    @ParametersAreNonnullByDefault
//    public void doRender(T entity, double x, double y, double z, float p_doRender_8_, float dt) {
//        GlStateManager.pushMatrix();
//        GlStateManager.disableCull();
//        float yaw = this.getRenderYaw(entity.prevRotationYaw, entity.rotationYaw, dt);
//        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * dt;
//        GlStateManager.translate((float)x, (float)y, (float)z);
//        GlStateManager.enableRescaleNormal();
//        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
//        GlStateManager.enableAlpha();
//        this.bindEntityTexture(entity);
//        if (this.renderOutlines) {
//            GlStateManager.enableColorMaterial();
//            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
//        }
//
//        this.mainModel.render(entity, 0.0F, 0.0F, 0.0F, yaw, pitch, 0.0625F);
//        if (this.renderOutlines) {
//            GlStateManager.disableOutlineMode();
//            GlStateManager.disableColorMaterial();
//        }
//
//        GlStateManager.popMatrix();
//        super.doRender(entity, x, y, z, p_doRender_8_, dt);
//    }
//
//}
