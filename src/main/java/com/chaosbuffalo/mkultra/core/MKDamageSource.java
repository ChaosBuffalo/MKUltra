package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.effects.spells.AbilityMagicDamage;
import com.chaosbuffalo.mkultra.effects.spells.AbilityMeleeDamage;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * Created by Jacob on 7/14/2018.
 */
public class MKDamageSource extends EntityDamageSourceIndirect {

    private static String ABILITY_DMG_TYPE = "mkUltraAbility";

    private boolean ignoreTriggerOnAttackEntity;

    private final ResourceLocation abilityId;

    public ResourceLocation getAbilityId() {
        return abilityId;
    }

    public MKDamageSource(ResourceLocation abilityId, String damageTypeIn,
                          Entity source, @Nullable Entity indirectEntityIn) {
        super(ABILITY_DMG_TYPE, source, indirectEntityIn);
        this.abilityId = abilityId;
        this.ignoreTriggerOnAttackEntity = false;
    }

    public boolean isIndirectMagic() {
        return abilityId.equals(AbilityMagicDamage.INDIRECT_MAGIC_DMG_ABILITY_ID);
    }

    public boolean isMeleeAbility() {
        return abilityId.equals(AbilityMeleeDamage.INDIRECT_DMG_ABILITY_ID);
    }

    public static DamageSource causeIndirectMagicDamage(ResourceLocation abilityId, Entity source,
                                                        @Nullable Entity indirectEntityIn) {
        return new MKDamageSource(abilityId, ABILITY_DMG_TYPE, source, indirectEntityIn)
                .setDamageBypassesArmor()
                .setMagicDamage();
    }

    public static DamageSource causeIndirectMagicDamageIgnoreAttackTriggers(ResourceLocation abilityId, Entity source,
                                                                            @Nullable Entity indirectEntityIn) {
        return new MKDamageSource(abilityId, ABILITY_DMG_TYPE, source, indirectEntityIn)
                .setIgnoreAttackTriggers()
                .setDamageBypassesArmor()
                .setMagicDamage();
    }

    public DamageSource setIgnoreAttackTriggers(){
        this.ignoreTriggerOnAttackEntity = true;
        return this;
    }

    public boolean ignoreAttackEntityTrigger(){
        return ignoreTriggerOnAttackEntity;
    }

    public static DamageSource fromMeleeSkill(ResourceLocation abilityId, Entity source,
                                              @Nullable Entity indirectEntityIn) {
        return new MKDamageSource(abilityId, ABILITY_DMG_TYPE, source, indirectEntityIn);
    }
}
