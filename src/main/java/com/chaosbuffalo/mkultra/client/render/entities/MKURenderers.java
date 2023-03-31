package com.chaosbuffalo.mkultra.client.render.entities;

import com.chaosbuffalo.mknpc.client.render.models.MKBipedModel;
import com.chaosbuffalo.mknpc.client.render.models.MKGolemModel;
import com.chaosbuffalo.mknpc.client.render.models.MKPiglinModel;
import com.chaosbuffalo.mknpc.client.render.models.MKSkeletalModel;
import com.chaosbuffalo.mknpc.client.render.models.styling.ModelArgs;
import com.chaosbuffalo.mknpc.client.render.models.styling.ModelStyle;
import com.chaosbuffalo.mknpc.client.render.models.styling.ModelStyles;
import com.chaosbuffalo.mknpc.client.render.renderers.SkeletalGroupRenderer;
import com.chaosbuffalo.mknpc.client.render.renderers.ZombifiedPiglinGroupRenderer;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.client.render.entities.golems.GolemGroupRenderer;
import com.chaosbuffalo.mkultra.client.render.entities.humans.HumanGroupRenderer;
import com.chaosbuffalo.mkultra.client.render.entities.orcs.OrcGroupRenderer;
import com.chaosbuffalo.mkultra.client.render.styling.MKUGolems;
import com.chaosbuffalo.mkultra.client.render.styling.MKUHumans;
import com.chaosbuffalo.mkultra.client.render.styling.MKUPiglins;
import com.chaosbuffalo.mkultra.client.render.styling.MKUSkeletons;
import com.chaosbuffalo.mkultra.init.MKUEntities;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

/**
 * Created by Jacob on 7/15/2016.
 */
@Mod.EventBusSubscriber(modid = MKUltra.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MKURenderers {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers evt) {
        evt.registerEntityRenderer(MKUEntities.CLEANSING_SEED_TYPE.get(),
                (context) -> new SpriteProjectileRenderer<>(context, 1.0f, true));
        evt.registerEntityRenderer(MKUEntities.SPIRIT_BOMB_TYPE.get(),
                (context) -> new SpriteProjectileRenderer<>(context, 1.0f, true));
        evt.registerEntityRenderer(MKUEntities.SHADOWBOLT_TYPE.get(),
                (context) -> new SpriteProjectileRenderer<>(context, 1.0f, true));
        evt.registerEntityRenderer(MKUEntities.DROWN_TYPE.get(),
                (context) -> new SpriteProjectileRenderer<>(context, 1.0f, true));
        evt.registerEntityRenderer(MKUEntities.FIREBALL_TYPE.get(),
                (context) -> new SpriteProjectileRenderer<>(context, 1.0f, true));
        evt.registerEntityRenderer(MKUEntities.ORC_TYPE.get(), (context) -> new OrcGroupRenderer(context, MKUEntities.ORC_TYPE.getId()));
        evt.registerEntityRenderer(MKUEntities.HUMAN_TYPE.get(), (context) -> new HumanGroupRenderer(context, MKUEntities.HUMAN_TYPE.getId()));
        evt.registerEntityRenderer(MKUEntities.HYBOREAN_SKELETON_TYPE.get(), (context) ->
                new SkeletalGroupRenderer(context, MKUSkeletons.SKELETON_STYLES, MKUEntities.HYBOREAN_SKELETON_TYPE.getId()));
        evt.registerEntityRenderer(MKUEntities.ZOMBIFIED_PIGLIN_TYPE.get(),
                (context) -> new ZombifiedPiglinGroupRenderer(context, MKUPiglins.ZOMBIE_PIGLIN_STYLES, MKUEntities.ZOMBIFIED_PIGLIN_TYPE.getId()));
        evt.registerEntityRenderer(MKUEntities.GOLEM_TYPE.get(), (context) -> new GolemGroupRenderer(context, MKUEntities.GOLEM_TYPE.getId()));
    }

    @SubscribeEvent
    public static void layerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event)
    {

        List<ModelStyle> orcStyles = List.of(ModelStyles.LONG_HAIR_STYLE, ModelStyles.ARMORED_LONG_HAIR_STYLE, ModelStyles.BASIC_STYLE);
        for (ModelStyle style : orcStyles) {
            style.registerModelLayers(event, MKBipedModel::createBodyLayer, MKUEntities.ORC_TYPE.getId(),
                    64, 32,
                    new ModelArgs(CubeDeformation.NONE, false, 0.0f,
                            LayerDefinitions.OUTER_ARMOR_DEFORMATION, LayerDefinitions.INNER_ARMOR_DEFORMATION));
        }

        List<ModelStyle> humanStyles = List.of(ModelStyles.BASIC_STYLE, MKUHumans.TWO_LAYER_ARMOR_NO_HAIR,
                MKUHumans.TWO_LAYER_ARMOR_SHORT_HAIR, MKUHumans.GHOST_LONG_HAIR_STYLE, MKUHumans.TWO_LAYER_CLOTHES_SHORT_HAIR,
                MKUHumans.ARMORED_GHOST_LONG_HAIR_STYLE, ModelStyles.SHORT_HAIR_STYLE);
        for (ModelStyle style : humanStyles) {
            style.registerModelLayers(event, MKBipedModel::createBodyLayer,
                    MKUEntities.HUMAN_TYPE.getId(), 64, 32,
                    new ModelArgs(CubeDeformation.NONE, false, 0.0f,
                            LayerDefinitions.OUTER_ARMOR_DEFORMATION, LayerDefinitions.INNER_ARMOR_DEFORMATION));
        }

        List<ModelStyle> skeletonStyles = List.of(ModelStyles.BASIC_STYLE, ModelStyles.CLOTHES_ONLY_STYLE);
        for (ModelStyle style : skeletonStyles) {
            style.registerModelLayers(event, MKSkeletalModel::createBodyLayer,
                    MKUEntities.HYBOREAN_SKELETON_TYPE.getId(), 64, 32,
                    new ModelArgs(CubeDeformation.NONE, true, 0.0f,
                            LayerDefinitions.OUTER_ARMOR_DEFORMATION, LayerDefinitions.INNER_ARMOR_DEFORMATION));
        }

        List<ModelStyle> zombiePiglinStyles = List.of(ModelStyles.BASIC_STYLE, ModelStyles.CLOTHES_ONLY_STYLE,
                ModelStyles.CLOTHES_ARMOR_STYLE, ModelStyles.CLOTHES_ARMOR_TRANSLUCENT_STYLE);
        for (ModelStyle style : zombiePiglinStyles) {
            style.registerModelLayers(event, MKPiglinModel::createMesh,
                    MKUEntities.ZOMBIFIED_PIGLIN_TYPE.getId(), 64, 64,
                    new ModelArgs(CubeDeformation.NONE, true, 0.0f,
                            new CubeDeformation(1.02F), LayerDefinitions.INNER_ARMOR_DEFORMATION));
        }

        List<ModelStyle> golemStyles = List.of(MKUGolems.GOLEM_STYLE);
        for (ModelStyle style : golemStyles) {
            style.registerModelLayers(event, MKGolemModel::createBodyLayer,
                    MKUEntities.GOLEM_TYPE.getId(), 128, 128,
                    new ModelArgs(CubeDeformation.NONE, false, 0.0f,
                            LayerDefinitions.OUTER_ARMOR_DEFORMATION, LayerDefinitions.INNER_ARMOR_DEFORMATION));
        }


    }
}
