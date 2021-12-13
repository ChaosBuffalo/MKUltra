package com.chaosbuffalo.mkultra.client.render.entities;

import com.chaosbuffalo.mkultra.entities.IMKRenderAsItem;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;

public class MKSpriteRenderer <T extends Entity & IMKRenderAsItem> extends EntityRenderer<T> {
    private final net.minecraft.client.renderer.ItemRenderer itemRenderer;
    private final float scale;
    private final boolean doBlockLight;

    public MKSpriteRenderer(EntityRendererManager renderManagerIn, net.minecraft.client.renderer.ItemRenderer itemRendererIn,
                            float scaleIn, boolean doBlockLightIn) {
        super(renderManagerIn);
        this.itemRenderer = itemRendererIn;
        this.scale = scaleIn;
        this.doBlockLight = doBlockLightIn;
    }

    public MKSpriteRenderer(EntityRendererManager renderManagerIn, net.minecraft.client.renderer.ItemRenderer itemRendererIn) {
        this(renderManagerIn, itemRendererIn, 1.0F, false);
    }

    protected int getBlockLight(T entityIn, BlockPos pos) {
        return this.doBlockLight ? 15 : super.getBlockLight(entityIn, pos);
    }

    public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {

        matrixStackIn.push();
        matrixStackIn.scale(this.scale, this.scale, this.scale);
        matrixStackIn.rotate(this.renderManager.getCameraOrientation());
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
        this.itemRenderer.renderItem(entityIn.getItem(), ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getEntityTexture(Entity entity) {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }
}