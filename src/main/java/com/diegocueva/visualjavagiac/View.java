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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class View extends JFrame{
    
    private final JPanel panelMain;
    
    private JComponent    component;
    private BufferedImage bufferedImage;
        
    public View(){
        this.panelMain = new JPanel();
        super.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        super.setLayout(new BorderLayout());
    }
    
    public void display(BufferedImage bufferedImage){
        this.bufferedImage = bufferedImage;
        
        component = new JComponent() {
            @Override
            public void paint(Graphics g) {
                g.drawImage(bufferedImage, 1, 1, null);
            }

            @Override
            public void update(Graphics g) {
                paint(g);
            }    

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight());
            }            
        };
        
        this.setSize(this.bufferedImage.getWidth()+40, this.bufferedImage.getHeight()+60);
        this.setPreferredSize(new Dimension(this.bufferedImage.getWidth()+40, this.bufferedImage.getHeight()+60));
        panelMain.add(component, BorderLayout.CENTER);
        add(BorderLayout.CENTER, panelMain);
        display();
    }
    
    private void display(){
        super.setLocation(20, 20);
        super.setVisible(true);
    }
    
    
}
