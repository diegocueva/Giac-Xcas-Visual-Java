
package com.diegocueva.giacvisualjava;

import com.diegocueva.visualjavagiac.Log;
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
