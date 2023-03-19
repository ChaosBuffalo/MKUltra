package com.chaosbuffalo.mkultra.init;


import com.chaosbuffalo.mkcore.abilities.MKAbility;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.abilities.brawler.*;
import com.chaosbuffalo.mkultra.abilities.cleric.*;
import com.chaosbuffalo.mkultra.abilities.green_knight.*;
import com.chaosbuffalo.mkultra.abilities.misc.*;
import com.chaosbuffalo.mkultra.abilities.necromancer.EngulfingDarknessAbility;
import com.chaosbuffalo.mkultra.abilities.necromancer.LifeSpikeAbility;
import com.chaosbuffalo.mkultra.abilities.necromancer.ShadowBoltAbility;
import com.chaosbuffalo.mkultra.abilities.necromancer.ShadowPulseAbility;
import com.chaosbuffalo.mkultra.abilities.nether_mage.*;
import com.chaosbuffalo.mkultra.abilities.passives.LifeSiphonAbility;
import com.chaosbuffalo.mkultra.abilities.passives.SoulDrainAbility;
import com.chaosbuffalo.mkultra.abilities.wet_wizard.DrownAbility;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

public class MKUAbilities {

    public static final DeferredRegister<MKAbility> ABILITIES = DeferredRegister.create(MKAbility.class, MKUltra.MODID);
    //misc
    public static final RegistryObject<WrathBeamAbility> WRATH_BEAM = ABILITIES.register("ability.wrath_beam", WrathBeamAbility::new);
    public static RegistryObject<WrathBeamFlurryAbility> WRATH_BEAM_FLURRY = ABILITIES.register("ability.wrath_beam_flurry", WrathBeamFlurryAbility::new);
    public static final RegistryObject<SeverTendonAbility> SEVER_TENDON = ABILITIES.register("ability.sever_tendon", SeverTendonAbility::new);
    public static final RegistryObject<FireballAbility> FIREBALL = ABILITIES.register("ability.fireball", FireballAbility::new);

    //necromancer
    public static final RegistryObject<MKEntitySummonAbility> TEST_SUMMON = ABILITIES.register("ability.test_summon",
            () -> new MKEntitySummonAbility(new ResourceLocation(MKUltra.MODID, "hyborean_sorcerer_queen"), MKAttributes.NECROMANCY));
    public static final RegistryObject<ShadowPulseAbility> SHADOW_PULSE = ABILITIES.register("ability.shadow_pulse", ShadowPulseAbility::new);
    public static final RegistryObject<ShadowBoltAbility> SHADOW_BOLT = ABILITIES.register("ability.shadow_bolt", ShadowBoltAbility::new);
    public static final RegistryObject<LifeSpikeAbility> LIFE_SPIKE = ABILITIES.register("ability.life_spike", LifeSpikeAbility::new);
    public static final RegistryObject<EngulfingDarknessAbility> ENGULFING_DARKNESS = ABILITIES.register("ability.engulfing_darkness", EngulfingDarknessAbility::new);

    // nethermage
    public static final RegistryObject<EmberAbility> EMBER = ABILITIES.register("ability.ember", EmberAbility::new);
    public static final RegistryObject<FireArmorAbility> FIRE_ARMOR = ABILITIES.register("ability.fire_armor", FireArmorAbility::new);
    public static final RegistryObject<FlameWaveAbility> FLAME_WAVE = ABILITIES.register("ability.flame_wave", FlameWaveAbility::new);
    public static final RegistryObject<IgniteAbility> IGNITE = ABILITIES.register("ability.ignite", IgniteAbility::new);
    public static final RegistryObject<WarpCurseAbility> WARP_CURSE = ABILITIES.register("ability.warp_curse", WarpCurseAbility::new);

    //green knight
    public static final RegistryObject<SpiritBombAbility> SPIRIT_BOMB = ABILITIES.register("ability.spirit_bomb", SpiritBombAbility::new);
    public static final RegistryObject<SkinLikeWoodAbility> SKIN_LIKE_WOOD = ABILITIES.register("ability.skin_like_wood", SkinLikeWoodAbility::new);
    public static final RegistryObject<NaturesRemedyAbility> NATURES_REMEDY = ABILITIES.register("ability.natures_remedy", NaturesRemedyAbility::new);
    public static final RegistryObject<ExplosiveGrowthAbility> EXPLOSIVE_GROWTH = ABILITIES.register("ability.explosive_growth", ExplosiveGrowthAbility::new);
    public static final RegistryObject<CleansingSeedAbility> CLEANSING_SEED = ABILITIES.register("ability.cleansing_seed", CleansingSeedAbility::new);

    //cleric
    public static final RegistryObject<SmiteAbility> SMITE = ABILITIES.register("ability.smite", SmiteAbility::new);
    public static final RegistryObject<PowerWordSummonAbility> POWER_WORD_SUMMON = ABILITIES.register("ability.power_word_summon", PowerWordSummonAbility::new);
    public static final RegistryObject<InspireAbility> INSPIRE = ABILITIES.register("ability.inspire", InspireAbility::new);
    public static final RegistryObject<HealAbility> HEAL = ABILITIES.register("ability.heal", HealAbility::new);
    public static final RegistryObject<GalvanizeAbility> GALVANIZE = ABILITIES.register("ability.galvanize", GalvanizeAbility::new);

    //brawler
    public static final RegistryObject<YaupAbility> YAUP = ABILITIES.register("ability.yaup", YaupAbility::new);
    public static final RegistryObject<YankAbility> YANK = ABILITIES.register("ability.yank", YankAbility::new);
    public static final RegistryObject<WhirlwindBladesAbility> WHIRLWIND_BLADES = ABILITIES.register("ability.whirlwind_blades", WhirlwindBladesAbility::new);
    public static final RegistryObject<StunningShoutAbility> STUNNING_SHOUT = ABILITIES.register("ability.stunning_shout", StunningShoutAbility::new);
    public static final RegistryObject<FuriousBroodingAbility> FURIOUS_BROODING = ABILITIES.register("ability.furious_brooding", FuriousBroodingAbility::new);

    //Wet Wizard
    public static final RegistryObject<DrownAbility> DROWN = ABILITIES.register("ability.drown", DrownAbility::new);

    //talents
    public static final RegistryObject<LifeSiphonAbility> LIFE_SIPHON = ABILITIES.register("ability.life_siphon", LifeSiphonAbility::new);
    public static final RegistryObject<SoulDrainAbility> SOUL_DRAIN = ABILITIES.register("ability.soul_drain", SoulDrainAbility::new);


    public static void register() {
        ABILITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
