package com.chaosbuffalo.mkultra.abilities.passives;

import com.chaosbuffalo.mkcore.abilities.MKAbility;
import com.chaosbuffalo.mkcore.abilities.MKPassiveAbility;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.MKEffect;
import com.chaosbuffalo.mkcore.serialization.attributes.FloatAttribute;
import com.chaosbuffalo.mkultra.effects.SoulDrainEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class SoulDrainAbility extends MKPassiveAbility {
    protected final FloatAttribute base = new FloatAttribute("base", 4.0f);
    protected final FloatAttribute scale = new FloatAttribute("scale", 4.0f);

    public SoulDrainAbility() {
        super();
        addSkillAttribute(MKAttributes.EVOCATION);
        addAttributes(base, scale);
    }

    @Override
    public MKEffect getPassiveEffect() {
        return SoulDrainEffect.INSTANCE;
    }

    public float getDrainValue(LivingEntity entity) {
        float skillLevel = MKAbility.getSkillLevel(entity, MKAttributes.EVOCATION);
        return base.value() + scale.value() * skillLevel;
    }

    @Override
    protected Component getAbilityDescription(IMKEntityData entityData) {
        float value = getDrainValue(entityData.getEntity());
        return new TranslatableComponent(getDescriptionTranslationKey(), value);
    }
}
