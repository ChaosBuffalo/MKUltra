package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.GameConstants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class PlayerAbilityInfo {
    private ResourceLocation id;
    private int rank;
    private int cooldown;

    public PlayerAbilityInfo(ResourceLocation abilityId) {
        id = abilityId;
        rank = GameConstants.ABILITY_INVALID_RANK;
        cooldown = 0;
    }

    public PlayerAbilityInfo(ResourceLocation abilityId, int rank) {
        id = abilityId;
        this.rank = rank;
        cooldown = 0;
    }

    public ResourceLocation getId() {
        return id;
    }

    public int getRank() {
        return rank;
    }

    public boolean isCurrentlyKnown() {
        return rank > GameConstants.ABILITY_INVALID_RANK;
    }

    public void upgrade() {
        if (rank < GameConstants.MAX_ABILITY_RANK) {
            rank += 1;
        }
    }

    public boolean downgrade() {
        if (isCurrentlyKnown()) {
            rank -= 1;
            return true;
        }
        return false;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldownTicks(int ticks) {
        this.cooldown = ticks;
    }

    public void serialize(NBTTagCompound tag) {
        tag.setString("id", id.toString());
        // Leaving this as "level" to avoid breaking existing saves
        tag.setInteger("level", rank);
        tag.setInteger("cooldown", cooldown);
    }

    public void deserialize(NBTTagCompound tag) {
        id = new ResourceLocation(tag.getString("id"));
        rank = tag.getInteger("level");
        cooldown = tag.getInteger("cooldown");
    }
}
