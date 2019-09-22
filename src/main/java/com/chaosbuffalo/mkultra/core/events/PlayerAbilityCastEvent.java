package com.chaosbuffalo.mkultra.core.events;

import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.PlayerAbilityInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

public class PlayerAbilityCastEvent extends PlayerDataEvent {
    private PlayerAbility ability;
    private PlayerAbilityInfo abilityInfo;

    private PlayerAbilityCastEvent(EntityPlayer player, IPlayerData data, PlayerAbility ability, PlayerAbilityInfo abilityInfo) {
        super(player, data);
        this.ability = ability;
        this.abilityInfo = abilityInfo;
    }

    public PlayerAbility getAbility() {
        return ability;
    }

    public PlayerAbilityInfo getAbilityInfo() {
        return abilityInfo;
    }

    public static class Completed extends PlayerAbilityCastEvent {

        public Completed(EntityPlayer player, IPlayerData data, PlayerAbility ability, PlayerAbilityInfo abilityInfo) {
            super(player, data, ability, abilityInfo);
        }
    }

    @Cancelable
    public static class Starting extends PlayerAbilityCastEvent {

        public Starting(EntityPlayer player, IPlayerData data, PlayerAbility ability, PlayerAbilityInfo abilityInfo) {
            super(player, data, ability, abilityInfo);
        }
    }
}
