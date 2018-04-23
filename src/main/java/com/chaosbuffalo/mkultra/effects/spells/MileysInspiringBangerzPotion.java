package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.SongPotionBase;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jacob on 4/22/2018.
 */
@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class MileysInspiringBangerzPotion extends SongPotionBase {
    public static final MileysInspiringBangerzPotion INSTANCE = new MileysInspiringBangerzPotion();
    public static final int PERIOD = 4 * GameConstants.TICKS_PER_SECOND;

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    @Override
    public Set<PotionEffect> getPotionsToApply(Entity source, int level) {
        HashSet<PotionEffect> ret = new HashSet<>();
        ret.add(new PotionEffect(MobEffects.ABSORPTION, PERIOD, level - 1));
        ret.add(new PotionEffect(MobEffects.RESISTANCE, PERIOD, level));
        ret.add(new PotionEffect(MobEffects.REGENERATION, PERIOD, level));
        return ret;
    }

    private MileysInspiringBangerzPotion() {
        super(PERIOD, false, true, false, 16762880);
        register(MKUltra.MODID, "effect.mileys_bangerz");
    }

    @Override
    public EnumParticleTypes getSongParticle() { return EnumParticleTypes.SPELL; }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/mileys_bangerz.png");
    }

    @Override
    public ResourceLocation getAssociatedAbilityId() {
        return new ResourceLocation("mkultra", "ability.mileys_bangerz");
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.FRIENDLY;
    }

    @Override
    public float getDistance(int level) {
        return 2.0f + level * 4.0f;
    }
}

