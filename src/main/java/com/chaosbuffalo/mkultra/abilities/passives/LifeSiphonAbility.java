package com.chaosbuffalo.mkultra.abilities.passives;

import com.chaosbuffalo.mkcore.abilities.MKAbility;
import com.chaosbuffalo.mkcore.abilities.MKPassiveAbility;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.MKEffect;
import com.chaosbuffalo.mkcore.serialization.attributes.FloatAttribute;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.LifeSiphonEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class LifeSiphonAbility extends MKPassiveAbility {

    public static final LifeSiphonAbility INSTANCE = new LifeSiphonAbility();

    protected final FloatAttribute base = new FloatAttribute("base", 4.0f);
    protected final FloatAttribute scale = new FloatAttribute("scale", 4.0f);
    protected final FloatAttribute modifierScaling = new FloatAttribute("modifierScaling", 1.0f);

    public LifeSiphonAbility() {
        super(new ResourceLocation(MKUltra.MODID, "ability.life_siphon"));
        addSkillAttribute(MKAttributes.NECROMANCY);
        addAttributes(base, scale, modifierScaling);
    }

    public float getHealingValue(LivingEntity entity) {
        int necromancyLevel = MKAbility.getSkillLevel(entity, MKAttributes.NECROMANCY);
        return base.value() + scale.value() * necromancyLevel;
    }

    public float getModifierScaling() {
        return modifierScaling.value();
    }

    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {
        int level = getSkillLevel(entityData.getEntity(), MKAttributes.NECROMANCY);
        ITextComponent valueStr = getHealDescription(entityData, base.value(), scale.value(), level, modifierScaling.value());
        return new TranslationTextComponent(getDescriptionTranslationKey(), valueStr);
    }

    @Override
    public MKEffect getPassiveEffect() {
        return LifeSiphonEffect.INSTANCE;
    }

    @SuppressWarnings("unused")
    @Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    private static class RegisterMe {
        @SubscribeEvent
        public static void register(RegistryEvent.Register<MKAbility> event) {
            event.getRegistry().register(INSTANCE);
        }
    }
}
