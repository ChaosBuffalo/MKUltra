package com.chaosbuffalo.mkultra.client.model;

import com.chaosbuffalo.mkultra.entities.mobs.EntityMobBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelRanger extends ModelBiped {
    public ModelRanger() {
        this(0.0F, false);
    }

    public ModelRanger(float p_i1168_1_, boolean p_i1168_2_)
    {
        super(p_i1168_1_, 0.0F, 64, 32);
    }


    public void setLivingAnimations(EntityLivingBase entity, float float1, float float2, float float3) {
        this.rightArmPose = ArmPose.EMPTY;
        this.leftArmPose = ArmPose.EMPTY;
        ItemStack inMainhand = entity.getHeldItem(EnumHand.MAIN_HAND);
        if (inMainhand.getItem()instanceof ItemBow && ((EntityMobBase)entity).isSwingingArms()) {
            if (entity.getPrimaryHand() == EnumHandSide.RIGHT) {
                this.rightArmPose = ArmPose.BOW_AND_ARROW;
            } else {
                this.leftArmPose = ArmPose.BOW_AND_ARROW;
            }
        }

        super.setLivingAnimations(entity, float1, float2, float3);
    }

    public void setRotationAngles(float p_setRotationAngles_1_, float p_setRotationAngles_2_, float p_setRotationAngles_3_, float p_setRotationAngles_4_, float p_setRotationAngles_5_, float p_setRotationAngles_6_, Entity p_setRotationAngles_7_) {
        super.setRotationAngles(p_setRotationAngles_1_, p_setRotationAngles_2_, p_setRotationAngles_3_, p_setRotationAngles_4_, p_setRotationAngles_5_, p_setRotationAngles_6_, p_setRotationAngles_7_);
        ItemStack itemMainhand = ((EntityLivingBase)p_setRotationAngles_7_).getHeldItemMainhand();
        EntityMobBase mobBase = (EntityMobBase) p_setRotationAngles_7_;
        if (mobBase.isSwingingArms() && (itemMainhand.isEmpty() || !(itemMainhand.getItem() instanceof ItemBow))) {
            float lvt_10_1_ = MathHelper.sin(this.swingProgress * 3.1415927F);
            float lvt_11_1_ = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * 3.1415927F);
            this.bipedRightArm.rotateAngleZ = 0.0F;
            this.bipedLeftArm.rotateAngleZ = 0.0F;
            this.bipedRightArm.rotateAngleY = -(0.1F - lvt_10_1_ * 0.6F);
            this.bipedLeftArm.rotateAngleY = 0.1F - lvt_10_1_ * 0.6F;
            this.bipedRightArm.rotateAngleX = -1.5707964F;
            this.bipedLeftArm.rotateAngleX = -1.5707964F;
            this.bipedRightArm.rotateAngleX -= lvt_10_1_ * 1.2F - lvt_11_1_ * 0.4F;
            this.bipedLeftArm.rotateAngleX -= lvt_10_1_ * 1.2F - lvt_11_1_ * 0.4F;
            this.bipedRightArm.rotateAngleZ += MathHelper.cos(p_setRotationAngles_3_ * 0.09F) * 0.05F + 0.05F;
            this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(p_setRotationAngles_3_ * 0.09F) * 0.05F + 0.05F;
            this.bipedRightArm.rotateAngleX += MathHelper.sin(p_setRotationAngles_3_ * 0.067F) * 0.05F;
            this.bipedLeftArm.rotateAngleX -= MathHelper.sin(p_setRotationAngles_3_ * 0.067F) * 0.05F;
        }

    }

    public void postRenderArm(float p_postRenderArm_1_, EnumHandSide p_postRenderArm_2_) {
        float lvt_3_1_ = p_postRenderArm_2_ == EnumHandSide.RIGHT ? 1.0F : -1.0F;
        ModelRenderer lvt_4_1_ = this.getArmForSide(p_postRenderArm_2_);
        lvt_4_1_.rotationPointX += lvt_3_1_;
        lvt_4_1_.postRender(p_postRenderArm_1_);
        lvt_4_1_.rotationPointX -= lvt_3_1_;
    }
}
