package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import com.chaosbuffalo.mkultra.effects.PassiveEffect;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class BolsteringRoarPotion extends PassiveEffect {

    private static final int ABSORPTION_AMOUNT = 4;
    public static final UUID MODIFIER_ID = UUID.fromString("61448838-8d44-4c66-8855-58d547feaa77");
    public static final BolsteringRoarPotion INSTANCE = (BolsteringRoarPotion) (new BolsteringRoarPotion()
            .registerPotionAttributeModifier(PlayerAttributes.MELEE_CRITICAL_DAMAGE, MODIFIER_ID.toString(), 1.5, PlayerAttributes.OP_INCREMENT)
            .registerPotionAttributeModifier(SharedMonsterAttributes.ARMOR, MODIFIER_ID.toString(), 3, PlayerAttributes.OP_INCREMENT)
            .registerPotionAttributeModifier(PlayerAttributes.MAGIC_ARMOR, MODIFIER_ID.toString(), 3, PlayerAttributes.OP_INCREMENT)
    );

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private BolsteringRoarPotion() {
        super(false, 1665535);
        setPotionName("effect.bolstering_roar");
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/bolstering_roar.png");
    }

    @Override
    public void onPotionAdd(SpellCast cast, EntityLivingBase target, AbstractAttributeMap attributes, int amplifier) {
        target.setAbsorptionAmount(target.getAbsorptionAmount() + (float) (amplifier * ABSORPTION_AMOUNT));

    }

    @Override
    public void onPotionRemove(SpellCast cast, EntityLivingBase target, AbstractAttributeMap attributes, int amplifier) {
        target.setAbsorptionAmount(target.getAbsorptionAmount() - (float) (amplifier * ABSORPTION_AMOUNT));
    }
}