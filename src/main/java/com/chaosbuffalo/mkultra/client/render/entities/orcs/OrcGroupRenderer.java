package com.chaosbuffalo.mkultra.client.render.entities.orcs;


import com.chaosbuffalo.mknpc.client.render.models.MKBipedModel;
import com.chaosbuffalo.mknpc.client.render.models.styling.ModelStyles;
import com.chaosbuffalo.mknpc.client.render.renderers.BipedGroupRenderer;
import com.chaosbuffalo.mkultra.client.render.styling.MKUOrcs;
import com.chaosbuffalo.mkultra.entities.orcs.OrcEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Map;

public class OrcGroupRenderer extends BipedGroupRenderer<OrcEntity, MKBipedModel<OrcEntity>> {


    public OrcGroupRenderer(EntityRendererProvider.Context context, ResourceLocation entityType) {
        super(context);
        putRenderer(ModelStyles.LONG_HAIR_NAME, new OrcRenderer(context, ModelStyles.LONG_HAIR_STYLE, entityType));
        putRenderer(ModelStyles.LONG_HAIR_ARMORED_NAME, new OrcRenderer(context, ModelStyles.ARMORED_LONG_HAIR_STYLE, entityType));
        putRenderer(ModelStyles.BASIC_NAME, new OrcRenderer(context, ModelStyles.BASIC_STYLE, entityType));
        putLook(MKUOrcs.DEFAULT_NAME, MKUOrcs.ORC_BASE);
        putLook(MKUOrcs.GREEN_LADY_NAME, MKUOrcs.GREEN_LADY_LOOK);
        putLook(MKUOrcs.GREEN_LADY_GUARD_1_NAME, MKUOrcs.GREEN_LADY_GUARD_1_LOOK);
        putLook(MKUOrcs.GREEN_LADY_GUARD_2_NAME, MKUOrcs.GREEN_LADY_GUARD_2_LOOK);
        putLook(MKUOrcs.GREEN_SMITH_NAME, MKUOrcs.GREEN_SMITH_LOOK);
    }

    @Nonnull
    @Override
    public ResourceLocation getBaseTexture(OrcEntity entity) {
        return MKUOrcs.GREEN_ORC;
    }
}