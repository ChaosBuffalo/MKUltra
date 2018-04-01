package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.mkultra.effects.Targeting;
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
public class WaveBreakPotion extends SpellPotionBase {
    public static final UUID MODIFIER_ID = UUID.fromString("771f29b8-c161-4b80-897f-724f84e08ae7");
    public static final WaveBreakPotion INSTANCE = (WaveBreakPotion)(new WaveBreakPotion()
            .registerPotionAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, MODIFIER_ID.toString(), 5, PlayerAttributes.OP_INCREMENT)
            .registerPotionAttributeModifier(SharedMonsterAttributes.ARMOR, MODIFIER_ID.toString(), 2, PlayerAttributes.OP_INCREMENT)
            .registerPotionAttributeModifier(PlayerAttributes.MAGIC_ARMOR, MODIFIER_ID.toString(), 3, PlayerAttributes.OP_INCREMENT)
    );

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private WaveBreakPotion() {
        super(false, 1665535);
        register(MKUltra.MODID, "effect.wave_break");
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/wave_break.png");
    }

    @Override
    public double getAttributeModifierAmount(int amplifier, AttributeModifier modifier)
    {
        return modifier.getAmount() * (double)(amplifier);
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.FRIENDLY;
    }

    @Override
    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {

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

