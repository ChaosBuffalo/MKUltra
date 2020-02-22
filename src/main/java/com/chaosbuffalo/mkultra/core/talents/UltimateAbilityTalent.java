package com.chaosbuffalo.mkultra.core.talents;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.core.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class UltimateAbilityTalent extends BaseTalent {

    private PlayerAbility ability;

    public UltimateAbilityTalent(ResourceLocation name, PlayerAbility ability) {
        super(name, TalentType.ULTIMATE);
        this.ability = ability;
    }

    public PlayerAbility getAbility() {
        return ability;
    }

    @Override
    public boolean onRemove(EntityPlayer player, PlayerClassInfo classInfo) {
        PlayerData data = (PlayerData) MKUPlayerData.get(player);
        if (data == null)
            return false;

        data.clearUltimate(getAbility().getAbilityId());
        return true;
    }

}
