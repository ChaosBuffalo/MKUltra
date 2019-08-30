package com.chaosbuffalo.mkultra.effects.spells;

/**
 * Created by Jacob on 4/22/2018.
 */

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerFormulas;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.songs.SongEffect;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class SwiftsRodeoHBPotion extends SongEffect {
    public static final SwiftsRodeoHBPotion INSTANCE = new SwiftsRodeoHBPotion();
    public static final int PERIOD = 6 * GameConstants.TICKS_PER_SECOND;

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    @Override
    public AreaEffectBuilder prepareAreaEffect(EntityPlayer source, IPlayerData playerData, int level, AreaEffectBuilder builder) {
        int duration = PlayerFormulas.applyBuffDurationBonus(playerData, PERIOD);
        builder.effect(new PotionEffect(MobEffects.SPEED, duration, level), Targeting.TargetType.FRIENDLY);
        return builder;
    }

    private SwiftsRodeoHBPotion() {
        super(PERIOD, false, 65330);
        setPotionName("effect.swifts_rodeo_hb");
    }

    @Override
    public EnumParticleTypes getSongParticle() {
        return EnumParticleTypes.NOTE;
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/swifts_rodeo_heartbreak.png");
    }


    @Override
    public float getSongDistance(int level) {
        return 2.0f + level * 4.0f;
    }
}

