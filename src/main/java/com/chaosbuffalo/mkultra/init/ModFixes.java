//package com.chaosbuffalo.mkultra.init;
//
//import com.chaosbuffalo.mkultra.MKUltra;
//import com.chaosbuffalo.mkultra.core.PlayerDataFixer;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.util.datafix.IFixableData;
//import net.minecraftforge.common.util.Constants;
//import net.minecraftforge.common.util.ModFixs;
//import net.minecraftforge.fml.common.FMLCommonHandler;
//
//import javax.annotation.Nonnull;
//
//public class ModFixes {
//
//    static final int DATA_FIXER_VERSION = 1;
//
//    public static void init() {
//        ModFixs fixes = FMLCommonHandler.instance().getDataFixer().init(MKUltra.MODID, DATA_FIXER_VERSION);
//        PlayerDataFixer.init(fixes);
//    }
//
//    public static abstract class CapabilityDataFix implements IFixableData {
//        private String capName;
//        private int version;
//
//        protected CapabilityDataFix(String capName, int version) {
//            this.capName = capName;
//            this.version = version;
//        }
//
//        @Override
//        public int getFixVersion() {
//            return version;
//        }
//
//        @Nonnull
//        @Override
//        public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
//            if (compound.hasKey("ForgeCaps", Constants.NBT.TAG_COMPOUND)) {
//                NBTTagCompound caps = compound.getCompoundTag("ForgeCaps");
//                if (caps.hasKey(capName, Constants.NBT.TAG_COMPOUND)) {
//                    NBTTagCompound cap = caps.getCompoundTag(capName);
//                    fixCapability(cap);
//                }
//            }
//            return compound;
//        }
//
//        protected abstract void fixCapability(NBTTagCompound compound);
//    }
//}
