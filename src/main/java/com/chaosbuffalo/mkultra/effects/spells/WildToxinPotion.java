package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Jacob on 6/23/2018.
 */
@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class WildToxinPotion extends SpellPotionBase {
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
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/wild_toxin.png");
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.FRIENDLY;
    }

    @Override
    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {

    }

    @Override
    public boolean canSelfCast() {
        return true;
    }

    @Override
    public boolean isReady(int duration, int amplitude) {
        return false;
    }

    @Override
    public boolean isInstant() {
        return false;
    }


    public void onAttackEntity(EntityPlayer player, Entity target, PotionEffect potion) {

        if (target instanceof EntityLivingBase) {
            IPlayerData pData = MKUPlayerData.get(player);
            if (pData.getMana() >= potion.getAmplifier()) {
                pData.setMana(pData.getMana() - potion.getAmplifier());
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
        }
    }
}
