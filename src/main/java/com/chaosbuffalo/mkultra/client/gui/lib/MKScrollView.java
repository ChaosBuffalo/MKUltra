package com.chaosbuffalo.mkultra.client.gui.lib;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class MKScrollView extends MKWidget{

    private int offsetX;
    private int offsetY;
    private int lastMouseX;
    private int lastMouseY;
    private int screenWidth;
    private int screenHeight;
    private boolean isDragging;
    private boolean clipBounds;
    private int scaleFactor;

    public MKScrollView(int x, int y, int width, int height, int screenWidth, int screenHeight, int scaleFactor,
                        boolean clipBounds) {
        super(x, y, width, height);
        offsetX = 0;
        offsetY = 0;
        this.clipBounds = clipBounds;
        isDragging = false;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.scaleFactor = scaleFactor;
    }

    @Override
    public void draw(Minecraft mc, int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glTranslatef(offsetX, offsetY, 0);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        int y1 = screenHeight - y - height;
        GL11.glScissor(x * scaleFactor, y1 * scaleFactor, width * scaleFactor, height * scaleFactor);
    }

    @Override
    public void postDraw(Minecraft mc, int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks){
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();
    }

    @Override
    public MKWidget mousePressed(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        if (!this.isEnabled() || !this.isVisible() || !this.isInBounds(mouseX, mouseY)){
            return null;
        }
        for (MKWidget child : reverseChildren){
            if (child.mousePressed(minecraft, mouseX - offsetX, mouseY - offsetY, mouseButton) != null){
                return child;
            }
        }
        if (onMousePressed(minecraft, mouseX, mouseY, mouseButton)){
            return this;
        }
        return null;
    }

    @Override
    public boolean mouseDragged(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        for (MKWidget child : reverseChildren){
            if (child.mouseDragged(minecraft, mouseX - offsetX, mouseY - offsetY, mouseButton)){
                return true;
            }
        }
        if (onMouseDragged(minecraft, mouseX, mouseY, mouseButton)){
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int mouseButton) {
        for (MKWidget child : reverseChildren){
            if (child.mouseReleased(mouseX - offsetX, mouseY - offsetX, mouseButton)){
                return true;
            }
        }
        if (onMouseRelease(mouseX, mouseY, mouseButton)){
            return true;
        }
        return false;
    }

    @Override
    public boolean onMouseDragged(Minecraft minecraft, int mouseX, int mouseY, int mouseButton){
        if (isDragging){
            int dX = mouseX - lastMouseX;
            int dY = mouseY - lastMouseY;
            offsetX += dX;
            offsetY += dY;
            lastMouseX = mouseX;
            lastMouseY = mouseY;
            return true;
        }
        return false;
    }

    @Override
    public boolean onMousePressed(Minecraft minecraft, int mouseX, int mouseY, int mouseButton){
        isDragging = true;
        lastMouseX = mouseX;
        lastMouseY = mouseY;
        return true;
    }

    @Override
    public boolean onMouseRelease(int mouseX, int mouseY, int mouseButton){
        if (isDragging){
            isDragging = false;
            return true;
        }
        return false;
    }

    public boolean addWidget(MKWidget widget){
        if (this.children.size() > 0){
            return false;
        }
        widget.setParent(this);
        this.children.add(widget);
        this.reverseChildren = new ArrayList<>(Lists.reverse(children));
        return true;
    }

    public void removeWidget(MKWidget widget){
        if (widget.getParent() != null && widget.getParent().id.equals(this.id)){
            if (children.removeIf((x) -> x.id.equals(widget.id))){
                this.reverseChildren = new ArrayList<>(Lists.reverse(children));
            }
            widget.setParent(null);
        }
    }
}
