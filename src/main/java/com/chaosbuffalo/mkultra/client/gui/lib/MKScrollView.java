package com.chaosbuffalo.mkultra.client.gui.lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.Iterator;

public class MKScrollView extends MKWidget {

    private int offsetX;
    private int offsetY;
    private int lastMouseX;
    private int lastMouseY;
    private int screenWidth;
    private int screenHeight;
    private boolean isDragging;
    private boolean clipBounds;
    private int scaleFactor;
    private boolean doScrollLock;
    private int scrollMarginX;
    private int scrollMarginY;
    private boolean doScrollX;
    private boolean doScrollY;
    private boolean drawScrollBars;
    private int SCROLL_VELOCITY = 10;
    private static final int SCROLL_BAR_WIDTH = 1;

    public MKScrollView(int x, int y, int width, int height, int scaleFactor,
                        boolean clipBounds) {
        super(x, y, width, height);
        offsetX = 0;
        offsetY = 0;
        this.clipBounds = clipBounds;
        isDragging = false;
        this.scaleFactor = scaleFactor;
        this.doScrollLock = true;
        scrollMarginX = 0;
        scrollMarginY = 0;
        doScrollX = true;
        doScrollY = true;
        drawScrollBars = true;
    }

    public MKScrollView(int x, int y, int width, int height, boolean clipBounds) {
        this(x, y, width, height, new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor(), clipBounds);
    }

    @Override
    public void setScreen(MKScreen screen) {
        super.setScreen(screen);
        if (screen != null) {
            screenWidth = screen.width;
            screenHeight = screen.height;
        }

    }

    public MKScrollView setScrollLock(boolean state) {
        this.doScrollLock = state;
        return this;
    }

    public MKScrollView setDrawScrollBars(boolean value) {
        drawScrollBars = value;
        return this;
    }

    @Override
    public boolean onMouseScrollWheel(Minecraft minecraft, int mouseX, int mouseY, int direction) {
        int dY = direction * SCROLL_VELOCITY;
        if (isScrollLockOn()) {
            MKWidget child = getChild();
            if (child != null) {
                dY = lockScrollY(child, dY);
            }
        }
        offsetY += dY;
        return true;
    }

    public boolean shouldDrawScrollbars() {
        return drawScrollBars;
    }

    public boolean isClipBoundsEnabled() {
        return clipBounds;
    }

    public MKScrollView setOffsetX(int value) {
        offsetX = value;
        return this;
    }

    public MKScrollView setDoScrollX(boolean value) {
        doScrollX = value;
        return this;
    }

    public MKScrollView setDoScrollY(boolean value) {
        doScrollY = value;
        return this;
    }

    public boolean shouldScrollY() {
        return doScrollY;
    }

    public boolean shouldScrollX() {
        return doScrollX;
    }

    public MKScrollView setOffsetY(int value) {
        offsetY = value;
        return this;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public MKScrollView setClipBoundsEnabled(boolean value) {
        clipBounds = value;
        return this;
    }

    public boolean isScrollLockOn() {
        return doScrollLock;
    }

    public int getScrollMarginX() {
        return scrollMarginX;
    }

    public int getScrollMarginY() {
        return scrollMarginY;
    }

    public MKScrollView setScrollMarginX(int value) {
        scrollMarginX = value;
        return this;
    }

    public MKScrollView setScrollMarginY(int value) {
        scrollMarginY = value;
        return this;
    }

    public void centerContentX() {
        if (children.size() > 0) {
            MKWidget child = this.children.getFirst();
            setOffsetX(getWidth() / 2 - child.getWidth() / 2 + getX());
        }
    }

    public void centerContentY() {
        if (children.size() > 0) {
            MKWidget child = this.children.getFirst();
            setOffsetX(getHeight() / 2 - child.getHeight() / 2 + getY());
        }
    }

    public void setToTop() {
        setOffsetY(getY());
    }

    public void setToRight() {
        setOffsetX(getX());
    }

    @Override
    public void draw(Minecraft mc, int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glTranslatef(offsetX, offsetY, 0);

        if (isClipBoundsEnabled()) {
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            int y1 = screenHeight - y - height;
            GL11.glScissor(x * scaleFactor, y1 * scaleFactor,
                    width * scaleFactor, height * scaleFactor);
        }
    }

    public boolean isContentWider() {
        MKWidget child = getChild();
        if (child == null) {
            return false;
        }
        return child.getWidth() > getWidth();
    }

    public boolean isContentTaller() {
        MKWidget child = getChild();
        if (child == null) {
            return false;
        }
        return child.getHeight() > getHeight();
    }

    @Override
    public void postDraw(Minecraft mc, int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {
        if (isClipBoundsEnabled()) {
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        }
        GL11.glPopMatrix();
        if (shouldDrawScrollbars()) {
            MKWidget child = getChild();
            if (child == null) {
                return;
            }
            if (isContentTaller()) {
                float ratio = (float) getHeight() / (float) child.getHeight();
                int heightForScrollbar = (int) (ratio * getHeight());
                float posRatio = (float) (getY() - getOffsetY()) / (float) child.getHeight();
                int pos = (int) (posRatio * getHeight());
                int barX = getX() + getWidth() - SCROLL_BAR_WIDTH;
                int barY = getY() + pos;
                drawRect(barX, barY, barX + SCROLL_BAR_WIDTH, barY + heightForScrollbar,
                        0x7DFFFFFF);
            }
            if (isContentWider()) {
                float ratio = (float) getWidth() / (float) child.getWidth();
                int widthForScrollbar = (int) (ratio * getWidth());
                float posRatio = (float) (getX() - getOffsetX()) / (float) child.getWidth();
                int pos = (int) (posRatio * getWidth());
                int barX = getX() + pos;
                int barY = getY() + getHeight() - SCROLL_BAR_WIDTH;
                drawRect(barX, barY, barX + widthForScrollbar, barY + SCROLL_BAR_WIDTH,
                        0x7DFFFFFF);
            }
        }
    }

    @Override
    public Vec2i getParentCoords(Vec2i pos) {
        if (parent == null) {
            return pos.add(offsetX, offsetY);
        } else {
            return parent.getParentCoords(pos.add(offsetX, offsetY));
        }
    }

    @Override
    public void drawWidget(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        draw(mc, getX(), getY(), getWidth(), getHeight(), mouseX, mouseY, partialTicks);
        for (MKWidget child : children) {
            if (child.isVisible()) {
                child.drawWidget(mc, mouseX - offsetX, mouseY - offsetY, partialTicks);
            }
        }
        postDraw(mc, getX(), getY(), getWidth(), getHeight(), mouseX, mouseY, partialTicks);
    }

    @Override
    public MKWidget mousePressed(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        if (!this.isEnabled() || !this.isVisible() || !this.isInBounds(mouseX, mouseY)) {
            return null;
        }
        Iterator<MKWidget> it = children.descendingIterator();
        while (it.hasNext()) {
            MKWidget child = it.next();
            if (child.mousePressed(minecraft, mouseX - offsetX, mouseY - offsetY, mouseButton) != null) {
                return child;
            }
        }
        if (onMousePressed(minecraft, mouseX, mouseY, mouseButton)) {
            return this;
        }
        return null;
    }

    @Override
    public boolean mouseDragged(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        Iterator<MKWidget> it = children.descendingIterator();
        while (it.hasNext()) {
            MKWidget child = it.next();
            if (child.mouseDragged(minecraft, mouseX - offsetX, mouseY - offsetY, mouseButton)) {
                return true;
            }
        }
        if (onMouseDragged(minecraft, mouseX, mouseY, mouseButton)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int mouseButton) {
        Iterator<MKWidget> it = children.descendingIterator();
        while (it.hasNext()) {
            MKWidget child = it.next();
            if (child.mouseReleased(mouseX - offsetX, mouseY - offsetX, mouseButton)) {
                return true;
            }
        }
        if (onMouseRelease(mouseX, mouseY, mouseButton)) {
            return true;
        }
        return false;
    }


    @Nullable
    public MKWidget getChild() {
        if (children.size() > 0) {
            return this.children.getFirst();
        } else {
            return null;
        }
    }

    @Override
    public boolean onMouseDragged(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        if (isDragging) {
            int dX = mouseX - lastMouseX;
            int dY = mouseY - lastMouseY;
            MKWidget child = getChild();
            if (isScrollLockOn() && child != null) {
                dX = lockScrollX(child, dX);
                dY = lockScrollY(child, dY);
            }
            if (doScrollX) {
                offsetX += dX;
            }
            if (doScrollY) {
                offsetY += dY;
            }
            lastMouseX = mouseX;
            lastMouseY = mouseY;
            return true;
        }
        return false;
    }

    public int lockScrollX(MKWidget child, int dX) {
        int scrollX = getX();
        int childWidth = child.getWidth();
        int scrollWidth = getWidth();
//        Log.info("ScrollX : %d, childWidth : %d, scrollWidth : %d, cameraX: %d, dX : %d", scrollX, childWidth, scrollWidth, offsetX, dX);
        if (childWidth < scrollWidth) {
            if (offsetX + dX < scrollX) {
                dX = scrollX - offsetX;
            } else if (offsetX + dX + childWidth > scrollX + scrollWidth) {
                dX = scrollX + scrollWidth - offsetX - childWidth;
            }
        } else {
            if (offsetX + dX > scrollX + getScrollMarginX()) {
                dX = scrollX - offsetX + getScrollMarginX();
            } else if (offsetX + dX + childWidth <= scrollX + scrollWidth - getScrollMarginX()) {
                dX = scrollX + scrollWidth - getScrollMarginX() - offsetX - childWidth;
            }
        }
        return dX;
    }

    public int lockScrollY(MKWidget child, int dY) {
        int scrollY = getY();
        int childHeight = child.getHeight();
        int scrollHeight = getHeight();
        if (childHeight < scrollHeight) {
            return getY() - offsetY;
        } else {
            if (offsetY + dY > scrollY + getScrollMarginY()) {
                dY = scrollY - offsetY + getScrollMarginY();
            } else if (offsetY + dY + childHeight <= scrollY + scrollHeight - getScrollMarginY()) {
                dY = scrollY + scrollHeight - getScrollMarginY() - offsetY - childHeight;
            }
        }
        return dY;
    }

    @Override
    public boolean onMousePressed(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        isDragging = true;
        lastMouseX = mouseX;
        lastMouseY = mouseY;
        return true;
    }

    @Override
    public boolean onMouseRelease(int mouseX, int mouseY, int mouseButton) {
        if (isDragging) {
            isDragging = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean addWidget(MKWidget widget) {
        if (this.children.size() > 0) {
            return false;
        }
        return super.addWidget(widget);
    }
}
