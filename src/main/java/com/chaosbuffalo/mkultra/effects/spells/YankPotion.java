package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.mkultra.effects.Targeting;
import com.chaosbuffalo.mkultra.core.abilities.Yank;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class YankPotion extends SpellPotionBase {

    public static YankPotion INSTANCE = new YankPotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static SpellCast Create(Entity source, EntityLivingBase target) {
        return INSTANCE.newSpellCast(source).setTarget(target);
    }

    private YankPotion() {
        // boolean isBadEffectIn, int liquidColorIn
        super(true, 4393423);
        SpellPotionBase.register("effect.yank", this);
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ALL;
    }

    @Override
    public boolean canSelfCast() {
        return false;
    }

    @Override
    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {

        Vec3d playerOrigin = caster.getPositionVector();
        Vec3d targetPos = target.getPositionVector();
        Vec3d awayFrom = playerOrigin.subtract(targetPos).normalize().scale(
                Yank.BASE_FORCE + Yank.FORCE_SCALE * amplifier);
        if (target.equals(caster)) {
            return;
        }
        target.addVelocity(awayFrom.x, awayFrom.y, awayFrom.z);
        if (target instanceof EntityPlayerMP && !caster.world.isRemote) {
            ((EntityPlayerMP) target).connection.sendPacket(new SPacketEntityVelocity(target));
        }
    }
}
