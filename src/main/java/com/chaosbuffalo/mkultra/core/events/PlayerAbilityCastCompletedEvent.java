package com.chaosbuffalo.mkultra.core.events;

import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAbilityInfo;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerAbilityCastCompletedEvent extends PlayerDataEvent {
    private PlayerAbilityInfo abilityInfo;

    public PlayerAbilityCastCompletedEvent(EntityPlayer player, IPlayerData data, PlayerAbilityInfo abilityInfo) {
        super(player, data);
        this.abilityInfo = abilityInfo;
    }

    public PlayerAbilityInfo getAbilityInfo() {
        return abilityInfo;
    }
}
