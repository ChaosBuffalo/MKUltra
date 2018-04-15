package com.chaosbuffalo.mkultra.core.classes;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.BaseAbility;
import com.chaosbuffalo.mkultra.core.BaseClass;
import com.chaosbuffalo.mkultra.core.abilities.*;
import com.chaosbuffalo.mkultra.core.ArmorClass;
import com.chaosbuffalo.mkultra.init.ModItems;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class Brawler extends BaseClass {

    public static final List<BaseAbility> abilities = new ArrayList<>(5);

    static {
        abilities.add(new Yank());
        abilities.add(new FuriousBrooding());
        abilities.add(new Yaup());
        abilities.add(new StunningShout());
        abilities.add(new WhirlwindBlades());
    }

    public Brawler() {
        super(MKUltra.MODID, "class.brawler", "Brawler");
    }

    @Override
    public ArmorClass getArmorClass() {
        return ArmorClass.MEDIUM;
    }

    @Override
    public List<BaseAbility> getAbilities() {
        return abilities;
    }

    @Override
    public int getHealthPerLevel() {
        return 2;
    }

    @Override
    public int getBaseHealth() {
        return 26;
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
        return 8;
    }

    @Override
    public int getManaPerLevel() {
        return 1;
    }

    @Override
    public Item getUnlockItem() {
        return ModItems.sunicon;
    }
}
