package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.util.ResourceLocation;

public class ClassClientData {

    public static class SunIcon implements IClassClientData {
        public static SunIcon INSTANCE = new SunIcon();

        @Override
        public ResourceLocation getIconForProvider() {
            return new ResourceLocation(MKUltra.MODID, "textures/class/icons/sun.png");
        }

        @Override
        public String getXpTableText() {
            return "Give your Brouzoufs to Solarius. Receive his blessings.";
        }

        @Override
        public ResourceLocation getXpTableBackground() {
            return new ResourceLocation(MKUltra.MODID, "textures/gui/xp_table_background.png");
        }

        @Override
        public int getXpTableTextColor() {
            return 38600;
        }
    }

    public static class MoonIcon implements IClassClientData {
        public static MoonIcon INSTANCE = new MoonIcon();

        @Override
        public ResourceLocation getIconForProvider() {
            return new ResourceLocation(MKUltra.MODID, "textures/class/icons/moon.png");
        }

        @Override
        public String getXpTableText() {
            return "Thalassa, Goddess of the Moon, demands brouzouf in exchange for her powers.";
        }

        @Override
        public ResourceLocation getXpTableBackground() {
            return new ResourceLocation(MKUltra.MODID, "textures/gui/xp_table_background_moon.png");
        }

        @Override
        public int getXpTableTextColor() {
            return 4404838;
        }
    }

    public static class DesperateIcon implements IClassClientData {
        public static DesperateIcon INSTANCE = new DesperateIcon();

        @Override
        public ResourceLocation getIconForProvider() {
            return new ResourceLocation(MKUltra.MODID, "textures/class/icons/desperate.png");
        }

        @Override
        public String getXpTableText() {
            return "Ydira, Elusive Spirit of the Wood, will increase your powers in exchange for brouzouf.";
        }

        @Override
        public ResourceLocation getXpTableBackground() {
            return new ResourceLocation(MKUltra.MODID, "textures/gui/xp_table_background_desperate.png");
        }

        @Override
        public int getXpTableTextColor() {
            return 32025;
        }
    }
}
