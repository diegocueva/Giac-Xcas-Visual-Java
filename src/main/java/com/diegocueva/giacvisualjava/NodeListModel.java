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

import com.diegocueva.visualjavagiac.Log;
import java.util.Arrays;
import javax.swing.DefaultListModel;

/**
 *
 * @author dcueva
 */
public class NodeListModel extends DefaultListModel<Node>{

    public static final int LIST_SIZE = 10;
    private final Node[] nodes = new Node[LIST_SIZE];
    
    private int ind=0;
    
    @Override
    public int getSize() {
        Log.debug("getSize "+ind);
        return ind;
    }

    @Override
    public Node getElementAt(int index) {
        Log.debug("getElementAt "+index);
        return nodes[index];
    }
    
    public void addNode(Node node){
        if(ind >= LIST_SIZE){
            for (int i = 0; i < LIST_SIZE - 1; i++) {
                nodes[i] = nodes[i+1];
            }            
            ind = LIST_SIZE-1;
        }        
        nodes[ind++] = node;
    }
    
    public int getMaxId(){
        return Arrays.asList(nodes).stream().filter(n->n!=null).mapToInt(Node::getId).max().orElse(1);
    }
    
}
