package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.MKDamageSource;
import com.chaosbuffalo.mkultra.core.abilities.WarpCurse;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPeriodicPotionBase;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class WarpCursePotion extends SpellPeriodicPotionBase {

    private static final int DEFAULT_PERIOD = 40;

    public static final WarpCursePotion INSTANCE = new WarpCursePotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private WarpCursePotion() {
        super(DEFAULT_PERIOD, true, 4393423);
        register(MKUltra.MODID, "effect.warp_curse");
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/warp_curse.png");
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public void doEffect(Entity source, Entity indirectSource, EntityLivingBase target, int amplifier, SpellCast cast) {
        Vec3d playerOrigin = target.getPositionVector();
        target.attackEntityFrom(MKDamageSource.causeIndirectMagicDamage(
                new WarpCurse().getAbilityId(), source, indirectSource), amplifier * 3.0f);
        double nextX = playerOrigin.x + (target.getRNG().nextInt(amplifier * 6) - target.getRNG().nextInt(amplifier * 6));
        double nextY = playerOrigin.y + 5.0;
        double nextZ = playerOrigin.z + (target.getRNG().nextInt(amplifier * 6) - target.getRNG().nextInt(amplifier * 6));
        target.setPositionAndUpdate(nextX, nextY, nextZ);
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.LAVA.getParticleID(),
                        ParticleEffects.CIRCLE_MOTION, 50, 10,
                        target.posX, target.posY + 1.0f,
                        target.posZ, .75, 1.0, .75, 1.5,
                        target.getLookVec()),
                target.dimension, target.posX,
                target.posY, target.posZ, 50.0f);
    }
}
