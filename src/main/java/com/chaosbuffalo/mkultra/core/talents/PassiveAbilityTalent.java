package com.chaosbuffalo.mkultra.core.talents;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerClassInfo;
import com.chaosbuffalo.mkultra.core.PlayerData;
import com.chaosbuffalo.mkultra.core.PlayerPassiveAbility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class PassiveAbilityTalent extends BaseTalent {

    private PlayerPassiveAbility ability;

    public PassiveAbilityTalent(ResourceLocation name, PlayerPassiveAbility ability) {
        super(name, TalentType.PASSIVE);
        this.ability = ability;
    }

    public PlayerPassiveAbility getAbility() {
        return ability;
    }

    @Override
    public boolean onRemove(EntityPlayer player, PlayerClassInfo classInfo) {
        PlayerData data = (PlayerData) MKUPlayerData.get(player);
        if (data == null)
            return false;

        data.clearPassive(getAbility().getAbilityId());
        return true;
    }
}
