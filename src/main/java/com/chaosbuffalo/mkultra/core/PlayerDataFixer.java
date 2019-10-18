package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.event.EntityEventHandler;
import com.chaosbuffalo.mkultra.init.ModFixes;
import com.chaosbuffalo.mkultra.log.Log;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.FixTypes;
import net.minecraftforge.common.util.ModFixs;

public class PlayerDataFixer {
    private static final String CAP_ID = EntityEventHandler.PLAYER_DATA.toString();

    public static void init(ModFixs fixer) {
        fixer.registerFix(FixTypes.PLAYER, new UpgradeAbilitiesV1());
    }

    public static class UpgradeAbilitiesV1 extends ModFixes.CapabilityDataFix {

        public UpgradeAbilitiesV1() {
            super(CAP_ID, 0);
        }

        @Override
        protected void fixCapability(NBTTagCompound compound) {
            Log.info("!!!!!!!!!!!!!!!player");
            Log.info("%s", compound);
        }
    }
}
