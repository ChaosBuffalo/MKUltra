package com.chaosbuffalo.mkultra.core.classes;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.core.abilities.archer.*;
import com.chaosbuffalo.mkultra.init.ModPlayerClasses;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;


@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class Archer extends PlayerClass {
    public static ResourceLocation ID = new ResourceLocation(MKUltra.MODID, "class.archer");
    public static final Archer INSTANCE = new Archer();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<PlayerClass> event) {
        event.getRegistry().register(INSTANCE.setRegistryName(INSTANCE.getClassId()));
    }

    public static final List<PlayerAbility> abilities = new ArrayList<>(5);

    static {
        abilities.add(FlintHound.INSTANCE);
        abilities.add(FireArrow.INSTANCE);
        abilities.add(PoisonArrow.INSTANCE);
        abilities.add(Repulse.INSTANCE);
        abilities.add(ArrowStorm.INSTANCE);
    }

    public Archer() {
        super(ID);
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
    public IClassClientData getClientData() {
        return ModPlayerClasses.sunIcon;
    }
}
