package com.chaosbuffalo.mkultra.init;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.BaseMobAbility;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MKUMobData;
import com.chaosbuffalo.mkultra.core.MKURegistry;
import com.chaosbuffalo.mkultra.core.mob_abilities.TestHealDot;
import com.chaosbuffalo.mkultra.mob_ai.EntityAIBuffSelf;
import com.chaosbuffalo.mkultra.mob_ai.EntityAINearestAttackableTargetMK;
import com.chaosbuffalo.mkultra.spawner.*;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.function.BiFunction;


@Mod.EventBusSubscriber
public class ModSpawn {

    public static final int MAX_LEVEL = 10;

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerItemOptions(RegistryEvent.Register<ItemOption> event) {
        ItemOption mh_test = new ItemOption(
                new ResourceLocation(MKUltra.MODID, "mh_test"),
                ItemAssigners.MAINHAND,
                new ItemChoice(new ItemStack(Items.IRON_SWORD, 1), 5, 0));
        event.getRegistry().register(mh_test);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerAttributeRanges(RegistryEvent.Register<AttributeRange> event) {
        AttributeRange health_test = new AttributeRange(
                new ResourceLocation(MKUltra.MODID, "health_test"),
                BaseSpawnAttributes.MAX_HEALTH, 20.0, 50.0);
        event.getRegistry().register(health_test);
        AttributeRange set_follow = new AttributeRange(
                new ResourceLocation(MKUltra.MODID, "follow_range"),
                BaseSpawnAttributes.FOLLOW_RANGE, 20.0, 20.0);
        event.getRegistry().register(set_follow);
        AttributeRange set_aggro = new AttributeRange(
                new ResourceLocation(MKUltra.MODID, "aggro_range"),
                MKSpawnAttributes.SET_AGGRO_RADIUS, 8.0, 8.0);
        event.getRegistry().register(set_aggro);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerMobAbilities(RegistryEvent.Register<BaseMobAbility> event) {
        BaseMobAbility test_buff = new TestHealDot();
        event.getRegistry().register(test_buff);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerMobDefinitions(RegistryEvent.Register<MobDefinition> event) {

        MobDefinition test_mob =  new MobDefinition(
                new ResourceLocation(MKUltra.MODID, "test_skeleton"),
                EntitySkeleton.class, 10)
                .withAttributeRanges(
                        MKURegistry.getAttributeRange(
                                new ResourceLocation(MKUltra.MODID, "health_test")),
                        MKURegistry.getAttributeRange(
                                new ResourceLocation(MKUltra.MODID, "aggro_range"))
                        )
                .withItemOptions(
                        MKURegistry.getItemOption(
                                new ResourceLocation(MKUltra.MODID, "mh_test")))
                .withAbilities(MKURegistry.getMobAbility(
                        new ResourceLocation(MKUltra.MODID, "mob_ability.test_heal_dot")))
                .withAIModifiers(
                        MKURegistry.REGISTRY_MOB_AI_MODS.getValue(
                                new ResourceLocation(MKUltra.MODID, "remove_wander")),
                        MKURegistry.REGISTRY_MOB_AI_MODS.getValue(
                                new ResourceLocation(MKUltra.MODID, "remove_watch_closest")),
                        MKURegistry.REGISTRY_MOB_AI_MODS.getValue(
                                new ResourceLocation(MKUltra.MODID, "long_range_watch_closest")),
                        MKURegistry.REGISTRY_MOB_AI_MODS.getValue(
                                new ResourceLocation(MKUltra.MODID, "add_self_buff")
                        ))
                .withMobName("Test Skeleton");
        event.getRegistry().register(test_mob);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerAIModifiers(RegistryEvent.Register<AIModifier> event) {
        AIModifier remove_wander = new AIModifier(
                new ResourceLocation(MKUltra.MODID, "remove_wander"),
                AIModifiers.REMOVE_AI,
                new BehaviorChoice(EntityAIWanderAvoidWater.class, 0, BehaviorChoice.TaskType.TASK),
                new BehaviorChoice(EntityAINearestAttackableTarget.class, 0, BehaviorChoice.TaskType.TARGET_TASK));
        event.getRegistry().register(remove_wander);
        AIModifier remove_all_tasks = new AIModifier(
                new ResourceLocation(MKUltra.MODID, "remove_all_tasks"),
                AIModifiers.REMOVE_ALL_TASKS);
        event.getRegistry().register(remove_all_tasks);
        AIModifier remove_all_target_tasks = new AIModifier(
                new ResourceLocation(MKUltra.MODID, "remove_all_target_tasks"),
                AIModifiers.REMOVE_ALL_TARGET_TASKS);
        event.getRegistry().register(remove_all_target_tasks);
        BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> getWatchClosestLongRange =
                (entity, choice) -> new EntityAIWatchClosest(entity, EntityPlayer.class, 16.0F);
        AIModifier add_watch_closest = new AIModifier(
                new ResourceLocation(MKUltra.MODID, "long_range_watch_closest"),
                AIModifiers.ADD_TASKS,
                new BehaviorChoice(getWatchClosestLongRange, 0, 6, BehaviorChoice.TaskType.TASK)
        );
        event.getRegistry().register(add_watch_closest);
        BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> addSelfBuff = (entity, choice) -> {
            IMobData mobData = MKUMobData.get(entity);
            return new EntityAIBuffSelf(entity, mobData, .75f);
        };
        BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> addAggroTarget = (entity, choice) -> {
            return new EntityAINearestAttackableTargetMK((EntityCreature) entity, EntityPlayer.class, true);
        };
        AIModifier add_self_buff = new AIModifier(
                new ResourceLocation(MKUltra.MODID, "add_self_buff"),
                AIModifiers.ADD_TASKS,
                new BehaviorChoice(addSelfBuff, 0, 3, BehaviorChoice.TaskType.TASK),
                new BehaviorChoice(addAggroTarget, 0, 2, BehaviorChoice.TaskType.TARGET_TASK)
        );
        event.getRegistry().register(add_self_buff);
        AIModifier remove_watch_closest = new AIModifier(
                new ResourceLocation(MKUltra.MODID, "remove_watch_closest"),
                AIModifiers.REMOVE_AI,
                new BehaviorChoice(EntityAIWatchClosest.class, 0, BehaviorChoice.TaskType.TASK));
        event.getRegistry().register(remove_watch_closest);
    }

}
