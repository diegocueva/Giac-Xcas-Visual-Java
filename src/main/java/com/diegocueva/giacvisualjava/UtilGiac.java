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
package com.diegocueva.giacvisualjava;

import javagiac.context;
import javagiac.gen;

/**
 *
 * @author dcueva
 */
public class UtilGiac {


    public static String resultToString(gen result, context giacContext) {
        String resultStr = result.print(giacContext);
        Log.debug("# tpy="+result.getType()+" sbtpy="+result.getSubtype()+" tpy_u="+result.getType_unused()+" val="+result.getVal()+ " resv="+result.getReserved()+ " | "+resultStr);
        return resultStr;
    }    
}
