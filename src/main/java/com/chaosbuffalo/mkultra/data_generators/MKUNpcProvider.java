package com.chaosbuffalo.mkultra.data_generators;


import com.chaosbuffalo.mkcore.abilities.training.requirements.HasEntitlementRequirement;
import com.chaosbuffalo.mkcore.client.rendering.skeleton.BipedSkeleton;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.fx.particles.effect_instances.BoneEffectInstance;
import com.chaosbuffalo.mkfaction.init.Factions;
import com.chaosbuffalo.mknpc.data.NpcDefinitionProvider;
import com.chaosbuffalo.mknpc.entity.boss.BossStage;
import com.chaosbuffalo.mknpc.npc.NpcAttributeEntry;
import com.chaosbuffalo.mknpc.npc.NpcDefinition;
import com.chaosbuffalo.mknpc.npc.NpcItemChoice;
import com.chaosbuffalo.mknpc.npc.entries.LootOptionEntry;
import com.chaosbuffalo.mknpc.npc.options.*;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.abilities.brawler.FuriousBroodingAbility;
import com.chaosbuffalo.mkultra.abilities.brawler.WhirlwindBladesAbility;
import com.chaosbuffalo.mkultra.abilities.brawler.YankAbility;
import com.chaosbuffalo.mkultra.abilities.cleric.*;
import com.chaosbuffalo.mkultra.abilities.green_knight.*;
import com.chaosbuffalo.mkultra.abilities.misc.*;
import com.chaosbuffalo.mkultra.abilities.necromancer.ShadowBoltAbility;
import com.chaosbuffalo.mkultra.abilities.nether_mage.*;
import com.chaosbuffalo.mkultra.abilities.wet_wizard.DrownAbility;
import com.chaosbuffalo.mkultra.client.render.styling.*;
import com.chaosbuffalo.mkultra.init.*;
import com.chaosbuffalo.mkweapons.items.randomization.slots.LootSlotManager;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.UUID;

public class MKUNpcProvider extends NpcDefinitionProvider {


    public MKUNpcProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    public void run(@Nonnull HashCache cache) {
        writeDefinition(generateGreenLady(), cache);
        writeDefinition(generateGreenLadyGuard1(), cache);
        writeDefinition(generateGreenLadyGuard2(), cache);
        writeDefinition(generateHyboreanWarrior(), cache);
        writeDefinition(generateHyboreanHonorGuard(), cache);
        writeDefinition(generateHyboreanArcher(), cache);
        writeDefinition(generateHyboreanSorcerer(), cache);
        writeDefinition(generateAncientKing(), cache);
        writeDefinition(generateHyboreanSorcererQueen(), cache);
        writeDefinition(generateCrumblingTrooper(), cache);
        writeDefinition(generateCrumblingTrooperMage(), cache);
        writeDefinition(generateDecayingZombieArcher(), cache);
        writeDefinition(generateDecayingZombiePiglin(), cache);
        writeDefinition(generateGreenSmith(), cache);
        writeDefinition(generateImperialMagus(), cache);
        writeDefinition(generateTrooperCaptain(), cache);
        writeDefinition(generateTrooperExecution(), cache);
        writeDefinition(generateSkeletalTrooperMage(), cache);
        writeDefinition(generateBurningSkeleton(), cache);
        writeDefinition(generateClericAcolyte(), cache);
        writeDefinition(generateClericApprentice(), cache);
        writeDefinition(generateForlornGhost(), cache);
        writeDefinition(generateNetherMageInitiate(), cache);
        writeDefinition(generateTempleGuard(), cache);
        writeDefinition(generateTempleGuard2(), cache);
        writeDefinition(generateCleric(), cache);
        writeDefinition(generateNecrotideCultistAcolyte(), cache);
        writeDefinition(generateNecrotideCultist(), cache);
        writeDefinition(generateSkeletalLock(), cache);
        writeDefinition(generateNecrotideGolem(), cache);
    }

    private NpcDefinition generateSkeletalLock() {
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "skeletal_lock"),
                MKUEntities.HYBOREAN_SKELETON_TYPE.getId(), null);
        def.addOption(new FactionOption().setValue(MKUFactions.NECROTIDE_CULTISTS_NAME));
        def.addOption(new RenderGroupOption().setValue(MKUSkeletons.BASIC_NAME));
        def.addOption(new MKSizeOption().setValue(1.0f));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 50.0)));
        def.addOption(new NameOption().setValue("A Skeletal Lock"));
        def.addOption(new AbilitiesOption()
                .withAbilityOption(MKUAbilities.SHADOW_BOLT.get(), 1, 1.0)
        );
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.MAGE));
        def.addOption(new NotableOption());
        return def;
    }

    private NpcDefinition generateNecrotideGolem() {
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "necrotide_golem"),
                MKUEntities.GOLEM_TYPE.get().getRegistryName(), null);
        def.addOption(new FactionOption().setValue(MKUFactions.NECROTIDE_CULTISTS_NAME));
        def.addOption(new RenderGroupOption().setValue(MKUGolems.NECROTIDE_GOLEM_NAME));
        def.addOption(new MKSizeOption().setValue(1.25f));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 500.0)));
        def.addOption(new NameOption().setValue("A Necrotide Construction"));
        def.addOption(new AbilitiesOption()
                .withAbilityOption(MKUAbilities.SHADOW_BOLT.get(), 1, 1.0)
        );
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.NECROMANCER));
        def.addOption(new NotableOption());
        return def;
    }

    private NpcDefinition generateNecrotideCultistAcolyte() {
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "necrotide_acolyte"),
                MKUEntities.HUMAN_TYPE.getId(), null);
        def.addOption(new FactionOption().setValue(MKUFactions.NECROTIDE_CULTISTS_NAME));
        def.addOption(new RenderGroupOption().setValue(MKUHumans.NECROTIDE_CULTIST_1_NAME));
        def.addOption(new MKSizeOption().setValue(0.95f));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 50.0)));
        def.addOption(new NameOption().setValue("A Necrotide Acolyte"));
        def.addOption(new AbilitiesOption()
                .withAbilityOption(MKUAbilities.SHADOW_BOLT.get(), 1, 1.0)
                .withAbilityOption(MKUAbilities.DROWN.get(), 2, 0.5)
                .withAbilityOption(MKUAbilities.SHADOW_PULSE.get(), 3, 0.5)
        );
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.MAGE));
        return def;
    }

    private NpcDefinition generateNecrotideCultist() {
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "necrotide_cultist"),
                MKUEntities.HUMAN_TYPE.getId(), null);
        def.addOption(new FactionOption().setValue(MKUFactions.NECROTIDE_CULTISTS_NAME));
        def.addOption(new RenderGroupOption().setValue(MKUHumans.NECROTIDE_CULTIST_SKULL_1_NAME));
        def.addOption(new MKSizeOption().setValue(1.05f));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 125.0)));
        def.addOption(new FactionNameOption().setTitle("Cultist"));
        def.addOption(new AbilitiesOption()
                .withAbilityOption(MKUAbilities.SHADOW_BOLT.get(), 1, 1.0)
                .withAbilityOption(MKUAbilities.DROWN.get(), 2, 1.0)
                .withAbilityOption(MKUAbilities.SHADOW_PULSE.get(), 3, 1.0)
                .withAbilityOption(MKUAbilities.TEST_SUMMON.get(), 4, 1.0)
        );
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.NECROMANCER));
        return def;
    }

    private NpcDefinition generateClericAcolyte(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "solangian_acolyte"),
                MKUEntities.HUMAN_TYPE.getId(), null);
        def.addOption(new FactionOption().setValue(MKUFactions.SEE_OF_SOLANG_NAME));
        def.addOption(new RenderGroupOption().setValue(MKUHumans.CLERIC_1_NAME));
        def.addOption(new MKSizeOption().setValue(1.05f));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 150.0)));
        def.addOption(new FactionNameOption().setTitle("Acolyte"));
        def.addOption(new NotableOption());
        def.addOption(new AbilitiesOption()
                .withAbilityOption(MKUAbilities.HEAL.get(), 1, 1.0)
                .withAbilityOption(MKUAbilities.SMITE.get(), 2, 1.0)
                .withAbilityOption(MKUAbilities.GALVANIZE.get(), 3, 1.0)
        );
        def.addOption(new DialogueOption().setValue(new ResourceLocation(MKUltra.MODID, "intro_cleric_acolyte")));
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:mace_iron"))), 1.0, 0.0f));
        def.addOption(new QuestOfferingOption(new ResourceLocation("mkultra", "cleric_intro")));
        def.addOption(new AbilityTrainingOption()
                        .withTrainingOption(MKUAbilities.HEAL.get(), new HasEntitlementRequirement(MKUEntitlements.IntroClericTier1))
                        .withTrainingOption(MKUAbilities.SMITE.get(), new HasEntitlementRequirement(MKUEntitlements.IntroClericTier1))
        );
        def.addOption(equipOption);
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.CLERIC));
        return def;
    }

    private NpcDefinition generateCleric(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "solangian_cleric"),
                MKUEntities.HUMAN_TYPE.getId(), null);
        def.addOption(new FactionOption().setValue(MKUFactions.SEE_OF_SOLANG_NAME));
        def.addOption(new RenderGroupOption().setValue(MKUHumans.CLERIC_1_NAME));
        def.addOption(new MKSizeOption().setValue(1.05f));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 500.0)));
        def.addOption(new FactionNameOption().setTitle("Cleric"));
        def.addOption(new NotableOption());
        def.addOption(new AbilitiesOption()
                .withAbilityOption(MKUAbilities.HEAL.get(), 1, 1.0)
                .withAbilityOption(MKUAbilities.SMITE.get(), 2, 1.0)
                .withAbilityOption(MKUAbilities.GALVANIZE.get(), 3, 1.0)
                .withAbilityOption(MKUAbilities.POWER_WORD_SUMMON.get(), 4, 1.0)
                .withAbilityOption(MKUAbilities.INSPIRE.get(), 5, 1.0)
        );
//        def.addOption(new DialogueOption().setValue(new ResourceLocation(MKUltra.MODID, "intro_cleric_acolyte")));
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:mace_gold"))), 1.0, 0.0f));
//        def.addOption(new QuestOfferingOption(new ResourceLocation("mkultra", "cleric_intro")));
        def.addOption(new AbilityTrainingOption()
                .withTrainingOption(MKUAbilities.HEAL.get(), new HasEntitlementRequirement(MKUEntitlements.ClericTier1))
                .withTrainingOption(MKUAbilities.SMITE.get(), new HasEntitlementRequirement(MKUEntitlements.ClericTier1))
                .withTrainingOption(MKUAbilities.GALVANIZE.get(), new HasEntitlementRequirement(MKUEntitlements.ClericTier2))
                .withTrainingOption(MKUAbilities.POWER_WORD_SUMMON.get(), new HasEntitlementRequirement(MKUEntitlements.ClericTier2))
                .withTrainingOption(MKUAbilities.INSPIRE.get(), new HasEntitlementRequirement(MKUEntitlements.ClericTier3))
        );
        def.addOption(equipOption);
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.CLERIC));
        return def;
    }

    private NpcDefinition generateForlornGhost(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "forlorn_ghost"),
                MKUEntities.HUMAN_TYPE.getId(), null);
        def.addOption(new FactionOption().setValue(MKUFactions.SEE_OF_SOLANG_NAME));
        def.addOption(new RenderGroupOption().setValue(MKUHumans.GHOST_1_NAME));
        def.addOption(new MKSizeOption().setValue(1.0f));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 100.0)));
        def.addOption(new NameOption().setValue("Forlorn Ghost"));
        def.addOption(new NotableOption());
        def.addOption(new GhostOption().setValue(1.0f));
//        def.addOption(new AbilitiesOption()
//                .withAbilityOption(HealAbility.INSTANCE, 1, 1.0)
//                .withAbilityOption(SmiteAbility.INSTANCE, 2, 1.0)
//        );
//        EquipmentOption equipOption = new EquipmentOption();
//        equipOption.addItemChoice(EquipmentSlotType.MAINHAND,
//                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
//                        new ResourceLocation("mkweapons:mace_iron"))), 1.0, 0.0f));
//        def.addOption(equipOption);
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.FIGHTER));
        return def;
    }

    private NpcDefinition generateTempleGuard2() {
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "solangian_temple_guard_2"),
                MKUEntities.HUMAN_TYPE.getId(), null);
        def.addOption(new FactionOption().setValue(MKUFactions.SEE_OF_SOLANG_NAME));
        def.addOption(new RenderGroupOption().setValue(MKUHumans.TEMPLE_GUARD_2_NAME));
        def.addOption(new MKSizeOption().setValue(1.0f));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 300.0)));
        def.addOption(new FactionNameOption().setTitle("Temple Guard"));
        def.addOption(new NotableOption());
        def.addOption(new AbilitiesOption()
                .withAbilityOption(MKUAbilities.HEAL.get(), 1, 1.0)
                .withAbilityOption(MKUAbilities.SMITE.get(), 2, 1.0)
                .withAbilityOption(MKUAbilities.SEVER_TENDON.get(), 3, 1.0)
        );
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:spear_gold"))), 1.0, 0.0f));
        def.addOption(equipOption);
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.PALADIN));
        return def;
    }

    private NpcDefinition generateTempleGuard() {
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "solangian_temple_guard"),
                MKUEntities.HUMAN_TYPE.getId(), null);
        def.addOption(new FactionOption().setValue(MKUFactions.SEE_OF_SOLANG_NAME));
        def.addOption(new RenderGroupOption().setValue(MKUHumans.TEMPLE_GUARD_1_NAME));
        def.addOption(new MKSizeOption().setValue(1.0f));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 250.0)));
        def.addOption(new FactionNameOption().setTitle("Temple Guard"));
        def.addOption(new NotableOption());
        def.addOption(new AbilitiesOption()
                .withAbilityOption(MKUAbilities.HEAL.get(), 1, 1.0)
                .withAbilityOption(MKUAbilities.SMITE.get(), 2, 1.0)
        );
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:spear_gold"))), 1.0, 0.0f));
        def.addOption(equipOption);
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.PALADIN));
        return def;
    }

    private NpcDefinition generateClericApprentice(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "solangian_apprentice"),
                MKUEntities.HUMAN_TYPE.getId(), null);
        def.addOption(new FactionOption().setValue(MKUFactions.SEE_OF_SOLANG_NAME));
        def.addOption(new RenderGroupOption().setValue(MKUHumans.CLERIC_2_NAME));
        def.addOption(new MKSizeOption().setValue(0.85f));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 100.0)));
        def.addOption(new FactionNameOption().setTitle("Apprentice"));
        def.addOption(new NotableOption());
        def.addOption(new AbilitiesOption()
                .withAbilityOption(MKUAbilities.HEAL.get(), 1, 1.0)
                .withAbilityOption(MKUAbilities.SMITE.get(), 2, 1.0)
        );
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:mace_iron"))), 1.0, 0.0f));
        def.addOption(equipOption);
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.CLERIC));
        return def;
    }

    private NpcDefinition generateNetherMageInitiate(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "nether_mage_initiate"),
                MKUEntities.HUMAN_TYPE.getId(), null);
        def.addOption(new FactionOption().setValue(MKUFactions.NETHER_MAGE_NAME));
        def.addOption(new RenderGroupOption().setValue(MKUHumans.NETHER_MAGE_1_NAME));
        def.addOption(new MKSizeOption().setValue(0.90f));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 150.0)));
        def.addOption(new FactionNameOption().setTitle("Initiate"));
        def.addOption(new NotableOption());
        def.addOption(new AbilitiesOption()
                .withAbilityOption(MKUAbilities.EMBER.get(), 1, 1.0)
                .withAbilityOption(MKUAbilities.FIREBALL.get(), 2, 1.0)
                .withAbilityOption(MKUAbilities.FLAME_WAVE.get(), 3, 1.0)
        );
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:staff_wood"))), 1.0, 0.0f));
        def.addOption(equipOption);
        def.addOption(new AbilityTrainingOption()
                .withTrainingOption(MKUAbilities.EMBER.get(), new HasEntitlementRequirement(MKUEntitlements.IntroNetherMageTier1))
                .withTrainingOption(MKUAbilities.FIRE_ARMOR.get(), new HasEntitlementRequirement(MKUEntitlements.IntroNetherMageTier1))
        );
        def.addOption(new DialogueOption().setValue(new ResourceLocation(MKUltra.MODID, "intro_nether_mage_initiate")));
        def.addOption(new QuestOfferingOption(new ResourceLocation("mkultra", "nether_mage_intro")));
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.MAGE));
        return def;
    }

    private NpcDefinition generateBurningSkeleton(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "burning_skeleton"),
                MKUEntities.HYBOREAN_SKELETON_TYPE.getId(), null);
        def.addOption(new FactionOption().setValue(MKUFactions.HYBOREAN_DEAD_NAME));
        def.addOption(new MKSizeOption().setValue(1.0f));
        def.addOption(new RenderGroupOption().setValue(MKUSkeletons.BURNING_NAME));
        def.addOption(new AttributesOption()
                .addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 250.0))
                .addAttributeEntry(new NpcAttributeEntry(Attributes.ARMOR, 10.0))
                .addAttributeEntry(new NpcAttributeEntry(Attributes.ATTACK_DAMAGE, 5.0))
                .addAttributeEntry(new NpcAttributeEntry(MKAttributes.BLEED_RESISTANCE, 1.25))
        );
        def.addOption(new NameOption().setValue("Burning Revenant"));
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:dagger_stone"))), 1.0, 0.0f));
        def.addOption(new NotableOption());
        def.addOption(equipOption);
        def.addOption(new BossStageOption()
                .withStage(new BossStage()
                        .withOption(new TempAbilitiesOption()
                                .withAbilityOption(MKUAbilities.FIRE_ARMOR.get(), 3, 1.0)
                                .withAbilityOption(MKUAbilities.FIREBALL.get(), 2, 1.0)
                                .withAbilityOption(MKUAbilities.WRATH_BEAM.get(), 1, 1.0))
//                        .withOption(new ParticleEffectsOption().withEffects(Collections.singletonList(
//                                new BoneEffectInstance(UUID.fromString("3e7496f1-f5bf-45e6-b8e5-64192633ae9f"),
//                                        BipedSkeleton.HEAD_BONE_NAME, new ResourceLocation(MKUltra.MODID, "flame_wave_casting")))))
                )
                .withStage(new BossStage()
                        .withOption(new TempAbilitiesOption()
                                .withAbilityOption(MKUAbilities.FIREBALL.get(), 3, 1.0)
                                .withAbilityOption(MKUAbilities.WRATH_BEAM_FLURRY.get(), 1, 1.0))
//                        .withOption(new ParticleEffectsOption().withEffects(Collections.singletonList(
//                                new BoneEffectInstance(UUID.fromString("e45696e1-ddb1-4709-bc29-1733ee1bced9"),
//                                BipedSkeleton.HEAD_BONE_NAME, new ResourceLocation(MKUltra.MODID, "flame_wave_casting")))))
                        .withParticleMode(BossStage.ParticleMode.LINE_HEIGHT)
                        .withTransitionParticles(new ResourceLocation(MKUltra.MODID, "wrath_skeleton_transition"))
                        .withTransitionSound(ModSounds.spell_dark_8.getRegistryName())
                )
        );
        def.addOption(new ExperienceOption().setValue(50));
        def.addOption(new ParticleEffectsOption().withEffects(Collections.singletonList(
                new BoneEffectInstance(UUID.fromString("3e7496f1-f5bf-45e6-b8e5-64192633ae9f"),
                        BipedSkeleton.HEAD_BONE_NAME, new ResourceLocation(MKUltra.MODID, "burning_skeleton_head"))
        )));
        ResourceLocation lootTierName = new ResourceLocation(MKUltra.MODID, "burning_skeleton");
        def.addOption(new ExtraLootOption().withLootOptions(new LootOptionEntry(LootSlotManager.MAIN_HAND.getName(), lootTierName, 1.0))
                .withLootOptions(new LootOptionEntry(LootSlotManager.RINGS.getName(), lootTierName, 3.0))
                .withLootOptions(new LootOptionEntry(LootSlotManager.EARRINGS.getName(), lootTierName, 2.0))
                .withDropChances(1)
                .withNoLootChance(0.1)
                .withNoLootIncrease(0.0));
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.MAGE));
        return def;
    }

    private NpcDefinition generateDecayingZombiePiglin(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "decaying_piglin"),
                MKUEntities.ZOMBIFIED_PIGLIN_TYPE.getId(), null);
        def.addOption(new FactionOption().setValue(MKUFactions.IMPERIAL_DEAD_NAME));
        def.addOption(new MKSizeOption().setValue(0.9f));
        def.addOption(new RenderGroupOption().setValue(MKUPiglins.ZOMBIE_PIG_NAME));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 20.0)));
        def.addOption(new NameOption().setValue("Decaying Zombie"));
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:dagger_stone"))), 1.0, 0.0f));
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(ItemStack.EMPTY, 1.0, 0.0f));
        def.addOption(equipOption);
        def.addOption(new ExperienceOption().setValue(5));
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.FIGHTER));
        return def;
    }

    private NpcDefinition generateDecayingZombieArcher(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "decaying_piglin_archer"),
                MKUEntities.ZOMBIFIED_PIGLIN_TYPE.getId(), null);
        def.addOption(new FactionOption().setValue(MKUFactions.IMPERIAL_DEAD_NAME));
        def.addOption(new MKSizeOption().setValue(0.85f));
        def.addOption(new RenderGroupOption().setValue(MKUPiglins.ZOMBIE_PIG_NAME));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 15.0)));
        def.addOption(new NameOption().setValue("Shambling Archer"));
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(Items.BOW), 1.0, 0.0f));
        def.addOption(equipOption);
        def.addOption(new ExperienceOption().setValue(5));
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.ARCHER));
        return def;
    }

    private NpcDefinition generateCrumblingTrooperMage(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "crumbling_trooper_mage"),
                MKUEntities.ZOMBIFIED_PIGLIN_TYPE.getId(), null);
        def.addOption(new FactionOption().setValue(MKUFactions.IMPERIAL_DEAD_NAME));
        def.addOption(new MKSizeOption().setValue(0.95f));
        def.addOption(new RenderGroupOption().setValue(MKUPiglins.ZOMBIE_PIG_MAGUS_NAME));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 30.0)));
        def.addOption(new NameOption().setValue("Crumbling Trooper Mage"));
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:dagger_stone"))), 1.0, 0.0f));
        def.addOption(equipOption);
        def.addOption(new AbilitiesOption()
                .withAbilityOption(MKUAbilities.FIREBALL.get(), 1, 1.0)
                .withAbilityOption(MKUAbilities.EMBER.get(), 2, 0.5)
        );
        ResourceLocation lootTierName = new ResourceLocation(MKUltra.MODID, "zombie_trooper");
        def.addOption(new ExtraLootOption()
                .withLootOptions(new LootOptionEntry(LootSlotManager.ITEMS.getName(), lootTierName, 1.0))
                .withDropChances(2)
                .withNoLootChance(0.1)
                .withNoLootIncrease(0.25)
        );
        def.addOption(new ExperienceOption().setValue(10));
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.MAGE));
        return def;
    }

    private NpcDefinition generateTrooperExecution(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "trooper_executioner"),
                MKUEntities.ZOMBIFIED_PIGLIN_TYPE.getId(), null);
        def.addOption(new FactionOption().setValue(MKUFactions.IMPERIAL_DEAD_NAME));
        def.addOption(new MKSizeOption().setValue(1.0f));
        def.addOption(new RenderGroupOption().setValue(MKUPiglins.DESTROYED_SKELETAL_TROOPER_NAME));
        def.addOption(new MKComboSettingsOption().setComboDelay(20).setComboCount(3));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 180.0)));
        def.addOption(new NameOption().setValue("Trooper Executioner"));
        def.addOption(new LungeSpeedOption().setValue(0.75));
        def.addOption(new NotableOption());
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:battleaxe_stone"))), 1.0, 0.0f));
        def.addOption(equipOption);
        def.addOption(new AbilitiesOption()
                .withAbilityOption(MKUAbilities.FURIOUS_BROODING.get(), 3, 1.0)
                .withAbilityOption(MKUAbilities.WHIRLWIND_BLADES.get(), 1, 1.0)
                .withAbilityOption(MKUAbilities.YANK.get(), 2, 1.0)
        );
        ResourceLocation lootTierName = new ResourceLocation(MKUltra.MODID, "trooper_executioner");
        def.addOption(new ExtraLootOption()
                .withLootOptions(new LootOptionEntry(LootSlotManager.MAIN_HAND.getName(), lootTierName, 1.0))
                .withLootOptions(new LootOptionEntry(LootSlotManager.RINGS.getName(), lootTierName, 3.0))
                .withDropChances(2)
                .withNoLootChance(0.2)
                .withNoLootIncrease(0.25)
        );
        def.addOption(new ExperienceOption().setValue(20));
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.FIGHTER));
        return def;
    }

    private NpcDefinition generateImperialMagus(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "imperial_magus"),
                MKUEntities.ZOMBIFIED_PIGLIN_TYPE.getId(), null);
        def.addOption(new FactionOption().setValue(MKUFactions.IMPERIAL_DEAD_NAME));
        def.addOption(new MKSizeOption().setValue(1.0f));
        def.addOption(new RenderGroupOption().setValue(MKUPiglins.SKELETAL_MAGE_NAME));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 150.0)));
        def.addOption(new FactionNameOption().setTitle("Imperial Magus").setHasLastName(true));
        def.addOption(new NotableOption());
        def.addOption(new LungeSpeedOption().setValue(0.5));
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:mace_stone"))), 1.0, 0.0f));
        def.addOption(equipOption);
        def.addOption(new AbilitiesOption()
                .withAbilityOption(MKUAbilities.FIREBALL.get(), 3, 1.0)
                .withAbilityOption(MKUAbilities.FLAME_WAVE.get(), 6, 1.0)
                .withAbilityOption(MKUAbilities.SMITE.get(), 4, 1.0)
                .withAbilityOption(MKUAbilities.FIRE_ARMOR.get(), 5, 1.0)
                .withAbilityOption(MKUAbilities.HEAL.get(), 1, 1.0)
                .withAbilityOption(MKUAbilities.NATURES_REMEDY.get(), 2, 1.0)
                .withAbilityOption(MKUAbilities.POWER_WORD_SUMMON.get(), 7, 1.0)
        );
        ResourceLocation lootTierName = new ResourceLocation(MKUltra.MODID, "trooper_magus");
        def.addOption(new ExtraLootOption()
                .withLootOptions(new LootOptionEntry(LootSlotManager.MAIN_HAND.getName(), lootTierName, 1.0))
                .withLootOptions(new LootOptionEntry(LootSlotManager.RINGS.getName(), lootTierName, 2.0))
                .withLootOptions(new LootOptionEntry(LootSlotManager.EARRINGS.getName(), lootTierName, 1.0))
                .withDropChances(2)
                .withNoLootChance(0.2)
                .withNoLootIncrease(0.25)
        );
        def.addOption(new ExperienceOption().setValue(25));
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.MAGE));
        return def;
    }

    private NpcDefinition generateTrooperCaptain(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "trooper_captain"),
                MKUEntities.ZOMBIFIED_PIGLIN_TYPE.getId(), null);
        def.addOption(new FactionOption().setValue(MKUFactions.IMPERIAL_DEAD_NAME));
        def.addOption(new MKSizeOption().setValue(1.2f));
        def.addOption(new RenderGroupOption().setValue(MKUPiglins.SKELETAL_TROOPER_NAME));
        def.addOption(new MKComboSettingsOption().setComboDelay(10).setComboCount(3));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 100.0)));
        def.addOption(new FactionNameOption().setTitle("Captain").setHasLastName(true));
        def.addOption(new LungeSpeedOption().setValue(1.0));
        def.addOption(new NotableOption());
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:greatsword_stone"))), 1.0, 0.0f));
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:katana_stone"))), 1.0, 0.0f));
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:warhammer_stone"))), 1.0, 0.0f));
        def.addOption(equipOption);
        def.addOption(new AbilitiesOption()
                .withAbilityOption(MKUAbilities.HEAL.get(), 2, 1.0)
                .withAbilityOption(MKUAbilities.FURIOUS_BROODING.get(), 3, 1.0)
                .withAbilityOption(MKUAbilities.SMITE.get(), 1, 1.0)
        );
        ResourceLocation lootTierName = new ResourceLocation(MKUltra.MODID, "trooper_captain");
        def.addOption(new ExtraLootOption()
                .withLootOptions(new LootOptionEntry(LootSlotManager.MAIN_HAND.getName(), lootTierName, 1.0))
                .withLootOptions(new LootOptionEntry(LootSlotManager.EARRINGS.getName(), lootTierName, 3.0))
                .withDropChances(2)
                .withNoLootChance(0.2)
                .withNoLootIncrease(0.25)
        );
        def.addOption(new ExperienceOption().setValue(25));
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.PALADIN));
        return def;
    }

    private NpcDefinition generateSkeletalTrooperMage(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "skeletal_trooper_mage"),
                MKUEntities.ZOMBIFIED_PIGLIN_TYPE.getId(), null);
        def.addOption(new FactionOption().setValue(MKUFactions.IMPERIAL_DEAD_NAME));
        def.addOption(new MKSizeOption().setValue(1.0f));
        def.addOption(new RenderGroupOption().setValue(MKUPiglins.DESTROYED_SKELETAL_MAGE_NAME));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 50.0)));
        def.addOption(new NameOption().setValue("Skeletal Magus"));
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:dagger_stone"))), 1.0, 0.0f));
        def.addOption(equipOption);
        def.addOption(new AbilitiesOption()
                .withAbilityOption(MKUAbilities.FIREBALL.get(), 1, 1.0)
                .withAbilityOption(MKUAbilities.EMBER.get(), 2, 0.75)
                .withAbilityOption(MKUAbilities.FLAME_WAVE.get(), 3, 0.5)
                .withAbilityOption(MKUAbilities.SMITE.get(), 4, 0.25)
                .withAbilityOption(MKUAbilities.FIRE_ARMOR.get(), 5, 0.75)
        );
//        ResourceLocation lootTierName = new ResourceLocation(MKUltra.MODID, "zombie_trooper");
//        ResourceLocation templateName = new ResourceLocation(MKUltra.MODID, "empty");
//        def.addOption(new ExtraLootOption()
//                .withLootOptions(new LootOptionEntry(LootSlotManager.ITEMS.getName(), lootTierName, templateName, 1.0))
//                .withDropChances(2)
//                .withNoLootChance(0.1)
//                .withNoLootIncrease(0.25)
//        );
        def.addOption(new ExperienceOption().setValue(15));
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.MAGE));
        return def;
    }

    private NpcDefinition generateCrumblingTrooper(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "crumbling_trooper"),
                MKUEntities.ZOMBIFIED_PIGLIN_TYPE.getId(), null);
        def.addOption(new FactionOption().setValue(MKUFactions.IMPERIAL_DEAD_NAME));
        def.addOption(new MKSizeOption().setValue(1.1f));
        def.addOption(new RenderGroupOption().setValue(MKUPiglins.ZOMBIE_PIG_TROOPER_NAME));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 50.0)));
        def.addOption(new NameOption().setValue("Crumbling Trooper"));
        def.addOption(new LungeSpeedOption().setValue(0.35));
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:longsword_stone"))), 1.0, 0.0f));
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:warhammer_stone"))), 1.0, 0.0f));
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:mace_stone"))), 1.0, 0.0f));
        def.addOption(equipOption);
        def.addOption(new AbilitiesOption()
                .withAbilityOption(MKUAbilities.SEVER_TENDON.get(), 3, 1.0)
                .withAbilityOption(MKUAbilities.EMBER.get(), 2, 0.5)
        );
        ResourceLocation lootTierName = new ResourceLocation(MKUltra.MODID, "zombie_trooper");
        def.addOption(new ExtraLootOption()
                .withLootOptions(new LootOptionEntry(LootSlotManager.ITEMS.getName(), lootTierName, 1.0))
                .withDropChances(2)
                .withNoLootChance(0.1)
                .withNoLootIncrease(0.25)
        );
        def.addOption(new ExperienceOption().setValue(10));
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.FIGHTER));
        return def;
    }


    private NpcDefinition generateHyboreanWarrior(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "hyborean_warrior"),
                MKUEntities.HYBOREAN_SKELETON_TYPE.getId(), null);
        def.addOption(new FactionOption().setValue(Factions.UNDEAD_FACTION_NAME));
        def.addOption(new MKSizeOption().setValue(1.0f));
        def.addOption(new RenderGroupOption().setValue(MKUSkeletons.HYBOREAN_WARRIOR_NAME));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 30.0)));
        def.addOption(new NameOption().setValue("Hyborean Warrior"));
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:battleaxe_stone"))), 1.0, 0.0f));
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:spear_stone"))), 1.0, 0.0f));
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:greatsword_stone"))), 1.0, 0.0f));
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:longsword_stone"))), 1.0, 0.0f));
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:warhammer_stone"))), 1.0, 0.0f));
        def.addOption(equipOption);
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.WARRIOR));
        return def;
    }

    private NpcDefinition generateHyboreanArcher(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "hyborean_archer"),
                MKUEntities.HYBOREAN_SKELETON_TYPE.getId(), null);
        def.addOption(new FactionOption().setValue(Factions.UNDEAD_FACTION_NAME));
        def.addOption(new MKSizeOption().setValue(0.95f));
        def.addOption(new RenderGroupOption().setValue(MKUSkeletons.HYBOREAN_ARCHER_NAME));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 25.0)));
        def.addOption(new NameOption().setValue("Decaying Archer"));
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:longbow_stone"))), 1.0, 0.0f));
        def.addOption(equipOption);
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.ARCHER));
        return def;
    }

    private NpcDefinition generateHyboreanSorcererQueen(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "hyborean_sorcerer_queen"),
                MKUEntities.HYBOREAN_SKELETON_TYPE.getId(), null);
        def.addOption(new FactionOption().setValue(Factions.UNDEAD_FACTION_NAME));
        def.addOption(new MKSizeOption().setValue(1.1f));
        def.addOption(new RenderGroupOption().setValue(MKUSkeletons.SORCERER_QUEEN_NAME));
        def.addOption(new AttributesOption()
                .addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 110.0))
                .addAttributeEntry(new NpcAttributeEntry(MKAttributes.EVOCATION, 3))
        );
        def.addOption(new NameOption().setValue("Hyborean Sorcerer Queen"));
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:katana_iron"))), 1.0, 0.0f));
        def.addOption(equipOption);
        def.addOption(new AbilitiesOption()
                .withAbilityOption(MKUAbilities.FIRE_ARMOR.get(), 5, 1.0)
                .withAbilityOption(MKUAbilities.FIREBALL.get(), 6, 1.0)
                .withAbilityOption(MKUAbilities.EMBER.get(), 1, 1.0)
                .withAbilityOption(MKUAbilities.IGNITE.get(), 2, 0.5)
                .withAbilityOption(MKUAbilities.FLAME_WAVE.get(), 3, 1.0)
                .withAbilityOption(MKUAbilities.WARP_CURSE.get(), 4, 0.5)
        );
        def.addOption(new MKComboSettingsOption().setComboCount(5).setComboDelay(60));
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.MAGE));
        return def;
    }

    private NpcDefinition generateAncientKing(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "an_ancient_king"),
                MKUEntities.HYBOREAN_SKELETON_TYPE.getId(), null);
        def.addOption(new FactionOption().setValue(Factions.UNDEAD_FACTION_NAME));
        def.addOption(new MKSizeOption().setValue(1.15f));
        def.addOption(new RenderGroupOption().setValue(MKUSkeletons.ANCIENT_KING_NAME));
        def.addOption(new AttributesOption()
                .addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 165.0))
                .addAttributeEntry(new NpcAttributeEntry(MKAttributes.EVOCATION, 2))
                .addAttributeEntry(new NpcAttributeEntry(MKAttributes.RESTORATION, 1))
                .addAttributeEntry(new NpcAttributeEntry(MKAttributes.PANKRATION, 2))
        );
        def.addOption(new NameOption().setValue("An Ancient King"));
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:battleaxe_iron"))), 1.0, 0.0f));
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:greatsword_iron"))), 1.0, 0.0f));
        def.addOption(equipOption);
        def.addOption(new AbilitiesOption()
                .withAbilityOption(MKUAbilities.NATURES_REMEDY.get(), 2, 1.0)
                .withAbilityOption(MKUAbilities.SEVER_TENDON.get(), 3, 1.0)
                .withAbilityOption(MKUAbilities.HEAL.get(), 1, 1.0)
                .withAbilityOption(MKUAbilities.POWER_WORD_SUMMON.get(), 4, 0.5)
                .withAbilityOption(MKUAbilities.EXPLOSIVE_GROWTH.get(), 5, 0.5)
                .withAbilityOption(MKUAbilities.FIREBALL.get(), 6, 0.5)

        );
        def.addOption(new MKComboSettingsOption().setComboCount(2).setComboDelay(10));
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.PALADIN));
        return def;
    }

    private NpcDefinition generateHyboreanSorcerer(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "hyborean_sorcerer"),
                MKUEntities.HYBOREAN_SKELETON_TYPE.getId(), null);
        def.addOption(new FactionOption().setValue(Factions.UNDEAD_FACTION_NAME));
        def.addOption(new MKSizeOption().setValue(0.9f));
        def.addOption(new RenderGroupOption().setValue(MKUSkeletons.SORCERER_NAME));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 40.0)));
        def.addOption(new NameOption().setValue("Hyborean Sorcerer"));
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:dagger_stone"))), 1.0, 0.0f));
        def.addOption(equipOption);
        def.addOption(new AbilitiesOption()
                .withAbilityOption(MKUAbilities.FIRE_ARMOR.get(), 2, 0.5)
                .withAbilityOption(MKUAbilities.FIREBALL.get(), 1, 1.0)
                .withAbilityOption(MKUAbilities.EMBER.get(), 3, 1.0)
        );
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.MAGE));
        return def;
    }

    private NpcDefinition generateHyboreanHonorGuard(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "hyborean_honor_guard"),
                MKUEntities.HYBOREAN_SKELETON_TYPE.getId(), null);
        def.addOption(new FactionOption().setValue(Factions.UNDEAD_FACTION_NAME));
        def.addOption(new MKSizeOption().setValue(1.0f));
        def.addOption(new RenderGroupOption().setValue(MKUSkeletons.HONOR_GUARD_NAME));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 65.0)));
        def.addOption(new NameOption().setValue("Undying Honor Guard"));
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:battleaxe_iron"))), 1.0, 0.0f));
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:greatsword_iron"))), 1.0, 0.0f));
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:warhammer_iron"))), 1.0, 0.0f));
        def.addOption(equipOption);
        def.addOption(new AbilitiesOption()
                .withAbilityOption(MKUAbilities.NATURES_REMEDY.get(), 2, 1.0)
                .withAbilityOption(MKUAbilities.SEVER_TENDON.get(), 3, 1.0)
        );
        def.addOption(new MKComboSettingsOption().setComboCount(4).setComboDelay(30));
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.PALADIN));
        return def;
    }

    private NpcDefinition generateGreenSmith(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "green_smith"),
                MKUEntities.ORC_TYPE.getId(), null);
        def.addOption(new FactionOption().setValue(MKUFactions.GREEN_KNIGHT_FACTION_NAME));
        def.addOption(new MKSizeOption().setValue(1.5f));
        def.addOption(new RenderGroupOption().setValue(MKUOrcs.GREEN_SMITH_NAME));
        def.addOption(new AbilitiesOption()
                .withAbilityOption(MKUAbilities.SKIN_LIKE_WOOD.get(), 1, 1.0)
                .withAbilityOption(MKUAbilities.NATURES_REMEDY.get(), 2, 1.0)
        );
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 400.0)));
        def.addOption(new NameOption().setValue("Green Smith"));
        def.addOption(new NotableOption());
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:warhammer_iron"))), 1.0, 0.0f));
        def.addOption(equipOption);
        def.addOption(new QuestOfferingOption(new ResourceLocation("mkultra", "trooper_armor")));
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.PALADIN));
        return def;
    }


    private NpcDefinition generateGreenLady(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "green_lady"),
                MKUEntities.ORC_TYPE.getId(), null);
        def.addOption(new FactionOption().setValue(MKUFactions.GREEN_KNIGHT_FACTION_NAME));
        def.addOption(new MKSizeOption().setValue(1.1f));
        def.addOption(new RenderGroupOption().setValue(MKUOrcs.GREEN_LADY_NAME));
        def.addOption(new AbilityTrainingOption()
                .withTrainingOption(MKUAbilities.SKIN_LIKE_WOOD.get(), new HasEntitlementRequirement(MKUEntitlements.GreenKnightTier1))
                .withTrainingOption(MKUAbilities.NATURES_REMEDY.get(), new HasEntitlementRequirement(MKUEntitlements.GreenKnightTier1))
                .withTrainingOption(MKUAbilities.SPIRIT_BOMB.get(), new HasEntitlementRequirement(MKUEntitlements.GreenKnightTier2))
                .withTrainingOption(MKUAbilities.CLEANSING_SEED.get(), new HasEntitlementRequirement(MKUEntitlements.GreenKnightTier2))
                .withTrainingOption(MKUAbilities.EXPLOSIVE_GROWTH.get(), new HasEntitlementRequirement(MKUEntitlements.GreenKnightTier3))
        );
        def.addOption(new AbilitiesOption()
                .withAbilityOption(MKUAbilities.SKIN_LIKE_WOOD.get(), 1, 1.0)
                .withAbilityOption(MKUAbilities.NATURES_REMEDY.get(), 2, 1.0)
                .withAbilityOption(MKUAbilities.SPIRIT_BOMB.get(), 4, 1.0)
                .withAbilityOption(MKUAbilities.NATURES_REMEDY.get(), 3, 1.0)
                .withAbilityOption(MKUAbilities.CLEANSING_SEED.get(), 5, 1.0)
        );
        def.addOption(new DialogueOption().setValue(new ResourceLocation(MKUltra.MODID, "open_abilities")));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 400.0)));
        def.addOption(new NameOption().setValue("Green Lady"));
        def.addOption(new NotableOption());
        def.addOption(new QuestOfferingOption(new ResourceLocation("mkultra", "intro_quest")));
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.CLERIC));
        return def;
    }

    private NpcDefinition generateGreenLadyGuard2(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "green_lady_guard_2"),
                MKUEntities.ORC_TYPE.getId(), null);
        def.addOption(new FactionOption().setValue(MKUFactions.GREEN_KNIGHT_FACTION_NAME));
        def.addOption(new MKSizeOption().setValue(1.1f));
        def.addOption(new RenderGroupOption().setValue(MKUOrcs.GREEN_LADY_GUARD_2_NAME));
        def.addOption(new AbilitiesOption()
                .withAbilityOption(MKUAbilities.SKIN_LIKE_WOOD.get(), 1, 1.0)
                .withAbilityOption(MKUAbilities.NATURES_REMEDY.get(), 2, 1.0)
                .withAbilityOption(MKUAbilities.SPIRIT_BOMB.get(), 4, 1.0)
                .withAbilityOption(MKUAbilities.EXPLOSIVE_GROWTH.get(), 3, 1.0)
                .withAbilityOption(MKUAbilities.CLEANSING_SEED.get(), 5, 1.0)
        );
//        def.addOption(new DialogueOption().setValue(new ResourceLocation(MKUltra.MODID, "open_abilities")));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 150.0)));
        def.addOption(new NameOption().setValue("Green Guardian"));
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlot.HEAD,
                new NpcItemChoice(new ItemStack(MKUItems.greenKnightHelmet), 1.0, 0.05f));
        equipOption.addItemChoice(EquipmentSlot.CHEST,
                new NpcItemChoice(new ItemStack(MKUItems.greenKnightChestplate), 1.0, 0.05f));
        equipOption.addItemChoice(EquipmentSlot.LEGS,
                new NpcItemChoice(new ItemStack(MKUItems.greenKnightLeggings), 1.0, 0.05f));
        equipOption.addItemChoice(EquipmentSlot.FEET,
                new NpcItemChoice(new ItemStack(MKUItems.greenKnightBoots), 1.0, 0.05f));
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:dagger_stone"))), 1.0, 0.0f));
        def.addOption(equipOption);
        def.addOption(new NotableOption());
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.PALADIN));
        return def;
    }

    private NpcDefinition generateGreenLadyGuard1(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "green_lady_guard_1"),
                MKUEntities.ORC_TYPE.getId(), null);
        def.addOption(new FactionOption().setValue(MKUFactions.GREEN_KNIGHT_FACTION_NAME));
        def.addOption(new MKSizeOption().setValue(1.1f));
        def.addOption(new RenderGroupOption().setValue(MKUOrcs.GREEN_LADY_GUARD_1_NAME));
        def.addOption(new AbilitiesOption()
                .withAbilityOption(MKUAbilities.SKIN_LIKE_WOOD.get(), 1, 1.0)
                .withAbilityOption(MKUAbilities.NATURES_REMEDY.get(), 2, 1.0)
                .withAbilityOption(MKUAbilities.SPIRIT_BOMB.get(), 4, 1.0)
                .withAbilityOption(MKUAbilities.EXPLOSIVE_GROWTH.get(), 3, 1.0)
                .withAbilityOption(MKUAbilities.CLEANSING_SEED.get(), 5, 1.0)
        );
//        def.addOption(new DialogueOption().setValue(new ResourceLocation(MKUltra.MODID, "open_abilities")));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 150.0)));
        def.addOption(new NameOption().setValue("Green Knight"));
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlot.HEAD,
                new NpcItemChoice(new ItemStack(MKUItems.greenKnightHelmet), 1.0, 0.05f));
        equipOption.addItemChoice(EquipmentSlot.CHEST,
                new NpcItemChoice(new ItemStack(MKUItems.greenKnightChestplate), 1.0, 0.05f));
        equipOption.addItemChoice(EquipmentSlot.LEGS,
                new NpcItemChoice(new ItemStack(MKUItems.greenKnightLeggings), 1.0, 0.05f));
        equipOption.addItemChoice(EquipmentSlot.FEET,
                new NpcItemChoice(new ItemStack(MKUItems.greenKnightBoots), 1.0, 0.05f));
        equipOption.addItemChoice(EquipmentSlot.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:battleaxe_stone"))), 1.0, 0.0f));
        def.addOption(equipOption);
        def.addOption(new NotableOption());
        def.addOption(MKUNpcGenUtils.GetSkillOptionForClass(MKUNpcGenUtils.NpcSkillClass.PALADIN));
        return def;
    }

    @Override
    public String getName() {
        return "MKU NPC GEN";
    }
}
