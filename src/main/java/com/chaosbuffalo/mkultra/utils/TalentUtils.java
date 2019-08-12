package com.chaosbuffalo.mkultra.utils;

import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.MKURegistry;
import com.chaosbuffalo.mkultra.core.talents.PassiveAbilityTalent;
import com.chaosbuffalo.mkultra.core.talents.RangedAttributeTalent;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;

public class TalentUtils {

    public static void removeAllAttributeTalents(EntityPlayer player) {
        AbstractAttributeMap attributeMap = player.getAttributeMap();
        for (RangedAttributeTalent entry : MKURegistry.getAllAttributeTalents()) {
            IAttributeInstance instance = attributeMap.getAttributeInstance(entry.getAttribute());
            if (instance != null) {
                instance.removeModifier(entry.getUUID());
            }
        }
    }

    public static void removeAllPassiveTalents(EntityPlayer player) {
        ArrayList<PassiveAbilityTalent> passiveTalents = MKURegistry.getAllPassiveTalents();
        IPlayerData data = MKUPlayerData.get(player);
        if (data == null) {
            return;
        }
        for (PassiveAbilityTalent talent : passiveTalents) {
            if (player.isPotionActive(talent.getAbility().getPassiveEffect())) {
                talent.getAbility().removeEffect(player, data, player.world);
            }
        }
    }
}
