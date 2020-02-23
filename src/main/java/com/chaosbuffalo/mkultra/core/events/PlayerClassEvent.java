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
        private ResourceLocation newClassId;

        public ClassChanged(EntityPlayer player, IPlayerData data, ResourceLocation previousClassId, ResourceLocation newClassId) {
            super(player, data);
            this.previousClassId = previousClassId;
            this.newClassId = newClassId;
        }

        public ResourceLocation getPreviousClassId() {
            return previousClassId;
        }

        public ResourceLocation getNewClassId() {
            return newClassId;
        }
    }

    public static class LevelChanged extends PlayerClassEvent {
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

    public static class Learned extends PlayerClassEvent {

        public Learned(EntityPlayer player, IPlayerData data) {
            super(player, data);
        }
    }
    public static class Removed extends PlayerClassEvent {

        public Removed(EntityPlayer player, IPlayerData data) {
            super(player, data);
        }
    }

    public static class Updated extends PlayerClassEvent {
        private ResourceLocation classId;

        public Updated(EntityPlayer player, IPlayerData data, ResourceLocation classId) {
            super(player, data);
            this.classId = classId;
        }

        public boolean isCurrentClass() {
            return getPlayerData().getClassId().equals(classId);
        }
    }
}
