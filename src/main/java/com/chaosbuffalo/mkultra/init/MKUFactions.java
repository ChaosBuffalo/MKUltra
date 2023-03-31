package com.chaosbuffalo.mkultra.init;

import com.chaosbuffalo.mkfaction.faction.FactionConstants;
import com.chaosbuffalo.mkfaction.faction.MKFaction;
import com.chaosbuffalo.mkfaction.init.Factions;
import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.resources.ResourceLocation;
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
    public static final ResourceLocation SEE_OF_SOLANG_NAME = new ResourceLocation(MKUltra.MODID,
            "see_of_solang");
    public static final ResourceLocation GHOSTS_OF_HYBORIA_NAME = new ResourceLocation(MKUltra.MODID,
            "ghosts_of_hyboria");
    public static final ResourceLocation NETHER_MAGE_NAME = new ResourceLocation(MKUltra.MODID,
            "nether_mages");
    public static final ResourceLocation NECROTIDE_CULTISTS_NAME = new ResourceLocation(MKUltra.MODID,
            "necrotide_cultists");

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
        factions.add(NECROTIDE_CULTISTS_NAME);
        return factions;
    }

    @ObjectHolder("mkultra:green_knights")
    public static MKFaction GREEN_KNIGHTS_FACTION;

    @ObjectHolder("mkultra:hyborean_dead")
    public static MKFaction HYBOREAN_DEAD;

    @ObjectHolder("mkultra:imperial_dead")
    public static MKFaction IMPERIAL_DEAD;

    @ObjectHolder("mkultra:see_of_solang")
    public static MKFaction SEE_OF_SOLANG;

    @ObjectHolder("mkultra:ghosts_of_hyboria")
    public static MKFaction GHOSTS_OF_HYBORIA;

    @ObjectHolder("mkultra:nether_mages")
    public static MKFaction NETHER_MAGES;

    @ObjectHolder("mkultra:necrotide_cultists")
    public static MKFaction NECROTIDE_CULTISTS;


    @SubscribeEvent
    public static void registerFactions(RegistryEvent.Register<MKFaction> event) {
        MKFaction greenKnightFaction = new MKFaction(GREEN_KNIGHT_FACTION_NAME,
                FactionConstants.FRIENDLY_THRESHOLD,
                getDefaultGoodFactionSet(),
                getDefaultBadFactionSet()
        );
        greenKnightFaction.addEnemy(HYBOREAN_DEAD_NAME);
        greenKnightFaction.addEnemy(IMPERIAL_DEAD_NAME);
        greenKnightFaction.addEnemy(NECROTIDE_CULTISTS_NAME);
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
        setupRomanNames(imperialDeadFaction);
        event.getRegistry().register(imperialDeadFaction);
        MKFaction seeFaction = new MKFaction(SEE_OF_SOLANG_NAME,
                FactionConstants.FRIENDLY_THRESHOLD,
                getDefaultGoodFactionSet(),
                getDefaultBadFactionSet());
        seeFaction.addAlly(GREEN_KNIGHT_FACTION_NAME);
        seeFaction.addEnemy(HYBOREAN_DEAD_NAME);
        seeFaction.addEnemy(IMPERIAL_DEAD_NAME);
        setupItalianFirstNames(seeFaction);
        event.getRegistry().register(seeFaction);
        MKFaction ghosts = new MKFaction(GHOSTS_OF_HYBORIA_NAME,
                FactionConstants.TRUE_NEUTRAL);
        event.getRegistry().register(ghosts);
        MKFaction nether_mages = new MKFaction(NETHER_MAGE_NAME,
                FactionConstants.FRIENDLY_THRESHOLD, getDefaultGoodFactionSet(), getDefaultBadFactionSet());
        setupMongolianNames(nether_mages);
        event.getRegistry().register(nether_mages);
        MKFaction necrotideCultists = new MKFaction(NECROTIDE_CULTISTS_NAME,
                FactionConstants.ENEMY_THRESHOLD, new HashSet<>(), getDefaultGoodFactionSet());
        setupMongolianNames(necrotideCultists);
        event.getRegistry().register(necrotideCultists);
    }

    private static void setupMongolianNames(MKFaction faction){
        String[] names = {
                "A'uchu","Achiq-shirun","Adarkidai","Ajai","Alaq","Alaqush-digit-quri","Alchidai","Alchiq","Altan","Altun-ashuq",
                "Amal","Ambaqai","Aqutai","Arajan","Arasen","Arqai-qasar","Arslan","Asha-gambu","Ashiq","Ba'aridai","Badai","Bala",
                "Balaqachi","Barqudai","Batachi","Batu","Bedu'un","Bekter","Belgunutei","Belgutei","Bilge","Bo'orchu","Bodonchar",
                "Borjigidai","Boro'ul","Boroldai","Buch-aran","Bugidei","Bugunutei","Bujek","Bulaqadar","Buqa","Buqatai","Buqatu-salji",
                "Buqu-qadagi","Buri","Burqan","Buyiruq","Cha'adai","Cha'urqai","Cha'urqan","Chanai","Chanar","Chaqa'an-qo'a","Chaqu'au",
                "Chaqurqan","Charaqa","Charaqa-linqu","Checheyigen","Chigidei","Chila'un","Chiledu","Chilger","Chilgutei","Chimbai",
                "Chinggis","Choji-darmala","Chormaqan","Da'aritai","Daritai","Dayyir","Degei","Dei","Dobun","Dodai","Doqolqu","Dorbei",
                "Duwa","Eljigedei","Emel","Erke-qara","Gu'un","Guchu","Guchugur","Guchuluk","Gur","Gurin","Guyigunek","Guyuk","Harqasun",
                "Hobogetur","Horqudaq","Idoqudai","Idu'ut","Ile","Iluge","Inalchi","Inancha","Iturgen","Jajiradai","Jalayirtai","Jamuqa",
                "Janggi","Jaqa","Jaqa-gambu","Jarchi'udai","Jebe","Jebke","Jedei","Jegu","Jelme","Jetei","Jewuredei","Jirqo'adai","Jochi",
                "Jungsai","Jurchedei","Kete","Kiratai","Kishiliq","Kogse'u-sabraq","Kokochu","Kuchu","Lablaqa","Mangqutai","Megujin","Menen",
                "Monggetu","Mongke","Mongke'ur","Monglik","Monke","Morichi","Mulke","Mulqalqu","Muqali","Nachin","Naqu","Narin-ke'en","Naya'a",
                "Nilqa-senggum","Ogodei","Ogole","Ogolei","Okin","Oldaqar","Onggur","Oqda","Oqotur","Otchigin","Otchigui","Qa'atai","Qabichi",
                "Qabul","Qachi","Qachi'u","Qachi'un","Qachin","Qada","Qada'an","Qadac","Qadai","Qadaq","Qaidu","Qali'udar","Qara'udar","Qarachar",
                "Qaraldai","Qarchu","Qarqai","Qasar","Qongqai","Qongqortai","Qongtaqar","Qorchi","Qori'aqachar","Qori-shilemun-taishi","Qori-subechi",
                "Qoridai","Qorilartai","Qorqasun","Qubilai","Quchar","Qudu","Quduqa","Qudus","Qulbari-quri","Qunan","Quri-shilemun","Qutu","Qutula",
                "Quyildar","Sacha","Senggum","Senggum-bilqe","Shidurqu","Shigi-qutuqu","Shigiken","Shiki'ur","Shirgu'etu","Soqor","Sorqan-shira",
                "Soyiketu","Sube'etei","Subegei","Sugegei-je'un","Sukegei","Taichar","Taichu","Taqai","Tarqutai","Tarqutai-kiriltuq","Tayang",
                "Teb-tenggeri","Telegetu","Temuder","Temuge","Temuge-otchigin","Temujin","Temujin-uje","Temur","Tenggeri","Terge","To'oril",
                "Todo'en-girte","Togus","Tolui","Tolun","Toqto'a","Toquchar","Torbi-tash","Torolchi","Toroquoljin","Tuge","Tumbinai","Tusaqa",
                "Ui'urtai","Unggur","Uquna","Usun","Yadir","Yalbaq","Yedi-tubluq","Yegei","Yegu","Yeke-cheren","Yeke-ne'urin","Yisuder",
                "Yisugei","Yisun-te'e","Yisungge"
        };
        for (String name : names){
            faction.addFirstName(name);
        }
    }

    private static void setupItalianFirstNames(MKFaction faction){
        String[] names = {
                "Bodo","Bonauito","Bonaventura","Bonfilio","Bonizo","Brizio","Bruno","Callisto","Calogero","Camaino",
                "Cambio","Camillo","Carlito","Carlo","Cesare","Cipriano","Ciro","Claudio","Columbano","Constanzo","Coppo",
                "Corfino","Corrado","Cosimo","Cristoforo","Curzio","Damiano","Dario","Daniele","Demetrio","Dino","Dionigi",
                "Domenico","Donato","Eduardo","Efisio","Elio","Elpidio","Emiliano","Ennio","Enrico","Enzio","Enzo","Erberto",
                "Ercole","Ermanno","Ettore","Eugenio","Eustachio","Evaristo","Ezzo","Fabiano","Fabio","Federico","Felice",
                "Feliciano","Ferdinando","Ferrando","Ferranti","Ferrucio","Filiberto","Fiorenzo","Francesco","Franco","Frederico",
                "Fulvio","Furio","Gabriel","Gabriele","Galeazzo","Galileo","Galimberto","Garibaldo","Gaspare","Gavino","Geronimo",
                "Giacomo","Giambattista","Giampaolo","Giampietro","Gian","Gianfranco","Gianlorenzo","Gianluca","Gianluigi",
                "Gianni","Gianpiero","Gilberto","Gino","Giorgio","Giotto","Giovane","Giovanni","Giraldo","Giuliano","Giulio",
                "Giuseppe","Giusto","Goffredo","Gregorio","Gualberto","Gualtieri","Guglielmo","Guido","Guilio","Iachimo",
                "Ignazio","Ilario","Italo","Jacopo","Javier","Lando","Leandro","Leonardo","Leone","Leopoldo","Lorenzo",
                "Lotario","Luca","Luciano","Lucio","Ludovico","Luigi","Macario","Manfredo","Marcellino","Marcello",
                "Marco","Marcovaldo","Mariano","Mario","Massimiliano","Massimo","Matteo","Maurizio","Mauro","Melchiorre",
                "Mercurino","Michelangelo","Michele","Modesto","Moreno","Nardo","Nataniele","Nazario","Nero",
                "Nestore","Niccolo","Nicodemo","Nicola","Nicoletto","Nunzio","Odilon","Onofrio","Orfeo","Orlando","Pacifico",
                "Palmiro","Pancrazio","Pao","Paolo","Pasquale","Pellegrino","Peppe","Pietro","Ponzio","Primo",
                "Prospero","Quirino","Raffaele","Raffaello","Raimondo","Rainieri","Raniero","Raul","Renzo","Rico",
                "Rinaldo","Roberto","Rocco","Roderico","Rodrigo","Romero","Rossano","Ruffino","Ruggiero","Salvatore",
                "Sandro","Santo","Saverio","Sergio","Severiano","Severino","Silvano","Silvestro","Silvio","Simone",
                "Sperandeo","Stefano","Taddeo","Tammaro","Tancredo","Teodosio","Thaddeo","Timoteo","Tino","Tito",
                "Tiziano","Tommaso","Tomme","Torquato","Toto","Tullio","Ubaldo","Ugo","Ugolino","Umberto","Valentino",
                "Vasco","Vespasiano","Viaro","Vincenzo","Virgilio","Vitale","Vito","Viviano","Xaverio","Adalgisa",
                "Addolorata","Adriana","Agata","Agnesca","Agnese","Alessandra","Alessia","Aletea","Allegra",
                "Amadea","Amalia","Amelia","Amica","Amina","Ammanata","Angela","Angelica","Aniella","Anna",
                "AnnaMaria","Annabella","Annunziata","Antonia","Argento","Arianna","Armida","Arminia","Artemisia",
                "Assunta","Assuntina","Aurea","Aurelia","Barbara","Bartolomaea","Beatrice","Benedetta","Bertana",
                "Bertilla","Bettina","Bianca","Bibiana","Bice","Brigita","Brunella","Camilla","Caprice","Carissa",
                "Carita","Carlotta","Carmela","Caterina","Cecilia","Cella","Cesira","Chiara","Christina","Cinzia",
                "Citha","Claricia","Claudia","Clementia","Conccetta","Consilia","Consolata","Costante","Cristina",
                "Daniela","Deborah","Delfina","Detta","Diana","Domenica","Domitilla","Donata","Donatella","Dora",
                "Edetta","Elda","Eleonora","Elettra","Eliana","Elisa","Elisabetta","Eloisa","Elvira","Emerenzia",
                "Emilia","Enrichetta","Esmeralda","Etheria","Eugenia","Eusapia","Eva","Evelina","Fabia","Fabiola",
                "Fabrizia","Federica","Felicita","Fiametta","Fiammetta","Filomena","Fiora","Fiorella","Fiorenza",
                "Fioretta","Franca","Francesca","Gaetana","Gemma","Giachetta","Giada","Gianetta","Gianna","Giannina",
                "Gilberta","Gilda","Gina","Ginevra","Gioconda","Gisela","Gisella","Giulia","Giuliana","Giulietta",
                "Giuseppina","Gravina","Grazia","Graziella","Griselda","Illaria","Ilvia","Imelda","Immacolata",
                "Innocenza","Isabella","Isolde","Jolanda","Julitta","Jutta","Lara","Laura","Lauretta","Lavinia",
                "Lea","Leda","Lena","Leonara","Leontina","Letizia","Lia","Lidia","Liliana","Lisa","Lisabetta",
                "Lisetta","Lizia","Loredana","Loretta","Luana","Lucia","Luciella","Lucretzia","Luigina","Luisa",
                "Maddelena","Mafalda","Maia","Mara","Marcella","Margherita","Maria","Mariella","Marietta","Marisa",
                "Maura","Melina","Melissa","Michela","Miuccia","Monica","Morena","Natalia","Nerina","Nicoletta",
                "Nina","Noemi","Nunziatella","Nunziatina","Olimpia","Olinda","Oriana","Ornella","Ornetta","Orsola",
                "Pamela","Paola","Paolina","Patrizia","Pelagia","Perla","Perna","Petronilla","Pia","Piacenza","Pina",
                "Porzia","Prada","Prasede","Pudenziana","Rachele","Raimonda","Renata","Rina","Rita","Romilda","Rosa",
                "Rosalba","Rosalia","Rosangela","Rosetta","Rosina","Sabina","Sabrina","Samantha","Sandra","Santuzza",
                "Sara","Savina","Scevola","Serafina","Serena","Silvana","Silvia","Simona","Simonetta","Sitha","Smeralda",
                "Sofia","Sophonsiba","Stefania","Stefanina","Susanna", "Teodora","Teresina","Theresa","Tita","Tiziana",
                "Tommasa","Tommasina","Tullia","Valencia","Vanozza","Venezia","Veronica","Viola","Violante","Vittoria",
                "Viviana","Zaira","Zita"
        };
        for (String name : names){
            faction.addFirstName(name);
        }
    }

    private static void setupRomanNames(MKFaction faction){
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
