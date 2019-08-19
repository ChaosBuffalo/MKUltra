package com.chaosbuffalo.mkultra.core.abilities.passives;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.PlayerPassiveAbility;
import com.chaosbuffalo.mkultra.effects.PassiveAbilityPotionBase;
import com.chaosbuffalo.mkultra.effects.spells.BurningSoulPotion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class BurningSoul extends PlayerPassiveAbility {

    public static final BurningSoul INSTANCE = new BurningSoul();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<PlayerAbility> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public BurningSoul() {
        super(MKUltra.MODID, "ability.burning_soul");
    }

    @Override
    public PassiveAbilityPotionBase getPassiveEffect() {
        return BurningSoulPotion.INSTANCE;
    }

    @Override
    public void applyEffect(EntityPlayer entity, IPlayerData pData, World theWorld) {
        entity.addPotionEffect(getPassiveEffect().createInstance(entity));
    }

}
