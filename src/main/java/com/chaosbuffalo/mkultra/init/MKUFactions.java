package com.chaosbuffalo.mkultra.init;

import com.chaosbuffalo.mkfaction.faction.FactionConstants;
import com.chaosbuffalo.mkfaction.faction.MKFaction;
import com.chaosbuffalo.mkfaction.init.Factions;
import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class MKUFactions {

    public static final ResourceLocation GREEN_KNIGHT_FACTION_NAME = new ResourceLocation(MKUltra.MODID,
            "green_knights");
    public static final ResourceLocation HYBOREAN_DEAD_NAME = new ResourceLocation(MKUltra.MODID,
            "hyborean_dead");
    public static final ResourceLocation IMPERIAL_DEAD_NAME = new ResourceLocation(MKUltra.MODID,
            "imperial_dead");

    protected static Set<ResourceLocation> getDefaultGoodFactionSet(){
        Set<ResourceLocation> factions = new HashSet<>();
        factions.add(Factions.DOMESTICATED_ANIMALS_FACTION_NAME);
        factions.add(Factions.VILLAGER_FACTION_NAME);
        return factions;
    }

    protected static Set<ResourceLocation> getDefaultBadFactionSet(){
        Set<ResourceLocation> factions = new HashSet<>();
        factions.add(Factions.ILLAGERS_FACTION_NAME);
        factions.add(Factions.HOSTILE_ANIMALS_FACTION_NAME);
        factions.add(Factions.UNDEAD_FACTION_NAME);
        factions.add(Factions.MONSTERS_FACTION_NAME);
        return factions;
    }

    @ObjectHolder("mkultra:green_knights")
    public static MKFaction GREEN_KNIGHTS_FACTION;

    @ObjectHolder("mkultra:hyborean_dead")
    public static MKFaction HYBOREAN_DEAD;

    @ObjectHolder("mkultra:imperial_dead")
    public static MKFaction IMPERIAL_DEAD;


    @SubscribeEvent
    public static void registerFactions(RegistryEvent.Register<MKFaction> event) {

        MKFaction greenKnightFaction = new MKFaction(GREEN_KNIGHT_FACTION_NAME,
                FactionConstants.FRIENDLY_THRESHOLD,
                getDefaultGoodFactionSet(),
                getDefaultBadFactionSet()
        );
        greenKnightFaction.addEnemy(HYBOREAN_DEAD_NAME);
        greenKnightFaction.addEnemy(IMPERIAL_DEAD_NAME);
        event.getRegistry().register(greenKnightFaction);
        MKFaction hyboreanDeadFaction = new MKFaction(HYBOREAN_DEAD_NAME,
                FactionConstants.ENEMY_THRESHOLD,
                new HashSet<>(),
                getDefaultGoodFactionSet()
        );
        hyboreanDeadFaction.addEnemy(GREEN_KNIGHT_FACTION_NAME);
        hyboreanDeadFaction.addAlly(Factions.UNDEAD_FACTION_NAME);
        event.getRegistry().register(hyboreanDeadFaction);
        MKFaction imperialDeadFaction = new MKFaction(IMPERIAL_DEAD_NAME,
                FactionConstants.ENEMY_THRESHOLD,
                new HashSet<>(),
                getDefaultGoodFactionSet());
        imperialDeadFaction.addEnemy(GREEN_KNIGHT_FACTION_NAME);
        imperialDeadFaction.addAlly(Factions.UNDEAD_FACTION_NAME);
        setupImperialNames(imperialDeadFaction);
        event.getRegistry().register(imperialDeadFaction);
    }

    private static void setupImperialNames(MKFaction faction){
        String[] names = {
                "Cilla", "Bore", "Muxom", "Cogi",
                "Erir", "Rogi", "Inealbh", "Boge",
                "Iceal", "Uris", "Ruocha", "Cone",
                "Carta", "Nina", "Cumma", "Brisa",
                "Liba", "Bonda", "Cula", "Ulir",
                "Leina", "Dacha", "Cintio", "Briga",
                "Racha", "Ceithla", "Macha", "Eris",
                "Lina", "Duge", "Eabat", "Luage",
                "Carda", "Bodia", "Mana", "Orgreir",
                "Dade", "Liesa", "Muthi", "Derbra",
                "Sabra", "Cospa", "Thuma", "Coni",
                "Ceithla", "Bara", "Noga", "Aglon",
                "Docci", "Mati", "Danu", "Vilbia",
                "Mori", "Brane", "Dica", "Ullos",
                "Soma", "Feba", "Aicawst", "Mare",
                "Sinu", "Canne", "Iogen", "Mola",
                "Erig", "Dane", "Cati", "Sare"
        };
        for (String name : names){
            faction.addFirstName(name);
        }
        String[] lastNames = {
                "I",
                "II",
                "III",
                "IV",
                "V",
                "VI",
                "VII",
                "VIII",
                "IX",
                "X",
                "XI",
                "XII",
                "XIII",
                "XIV",
                "XV",
                "XVI",
                "XVII",
                "XVIII",
                "XIX",
                "XX",
                "XXI",
                "XXII",
                "XXIII",
                "XXIV",
                "XXV",
                "XXVI",
                "XXVII",
                "XXVIII",
                "XXIX",
                "XXX",
                "XXI",
                "XXII"
        };
        for (String name : lastNames){
            faction.addLastName(name);
        }
    }


}
