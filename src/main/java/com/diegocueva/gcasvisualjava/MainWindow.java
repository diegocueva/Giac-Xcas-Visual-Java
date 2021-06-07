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
package com.diegocueva.gcasvisualjava;

import com.diegocueva.visualjavagiac.Log;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javagiac.context;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MainWindow extends JFrame implements KeyListener, AdjustmentListener, WindowListener, ActionListener, ListSelectionListener, ListCellRenderer {
    
    private static final long serialVersionUID = 1L;
    
    private final context giacContext = new context();
    
    private final JPanel panelNorth         = new JPanel(new BorderLayout());
    private final JPanel panelUpperLineEnd  = new JPanel(new BorderLayout());
    private final JScrollPane scrllList = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);    
    private final JPanel panelBottom        = new JPanel(new BorderLayout());
    private final JPanel panelBottomLineEnd = new JPanel(new BorderLayout());
    
    private final JLabel  lblAlert    = new JLabel("Welcome to CAS world");
    private final JButton btnClearAll = new JButton("CL");
    private final JButton btnCopy     = new JButton("Copy");
    
    private final JList<Node> lstNodes;
    private final NodeListModel nodesModel;
    
    private final JTextField txtInput = new JTextField();
    private final JButton btnClear    = new JButton("C");
    
    public static final Font FONT_ALERT  = new Font("Courier New", Font.PLAIN, 16);
    public static final Font FONT_LIST   = new Font("Courier New", Font.PLAIN, 12);
    public static final Font FONT_INPUT  = new Font("Courier New", Font.PLAIN, 14);
    public static final Font FONT_BUTTON = new Font("Courier New", Font.PLAIN, 12);
    public static final Font FONT_OUTPUT = new Font("Courier New", Font.PLAIN, 14);
    
    public MainWindow(){
        super("Giac Visual Java");
        super.setLayout(new BorderLayout());
        this.nodesModel = new NodeListModel();
        this.lstNodes  = new JList<>(nodesModel);
    }
    
    public void display(){
        //-- UPPER PANEL
        lblAlert.setFont(FONT_ALERT);
        lblAlert.setForeground(Color.RED);
        lblAlert.setBackground(Color.WHITE);
        
        btnClearAll.setFont(FONT_BUTTON);
        btnClearAll.setForeground(Color.red);
        btnClearAll.addActionListener(this);
        btnCopy.setFont(FONT_BUTTON);
        btnCopy.addActionListener(this);
        panelUpperLineEnd.add(btnClearAll, BorderLayout.LINE_START);
        panelUpperLineEnd.add(btnCopy, BorderLayout.LINE_END);
        
        panelNorth.add(panelUpperLineEnd, BorderLayout.LINE_END);
        panelNorth.add(lblAlert, BorderLayout.CENTER);
        
        this.add(BorderLayout.NORTH, panelNorth);
        
        //-- CENTER PANEL
        lstNodes.setFont(FONT_LIST);
        lstNodes.setAutoscrolls(true);
        lstNodes.addListSelectionListener(this);
        lstNodes.setCellRenderer(this);        
        scrllList.getViewport().add(lstNodes);
        scrllList.getVerticalScrollBar().addAdjustmentListener(this);
        scrllList.setFocusable(false);
        this.add(BorderLayout.CENTER, scrllList);
        
        //-- BOTTOM PANEL
        btnClear.setFont(FONT_BUTTON);
        btnClear.addActionListener(this);
        panelBottomLineEnd.add(btnClear, BorderLayout.LINE_END);
        
        txtInput.setFont(FONT_INPUT);
        txtInput.setFocusTraversalKeysEnabled(false);
        txtInput.addKeyListener(this);    
        
        panelBottom.add(panelBottomLineEnd, BorderLayout.LINE_END);
        panelBottom.add(txtInput, BorderLayout.CENTER);
        this.add(BorderLayout.SOUTH, panelBottom);
        
        //-- WINDOW CONFIG
        this.addWindowListener(this);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setSize(400, 500);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        super.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        super.setVisible(true);
        txtInput.requestFocus();        
    }
    
    private void inputsEnable(boolean enable){
        btnClear.setEnabled(enable);
        btnClearAll.setEnabled(enable);
        btnCopy.setEnabled(enable);
        txtInput.setEnabled(enable);
        scrllList.setEnabled(enable);
        if(!enable){
            lblAlert.setText("Wait...");
        }
    }
    
    /**
     * GIAC evaluation
     * 
     * @param input expressión to evaluate
     */
    private void processInput(String input){
        try{
            Log.debug("----"+input);
            Node node = new Node(nodesModel.getMaxId(), input, giacContext);
            nodesModel.addNode(node);
            lblAlert.setText(" ");
            txtInput.setText("");
            txtInput.requestFocus();
            lstNodes.repaint();
        } catch (Throwable e) {
            lblAlert.setText(e.getMessage());
            this.repaint();
            // setPositionList();
            Log.error("", e);
        }finally{
            inputsEnable(true);
        }
    }
    
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Log.debug("getListCellRendererComponent");
        return nodesModel.getElementAt(index);
    }
    @Override
    public void valueChanged(ListSelectionEvent e) {
        Log.debug("valueChanged");
    }
    
    @Override
    public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
        Log.debug("adjustmentValueChanged");
        adjustmentEvent.getAdjustable().setValue(adjustmentEvent.getAdjustable().getMaximum());
    }
    
    @Override public void windowOpened(WindowEvent e) {}
    @Override public void windowClosed(WindowEvent e) {}
    @Override public void windowIconified(WindowEvent e) {}
    @Override public void windowDeiconified(WindowEvent e) {}
    @Override public void windowActivated(WindowEvent e) {}
    @Override public void windowDeactivated(WindowEvent e) {}        
    @Override
    public void windowClosing(WindowEvent e) {
        Log.info("Goodbye");
        System.exit(0);
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyPressed(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent keyEvent) {
        //Log.debug("keyReleased "+keyEvent);
        lblAlert.setText(" ");
        if (keyEvent.getKeyCode() == 10 && txtInput.getText() != null && txtInput.getText().length()>2) {
            inputsEnable(false);
            SwingUtilities.invokeLater(() -> {
                processInput(txtInput.getText());
            });
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent actionEvent) {    
        Log.debug("actionPerformed "+actionEvent);
    }

}