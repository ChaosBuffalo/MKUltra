package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellTriggers;
import com.chaosbuffalo.mkultra.effects.passives.PassiveAbilityPotionBase;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.mkultra.utils.AbilityUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class GuardianAngelPotion extends PassiveAbilityPotionBase {
    public static final GuardianAngelPotion INSTANCE = new GuardianAngelPotion();

    private static final ResourceLocation TIMER_NAME = new ResourceLocation(MKUltra.MODID, "timer.guardian_angel_invulnerability");
    private static final int TIMER_COOLDOWN = 2 * 60 * GameConstants.TICKS_PER_SECOND;
    private static final int INVULN_DURATION = 10 * GameConstants.TICKS_PER_SECOND;

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private GuardianAngelPotion() {
        super();
        setPotionName("effect.guardian_angel");
        SpellTriggers.PLAYER_DEATH.register(this, this::onPlayerDeath);
    }

    @Override
    public void doEffect(Entity source, Entity indirectSource, EntityLivingBase target, int amplifier, SpellCast cast) {
        super.doEffect(source, indirectSource, target, amplifier, cast);
    }

    public void onPlayerDeath(LivingDeathEvent event, DamageSource source, EntityPlayer player) {
        IPlayerData pData = MKUPlayerData.get(player);
        if (pData != null) {
            if (!pData.hasActiveTimer(TIMER_NAME)) {
                player.setHealth(1.0f);
                event.setCanceled(true);
                AbilityUtils.playSoundAtServerEntity(player, ModSounds.spell_heal_9, SoundCategory.PLAYERS);
                player.addPotionEffect(GuardianAngelInvulnerabilityPotion.Create(player).setTarget(player).toPotionEffect(INVULN_DURATION, 1));
                pData.setTimer(TIMER_NAME, TIMER_COOLDOWN);
            }
        }
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/guardian_angel.png");
    }
}
