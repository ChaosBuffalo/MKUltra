package com.chaosbuffalo.mkultra;


import com.chaosbuffalo.mkultra.item.MKUltraTab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;


@Mod(modid = MKUltra.MODID)
public class MKUltra {
    public static final String MODID = "mkultra";
    public static final CreativeTabs MKULTRA_TAB = new MKUltraTab(CreativeTabs.getNextID(),
            MODID + ".general");

    public MKUltra(){

    }
}
