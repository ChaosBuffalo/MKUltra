package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.abilities.NotoriousDOT;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SongPotionBase;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;


/**
 * Created by Jacob on 4/21/2018.
 */
@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class NotoriousDOTPotion extends SongPotionBase{
    public static final NotoriousDOTPotion INSTANCE = new NotoriousDOTPotion();
    public static final int PERIOD = 3 * GameConstants.TICKS_PER_SECOND;

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    @Override
    public AreaEffectBuilder prepareAreaEffect(Entity source, int level, AreaEffectBuilder builder){
        builder.spellCast(InstantIndirectMagicDamagePotion.Create(
                source, NotoriousDOT.BASE_DAMAGE, NotoriousDOT.DAMAGE_SCALE),
                level, Targeting.TargetType.ENEMY);
        return builder;
    }

    private NotoriousDOTPotion() {
        super(PERIOD, false, true, false, 16750080);
        register(MKUltra.MODID, "effect.notorious_dot");
    }

    @Override
    public ResourceLocation getAssociatedAbilityId() {
        return new ResourceLocation("mkultra", "ability.notorious_dot");
    }

    @Override
    public EnumParticleTypes getSongParticle() { return EnumParticleTypes.DAMAGE_INDICATOR; }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/notorious_dot.png");
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.SELF;
    }

    @Override
    public float getDistance(int level) {
        return 3.0f + level * 3.0f;
    }
}
