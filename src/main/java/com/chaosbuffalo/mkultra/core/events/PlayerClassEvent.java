package com.chaosbuffalo.mkultra.core.events;

import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerClassInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class PlayerClassEvent extends PlayerDataEvent {
    private PlayerClassInfo classInfo;

    private PlayerClassEvent(EntityPlayer player, IPlayerData data, PlayerClassInfo classInfo) {
        super(player, data);
        this.classInfo = classInfo;
    }

    public PlayerClassInfo getClassInfo() {
        return classInfo;
    }

    public static class ClassChanged extends PlayerClassEvent {
        private ResourceLocation previousClassId;

        public ClassChanged(EntityPlayer player, IPlayerData data, PlayerClassInfo classInfo, ResourceLocation previousClassId) {
            super(player, data, classInfo);
            this.previousClassId = previousClassId;
        }

        public ResourceLocation getPreviousClassId() {
            return previousClassId;
        }

        public ResourceLocation getNewClassId() {
            return getClassInfo().getClassId();
        }
    }

    public static class LevelChanged extends PlayerClassEvent {
        private int oldLevel;
        private int newLevel;

        public LevelChanged(EntityPlayer player, IPlayerData data, PlayerClassInfo classInfo, int oldLevel, int newLevel) {
            super(player, data, classInfo);
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

        public Learned(EntityPlayer player, IPlayerData data, PlayerClassInfo classInfo) {
            super(player, data, classInfo);
        }
    }
    public static class Removed extends PlayerClassEvent {

        public Removed(EntityPlayer player, IPlayerData data, PlayerClassInfo classInfo) {
            super(player, data, classInfo);
        }
    }

    public static class Updated extends PlayerClassEvent {

        public Updated(EntityPlayer player, IPlayerData data, PlayerClassInfo classInfo) {
            super(player, data, classInfo);
        }

        public boolean isCurrentClass() {
            return getPlayerData().getClassId().equals(getClassInfo().getClassId());
        }
    }
}
