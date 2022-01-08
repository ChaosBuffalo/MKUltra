package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkcore.MKCore;
import com.chaosbuffalo.mkcore.effects.MKEffect;
import com.chaosbuffalo.mkcore.effects.MKEffectState;
import com.chaosbuffalo.mkcore.effects.MKSimplePassiveState;
import com.chaosbuffalo.mkcore.effects.SpellTriggers;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.abilities.passives.SoulDrainAbility;
import com.chaosbuffalo.mkultra.init.ModSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SoulDrainEffect extends MKEffect {

    public static final SoulDrainEffect INSTANCE = new SoulDrainEffect();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<MKEffect> event) {
        event.getRegistry().register(INSTANCE);
    }

    public SoulDrainEffect() {
        super(EffectType.BENEFICIAL);
        setRegistryName(MKUltra.MODID, "effect.soul_drain");
        SpellTriggers.LIVING_KILL_ENTITY.register(this, this::onLivingKillEntity);
    }

    public void onLivingKillEntity(LivingDeathEvent event, DamageSource source, LivingEntity living) {
        MKCore.getPlayer(living).ifPresent(data -> {
            SoundUtils.playSoundAtEntity(living, ModSounds.spell_dark_4);
            float mana = SoulDrainAbility.INSTANCE.getDrainValue(living);
            data.getStats().addMana(mana);
        });
    }

    @Override
    public MKEffectState makeState() {
        return MKSimplePassiveState.INSTANCE;
    }
}
