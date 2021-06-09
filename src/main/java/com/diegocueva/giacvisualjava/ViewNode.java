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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class ViewNode extends JFrame{
    
    private final JPanel panelMain;
    
    private JComponent    component;
    private BufferedImage bufferedImage;
        
    public ViewNode(){
        this.panelMain = new JPanel();
        this.panelMain.setBackground(Color.WHITE);
        super.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        super.setLayout(new BorderLayout());
    }
    
    public void display(Node node){
        this.bufferedImage = UtilLatex.latexToImage(node.getLatex(), 26);
        
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
        
        this.setSize(this.bufferedImage.getWidth()+40, this.bufferedImage.getHeight()+100);
        this.setPreferredSize(new Dimension(this.bufferedImage.getWidth()+40, this.bufferedImage.getHeight()+60));
        panelMain.add(component, BorderLayout.CENTER);
        JTextField input = new JTextField(node.getInput());
        input.setFont(MainWindow.FONT_INPUT);
        input.setEditable(false);
        
        JTextField output = new JTextField(node.getOutput());
        output.setFont(MainWindow.FONT_OUTPUT);
        output.setEditable(false);
        
        add(BorderLayout.PAGE_START, input);
        add(BorderLayout.CENTER, panelMain);
        add(BorderLayout.PAGE_END, output);
        display();
    }
    
    private void display(){
        Random random = new Random();
        super.setLocation(random.nextInt(300)+5, random.nextInt(300)+5);
        super.setVisible(true);
    }
    
    
}
