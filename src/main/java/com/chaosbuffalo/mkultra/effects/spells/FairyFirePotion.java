package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.MKDamageSource;
import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import com.chaosbuffalo.mkultra.core.abilities.ranger.FairyFire;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPeriodicPotionBase;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class FairyFirePotion extends SpellPeriodicPotionBase {
    private static final int DEFAULT_PERIOD = 20;
    public static final UUID MODIFIER_ID = UUID.fromString("777777b8-c161-4b80-897f-724f84e08ae7");

    public static final FairyFirePotion INSTANCE = (FairyFirePotion) new FairyFirePotion()
            .registerPotionAttributeModifier(
                    SharedMonsterAttributes.ARMOR,
                    MODIFIER_ID.toString(),
                    -.25, PlayerAttributes.OP_SCALE_MULTIPLICATIVE);

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private FairyFirePotion() {
        super(DEFAULT_PERIOD, true, 11540991);
        setPotionName("effect.fairy_fire");
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/fairy_fire.png");
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public void doEffect(Entity source, Entity indirectSource, EntityLivingBase target, int amplifier, SpellCast cast) {
        target.attackEntityFrom(MKDamageSource.causeIndirectMagicDamage(FairyFire.INSTANCE.getAbilityId(), source,
                indirectSource, 0.6f), amplifier * 1.0f);
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.SPELL_INSTANT.getParticleID(),
                        ParticleEffects.CIRCLE_MOTION, 35, 10,
                        target.posX, target.posY + 1.0f,
                        target.posZ, .75, 1.0, .75, 1.5,
                        target.getLookVec()),
                target.dimension, target.posX,
                target.posY, target.posZ, 50.0f);
    }
}