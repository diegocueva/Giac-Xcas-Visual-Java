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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author pc
 */
public class ViewerManager {
    
    private final Map<Node, ViewNode> viewersMap = new HashMap<>();
    
    public void add(Node node){
        ViewNode viewNode = viewersMap.get(node);
        if(viewNode==null){
            viewNode = new ViewNode();
            viewNode.display(node);
            viewersMap.put(node, viewNode);
        }else{
            viewNode.setVisible(true);
        }
    }
    
    public void clear(){
        viewersMap.clear();
    }
}
