package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.log.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class SpellPotionBase extends Potion {

    private final ResourceLocation iconTexture;

    public static void register(String name, Potion potion) {
        potion.setRegistryName(MKUltra.MODID, name);
        potion.setPotionName(name);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc)
    {
        if (mc.currentScreen != null && iconTexture != null)
        {
            mc.getTextureManager().bindTexture(iconTexture);
            Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0, 0, 18, 18, 16, 16);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha)
    {
        if (iconTexture != null){
            mc.getTextureManager().bindTexture(iconTexture);
            Gui.drawModalRectWithCustomSizedTexture(x + 3, y + 3, 0, 0, 18, 18, 16, 16);
        }

    }

    protected SpellPotionBase(boolean isBadEffectIn, int liquidColorIn) {
        super(isBadEffectIn, liquidColorIn);
        iconTexture = null;
        if (!isBadEffectIn) {
            setBeneficial();
        }
    }

    protected SpellPotionBase(boolean isBadEffectIn, int liquidColorIn, ResourceLocation iconTextureIn) {
        super(isBadEffectIn, liquidColorIn);
        this.iconTexture = iconTextureIn;
        if (!isBadEffectIn) {
            setBeneficial();
        }
    }

    public abstract Targeting.TargetType getTargetType();

    public boolean canSelfCast() {
        return false;
    }

    public boolean isValidTarget(Targeting.TargetType targetType, Entity caster, EntityLivingBase target, boolean excludeCaster) {
        return Targeting.isValidTarget(targetType, caster, target, excludeCaster);
    }

    @Override
    public boolean isReady(int duration, int amplitude) {
        // Controls whether performEffect is called
        //System.out.printf("SpellPotionBase - isReady %d %d\n", duration, amplitude);
        return duration >= 1;
    }

    @Override
    public boolean isInstant() {
        return true;
    }

    @Override
    public void affectEntity(Entity applier, Entity caster, EntityLivingBase target, int amplifier, double health) {

        SpellCast meta = SpellCast.get(target, this);
        if (meta == null) {
            Log.warn("affectEntity cast was null!");
            return;
        }

        if (!isValidTarget(getTargetType(), caster, target, !canSelfCast()))
            return;

        doEffect(applier, caster, target, amplifier, meta);
    }

    @Override
    public void performEffect(EntityLivingBase target, int amplifier) {

        SpellCast meta = SpellCast.get(target, this);
        if (meta == null) {
            Log.warn("performEffect cast was null!");
            return;
        }

        if (!isValidTarget(getTargetType(), meta.getCaster(), target, !canSelfCast()))
            return;

        doEffect(meta.getApplier(), meta.getCaster(), target, amplifier, meta);
    }

    @Override
    public void applyAttributesModifiersToEntity(EntityLivingBase target, AbstractAttributeMap attributes, int amplifier) {
        // Called on application
        super.applyAttributesModifiersToEntity(target, attributes, amplifier);

        SpellCast cast = SpellCast.get(target, this);
        if (cast != null) {
            onPotionAdd(cast, target, attributes, amplifier);
        }
    }

    @Override
    public void removeAttributesModifiersFromEntity(EntityLivingBase target, AbstractAttributeMap attributes, int amplifier) {
        // Called on removal
        super.removeAttributesModifiersFromEntity(target, attributes, amplifier);

        SpellCast cast = SpellCast.get(target, this);
        if (cast != null) {
            onPotionRemove(cast, target, attributes, amplifier);
        }
    }

    public void onPotionAdd(SpellCast cast, EntityLivingBase target, AbstractAttributeMap attributes, int amplifier) {
    }

    public void onPotionRemove(SpellCast cast, EntityLivingBase target, AbstractAttributeMap attributes, int amplifier) {
    }

    public abstract void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast);

    protected boolean shouldShowParticles() {
        return true;
    }

    protected boolean isAmbient() {
        return false;
    }

    public PotionEffect toPotionEffect(int amplifier) {
        return toPotionEffect(1, amplifier);
    }

    public PotionEffect toPotionEffect(int duration, int amplifier) {
        return new PotionEffect(this, duration, amplifier, isAmbient(), shouldShowParticles());
    }

    public SpellCast newSpellCast(Entity caster) {
        return SpellCast.create(this, caster);
    }

    @Override
    public boolean equals(Object other) {
        // Only exact class matches work
        return other != null && other.getClass() == getClass();

        // For subclasses, instead use
        //return other != null && other.getClass().isAssignableFrom(getClass());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + getLiquidColor();
        hash = 31 * hash + (isBadEffect() ? 1 : 0);
        return hash;
    }
}
