package com.chaosbuffalo.mkultra.client.render.styling;

import com.chaosbuffalo.mknpc.client.render.models.styling.ModelLook;
import com.chaosbuffalo.mknpc.client.render.models.styling.ModelStyles;
import com.chaosbuffalo.mknpc.client.render.renderers.SkeletonStyles;

import java.util.HashMap;
import java.util.Map;

public class MKUSkeletons {

    public static final ModelLook HYBOREAN_WARRIOR = new ModelLook(ModelStyles.CLOTHES_ONLY_STYLE,
            SkeletonStyles.SKELETON_TEXTURES, MKUClothes.LOINCLOTH);

    public static final ModelLook HYBOREAN_ARCHER = new ModelLook(ModelStyles.CLOTHES_ONLY_STYLE,
            SkeletonStyles.SKELETON_TEXTURES, MKUClothes.LOINCLOTH_2);

    public static final ModelLook HONOR_GUARD = new ModelLook(ModelStyles.CLOTHES_ONLY_STYLE,
            SkeletonStyles.SKELETON_TEXTURES, MKUClothes.HYBOREAN_ARMOR);

    public static final ModelLook SORCERER_QUEEN = new ModelLook(ModelStyles.CLOTHES_ONLY_STYLE,
            SkeletonStyles.STRAY_SKELETON_TEXTURES, MKUClothes.FUR_LINED_SCRAPS_2);

    public static final ModelLook SORCERER = new ModelLook(ModelStyles.CLOTHES_ONLY_STYLE,
            SkeletonStyles.SKELETON_TEXTURES, MKUClothes.IRON_PONCHO);

    public static final ModelLook ANCIENT_KING = new ModelLook(ModelStyles.CLOTHES_ONLY_STYLE,
            SkeletonStyles.WITHER_SKELETON_TEXTURES, MKUClothes.FUR_LINED_SCRAPS);

    public static final ModelLook BURNING_SKELETON = new ModelLook(ModelStyles.CLOTHES_ONLY_STYLE,
            SkeletonStyles.WITHER_SKELETON_TEXTURES, MKUClothes.IRON_PONCHO);

    public static final Map<String, ModelLook> SKELETON_STYLES = new HashMap<>();

    public static final String ANCIENT_KING_NAME = "ancient_king";

    public static final String SORCERER_NAME = "sorcerer";

    public static final String BURNING_NAME = "burning_skeleton";

    public static final String SORCERER_QUEEN_NAME = "sorcerer_queen";

    public static final String HONOR_GUARD_NAME = "honor_guard";

    public static final String HYBOREAN_ARCHER_NAME = "hyborean_archer";

    public static final String HYBOREAN_WARRIOR_NAME = "hyborean_warrior";

    public static final String BASIC_NAME = "basic";

    public static final ModelLook BASIC = new ModelLook(ModelStyles.BASIC_STYLE, SkeletonStyles.SKELETON_TEXTURES);

    static {
        SKELETON_STYLES.put(ANCIENT_KING_NAME, ANCIENT_KING);
        SKELETON_STYLES.put(SORCERER_NAME, SORCERER);
        SKELETON_STYLES.put(SORCERER_QUEEN_NAME, SORCERER_QUEEN);
        SKELETON_STYLES.put(HONOR_GUARD_NAME, HONOR_GUARD);
        SKELETON_STYLES.put(HYBOREAN_ARCHER_NAME, HYBOREAN_ARCHER);
        SKELETON_STYLES.put(HYBOREAN_WARRIOR_NAME, HYBOREAN_WARRIOR);
        SKELETON_STYLES.put(BURNING_NAME, BURNING_SKELETON);
        SKELETON_STYLES.put(BASIC_NAME, BASIC);


    }
}
