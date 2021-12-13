package com.chaosbuffalo.mkultra.abilities.passives;

import com.chaosbuffalo.mkcore.abilities.MKAbility;
import com.chaosbuffalo.mkcore.abilities.PassiveTalentAbility;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.PassiveTalentEffect;
import com.chaosbuffalo.mkcore.serialization.attributes.FloatAttribute;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.spells.LifeSiphonEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LifeSiphonAbility extends PassiveTalentAbility {

    public static final LifeSiphonAbility INSTANCE = new LifeSiphonAbility();

    protected final FloatAttribute base = new FloatAttribute("base", 4.0f);
    protected final FloatAttribute scale = new FloatAttribute("scale", 4.0f);
    protected final FloatAttribute modifierScaling = new FloatAttribute("modifierScaling", 1.0f);

    @SubscribeEvent
    public static void register(RegistryEvent.Register<MKAbility> event) {
        event.getRegistry().register(INSTANCE);
    }

    public float getHealingValue(LivingEntity entity){
        int necromancyLevel = MKAbility.getSkillLevel(entity, MKAttributes.NECROMANCY);
        return base.getValue() + scale.getValue() * necromancyLevel;
    }

    public float getModifierScaling(){
        return modifierScaling.getValue();
    }

    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {
        int level = getSkillLevel(entityData.getEntity(), MKAttributes.NECROMANCY);
        ITextComponent valueStr = getHealDescription(entityData, base.getValue(), scale.getValue(), level,
                modifierScaling.getValue());

        return new TranslationTextComponent(getDescriptionTranslationKey(), valueStr);
    }

    public LifeSiphonAbility() {
        super(new ResourceLocation(MKUltra.MODID, "ability.life_siphon"));
        addSkillAttribute(MKAttributes.NECROMANCY);
        addAttributes(base, scale, modifierScaling);
    }

    @Override
    public PassiveTalentEffect getPassiveEffect() {
        return LifeSiphonEffect.INSTANCE;
    }
}
