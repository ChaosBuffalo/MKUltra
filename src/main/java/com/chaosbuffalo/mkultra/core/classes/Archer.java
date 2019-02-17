package com.chaosbuffalo.mkultra.core.classes;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.BaseClass;
import com.chaosbuffalo.mkultra.core.abilities.*;
import com.chaosbuffalo.mkultra.core.ArmorClass;
import com.chaosbuffalo.mkultra.init.ModItems;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;


public class Archer extends BaseClass {

    public static final List<PlayerAbility> abilities = new ArrayList<>(5);

    static {
        abilities.add(new FlintHound());
        abilities.add(new FireArrow());
        abilities.add(new PoisonArrow());
        abilities.add(new Repulse());
        abilities.add(new ArrowStorm());
    }

    public Archer() {
        super(MKUltra.MODID, "class.archer", "Archer");
    }

    @Override
    public ArmorClass getArmorClass() {
        return ArmorClass.LIGHT;
    }

    @Override
    public List<PlayerAbility> getAbilities() {
        return abilities;
    }

    @Override
    public int getHealthPerLevel() {
        return 2;
    }

    @Override
    public int getBaseHealth() {
        return 18;
    }

    @Override
    public float getBaseManaRegen() {
        return 1;
    }

    @Override
    public float getManaRegenPerLevel() {
        return 0.2f;
    }

    @Override
    public int getBaseMana() {
        return 15;
    }

    @Override
    public int getManaPerLevel() {
        return 1;
    }

    @Override
    public Item getUnlockItem() {
        return ModItems.sun_icon;
    }
}
