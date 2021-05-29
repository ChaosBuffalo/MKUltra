package com.chaosbuffalo.mkultra.client.render.styling;

import com.chaosbuffalo.mknpc.client.render.models.styling.ModelLook;
import com.chaosbuffalo.mknpc.client.render.models.styling.ModelStyles;
import com.chaosbuffalo.mknpc.client.render.renderers.SkeletonStyles;

public class MKUSkeletons {

    public static final ModelLook SKELETAL_WARRIOR = new ModelLook(ModelStyles.CLOTHES_ONLY_STYLE,
            SkeletonStyles.SKELETON_TEXTURES, MKUClothes.LOINCLOTH);

    public static final ModelLook SKELETAL_ARCHER = new ModelLook(ModelStyles.CLOTHES_ONLY_STYLE,
            SkeletonStyles.SKELETON_TEXTURES, MKUClothes.LOINCLOTH_2);

    public static final ModelLook HONOR_GUARD = new ModelLook(ModelStyles.CLOTHES_ONLY_STYLE,
            SkeletonStyles.SKELETON_TEXTURES, MKUClothes.HYBOREAN_ARMOR);

    public static final ModelLook SORCERER_QUEEN = new ModelLook(ModelStyles.CLOTHES_ONLY_STYLE,
            SkeletonStyles.STRAY_SKELETON_TEXTURES, MKUClothes.FUR_LINED_SCRAPS_2);

    public static final ModelLook SORCERER = new ModelLook(ModelStyles.CLOTHES_ONLY_STYLE,
            SkeletonStyles.SKELETON_TEXTURES, MKUClothes.IRON_PONCHO);

    public static final ModelLook ANCIENT_KING = new ModelLook(ModelStyles.CLOTHES_ONLY_STYLE,
            SkeletonStyles.WITHER_SKELETON_TEXTURES, MKUClothes.FUR_LINED_SCRAPS);
}
