package com.chaosbuffalo.mkultra.event;

import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber
public class PotionEventHandler {

    @SubscribeEvent
    public static void onPotionRemove(PotionEvent.PotionRemoveEvent event){
        if (event.getPotionEffect() != null && event.getPotion() instanceof SpellPotionBase){
            SpellPotionBase spellPotion = (SpellPotionBase) event.getPotion();
            if (!spellPotion.shouldPotionRemove(event.getEntityLiving(), event.getPotionEffect().getAmplifier())){
                event.setCanceled(true);
            }
        }
    }
}
