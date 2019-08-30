package com.chaosbuffalo.mkultra.client.gui.lib;

public class MKStackLayoutHorizontal extends MKLayout {

    private int currentX;
    private boolean doSetHeight;


    public MKStackLayoutHorizontal(int x, int y, int height) {
        super(x, y, 0, height);
        currentX = x;
        doSetHeight = false;
    }

    public MKStackLayoutHorizontal doSetHeight(boolean value) {
        doSetHeight = value;
        return this;
    }

    public boolean shouldSetHeight() {
        return doSetHeight;
    }

    @Override
    public void setupLayoutStartState() {
        currentX = getX();
        currentX += getMarginLeft();
    }

    @Override
    public void doLayout(MKWidget widget, int index) {
        widget.setX(currentX);
        widget.setY(getY() + getMarginTop() + (int) ((getHeight() - getMarginTop() - getMarginBot()) * widget.getPosHintY()));
        if (shouldSetHeight()) {
            widget.setHeight((int) ((getHeight() - getMarginTop() - getMarginBot()) * widget.getSizeHintHeight()));
        }
        currentX += widget.getWidth() + getPaddingLeft() + getPaddingRight();
    }

    @Override
    public boolean addWidget(MKWidget widget) {
        super.addWidget(widget);
        setWidth(getWidth() + widget.getWidth() + getPaddingLeft() + getPaddingRight());
        return true;
    }

    @Override
    public void removeWidget(MKWidget widget) {
        super.removeWidget(widget);
        setWidth(getWidth() - widget.getWidth() - getPaddingLeft() - getPaddingRight());
    }

    @Override
    public MKLayout setMarginLeft(int value) {
        super.setMarginLeft(value);
        recomputeChildren();
        return this;
    }

    @Override
    public MKLayout setMarginRight(int value) {
        super.setMarginRight(value);
        recomputeChildren();
        return this;
    }

    @Override
    public MKLayout setPaddingLeft(int value) {
        super.setPaddingLeft(value);
        recomputeChildren();
        return this;
    }

    @Override
    public MKLayout setPaddingRight(int value) {
        super.setPaddingRight(value);
        recomputeChildren();
        return this;
    }

    @Override
    public void recomputeChildren() {
        int width = getMarginLeft();
        int i = 0;
        for (MKWidget child : children) {
            width += child.getWidth();
            if (i != 0) {
                width += getPaddingLeft();
            }
            if (i != children.size() - 1) {
                width += getPaddingRight();
            }
            i++;
        }
        width += getMarginRight();
        setWidth(width);
    }
}
