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
}
