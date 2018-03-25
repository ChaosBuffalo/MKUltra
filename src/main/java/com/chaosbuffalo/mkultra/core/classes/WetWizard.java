package com.chaosbuffalo.mkultra.core.classes;

import com.chaosbuffalo.mkultra.core.BaseAbility;
import com.chaosbuffalo.mkultra.core.BaseClass;
import com.chaosbuffalo.mkultra.core.abilities.*;
import com.chaosbuffalo.mkultra.core.ArmorClass;
import com.chaosbuffalo.mkultra.init.ModItems;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class WetWizard extends BaseClass {

    public static final List<BaseAbility> abilities = new ArrayList<>(5);
    static {
        abilities.add(new Drown());
        abilities.add(new Geyser());
        abilities.add(new Esuna());
        abilities.add(new Undertow());
        abilities.add(new MassDrown());
    }

    public WetWizard() {
        super("class.wet_wizard", "Wet Wizard");
    }

    @Override
    public ArmorClass getArmorClass() {
        return ArmorClass.ROBES;
    }

    @Override
    public List<BaseAbility> getAbilities() {
        return abilities;
    }

    @Override
    public int getIconU(){return 0;}

    @Override
    public int getIconV(){return 0;}

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
    public Item getUnlockItem() {
        return ModItems.sunicon;
    }
}