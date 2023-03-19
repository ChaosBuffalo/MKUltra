package com.chaosbuffalo.mkultra.client.render.entities.humans;

import com.chaosbuffalo.mknpc.client.render.models.MKBipedModel;
import com.chaosbuffalo.mknpc.client.render.models.styling.ModelStyles;
import com.chaosbuffalo.mknpc.client.render.renderers.BipedGroupRenderer;
import com.chaosbuffalo.mkultra.client.render.styling.MKUHumans;
import com.chaosbuffalo.mkultra.entities.humans.HumanEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class HumanGroupRenderer extends BipedGroupRenderer<HumanEntity, MKBipedModel<HumanEntity>> {

    public HumanGroupRenderer(EntityRendererManager rendererManager) {
        super(rendererManager);
        putRenderer(ModelStyles.BASIC_NAME, new HumanRenderer(rendererManager, ModelStyles.BASIC_STYLE));
        putRenderer(MKUHumans.TWO_LAYER_CLOTHES_SHORT_HAIR_NAME,
                new HumanRenderer(rendererManager, MKUHumans.TWO_LAYER_CLOTHES_SHORT_HAIR));
        putRenderer(ModelStyles.SHORT_HAIR_NAME, new HumanRenderer(rendererManager, ModelStyles.SHORT_HAIR_STYLE));
        putRenderer(MKUHumans.GHOST_LONG_HAIR_NAME, new HumanRenderer(rendererManager, MKUHumans.GHOST_LONG_HAIR_STYLE));
        putRenderer(MKUHumans.GHOST_LONG_HAIR_ARMORED_NAME, new HumanRenderer(rendererManager, MKUHumans.ARMORED_GHOST_LONG_HAIR_STYLE));
        putRenderer(MKUHumans.TWO_LAYER_ARMOR_SHORT_HAIR_NAME, new HumanRenderer(rendererManager, MKUHumans.TWO_LAYER_ARMOR_SHORT_HAIR));
        putRenderer(MKUHumans.TWO_LAYER_ARMOR_NO_HAIR_NAME, new HumanRenderer(rendererManager, MKUHumans.TWO_LAYER_ARMOR_NO_HAIR));
        putLook(MKUHumans.DEFAULT_NAME, MKUHumans.HUMAN_BASE);
        putLook(MKUHumans.CLERIC_1_NAME, MKUHumans.CLERIC_1_LOOK);
        putLook(MKUHumans.CLERIC_2_NAME, MKUHumans.CLERIC_2_LOOK);
        putLook(MKUHumans.GHOST_1_NAME, MKUHumans.GHOST_LOOK_1);
        putLook(MKUHumans.NETHER_MAGE_1_NAME, MKUHumans.NETHER_MAGE_1_LOOK);
        putLook(MKUHumans.TEMPLE_GUARD_1_NAME, MKUHumans.TEMPLE_GUARD_1_LOOK);
        putLook(MKUHumans.TEMPLE_GUARD_2_NAME, MKUHumans.TEMPLE_GUARD_2_LOOK);
        putLook(MKUHumans.NECROTIDE_CULTIST_1_NAME, MKUHumans.NECROTIDE_CULTIST_1);
        putLook(MKUHumans.NECROTIDE_CULTIST_SKULL_1_NAME, MKUHumans.NECROTIDE_CULTIST_SKULL_1);
    }

    @Nonnull
    @Override
    public ResourceLocation getBaseTexture(HumanEntity humanEntity) {
        return MKUHumans.HUMAN_SKIN_1;
    }


}
