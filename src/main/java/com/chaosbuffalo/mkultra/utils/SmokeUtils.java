package com.chaosbuffalo.mkultra.utils;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.mkultra.effects.spells.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Jacob on 8/5/2018.
 */
public class SmokeUtils {
    private static final ArrayList<SmokeableEntry> SMOKEABLES = new ArrayList<>();

    private static class SmokeableEntry {

        public final ItemStack item;
        public final Potion effect;
        public final int duration;
        public final int level;

        public SmokeableEntry(ItemStack item, Potion potion, int duration, int level) {
            this.item = item;
            this.effect = potion;
            this.duration = duration;
            this.level = level;
        }
    }

    static {
        registerSmokeable(Items.BLAZE_POWDER, ManaSmokeEffect.INSTANCE, 30 * GameConstants.TICKS_PER_SECOND, 1);
        registerSmokeable(Items.BONE, HealthRegenSmokeEffect.INSTANCE, 10*GameConstants.TICKS_PER_SECOND, 1);
        registerSmokeable(Items.FEATHER, FeatherFallPotion.INSTANCE, 10*GameConstants.TICKS_PER_SECOND, 1);
        registerSmokeable(Items.SNOWBALL, CurePotion.INSTANCE, 1, 1);
        registerSmokeable(Items.GLOWSTONE_DUST, SpeedSmokeEffect.INSTANCE, 60*GameConstants.TICKS_PER_SECOND, 1);
        registerSmokeable(Items.WHEAT_SEEDS, ShieldingPotion.INSTANCE, 10*GameConstants.TICKS_PER_SECOND, 1);
    }

    public static void registerSmokeable(ItemStack item, Potion potion, int duration, int level){
        SMOKEABLES.add(new SmokeableEntry(item, potion, duration, level));
    }

    public static void registerSmokeable(Item item, Potion potion, int duration, int level){
        registerSmokeable(new ItemStack(item), potion, duration, level);
    }

    public static boolean isSmokeable(ItemStack stack) {
        // TEMP
        if (!stack.isEmpty()){
            SmokeableEntry entry = find_smokeable_entry(stack);
            if (entry != null){
                return true;
            }
        }
        return false;
    }

    public static boolean is_smokeable_match(SmokeableEntry smokeable, ItemStack stack){
        return ItemHandlerHelper.canItemStacksStack(smokeable.item, stack);
    }

    @Nullable
    public static SmokeableEntry find_smokeable_entry(ItemStack stack){
        for (SmokeableEntry smokeable : SMOKEABLES){
            if (is_smokeable_match(smokeable, stack)){
                return smokeable;
            }
        }
        return null;
    }

   @Nullable
    public static PotionEffect getPotionEffectForSmokeable(ItemStack stack, EntityLivingBase caster){
       SmokeableEntry smokeable = find_smokeable_entry(stack);
       if (smokeable != null){
           if (smokeable.effect instanceof SpellPotionBase){
               SpellPotionBase potionBase = (SpellPotionBase) smokeable.effect;
               SpellCast cast = potionBase.newSpellCast(caster);
               cast.setTarget(caster);
               return cast.toPotionEffect(smokeable.duration, smokeable.level);
           } else {
               return  new PotionEffect(smokeable.effect, smokeable.duration, smokeable.level);
           }
       }
       return null;
    }


    public static ItemStack findSmokeable(EntityPlayer player)
    {
        if (isSmokeable(player.getHeldItem(EnumHand.OFF_HAND))){
            return player.getHeldItem((EnumHand.OFF_HAND));
        } else if (isSmokeable(player.getHeldItem(EnumHand.MAIN_HAND))){
            return player.getHeldItem(EnumHand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
            {
                ItemStack itemstack = player.inventory.getStackInSlot(i);


                ItemStack smokeableStack = itemstack.copy();
                smokeableStack.setCount(1);
                if (isSmokeable(smokeableStack))
                {
                    return itemstack;
                }
            }

            return ItemStack.EMPTY;
        }
    }
}
