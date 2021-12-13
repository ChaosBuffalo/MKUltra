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

    public static final ResourceLocation IMPERIAL_TROOPER_ARMOR = new ResourceLocation(
            MKUltra.MODID,
            "textures/entity/piglin/imperial_armor.png"
    );

    public static final ResourceLocation IMPERIAL_TROOPER_ARMOR_DAMAGED = new ResourceLocation(
            MKUltra.MODID,
            "textures/entity/piglin/imperial_trooper_damaged.png"
    );

    public static final ResourceLocation IMPERIAL_MAGUS_ARMOR = new ResourceLocation(
            MKUltra.MODID,
            "textures/entity/piglin/imperial_magus_armor.png"
    );

    public static final ResourceLocation IMPERIAL_MAGUS_ARMOR_DAMAGED = new ResourceLocation(
            MKUltra.MODID,
            "textures/entity/piglin/imperial_magus_armor_damaged.png"
    );

    public static final ModelLook ZOMBIE_PIG_TROOPER_LOOK = new ModelLook(ModelStyles.CLOTHES_ARMOR_TRANSLUCENT_STYLE,
            VANILLA_ZOMBIFIED_PIGLIN_TEXTURE, IMPERIAL_TROOPER_ARMOR_DAMAGED);
    public static final ModelLook ZOMBIE_PIG_MAGE_LOOK = new ModelLook(ModelStyles.CLOTHES_ARMOR_TRANSLUCENT_STYLE,
            VANILLA_ZOMBIFIED_PIGLIN_TEXTURE, IMPERIAL_MAGUS_ARMOR_DAMAGED);

    public static final Map<String, ModelLook> ZOMBIE_PIGLIN_STYLES = new HashMap<>();

    public static final String ZOMBIE_PIG_TROOPER_NAME = "zombie_pig_trooper";
    public static final String ZOMBIE_PIG_MAGUS_NAME = "zombie_pig_magus";

    static {
        ZOMBIE_PIGLIN_STYLES.put(ZOMBIE_PIG_TROOPER_NAME, ZOMBIE_PIG_TROOPER_LOOK);
        ZOMBIE_PIGLIN_STYLES.put(ZOMBIE_PIG_MAGUS_NAME, ZOMBIE_PIG_MAGE_LOOK);
    }
}

