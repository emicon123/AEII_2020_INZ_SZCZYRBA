package org.gephi.plugins.preview.heatmap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import org.gephi.preview.api.*;
import org.gephi.preview.plugin.builders.NodeBuilder;
import org.gephi.preview.plugin.items.NodeItem;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.preview.spi.Renderer;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 * Adds a heatmap effect on nodes by drawing a gradient sphere under nodes.
 */
@ServiceProvider(service = Renderer.class, position = 10)
public class HeatmapRenderer implements Renderer {

    //Custom properties
    public static final String ENABLE_NODE_GLOW = "node.glow.enable";

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(HeatmapRenderer.class, "GlowRenderer.name");
    }

    @Override
    public void preProcess(PreviewModel pm) {
    }

    @Override
    public CanvasSize getCanvasSize(Item item, PreviewProperties properties) {
        return new CanvasSize();
    }

    @Override
    public void render(Item item, RenderTarget target, PreviewProperties properties) {
        if (target instanceof G2DTarget) {
            renderJava2D(item, (G2DTarget) target, properties);
        } else if (target instanceof SVGTarget) {
            renderSVG(item, (SVGTarget) target, properties);
        } else if (target instanceof PDFTarget) {
            renderPDF(item, (PDFTarget) target, properties);
        }
    }

    public void renderJava2D(Item item, G2DTarget target, PreviewProperties properties) {

        //Params
        Float x = item.getData(NodeItem.X);
        Float y = item.getData(NodeItem.Y);
        Float size = item.getData(NodeItem.SIZE);
        Color color = item.getData(NodeItem.COLOR);
        Color startColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 255);
        Color endColor = new Color(startColor.getRed(), startColor.getGreen(), startColor.getBlue(), 0);
        float radius = size * 6;

        //Get Java2D canvas
        Graphics2D g2 = target.getGraphics();
        RadialGradientPaint p = new RadialGradientPaint(new Point2D.Double(x, y), radius,
                new float[]{
                    0.0f, 1.0f},
                new Color[]{
                    startColor,
                    endColor});
        g2.setPaint(p);
        g2.fillOval((int) (x - radius), (int) (y - radius), (int) (radius * 2), (int) (radius * 2));
    }

    public void renderPDF(Item item, PDFTarget target, PreviewProperties properties) {
        //TODO Not implemented
    }

    public void renderSVG(Item item, SVGTarget target, PreviewProperties properties) {
        //TODO Not implemented
    }

    @Override
    public PreviewProperty[] getProperties() {
        return new PreviewProperty[]{
            PreviewProperty.createProperty(this, ENABLE_NODE_GLOW, Boolean.class,
            "Show heatmap effect",
            "Heatmap effect",
            PreviewProperty.CATEGORY_NODES).setValue(false)
        };
    }

    @Override
    public boolean isRendererForitem(Item item, PreviewProperties properties) {
        return item instanceof NodeItem && properties.getBooleanValue(ENABLE_NODE_GLOW);
    }

    @Override
    public boolean needsItemBuilder(ItemBuilder itemBuilder, PreviewProperties properties) {
        return itemBuilder instanceof NodeBuilder && properties.getBooleanValue(ENABLE_NODE_GLOW);
    }
}
