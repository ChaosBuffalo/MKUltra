package com.chaosbuffalo.mkultra.extensions;


import com.chaosbuffalo.mknpc.MKNpc;
import com.chaosbuffalo.mknpc.init.MKNpcWorldGen;
import com.chaosbuffalo.mknpc.npc.INpcOptionExtension;
import com.chaosbuffalo.mkultra.init.MKUWorldGen;
import net.minecraftforge.fml.InterModComms;

public class MKUNpcExtensions implements INpcOptionExtension {

    public static void sendExtension() {
        InterModComms.sendTo(MKNpc.MODID, MKNpc.REGISTER_NPC_OPTIONS_EXTENSION,
                MKUNpcExtensions::new);
    }


    @Override
    public void registerNpcOptionExtension() {
//        MKNpcWorldGen.NO_WATER_STRUCTURES.add(MKUWorldGen.CRYPT_STRUCTURE);
        MKNpcWorldGen.NO_WATER_STRUCTURES.add(MKUWorldGen.INTRO_CASTLE);
    }
}
