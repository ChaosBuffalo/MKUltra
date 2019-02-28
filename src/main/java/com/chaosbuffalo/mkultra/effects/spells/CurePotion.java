package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class CurePotion extends SpellPotionBase {

    public static final CurePotion INSTANCE = new CurePotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private CurePotion() {
        // boolean isBadEffectIn, int liquidColorIn
        super(false, 4393423);
        setPotionName("effect.cure");
    }

    private static void apply(EntityLivingBase entity) {
        ArrayList<PotionEffect> toRemove = new ArrayList<>();
        for (PotionEffect potioneffect : entity.getActivePotionEffects()) {
            if (potioneffect.getPotion().isBadEffect()) {
                toRemove.add(potioneffect);
            }
        }
        for (PotionEffect potioneffect : toRemove) {
            entity.removePotionEffect(potioneffect.getPotion());
        }
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.FRIENDLY;
    }

    @Override
    public boolean canSelfCast() {
        return true;
    }

    @Override
    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {

        apply(target);
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.VILLAGER_HAPPY.getParticleID(),
                        ParticleEffects.CIRCLE_PILLAR_MOTION, 50, 0,
                        target.posX, target.posY + 0.05,
                        target.posZ, 1.0, 1.0, 1.0, 3.0, 0.0, 0.0, 0.0),
                target.dimension, target.posX,
                target.posY, target.posZ, 50.0f);
    }

}