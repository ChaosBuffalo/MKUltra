package com.chaosbuffalo.mkultra.client.render.entities.humans;

import com.chaosbuffalo.mknpc.client.render.models.MKBipedModel;
import com.chaosbuffalo.mknpc.client.render.models.MKSkeletalModel;
import com.chaosbuffalo.mknpc.client.render.models.styling.ModelStyle;
import com.chaosbuffalo.mknpc.client.render.renderers.MKBipedRenderer;
import com.chaosbuffalo.mknpc.client.render.renderers.SkeletonStyles;
import com.chaosbuffalo.mkultra.client.render.styling.MKUHumans;
import com.chaosbuffalo.mkultra.entities.humans.HumanEntity;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;


public class HumanRenderer extends MKBipedRenderer<HumanEntity, MKBipedModel<HumanEntity>> {

    public HumanRenderer(EntityRendererProvider.Context context, ModelStyle style,
                         ResourceLocation entityType) {
        super(context, style, MKUHumans.HUMAN_BASE, 0.5f, MKBipedModel::new, entityType);
    }

}