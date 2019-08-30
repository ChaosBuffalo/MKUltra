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
 * Created by Jacob on 8/5/2018.
 */
@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class SpeedSmokeEffect extends PassiveEffect {
    public static final UUID MODIFIER_ID = UUID.fromString("7354e47c-ceda-4b03-a032-0ba541728efb");
    public static final SpeedSmokeEffect INSTANCE = (SpeedSmokeEffect) (new SpeedSmokeEffect()
            .registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED,
                    MODIFIER_ID.toString(), .15, PlayerAttributes.OP_SCALE_MULTIPLICATIVE)
    );

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private SpeedSmokeEffect() {
        super(false, 1665535);
        setPotionName("effect.speed_smoke");
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/speed_smoke.png");
    }
}
