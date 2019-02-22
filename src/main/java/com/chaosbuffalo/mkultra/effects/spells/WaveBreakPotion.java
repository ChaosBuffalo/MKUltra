package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import com.chaosbuffalo.mkultra.effects.PassiveEffect;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.mkultra.effects.SpellTriggers;
import com.chaosbuffalo.mkultra.party.PartyManager;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Jacob on 3/25/2018.
 */
@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class WaveBreakPotion extends PassiveEffect {
    public static final UUID MODIFIER_ID = UUID.fromString("771f29b8-c161-4b80-897f-724f84e08ae7");
    public static final WaveBreakPotion INSTANCE = (WaveBreakPotion) (new WaveBreakPotion()
            .registerPotionAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, MODIFIER_ID.toString(), 5, PlayerAttributes.OP_INCREMENT)
            .registerPotionAttributeModifier(SharedMonsterAttributes.ARMOR, MODIFIER_ID.toString(), 2, PlayerAttributes.OP_INCREMENT)
            .registerPotionAttributeModifier(PlayerAttributes.MAGIC_ARMOR, MODIFIER_ID.toString(), 2, PlayerAttributes.OP_INCREMENT)
    );

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private WaveBreakPotion() {
        super(false, 1665535);
        setPotionName("effect.wave_break");
        SpellTriggers.ENTITY_HURT_PLAYER.registerPreScale(this::playerHurtPreScale);
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/wave_break.png");
    }

    private void playerHurtPreScale(LivingHurtEvent event, DamageSource source, EntityPlayer livingTarget, IPlayerData targetData) {
        ArrayList<EntityPlayer> teammates = PartyManager.getPlayersOnTeam(livingTarget);
        ArrayList<EntityPlayer> damageAbsorbers = new ArrayList<>();
        for (EntityPlayer teammate : teammates) {
            if (teammate == null) {
                continue;
            }
            if (teammate.isPotionActive(WaveBreakPotion.INSTANCE)) {
                IPlayerData teammatedata = MKUPlayerData.get(teammate);
                if (teammatedata == null) {
                    continue;
                }
                if (teammatedata.getMana() > 0) {
                    damageAbsorbers.add(teammate);
                    teammatedata.setMana(teammatedata.getMana() - 1);
                } else {
                    teammate.removePotionEffect(WaveBreakPotion.INSTANCE);
                }

            }
        }

        int absorbCount = damageAbsorbers.size();
        if (absorbCount > 0) {
            float newDamage = event.getAmount() / (absorbCount + 1);
            for (EntityPlayer absorber : damageAbsorbers) {
                absorber.attackEntityFrom(source, newDamage);
            }
            event.setAmount(newDamage);
        }
    }
}

