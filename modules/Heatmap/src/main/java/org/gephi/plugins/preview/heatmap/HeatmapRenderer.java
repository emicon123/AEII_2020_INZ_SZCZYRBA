package org.gephi.plugins.preview.heatmap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

import org.apache.xpath.operations.Bool;
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
    public static final String ENABLE_NODE_HEATMAP = "node.heatmap.enable";
    //public float volume = 0;

    //to get a prop, a later a value(color) as it didn't other way
    PreviewProperty colorProp = null;
    boolean colorsEnabled = false;
    Color color;
    PreviewModel pm;

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(HeatmapRenderer.class, "HeatmapRenderer.name");
    }

    @Override
    public void preProcess(PreviewModel pm) {

        this.pm = pm;
        PreviewProperties properties = pm.getProperties();
        PreviewProperty[] props = properties.getProperties();

        for(PreviewProperty prop : props){
            if(prop.getName().equals("node.color.enable")){
                colorsEnabled = prop.getValue();
            }
            if(prop.getName().equals("node.color")){
                colorProp = prop;
            }
        }

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

        PreviewProperty[] props = properties.getProperties();

        if(!colorsEnabled) {
            color = item.getData(NodeItem.COLOR);
        }
        else {
            color = colorProp.getValue();
        }



        //Params
        Float x = item.getData(NodeItem.X);
        Float y = item.getData(NodeItem.Y);
        //Float size = item.getData(NodeItem.SIZE);

        Color startColor = new Color(color.getRed(), color.getGreen(), color.getBlue(),  255);
        Color endColor = new Color(startColor.getRed(), startColor.getGreen(), startColor.getBlue(), 0);
       //volume = size * 6;
        float volume = props[1].getValue();
        //Get Java2D canvas
        Graphics2D g2 = target.getGraphics();
//        RadialGradientPaint p = new RadialGradientPaint(new Point2D.Double(x, y), volume,
//                new float[]{
//                    0.0f, 1.0f},
//                new Color[]{
//                    startColor,
//                    endColor});
//        g2.setPaint(p);
//        g2.fillOval((int) (x - volume), (int) (y - volume), (int) (volume * 2), (int) (volume * 2));
        g2.setComposite(new AdditiveComposite(Color.BLACK));
    }

    public void renderPDF(Item item, PDFTarget target, PreviewProperties properties) {
        //TODO Not implemented
    }

    public void renderSVG(Item item, SVGTarget target, PreviewProperties properties) {
        //TODO Not implemented
    }

    @Override
    public PreviewProperty[] getProperties() {



        return new PreviewProperty[]{ //heatmapProp, volumeProp, enableColorProp, colorProp
                PreviewProperty.createProperty(this, ENABLE_NODE_HEATMAP, Boolean.class,
                "Show heatmap effect",
                "Heatmap effect",
                "Heatmap").setValue(false),

            PreviewProperty.createProperty(this, "node.volume", Float.class,
                "Volume",
                "Sets the volume of node for heatmap effect",
                "Heatmap").setValue(100.0f),

            PreviewProperty.createProperty(this, "node.color.enable", Boolean.class,
                "Enable nodes color",
                "Enables one color for all nodes when generating heatmap",
                "Heatmap",
                ENABLE_NODE_HEATMAP).setValue(false),

            PreviewProperty.createProperty(this, "node.color", Color.class,
                "Color",
                "Sets color of nodes",
                "Heatmap",
                "node.color.enable").setValue(Color.RED)
        };
    }

    @Override
    public boolean isRendererForitem(Item item, PreviewProperties properties) {
        return item instanceof NodeItem && properties.getBooleanValue(ENABLE_NODE_HEATMAP);
    }

    @Override
    public boolean needsItemBuilder(ItemBuilder itemBuilder, PreviewProperties properties) {
        return itemBuilder instanceof NodeBuilder && properties.getBooleanValue(ENABLE_NODE_HEATMAP);
    }
}
