package com.chaosbuffalo.mkultra.core.talents;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.List;

public class TalentTree extends IForgeRegistryEntry.Impl<TalentTree> {

    private Multimap<String, TalentNode> talents = MultimapBuilder.hashKeys().arrayListValues().build();
    private int version;

    public TalentTree(ResourceLocation name, int version) {
        setRegistryName(name);
        this.version = version;
    }

    public void addLine(String name, List<TalentNode> nodes) {
        talents.putAll(name, nodes);
    }

    public Multimap<String, TalentNode> getLines() {
        return talents;
    }

    public int getVersion() {
        return version;
    }

    @SideOnly(Side.CLIENT)
    public String getName() {
        return I18n.format(String.format("%s.%s.name",
                getRegistryName().getNamespace(), getRegistryName().getPath()));
    }
}
