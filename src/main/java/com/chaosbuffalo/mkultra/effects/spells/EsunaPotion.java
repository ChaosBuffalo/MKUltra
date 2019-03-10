package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.effects.*;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class EsunaPotion extends SpellPeriodicPotionBase {
    private static final int DEFAULT_PERIOD = 30;

    public static final EsunaPotion INSTANCE = new EsunaPotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private EsunaPotion() {
        super(DEFAULT_PERIOD, false, 4393423);
        setPotionName("effect.esuna");
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/esuna.png");
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.FRIENDLY;
    }

    @Override
    public boolean canSelfCast() {
        return true;
    }

    @Override
    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {

        if (target instanceof EntityPlayer) {
            EntityPlayer targetPlayer = (EntityPlayer) target;
            IPlayerData pData = MKUPlayerData.get(targetPlayer);
            if (pData == null)
                return;

            int boostAmount = 10;
            int amount = 2 * (amplifier);

            int maxMana = pData.getTotalMana() + boostAmount;
            int curMana = pData.getMana();

            amount = Math.min(maxMana - curMana, amount);

            pData.setMana(curMana + amount);
        }

        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.DRIP_WATER.getParticleID(),
                        ParticleEffects.DIRECTED_SPOUT, 60, 1,
                        target.posX, target.posY + 1.0,
                        target.posZ, 1.0, 1.5, 1.0, 1.0,
                        new Vec3d(0., 1.0, 0.0)),
                target.dimension, target.posX, target.posY, target.posZ, 50.0f);
    }
}