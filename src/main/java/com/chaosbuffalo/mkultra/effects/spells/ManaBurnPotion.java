package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKDamageSource;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.mob_abilities.ManaBurn;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPeriodicPotionBase;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class ManaBurnPotion extends SpellPeriodicPotionBase {

    private static final int DEFAULT_PERIOD = 40;

    public static final ManaBurnPotion INSTANCE = new ManaBurnPotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source, float baseDamage, float damageScale) {
        return INSTANCE.newSpellCast(source).setScalingParameters(baseDamage, damageScale);
    }

    private ManaBurnPotion() {
        super(DEFAULT_PERIOD, true, 4393423);
        setPotionName("effect.mana_burn");
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/mana_burn.png");
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public void doEffect(Entity source, Entity indirectSource, EntityLivingBase target, int amplifier, SpellCast cast) {

        float damage = cast.getScaledValue(amplifier);
        target.attackEntityFrom(MKDamageSource.causeIndirectMagicDamage(ManaBurn.MANA_BURN_ID,
                source, indirectSource), damage);
        if (target instanceof EntityPlayer){
            EntityPlayer playerTarget = (EntityPlayer) target;
            IPlayerData data = MKUPlayerData.get(playerTarget);
            if (data != null){
                data.setMana(Math.max(0, data.getMana() - (int)damage));
            }
        }

        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID(),
                        ParticleEffects.CIRCLE_MOTION, 50, 10,
                        target.posX, target.posY + 1.0f,
                        target.posZ, .75, 1.0, .75, 1.5,
                        target.getLookVec()),
                target.dimension, target.posX,
                target.posY, target.posZ, 50.0f);
    }
}

