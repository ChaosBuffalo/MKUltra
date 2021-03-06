package com.chaosbuffalo.mkultra.data_generators;


import com.chaosbuffalo.mkcore.abilities.MKAbility;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkfaction.init.Factions;
import com.chaosbuffalo.mknpc.data.NpcDefinitionProvider;
import com.chaosbuffalo.mknpc.npc.NpcAttributeEntry;
import com.chaosbuffalo.mknpc.npc.NpcDefinition;
import com.chaosbuffalo.mknpc.npc.NpcItemChoice;
import com.chaosbuffalo.mknpc.npc.options.*;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.abilities.cleric.*;
import com.chaosbuffalo.mkultra.abilities.green_knight.*;
import com.chaosbuffalo.mkultra.abilities.misc.FireballAbility;
import com.chaosbuffalo.mkultra.abilities.misc.SeverTendonAbility;
import com.chaosbuffalo.mkultra.abilities.nether_mage.*;
import com.chaosbuffalo.mkultra.client.render.styling.MKUOrcs;
import com.chaosbuffalo.mkultra.client.render.styling.MKUSkeletons;
import com.chaosbuffalo.mkultra.init.MKUEntities;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class MKUNpcProvider extends NpcDefinitionProvider {


    public MKUNpcProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    public void act(@Nonnull DirectoryCache cache) {
        writeDefinition(generateGreenLady(), cache);
        writeDefinition(generateGreenLadyGuard1(), cache);
        writeDefinition(generateGreenLadyGuard2(), cache);
        writeDefinition(generateHyboreanWarrior(), cache);
        writeDefinition(generateHyboreanHonorGuard(), cache);
        writeDefinition(generateHyboreanArcher(), cache);
        writeDefinition(generateHyboreanSorcerer(), cache);
        writeDefinition(generateAncientKing(), cache);
        writeDefinition(generateHyboreanSorcererQueen(), cache);
    }

    private NpcDefinition generateHyboreanWarrior(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "hyborean_warrior"),
                MKUEntities.HYBOREAN_SKELETON_TYPE.getRegistryName(), null);
        def.addOption(new FactionOption().setValue(Factions.UNDEAD_FACTION_NAME));
        def.addOption(new MKSizeOption().setValue(1.0f));
        def.addOption(new RenderGroupOption().setValue(MKUSkeletons.HYBOREAN_WARRIOR_NAME));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 30.0)));
        def.addOption(new NameOption().setValue("Hyborean Warrior"));
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlotType.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:battleaxe_stone"))), 1.0, 0.0f));
        equipOption.addItemChoice(EquipmentSlotType.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:spear_stone"))), 1.0, 0.0f));
        equipOption.addItemChoice(EquipmentSlotType.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:greatsword_stone"))), 1.0, 0.0f));
        equipOption.addItemChoice(EquipmentSlotType.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:longsword_stone"))), 1.0, 0.0f));
        equipOption.addItemChoice(EquipmentSlotType.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:warhammer_stone"))), 1.0, 0.0f));
        def.addOption(equipOption);
        return def;
    }

    private NpcDefinition generateHyboreanArcher(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "hyborean_archer"),
                MKUEntities.HYBOREAN_SKELETON_TYPE.getRegistryName(), null);
        def.addOption(new FactionOption().setValue(Factions.UNDEAD_FACTION_NAME));
        def.addOption(new MKSizeOption().setValue(0.95f));
        def.addOption(new RenderGroupOption().setValue(MKUSkeletons.HYBOREAN_ARCHER_NAME));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 25.0)));
        def.addOption(new NameOption().setValue("Decaying Archer"));
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlotType.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:longbow_stone"))), 1.0, 0.0f));
        def.addOption(equipOption);
        return def;
    }

    private NpcDefinition generateHyboreanSorcererQueen(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "hyborean_sorcerer_queen"),
                MKUEntities.HYBOREAN_SKELETON_TYPE.getRegistryName(), null);
        def.addOption(new FactionOption().setValue(Factions.UNDEAD_FACTION_NAME));
        def.addOption(new MKSizeOption().setValue(1.1f));
        def.addOption(new RenderGroupOption().setValue(MKUSkeletons.SORCERER_QUEEN_NAME));
        def.addOption(new AttributesOption()
                .addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 110.0))
                .addAttributeEntry(new NpcAttributeEntry(MKAttributes.EVOCATION, 3))
        );
        def.addOption(new NameOption().setValue("Hyborean Sorcerer Queen"));
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlotType.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:katana_iron"))), 1.0, 0.0f));
        def.addOption(equipOption);
        def.addOption(new AbilitiesOption()
                .withAbilityOption(FireArmorAbility.INSTANCE, 5, 1.0)
                .withAbilityOption(FireballAbility.INSTANCE, 6, 1.0)
                .withAbilityOption(EmberAbility.INSTANCE, 1, 1.0)
                .withAbilityOption(IgniteAbility.INSTANCE, 2, 0.5)
                .withAbilityOption(FlameWaveAbility.INSTANCE, 3, 1.0)
                .withAbilityOption(WarpCurseAbility.INSTANCE, 4, 0.5)
        );
        def.addOption(new MKComboSettingsOption().setComboCount(5).setComboDelay(60));
        return def;
    }

    private NpcDefinition generateAncientKing(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "an_ancient_king"),
                MKUEntities.HYBOREAN_SKELETON_TYPE.getRegistryName(), null);
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
        equipOption.addItemChoice(EquipmentSlotType.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:battleaxe_iron"))), 1.0, 0.0f));
        equipOption.addItemChoice(EquipmentSlotType.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:greatsword_iron"))), 1.0, 0.0f));
        def.addOption(equipOption);
        def.addOption(new AbilitiesOption()
                .withAbilityOption(NaturesRemedyAbility.INSTANCE, 2, 1.0)
                .withAbilityOption(SeverTendonAbility.INSTANCE, 3, 1.0)
                .withAbilityOption(HealAbility.INSTANCE, 1, 1.0)
                .withAbilityOption(PowerWordSummonAbility.INSTANCE, 4, 0.5)
                .withAbilityOption(ExplosiveGrowthAbility.INSTANCE, 5, 0.5)
                .withAbilityOption(FireballAbility.INSTANCE, 6, 0.5)

        );
        def.addOption(new MKComboSettingsOption().setComboCount(2).setComboDelay(10));
        return def;
    }

    private NpcDefinition generateHyboreanSorcerer(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "hyborean_sorcerer"),
                MKUEntities.HYBOREAN_SKELETON_TYPE.getRegistryName(), null);
        def.addOption(new FactionOption().setValue(Factions.UNDEAD_FACTION_NAME));
        def.addOption(new MKSizeOption().setValue(0.9f));
        def.addOption(new RenderGroupOption().setValue(MKUSkeletons.SORCERER_NAME));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 40.0)));
        def.addOption(new NameOption().setValue("Hyborean Sorcerer"));
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlotType.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:dagger_stone"))), 1.0, 0.0f));
        def.addOption(equipOption);
        def.addOption(new AbilitiesOption()
                .withAbilityOption(FireArmorAbility.INSTANCE, 2, 0.5)
                .withAbilityOption(FireballAbility.INSTANCE, 1, 1.0)
                .withAbilityOption(EmberAbility.INSTANCE, 3, 1.0)
        );
        return def;
    }

    private NpcDefinition generateHyboreanHonorGuard(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "hyborean_honor_guard"),
                MKUEntities.HYBOREAN_SKELETON_TYPE.getRegistryName(), null);
        def.addOption(new FactionOption().setValue(Factions.UNDEAD_FACTION_NAME));
        def.addOption(new MKSizeOption().setValue(1.0f));
        def.addOption(new RenderGroupOption().setValue(MKUSkeletons.HONOR_GUARD_NAME));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 65.0)));
        def.addOption(new NameOption().setValue("Undying Honor Guard"));
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlotType.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:battleaxe_iron"))), 1.0, 0.0f));
        equipOption.addItemChoice(EquipmentSlotType.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:greatsword_iron"))), 1.0, 0.0f));
        equipOption.addItemChoice(EquipmentSlotType.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:warhammer_iron"))), 1.0, 0.0f));
        def.addOption(equipOption);
        def.addOption(new AbilitiesOption()
                .withAbilityOption(NaturesRemedyAbility.INSTANCE, 2, 1.0)
                .withAbilityOption(SeverTendonAbility.INSTANCE, 3, 1.0)
        );
        def.addOption(new MKComboSettingsOption().setComboCount(4).setComboDelay(30));
        return def;
    }

    private NpcDefinition generateGreenLady(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "green_lady"),
                MKUEntities.ORC_TYPE.getRegistryName(), null);
        def.addOption(new FactionOption().setValue(Factions.VILLAGER_FACTION_NAME));
        def.addOption(new MKSizeOption().setValue(1.1f));
        def.addOption(new RenderGroupOption().setValue(MKUOrcs.GREEN_LADY_NAME));
        List<MKAbility> abilities = new ArrayList<>();
        abilities.add(HealAbility.INSTANCE);
        abilities.add(GalvanizeAbility.INSTANCE);
        abilities.add(SmiteAbility.INSTANCE);
        abilities.add(PowerWordSummonAbility.INSTANCE);
        abilities.add(InspireAbility.INSTANCE);
        def.addOption(new AbilityTrainingOption().withOptions(abilities));
        def.addOption(new AbilitiesOption()
                .withAbilityOption(HealAbility.INSTANCE, 1, 1.0)
                .withAbilityOption(NaturesRemedyAbility.INSTANCE, 2, 1.0)
                .withAbilityOption(SpiritBombAbility.INSTANCE, 4, 1.0)
                .withAbilityOption(InspireAbility.INSTANCE, 3, 1.0)
        );
        def.addOption(new DialogueOption().setValue(new ResourceLocation(MKUltra.MODID, "open_abilities")));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 400.0)));
        def.addOption(new NameOption().setValue("Green Lady"));
        return def;
    }

    private NpcDefinition generateGreenLadyGuard2(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "green_lady_guard_2"),
                MKUEntities.ORC_TYPE.getRegistryName(), null);
        def.addOption(new FactionOption().setValue(Factions.VILLAGER_FACTION_NAME));
        def.addOption(new MKSizeOption().setValue(1.1f));
        def.addOption(new RenderGroupOption().setValue(MKUOrcs.GREEN_LADY_GUARD_2_NAME));
        List<MKAbility> abilities = new ArrayList<>();
        abilities.add(EmberAbility.INSTANCE);
        abilities.add(FlameWaveAbility.INSTANCE);
        abilities.add(FireArmorAbility.INSTANCE);
        abilities.add(WarpCurseAbility.INSTANCE);
        abilities.add(IgniteAbility.INSTANCE);
        def.addOption(new AbilityTrainingOption().withOptions(abilities));
        def.addOption(new AbilitiesOption()
                .withAbilityOption(EmberAbility.INSTANCE, 1, 1.0)
                .withAbilityOption(NaturesRemedyAbility.INSTANCE, 2, 1.0)
                .withAbilityOption(FlameWaveAbility.INSTANCE, 4, 1.0)
                .withAbilityOption(IgniteAbility.INSTANCE, 3, 1.0)
        );
        def.addOption(new DialogueOption().setValue(new ResourceLocation(MKUltra.MODID, "open_abilities")));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 150.0)));
        def.addOption(new NameOption().setValue("Green Guardian"));
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlotType.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:dagger_stone"))), 1.0, 0.0f));
        def.addOption(equipOption);
        return def;
    }

    private NpcDefinition generateGreenLadyGuard1(){
        NpcDefinition def = new NpcDefinition(new ResourceLocation(MKUltra.MODID, "green_lady_guard_1"),
                MKUEntities.ORC_TYPE.getRegistryName(), null);
        def.addOption(new FactionOption().setValue(Factions.VILLAGER_FACTION_NAME));
        def.addOption(new MKSizeOption().setValue(1.1f));
        def.addOption(new RenderGroupOption().setValue(MKUOrcs.GREEN_LADY_GUARD_1_NAME));
        List<MKAbility> abilities = new ArrayList<>();
        abilities.add(SkinLikeWoodAbility.INSTANCE);
        abilities.add(NaturesRemedyAbility.INSTANCE);
        abilities.add(CleansingSeedAbility.INSTANCE);
        abilities.add(SpiritBombAbility.INSTANCE);
        abilities.add(ExplosiveGrowthAbility.INSTANCE);
        def.addOption(new AbilityTrainingOption().withOptions(abilities));
        def.addOption(new AbilitiesOption()
                .withAbilityOption(SkinLikeWoodAbility.INSTANCE, 1, 1.0)
                .withAbilityOption(NaturesRemedyAbility.INSTANCE, 2, 1.0)
                .withAbilityOption(SpiritBombAbility.INSTANCE, 4, 1.0)
                .withAbilityOption(ExplosiveGrowthAbility.INSTANCE, 3, 1.0)
        );
        def.addOption(new DialogueOption().setValue(new ResourceLocation(MKUltra.MODID, "open_abilities")));
        def.addOption(new AttributesOption().addAttributeEntry(new NpcAttributeEntry(Attributes.MAX_HEALTH, 150.0)));
        def.addOption(new NameOption().setValue("Green Knight"));
        EquipmentOption equipOption = new EquipmentOption();
        equipOption.addItemChoice(EquipmentSlotType.HEAD,
                new NpcItemChoice(new ItemStack(Items.IRON_HELMET), 1.0, 0.0f));
        equipOption.addItemChoice(EquipmentSlotType.CHEST,
                new NpcItemChoice(new ItemStack(Items.IRON_CHESTPLATE), 1.0, 0.0f));
        equipOption.addItemChoice(EquipmentSlotType.LEGS,
                new NpcItemChoice(new ItemStack(Items.IRON_LEGGINGS), 1.0, 0.0f));
        equipOption.addItemChoice(EquipmentSlotType.FEET,
                new NpcItemChoice(new ItemStack(Items.IRON_BOOTS), 1.0, 0.0f));
        equipOption.addItemChoice(EquipmentSlotType.MAINHAND,
                new NpcItemChoice(new ItemStack(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("mkweapons:battleaxe_stone"))), 1.0, 0.0f));
        def.addOption(equipOption);

        return def;
    }

    @Override
    public String getName() {
        return "MKU NPC GEN";
    }
}
