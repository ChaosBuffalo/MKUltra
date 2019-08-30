package com.chaosbuffalo.mkultra.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RangedWeaponry {

    private static List<IRangedWeapon> adapters = new ArrayList<>();

    static {
        registerWeapon(new VanillaAdapter());
    }

    public static void registerWeapon(IRangedWeapon weapon) {
        adapters.add(weapon);
    }

    public static IRangedWeapon findWeapon(ItemStack weapon) {
        return adapters.stream().filter(r -> r.isRangedWeapon(weapon)).findFirst().orElse(null);
    }

    public interface IRangedWeapon {
        boolean isRangedWeapon(ItemStack stack);

        ItemStack findAmmo(EntityPlayer player);

        void applyEffects(EntityArrow arrow, ItemStack bow, ItemStack ammo);

        EntityArrow createAmmoEntity(World world, ItemStack ammo, EntityLivingBase shooter);

        default void consumeAmmo(EntityPlayer player, ItemStack ammo, int amount) {
            ItemHelper.shrinkStack(player, ammo, amount);
        }
    }

    static class VanillaAdapter implements IRangedWeapon {

        @Override
        public ItemStack findAmmo(EntityPlayer player) {
            return ItemHelper.find(player, i -> i.getItem() instanceof ItemArrow);
        }

        @Override
        public void applyEffects(EntityArrow arrow, ItemStack bow, ItemStack ammo) {
            if (arrow instanceof EntityTippedArrow) {
                for (PotionEffect e : PotionUtils.getEffectsFromStack(ammo)) {
                    ((EntityTippedArrow) arrow).addEffect(e);
                }
            }
        }

        @Override
        public boolean isRangedWeapon(ItemStack stack) {
            return stack.getItem() instanceof ItemBow;
        }

        @Override
        public EntityArrow createAmmoEntity(World world, ItemStack ammo, EntityLivingBase shooter) {
            return ((ItemArrow) ammo.getItem()).createArrow(world, ammo, shooter);
        }
    }
}
