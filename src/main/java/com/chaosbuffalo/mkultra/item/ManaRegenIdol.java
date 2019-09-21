package com.chaosbuffalo.mkultra.item;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import com.chaosbuffalo.mkultra.core.events.PlayerAbilityCastCompletedEvent;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

@Mod.EventBusSubscriber
public class ManaRegenIdol extends Item {

    private final float regen_amount;
    private final int bonus_mana;
    private final int bonus_magic_damage;
    private final int magic_armor;

    private static final UUID OFFHAND_MODIFIER_ID = UUID.fromString("771f39b8-c161-4b80-897f-724f88e08ae7");
    private static final UUID MAINHAND_MODIFIER_ID = UUID.fromString("771f39b8-c161-4b80-897f-724f88e08ae4");


    public ManaRegenIdol(String unlocalizedName, float regenBonus, int bonus_mana, int bonus_magic_damage,
                         int magic_armor, int maxDamage) {
        super();
        this.setTranslationKey(unlocalizedName);
        this.regen_amount = regenBonus;
        this.bonus_magic_damage = bonus_magic_damage;
        this.bonus_mana = bonus_mana;
        this.magic_armor = magic_armor;
        this.setMaxDamage(maxDamage);
        this.setMaxStackSize(1);
        this.setCreativeTab(MKUltra.MKULTRA_TAB);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {

        Multimap<String, AttributeModifier> mods = super.getAttributeModifiers(slot, stack);

        if (slot == EntityEquipmentSlot.OFFHAND) {
            if (this.regen_amount > 0) {
                AttributeModifier regen_mod =
                        new AttributeModifier(OFFHAND_MODIFIER_ID, "Bonus Mana Regen", this.regen_amount, PlayerAttributes.OP_INCREMENT)
                                .setSaved(false);
                mods.put(PlayerAttributes.MANA_REGEN.getName(), regen_mod);
            }
            if (this.bonus_mana > 0) {
                AttributeModifier mana_mod =
                        new AttributeModifier(OFFHAND_MODIFIER_ID, "Bonus Mana", this.bonus_mana, PlayerAttributes.OP_INCREMENT)
                                .setSaved(false);
                mods.put(PlayerAttributes.MAX_MANA.getName(), mana_mod);
            }
            if (this.bonus_magic_damage > 0) {
                AttributeModifier mana_mod =
                        new AttributeModifier(OFFHAND_MODIFIER_ID, "Bonus Magic Damage", this.bonus_magic_damage, PlayerAttributes.OP_INCREMENT)
                                .setSaved(false);
                mods.put(PlayerAttributes.MAGIC_ATTACK_DAMAGE.getName(), mana_mod);
            }
            if (this.magic_armor > 0) {
                AttributeModifier mana_mod =
                        new AttributeModifier(OFFHAND_MODIFIER_ID, "Bonus Magic Armor", this.magic_armor, PlayerAttributes.OP_INCREMENT)
                                .setSaved(false);
                mods.put(PlayerAttributes.MAGIC_ARMOR.getName(), mana_mod);
            }
        }
        if (slot == EntityEquipmentSlot.MAINHAND) {
            if (this.regen_amount > 0) {
                AttributeModifier regen_mod =
                        new AttributeModifier(MAINHAND_MODIFIER_ID, "Bonus Mana Regen", this.regen_amount, PlayerAttributes.OP_INCREMENT)
                                .setSaved(false);
                mods.put(PlayerAttributes.MANA_REGEN.getName(), regen_mod);
            }
            if (this.bonus_mana > 0) {
                AttributeModifier mana_mod =
                        new AttributeModifier(MAINHAND_MODIFIER_ID, "Bonus Mana", this.bonus_mana, PlayerAttributes.OP_INCREMENT)
                                .setSaved(false);
                mods.put(PlayerAttributes.MAX_MANA.getName(), mana_mod);
            }
            if (this.bonus_magic_damage > 0) {
                AttributeModifier mana_mod =
                        new AttributeModifier(MAINHAND_MODIFIER_ID, "Bonus Magic Damage", this.bonus_magic_damage, PlayerAttributes.OP_INCREMENT)
                                .setSaved(false);
                mods.put(PlayerAttributes.MAGIC_ATTACK_DAMAGE.getName(), mana_mod);
            }
            if (this.magic_armor > 0) {
                AttributeModifier mana_mod =
                        new AttributeModifier(MAINHAND_MODIFIER_ID, "Bonus Magic Armor", this.magic_armor, PlayerAttributes.OP_INCREMENT)
                                .setSaved(false);
                mods.put(PlayerAttributes.MAGIC_ARMOR.getName(), mana_mod);
            }
        }

        return mods;
    }

    @SubscribeEvent
    public static void onPlayerCompletedAbility(PlayerAbilityCastCompletedEvent event) {
        EntityPlayer player = event.getPlayer();
        for (EnumHand hand : EnumHand.values()) {
            ItemStack heldItem = player.getHeldItem(hand);
            if (heldItem.getItem() instanceof ManaRegenIdol) {
                ItemHelper.damageStack(player, heldItem, 1);
            }
        }
    }
}
