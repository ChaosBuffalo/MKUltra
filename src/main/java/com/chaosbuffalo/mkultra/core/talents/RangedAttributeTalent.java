package com.chaosbuffalo.mkultra.core.talents;

import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class RangedAttributeTalent extends BaseTalent {
    private final UUID id;
    private final RangedAttribute attr;
    private int op;
    private boolean renderAsPercentage;

    public RangedAttributeTalent(ResourceLocation name, RangedAttribute attr, UUID id) {
        this(name, attr, id, false);
    }

    public RangedAttributeTalent(ResourceLocation name, RangedAttribute attr, UUID id, boolean renderAsPercentage) {
        super(name, TalentType.ATTRIBUTE);
        this.id = id;
        this.attr = attr;
        this.op = PlayerAttributes.OP_INCREMENT;
        this.renderAsPercentage = renderAsPercentage;

    }

    public RangedAttribute getAttribute() {
        return attr;
    }

    public UUID getUUID() {
        return id;
    }

    public int getOp() {
        return op;
    }

    @SideOnly(Side.CLIENT)
    public String getTalentDescription(double perRank, double currentValue) {
        String amount;
        String totalAmount;
        if (renderAsPercentage){
            amount = Double.toString(perRank * 100.0) + "%";
            totalAmount = Double.toString(currentValue * 100.0) + "%";
        } else {
            amount = Double.toString(perRank);
            totalAmount = Double.toString(currentValue);
        }
        String finalAmount = String.format("%s (%s)", amount, totalAmount);
        return TextFormatting.GRAY + I18n.format(String.format("%s.%s.description",
                getRegistryName().getNamespace(), getRegistryName().getPath()), finalAmount);
    }

    public AttributeModifier createModifier(double value) {
        return new AttributeModifier(getUUID(), getRegistryName().toString(), value, getOp()).setSaved(false);
    }
}
