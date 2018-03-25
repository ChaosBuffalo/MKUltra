package com.chaosbuffalo.mkultra.event;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.item.interfaces.IExtendedReach;
import com.chaosbuffalo.mkultra.network.packets.client.RangeSwordAttackPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by Jacob on 3/16/2016.
 */
public class MouseHandler {

    private static Minecraft mc;

    public MouseHandler() {
        mc = Minecraft.getMinecraft();
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onMouseEvent(MouseEvent event) {
        if (event.getButton() == 0 && event.isButtonstate()) {
            EntityPlayer thePlayer = mc.player;
            if (thePlayer != null) {
                ItemStack itemstack = thePlayer.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
                if (itemstack.getItem() instanceof IExtendedReach) {

                    IExtendedReach weapon = (IExtendedReach) itemstack.getItem();

                    RayTraceResult mov = getMouseOverExtended(weapon.getReach());
                    if (mov != null &&
                            mov.entityHit != null &&
                            mov.entityHit.hurtResistantTime == 0 &&
                            mov.entityHit != thePlayer) {
                        MKUltra.packetHandler.sendToServer(
                                new RangeSwordAttackPacket(mov.entityHit.getEntityId()));
                    }

                }
            }
        }
    }

    // This is mostly copied from the EntityRenderer#getMouseOver() method
    public static RayTraceResult getMouseOverExtended(float dist) {
        //Minecraft mc = FMLClientHandler.instance().getClient();
        Entity theRenderViewEntity = mc.getRenderViewEntity();
        AxisAlignedBB theViewBoundingBox = new AxisAlignedBB(
                theRenderViewEntity.posX - 0.5D,
                theRenderViewEntity.posY - 0.0D,
                theRenderViewEntity.posZ - 0.5D,
                theRenderViewEntity.posX + 0.5D,
                theRenderViewEntity.posY + 1.5D,
                theRenderViewEntity.posZ + 0.5D
        );
        RayTraceResult returnMOP = null;
        if (mc.world != null) {
            double var2 = dist;
            returnMOP = theRenderViewEntity.rayTrace(var2, 0);
            double calcdist = var2;
            Vec3d pos = theRenderViewEntity.getPositionEyes(0);
            var2 = calcdist;
            if (returnMOP != null) {
                calcdist = returnMOP.hitVec.distanceTo(pos);
            }

            Vec3d lookvec = theRenderViewEntity.getLook(0);
            Vec3d var8 = pos.addVector(lookvec.x * var2,
                    lookvec.y * var2,
                    lookvec.z * var2);
            Entity pointedEntity = null;
            float var9 = 1.0F;
            @SuppressWarnings("unchecked")
            List<Entity> list = mc.world.getEntitiesWithinAABBExcludingEntity(
                    theRenderViewEntity,
                    theViewBoundingBox.grow(
                            lookvec.x * var2,
                            lookvec.y * var2,
                            lookvec.z * var2).expand(var9, var9, var9));
            double d = calcdist;

            for (Entity entity : list) {
                if (entity.canBeCollidedWith()) {
                    float bordersize = entity.getCollisionBorderSize();
                    AxisAlignedBB aabb = new AxisAlignedBB(
                            entity.posX - entity.width / 2,
                            entity.posY,
                            entity.posZ - entity.width / 2,
                            entity.posX + entity.width / 2,
                            entity.posY + entity.height,
                            entity.posZ + entity.width / 2);
                    aabb.expand(bordersize, bordersize, bordersize);
                    RayTraceResult mop0 = aabb.calculateIntercept(pos, var8);

                    if (aabb.contains(pos)) {
                        if (0.0D < d || d == 0.0D) {
                            pointedEntity = entity;
                            d = 0.0D;
                        }
                    } else if (mop0 != null) {
                        double d1 = pos.distanceTo(mop0.hitVec);

                        if (d1 < d || d == 0.0D) {
                            pointedEntity = entity;
                            d = d1;
                        }
                    }
                }
            }

            if (pointedEntity != null && (d < calcdist || returnMOP == null)) {
                returnMOP = new RayTraceResult(pointedEntity);
            }
        }
        return returnMOP;
    }
}
