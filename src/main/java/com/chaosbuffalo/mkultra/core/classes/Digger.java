package com.chaosbuffalo.mkultra.core.classes;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.BaseAbility;
import com.chaosbuffalo.mkultra.core.BaseClass;
import com.chaosbuffalo.mkultra.core.abilities.*;
import com.chaosbuffalo.mkultra.core.ArmorClass;
import com.chaosbuffalo.mkultra.init.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;

import java.util.ArrayList;
import java.util.List;

public class Digger extends BaseClass {

    public static final List<BaseAbility> abilities = new ArrayList<>(5);
    private static final ArmorClass ARMORCLASS = new DiggerArmorClass();

    static {
        abilities.add(new GoldenOpportunity());
        abilities.add(new HopeBread());
        abilities.add(new PierceTheHeavens());
        abilities.add(new TNTWhisperer());
        abilities.add(new LavaWanderer());
    }

    public Digger() {
        super(MKUltra.MODID, "class.digger", "Digger");
    }

    @Override
    public List<BaseAbility> getAbilities() {
        return abilities;
    }

    @Override
    public int getHealthPerLevel() {
        return 4;
    }

    @Override
    public int getBaseHealth() {
        return 22;
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
        return 10;
    }

    @Override
    public int getManaPerLevel() {
        return 1;
    }


    @Override
    public ArmorClass getArmorClass() {
        return ARMORCLASS;
    }

    @Override
    public Item getUnlockItem() {
        return ModItems.sunicon;
    }

    private static class DiggerArmorClass extends ArmorClass {
        @Override
        public boolean canWear(ItemArmor.ArmorMaterial material) {
            return true;
        }
    }
}
