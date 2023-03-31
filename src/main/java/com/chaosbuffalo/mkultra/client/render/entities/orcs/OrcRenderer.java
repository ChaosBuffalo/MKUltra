package com.chaosbuffalo.mkultra.client.render.entities.orcs;

import com.chaosbuffalo.mknpc.client.render.models.MKBipedModel;
import com.chaosbuffalo.mknpc.client.render.models.styling.ModelStyle;
import com.chaosbuffalo.mknpc.client.render.renderers.MKBipedRenderer;
import com.chaosbuffalo.mkultra.client.render.styling.MKUHumans;
import com.chaosbuffalo.mkultra.client.render.styling.MKUOrcs;
import com.chaosbuffalo.mkultra.entities.orcs.OrcEntity;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class OrcRenderer extends MKBipedRenderer<OrcEntity, MKBipedModel<OrcEntity>> {


    public OrcRenderer(EntityRendererProvider.Context context, ModelStyle style,
                         ResourceLocation entityType) {
        super(context, style, MKUOrcs.ORC_BASE, 0.6f, MKBipedModel::new, entityType);
    }

}