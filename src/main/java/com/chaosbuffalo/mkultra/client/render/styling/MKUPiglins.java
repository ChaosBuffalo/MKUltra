package com.chaosbuffalo.mkultra.client.render.styling;

import com.chaosbuffalo.mknpc.client.render.models.styling.ModelLook;
import com.chaosbuffalo.mknpc.client.render.models.styling.ModelStyles;
import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;


public class MKUPiglins {

    public static final ResourceLocation VANILLA_ZOMBIFIED_PIGLIN_TEXTURE = new ResourceLocation(
            "textures/entity/piglin/zombified_piglin.png");

    public static final ResourceLocation SKELETAL_ZOMBIFIED_PIGLIN_TEXTURE = new ResourceLocation(MKUltra.MODID,
            "textures/entity/piglin/zombified_piglin_skeletal_face.png");

    public static final ResourceLocation IMPERIAL_TROOPER_ARMOR = new ResourceLocation(
            MKUltra.MODID,
            "textures/entity/piglin/imperial_armor.png"
    );

    public static final ResourceLocation IMPERIAL_TROOPER_ARMOR_NO_HELMET = new ResourceLocation(
            MKUltra.MODID,
            "textures/entity/piglin/imperial_armor_no_helmet.png"
    );

    public static final ResourceLocation IMPERIAL_TROOPER_ARMOR_DAMAGED = new ResourceLocation(
            MKUltra.MODID,
            "textures/entity/piglin/imperial_trooper_damaged.png"
    );

    public static final ResourceLocation IMPERIAL_MAGUS_ARMOR_NO_HELMET = new ResourceLocation(
            MKUltra.MODID,
            "textures/entity/piglin/imperial_magus_armor.png"
    );


    public static final ResourceLocation IMPERIAL_MAGUS_ARMOR = new ResourceLocation(
            MKUltra.MODID,
            "textures/entity/piglin/imperial_magus_armor_no_helmet.png"
    );

    public static final ResourceLocation IMPERIAL_MAGUS_ARMOR_DAMAGED = new ResourceLocation(
            MKUltra.MODID,
            "textures/entity/piglin/imperial_magus_armor_damaged.png"
    );

    public static final ModelLook ZOMBIE_PIG_TROOPER_LOOK = new ModelLook(ModelStyles.CLOTHES_ARMOR_TRANSLUCENT_STYLE,
            VANILLA_ZOMBIFIED_PIGLIN_TEXTURE, IMPERIAL_TROOPER_ARMOR_DAMAGED);
    public static final ModelLook ZOMBIE_PIG_MAGE_LOOK = new ModelLook(ModelStyles.CLOTHES_ARMOR_TRANSLUCENT_STYLE,
            VANILLA_ZOMBIFIED_PIGLIN_TEXTURE, IMPERIAL_MAGUS_ARMOR_DAMAGED);
    public static final ModelLook SKELETAL_TROOPER_LOOK = new ModelLook(ModelStyles.CLOTHES_ARMOR_STYLE,
            SKELETAL_ZOMBIFIED_PIGLIN_TEXTURE, IMPERIAL_TROOPER_ARMOR_NO_HELMET);
    public static final ModelLook SKELETAL_MAGE_LOOK = new ModelLook(ModelStyles.CLOTHES_ARMOR_STYLE,
            SKELETAL_ZOMBIFIED_PIGLIN_TEXTURE, IMPERIAL_MAGUS_ARMOR_NO_HELMET);
    public static final ModelLook DESTROYED_SKELETAL_MAGE_LOOK = new ModelLook(ModelStyles.CLOTHES_ARMOR_TRANSLUCENT_STYLE,
            SKELETAL_ZOMBIFIED_PIGLIN_TEXTURE, IMPERIAL_MAGUS_ARMOR_DAMAGED);
    public static final ModelLook DESTROYED_SKELETAL_TROOPER_LOOK = new ModelLook(ModelStyles.CLOTHES_ARMOR_TRANSLUCENT_STYLE,
            SKELETAL_ZOMBIFIED_PIGLIN_TEXTURE, IMPERIAL_TROOPER_ARMOR_DAMAGED);
    public static final ModelLook BASE_LOOK = new ModelLook(ModelStyles.BASIC_STYLE, VANILLA_ZOMBIFIED_PIGLIN_TEXTURE);

    public static final Map<String, ModelLook> ZOMBIE_PIGLIN_STYLES = new HashMap<>();

    public static final String ZOMBIE_PIG_TROOPER_NAME = "zombie_pig_trooper";
    public static final String ZOMBIE_PIG_MAGUS_NAME = "zombie_pig_magus";
    public static final String ZOMBIE_PIG_NAME = "zombie_pig";
    public static final String SKELETAL_TROOPER_NAME = "skeletal_trooper";
    public static final String SKELETAL_MAGE_NAME = "skeletal_mage";
    public static final String DESTROYED_SKELETAL_MAGE_NAME = "destroyed_skeletal_mage";
    public static final String DESTROYED_SKELETAL_TROOPER_NAME = "destroyed_skeletal_trooper";

    static {
        ZOMBIE_PIGLIN_STYLES.put(ZOMBIE_PIG_TROOPER_NAME, ZOMBIE_PIG_TROOPER_LOOK);
        ZOMBIE_PIGLIN_STYLES.put(ZOMBIE_PIG_MAGUS_NAME, ZOMBIE_PIG_MAGE_LOOK);
        ZOMBIE_PIGLIN_STYLES.put(ZOMBIE_PIG_NAME, BASE_LOOK);
        ZOMBIE_PIGLIN_STYLES.put(SKELETAL_TROOPER_NAME, SKELETAL_TROOPER_LOOK);
        ZOMBIE_PIGLIN_STYLES.put(SKELETAL_MAGE_NAME, SKELETAL_MAGE_LOOK);
        ZOMBIE_PIGLIN_STYLES.put(DESTROYED_SKELETAL_MAGE_NAME, DESTROYED_SKELETAL_MAGE_LOOK);
        ZOMBIE_PIGLIN_STYLES.put(DESTROYED_SKELETAL_TROOPER_NAME, DESTROYED_SKELETAL_TROOPER_LOOK);
    }
}

