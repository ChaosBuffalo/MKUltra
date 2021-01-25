//package com.chaosbuffalo.mkultra.effects.spells;
//
//import com.chaosbuffalo.mkultra.MKUltra;
//import com.chaosbuffalo.mkultra.core.IPlayerData;
//import com.chaosbuffalo.mkultra.core.PlayerAttributes;
//import com.chaosbuffalo.mkultra.effects.SpellCast;
//import com.chaosbuffalo.mkultra.effects.SpellTriggers;
//import com.chaosbuffalo.mkultra.effects.passives.PassiveAbilityPotionBase;
//import com.chaosbuffalo.mkultra.utils.ItemUtils;
//import com.google.common.collect.HashMultimap;
//import com.google.common.collect.Multimap;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.SharedMonsterAttributes;
//import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
//import net.minecraft.entity.ai.attributes.AttributeModifier;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.inventory.EntityEquipmentSlot;
//import net.minecraft.potion.Potion;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.event.RegistryEvent;
//import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//
//import java.util.UUID;
//
//@Mod.EventBusSubscriber(modid = MKUltra.MODID)
//public class TwoHandedStylePotion extends PassiveAbilityPotionBase {
//    public static final UUID ARMOR_UUID = UUID.fromString("229ae2a5-f4fe-408f-b50e-46ff66253c7d");
//
//    public static final TwoHandedStylePotion INSTANCE = new TwoHandedStylePotion();
//
//    private static final Multimap<String, AttributeModifier> POTION_MODIFIERS = HashMultimap.create();
//
//    static {
//        POTION_MODIFIERS.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(ARMOR_UUID,
//                "Passive Modifier",
//                4.0, PlayerAttributes.OP_INCREMENT));
//    }
//
//    @SubscribeEvent
//    public static void register(RegistryEvent.Register<Potion> event) {
//        event.getRegistry().register(INSTANCE.finish());
//    }
//
//    public static SpellCast Create(Entity source) {
//        return INSTANCE.newSpellCast(source);
//    }
//
//    private TwoHandedStylePotion() {
//        super();
//        setPotionName("effect.two_handed_style");
//        SpellTriggers.PLAYER_EQUIPMENT_CHANGE.register(this, this::onEquipmentChange);
//    }
//
//    @Override
//    public void onPotionAdd(SpellCast cast, EntityLivingBase target, AbstractAttributeMap attributes, int amplifier) {
//        super.onPotionAdd(cast, target, attributes, amplifier);
//        if (target instanceof EntityPlayer) {
//            EntityPlayer player = (EntityPlayer) target;
//            if (ItemUtils.isTwoHandedWeapon(target.getHeldItemMainhand().getItem())) {
//                player.getAttributeMap().applyAttributeModifiers(POTION_MODIFIERS);
//            }
//        }
//    }
//
//    @Override
//    public void onPotionRemove(SpellCast cast, EntityLivingBase target, AbstractAttributeMap attributes, int amplifier) {
//        super.onPotionRemove(cast, target, attributes, amplifier);
//        if (target instanceof EntityPlayer) {
//            EntityPlayer player = (EntityPlayer) target;
//            if (ItemUtils.isTwoHandedWeapon(target.getHeldItemMainhand().getItem())) {
//                player.getAttributeMap().removeAttributeModifiers(POTION_MODIFIERS);
//            }
//        }
//    }
//
//    public void onEquipmentChange(LivingEquipmentChangeEvent event, IPlayerData data, EntityPlayer player) {
//        if (event.getSlot() == EntityEquipmentSlot.MAINHAND) {
//            if (ItemUtils.isTwoHandedWeapon(event.getFrom().getItem())) {
//                player.getAttributeMap().removeAttributeModifiers(POTION_MODIFIERS);
//            }
//            if (ItemUtils.isTwoHandedWeapon(event.getTo().getItem())) {
//                player.getAttributeMap().applyAttributeModifiers(POTION_MODIFIERS);
//            }
//        }
//    }
//
//    @Override
//    public ResourceLocation getIconTexture() {
//        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/two_handed_style.png");
//    }
//}