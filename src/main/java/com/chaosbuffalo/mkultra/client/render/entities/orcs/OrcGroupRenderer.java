package com.chaosbuffalo.mkultra.client.render.entities.orcs;


import com.chaosbuffalo.mknpc.client.render.models.MKBipedModel;
import com.chaosbuffalo.mknpc.client.render.models.styling.ModelStyles;
import com.chaosbuffalo.mknpc.client.render.renderers.BipedGroupRenderer;
import com.chaosbuffalo.mkultra.client.render.styling.MKUOrcs;
import com.chaosbuffalo.mkultra.entities.orcs.OrcEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class OrcGroupRenderer extends BipedGroupRenderer<OrcEntity, MKBipedModel<OrcEntity>> {




    public OrcGroupRenderer(EntityRendererManager rendererManager) {
        super(rendererManager);
        putRenderer(ModelStyles.LONG_HAIR_NAME, new OrcRenderer(rendererManager, ModelStyles.LONG_HAIR_STYLE));
        putRenderer(ModelStyles.LONG_HAIR_ARMORED_NAME,
                new OrcRenderer(rendererManager, ModelStyles.ARMORED_LONG_HAIR_STYLE));
        putRenderer(ModelStyles.BASIC_NAME, new OrcRenderer(rendererManager, ModelStyles.BASIC_STYLE));
        putLook(MKUOrcs.DEFAULT_NAME, MKUOrcs.ORC_BASE);
        putLook(MKUOrcs.GREEN_LADY_NAME, MKUOrcs.GREEN_LADY_LOOK);
        putLook(MKUOrcs.GREEN_LADY_GUARD_1_NAME, MKUOrcs.GREEN_LADY_GUARD_1_LOOK);
        putLook(MKUOrcs.GREEN_LADY_GUARD_2_NAME, MKUOrcs.GREEN_LADY_GUARD_2_LOOK);
    }

    @Nonnull
    @Override
    public ResourceLocation getBaseTexture(OrcEntity entity) {
        return MKUOrcs.GREEN_ORC;
    }
}