package com.chaosbuffalo.mkultra.client.render.styling;


import com.chaosbuffalo.mknpc.client.render.models.styling.ModelLook;
import com.chaosbuffalo.mknpc.client.render.models.styling.ModelStyle;
import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.resources.ResourceLocation;

public class MKUGolems {

    public static final ResourceLocation NECROTIDE_GOLEM = new ResourceLocation(MKUltra.MODID,
            "textures/entity/golem/necrotide_golem.png");

    public static final String BASIC_GOLEM_NAME = "basic_golem";
    public static final ModelStyle GOLEM_STYLE = new ModelStyle(BASIC_GOLEM_NAME, false, false);

    public static final String NECROTIDE_GOLEM_NAME = "necrotide_golem";

    public static ModelLook NECROTIDE_GOLEM_LOOK = new ModelLook(GOLEM_STYLE, NECROTIDE_GOLEM);
}
