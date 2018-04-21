package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
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

    public static final UUID MODIFIER_ID = UUID.fromString("771f39b8-c161-4b80-897f-724f84e08ae7");

    public static final UnderwayPotion INSTANCE = (UnderwayPotion)(new UnderwayPotion()
        .registerPotionAttributeModifier(PlayerAttributes.COOLDOWN, MODIFIER_ID.toString(),
                -0.1, PlayerAttributes.OP_INCREMENT)
        .registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, MODIFIER_ID.toString(),
                .1, PlayerAttributes.OP_SCALE_MULTIPLICATIVE)
    );




    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private UnderwayPotion() {
        super(false, 1665535);
        register(MKUltra.MODID, "effect.underway");
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/underway.png");
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.FRIENDLY;
    }

    @Override
    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {

    }

    @Override
    public double getAttributeModifierAmount(int amplifier, AttributeModifier modifier)
    {
        return modifier.getAmount() * (double)(amplifier);
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
