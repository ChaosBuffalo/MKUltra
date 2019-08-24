package com.chaosbuffalo.mkultra.event;

import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerData;
import com.chaosbuffalo.mkultra.effects.passives.PassiveAbilityPotionBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber
public class PotionEventHandler {

    @SubscribeEvent
    public static void onPotionRemove(PotionEvent.PotionRemoveEvent event) {
//        Log.debug("PotionRemoveEvent - %s - %s", event.getEntityLiving(), event.getPotionEffect());

        if (event.getEntityLiving() instanceof EntityPlayerMP && event.getPotion() instanceof PassiveAbilityPotionBase) {
            EntityPlayerMP player = (EntityPlayerMP)event.getEntityLiving();
            PlayerData data = (PlayerData) MKUPlayerData.get(player);
            if (data != null && !data.getPassiveTalentsUnlocked()) {
                data.setRefreshPassiveTalents();
            }
        }
    }
}
