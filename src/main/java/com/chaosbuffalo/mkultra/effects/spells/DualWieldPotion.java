//package com.chaosbuffalo.mkultra.effects.spells;
//
//import com.chaosbuffalo.mkultra.MKUltra;
//import com.chaosbuffalo.mkultra.core.MKUPlayerData;
//import com.chaosbuffalo.mkultra.core.PlayerAttributes;
//import com.chaosbuffalo.mkultra.core.PlayerData;
//import com.chaosbuffalo.mkultra.core.events.ServerSideLeftClickEmpty;
//import com.chaosbuffalo.mkultra.effects.SpellCast;
//import com.chaosbuffalo.mkultra.effects.SpellTriggers;
//import com.chaosbuffalo.mkultra.effects.passives.PassiveAbilityPotionBase;
//import com.chaosbuffalo.mkultra.log.Log;
//import com.chaosbuffalo.mkultra.utils.ItemUtils;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.SharedMonsterAttributes;
//import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.potion.Potion;
//import net.minecraft.potion.PotionEffect;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.event.RegistryEvent;
//import net.minecraftforge.event.entity.player.PlayerInteractEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//
//import java.util.UUID;
//
//
//@Mod.EventBusSubscriber(modid = MKUltra.MODID)
//public class DualWieldPotion extends PassiveAbilityPotionBase {
//
//    public static final UUID MODIFIER_ID = UUID.fromString("33fdd512-830a-4bcd-8e91-f277e58a40d4");
//
//    public static final DualWieldPotion INSTANCE = (DualWieldPotion) (new DualWieldPotion().registerPotionAttributeModifier(
//            SharedMonsterAttributes.ATTACK_SPEED, MODIFIER_ID.toString(),
//            0.6, PlayerAttributes.OP_SCALE_MULTIPLICATIVE));
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
//    private DualWieldPotion() {
//        setPotionName("effect.dual_wield");
//        SpellTriggers.PLAYER_ATTACK_ENTITY.register(this, this::onAttackEntity);
//        SpellTriggers.EMPTY_LEFT_CLICK.register(this, this::onLeftClickEmpty);
//    }
//
//    public void onLeftClickEmpty(ServerSideLeftClickEmpty event, EntityPlayer player, PotionEffect effect) {
//        if (ItemUtils.isSuitableOffhandWeapon(player.getHeldItemOffhand())) {
//            PlayerData pData = (PlayerData) MKUPlayerData.get(player);
//            if (pData == null) {
//                return;
//            }
//            pData.performDualWieldSequence();
//
//        }
//    }
//
//    public void onAttackEntity(EntityLivingBase entity, Entity target, PotionEffect effect) {
//        if (ItemUtils.isSuitableOffhandWeapon(entity.getHeldItemOffhand())) {
//            if (entity instanceof EntityPlayer) {
//                EntityPlayer player = (EntityPlayer) entity;
//                PlayerData pData = (PlayerData) MKUPlayerData.get(player);
//                if (pData == null) {
//                    return;
//                }
//                pData.performDualWieldSequence();
//            }
//        }
//    }
//
//    @Override
//    public void onPotionRemove(SpellCast cast, EntityLivingBase target, AbstractAttributeMap attributes, int amplifier) {
//        super.onPotionRemove(cast, target, attributes, amplifier);
//        if (target instanceof EntityPlayer) {
//            EntityPlayer player = (EntityPlayer) target;
//            PlayerData pData = (PlayerData) MKUPlayerData.get(player);
//            if (pData == null) {
//                return;
//            }
//
//            pData.endDualWieldSequence();
//        }
//    }
//
//    @Override
//    public ResourceLocation getIconTexture() {
//        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/dual_wield.png");
//    }
//}
