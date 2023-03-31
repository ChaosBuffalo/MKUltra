package com.chaosbuffalo.mkultra.abilities.passives;

import com.chaosbuffalo.mkcore.abilities.MKAbility;
import com.chaosbuffalo.mkcore.abilities.MKPassiveAbility;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.MKEffect;
import com.chaosbuffalo.mkcore.serialization.attributes.FloatAttribute;
import com.chaosbuffalo.mkultra.effects.LifeSiphonEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class LifeSiphonAbility extends MKPassiveAbility {
    protected final FloatAttribute base = new FloatAttribute("base", 4.0f);
    protected final FloatAttribute scale = new FloatAttribute("scale", 4.0f);
    protected final FloatAttribute modifierScaling = new FloatAttribute("modifierScaling", 1.0f);

    public LifeSiphonAbility() {
        super();
        addSkillAttribute(MKAttributes.NECROMANCY);
        addAttributes(base, scale, modifierScaling);
    }

    public float getHealingValue(LivingEntity entity) {
        float necromancyLevel = MKAbility.getSkillLevel(entity, MKAttributes.NECROMANCY);
        return base.value() + scale.value() * necromancyLevel;
    }

    public float getModifierScaling() {
        return modifierScaling.value();
    }

    @Override
    protected Component getAbilityDescription(IMKEntityData entityData) {
        float level = getSkillLevel(entityData.getEntity(), MKAttributes.NECROMANCY);
        Component valueStr = getHealDescription(entityData, base.value(), scale.value(), level, modifierScaling.value());
        return new TranslatableComponent(getDescriptionTranslationKey(), valueStr);
    }

    @Override
    public MKEffect getPassiveEffect() {
        return LifeSiphonEffect.INSTANCE;
    }

}
