package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import com.chaosbuffalo.mkultra.effects.PassiveEffect;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

/**
 * Created by Jacob on 6/23/2018.
 */
@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class WildToxinEffectPotion extends PassiveEffect {
    public static final UUID MODIFIER_ID = UUID.fromString("222f29b8-c161-4b80-897f-724f84e08ae7");
    public static final WildToxinEffectPotion INSTANCE = (WildToxinEffectPotion) (new WildToxinEffectPotion()
            .registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, MODIFIER_ID.toString(), -.40, PlayerAttributes.OP_SCALE_MULTIPLICATIVE)
            .registerPotionAttributeModifier(SharedMonsterAttributes.ARMOR, MODIFIER_ID.toString(), -.25, PlayerAttributes.OP_SCALE_MULTIPLICATIVE)
            .registerPotionAttributeModifier(PlayerAttributes.MAGIC_ARMOR, MODIFIER_ID.toString(), -.25, PlayerAttributes.OP_SCALE_MULTIPLICATIVE)
    );

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private WildToxinEffectPotion() {
        super(false, 10223410);
        setPotionName("effect.wild_toxin_effect");
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/wild_toxin.png");
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public boolean canSelfCast() {
        return false;
    }
}