package com.chaosbuffalo.mkultra.core.classes;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.ArmorClass;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.PlayerClass;
import com.chaosbuffalo.mkultra.core.abilities.*;
import com.chaosbuffalo.mkultra.init.ModItems;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class Cleric extends PlayerClass {

    private static final List<PlayerAbility> abilities = new ArrayList<>(5);

    static {
        abilities.add(new Smite());
        abilities.add(new Heal());
        abilities.add(new Galvanize());
        abilities.add(new PowerWordSummon());
        abilities.add(new Inspire());
    }

    public Cleric() {
        super(MKUltra.MODID, "class.cleric", "Cleric");
    }

    @Override
    public List<PlayerAbility> getAbilities() {
        return abilities;
    }

    @Override
    public int getHealthPerLevel(){
        return 2;
    }

    @Override
    public int getBaseHealth(){
        return 20;
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
        return 1;
    }

    @Override
    public ArmorClass getArmorClass() {
        return ArmorClass.HEAVY;
    }

    @Override
    public Item getUnlockItem() {
        return ModItems.sun_icon;
    }
}
