package com.chaosbuffalo.mkultra.core.classes;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.core.abilities.*;
import com.chaosbuffalo.mkultra.init.ModItems;
import com.chaosbuffalo.mkultra.item.ClassIcon;
import com.chaosbuffalo.mkultra.core.IClassProvider;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class WetWizard extends PlayerClass {
    public static ResourceLocation ID = new ResourceLocation(MKUltra.MODID, "class.wet_wizard");

    public static final List<PlayerAbility> abilities = new ArrayList<>(5);
    static {
        abilities.add(new Drown());
        abilities.add(new Geyser());
        abilities.add(new Esuna());
        abilities.add(new Undertow());
        abilities.add(new MassDrown());
    }

    public WetWizard() {
        super(ID);
    }

    @Override
    public ArmorClass getArmorClass() {
        return ArmorClass.ROBES;
    }

    @Override
    public List<PlayerAbility> getAbilities() {
        return abilities;
    }

    @Override
    public int getHealthPerLevel(){
        return 1;
    }

    @Override
    public int getBaseHealth(){
        return 24;
    }

    @Override
    public float getBaseManaRegen(){
        return 1;
    }

    @Override
    public float getManaRegenPerLevel(){
        return 0.2f;
    }

    @Override
    public int getBaseMana(){
        return 12;
    }

    @Override
    public int getManaPerLevel(){
        return 2;
    }

    @Override
    public IClassProvider getClassProvider() {
        return (ClassIcon) ModItems.sun_icon;
    }

    @Override
    public IClassClientData getClientData() {
        return ClassClientData.SunIcon.INSTANCE;
    }
}
