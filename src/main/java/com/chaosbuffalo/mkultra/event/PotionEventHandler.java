package com.chaosbuffalo.mkultra.event;

import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.Targeting;
import com.chaosbuffalo.mkultra.effects.spells.*;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerDataProvider;
import com.chaosbuffalo.mkultra.core.abilities.FlameBlade;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber
public class PotionEventHandler {

    public static boolean isPlayerPhysicalDamage(DamageSource source) {
        return (!source.isFireDamage() && !source.isExplosion() && !source.isMagicDamage() &&
                source.getDamageType().equals("player"));
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        EntityLivingBase living = event.getEntityLiving();
        if (source == DamageSource.FALL) { // TODO: maybe just use LivingFallEvent?
            EntityLivingBase entity = event.getEntityLiving();
            if (entity.isPotionActive(FeatherFallPotion.INSTANCE)) {
                event.setAmount(0.0f);
                if (entity instanceof EntityPlayer) {
                    entity.sendMessage(new TextComponentString("My legs are OK"));
                }
            }
        } else if (source.getTrueSource() != null && source.getTrueSource() instanceof EntityPlayer) {
            EntityPlayer sourceEntity = (EntityPlayer) source.getTrueSource();
            IPlayerData data = PlayerDataProvider.get(sourceEntity);
            if (data == null)
                return;

            PotionEffect potion = sourceEntity.getActivePotionEffect(VampiricReverePotion.INSTANCE);
            if (potion != null && isPlayerPhysicalDamage(source) && data.getMana() > 0) {
                data.setMana(data.getMana() - 1);
                sourceEntity.heal(event.getAmount() * .15f * potion.getAmplifier());
            }

            potion = sourceEntity.getActivePotionEffect(NocturnalCommunionPotion.INSTANCE);
            if (potion != null) {
                sourceEntity.heal(event.getAmount() * .1f * potion.getAmplifier());
            }
        } else if (living.getActivePotionEffect(MoonTrancePotion.INSTANCE) != null) {
            PotionEffect effect = living.getActivePotionEffect(MoonTrancePotion.INSTANCE);
            if (event.getSource().getTrueSource() instanceof EntityLivingBase) {
                EntityLivingBase attacker = (EntityLivingBase) event.getSource().getTrueSource();
                attacker.attackEntityFrom(DamageSource.causeIndirectMagicDamage(living, living),
                        Math.min(event.getAmount(), 4.0f * effect.getAmplifier()));
            }


        }
    }

    @SubscribeEvent
    public static void onAttackEntityEvent(AttackEntityEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        Entity target = event.getTarget();
        PotionEffect potion = player.getActivePotionEffect(UndertowPotion.INSTANCE);
        if (potion != null) {
            if (target instanceof EntityLivingBase) {
                EntityLivingBase livingEnt = (EntityLivingBase) target;
                PotionEffect drownEffect = livingEnt.getActivePotionEffect(DrownPotion.INSTANCE);
                if (drownEffect != null) {
                    livingEnt.attackEntityFrom(DamageSource.causeIndirectMagicDamage(livingEnt, player),
                            5.0f * potion.getAmplifier());
                }
            }

        }
        potion = player.getActivePotionEffect(FlameBladePotion.INSTANCE);
        if (potion != null) {

            SpellCast flames = FlameBladeEffectPotion.Create(player, FlameBlade.BASE_DAMAGE, FlameBlade.DAMAGE_SCALE);
            SpellCast particles = ParticlePotion.Create(player,
                    EnumParticleTypes.LAVA.getParticleID(),
                    ParticleEffects.SPHERE_MOTION, false, new Vec3d(1.0, 1.0, 1.0),
                    new Vec3d(0.0, 1.0, 0.0), 15, 5, 1.0);

            AreaEffectBuilder.Create(player, target)
                    .spellCast(flames, potion.getAmplifier(), Targeting.TargetType.ENEMY)
                    .spellCast(particles, potion.getAmplifier(), Targeting.TargetType.ENEMY)
                    .duration(6).waitTime(0)
                    .color(16737305).radius(2.0f, true)
                    .particle(EnumParticleTypes.LAVA)
                    .spawn();
        }


    }
}
