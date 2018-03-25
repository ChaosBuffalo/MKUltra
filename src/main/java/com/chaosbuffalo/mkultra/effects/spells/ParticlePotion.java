package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.mkultra.effects.Targeting;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class ParticlePotion extends SpellPotionBase {

    public static final ParticlePotion INSTANCE = new ParticlePotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static SpellCast Create(Entity source, int particleId, int motionType, boolean includeSelf,
                                   Vec3d radius, Vec3d offsets, int particleCount, int particleData,
                                   double particleSpeed) {
        SpellCast cast = INSTANCE.newSpellCast(source);
        INSTANCE.setParameters(cast, particleId, motionType, includeSelf, radius, offsets, particleCount, particleData, particleSpeed);
        return cast;
    }

    protected ParticlePotion() {
        super(true, 123);
        SpellPotionBase.register("effect.particle_potion", this);
    }

    private ParticlePotion setParameters(SpellCast cast, int particleId, int motionType, boolean includeSelf,
                                         Vec3d radius, Vec3d offsets, int particleCount,
                                         int particleData, double particleSpeed) {

        cast.setBoolean("includeSelf", includeSelf);

        cast.setInt("particleId", particleId);
        cast.setInt("particleCount", particleCount);
        cast.setInt("particleData", particleData);
        cast.setDouble("particleSpeed", particleSpeed);
        cast.setInt("motionType", motionType);

        cast.setDouble("radiusX", radius.x);
        cast.setDouble("radiusY", radius.y);
        cast.setDouble("radiusZ", radius.z);

        cast.setDouble("xOffset", offsets.x);
        cast.setDouble("yOffset", offsets.y);
        cast.setDouble("zOffset", offsets.z);
        return this;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ALL;
    }

    @Override
    public boolean canSelfCast() {
        // Since this can be configured per-cast we return true here and then filter in doEffect where we have the
        // SpellCast object carrying the state
        return true;
    }

    @Override
    public void doEffect(Entity applier, Entity caster,
                         EntityLivingBase target, int amplifier, SpellCast cast) {

        // Check canSelfCast here because
        if (!cast.getBoolean("includeSelf") && target.equals(caster)) {
            return;
        }

        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        cast.getInt("particleId"),
                        cast.getInt("motionType"),
                        cast.getInt("particleCount"),
                        cast.getInt("particleData"),
                        target.posX + cast.getDouble("xOffset"),
                        target.posY + cast.getDouble("yOffset"),
                        target.posZ + cast.getDouble("zOffset"),
                        cast.getDouble("radiusX"),
                        cast.getDouble("radiusY"),
                        cast.getDouble("radiusZ"),
                        cast.getDouble("particleSpeed"),
                        caster.getPositionVector().subtract(target.getPositionVector()).normalize()),
                target.dimension, target.posX,
                target.posY, target.posZ, 50.0f);
    }
}
