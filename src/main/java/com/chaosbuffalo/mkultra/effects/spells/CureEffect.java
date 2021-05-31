package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkcore.effects.SpellCast;
import com.chaosbuffalo.mkcore.effects.SpellEffectBase;
import com.chaosbuffalo.mkcore.fx.ParticleEffects;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.network.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CureEffect extends SpellEffectBase {

    public static final CureEffect INSTANCE = new CureEffect();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Effect> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private CureEffect() {
        super(EffectType.BENEFICIAL, 4393423);
        setRegistryName(MKUltra.MODID, "effect.cure");
    }

    private static void apply(LivingEntity entity, int amplifier) {
        ArrayList<EffectInstance> toRemove = new ArrayList<>();
        int count = 0;
        for (EffectInstance effect : entity.getActivePotionEffects()) {
            if (count > amplifier){
                break;
            }
            if (!effect.getPotion().isBeneficial()) {
                toRemove.add(effect);
                count++;
            }
        }
        for (EffectInstance effect : toRemove) {
            entity.removePotionEffect(effect.getPotion());
        }
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.FRIENDLY;
    }


    @Override
    public void doEffect(Entity applier, Entity caster, LivingEntity target, int amplifier, SpellCast cast) {
        apply(target, amplifier);
        PacketHandler.sendToTrackingAndSelf(
                new ParticleEffectSpawnPacket(
                        ParticleTypes.HAPPY_VILLAGER,
                        ParticleEffects.CIRCLE_PILLAR_MOTION, 30, 0,
                        target.getPosX(), target.getPosY() + 0.05,
                        target.getPosZ(), 1.0, 1.0, 1.0,
                        3.0, 0.0, 0.0, 0.0),
                target);
    }

}