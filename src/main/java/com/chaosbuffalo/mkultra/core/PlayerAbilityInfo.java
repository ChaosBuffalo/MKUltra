package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.GameConstants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class PlayerAbilityInfo {
    public ResourceLocation id;
    public int level;
    private int cooldown;

    public PlayerAbilityInfo(ResourceLocation abilityId) {
        id = abilityId;
        level = GameConstants.ACTION_BAR_INVALID_LEVEL;
        cooldown = 0;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldownTicks(int ticks) {
        this.cooldown = ticks;
    }

    public void serialize(NBTTagCompound tag) {
        tag.setString("id", id.toString());
        tag.setInteger("level", level);
        tag.setInteger("cooldown", cooldown);
    }

    public void deserialize(NBTTagCompound tag) {
        id = new ResourceLocation(tag.getString("id"));
        level = tag.getInteger("level");
        cooldown = tag.getInteger("cooldown");
    }
}
