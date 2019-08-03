package com.chaosbuffalo.mkultra.client.render.entities;


import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.client.model.ModelHumanoidBase;
import com.chaosbuffalo.mkultra.entities.mobs.EntityOrbMother;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderOrbMother implements IRenderFactory<EntityOrbMother> {


    RenderOrbMother() {
    }

    @Override
    public RenderBiped<EntityOrbMother> createRenderFor(RenderManager manager) {
        return new Renderer(manager);
    }

    private static class Renderer extends RenderBiped<EntityOrbMother> {
        private static final ResourceLocation TEXTURE_LOC = new ResourceLocation(MKUltra.MODID, "textures/entity/orb_mother/orb_mother.png");

        public Renderer(RenderManager renderManager) {
            super(renderManager, new ModelHumanoidBase(), 0.5F);
            this.addLayer(new LayerHeldItem(this));
            this.addLayer(new LayerBipedArmor(this) {
                protected void initArmor() {
                    this.modelLeggings = new ModelHumanoidBase(0.5F, true);
                    this.modelArmor = new ModelHumanoidBase(1.0F, true);
                }
            });
        }

        @Override
        public void transformHeldFull3DItemLayer() {
            GlStateManager.translate(0.09375F, 0.1875F, 0.0F);
        }

        @Override
        protected ResourceLocation getEntityTexture(EntityOrbMother entity) {
            return TEXTURE_LOC;
        }
    }
}