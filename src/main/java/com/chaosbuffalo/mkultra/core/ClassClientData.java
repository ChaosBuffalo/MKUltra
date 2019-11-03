package com.chaosbuffalo.mkultra.core;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.registries.IForgeRegistryEntry;


public class ClassClientData extends IForgeRegistryEntry.Impl<ClassClientData>
        implements IClassClientData {

    private final ResourceLocation name;
    private final ResourceLocation icon;
    private final ResourceLocation xpBackground;
    private final int xpColor;

    public ClassClientData(ResourceLocation name, ResourceLocation icon,
                           ResourceLocation xpBackground, int xpColor){
        setRegistryName(name);
        this.name = name;
        this.xpColor = xpColor;
        this.icon = icon;
        this.xpBackground = xpBackground;
    }

    @Override
    public ResourceLocation getIcon() {
        return icon;
    }

    @Override
    public String getXpTableText() {
        return I18n.format(String.format("%s.%s.text",
                name.getNamespace(), name.getPath()));
    }


    @Override
    public ResourceLocation getXpTableBackground() {
        return xpBackground;
    }

    @Override
    public int getXpTableTextColor() {
        return xpColor;
    }
}
