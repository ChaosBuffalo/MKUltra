package com.chaosbuffalo.mkultra.client.render.styling;

import com.chaosbuffalo.mknpc.client.render.models.styling.ModelLook;
import com.chaosbuffalo.mknpc.client.render.models.styling.ModelStyles;
import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.resources.ResourceLocation;

public class MKUOrcs {

    public static final ResourceLocation BLUE_ORC = new ResourceLocation(MKUltra.MODID,
            "textures/entity/orcs/blue_orc.png");
    public static final ResourceLocation GREEN_ORC = new ResourceLocation(MKUltra.MODID,
            "textures/entity/orcs/green_orc.png");
    public static final ResourceLocation RED_ORC = new ResourceLocation(MKUltra.MODID,
            "textures/entity/orcs/red_orc.png");

    public static final ResourceLocation GREEN_LADY_HAIR_1 = new ResourceLocation(MKUltra.MODID,
            "textures/entity/orcs/green_lady_hair_1.png");
    public static final ResourceLocation GREEN_LADY_HAIR_2 = new ResourceLocation(MKUltra.MODID,
            "textures/entity/orcs/green_lady_hair_2.png");

    public static final ResourceLocation ORC_HAIR_1 = new ResourceLocation(MKUltra.MODID,
            "textures/entity/orcs/orc_hair_1.png");
    public static final ResourceLocation ORC_HAIR_2 = new ResourceLocation(MKUltra.MODID,
            "textures/entity/orcs/orc_hair_2.png");
    public static final ResourceLocation ORC_LONG_HAIR_1_LAYER_2 = new ResourceLocation(MKUltra.MODID,
            "textures/entity/orcs/orc_long_hair_1_layer_2.png");
    public static final ResourceLocation ORC_LONG_HAIR_2_LAYER_2 = new ResourceLocation(MKUltra.MODID,
            "textures/entity/orcs/orc_long_hair_2_layer_2.png");
    public static final ResourceLocation ORC_LONG_HAIR_3_LAYER_2 = new ResourceLocation(MKUltra.MODID,
            "textures/entity/orcs/orc_long_hair_3_layer_2.png");

    public static ModelLook GREEN_LADY_LOOK = new ModelLook(ModelStyles.LONG_HAIR_STYLE, GREEN_ORC, GREEN_LADY_HAIR_1,
            MKUClothes.GREEN_LADY_CLOTHES, GREEN_LADY_HAIR_2);

    public static ModelLook GREEN_LADY_GUARD_1_LOOK = new ModelLook(ModelStyles.LONG_HAIR_STYLE, BLUE_ORC,
            ORC_HAIR_2, MKUClothes.LOINCLOTH, ORC_LONG_HAIR_3_LAYER_2);

    public static ModelLook GREEN_LADY_GUARD_2_LOOK = new ModelLook(ModelStyles.LONG_HAIR_STYLE, GREEN_ORC,
            ORC_HAIR_1, MKUClothes.LOINCLOTH_2, ORC_LONG_HAIR_1_LAYER_2);

    public static ModelLook GREEN_SMITH_LOOK = new ModelLook(ModelStyles.LONG_HAIR_STYLE, GREEN_ORC,
            ORC_HAIR_2, MKUClothes.LOINCLOTH, ORC_LONG_HAIR_2_LAYER_2);

    public static ModelLook ORC_BASE = new ModelLook(ModelStyles.BASIC_STYLE, GREEN_ORC);

    public static final String DEFAULT_NAME = "default";
    public static final String GREEN_LADY_NAME = "green_lady";
    public static final String GREEN_LADY_GUARD_1_NAME = "green_lady_guard_1";
    public static final String GREEN_LADY_GUARD_2_NAME = "green_lady_guard_2";
    public static final String GREEN_SMITH_NAME = "green_smith";


}
