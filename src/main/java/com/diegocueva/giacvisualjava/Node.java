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
import javagiac.context;
import javagiac.gen;
import javagiac.giac;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Node extends JPanel{
    private int id;
    
    private String input;
    private String output;
    
    private gen    result;
    private String latex;
    private BufferedImage  image;
    
    private Node(){}

    public Node(int id, String input, context giacContext) {
        this.id    = id;
        this.input = input;
        gen g = new gen (input, giacContext);
        gen f = giac._factor(g, giacContext);
        gen e = giac._eval  (f, giacContext);
        gen l = giac._latex (e, giacContext);
        
        this.result = e;
        this.output = UtilGiac.resultToString(e, giacContext);
        this.latex  = UtilGiac.resultToString(l, giacContext).replaceAll("^\"|\"$", "");
        this.image  = UtilLatex.latexToImage(this.latex, 18);
        
        JTextField inputLabel = new JTextField(this.input);
        inputLabel.setFont(MainWindow.FONT_INPUT);
        inputLabel.setBackground(Color.WHITE);
        inputLabel.setEditable(false);
        JLabel idLabel = new JLabel(String.valueOf(this.id)+": ");
        idLabel.setFont(MainWindow.FONT_INPUT);
        idLabel.setBackground(Color.GRAY);
        idLabel.setForeground(Color.RED);
        idLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        
        JComponent outputVisual = new JComponent() {
            @Override
            public void paint(Graphics gg) {
                super.paint(gg);
                if (image != null) {
                    this.setSize(image.getWidth(), image.getHeight());
                    gg.drawImage(image, 0, 0, null);
                }
            }

            @Override
            public void update(Graphics g) {
                paint(g);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(image.getWidth(), image.getHeight());
            }
        };
        super.setLayout(new BorderLayout());
        super.setBackground(Color.WHITE);
        super.add(inputLabel, BorderLayout.PAGE_START);
        super.add(idLabel, BorderLayout.LINE_START);
        super.add(outputVisual, BorderLayout.CENTER);
        super.add(new JLabel("  "), BorderLayout.SOUTH);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
        
    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public gen getResult() {
        return result;
    }

    public void setResult(gen result) {
        this.result = result;
    }

    public String getLatex() {
        return latex;
    }

    public void setLatex(String latex) {
        this.latex = latex;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Node other = (Node) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Node{" + "id=" + id + ", in=" + input + ", out=" + output + '}';
    }

}
