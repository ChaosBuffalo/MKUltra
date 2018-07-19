package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.songs.SongApplicator;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Set;


/**
 * Created by Jacob on 4/21/2018.
 */
@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class NotoriousDOTSongPotion extends SongApplicator {
    public static final NotoriousDOTSongPotion INSTANCE = new NotoriousDOTSongPotion();

    public static final int PERIOD = 18 * GameConstants.TICKS_PER_SECOND;

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private NotoriousDOTSongPotion() {
        super(PERIOD, false, 16750080);
        setPotionName("effect.notorious_dot_song");
    }

    @Override
    public Set<SpellCast> getSpellCasts(Entity source) {
        Set<SpellCast> ret = super.getSpellCasts(source);
        ret.add(NotoriousDOTPotion.Create(source));
        return ret;
    }


    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/notorious_dot.png");
    }

}
