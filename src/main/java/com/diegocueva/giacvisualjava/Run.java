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

/**
 *
 * @author dcueva
 */
public class Run {
    public static void main(String[] arg){
        Log.init("GVJ");
        loadWindowsLibrary();
        MainWindow mainWindow = new MainWindow();
        mainWindow.display();
    } 
    public static void loadWindowsLibrary() {
        try{
            Log.info("java.library.path:\n" + System.getProperty("java.library.path"));
            System.loadLibrary("javagiac");
            Log.info("Loaded javagiac");            
        }catch(Exception e){
            Log.error("loadWindowsLibrary", e);
        }
    }    
}
