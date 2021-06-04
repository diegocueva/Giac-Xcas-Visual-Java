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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javagiac.gen;

public class StackElement {

    public gen    result;
    public String input;
    public String visual;
    public String latex;
    public BufferedImage  image;

    public StackElement() {
        input  = " ";
        visual = " ";
    }

    public void set(StackElement src) {
        result = src.result;
        input  = src.input;
        visual = src.visual;
        latex  = src.latex;
        image  = src.image;
    }
    
    public void clear(){
        result = null;
        input  = "";
        visual = "";
        latex  = "";
        image  = buildDefaultImage();
    }

    public static BufferedImage buildDefaultImage(){
        int width = 300;
        int height= 60;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        return bufferedImage;
    }    
}
