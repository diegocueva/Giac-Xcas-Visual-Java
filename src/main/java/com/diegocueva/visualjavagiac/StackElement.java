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

import java.awt.Image;
import javagiac.gen;

public class StackElement {

    public gen    result;
    public String input;
    public String visual;
    public String latex;
    public Image  image;

    public StackElement() {
        input  = " ";
        visual = " ";
    }

    public void set(StackElement src) {
        input = src.input;
        visual = src.visual;
        result = src.result;
    }    
}
