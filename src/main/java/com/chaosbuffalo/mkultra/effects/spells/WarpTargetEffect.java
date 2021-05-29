package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkcore.effects.SpellCast;
import com.chaosbuffalo.mkcore.effects.SpellEffectBase;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WarpTargetEffect extends SpellEffectBase {

    public static final WarpTargetEffect INSTANCE = new WarpTargetEffect();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Effect> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private WarpTargetEffect() {
        // boolean isBadEffectIn, int liquidColorIn
        super(EffectType.HARMFUL, 4393423);
        setRegistryName(MKUltra.MODID, "effect.warp_target");
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.ALL;
    }

    @Override
    public void doEffect(Entity applier, Entity caster, LivingEntity target, int amplifier, SpellCast cast) {
        Vector3d playerOrigin = caster.getPositionVec();
        Vector3d heading = caster.getLookVec();
        target.setPositionAndUpdate(playerOrigin.x + heading.x, playerOrigin.y + heading.y + 1.0,
                playerOrigin.z + heading.z);
    }
}
