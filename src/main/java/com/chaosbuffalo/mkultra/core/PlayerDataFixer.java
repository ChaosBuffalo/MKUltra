package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.event.EntityEventHandler;
import com.chaosbuffalo.mkultra.init.ModFixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.FixTypes;
import net.minecraftforge.common.util.Constants;
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
            if (compound.hasKey("allSkills")) {
                NBTTagList skills = compound.getTagList("allSkills", Constants.NBT.TAG_COMPOUND);
                compound.setTag("abilities", skills);
                for (int i = 0; i < skills.tagCount(); i++) {
                    NBTTagCompound sk = skills.getCompoundTagAt(i);
                    if (sk.hasKey("level", Constants.NBT.TAG_INT)) {
                        sk.setInteger("rank", sk.getInteger("level"));
                    }
                }
            }
        }
    }
}
