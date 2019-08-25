package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.effects.songs.SongApplicator;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Set;

/**
 * Created by Jacob on 4/21/2018.
 */
public abstract class PlayerToggleGroupAbility extends PlayerToggleAbility {

    public abstract Set<SpellPotionBase> getToggleGroup();

    public PlayerToggleGroupAbility(String domain, String id) {
        this(new ResourceLocation(domain, id));
    }

    public PlayerToggleGroupAbility(ResourceLocation abilityId) {
        super(abilityId);
    }

    @Override
    public AbilityType getType() {
        return AbilityType.Toggle;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);
        if (entity.getActivePotionEffect(getToggleEffect()) != null) {
            removeEffect(entity, pData, theWorld);
        } else {
            for (SpellPotionBase spellPotion : getToggleGroup()) {
                if (entity.isPotionActive(spellPotion)) {
                    entity.removePotionEffect(spellPotion);
                    if (spellPotion instanceof SongApplicator) {
                        ResourceLocation abilityId = PlayerToggleAbility.getToggleAbilityIdForPotion(spellPotion);
                        if (abilityId != null) {
                            pData.setCooldown(abilityId, pData.getAbilityCooldown(MKURegistry.getAbility(abilityId)));
                        }
                    }
                }
            }
            applyEffect(entity, pData, theWorld);
        }
    }
}
