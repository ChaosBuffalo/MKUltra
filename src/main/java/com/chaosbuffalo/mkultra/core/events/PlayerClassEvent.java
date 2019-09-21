package com.chaosbuffalo.mkultra.core.events;

import com.chaosbuffalo.mkultra.core.IPlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class PlayerClassEvent extends PlayerDataEvent {

    private PlayerClassEvent(EntityPlayer player, IPlayerData data) {
        super(player, data);
    }

    public static class ClassChanged extends PlayerClassEvent {
        private ResourceLocation previousClassId;

        public ClassChanged(EntityPlayer player, IPlayerData data, ResourceLocation previousClassId) {
            super(player, data);
            this.previousClassId = previousClassId;
        }

        public ResourceLocation getPreviousClassId() {
            return previousClassId;
        }

        public ResourceLocation getNewClassId() {
            return getPlayerData().getClassId();
        }
    }

    public static class LevelChanged extends PlayerDataEvent {
        private int oldLevel;
        private int newLevel;

        public LevelChanged(EntityPlayer player, IPlayerData data, int oldLevel, int newLevel) {
            super(player, data);
            this.oldLevel = oldLevel;
            this.newLevel = newLevel;
        }

        public int getOldLevel() {
            return oldLevel;
        }

        public int getNewLevel() {
            return newLevel;
        }
    }
}
