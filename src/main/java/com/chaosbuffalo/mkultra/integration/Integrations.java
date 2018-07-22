package com.chaosbuffalo.mkultra.integration;
import com.chaosbuffalo.mkultra.event.ItemRestrictionHandler;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.utils.ItemUtils;
import com.chaosbuffalo.targeting_api.Targeting;
import com.google.common.collect.Lists;
import com.oblivioussp.spartanweaponry.item.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

import java.util.List;

public class Integrations {

    public static boolean isLootableBodiesPresent() {
        return Loader.isModLoaded("lootablebodies");
    }

    public static boolean isSpartanWeaponryPresent() { return Loader.isModLoaded("spartanweaponry"); }

    private static Class lootableBodyClass;
    private final static String BODY_ENTITY_NAME = "cyano.lootable.entities.EntityLootableBody";

    private static void setupLootableBodies() {
        if (!isLootableBodiesPresent())
            return;


        Targeting.registerFriendlyEntity(BODY_ENTITY_NAME);

        try {
            lootableBodyClass = Class.forName(BODY_ENTITY_NAME);
        }
        catch (ClassNotFoundException c) {

        }
    }

    private static void setupSpartanWeaponry() {
        if (!isSpartanWeaponryPresent()){
            return;
        }

        ItemRestrictionHandler.addShieldRestrictedItem(ItemLongbow.class, 0);
        ItemRestrictionHandler.addShieldRestrictedItem(ItemKatana.class, 0);
        ItemRestrictionHandler.addShieldRestrictedItem(ItemCrossbow.class, 0);
        ItemRestrictionHandler.addShieldRestrictedItem(ItemHalberd.class, 0);
        ItemRestrictionHandler.addShieldRestrictedItem(ItemWarhammer.class, 0);
        ItemRestrictionHandler.addShieldRestrictedItem(ItemGreatsword.class, 0);
        ItemRestrictionHandler.addShieldRestrictedItem(ItemPike.class, 0);

        ItemUtils.addCriticalStats(ItemKatana.class, 1, .1f, 3.0f);
        ItemUtils.addCriticalStats(ItemRapier.class, 1, .1f, 2.5f);
        ItemUtils.addCriticalStats(ItemLongsword.class, 1, .05f, 2.5f);
        ItemUtils.addCriticalStats(ItemSaber.class, 1, .05f, 2.5f);
        ItemUtils.addCriticalStats(ItemHammer.class, 1, .15f, 2.0f);
        ItemUtils.addCriticalStats(ItemWarhammer.class, 1, .15f, 2.0f);
        ItemUtils.addCriticalStats(ItemCaestus.class, 1, .2f, 1.5f);
        ItemUtils.addCriticalStats(ItemSpear.class, 1, .05f, 2.5f);
        ItemUtils.addCriticalStats(ItemHalberd.class, 1, .1f, 2.0f);
        ItemUtils.addCriticalStats(ItemPike.class, 1, .05f, 2.0f);
        ItemUtils.addCriticalStats(ItemLance.class, 1, .05f, 2.5f);
    }

    public static List<Entity> getLootableBodiesForPlayer(EntityPlayer player) {

        World world = player.world;

        if (lootableBodyClass != null) {

            String corpseName = player.getName();
            List<Entity> corpses = world.getEntities(lootableBodyClass, e -> e.getCustomNameTag() != null && e.getCustomNameTag().equals(corpseName));
            corpses.forEach(e -> Log.info("found lootable body %s %d", e.getUniqueID().toString(), e.ticksExisted));
            return corpses;
        }

        return Lists.newArrayList();
    }

    public static List<Entity> getLootableBodiesAroundPlayer(EntityPlayer player, float range) {

        World world = player.world;

        if (lootableBodyClass != null && world != null) {
            List<Entity> corpses = world.getEntitiesWithinAABB(lootableBodyClass, player.getEntityBoundingBox().grow(range));
            corpses.forEach(e -> Log.info("found lootable body %s %d", e.getUniqueID().toString(), e.ticksExisted));
            return corpses;
        }

        return Lists.newArrayList();
    }

    public static Entity getLootableBodyAroundPlayer(EntityPlayer player, float range) {

        World world = player.world;

        if (lootableBodyClass != null) {
            return world.findNearestEntityWithinAABB(lootableBodyClass, player.getEntityBoundingBox().grow(range), player);
        }

        return null;
    }

    public static void setup() {
        setupLootableBodies();
        setupSpartanWeaponry();
    }
}
