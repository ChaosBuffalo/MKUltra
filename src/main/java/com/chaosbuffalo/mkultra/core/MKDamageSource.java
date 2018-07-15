package com.chaosbuffalo.mkultra.core;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import javax.annotation.Resource;

/**
 * Created by Jacob on 7/14/2018.
 */
public class MKDamageSource extends EntityDamageSourceIndirect {

    public static String ABILITY_DMG_TYPE = "mkUltraAbility";

    public final ResourceLocation ability_id;

    public MKDamageSource(ResourceLocation ability_id, String damageTypeIn,
                          Entity source, @Nullable Entity indirectEntityIn){
        super(damageTypeIn, source, indirectEntityIn);
        this.ability_id = ability_id;
    }

    public static DamageSource causeIndirectMagicDamage(ResourceLocation ability_id, Entity source,
                                                        @Nullable Entity indirectEntityIn)
    {
        return (new MKDamageSource(ability_id, ABILITY_DMG_TYPE, source,
                indirectEntityIn)).setDamageBypassesArmor().setMagicDamage();
    }

    public static DamageSource causeIndirectMeleeDamage(ResourceLocation ability_id, Entity source,
                                                        @Nullable Entity indirectEntityIn){
        return (new MKDamageSource(ability_id, ABILITY_DMG_TYPE, source,
                indirectEntityIn));
    }
}
