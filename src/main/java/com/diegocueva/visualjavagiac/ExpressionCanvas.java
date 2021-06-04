/**
 * ******************************************
 * Java swing single interface for giac
 *
 * @author Diego Cueva - diegocueva.com
 *
 * Use java 1.8 or upper
 *
 * Code released under GLP 3 http://www.gnu.org/copyleft/gpl.html
 *
 */
package com.diegocueva.visualjavagiac;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

public class ExpressionCanvas extends JComponent {

    private String latexStr;
    private BufferedImage imageBuffer;

     public ExpressionCanvas(String text, int horizontalAlignment) {
        this.imageBuffer = StackElement.buildDefaultImage();
    }
     
    public String getLatexStr() {
        return latexStr;
    }

    public void setLatexStr(String latexStr) {
        this.latexStr = latexStr;
    }

    public BufferedImage getImageBuffer() {
        return imageBuffer;
    }

    public void setImageBuffer(BufferedImage imageBuffer) {
        this.imageBuffer = imageBuffer;
        this.setSize(this.imageBuffer.getWidth(), this.imageBuffer.getHeight());
    }

    @Override
    public void paint(Graphics gg) {
        super.paint(gg);
        if (imageBuffer != null) {
            this.setSize(imageBuffer.getWidth(), imageBuffer.getHeight());
            gg.drawImage(imageBuffer, 0, 0, null);
        }
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public Dimension getPreferredSize() {
        Log.debug("xxxx ");
        return new Dimension(this.imageBuffer.getWidth(), this.imageBuffer.getHeight());
    }



}
