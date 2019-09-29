package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.MKDamageSource;
import com.chaosbuffalo.mkultra.core.abilities.wet_wizard.Undertow;
import com.chaosbuffalo.mkultra.effects.PassiveEffect;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class UndertowPotion extends PassiveEffect {
    public static final UndertowPotion INSTANCE = new UndertowPotion();


    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private UndertowPotion() {
        super(false, 4393423);
        setPotionName("effect.undertow");
        SpellTriggers.ATTACK_ENTITY.register(this, this::onAttackEntity);
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/undertow.png");
    }

    private void onAttackEntity(EntityLivingBase player, Entity target, PotionEffect effect) {
        if (target instanceof EntityLivingBase) {
            EntityLivingBase livingEnt = (EntityLivingBase) target;
            if (livingEnt.isPotionActive(DrownPotion.INSTANCE)) {
                livingEnt.attackEntityFrom(MKDamageSource.causeIndirectMagicDamage(
                        new Undertow().getAbilityId(), livingEnt, player),
                        5.0f * effect.getAmplifier());
            }
        }
    }
}
