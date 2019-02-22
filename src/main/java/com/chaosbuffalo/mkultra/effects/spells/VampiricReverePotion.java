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
public class VampiricReverePotion extends PassiveEffect {

    public static final VampiricReverePotion INSTANCE = new VampiricReverePotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private VampiricReverePotion() {
        super(false, 4393423);
        setPotionName("effect.vampiric_revere");
        SpellTriggers.PLAYER_HURT_ENTITY.registerMelee(this::onMelee);
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/vampiric_revere.png");
    }

    private void onMelee(LivingHurtEvent event, DamageSource source, EntityLivingBase livingTarget, EntityPlayerMP playerSource, IPlayerData sourceData) {
        PotionEffect potion = playerSource.getActivePotionEffect(INSTANCE);
        if (potion != null && sourceData.getMana() > 0) {
            sourceData.setMana(sourceData.getMana() - 1);
            float healAmount = event.getAmount() * .25f * potion.getAmplifier();
            healAmount = PlayerFormulas.applyHealBonus(sourceData, healAmount);
            playerSource.heal(healAmount);
        }
    }
}

