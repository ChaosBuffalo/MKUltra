package com.chaosbuffalo.mkultra.client.render.styling;

import com.chaosbuffalo.mknpc.client.render.models.styling.LayerStyle;
import com.chaosbuffalo.mknpc.client.render.models.styling.ModelLook;
import com.chaosbuffalo.mknpc.client.render.models.styling.ModelStyle;
import com.chaosbuffalo.mknpc.client.render.models.styling.ModelStyles;
import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.resources.ResourceLocation;

public class MKUHumans {

    public static final ResourceLocation HUMAN_SKIN_1 = new ResourceLocation(MKUltra.MODID,
            "textures/entity/humans/human_skin_1.png");
    public static final ResourceLocation HUMAN_SKIN_2 = new ResourceLocation(MKUltra.MODID,
            "textures/entity/humans/human_skin_2.png");
    public static final ResourceLocation HUMAN_HAIR_1 = new ResourceLocation(MKUltra.MODID,
            "textures/entity/humans/human_hair_1.png");
    public static final ResourceLocation HUMAN_HAIR_2 = new ResourceLocation(MKUltra.MODID,
            "textures/entity/humans/human_hair_2.png");
    public static final ResourceLocation HUMAN_HAIR_3 = new ResourceLocation(MKUltra.MODID,
            "textures/entity/humans/human_hair_3.png");

    public static final ResourceLocation PALE_HUMAN_SKIN_1 = new ResourceLocation(MKUltra.MODID,
            "textures/entity/humans/pale_skin_1.png");

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

    public static final String TWO_LAYER_ARMOR_SHORT_HAIR_NAME = "two_layer_armor_short_hair";
    public static final ModelStyle TWO_LAYER_ARMOR_SHORT_HAIR = new ModelStyle(TWO_LAYER_ARMOR_SHORT_HAIR_NAME,
            false, false, new LayerStyle("hair_1", 0.25F),
            new LayerStyle("armor_lower", 0.5F), new LayerStyle("armor_upper", 1.0f));

    public static final String TWO_LAYER_ARMOR_NO_HAIR_NAME = "two_layer_armor_no_hair";
    public static final ModelStyle TWO_LAYER_ARMOR_NO_HAIR = new ModelStyle(TWO_LAYER_ARMOR_NO_HAIR_NAME,
            false, false,
            new LayerStyle("armor_lower", 0.5F), new LayerStyle("armor_upper", 1.0f));

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
    public static ModelLook TEMPLE_GUARD_1_LOOK = new ModelLook(TWO_LAYER_ARMOR_SHORT_HAIR, HUMAN_SKIN_1, HUMAN_HAIR_2,
            MKUClothes.SOLANGIAN_ARMOR_2, MKUClothes.SOLANGIAN_ARMOR_1);
    public static ModelLook TEMPLE_GUARD_2_LOOK = new ModelLook(TWO_LAYER_ARMOR_SHORT_HAIR, HUMAN_SKIN_1, HUMAN_HAIR_3,
            MKUClothes.SOLANGIAN_ARMOR_2, MKUClothes.SOLANGIAN_ARMOR_1);
    public static ModelLook HUMAN_BASE = new ModelLook(ModelStyles.BASIC_STYLE, HUMAN_SKIN_1);

    public static ModelLook NETHER_MAGE_1_LOOK = new ModelLook(ModelStyles.SHORT_HAIR_STYLE,
            HUMAN_SKIN_1, HUMAN_HAIR_3, MKUClothes.NETHER_MAGE_ROBES_1);

    public static final String CLERIC_1_NAME = "cleric_1";
    public static final String CLERIC_2_NAME = "cleric_2";
    public static final String DEFAULT_NAME = "default";
    public static final String NETHER_MAGE_1_NAME = "nether_mage_1";
    public static final String TEMPLE_GUARD_1_NAME = "temple_guard_1";
    public static final String TEMPLE_GUARD_2_NAME = "temple_guard_2";
    public static final String NECROTIDE_CULTIST_1_NAME = "necrotide_cultist_1";
    public static final String NECROTIDE_CULTIST_SKULL_1_NAME = "necrotide_cultist_skull_1";

    public static ModelLook NECROTIDE_CULTIST_1 = new ModelLook(TWO_LAYER_ARMOR_NO_HAIR, PALE_HUMAN_SKIN_1,
            MKUClothes.NECROTIDE_ROBES_2, MKUClothes.NECROTIDE_ROBES_1);
    public static ModelLook NECROTIDE_CULTIST_SKULL_1 = new ModelLook(TWO_LAYER_ARMOR_NO_HAIR, PALE_HUMAN_SKIN_1,
            MKUClothes.NECROTIDE_ROBES_2, MKUClothes.NECROTIDE_ROBES_SKULL_HOOD);

}
