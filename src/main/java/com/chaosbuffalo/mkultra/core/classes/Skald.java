package com.chaosbuffalo.mkultra.core.classes;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.PlayerClass;
import com.chaosbuffalo.mkultra.core.abilities.*;
import com.chaosbuffalo.mkultra.core.ArmorClass;
import com.chaosbuffalo.mkultra.init.ModItems;
import com.chaosbuffalo.mkultra.item.ClassIcon;
import com.chaosbuffalo.mkultra.item.interfaces.IClassProvider;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class Skald extends PlayerClass {

    public static final List<PlayerAbility> abilities = new ArrayList<>(5);
    static {
        abilities.add(new NotoriousDOT());
        abilities.add(new KanyeCutter());
        abilities.add(new SwiftsRodeoHeartbreak());
        abilities.add(new KPDarkWail());
        abilities.add(new MileysInspiringBangerz());
    }

    public Skald() {
        super(MKUltra.MODID, "class.skald");
    }

    @Override
    public ArmorClass getArmorClass() {
        return ArmorClass.MEDIUM;
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
        return 26;
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
        return 10;
    }

    @Override
    public int getManaPerLevel(){
        return 2;
    }

    @Override
    public IClassProvider getClassProvider() {
        return (ClassIcon) ModItems.sun_icon;
    }
}