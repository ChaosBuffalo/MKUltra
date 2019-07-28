package com.chaosbuffalo.mkultra.core.talents;

import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.util.ResourceLocation;

import java.util.UUID;

public class RangedAttributeTalent extends BaseTalent {
    private UUID id;
    private RangedAttribute attr;

    public RangedAttributeTalent(ResourceLocation name, RangedAttribute attr, UUID id){
        super(name, TalentType.ATTRIBUTE);
        this.id = id;
        this.attr = attr;
    }
}
