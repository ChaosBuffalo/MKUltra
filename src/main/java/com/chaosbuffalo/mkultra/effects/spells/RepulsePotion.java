package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.mkultra.api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class RepulsePotion extends SpellPotionBase {

    public static final RepulsePotion INSTANCE = new RepulsePotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static SpellCast Create(Entity source, float base, float scale) {
        return INSTANCE.newSpellCast(source).setScalingParameters(base, scale);
    }

    private RepulsePotion() {
        // boolean isBadEffectIn, int liquidColorIn
        super(true, 4393423);
        register(MKUltra.MODID, "effect.repulse");
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {
        EntityPlayer player = (EntityPlayer) caster;
        Vec3d playerOrigin = player.getPositionVector();
        Vec3d targetPos = target.getPositionVector();
        Vec3d awayFrom = targetPos.subtract(playerOrigin).normalize().scale(cast.getScaledValue(amplifier));
        if (target.equals(caster) || target.isOnSameTeam(caster)) {
            return;
        }
        target.addVelocity(awayFrom.x, awayFrom.y, awayFrom.z);
        if (target instanceof EntityPlayerMP && !player.world.isRemote) {
            ((EntityPlayerMP) target).connection.sendPacket(new SPacketEntityVelocity(target));
        }
    }
}