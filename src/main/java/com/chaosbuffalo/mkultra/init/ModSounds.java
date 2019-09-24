package com.chaosbuffalo.mkultra.init;

import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;


@Mod.EventBusSubscriber
@GameRegistry.ObjectHolder(MKUltra.MODID)
public class ModSounds {

    @GameRegistry.ObjectHolder("casting_general")
    public static SoundEvent casting_general;

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> evt){
        ResourceLocation cg_loc = new ResourceLocation(MKUltra.MODID, "casting_general");
        SoundEvent cg = new SoundEvent(cg_loc).setRegistryName(cg_loc);
        evt.getRegistry().register(cg);

    }

}
