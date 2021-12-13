package com.chaosbuffalo.mkultra.client.render.entities;

import com.chaosbuffalo.mkcore.entities.BaseProjectileEntity;
import com.chaosbuffalo.mkultra.entities.IMKRenderAsItem;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;

public class SpriteProjectileRenderer<T extends BaseProjectileEntity & IMKRenderAsItem> extends MKSpriteRenderer<T> {

    public SpriteProjectileRenderer(EntityRendererManager renderManagerIn, ItemRenderer itemRendererIn, float scaleIn, boolean doBlockLightIn) {
        super(renderManagerIn, itemRendererIn, scaleIn, doBlockLightIn);
    }

    public SpriteProjectileRenderer(EntityRendererManager renderManagerIn, ItemRenderer itemRendererIn) {
        super(renderManagerIn, itemRendererIn);
    }

    @Override
    public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        entityIn.clientGraphicalUpdate(partialTicks);
    }
}
