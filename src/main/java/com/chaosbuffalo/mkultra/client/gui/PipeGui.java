package com.chaosbuffalo.mkultra.client.gui;

import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;

/**
 * Created by Jacob on 8/5/2018.
 */
public class PipeGui extends GuiContainer {

    @SuppressWarnings("unused")
    private IItemHandler i;

    public PipeGui(IItemHandler i, EntityPlayer p ) {
        super( new PipeContainer(i, p));
        this.xSize = 176;
        this.ySize = 133;
        this.i = i;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color( 1.0F, 1.0F, 1.0F, 1.0F );
        this.mc.getTextureManager().bindTexture( new ResourceLocation(MKUltra.MODID,
                "textures/gui/pipe_container.png"));
        this.drawTexturedModalRect( this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize );
    }
}
