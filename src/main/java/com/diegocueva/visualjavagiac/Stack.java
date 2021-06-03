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

import javagiac.context;
import javagiac.gen;

/**
 *
 * @author dcueva
 */
public class Stack {

    /**
     * STACK
     */
    private final int stackSize;

    /**
     * Giac context
     */
    private final context C;

    /**
     * Stack Elements
     */
    private final StackElement[] elements;

    public Stack(int stackSize, context C) {
        this.stackSize = stackSize;
        this.C = C;
        elements = new StackElement[stackSize];
        for (int i = 0; i < stackSize; i++) {
            elements[i] = new StackElement();
        }
    }

    public StackElement put(String input, gen result, gen latex) {
        for (int i = 0; i < stackSize - 1; i++) {
            elements[i].set(elements[i + 1]);
        }

        elements[stackSize - 1].visual = resultToString(result, C);
        elements[stackSize - 1].input  = input;
        elements[stackSize - 1].result = result;
        String latexStr = resultToString(latex, C).replaceAll("^\"|\"$", "");        
        elements[stackSize - 1].latex  = latexStr;
        elements[stackSize - 1].image  = UtilLatex.latexToImage(latexStr);

        return elements[stackSize - 1];
    }

    public int getStackSize() {
        return stackSize;
    }

    public void clear() {
        for (StackElement element : elements) {
            element.input = " ";
            element.visual = " ";
        }
    }

    public String getInput(int index) {
        return elements[index].input;
    }

    public String getVisual(int index) {
        return elements[index].visual;
    }

    public gen getResult(int index) {
        return elements[index].result;
    }

    public String resultToString(gen result, context C) {
        String resultStr = result.print(C);
        System.out.println("# tpy="+result.getType()+" sbtpy="+result.getSubtype()+" tpy_u="+result.getType_unused()+" val="+result.getVal()+ " resv="+result.getReserved()+ " | "+resultStr);
        return resultStr;
    }
}
