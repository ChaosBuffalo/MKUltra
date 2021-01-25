//package com.chaosbuffalo.mkultra.effects.spells;
//
//import com.chaosbuffalo.mkultra.MKUltra;
//import com.chaosbuffalo.mkultra.core.IPlayerData;
//import com.chaosbuffalo.mkultra.core.PlayerAttributes;
//import com.chaosbuffalo.mkultra.effects.PassiveEffect;
//import com.chaosbuffalo.mkultra.effects.SpellCast;
//import com.chaosbuffalo.mkultra.effects.SpellTriggers;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.SharedMonsterAttributes;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.potion.Potion;
//import net.minecraft.potion.PotionEffect;
//import net.minecraft.util.DamageSource;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.event.RegistryEvent;
//import net.minecraftforge.event.entity.living.LivingHurtEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//
//import java.util.UUID;
//
///**
// * Created by Jacob on 7/28/2018.
// */
//@Mod.EventBusSubscriber(modid = MKUltra.MODID)
//public class SkinLikeWoodPotion extends PassiveEffect {
//    public static final UUID MODIFIER_ID = UUID.fromString("60f31ee6-4a8e-4c35-8746-6c5950187e77");
//    public static final SkinLikeWoodPotion INSTANCE = (SkinLikeWoodPotion) (new SkinLikeWoodPotion()
//            .registerPotionAttributeModifier(SharedMonsterAttributes.ARMOR, MODIFIER_ID.toString(), 2, PlayerAttributes.OP_INCREMENT)
//    );
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
//    private SkinLikeWoodPotion() {
//        super(false, 1665535);
//        setPotionName("effect.skin_like_wood");
//        SpellTriggers.ENTITY_HURT_PLAYER.registerPreScale(this::playerHurtPreScale);
//    }
//
//    @Override
//    public ResourceLocation getIconTexture() {
//        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/skin_like_wood.png");
//    }
//
//    @Override
//    public boolean shouldRender(PotionEffect effect) {
//        return false;
//    }
//
//    private void playerHurtPreScale(LivingHurtEvent event, DamageSource source, EntityPlayer livingTarget, IPlayerData targetData) {
//
//        if (livingTarget.isPotionActive(SkinLikeWoodPotion.INSTANCE)) {
//            if (!targetData.consumeMana(1)) {
//                livingTarget.removePotionEffect(SkinLikeWoodPotion.INSTANCE);
//            }
//        }
//    }
//}
//
