package com.chaosbuffalo.mkultra.core.classes;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.core.abilities.*;
import com.chaosbuffalo.mkultra.init.ModItems;
import com.chaosbuffalo.mkultra.item.ClassIcon;
import com.chaosbuffalo.mkultra.core.IClassProvider;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class Digger extends PlayerClass {

    public static final List<PlayerAbility> abilities = new ArrayList<>(5);
    private static final ArmorClass ARMORCLASS = new DiggerArmorClass(new ResourceLocation(MKUltra.MODID, "armor_class.digger"));

    static {
        abilities.add(new GoldenOpportunity());
        abilities.add(new HopeBread());
        abilities.add(new PierceTheHeavens());
        abilities.add(new TNTWhisperer());
        abilities.add(new LavaWanderer());
    }

    public Digger() {
        super(MKUltra.MODID, "class.digger");
    }

    @Override
    public List<PlayerAbility> getAbilities() {
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
    public IClassProvider getClassProvider() {
        return (ClassIcon) ModItems.sun_icon;
    }

    @Override
    public IClassClientData getClientData() {
        return ClassClientData.SunIcon.INSTANCE;
    }

    private static class DiggerArmorClass extends ArmorClass {
        public DiggerArmorClass(ResourceLocation location){
            super(location);
        }

        @Override
        public boolean canWear(ItemArmor.ArmorMaterial material) {
            return true;
        }
    }
}
