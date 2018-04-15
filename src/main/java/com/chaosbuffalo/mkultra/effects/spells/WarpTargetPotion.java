package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.mkultra.api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class WarpTargetPotion extends SpellPotionBase {

    public static final WarpTargetPotion INSTANCE = new WarpTargetPotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private WarpTargetPotion() {
        // boolean isBadEffectIn, int liquidColorIn
        super(true, 4393423);
        register(MKUltra.MODID, "effect.warp_target");
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ALL;
    }

    @Override
    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {

        Vec3d playerOrigin = caster.getPositionVector();
        Vec3d heading = caster.getLookVec();

        target.setPositionAndUpdate(playerOrigin.x + heading.x, playerOrigin.y + heading.y + 1.0,
                playerOrigin.z + heading.z);

    }
}
