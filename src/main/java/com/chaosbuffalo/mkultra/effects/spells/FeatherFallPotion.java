package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.PassiveEffect;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class FeatherFallPotion extends PassiveEffect {

    public static final FeatherFallPotion INSTANCE = new FeatherFallPotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private FeatherFallPotion() {
        super(false, 4393423);
        setPotionName("effect.featherfall");
        SpellTriggers.FALL.register(this::onFall);
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/featherfall.png");
    }

    private void onFall(LivingHurtEvent event, DamageSource source, EntityLivingBase entity) {
        if (entity.isPotionActive(INSTANCE)) {
            event.setAmount(0.0f);
            if (entity instanceof EntityPlayer) {
                entity.sendMessage(new TextComponentString("My legs are OK"));
            }
        }
    }

    @Override
    protected boolean shouldShowParticles() {
        return false;
    }
}
