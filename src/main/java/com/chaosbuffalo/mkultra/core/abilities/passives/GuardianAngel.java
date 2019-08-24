package com.chaosbuffalo.mkultra.core.abilities.passives;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.PlayerPassiveAbility;
import com.chaosbuffalo.mkultra.effects.passives.PassiveAbilityPotionBase;
import com.chaosbuffalo.mkultra.effects.spells.GuardianAngelPotion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class GuardianAngel extends PlayerPassiveAbility {

    public static final GuardianAngel INSTANCE = new GuardianAngel();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<PlayerAbility> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public GuardianAngel() {
        super(MKUltra.MODID, "ability.guardian_angel");
    }

    @Override
    public PassiveAbilityPotionBase getPassiveEffect() {
        return GuardianAngelPotion.INSTANCE;
    }

    @Override
    public void applyEffect(EntityPlayer entity, IPlayerData pData, World theWorld) {
        entity.addPotionEffect(getPassiveEffect().createInstance(entity));
    }
}
