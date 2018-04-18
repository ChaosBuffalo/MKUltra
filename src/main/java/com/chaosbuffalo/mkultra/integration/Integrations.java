package com.chaosbuffalo.mkultra.integration;
import com.chaosbuffalo.mkultra.core.ArmorClass;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.targeting_api.Targeting;
import com.google.common.collect.Lists;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.init.Materials;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

import java.util.List;

public class Integrations {

    public static boolean isLootableBodiesPresent() {
        return Loader.isModLoaded("lootablebodies");
    }

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



    private static void setupMinecolonies() {
        if (!Loader.isModLoaded("minecolonies"))
            return;

        final String CITIZEN_CLASS_NAME = "com.minecolonies.coremod.entity.EntityCitizen";
        Targeting.registerFriendlyEntity(CITIZEN_CLASS_NAME);

        try {
            Class citizenClass = Class.forName(CITIZEN_CLASS_NAME);
            Targeting.registerClassAssociation(citizenClass, EntityPlayer.class, Targeting.TargetType.FRIENDLY);
        }
        catch (Exception e) {
            Log.error("Failed to register EntityCitizen->EntityPlayer Friendly association!");
            e.printStackTrace();
        }
    }

//    private static void setupLycanites() {
//        Targeting.registerFriendlyEntity("com.lycanitesmobs.elementalmobs.entity.EntityNymph");
//    }

    private static void setupBasemetals() {
        ArmorClass.ROBES
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.STARSTEEL)));

        ArmorClass.LIGHT
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.AQUARIUM)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.TIN)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.MITHRIL)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.PEWTER)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.BISMUTH)));

        ArmorClass.MEDIUM
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.COPPER)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.BRASS)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.SILVER)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.ELECTRUM)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.NICKEL)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.QUARTZ)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.CUPRONICKEL)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.ANTIMONY)));

        ArmorClass.HEAVY
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.LEAD)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.STEEL)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.INVAR)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.COLDIRON)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.ADAMANTINE)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.OBSIDIAN)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.ZINC)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.EMERALD)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.PLATINUM)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.BRONZE)));
    }

    public static void setup() {
        setupBasemetals();
        setupLootableBodies();
        setupMinecolonies();
    }
}
