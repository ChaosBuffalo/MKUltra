package com.chaosbuffalo.mkultra.item;

import com.chaosbuffalo.mkultra.log.Log;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
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

        EntityArrow createAmmoEntity(World world, ItemStack ammo, EntityLivingBase shooter);
    }

    static class VanillaAdapter implements IRangedWeapon {

        @Override
        public ItemStack findAmmo(EntityPlayer player) {
            return ItemHelper.find(player, i -> i.getItem() instanceof ItemArrow);
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
