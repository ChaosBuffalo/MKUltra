package com.chaosbuffalo.mkultra.data_generators;

import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.abilities.misc.FireballAbility;
import com.chaosbuffalo.mkultra.abilities.misc.SeverTendonAbility;
import com.chaosbuffalo.mkultra.init.MKUItems;
import com.chaosbuffalo.mkweapons.data.LootTierProvider;
import com.chaosbuffalo.mkweapons.init.MKWeaponsItems;
import com.chaosbuffalo.mkweapons.items.effects.melee.UndeadDamageMeleeWeaponEffect;
import com.chaosbuffalo.mkweapons.items.randomization.LootTier;
import com.chaosbuffalo.mkweapons.items.randomization.options.*;
import com.chaosbuffalo.mkweapons.items.randomization.slots.LootSlot;
import com.chaosbuffalo.mkweapons.items.randomization.slots.LootSlotManager;
import com.chaosbuffalo.mkweapons.items.randomization.slots.RandomizationSlotManager;
import com.chaosbuffalo.mkweapons.items.randomization.templates.RandomizationTemplate;
import com.chaosbuffalo.mkweapons.items.weapon.types.MeleeWeaponTypes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;

public class MKULootTierProvider extends LootTierProvider {

    public MKULootTierProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    public void act(@Nonnull DirectoryCache cache) {
        writeLootTier(trooperKnightLootTier(), cache);
        writeLootTier(zombieTrooperTier(), cache);
        writeLootTier(trooperCaptain(), cache);
        writeLootTier(trooperExecutioner(), cache);
        writeLootTier(trooperMagus(), cache);
    }

    private LootTier trooperKnightLootTier(){
        LootTier tier = new LootTier(new ResourceLocation(MKUltra.MODID, "trooper_knight_armor"));
        tier.addItemToSlot(LootSlotManager.HEAD, MKUItems.trooperKnightHelmet);
        tier.addItemToSlot(LootSlotManager.CHEST, MKUItems.trooperKnightChestplate);
        tier.addItemToSlot(LootSlotManager.FEET, MKUItems.trooperKnightBoots);
        tier.addItemToSlot(LootSlotManager.LEGS, MKUItems.trooperKnightLeggings);
        introCastleAttrs(LootSlotManager.CHEST, tier);
        introCastleAttrs(LootSlotManager.FEET, tier);
        introCastleAttrs(LootSlotManager.HEAD, tier);
        introCastleAttrs(LootSlotManager.LEGS, tier);
        addTemplateTrooperKnight(LootSlotManager.CHEST, tier);
        addTemplateTrooperKnight(LootSlotManager.LEGS, tier);
        addTemplateTrooperKnight(LootSlotManager.FEET, tier);
        addTemplateTrooperKnight(LootSlotManager.HEAD, tier);
        return tier;
    }

    private void addTemplateTrooperKnight(LootSlot slot, LootTier tier){
        tier.addTemplate(new RandomizationTemplate(new ResourceLocation(MKUltra.MODID, String.format("one_effect_%s", slot.getName().getPath())),
                slot,
                RandomizationSlotManager.ATTRIBUTE_SLOT), 90);
        tier.addTemplate(new RandomizationTemplate(new ResourceLocation(MKUltra.MODID, String.format("two_effect_%s", slot.getName().getPath())),
                slot,
                RandomizationSlotManager.ATTRIBUTE_SLOT, RandomizationSlotManager.ATTRIBUTE_SLOT), 10);
    }

    private void addEarringOfMinorHealth(LootTier tier){
        tier.addItemToSlot(LootSlotManager.EARRINGS, MKWeaponsItems.GoldEarring);
        AttributeOption option = new AttributeOption();
        option.addApplicableSlot(LootSlotManager.EARRINGS);
        option.addAttributeModifier(Attributes.MAX_HEALTH, tier.getName().toString(),
                4, AttributeModifier.Operation.ADDITION);
        tier.addRandomizationOption(option);
        NameOption name = new NameOption(new StringTextComponent("Earring of Minor Health"));
        name.addApplicableSlot(LootSlotManager.EARRINGS);
        tier.addRandomizationOption(name);
        tier.addTemplate(new RandomizationTemplate(new ResourceLocation(MKUltra.MODID, "earring"),
                LootSlotManager.EARRINGS,
                RandomizationSlotManager.ATTRIBUTE_SLOT, RandomizationSlotManager.NAME_SLOT), 15);

    }

    private void addEarringOfMinorManaRegen(LootTier tier){
        tier.addItemToSlot(LootSlotManager.EARRINGS, MKWeaponsItems.SilverEarring);
        AttributeOption option = new AttributeOption();
        option.addApplicableSlot(LootSlotManager.EARRINGS);
        option.addAttributeModifier(MKAttributes.MANA_REGEN, tier.getName().toString(),
                0.25, AttributeModifier.Operation.ADDITION);
        tier.addRandomizationOption(option);
        NameOption name = new NameOption(new StringTextComponent("Earring of Quickening Thoughts"));
        name.addApplicableSlot(LootSlotManager.EARRINGS);
        tier.addRandomizationOption(name);
        tier.addTemplate(new RandomizationTemplate(new ResourceLocation(MKUltra.MODID, "earring"),
                LootSlotManager.EARRINGS,
                RandomizationSlotManager.ATTRIBUTE_SLOT, RandomizationSlotManager.NAME_SLOT), 15);
    }

    private void addRingOfMinorMana(LootTier tier){
        tier.addItemToSlot(LootSlotManager.RINGS, MKWeaponsItems.SilverRing);
        AttributeOption option = new AttributeOption();
        option.addApplicableSlot(LootSlotManager.RINGS);
        option.addAttributeModifier(MKAttributes.MAX_MANA, tier.getName().toString(),
                4, AttributeModifier.Operation.ADDITION);
        tier.addRandomizationOption(option);
        NameOption name = new NameOption(new StringTextComponent("Ring of Minor Mana"));
        name.addApplicableSlot(LootSlotManager.RINGS);
        tier.addRandomizationOption(name);
        tier.addTemplate(new RandomizationTemplate(new ResourceLocation(MKUltra.MODID, "ring"),
                LootSlotManager.RINGS,
                RandomizationSlotManager.ATTRIBUTE_SLOT, RandomizationSlotManager.NAME_SLOT), 15);

    }

    private void addRingOfMinorHealth(LootTier tier){
        tier.addItemToSlot(LootSlotManager.RINGS, MKWeaponsItems.GoldRing);
        AttributeOption option = new AttributeOption();
        option.addApplicableSlot(LootSlotManager.RINGS);
        option.addAttributeModifier(Attributes.MAX_HEALTH, tier.getName().toString(),
                4, AttributeModifier.Operation.ADDITION);
        tier.addRandomizationOption(option);
        NameOption name = new NameOption(new StringTextComponent("Ring of Minor Health"));
        name.addApplicableSlot(LootSlotManager.RINGS);
        tier.addRandomizationOption(name);
        tier.addTemplate(new RandomizationTemplate(new ResourceLocation(MKUltra.MODID, "ring"),
                LootSlotManager.RINGS,
                RandomizationSlotManager.ATTRIBUTE_SLOT, RandomizationSlotManager.NAME_SLOT), 15);

    }

    private LootTier trooperCaptain(){
        LootTier tier = new LootTier(new ResourceLocation(MKUltra.MODID, "trooper_captain"));
        tier.addItemToSlot(LootSlotManager.MAIN_HAND, MKWeaponsItems.lookupWeapon(MKWeaponsItems.IRON_TIER, MeleeWeaponTypes.KATANA_TYPE));
        MeleeEffectOption meleeEffect = new MeleeEffectOption();
        meleeEffect.addApplicableSlot(LootSlotManager.MAIN_HAND);
        meleeEffect.addEffect(new UndeadDamageMeleeWeaponEffect(1.25f));
        tier.addRandomizationOption(meleeEffect);
        NameOption name = new NameOption(new StringTextComponent("Stinging Blade"));
        name.addApplicableSlot(LootSlotManager.MAIN_HAND);
        tier.addRandomizationOption(name);
        introCastleAttrs(LootSlotManager.MAIN_HAND, tier);
        tier.addTemplate(new RandomizationTemplate(new ResourceLocation(MKUltra.MODID, "blade"),
                LootSlotManager.MAIN_HAND,
                RandomizationSlotManager.EFFECT_SLOT, RandomizationSlotManager.NAME_SLOT), 10);
        tier.addTemplate(new RandomizationTemplate(new ResourceLocation(MKUltra.MODID, "blade_crit"),
                LootSlotManager.MAIN_HAND,
                RandomizationSlotManager.EFFECT_SLOT, RandomizationSlotManager.NAME_SLOT, RandomizationSlotManager.ATTRIBUTE_SLOT), 1);
        addEarringOfMinorHealth(tier);
        return tier;
    }

    private LootTier trooperMagus(){
        LootTier tier = new LootTier(new ResourceLocation(MKUltra.MODID, "trooper_magus"));
        tier.addItemToSlot(LootSlotManager.MAIN_HAND, MKWeaponsItems.lookupWeapon(MKWeaponsItems.IRON_TIER, MeleeWeaponTypes.STAFF_TYPE));
        AddAbilityOption abilityOption = new AddAbilityOption(FireballAbility.INSTANCE, RandomizationSlotManager.ABILITY_SLOT);
        abilityOption.addApplicableSlot(LootSlotManager.MAIN_HAND);
        tier.addRandomizationOption(abilityOption);
        NameOption name = new NameOption(new StringTextComponent("Burning Staff"));
        name.addApplicableSlot(LootSlotManager.MAIN_HAND);
        tier.addRandomizationOption(name);
        introCastleAttrs(LootSlotManager.MAIN_HAND, tier);
        tier.addTemplate(new RandomizationTemplate(new ResourceLocation(MKUltra.MODID, "blade"),
                LootSlotManager.MAIN_HAND,
                RandomizationSlotManager.ABILITY_SLOT, RandomizationSlotManager.NAME_SLOT), 10);
        tier.addTemplate(new RandomizationTemplate(new ResourceLocation(MKUltra.MODID, "blade_crit"),
                LootSlotManager.MAIN_HAND,
                RandomizationSlotManager.ABILITY_SLOT, RandomizationSlotManager.NAME_SLOT, RandomizationSlotManager.ATTRIBUTE_SLOT), 1);
        addRingOfMinorMana(tier);
        addEarringOfMinorManaRegen(tier);
        return tier;
    }

    private LootTier trooperExecutioner(){
        LootTier tier = new LootTier(new ResourceLocation(MKUltra.MODID, "trooper_executioner"));
        tier.addItemToSlot(LootSlotManager.MAIN_HAND, MKWeaponsItems.lookupWeapon(MKWeaponsItems.IRON_TIER, MeleeWeaponTypes.GREATSWORD_TYPE));
        tier.addItemToSlot(LootSlotManager.MAIN_HAND, MKWeaponsItems.lookupWeapon(MKWeaponsItems.IRON_TIER, MeleeWeaponTypes.WARHAMMER_TYPE));
        tier.addItemToSlot(LootSlotManager.MAIN_HAND, MKWeaponsItems.lookupWeapon(MKWeaponsItems.IRON_TIER, MeleeWeaponTypes.BATTLEAXE_TYPE));
        AddAbilityOption abilityOption = new AddAbilityOption(SeverTendonAbility.INSTANCE, RandomizationSlotManager.ABILITY_SLOT);
        abilityOption.addApplicableSlot(LootSlotManager.MAIN_HAND);
        tier.addRandomizationOption(abilityOption);
        PrefixNameOption name = new PrefixNameOption(new StringTextComponent("Executioner's"));
        name.addApplicableSlot(LootSlotManager.MAIN_HAND);
        tier.addRandomizationOption(name);
        introCastleAttrs(LootSlotManager.MAIN_HAND, tier);
        tier.addTemplate(new RandomizationTemplate(new ResourceLocation(MKUltra.MODID, "blade"),
                LootSlotManager.MAIN_HAND,
                RandomizationSlotManager.ABILITY_SLOT, RandomizationSlotManager.NAME_SLOT), 10);
        tier.addTemplate(new RandomizationTemplate(new ResourceLocation(MKUltra.MODID, "blade_crit"),
                LootSlotManager.MAIN_HAND,
                RandomizationSlotManager.ABILITY_SLOT, RandomizationSlotManager.NAME_SLOT, RandomizationSlotManager.ATTRIBUTE_SLOT), 1);
        addRingOfMinorHealth(tier);
        return tier;
    }

    private LootTier zombieTrooperTier(){
        LootTier tier = new LootTier(new ResourceLocation(MKUltra.MODID, "zombie_trooper"));
        tier.addItemStackToSlot(LootSlotManager.ITEMS, new ItemStack(MKUItems.corruptedPigIronPlate), 10.0);
        tier.addItemStackToSlot(LootSlotManager.ITEMS, new ItemStack(MKUItems.destroyedTrooperBoots), 1.0);
        tier.addItemStackToSlot(LootSlotManager.ITEMS, new ItemStack(MKUItems.destroyedTrooperChestplate), 1.0);
        tier.addItemStackToSlot(LootSlotManager.ITEMS, new ItemStack(MKUItems.destroyedTrooperLeggings), 1.0);
        tier.addItemStackToSlot(LootSlotManager.ITEMS, new ItemStack(MKUItems.destroyedTrooperHelmet), 1.0);
        tier.addTemplate(new RandomizationTemplate(new ResourceLocation(MKUltra.MODID, "empty"), LootSlotManager.ITEMS), 1.0);
        return tier;

    }

    private void introCastleAttrs(LootSlot slot, LootTier tier){
        AttributeOption healthAttribute = new AttributeOption();
        healthAttribute.addAttributeModifier(Attributes.MAX_HEALTH, tier.getName().toString(),
                2, AttributeModifier.Operation.ADDITION);
        healthAttribute.addApplicableSlot(slot);
        tier.addRandomizationOption(healthAttribute);
        AttributeOption manaAttribute = new AttributeOption();
        manaAttribute.addAttributeModifier(MKAttributes.MAX_MANA, tier.getName().toString(),
                2, AttributeModifier.Operation.ADDITION);
        manaAttribute.addApplicableSlot(slot);
        tier.addRandomizationOption(manaAttribute);
        AttributeOption manaRegen = new AttributeOption();
        manaRegen.addAttributeModifier(MKAttributes.MANA_REGEN, tier.getName().toString(),
                0.25, AttributeModifier.Operation.ADDITION);
        manaRegen.addApplicableSlot(slot);
        tier.addRandomizationOption(manaRegen);
        AttributeOption runSpeed = new AttributeOption();
        runSpeed.addAttributeModifier(Attributes.MOVEMENT_SPEED, tier.getName().toString(),
                0.05, AttributeModifier.Operation.MULTIPLY_TOTAL);
        runSpeed.addApplicableSlot(slot);
        tier.addRandomizationOption(runSpeed);
        AttributeOption atkDamage = new AttributeOption();
        atkDamage.addAttributeModifier(Attributes.ATTACK_DAMAGE, tier.getName().toString(),
                1.0, AttributeModifier.Operation.ADDITION);
        atkDamage.addApplicableSlot(slot);
        tier.addRandomizationOption(atkDamage);
        AttributeOption armor = new AttributeOption();
        armor.addAttributeModifier(Attributes.ARMOR, tier.getName().toString(),
                1.0, AttributeModifier.Operation.ADDITION);
        armor.addApplicableSlot(slot);
        tier.addRandomizationOption(armor);
        AttributeOption natureDamage = new AttributeOption();
        natureDamage.addAttributeModifier(MKAttributes.NATURE_DAMAGE, tier.getName().toString(),
                1, AttributeModifier.Operation.ADDITION);
        natureDamage.addApplicableSlot(slot);
        tier.addRandomizationOption(natureDamage);
    }
}
