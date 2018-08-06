package com.chaosbuffalo.mkultra.utils;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.effects.spells.*;
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

        public SmokeableEntry(ItemStack item, Potion potion, int duration) {
            this.item = item;
            this.effect = potion;
            this.duration = duration;
        }
    }

    static {
        registerSmokeable(Items.BLAZE_POWDER, ManaSmokeEffect.INSTANCE, 20 * GameConstants.TICKS_PER_SECOND);
        registerSmokeable(Items.BONE, HealthRegenSmokeEffect.INSTANCE, 8*GameConstants.TICKS_PER_SECOND);
        registerSmokeable(Items.FEATHER, FeatherFallPotion.INSTANCE, 10*GameConstants.TICKS_PER_SECOND);
        registerSmokeable(Items.SNOWBALL, CurePotion.INSTANCE, 1);
        registerSmokeable(Items.GLOWSTONE_DUST, SpeedSmokeEffect.INSTANCE, 60*GameConstants.TICKS_PER_SECOND);
        registerSmokeable(Items.WHEAT_SEEDS, ShieldingPotion.INSTANCE, 6*GameConstants.TICKS_PER_SECOND);
    }

    public static void registerSmokeable(ItemStack item, Potion potion, int duration){
        SMOKEABLES.add(new SmokeableEntry(item, potion, duration));
    }

    public static void registerSmokeable(Item item, Potion potion, int duration){
        registerSmokeable(new ItemStack(item), potion, duration);
    }

    public static boolean isSmokeable(ItemStack stack) {
        // TEMP
        if (!stack.isEmpty()){
            for (SmokeableEntry smokeable : SMOKEABLES){
                if (ItemHandlerHelper.canItemStacksStack(smokeable.item, stack)){
                    return true;
                }
            }
        }
        return false;
    }

   @Nullable
    public static PotionEffect getPotionEffectForSmokeable(ItemStack stack){
        for (SmokeableEntry smokeable : SMOKEABLES) {
            if (ItemHandlerHelper.canItemStacksStack(smokeable.item, stack)){
                return new PotionEffect(smokeable.effect, smokeable.duration, 1);
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
