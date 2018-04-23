package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.SongPotionBase;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
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
public class MileysInspiringBangerzSongPotion extends SongPotionBase {

    public static final MileysInspiringBangerzSongPotion INSTANCE = new MileysInspiringBangerzSongPotion();

    public static final int PERIOD = 18 * GameConstants.TICKS_PER_SECOND;

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private MileysInspiringBangerzSongPotion() {
        super(PERIOD, true, false, false, 16762880);
        register(MKUltra.MODID, "effect.mileys_bangerz_song");
    }

    @Override
    public ResourceLocation getAssociatedAbilityId() {
        return new ResourceLocation("mkultra", "ability.mileys_bangerz");
    }

    @Override
    public Set<SpellCast> getSpellCasts(Entity source) {
        HashSet<SpellCast> ret = new HashSet<SpellCast>();
        ret.add(MileysInspiringBangerzPotion.Create(source));
        return ret;
    }


    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/mileys_bangerz.png");
    }


    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.SELF;
    }

    @Override
    public float getDistance(int level) {
        return 1.0f;
    }
}

