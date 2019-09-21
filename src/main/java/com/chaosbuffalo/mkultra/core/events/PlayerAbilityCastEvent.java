package com.chaosbuffalo.mkultra.core.events;

import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAbilityInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

public class PlayerAbilityCastEvent extends PlayerDataEvent {
    private PlayerAbilityInfo abilityInfo;

    private PlayerAbilityCastEvent(EntityPlayer player, IPlayerData data, PlayerAbilityInfo abilityInfo) {
        super(player, data);
        this.abilityInfo = abilityInfo;
    }

    public PlayerAbilityInfo getAbilityInfo() {
        return abilityInfo;
    }

    public static class Completed extends PlayerAbilityCastEvent {

        public Completed(EntityPlayer player, IPlayerData data, PlayerAbilityInfo abilityInfo) {
            super(player, data, abilityInfo);
        }
    }

    @Cancelable
    public static class Starting extends PlayerAbilityCastEvent {

        public Starting(EntityPlayer player, IPlayerData data, PlayerAbilityInfo abilityInfo) {
            super(player, data, abilityInfo);
        }
    }
}
