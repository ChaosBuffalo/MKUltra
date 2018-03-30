package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.mkultra.effects.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Jacob on 3/25/2018.
 */
@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class HeavingSeasPotion extends SpellPotionBase {

    public static final HeavingSeasPotion INSTANCE = new HeavingSeasPotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private HeavingSeasPotion() {
        // boolean isBadEffectIn, int liquidColorIn
        super(true, 1665535);
        SpellPotionBase.register(MKUltra.MODID, "effect.heavingseas", this);
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public boolean canSelfCast() {
        return true;
    }

    @Override
    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {

        target.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 1 * 20 * amplifier, amplifier, false, true));
        target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 1 * 20 * amplifier, amplifier, false, true));
        target.addVelocity(0.0, amplifier * 1.25f, 0.0);
        if (target instanceof EntityPlayerMP && !caster.world.isRemote) {
            ((EntityPlayerMP) target).connection.sendPacket(new SPacketEntityVelocity(target));
        }
    }
}