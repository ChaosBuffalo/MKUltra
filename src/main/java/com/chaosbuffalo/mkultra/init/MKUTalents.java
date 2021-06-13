package com.chaosbuffalo.mkultra.init;

import com.chaosbuffalo.mkcore.core.talents.MKTalent;
import com.chaosbuffalo.mkcore.core.talents.talent_types.PassiveTalent;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.abilities.passives.LifeSiphonAbility;
import com.chaosbuffalo.mkultra.abilities.passives.SoulDrainAbility;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(MKUltra.MODID)
public class MKUTalents {

    @ObjectHolder("talent.soul_drain")
    public static PassiveTalent SOUL_DRAIN_TALENT;

    @ObjectHolder("talent.life_siphon")
    public static PassiveTalent LIFE_SIPHON_TALENT;

    @SubscribeEvent
    public static void registerTalents(RegistryEvent.Register<MKTalent> event) {
        PassiveTalent soulDrain = new PassiveTalent(
                new ResourceLocation(MKUltra.MODID, "talent.soul_drain"),
                SoulDrainAbility.INSTANCE);
        event.getRegistry().register(soulDrain);
        PassiveTalent lifeSiphon = new PassiveTalent(
                new ResourceLocation(MKUltra.MODID, "talent.life_siphon"),
                LifeSiphonAbility.INSTANCE);
        event.getRegistry().register(lifeSiphon);
    }
}
