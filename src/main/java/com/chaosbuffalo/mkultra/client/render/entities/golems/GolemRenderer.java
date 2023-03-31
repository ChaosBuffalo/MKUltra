package com.chaosbuffalo.mkultra.client.render.entities.golems;

import com.chaosbuffalo.mknpc.client.render.models.MKGolemModel;
import com.chaosbuffalo.mknpc.client.render.models.MKSkeletalModel;
import com.chaosbuffalo.mknpc.client.render.models.styling.ModelStyle;
import com.chaosbuffalo.mknpc.client.render.renderers.MKBipedRenderer;
import com.chaosbuffalo.mknpc.client.render.renderers.SkeletonStyles;
import com.chaosbuffalo.mknpc.entity.MKGolemEntity;
import com.chaosbuffalo.mkultra.client.render.styling.MKUGolems;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class GolemRenderer extends MKBipedRenderer<MKGolemEntity, MKGolemModel<MKGolemEntity>> {

    public GolemRenderer(EntityRendererProvider.Context context, ModelStyle style,
                         ResourceLocation entityType) {
        super(context, style, MKUGolems.NECROTIDE_GOLEM_LOOK, 1.25f, MKGolemModel::new, entityType);
    }


}
