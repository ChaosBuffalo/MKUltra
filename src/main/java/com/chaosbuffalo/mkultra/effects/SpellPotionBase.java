package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public abstract class SpellPotionBase extends Potion {

    public void register(String name) {
        setPotionName(name);
    }

    public SpellPotionBase finish() {
        // This method is called from the registry callback, so our mod is the active mod
        // The single parameter overload of setRegistryName uses the domain of the active mod automatically
        setRegistryName(getName());
        return this;
    }

    protected SpellPotionBase(boolean isBadEffectIn, int liquidColorIn) {
        super(isBadEffectIn, liquidColorIn);
        if (!isBadEffectIn) {
            setBeneficial();
        }
    }

    public abstract Targeting.TargetType getTargetType();

    public boolean canSelfCast() {
        return false;
    }

    protected boolean isServerSideOnly() {
        return true;
    }

    public boolean isValidTarget(Targeting.TargetType targetType, Entity caster, EntityLivingBase target, boolean excludeCaster) {
        return !(caster == null || target == null) && Targeting.isValidTarget(targetType, caster, target, excludeCaster);
    }

    @Override
    public boolean isReady(int duration, int amplitude) {
        // Controls whether performEffect is called
        //System.out.printf("SpellPotionBase - isReady %d %d\n", duration, amplitude);
        return duration >= 1;
    }

    public boolean canPersistAcrossSessions() {
        return true;
    }

    public SpellCast createReapplicationCast(EntityLivingBase target) {
        if (!canPersistAcrossSessions()) {
            PotionEffect current = target.getActivePotionEffect(this);
            if (current != null) {
                return newSpellCast(target).setTarget(target);
            }
        }
        return null;
    }

    @Override
    public boolean isInstant() {
        return true;
    }

    // Called when the potion is being applied by an
    // AreaEffect or thrown potion bottle
    @Override
    public void affectEntity(Entity applier, Entity caster, @Nonnull EntityLivingBase target, int amplifier, double health) {

        if (target.world.isRemote && isServerSideOnly())
            return;

        SpellCast cast = SpellManager.get(target, this);
        if (cast == null) {
            cast = createReapplicationCast(target);
//            Log.debug("affectEntity cast was null, recasting Spell: %s", getName());
        }

        // Second chance
        if (cast == null) {
//            Log.warn("affectEntity cast was null after recast! Spell: %s", getName());
            return;
        }

        if (!isValidTarget(getTargetType(), caster, target, !canSelfCast()))
            return;

        doEffect(applier, caster, target, amplifier, cast);
    }

    // Called for effects that are applied directly to an entity
    @Override
    public void performEffect(@Nonnull EntityLivingBase target, int amplifier) {

        if (target.world.isRemote && isServerSideOnly())
            return;

        SpellCast cast = SpellManager.get(target, this);
        if (cast == null) {
            cast = createReapplicationCast(target);
//            Log.debug("performEffect cast was null, recasting Spell: %s", getName());
        }

        // Second chance
        if (cast == null) {
//            Log.warn("performEffect cast was null after recast! Spell: %s", getName());
            return;
        }

        if (!isValidTarget(getTargetType(), cast.getCaster(), target, !canSelfCast()))
            return;

        doEffect(cast.getApplier(), cast.getCaster(), target, amplifier, cast);
    }

    @Override
    public void applyAttributesModifiersToEntity(EntityLivingBase target, @Nonnull AbstractAttributeMap attributes, int amplifier) {
        super.applyAttributesModifiersToEntity(target, attributes, amplifier);

        if (!target.world.isRemote || !isServerSideOnly()) {
            SpellCast cast = SpellManager.get(target, this);
            if (cast == null) {
                cast = createReapplicationCast(target);
//                Log.debug("applyAttributesModifiersToEntity cast was null! Spell: %s", getName());
            }

            if (cast != null) {
                onPotionAdd(cast, target, attributes, amplifier);
            }
        }
    }


    @Override
    public void removeAttributesModifiersFromEntity(EntityLivingBase target, @Nonnull AbstractAttributeMap attributes, int amplifier) {
        super.removeAttributesModifiersFromEntity(target, attributes, amplifier);

        if (!target.world.isRemote || !isServerSideOnly()) {
            SpellCast cast = SpellManager.get(target, this);
            if (cast == null) {
                cast = createReapplicationCast(target);
//                Log.debug("removeAttributesModifiersFromEntity cast was null! Spell: %s", getName());
            }

            if (cast != null) {
                onPotionRemove(cast, target, attributes, amplifier);
            }
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
        return SpellManager.create(this, caster);
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

    public ResourceLocation getIconTexture() {
        return null;
    }

    @Override
    public boolean shouldRenderInvText(PotionEffect p_shouldRenderInvText_1_) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderInventoryEffect(PotionEffect effect, Gui gui, int x, int y, float partialTicks) {
        if (gui != null && getIconTexture() != null) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(getIconTexture());
            Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0, 0, 16, 16, 16, 16);

            String s1 = I18n.format(this.getName());
            if (effect.getAmplifier() == 2) {
                s1 = s1 + " " + I18n.format("enchantment.level.2");
            } else if (effect.getAmplifier() == 3) {
                s1 = s1 + " " + I18n.format("enchantment.level.3");
            } else if (effect.getAmplifier() == 4) {
                s1 = s1 + " " + I18n.format("enchantment.level.4");
            }
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(s1, (float) (x + 10 + 18), (float) (y + 6), 16777215);
            String s = Potion.getPotionDurationString(effect, 1.0F);
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(s, (float) (x + 10 + 18), (float) (y + 6 + 10), 8355711);
        }
    }


    @SideOnly(Side.CLIENT)
    @Override
    public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
        if (getIconTexture() != null) {
            mc.getTextureManager().bindTexture(getIconTexture());
            Gui.drawModalRectWithCustomSizedTexture(x + 4, y + 4, 0, 0, 16, 16, 16, 16);
        }

    }
}
