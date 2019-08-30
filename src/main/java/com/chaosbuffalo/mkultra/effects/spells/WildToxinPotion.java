package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.effects.PassiveEffect;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellTriggers;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Jacob on 6/23/2018.
 */
@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class WildToxinPotion extends PassiveEffect {
    public static final WildToxinPotion INSTANCE = new WildToxinPotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private WildToxinPotion() {
        super(false, 10223410);
        setPotionName("effect.wild_toxin");
        SpellTriggers.ATTACK_ENTITY.register(this, this::onAttackEntity);
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/wild_toxin.png");
    }

    @Override
    public boolean shouldRender(PotionEffect effect) {
        return false;
    }

    private void onAttackEntity(EntityLivingBase player, Entity target, PotionEffect potion) {

        if (target instanceof EntityLivingBase) {

            if (player instanceof EntityPlayer) {
                IPlayerData pData = MKUPlayerData.get((EntityPlayer) player);
                if (pData == null)
                    return;
                if (pData.consumeMana(potion.getAmplifier())) {
                    EntityLivingBase livingTarget = (EntityLivingBase) target;

                    SpellCast toxin = WildToxinEffectPotion.Create(player);
                    livingTarget.addPotionEffect(toxin.setTarget(livingTarget).toPotionEffect(
                            potion.getAmplifier() * 6 * GameConstants.TICKS_PER_SECOND,
                            potion.getAmplifier()));

                    MKUltra.packetHandler.sendToAllAround(
                            new ParticleEffectSpawnPacket(
                                    EnumParticleTypes.SPELL_MOB.getParticleID(),
                                    ParticleEffects.SPHERE_MOTION, 4, 4,
                                    1.0, 1.0, 1.0,
                                    target.posX, target.posY + 1.0f, target.posZ,
                                    1.0,
                                    target.getLookVec()),
                            target, 50.0f);
                } else {
                    player.removePotionEffect(WildToxinPotion.INSTANCE);
                }
            } else {
                EntityLivingBase livingTarget = (EntityLivingBase) target;
                SpellCast toxin = WildToxinEffectPotion.Create(player);
                livingTarget.addPotionEffect(toxin.setTarget(livingTarget).toPotionEffect(
                        potion.getAmplifier() * 6 * GameConstants.TICKS_PER_SECOND,
                        potion.getAmplifier()));
                MKUltra.packetHandler.sendToAllAround(
                        new ParticleEffectSpawnPacket(
                                EnumParticleTypes.SPELL_MOB.getParticleID(),
                                ParticleEffects.SPHERE_MOTION, 4, 4,
                                1.0, 1.0, 1.0,
                                target.posX, target.posY + 1.0f, target.posZ,
                                1.0,
                                target.getLookVec()),
                        target, 50.0f);
            }

        }
    }
}
