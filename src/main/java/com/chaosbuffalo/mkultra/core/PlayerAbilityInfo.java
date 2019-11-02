package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.log.Log;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class PlayerAbilityInfo {
    private PlayerAbility ability;
    private int rank;

    public PlayerAbilityInfo(PlayerAbility ability) {
        this.ability = ability;
        rank = GameConstants.ABILITY_INVALID_RANK;
    }

    public PlayerAbility getAbility() {
        return ability;
    }

    public ResourceLocation getId() {
        return ability.getAbilityId();
    }

    public int getRank() {
        return rank;
    }

    public boolean isCurrentlyKnown() {
        return rank > GameConstants.ABILITY_INVALID_RANK;
    }

    public boolean isUpgradeable() {
        return getRank() < getAbility().getMaxRank();
    }

    public boolean upgrade() {
        if (isUpgradeable()) {
            rank += 1;
            return true;
        }
        return false;
    }

    public boolean downgrade() {
        if (isCurrentlyKnown()) {
            rank -= 1;
            return true;
        }
        return false;
    }

    public void serialize(NBTTagCompound tag) {
        tag.setString("id", ability.getAbilityId().toString());
        tag.setInteger("rank", rank);
    }

    public boolean deserialize(NBTTagCompound tag) {
        ResourceLocation id = new ResourceLocation(tag.getString("id"));
        if (!id.equals(ability.getAbilityId())) {
            Log.error("Failed to deserialize ability! id was %s, linked ability was %s", id, ability.getAbilityId());
            return false;
        }
        if (tag.hasKey("rank")) {
            rank = tag.getInteger("rank");
        }
        return true;
    }
}
