package com.chaosbuffalo.mkultra.core.talents;

import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.MKURegistry;
import com.google.common.collect.Maps;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class TalentUtils {
    public static final Map<IAttribute, AttributeModifier> ALL_ATTRIBUTE_MODIFIERS = Maps.newHashMap();

    public static void loadAllAttributes() {
        for (RangedAttributeTalent talent : MKURegistry.getAllAttributeTalents()) {
            AttributeModifier mod = new AttributeModifier(talent.getUUID(),
                    talent.getRegistryName().toString(), 1.0, talent.getOp());
            ALL_ATTRIBUTE_MODIFIERS.put(talent.getAttribute(), mod);
        }
    }

    public static void removeAllAttributeTalents(EntityPlayer player){
        AbstractAttributeMap abstractAttributeMap = player.getAttributeMap();
        Iterator modIterator = ALL_ATTRIBUTE_MODIFIERS.entrySet().iterator();
        while(modIterator.hasNext()) {
            Map.Entry<IAttribute, AttributeModifier> entry = (Map.Entry)modIterator.next();
            IAttributeInstance iattributeinstance = abstractAttributeMap.getAttributeInstance(entry.getKey());
            if (iattributeinstance != null) {
                iattributeinstance.removeModifier(entry.getValue());
            }
        }
    }

    public static void removeAllPassiveTalents(EntityPlayer player){
        ArrayList<PassiveAbilityTalent> passiveTalents = MKURegistry.getAllPassiveTalents();
        IPlayerData data = MKUPlayerData.get(player);
        if (data == null){
            return;
        }
        for (PassiveAbilityTalent talent : passiveTalents){
            if (player.isPotionActive(talent.getAbility().getPassiveEffect())){
                talent.getAbility().removeEffect(player, data, player.world);
            }
        }
    }
}
