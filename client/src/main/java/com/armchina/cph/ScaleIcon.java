package com.armchina.cph;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;

/**
 * @author denzha01
 *
 */
public class ScaleIcon implements Icon {
    private Icon icon = null;


    public ScaleIcon(Icon icon) {
        this.icon  = icon;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        // TODO Auto-generated method stub
        float iconHeight = c.getHeight();
        float iconWidth  = c.getWidth();
        float picHeight  = icon.getIconHeight();
        float picWidth   = icon.getIconWidth();
        Graphics2D graph = (Graphics2D)g;
        graph.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graph.scale(iconWidth/picWidth, iconHeight/picHeight);
        icon.paintIcon(c, graph, 0, 0);
    }

    @Override
    public int getIconWidth() {
        // TODO Auto-generated method stub
        return icon.getIconWidth();
    }

    @Override
    public int getIconHeight() {
        // TODO Auto-generated method stub
        return icon.getIconHeight();
    }

}

