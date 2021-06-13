package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkcore.MKCore;
import com.chaosbuffalo.mkcore.effects.PassiveTalentEffect;
import com.chaosbuffalo.mkcore.effects.SpellCast;
import com.chaosbuffalo.mkcore.effects.SpellTriggers;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.abilities.passives.SoulDrainAbility;
import com.chaosbuffalo.mkultra.init.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SoulDrainEffect extends PassiveTalentEffect {

    public static final SoulDrainEffect INSTANCE = new SoulDrainEffect();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Effect> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private SoulDrainEffect() {
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
}

