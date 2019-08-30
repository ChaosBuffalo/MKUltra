package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import com.chaosbuffalo.mkultra.effects.PassiveEffect;
import com.chaosbuffalo.mkultra.effects.SpellCast;
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
public class SlayingEdgePotion extends PassiveEffect {
    public static final UUID MODIFIER_ID = UUID.fromString("666f29b8-c161-4b80-897f-724f84e08ae7");
    public static final SlayingEdgePotion INSTANCE = (SlayingEdgePotion) (new SlayingEdgePotion()
            .registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, MODIFIER_ID.toString(), 3, PlayerAttributes.OP_INCREMENT)
            .registerPotionAttributeModifier(PlayerAttributes.MAGIC_ATTACK_DAMAGE, MODIFIER_ID.toString(), 3, PlayerAttributes.OP_INCREMENT)
    );

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private SlayingEdgePotion() {
        super(false, 10158335);
        setPotionName("effect.slaying_edge");
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/slaying_edge.png");
    }
}
