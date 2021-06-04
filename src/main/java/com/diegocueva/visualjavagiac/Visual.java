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
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import javagiac.context;
import javagiac.gen;
import javagiac.giac;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public final class Visual extends JFrame
        implements KeyListener, AdjustmentListener,
        Runnable, WindowListener, ActionListener,
        ListSelectionListener, ListCellRenderer {

    /**
     * STACK SIZE !!!
     */
    public static final int STACK_SIZE = 3;

    /**
     * Font for list elements
     */
    public static final Font FONT_LIST = new Font("Courier New", Font.PLAIN, 14);

    /**
     * Font for button elements
     */
    public static final Font FONT_BUTTON = new Font("Arial", Font.BOLD, 11);
    public static final Font FONT_BUTTON_SMALL = new Font("Arial", Font.PLAIN, 9);

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Stack de expresiones
     */
    private final JList expressions;

    /**
     * Para poder hacer scroll vertical
     */
    private final JScrollPane jsrcoll;

    /**
     * Caja de texto inferior comando
     */
    private final JTextField cmdText;

    /**
     * Panel for numebr list and visual expressions
     */
    private final JPanel twoList;

    /**
     * Singleton
     */
    private static final Visual visual = new Visual();

    /**
     * Giac context
     */
    private static context C;

    /**
     * Input string to be evaluated by giac
     */
    private String toProcess = null;

    /**
     * Messages
     */
    private final JLabel alert = new JLabel("Welcome to Giac/Xcas Visual Java");

    /**
     * Stack
     */
    private final Stack stack;

    /**
     * Visual JLabel elements
     */
    private final JComponent[] components = new JComponent[STACK_SIZE * 2];

    /**
     * Variables for symbolic solve
     */
    private static final char vars[] = {'x', 'y', 'z', 't', 'n'};

    private static int indVar = 0;

    /**
     * Util Buttons
     */
    private final JButton button_C   = new JButton("C");
    private final JButton button_CL  = new JButton("CL");
    private final JButton button_N   = new JButton("S=D");
    private final JButton button_X   = new JButton("x");
    private final JButton button_CPY = new JButton("copy");
    private final JButton button_EVA = new JButton("eval");

    /**
     * Function buttons
     */
    private final JButton[] FbButton;
    /**
     * Labels
     */
    private final String[] FbLabel = {
        "root", "lcm", "gcd", "rand", "fact", "sipl",
        "fmax", "fmin", "perm", "+inf", "-inf", "limt",
        "canf", "solv", "diff", "intg", "sum", "lin"
    };
    /**
     * Input String
     */
    private final String[] FbInput = {
        "root(3,?)", "lcm(,)", "gcd(,)", "rand()", "factor(?)", "simplify(?)",
        "fmax(?,?)", "fmin(?,?)", "perm(,)", "+infinity", "-infinity", "limit(?, ?=0)",
        "canonical_form(?)", "solve(?=0, ?)", "function_diff(?)(?)", "integrate(?,?)", "sum(?, ?, 1, +infinity)", "lin(?)"
    };

    static {
        try {
            // System.load("/usr/local/lib/libgiacjava.so");
            loadWindowsLibrary();
        } catch (UnsatisfiedLinkError e) {
            Log.error("Native code library failed to load. See the chapter on Dynamic Linking Problems in the SWIG Java documentation for help.\n", e);
            System.exit(1);
        }
    }

    public static void loadWindowsLibrary() {
        Log.info("java.library.path:\n" + System.getProperty("java.library.path"));
        System.loadLibrary("javagiac");
        Log.info("Loaded javagiac");
    }

    /**
     * Entry point
     *
     * @param args
     */
    public static void main(String[] args) {
        Log.init("GXVJ");        
        visual.init();
        Thread thread = new Thread(visual);
        thread.start();
    }

    /**
     * Constructor for main frame. Create visual components and build the swing
     * interface
     */
    private Visual() {
        super("Giac/Xcas Visual Java");
        stack       = new Stack(STACK_SIZE, C);
        cmdText     = new JTextField();
        expressions = new JList(components);
        twoList     = new JPanel();
        jsrcoll     = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        FbButton    = new JButton[FbLabel.length];
    }
    
    public void init(){
        // Init giac context
        C = new context();


        this.addWindowListener(this);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        /**
         * Labels for stack
         */
        for (int i = 0; i < STACK_SIZE; i++) {
            components[i * 2] = newJLabelInput();
            components[i * 2 + 1] = newJCanvasResult();
        }

        cmdText.setFont(new Font("Courier New", Font.PLAIN, 14));

        alert.setFont(new Font("Courier New", Font.BOLD, 16));
        alert.setForeground(Color.red);
        alert.setBackground(Color.white);

        expressions.setAutoscrolls(true);
        expressions.addListSelectionListener(this);
        expressions.setCellRenderer(this);

        twoList.setLayout(new BorderLayout());
        twoList.add(BorderLayout.CENTER, expressions);

        jsrcoll.getViewport().add(twoList);

        button_C.setFont(FONT_BUTTON);
        button_CL.setFont(FONT_BUTTON);
        button_N.setFont(FONT_BUTTON);
        button_X.setFont(new Font("Arial", Font.ITALIC | Font.BOLD, 14));
        button_CPY.setFont(FONT_BUTTON_SMALL);
        button_EVA.setFont(FONT_BUTTON_SMALL);

        button_CL.setForeground(Color.red);
        button_X.setForeground(Color.blue);

        button_C.addActionListener(this);
        button_CL.addActionListener(this);
        button_N.addActionListener(this);
        button_X.addActionListener(this);
        button_CPY.addActionListener(this);
        button_EVA.addActionListener(this);

        JPanel buttonsPannel = new JPanel();
        buttonsPannel.setLayout(new GridLayout(4, 6, 10, 10));
        buttonsPannel.add(button_N);
        buttonsPannel.add(button_EVA);
        buttonsPannel.add(button_X);
        buttonsPannel.add(button_CPY);
        buttonsPannel.add(button_CL);
        buttonsPannel.add(button_C);

        for (int i = 0; i < FbLabel.length; i++) {
            FbButton[i] = new JButton(FbLabel[i]);
            FbButton[i].setFont(FONT_BUTTON_SMALL);
            FbButton[i].addActionListener(this);
            buttonsPannel.add(FbButton[i]);
        }

        JPanel panelSouth = new JPanel();
        panelSouth.setLayout(new BorderLayout());
        panelSouth.add(cmdText, BorderLayout.PAGE_START);
        panelSouth.add(buttonsPannel, BorderLayout.CENTER);

        // Ventana principal
        setSize(400, 500);
        setLayout(new BorderLayout());
        add(BorderLayout.NORTH, alert);
        add(BorderLayout.CENTER, jsrcoll);
        add(BorderLayout.SOUTH, panelSouth);

        jsrcoll.getVerticalScrollBar().addAdjustmentListener(this);
        jsrcoll.setFocusable(false);

        // Listeners
        cmdText.setFocusTraversalKeysEnabled(false);
        cmdText.addKeyListener(this);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        super.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        super.setVisible(true);
        cmdText.requestFocus();
    }

    /**
     * Thread for process
     */
    public void run() {
        cmdText.requestFocus();
        for (;;) {
            while (toProcess == null) {
                synchronized (visual) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Log.error("", e);
                    }
                }
            }

            try {
                /**
                 * GIAC evaluation
                 */
                String input = toProcess;
                gen g = new gen (input, C);
                gen f = giac._factor(g, C);
                gen e = giac._eval  (f, C);
                gen l = giac._latex (e, C);
                stack.put(input, e, l);

                repaintElements();
            } catch (Throwable ee) {
                alert.setText(ee.getMessage());
                this.repaint();
                setPositionList();
                Log.error("", ee);
            }
            toProcess = null;
            enableInputs();
            cmdText.requestFocus();
        }
    }

    public void setPositionList() {
        initScroll = false;
        jsrcoll.getViewport().setViewPosition(new Point(0, 0));
    }

    public void repaintElements() {
        twoList.repaint();
        setPositionList();
        cmdText.setText("");
        expressions.clearSelection();
        cmdText.requestFocus();
    }

    public JLabel newJLabelInput() {
        JLabel res = new JLabel("", JLabel.LEFT);
        res.setFont(new Font("Courier New", Font.PLAIN, 14));
        return res;
    }

    public ExpressionCanvas newJCanvasResult() {
        ExpressionCanvas res = new ExpressionCanvas("", JLabel.RIGHT);
        res.setFont(new Font("Courier New", Font.PLAIN, 20));
        return res;
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        int ind = index / 2;
        if ((index % 2) == 0) {
            ((JLabel)components[index]).setText(stack.getInput(ind));
        } else {
            ExpressionCanvas expr = ((ExpressionCanvas)components[index]);
            // expr.setText("\n");
            ExpressionCanvas canvas = ((ExpressionCanvas)components[index]);
            BufferedImage image = stack.getImage(ind);
            if(image != null){
                canvas.setImageBuffer(image);
                canvas.setLatexStr(stack.getLatex(ind));
            }            
        }

        return components[index];
    }

    public void disableInputs() {
        expressions.setEnabled(false);
        cmdText.setEnabled(false);
        button_C.setEnabled(false);
        button_CL.setEnabled(false);
        button_N.setEnabled(false);
        button_CPY.setEnabled(false);
        button_EVA.setEnabled(false);
    }

    public void enableInputs() {
        expressions.setEnabled(true);
        cmdText.setEnabled(true);
        button_X.setEnabled(true);
        button_C.setEnabled(true);
        button_CL.setEnabled(true);
        button_N.setEnabled(true);
        button_CPY.setEnabled(true);
        button_EVA.setEnabled(true);
    }

    public void sendInputToProcess(String input) {
        synchronized (visual) {
            toProcess = input;
            disableInputs();
            notifyAll();
        }
    }

    int indCursor = (STACK_SIZE) * 2 + 1;

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        alert.setText(" ");
        if (e.getKeyCode() == 10) {
            indCursor = (STACK_SIZE) * 2 + 1;
            if (toProcess != null) {
                return;
            }
            String input = cmdText.getText().trim();
            if (input.length() <= 0) {
                return;
            }
            sendInputToProcess(input);
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                if (indCursor < ((STACK_SIZE) * 2) + 1) {
                    String newInput = getTextIndCursor(indCursor + 1);
                    if (newInput.length() > 0) {
                        cmdText.setText(newInput);
                        indCursor++;
                    }
                }
                break;
            case KeyEvent.VK_UP:
                if (indCursor > 1) {
                    String newInput = getTextIndCursor(indCursor - 1);
                    if (newInput.length() > 0) {
                        cmdText.setText(newInput);
                        indCursor--;
                    }
                }
                break;
        }

    }

    private String getTextIndCursor(int indC) {
        int ind = (indC - 1) / 2;
        if (ind <= STACK_SIZE - 1) {
            if ((indC % 2) != 0) {
                return stack.getInput(ind).trim();
            } else {
                return stack.getVisual(ind).trim();
            }
        } else {
            return "";
        }
    }

    private static boolean initScroll = false;

    public void adjustmentValueChanged(AdjustmentEvent e) {
        if (!initScroll) {
            initScroll = true;
            e.getAdjustable().setValue(e.getAdjustable().getMaximum());
        }
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        Log.info("Goodbye");
        System.exit(0);
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == expressions && e.getValueIsAdjusting()) {
            int index = expressions.getSelectedIndex();
            int ind = index / 2;
            if (index >= 0) {
                if ((index % 2) == 0) {
                    String addInput = stack.getInput(index / 2);
                    String previo = cmdText.getText();
                    cmdText.setText(previo + addInput);
                } else {
                    String addResult = stack.getVisual(ind);
                    String previo = cmdText.getText();
                    cmdText.setText(previo + addResult);
                }
                cmdText.requestFocus();
                expressions.clearSelection();
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button_C) {
            cmdText.setText("");
        }
        if (e.getSource() == button_CL) {
            stack.clear();
            repaintElements();
        }
        if (e.getSource() == button_N) {
            gen g = stack.getResult(STACK_SIZE - 1);
            if (g != null) {
                String input = "evalf(" + g.print(C) + ")";
                sendInputToProcess(input);
            }
        }
        if (e.getSource() == button_EVA) {
            gen g = stack.getResult(STACK_SIZE - 1);
            if (g != null) {
                String input = "evala(" + g.print(C) + ")";
                sendInputToProcess(input);
            }
        }
        if (e.getSource() == button_X) {
            indVar = (indVar + 1) % vars.length;
            button_X.setText(String.valueOf(vars[indVar]));
            button_X.repaint();
        }
        if (e.getSource() == button_CPY) {
            StringBuilder text = new StringBuilder();

            for (int i = 0; i < STACK_SIZE; i++) {
                String input = stack.getInput(i).trim();
                if (input.length() > 0) {
                    String result = stack.getVisual(i);
                    text.append(input);
                    text.append("\n\t");
                    text.append(result);
                    text.append("\n");
                }
            }

            StringSelection stringSelection = new StringSelection(text.toString());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }

        /**
         * Function buttons
         */
        for (int i = 0; i < FbLabel.length; i++) {
            if (e.getSource() == FbButton[i]) {
                cmdText.setText(FbInput[i].replace('?', vars[indVar]));
            }
        }

        cmdText.requestFocus();
    }

}
