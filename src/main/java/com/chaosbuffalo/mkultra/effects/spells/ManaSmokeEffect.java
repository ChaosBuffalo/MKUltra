package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import com.chaosbuffalo.mkultra.effects.PassiveEffect;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
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
public class ManaSmokeEffect extends PassiveEffect {

    private static final double AMOUNT = 5.0;
    public static final UUID MODIFIER_ID = UUID.fromString("faa79ab5-d8c6-4737-8a28-01a00af63cf0");
    public static final ManaSmokeEffect INSTANCE = (ManaSmokeEffect) (new ManaSmokeEffect()
            .registerPotionAttributeModifier(PlayerAttributes.MAX_MANA,
                    MODIFIER_ID.toString(), AMOUNT, PlayerAttributes.OP_INCREMENT)
    );

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private ManaSmokeEffect() {
        super(false, 1665535);
        setPotionName("effect.mana_smoke");
    }


    @Override
    public void onPotionAdd(SpellCast cast, EntityLivingBase target, AbstractAttributeMap attributes, int amplifier) {
        super.onPotionAdd(cast, target, attributes, amplifier);
        if (target instanceof EntityPlayer){
            IPlayerData data = MKUPlayerData.get((EntityPlayer) target);
            if (data != null){
                data.addMana((float) (AMOUNT * amplifier));
            }
        }

    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/mana_smoke.png");
    }
}
