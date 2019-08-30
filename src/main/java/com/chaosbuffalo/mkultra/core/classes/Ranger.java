package com.chaosbuffalo.mkultra.core.classes;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.ArmorClass;
import com.chaosbuffalo.mkultra.core.IClassClientData;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.PlayerClass;
import com.chaosbuffalo.mkultra.core.abilities.*;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacob on 6/23/2018.
 */
public class Ranger extends PlayerClass {
    public static ResourceLocation ID = new ResourceLocation(MKUltra.MODID, "class.ranger");
    private static ClientData clientData = new ClientData();

    public static final List<PlayerAbility> abilities = new ArrayList<>(5);

    static {
        abilities.add(new PracticedHunter());
        abilities.add(new FairyFire());
        abilities.add(new DesperateSurge());
        abilities.add(new WildToxin());
        abilities.add(new SlayingEdge());
    }

    public Ranger() {
        super(ID);
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
    public int getHealthPerLevel() {
        return 2;
    }

    @Override
    public int getBaseHealth() {
        return 20;
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
        return 2;
    }

    @Override
    public IClassClientData getClientData() {
        return clientData;
    }

    private static class ClientData implements IClassClientData {
        @Override
        public ResourceLocation getIcon() {
            return new ResourceLocation(MKUltra.MODID, "textures/class/icons/ranger.png");
        }

        @Override
        public String getXpTableText() {
            return "The Watchful Ranger taught you the basics, but you must exchange brouzouf to learn more.";
        }

        @Override
        public ResourceLocation getXpTableBackground() {
            return new ResourceLocation(MKUltra.MODID, "textures/gui/xp_table_background_ranger.png");
        }

        @Override
        public int getXpTableTextColor() {
            return 16707252;
        }
    }
}
