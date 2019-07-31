package com.chaosbuffalo.mkultra.client.gui;

public class MKStackLayoutVertical extends MKLayout {

    private int currentY;
    private int verticalPadding;
    private int horizontalPadding;

    public MKStackLayoutVertical(int x, int y, int width, int verticalPadding, int horizontalPadding) {
        super(x, y, width, 0);
        currentY = y;
        this.verticalPadding = verticalPadding;
        this.horizontalPadding = horizontalPadding;
    }

    @Override
    public void setupLayoutStartState(){
        currentY = y;
    }

    @Override
    public void doLayout(MKWidget widget){
        int height = widget.calcHeight();
        widget.y = currentY;
        widget.x = x + horizontalPadding;
        currentY += height + verticalPadding;
    }

    @Override
    public void addWidget(MKWidget widget){
        super.addWidget(widget);
        this.height += widget.height;
    }

    @Override
    public void removeWidget(MKWidget widget){
        super.removeWidget(widget);
        this.height -= widget.height;
    }
}
