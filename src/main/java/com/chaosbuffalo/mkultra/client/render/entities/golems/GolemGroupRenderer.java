package com.chaosbuffalo.mkultra.client.render.entities.golems;

import com.chaosbuffalo.mknpc.client.render.models.MKGolemModel;
import com.chaosbuffalo.mknpc.client.render.renderers.BipedGroupRenderer;
import com.chaosbuffalo.mknpc.entity.MKGolemEntity;
import com.chaosbuffalo.mkultra.client.render.styling.MKUGolems;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class GolemGroupRenderer extends BipedGroupRenderer<MKGolemEntity, MKGolemModel<MKGolemEntity>> {

    public GolemGroupRenderer(EntityRendererProvider.Context context, ResourceLocation entityType) {
        super(context);
        putRenderer(MKUGolems.BASIC_GOLEM_NAME, new GolemRenderer(context, MKUGolems.GOLEM_STYLE, entityType));
        putLook(MKUGolems.NECROTIDE_GOLEM_NAME, MKUGolems.NECROTIDE_GOLEM_LOOK);
    }

    @Nonnull
    @Override
    public ResourceLocation getBaseTexture(MKGolemEntity golemEntity) {
        return MKUGolems.NECROTIDE_GOLEM;
    }
}