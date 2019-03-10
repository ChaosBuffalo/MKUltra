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

/**
 * Created by Jacob on 6/23/2018.
 */
public class Ranger extends PlayerClass {

    public static final List<PlayerAbility> abilities = new ArrayList<>(5);
    static {
        abilities.add(new PracticedHunter());
        abilities.add(new FairyFire());
        abilities.add(new DesperateSurge());
        abilities.add(new WildToxin());
        abilities.add(new SlayingEdge());
    }

    public Ranger() {
        super(MKUltra.MODID, "class.ranger");
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
        return 10;
    }

    @Override
    public int getManaPerLevel(){
        return 2;
    }

    @Override
    public Item getUnlockItem() {
        return ModItems.moon_icon;
    }
}