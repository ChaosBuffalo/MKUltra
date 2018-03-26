package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.mkultra.effects.Targeting;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

/**
 * Created by Jacob on 3/25/2018.
 */
@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class UnderwayPotion extends SpellPotionBase {

    public static final UnderwayPotion INSTANCE = new UnderwayPotion();
    private static final UUID MODIFIER_ID = UUID.fromString("771f39b8-c161-4b80-897f-724f84e08ae7");

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private UnderwayPotion() {
        // boolean isBadEffectIn, int liquidColorIn
        super(false, 4393423, new ResourceLocation(MKUltra.MODID, "textures/class/abilities/underway.png"));
        SpellPotionBase.register("effect.underway", this);
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.FRIENDLY;
    }

    @Override
    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {

    }

    @Override
    public void onPotionAdd(SpellCast cast, EntityLivingBase target, AbstractAttributeMap attributes, int amplifier) {
        if (target instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) target;
            Multimap<String, AttributeModifier> mods = HashMultimap.create();
            AttributeModifier mod =
                    new AttributeModifier(MODIFIER_ID, "Cooldown Reduction", -.1 * amplifier,
                            PlayerAttributes.OP_INCREMENT)
                            .setSaved(false);
            mods.put(PlayerAttributes.COOLDOWN.getName(), mod);
            attributes.applyAttributeModifiers(mods);
        }
    }

    @Override
    public void onPotionRemove(SpellCast cast, EntityLivingBase target, AbstractAttributeMap attributes, int amplifier) {
        if (target instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) target;
            Multimap<String, AttributeModifier> mods = HashMultimap.create();
            AttributeModifier mod =
                    new AttributeModifier(MODIFIER_ID, "Cooldown Reduction", -.1 * amplifier,
                            PlayerAttributes.OP_INCREMENT)
                            .setSaved(false);
            mods.put(PlayerAttributes.COOLDOWN.getName(), mod);
            attributes.removeAttributeModifiers(mods);
        }
    }


    @Override
    public boolean canSelfCast() {
        return true;
    }

    @Override
    public boolean isReady(int duration, int amplitude) {
        return false;
    }

    @Override
    public boolean isInstant() {
        return false;
    }
}
