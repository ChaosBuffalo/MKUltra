package com.chaosbuffalo.mkultra.effects.spells;

/**
 * Created by Jacob on 7/19/2016.
 */

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.mkultra.effects.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Jacob on 7/16/2016.
 */
@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class UndertowPotion extends SpellPotionBase {

    public static final UndertowPotion INSTANCE = new UndertowPotion();


    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private UndertowPotion() {
        // boolean isBadEffectIn, int liquidColorIn
        super(false, 4393423, new ResourceLocation(MKUltra.MODID, "textures/class/abilities/undertow.png"));
        SpellPotionBase.register("effect.undertow", this);
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.FRIENDLY;
    }

    @Override
    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {

    }

    @Override
    public boolean canSelfCast() {
        return true;
    }

    @Override
    public boolean isReady(int duration, int amplitude) {
        return false;
    }

    @Override
    public boolean isInstant() {
        return false;
    }
}
