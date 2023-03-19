package com.chaosbuffalo.mkultra.client.render.entities.golems;

import com.chaosbuffalo.mknpc.client.render.models.MKGolemModel;
import com.chaosbuffalo.mknpc.client.render.renderers.BipedGroupRenderer;
import com.chaosbuffalo.mknpc.entity.MKGolemEntity;
import com.chaosbuffalo.mkultra.client.render.styling.MKUGolems;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class GolemGroupRenderer extends BipedGroupRenderer<MKGolemEntity, MKGolemModel<MKGolemEntity>> {

    public GolemGroupRenderer(EntityRendererManager rendererManager) {
        super(rendererManager);
        putRenderer(MKUGolems.BASIC_GOLEM_NAME, new GolemRenderer(rendererManager, MKUGolems.GOLEM_STYLE));
        putLook(MKUGolems.NECROTIDE_GOLEM_NAME, MKUGolems.NECROTIDE_GOLEM_LOOK);
    }

    @Nonnull
    @Override
    public ResourceLocation getBaseTexture(MKGolemEntity golemEntity) {
        return MKUGolems.NECROTIDE_GOLEM;
    }
}