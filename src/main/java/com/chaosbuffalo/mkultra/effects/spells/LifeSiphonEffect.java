package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkcore.MKCore;
import com.chaosbuffalo.mkcore.core.healing.MKHealSource;
import com.chaosbuffalo.mkcore.core.healing.MKHealing;
import com.chaosbuffalo.mkcore.effects.PassiveTalentEffect;
import com.chaosbuffalo.mkcore.effects.SpellCast;
import com.chaosbuffalo.mkcore.effects.SpellTriggers;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.abilities.passives.LifeSiphonAbility;
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
public class LifeSiphonEffect extends PassiveTalentEffect {

    public static final LifeSiphonEffect INSTANCE = new LifeSiphonEffect();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Effect> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private LifeSiphonEffect() {
        setRegistryName(MKUltra.MODID, "effect.life_siphon");
        SpellTriggers.LIVING_KILL_ENTITY.register(this, this::onLivingKillEntity);
    }

    public void onLivingKillEntity(LivingDeathEvent event, DamageSource source, LivingEntity living) {
        MKCore.getEntityData(living).ifPresent(data -> {
            SoundUtils.playSoundAtEntity(living, ModSounds.spell_dark_5);
            MKHealSource healSource = new MKHealSource(LifeSiphonAbility.INSTANCE.getAbilityId(), living, living,
                    CoreDamageTypes.ShadowDamage, LifeSiphonAbility.INSTANCE.getModifierScaling());
            MKHealing.healEntityFrom(living, LifeSiphonAbility.INSTANCE.getHealingValue(living), healSource);
        });
    }
}