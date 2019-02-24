package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKConfig;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKDamageSource;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerFormulas;
import com.chaosbuffalo.mkultra.core.abilities.Heal;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class ClericHealPotion extends SpellPotionBase {

    public static final ClericHealPotion INSTANCE = new ClericHealPotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source, EntityLivingBase target, float base, float scaling) {
        return Create(source, base, scaling).setTarget(target);
    }

    public static SpellCast Create(Entity source, float base, float scaling) {
        return INSTANCE.newSpellCast(source)
                .setScalingParameters(base, scaling);
    }

    private ClericHealPotion() {
        // boolean isBadEffectIn, int liquidColorIn
        super(false, 4393481);
        setPotionName("effect.cleric_heal");
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.FRIENDLY;
    }

    @Override
    public boolean isValidTarget(Targeting.TargetType targetType, Entity caster, EntityLivingBase target, boolean excludeCaster) {
        return super.isValidTarget(targetType, caster, target, excludeCaster) ||
                (target.isEntityUndead() && MKConfig.gameplay.HEALS_DAMAGE_UNDEAD);
    }

    @Override
    public boolean canSelfCast() {
        return true;
    }

    @Override
    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {

        float value = cast.getScaledValue(amplifier);

        if (caster instanceof EntityPlayerMP) {
            IPlayerData data = MKUPlayerData.get((EntityPlayerMP)caster);
            if (data != null) {
                value = PlayerFormulas.applyHealBonus(data, value);
            }
        }

        if (target.isEntityUndead()) {
            if (MKConfig.gameplay.HEALS_DAMAGE_UNDEAD){
                target.attackEntityFrom(MKDamageSource.causeIndirectMagicDamage(
                        new Heal().getAbilityId(), applier, caster), MKConfig.gameplay.HEAL_DAMAGE_MULTIPLIER * value);
            }
        } else {
            target.heal(value);
        }
    }
}