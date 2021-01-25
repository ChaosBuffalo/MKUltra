//package com.chaosbuffalo.mkultra.client.model;
//
//import net.minecraft.client.model.ModelBase;
//import net.minecraft.client.model.ModelBox;
//import net.minecraft.client.model.ModelRenderer;
//import net.minecraft.entity.Entity;
//
//public class ModelMeteor extends ModelBase {
//    private final ModelRenderer bone;
//
//    public ModelMeteor() {
//        textureWidth = 32;
//        textureHeight = 32;
//
//        bone = new ModelRenderer(this);
//        bone.setRotationPoint(0.0F, -2.5F, 0.0F);
//        bone.cubeList.add(new ModelBox(bone, 0, 10, -2.0F,
//                -2.0F, -2.0F, 4, 4, 4,
//                0.0F, false));
//        bone.cubeList.add(new ModelBox(bone, 16, 0, -3.0F,
//                -1.0F, -1.0F, 6, 2, 2,
//                0.0F, false));
//        bone.cubeList.add(new ModelBox(bone, 16, 10, -1.0F,
//                -1.0F, -3.0F, 2, 2, 6,
//                0.0F, false));
//        bone.cubeList.add(new ModelBox(bone, 0, 0, -1.0F,
//                -3.0F, -1.0F, 2, 6, 2,
//                0.0F, false));
//    }
//
//    @Override
//    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
//        bone.render(f5);
//    }
//
//    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
//        modelRenderer.rotateAngleX = x;
//        modelRenderer.rotateAngleY = y;
//        modelRenderer.rotateAngleZ = z;
//    }
//}