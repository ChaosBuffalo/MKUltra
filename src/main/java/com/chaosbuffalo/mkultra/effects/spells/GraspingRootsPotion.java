package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import com.chaosbuffalo.mkultra.effects.PassiveEffect;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class GraspingRootsPotion extends PassiveEffect {
    public static final UUID MODIFIER_ID = UUID.fromString("9930b913-ae9a-4d2f-a99a-a55bc1f7b4e6");
    public static final GraspingRootsPotion INSTANCE = (GraspingRootsPotion) (new GraspingRootsPotion()
            .registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, MODIFIER_ID.toString(),
                    -1, PlayerAttributes.OP_SCALE_MULTIPLICATIVE)
    );

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
            event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private GraspingRootsPotion() {
        super(true, 1665535);
        setPotionName("effect.grasping_roots");
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/grasping_roots.png");
    }

    @Override
    public double getAttributeModifierAmount(int amplifier, AttributeModifier modifier) {
        return modifier.getAmount() * (double) (amplifier);
    }

}
