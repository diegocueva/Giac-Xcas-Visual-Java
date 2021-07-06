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
 * 
        solve(x^2-x-1=0,x)
        solve(x^3-x-27=0,x)
        8/((8^(1/3))*(8^(1/3)))
        desolve(3*x^3*diff(y)=((3*x^2-y^2)*y),y)
        desolve(x*y'-2*y-x*exp(4/x)*y^3,y)
        desolve(3*x^3*diff(y)=((3*x^2-y^2)*y),y)
        factor_xn(-x^4+3)
        collect((x^3-2*x^2+1)*sqrt(5))
        factors([x^3-2*x^2+1,x^2-x])
 */
package com.diegocueva.giacvisualjava;

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
import java.util.Collections;
import javagiac.context;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MainWindow extends JFrame implements KeyListener, AdjustmentListener, WindowListener, ActionListener, ListSelectionListener, ListCellRenderer {
    
    private static final long serialVersionUID = 1L;
    public static final int LIST_SIZE = 150;
    
    private final context giacContext = new context();
    
    private final JPanel panelNorth         = new JPanel(new BorderLayout());
    private final JPanel panelUpperLineEnd  = new JPanel(new BorderLayout());
    private final JScrollPane scrllList = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);    
    private final JPanel panelBottom        = new JPanel(new BorderLayout());
    private final JPanel panelBottomLineEnd = new JPanel(new BorderLayout());
    
    private final JTextField lblAlert = new JTextField("www.diegocueva.com");
    private final JButton btnClearAll = new JButton("CL");
    
    private final JPanel listPanel = new JPanel(new BorderLayout());
    private final JList<Node> lstNodes;
    private final DefaultListModel<Node> nodesModel;
    
    private final JTextField txtInput = new JTextField();
    private final JButton btnClear    = new JButton("C");
    private final ViewerManager viewerManager = new ViewerManager();
            
    public static final Font FONT_ALERT  = new Font("Courier New", Font.PLAIN, 16);
    public static final Font FONT_INPUT  = new Font("Courier New", Font.PLAIN, 14);
    public static final Font FONT_OUTPUT = new Font("Courier New", Font.ITALIC, 12);
    public static final Font FONT_BUTTON = new Font("Courier New", Font.BOLD, 12);
    
    private boolean initScroll = false;
    private int     indCursor  = 0;
    
    public MainWindow(){
        super("Giac Visual Java");
        super.setLayout(new BorderLayout());
        this.nodesModel = new DefaultListModel();
        this.lstNodes = new JList<>(this.nodesModel);
    }
    
    public void display(){
        //-- UPPER PANEL
        lblAlert.setFont(FONT_ALERT);
        lblAlert.setForeground(Color.RED);
        lblAlert.setBackground(Color.WHITE);
        lblAlert.setEditable(false);
        
        btnClearAll.setFont(FONT_BUTTON);
        btnClearAll.setForeground(Color.red);
        btnClearAll.addActionListener(this);
        panelUpperLineEnd.add(btnClearAll, BorderLayout.LINE_START);
        
        panelNorth.add(panelUpperLineEnd, BorderLayout.LINE_END);
        panelNorth.add(lblAlert, BorderLayout.CENTER);
        
        this.add(BorderLayout.NORTH, panelNorth);
        
        //-- CENTER PANEL
        lstNodes.setAutoscrolls(true);
        lstNodes.setBackground(Color.WHITE);
        lstNodes.addListSelectionListener(this);
        lstNodes.setCellRenderer(this);
        listPanel.add(BorderLayout.CENTER, lstNodes);
        scrllList.getViewport().add(listPanel);        
        this.add(BorderLayout.CENTER, scrllList);
        scrllList.getVerticalScrollBar().addAdjustmentListener(this);
        scrllList.setFocusable(false);        
        
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
            Log.debug("processInput "+input);
            int id = Collections.list(nodesModel.elements()).stream().mapToInt(n->n.getId()).max().orElse(0);
            txtInput.requestFocus();
            Node node = new Node(id+1, input, giacContext);
            if(nodesModel.size() >= LIST_SIZE){
                nodesModel.remove(0);
            }
            nodesModel.addElement(node);
            lblAlert.setText(node.getOutput());
            inputsEnable(true);            
            afterProcess();
        } catch (Throwable e) {
            lblAlert.setText(e.getMessage());
            this.repaint();
            lstNodes.clearSelection();
            setPositionList();
            Log.error("", e);
            inputsEnable(true);
        }
    }
    
    private void afterProcess(){
        listPanel.repaint();
        lstNodes.clearSelection();
        txtInput.setText("");
        txtInput.requestFocus();        
        setPositionList();
        indCursor=0;
    }
    
    private void setPositionList() {
        initScroll = false;
        JScrollBar vertical = scrllList.getVerticalScrollBar();
        vertical.setValue( vertical.getMaximum() );
    }
    
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        return nodesModel.getElementAt(index);
    }
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == lstNodes && e.getValueIsAdjusting()) {
            int index = lstNodes.getSelectedIndex();
            if (index >= 0) {
                Node node = nodesModel.get(index);
                txtInput.requestFocus();
                lstNodes.clearSelection();
                viewerManager.add(node);
            }
        }
        
    }
    
    @Override
    public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
        if (!initScroll) {
            initScroll = true;
            adjustmentEvent.getAdjustable().setValue(adjustmentEvent.getAdjustable().getMaximum());
        }
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
        if(keyEvent.getSource() == txtInput){
            lblAlert.setText(" ");
            if (keyEvent.getKeyCode() == 10 && txtInput.getText() != null && txtInput.getText().length()>=1) {
                inputsEnable(false);
                SwingUtilities.invokeLater(() -> {
                    processInput(txtInput.getText());
                });
            }else if(nodesModel.size()>=1){
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if (indCursor < nodesModel.size()) {
                            txtInput.setText(nodesModel.get(nodesModel.size()-indCursor-1).getInput());
                            indCursor++;
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (indCursor > 1) {
                            txtInput.setText(nodesModel.get(nodesModel.size()-indCursor+1).getInput());
                            indCursor--;
                        }else{
                            txtInput.setText("");
                            indCursor=0;
                        }
                        break;
                }            
            }            
        }

    }
    
    @Override
    public void actionPerformed(ActionEvent actionEvent) {    
        if(actionEvent.getSource()==btnClear){
            this.txtInput.setText("");
        }
        if(actionEvent.getSource()==btnClearAll){
            this.nodesModel.clear();
            this.viewerManager.clear();
            afterProcess();
        }
    }

}