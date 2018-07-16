package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class GeyserPotion extends SpellPotionBase {

    public static final GeyserPotion INSTANCE = new GeyserPotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source, float base, float scale) {
        return INSTANCE.newSpellCast(source).setScalingParameters(base, scale);
    }

    private GeyserPotion() {
        // boolean isBadEffectIn, int liquidColorIn
        super(true, 4393423);
        setPotionName("effect.geyser");
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ALL;
    }

    @Override
    public boolean canSelfCast() {
        return true;
    }

    @Override
    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {
        int baseDuration = 2 * GameConstants.TICKS_PER_SECOND * amplifier;
        if (Targeting.isValidTarget(Targeting.TargetType.FRIENDLY, caster, target, !canSelfCast())) {
            target.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, baseDuration, amplifier, false, true));
            target.addPotionEffect(FeatherFallPotion.Create(caster).setTarget(target).toPotionEffect(baseDuration + 40, amplifier));
        } else {
            target.attackEntityFrom(DamageSource.causeIndirectMagicDamage(applier, caster), cast.getScaledValue(amplifier));
            target.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, baseDuration * 2, amplifier, false, true));
            target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, baseDuration, amplifier, false, true));
        }
        target.addVelocity(0.0, amplifier * 1.5f, 0.0);
        if (target instanceof EntityPlayerMP && !caster.world.isRemote) {
            ((EntityPlayerMP) target).connection.sendPacket(new SPacketEntityVelocity(target));
        }
    }
}