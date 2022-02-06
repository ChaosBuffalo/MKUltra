package com.chaosbuffalo.mkultra.client.render.entities.humans;

import com.chaosbuffalo.mknpc.client.render.models.MKBipedModel;
import com.chaosbuffalo.mknpc.client.render.models.styling.ModelStyle;
import com.chaosbuffalo.mknpc.client.render.renderers.MKBipedRenderer;
import com.chaosbuffalo.mkultra.client.render.styling.MKUHumans;
import com.chaosbuffalo.mkultra.entities.humans.HumanEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;


public class HumanRenderer extends MKBipedRenderer<HumanEntity, MKBipedModel<HumanEntity>> {

    public HumanRenderer(EntityRendererManager rendererManager, ModelStyle style) {
        super(rendererManager, new MKBipedModel<>(0.0F, 0.0f, 64, 32),
                (size) -> new MKBipedModel<>(size, 0.0f, 64, 32),
                style, MKUHumans.HUMAN_BASE,0.5f);
    }
}