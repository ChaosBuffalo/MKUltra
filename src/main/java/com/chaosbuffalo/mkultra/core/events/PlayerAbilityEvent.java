package com.chaosbuffalo.mkultra.core.events;

import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.PlayerAbilityInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

public class PlayerAbilityEvent extends PlayerDataEvent {
    private PlayerAbilityInfo abilityInfo;

    private PlayerAbilityEvent(EntityPlayer player, IPlayerData data, PlayerAbilityInfo abilityInfo) {
        super(player, data);
        this.abilityInfo = abilityInfo;
    }

    public PlayerAbility getAbility() {
        return abilityInfo.getAbility();
    }

    public PlayerAbilityInfo getAbilityInfo() {
        return abilityInfo;
    }

    public static class Completed extends PlayerAbilityEvent {

        public Completed(EntityPlayer player, IPlayerData data, PlayerAbilityInfo abilityInfo) {
            super(player, data, abilityInfo);
        }
    }

    @Cancelable
    public static class StartCasting extends PlayerAbilityEvent {

        public StartCasting(EntityPlayer player, IPlayerData data, PlayerAbilityInfo abilityInfo) {
            super(player, data, abilityInfo);
        }
    }
}
