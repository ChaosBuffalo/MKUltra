package com.chaosbuffalo.mkultra.init;


import com.chaosbuffalo.mkcore.MKCoreRegistry;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class MKUAbilities {

    public static final DeferredRegister<MKAbility> REGISTRY = DeferredRegister.create(MKCoreRegistry.ABILITY_REGISTRY_NAME, MKUltra.MODID);
    //misc
    public static final net.minecraftforge.registries.RegistryObject<WrathBeamAbility> WRATH_BEAM = REGISTRY.register("ability.wrath_beam", WrathBeamAbility::new);
    public static net.minecraftforge.registries.RegistryObject<WrathBeamFlurryAbility> WRATH_BEAM_FLURRY = REGISTRY.register("ability.wrath_beam_flurry", WrathBeamFlurryAbility::new);
    public static final net.minecraftforge.registries.RegistryObject<SeverTendonAbility> SEVER_TENDON = REGISTRY.register("ability.sever_tendon", SeverTendonAbility::new);
    public static final net.minecraftforge.registries.RegistryObject<FireballAbility> FIREBALL = REGISTRY.register("ability.fireball", FireballAbility::new);
    public static final RegistryObject<ShadowPulseFlurryAbility> SHADOW_PUlSE_FLURRY = REGISTRY.register("ability.shadow_pulse_flurry", ShadowPulseFlurryAbility::new);

    //necromancer
    public static final net.minecraftforge.registries.RegistryObject<MKEntitySummonAbility> TEST_SUMMON = REGISTRY.register("ability.test_summon",
            () -> new MKEntitySummonAbility(new ResourceLocation(MKUltra.MODID, "hyborean_sorcerer_queen"), MKAttributes.NECROMANCY));
    public static final net.minecraftforge.registries.RegistryObject<ShadowPulseAbility> SHADOW_PULSE = REGISTRY.register("ability.shadow_pulse", ShadowPulseAbility::new);
    public static final net.minecraftforge.registries.RegistryObject<ShadowBoltAbility> SHADOW_BOLT = REGISTRY.register("ability.shadow_bolt", ShadowBoltAbility::new);
    public static final RegistryObject<LifeSpikeAbility> LIFE_SPIKE = REGISTRY.register("ability.life_spike", LifeSpikeAbility::new);
    public static final net.minecraftforge.registries.RegistryObject<EngulfingDarknessAbility> ENGULFING_DARKNESS = REGISTRY.register("ability.engulfing_darkness", EngulfingDarknessAbility::new);

    // nethermage
    public static final RegistryObject<EmberAbility> EMBER = REGISTRY.register("ability.ember", EmberAbility::new);
    public static final RegistryObject<FireArmorAbility> FIRE_ARMOR = REGISTRY.register("ability.fire_armor", FireArmorAbility::new);
    public static final net.minecraftforge.registries.RegistryObject<FlameWaveAbility> FLAME_WAVE = REGISTRY.register("ability.flame_wave", FlameWaveAbility::new);
    public static final net.minecraftforge.registries.RegistryObject<IgniteAbility> IGNITE = REGISTRY.register("ability.ignite", IgniteAbility::new);
    public static final net.minecraftforge.registries.RegistryObject<WarpCurseAbility> WARP_CURSE = REGISTRY.register("ability.warp_curse", WarpCurseAbility::new);

    //green knight
    public static final net.minecraftforge.registries.RegistryObject<SpiritBombAbility> SPIRIT_BOMB = REGISTRY.register("ability.spirit_bomb", SpiritBombAbility::new);
    public static final RegistryObject<SkinLikeWoodAbility> SKIN_LIKE_WOOD = REGISTRY.register("ability.skin_like_wood", SkinLikeWoodAbility::new);
    public static final net.minecraftforge.registries.RegistryObject<NaturesRemedyAbility> NATURES_REMEDY = REGISTRY.register("ability.natures_remedy", NaturesRemedyAbility::new);
    public static final net.minecraftforge.registries.RegistryObject<ExplosiveGrowthAbility> EXPLOSIVE_GROWTH = REGISTRY.register("ability.explosive_growth", ExplosiveGrowthAbility::new);
    public static final RegistryObject<CleansingSeedAbility> CLEANSING_SEED = REGISTRY.register("ability.cleansing_seed", CleansingSeedAbility::new);

    //cleric
    public static final net.minecraftforge.registries.RegistryObject<SmiteAbility> SMITE = REGISTRY.register("ability.smite", SmiteAbility::new);
    public static final net.minecraftforge.registries.RegistryObject<PowerWordSummonAbility> POWER_WORD_SUMMON = REGISTRY.register("ability.power_word_summon", PowerWordSummonAbility::new);
    public static final net.minecraftforge.registries.RegistryObject<InspireAbility> INSPIRE = REGISTRY.register("ability.inspire", InspireAbility::new);
    public static final net.minecraftforge.registries.RegistryObject<HealAbility> HEAL = REGISTRY.register("ability.heal", HealAbility::new);
    public static final RegistryObject<GalvanizeAbility> GALVANIZE = REGISTRY.register("ability.galvanize", GalvanizeAbility::new);

    //brawler
    public static final net.minecraftforge.registries.RegistryObject<YaupAbility> YAUP = REGISTRY.register("ability.yaup", YaupAbility::new);
    public static final RegistryObject<YankAbility> YANK = REGISTRY.register("ability.yank", YankAbility::new);
    public static final net.minecraftforge.registries.RegistryObject<WhirlwindBladesAbility> WHIRLWIND_BLADES = REGISTRY.register("ability.whirlwind_blades", WhirlwindBladesAbility::new);
    public static final net.minecraftforge.registries.RegistryObject<StunningShoutAbility> STUNNING_SHOUT = REGISTRY.register("ability.stunning_shout", StunningShoutAbility::new);
    public static final RegistryObject<FuriousBroodingAbility> FURIOUS_BROODING = REGISTRY.register("ability.furious_brooding", FuriousBroodingAbility::new);

    //Wet Wizard
    public static final RegistryObject<DrownAbility> DROWN = REGISTRY.register("ability.drown", DrownAbility::new);

    //talents
    public static final net.minecraftforge.registries.RegistryObject<LifeSiphonAbility> LIFE_SIPHON = REGISTRY.register("ability.life_siphon", LifeSiphonAbility::new);
    public static final net.minecraftforge.registries.RegistryObject<SoulDrainAbility> SOUL_DRAIN = REGISTRY.register("ability.soul_drain", SoulDrainAbility::new);


    public static void register() {
        REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
