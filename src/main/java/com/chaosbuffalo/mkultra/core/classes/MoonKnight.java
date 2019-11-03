package com.chaosbuffalo.mkultra.core.classes;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.core.abilities.moon_knight.*;
import com.chaosbuffalo.mkultra.init.ModPlayerClasses;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class MoonKnight extends PlayerClass {
    public static ResourceLocation ID = new ResourceLocation(MKUltra.MODID, "class.moon_knight");
    public static final MoonKnight INSTANCE = new MoonKnight();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<PlayerClass> event) {
        event.getRegistry().register(INSTANCE.setRegistryName(INSTANCE.getClassId()));
    }

    private static final List<PlayerAbility> abilities = new ArrayList<>(5);

    static {
        abilities.add(VampiricRevere.INSTANCE);
        abilities.add(CrescentSlash.INSTANCE);
        abilities.add(NocturnalCommunion.INSTANCE);
        abilities.add(DualityRune.INSTANCE);
        abilities.add(MoonTrance.INSTANCE);
    }

    public MoonKnight() {
        super(ID);
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
        return 12;
    }

    @Override
    public int getManaPerLevel() {
        return 1;
    }

    @Override
    public ArmorClass getArmorClass() {
        return ArmorClass.HEAVY;
    }

    @Override
    public IClassClientData getClientData() {
        return ModPlayerClasses.moonIcon;
    }
}
