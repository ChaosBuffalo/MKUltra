package com.chaosbuffalo.mkultra.core.talents;

import com.chaosbuffalo.mkultra.core.PlayerPassiveAbility;
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
}
