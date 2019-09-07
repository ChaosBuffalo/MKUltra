package com.chaosbuffalo.mkultra.core.talents;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.PlayerClassInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.regex.Pattern;

public class BaseTalent extends IForgeRegistryEntry.Impl<BaseTalent> {

    private final TalentType talentType;

    public enum TalentType {
        ATTRIBUTE,
        PASSIVE,
        ULTIMATE
    }

    public BaseTalent(ResourceLocation name,
                      TalentType type) {
        setRegistryName(name);
        this.talentType = type;
    }

    public TalentType getTalentType() {
        return talentType;
    }

    public boolean onAdd(EntityPlayer player, PlayerClassInfo classInfo) {
        return true;
    }

    public boolean onRemove(EntityPlayer player, PlayerClassInfo classInfo) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public String getTalentName() {
        return I18n.format(String.format("%s.%s.name",
                getRegistryName().getNamespace(), getRegistryName().getPath()));
    }

    @SideOnly(Side.CLIENT)
    public String getTalentDescription(TalentRecord record) {
        return TextFormatting.GRAY + I18n.format(String.format("%s.%s.description",
                getRegistryName().getNamespace(), getRegistryName().getPath()));
    }

    @SideOnly(Side.CLIENT)
    public String getTalentTypeName() {
        return TextFormatting.GOLD + I18n.format(String.format("%s.talent_type.%s.name",
                MKUltra.MODID, getTalentType().toString().toLowerCase()));
    }

    public ResourceLocation getIcon() {
        return new ResourceLocation(getRegistryName().getNamespace(),
                String.format("textures/talents/%s_icon.png",
                        getRegistryName().getPath().split(Pattern.quote("."))[1]));
    }

    public ResourceLocation getFilledIcon() {
        return new ResourceLocation(getRegistryName().getNamespace(),
                String.format("textures/talents/%s_icon_filled.png",
                        getRegistryName().getPath().split(Pattern.quote("."))[1]));
    }
}
