package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MKUMobData;
import com.chaosbuffalo.mkultra.core.abilities.brawler.Yank;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.targeting_api.Targeting;
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

    public static final YankPotion INSTANCE = new YankPotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source, EntityLivingBase target) {
        return INSTANCE.newSpellCast(source).setTarget(target);
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private YankPotion() {
        // boolean isBadEffectIn, int liquidColorIn
        super(true, 4393423);
        setPotionName("effect.yank");
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

        Vec3d playerOrigin = applier.getPositionVector();
        Vec3d targetPos = target.getPositionVector();
        Vec3d awayFrom = playerOrigin.subtract(targetPos).normalize().scale(
                Yank.BASE_FORCE + Yank.FORCE_SCALE * amplifier);
        if (target.equals(caster)) {
            return;
        }
        IMobData mobData = MKUMobData.get(target);
        if (mobData != null) {
            if (mobData.isBoss()) {
                return;
            }
        }
        target.addVelocity(awayFrom.x, awayFrom.y, awayFrom.z);
        if (target instanceof EntityPlayerMP && !caster.world.isRemote) {
            ((EntityPlayerMP) target).connection.sendPacket(new SPacketEntityVelocity(target));
        }
    }
}
