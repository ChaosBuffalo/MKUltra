package com.chaosbuffalo.mkultra.core.classes;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.ArmorClass;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.BaseClass;
import com.chaosbuffalo.mkultra.core.abilities.*;
import com.chaosbuffalo.mkultra.init.ModItems;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacob on 3/25/2018.
 */
public class WaveKnight extends BaseClass {

    private static final List<PlayerAbility> abilities = new ArrayList<>(5);

    static {
        abilities.add(new Whirlpool());
        abilities.add(new WaveDash());
        abilities.add(new Underway());
        abilities.add(new HeavingSeas());
        abilities.add(new WaveBreak());
    }

    public WaveKnight() {
        super(MKUltra.MODID, "class.wave_knight", "Wave Knight");
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
        return 14;
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
        return ModItems.moon_icon;
    }

}

