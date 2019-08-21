package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerFormulas;
import com.chaosbuffalo.mkultra.effects.PassiveAbilityPotionBase;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class SiphonLifePotion extends PassiveAbilityPotionBase {

    public static final SiphonLifePotion INSTANCE = new SiphonLifePotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private SiphonLifePotion() {
        setPotionName("effect.siphon_life");
        SpellTriggers.PLAYER_KILL_ENTITY.register(this, this::onPlayerKillEntity);
    }

    public void onPlayerKillEntity(LivingDeathEvent event, DamageSource source, EntityPlayer player){
        IPlayerData pData = MKUPlayerData.get(player);
        if (pData != null){
            if (SpellTriggers.isMeleeDamage(source)){
                float healAmount = PlayerFormulas.applyHealBonus(pData, 4.0f);
                player.heal(healAmount);
            }
        }
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/siphon_life.png");
    }
}