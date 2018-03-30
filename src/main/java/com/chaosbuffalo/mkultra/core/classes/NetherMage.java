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

public class NetherMage extends BaseClass {

    public static final List<BaseAbility> abilities = new ArrayList<>(5);
    static {
        abilities.add(new Ember());
        abilities.add(new WarpCurse());
        abilities.add(new FlameWave());
        abilities.add(new FireArmor());
        abilities.add(new Ignite());
    }

    public NetherMage() {
        super(MKUltra.MODID, "class.nether_mage", "Nether Mage");
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
        return 22;
    }

    @Override
    public float getBaseManaRegen(){
        return 2;
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
        return 3;
    }

    @Override
    public Item getUnlockItem() {
        return ModItems.sunicon;
    }
}