package com.chaosbuffalo.mkultra.client.render.entities.orcs;

import com.chaosbuffalo.mknpc.client.render.models.MKBipedModel;
import com.chaosbuffalo.mknpc.client.render.models.styling.ModelStyle;
import com.chaosbuffalo.mknpc.client.render.renderers.MKBipedRenderer;
import com.chaosbuffalo.mkultra.client.render.styling.MKUOrcs;
import com.chaosbuffalo.mkultra.entities.orcs.OrcEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;

public class OrcRenderer extends MKBipedRenderer<OrcEntity, MKBipedModel<OrcEntity>> {

    public OrcRenderer(EntityRendererManager rendererManager, ModelStyle style) {
        super(rendererManager, new MKBipedModel<>(0.0F, 0.0f, 64, 32),
                (size) -> new MKBipedModel<>(size, 0.0f, 64, 32),
                style, MKUOrcs.ORC_BASE,0.5f);
    }
}