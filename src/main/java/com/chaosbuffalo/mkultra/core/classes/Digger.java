package com.chaosbuffalo.mkultra.core.classes;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.core.abilities.*;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class Digger extends PlayerClass {
    public static ResourceLocation ID = new ResourceLocation(MKUltra.MODID, "class.digger");

    public static final List<PlayerAbility> abilities = new ArrayList<>(5);

    static {
        abilities.add(new GoldenOpportunity());
        abilities.add(new HopeBread());
        abilities.add(new PierceTheHeavens());
        abilities.add(new TNTWhisperer());
        abilities.add(new LavaWanderer());
    }

    public Digger() {
        super(ID);
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
        return ArmorClass.ALL;
    }

    @Override
    public IClassClientData getClientData() {
        return ClassClientData.SunIcon.INSTANCE;
    }
}
