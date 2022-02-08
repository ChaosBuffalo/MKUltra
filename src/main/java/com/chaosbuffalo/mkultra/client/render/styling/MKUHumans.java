package com.chaosbuffalo.mkultra.client.render.styling;

import com.chaosbuffalo.mknpc.client.render.models.styling.LayerStyle;
import com.chaosbuffalo.mknpc.client.render.models.styling.ModelLook;
import com.chaosbuffalo.mknpc.client.render.models.styling.ModelStyle;
import com.chaosbuffalo.mknpc.client.render.models.styling.ModelStyles;
import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.util.ResourceLocation;

public class MKUHumans {

    public static final ResourceLocation HUMAN_SKIN_1 = new ResourceLocation(MKUltra.MODID,
            "textures/entity/humans/human_skin_1.png");
    public static final ResourceLocation HUMAN_SKIN_2 = new ResourceLocation(MKUltra.MODID,
            "textures/entity/humans/human_skin_2.png");
    public static final ResourceLocation HUMAN_HAIR_1 = new ResourceLocation(MKUltra.MODID,
            "textures/entity/humans/human_hair_1.png");
    public static final ResourceLocation HUMAN_HAIR_2 = new ResourceLocation(MKUltra.MODID,
            "textures/entity/humans/human_hair_2.png");

    public static final ResourceLocation GHOST_SKIN_1 = new ResourceLocation(MKUltra.MODID,
            "textures/entity/humans/ghost_skin_1.png");
    public static final ResourceLocation GHOST_HAIR_1 = new ResourceLocation(MKUltra.MODID,
            "textures/entity/humans/ghost_hair_1.png");
    public static final ResourceLocation GHOST_HAIR_2 = new ResourceLocation(MKUltra.MODID,
            "textures/entity/humans/ghost_hair_2.png");

    public static final String TWO_LAYER_CLOTHES_SHORT_HAIR_NAME = "two_layer_clothes_short_hair";
    public static final ModelStyle TWO_LAYER_CLOTHES_SHORT_HAIR = new ModelStyle(TWO_LAYER_CLOTHES_SHORT_HAIR_NAME,
            true, false, new LayerStyle("hair_1", 0.25F),
            new LayerStyle("clothes_1", 0.3F), new LayerStyle("clothes_2", 0.5f));

    public static final String GHOST_LONG_HAIR_NAME = "ghost_long_hair";
    public static final String GHOST_LONG_HAIR_ARMORED_NAME = "ghost_long_hair_armored";

    public static final ModelStyle ARMORED_GHOST_LONG_HAIR_STYLE = new ModelStyle(GHOST_LONG_HAIR_ARMORED_NAME,
            true, false,
            new LayerStyle("hair_1", 0.25F, true),
            new LayerStyle("clothes_1", 0.4F, true),
            new LayerStyle("hair_2", 1.15F, true));
    public static final ModelStyle GHOST_LONG_HAIR_STYLE = new ModelStyle(GHOST_LONG_HAIR_NAME,
            true, true,
            new LayerStyle("hair_1", 0.25F, true),
            new LayerStyle("clothes_1", 0.4F, true),
            new LayerStyle("hair_2", 0.45F, true));

    public static ModelLook GHOST_LOOK_1 = new ModelLook(GHOST_LONG_HAIR_STYLE, GHOST_SKIN_1, GHOST_HAIR_1,
            MKUClothes.GHOST_LEATHERS_1, GHOST_HAIR_2);
    public static final String GHOST_1_NAME = "ghost_1";

    public static ModelLook CLERIC_1_LOOK = new ModelLook(TWO_LAYER_CLOTHES_SHORT_HAIR, HUMAN_SKIN_1, HUMAN_HAIR_1,
            MKUClothes.SOLANG_ROBES_1, MKUClothes.SOLANG_ROBES_2);
    public static ModelLook CLERIC_2_LOOK = new ModelLook(TWO_LAYER_CLOTHES_SHORT_HAIR, HUMAN_SKIN_2, HUMAN_HAIR_2,
            MKUClothes.SOLANG_ROBES_1, MKUClothes.SOLANG_ROBES_2);
    public static ModelLook HUMAN_BASE = new ModelLook(ModelStyles.BASIC_STYLE, HUMAN_SKIN_1);

    public static final String CLERIC_1_NAME = "cleric_1";
    public static final String CLERIC_2_NAME = "cleric_2";
    public static final String DEFAULT_NAME = "default";
}
