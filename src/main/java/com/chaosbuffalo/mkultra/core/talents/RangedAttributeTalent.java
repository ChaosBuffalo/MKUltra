package com.chaosbuffalo.mkultra.core.talents;

import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.util.ResourceLocation;

import java.util.UUID;

public class RangedAttributeTalent extends BaseTalent {
    private final UUID id;
    private final RangedAttribute attr;
    private int op;

    public RangedAttributeTalent(ResourceLocation name, RangedAttribute attr, UUID id){
        super(name, TalentType.ATTRIBUTE);
        this.id = id;
        this.attr = attr;
        this.op = PlayerAttributes.OP_INCREMENT;
    }

    public RangedAttribute getAttribute(){
        return attr;
    }

    public UUID getUUID(){
        return id;
    }

    public int getOp() { return op; }

    public AttributeModifier createModifier(double value) {
        return new AttributeModifier(getUUID(), getRegistryName().toString(), value, getOp()).setSaved(false);
    }
}
