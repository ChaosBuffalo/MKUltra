package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerFormulas;
import com.chaosbuffalo.mkultra.effects.PassiveEffect;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SoulDrainPotion extends PassiveEffect {

    public static final SoulDrainPotion INSTANCE = new SoulDrainPotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private SoulDrainPotion() {
        super(false, 4393423);
        setPotionName("effect.soul_drain");
        SpellTriggers.PLAYER_KILL_ENTITY.register(this::onPlayerKillEntity);
    }

    public void onPlayerKillEntity(LivingDeathEvent event, DamageSource source, EntityPlayer player){
        IPlayerData pData = MKUPlayerData.get(player);
        if (player.isPotionActive(this) && pData != null){
            if (SpellTriggers.isMKUltraAbilityDamage(source)){
                pData.setMana(Math.min(pData.getMana() + 4, pData.getTotalMana()));
            }
        }
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/soul_drain.png");
    }
}
