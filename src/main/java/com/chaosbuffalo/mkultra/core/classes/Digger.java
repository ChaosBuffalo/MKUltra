package com.chaosbuffalo.mkultra.core.classes;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.core.abilities.digger.*;
import com.chaosbuffalo.mkultra.init.ModPlayerClasses;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class Digger extends PlayerClass {
    public static ResourceLocation ID = new ResourceLocation(MKUltra.MODID, "class.digger");
    public static final Digger INSTANCE = new Digger();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<PlayerClass> event) {
        event.getRegistry().register(INSTANCE.setRegistryName(INSTANCE.getClassId()));
    }

    public static final List<PlayerAbility> abilities = new ArrayList<>(5);

    static {
        abilities.add(GoldenOpportunity.INSTANCE);
        abilities.add(HopeBread.INSTANCE);
        abilities.add(PierceTheHeavens.INSTANCE);
        abilities.add(TNTWhisperer.INSTANCE);
        abilities.add(LavaWanderer.INSTANCE);
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
        return ModPlayerClasses.sunIcon;
    }
}
