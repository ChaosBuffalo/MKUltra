package com.chaosbuffalo.mkultra.data_generators;

import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.init.MKUItems;
import com.chaosbuffalo.mkweapons.data.LootTierProvider;
import com.chaosbuffalo.mkweapons.items.randomization.LootTier;
import com.chaosbuffalo.mkweapons.items.randomization.options.AttributeOption;
import com.chaosbuffalo.mkweapons.items.randomization.slots.LootSlot;
import com.chaosbuffalo.mkweapons.items.randomization.slots.LootSlotManager;
import com.chaosbuffalo.mkweapons.items.randomization.slots.RandomizationSlotManager;
import com.chaosbuffalo.mkweapons.items.randomization.templates.RandomizationTemplate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class MKULootTierProvider extends LootTierProvider {

    public MKULootTierProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    public void act(@Nonnull DirectoryCache cache) {

        writeLootTier(trooperKnightLootTier(), cache);
        writeLootTier(zombieTrooperTier(), cache);
    }

    private LootTier trooperKnightLootTier(){
        LootTier tier = new LootTier(new ResourceLocation(MKUltra.MODID, "trooper_knight_armor"));
        tier.addItemToSlot(LootSlotManager.HEAD, MKUItems.trooperKnightHelmet);
        tier.addItemToSlot(LootSlotManager.CHEST, MKUItems.trooperKnightChestplate);
        tier.addItemToSlot(LootSlotManager.FEET, MKUItems.trooperKnightBoots);
        tier.addItemToSlot(LootSlotManager.LEGS, MKUItems.trooperKnightLeggings);
        setupTrooperKnightRandomizationOptions(LootSlotManager.CHEST, tier);
        setupTrooperKnightRandomizationOptions(LootSlotManager.FEET, tier);
        setupTrooperKnightRandomizationOptions(LootSlotManager.HEAD, tier);
        setupTrooperKnightRandomizationOptions(LootSlotManager.LEGS, tier);
        tier.addTemplate(new RandomizationTemplate(new ResourceLocation(MKUltra.MODID, "one_effect"),
                RandomizationSlotManager.ATTRIBUTE_SLOT), 90);
        tier.addTemplate(new RandomizationTemplate(new ResourceLocation(MKUltra.MODID, "two_effect"),
                RandomizationSlotManager.ATTRIBUTE_SLOT, RandomizationSlotManager.ATTRIBUTE_SLOT), 10);
        return tier;

    }

    private LootTier zombieTrooperTier(){
        LootTier tier = new LootTier(new ResourceLocation(MKUltra.MODID, "zombie_trooper"));
        tier.addItemStackToSlot(LootSlotManager.ITEMS, new ItemStack(MKUItems.corruptedPigIronPlate), 10.0);
        tier.addItemStackToSlot(LootSlotManager.ITEMS, new ItemStack(MKUItems.destroyedTrooperBoots), 1.0);
        tier.addItemStackToSlot(LootSlotManager.ITEMS, new ItemStack(MKUItems.destroyedTrooperChestplate), 1.0);
        tier.addItemStackToSlot(LootSlotManager.ITEMS, new ItemStack(MKUItems.destroyedTrooperLeggings), 1.0);
        tier.addItemStackToSlot(LootSlotManager.ITEMS, new ItemStack(MKUItems.destroyedTrooperHelmet), 1.0);
        tier.addTemplate(new RandomizationTemplate(new ResourceLocation(MKUltra.MODID, "empty")), 1.0);
        return tier;

    }

    private void setupTrooperKnightRandomizationOptions(LootSlot slot, LootTier tier){
        AttributeOption healthAttribute = new AttributeOption();
        healthAttribute.addAttributeModifier(Attributes.MAX_HEALTH, new AttributeModifier(tier.getName().toString(),
                2, AttributeModifier.Operation.ADDITION));
        healthAttribute.addApplicableSlot(slot);
        tier.addRandomizationOption(healthAttribute);
        AttributeOption manaAttribute = new AttributeOption();
        manaAttribute.addAttributeModifier(MKAttributes.MAX_MANA, new AttributeModifier(tier.getName().toString(),
                2, AttributeModifier.Operation.ADDITION));
        manaAttribute.addApplicableSlot(slot);
        tier.addRandomizationOption(manaAttribute);
        AttributeOption manaRegen = new AttributeOption();
        manaRegen.addAttributeModifier(MKAttributes.MANA_REGEN, new AttributeModifier(tier.getName().toString(),
                0.25, AttributeModifier.Operation.ADDITION));
        manaRegen.addApplicableSlot(slot);
        tier.addRandomizationOption(manaRegen);
        AttributeOption runSpeed = new AttributeOption();
        runSpeed.addAttributeModifier(Attributes.MOVEMENT_SPEED, new AttributeModifier(tier.getName().toString(),
                0.05, AttributeModifier.Operation.MULTIPLY_TOTAL));
        runSpeed.addApplicableSlot(slot);
        tier.addRandomizationOption(runSpeed);
        AttributeOption atkDamage = new AttributeOption();
        atkDamage.addAttributeModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(tier.getName().toString(),
                1.0, AttributeModifier.Operation.ADDITION));
        atkDamage.addApplicableSlot(slot);
        tier.addRandomizationOption(atkDamage);
        AttributeOption armor = new AttributeOption();
        armor.addAttributeModifier(Attributes.ARMOR, new AttributeModifier(tier.getName().toString(),
                1.0, AttributeModifier.Operation.ADDITION));
        armor.addApplicableSlot(slot);
        tier.addRandomizationOption(armor);
        AttributeOption natureDamage = new AttributeOption();
        natureDamage.addAttributeModifier(MKAttributes.NATURE_DAMAGE, new AttributeModifier(tier.getName().toString(),
                1, AttributeModifier.Operation.ADDITION));
        natureDamage.addApplicableSlot(slot);
        tier.addRandomizationOption(natureDamage);
    }
}
