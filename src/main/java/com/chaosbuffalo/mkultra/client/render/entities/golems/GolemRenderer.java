package com.chaosbuffalo.mkultra.client.render.entities.golems;

import com.chaosbuffalo.mknpc.client.render.models.MKGolemModel;
import com.chaosbuffalo.mknpc.client.render.models.styling.ModelStyle;
import com.chaosbuffalo.mknpc.client.render.renderers.MKBipedRenderer;
import com.chaosbuffalo.mknpc.entity.MKGolemEntity;
import com.chaosbuffalo.mkultra.client.render.styling.MKUGolems;
import net.minecraft.client.renderer.entity.EntityRendererManager;

public class GolemRenderer extends MKBipedRenderer<MKGolemEntity, MKGolemModel<MKGolemEntity>> {

    public GolemRenderer(EntityRendererManager rendererManager, ModelStyle style) {
        super(rendererManager, new MKGolemModel<>(0.0F),
                MKGolemModel::new,
                style, MKUGolems.NECROTIDE_GOLEM_LOOK,0.5f);
    }
}
