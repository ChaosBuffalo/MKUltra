package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerFormulas;
import com.chaosbuffalo.mkultra.effects.PassiveEffect;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.mkultra.effects.SpellTriggers;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Jacob on 3/24/2018.
 */
@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class NocturnalCommunionPotion extends PassiveEffect {

    public static final NocturnalCommunionPotion INSTANCE = new NocturnalCommunionPotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private NocturnalCommunionPotion() {
        super(false, 4393423);
        setPotionName("effect.nocturnal_communion");
        SpellTriggers.PLAYER_HURT_ENTITY.registerPostHandler(this::onPlayerHurtEntity);
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/nocturnal_communion.png");
    }

    private void onPlayerHurtEntity(LivingHurtEvent event, DamageSource source, EntityLivingBase livingTarget, EntityPlayerMP playerSource, IPlayerData sourceData) {
        PotionEffect potion = playerSource.getActivePotionEffect(NocturnalCommunionPotion.INSTANCE);
        if (potion != null) {
            float healAmount = event.getAmount() * .20f * potion.getAmplifier();
            healAmount = PlayerFormulas.applyHealBonus(sourceData, healAmount);
            playerSource.heal(healAmount);
        }
    }
}

