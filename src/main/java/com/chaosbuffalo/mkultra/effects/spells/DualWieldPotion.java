package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.effects.PassiveEffect;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellTriggers;
import com.chaosbuffalo.mkultra.utils.ItemUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;



@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class DualWieldPotion extends PassiveEffect {

    public static final DualWieldPotion INSTANCE = new DualWieldPotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private DualWieldPotion() {
        super(false, 4393423);
        setPotionName("effect.dual_wield");
        SpellTriggers.PLAYER_EQUIPMENT_CHANGE.register(this, this::onEquipmentChange);
        SpellTriggers.ATTACK_ENTITY.register(this, this::onAttackEntity);
    }


    public void onAttackEntity(EntityLivingBase player, Entity target, PotionEffect effect){
        ItemStack heldItem = player.getHeldItemOffhand();
        if (ItemUtils.isSuitableOffhandWeapon(heldItem)){
            heldItem.damageItem(1, player);
            player.swingArm(EnumHand.OFF_HAND);
        }
    }

    @Override
    public void onPotionAdd(SpellCast cast, EntityLivingBase target, AbstractAttributeMap attributes, int amplifier) {
        super.onPotionAdd(cast, target, attributes, amplifier);
        if (target instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) target;
            if (ItemUtils.isSuitableOffhandWeapon(target.getHeldItemOffhand())) {
                player.getAttributeMap().applyAttributeModifiers(
                        ItemUtils.getOffhandModifiersForItem(target.getHeldItemOffhand()));
            }
        }
    }

    @Override
    public void onPotionRemove(SpellCast cast, EntityLivingBase target, AbstractAttributeMap attributes, int amplifier) {
        super.onPotionRemove(cast, target, attributes, amplifier);
        if (target instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) target;
            if (ItemUtils.isSuitableOffhandWeapon(target.getHeldItemOffhand())) {
                player.getAttributeMap().removeAttributeModifiers(
                        ItemUtils.getOffhandModifiersForItem(target.getHeldItemOffhand()));
            }
        }
    }

    public void onEquipmentChange(LivingEquipmentChangeEvent event, IPlayerData data, EntityPlayer player){
        if (event.getSlot() == EntityEquipmentSlot.OFFHAND){
            if (ItemUtils.isSuitableOffhandWeapon(event.getFrom())) {
                player.getAttributeMap().removeAttributeModifiers(ItemUtils.getOffhandModifiersForItem(event.getFrom()));
            }
            if (ItemUtils.isSuitableOffhandWeapon(event.getTo())){
                player.getAttributeMap().applyAttributeModifiers(ItemUtils.getOffhandModifiersForItem(event.getTo()));
            }
        }
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/dual_wield.png");
    }
}
