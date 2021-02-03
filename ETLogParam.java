import javax.swing.JComponent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.util.Date;
import javax.swing.JOptionPane;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.Border;
import javax.swing.border.BevelBorder;
import javax.swing.text.JTextComponent;
import java.awt.Color;
import javax.swing.SpringLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JPanel;

// 
// Decompiled by Procyon v0.5.36
// 

public class ETLogParam extends JPanel
{
    public JComboBox prefix;
    public JComboBox source;
    public JComboBox target;
    public JTextField startNum;
    public JTextField stopNum;
    public JTextField time;
    public JTextField exp;
    public JTextArea comments;
    public JCheckBox iodine;
    public JCheckBox fringes;
    public JLabel numLabel;
    public JButton okbutton;
    public static int NOT_SAVED;
    public static int AUTO_SAVED;
    public static int IS_SAVED;
    public boolean isSet;
    public boolean autoDateTime;
    public SpringLayout layout;
    Color defaultColor;
    static String clipboard;
    
    public ETLogParam(final String clipboard) {
        this.isSet = false;
        this.autoDateTime = true;
        this.defaultColor = new JComboBox<Object>().getBackground();
        ETLogParam.clipboard = clipboard;
        final String[] items = { "Prefix" };
        final String[] items2 = { "Select a target", "None" };
        final String[] items3 = { "----Data----", "UAr+UAr", "Dark+UAr", "UAr+Dark", "Star+UAr","Star+Thar", "Star+Dark", "StarI2+Dark", "---Calibration---", "Tung+Tung", "Tung+Dark", "Dark+Tung", "ThAr+ThAr", "ThAr+Dark", "Dark+ThAr", "TungI2+TungI2", "TungI2+Dark", "Bias", "Dark", "---Misc---", "Junk", "Comment", "Other" };
        final boolean[] array = { false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, true, true, false, false, false, false, false, false };
        final boolean[] array2 = { false, false, false, false, false, false, true, false, false, false, false, false, false, true, true, true, false, false, false, false, false, false, false, false };
        this.prefix = new JComboBox(items);
        this.source = new JComboBox(items3);
        this.target = new JComboBox(items2);
        this.addCopyPaste(this.startNum = new JTextField(4));
        this.numLabel = new JLabel("to");
        this.addCopyPaste(this.stopNum = new JTextField(4));
        this.addCopyPaste(this.time = new JTextField(8));
        this.addCopyPaste(this.exp = new JTextField(7));
        (this.comments = new JTextArea(2, 20)).setBorder(new BevelBorder(1));
        this.addCopyPaste(this.comments);
        this.iodine = new JCheckBox();
        this.fringes = new JCheckBox();
        this.okbutton = new JButton("OK");
        this.source.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                final int selectedIndex = ETLogParam.this.source.getSelectedIndex();
                ETLogParam.this.iodine.setSelected(array[selectedIndex]);
                ETLogParam.this.fringes.setSelected(array2[selectedIndex]);
                ETLogParam.this.setState(ETLogParam.NOT_SAVED);
            }
        });
        final ActionListener actionListener = new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                ETLogParam.this.setState(ETLogParam.NOT_SAVED);
            }
        };
        this.prefix.addActionListener(actionListener);
        this.target.addActionListener(actionListener);
        this.iodine.addActionListener(actionListener);
        this.fringes.addActionListener(actionListener);
        final JTextComponent[] array3 = { this.startNum, this.stopNum, this.time, this.exp, this.comments };
        for (int i = 0; i < array3.length; ++i) {
            final int n = i;
            array3[n].addFocusListener(new FocusListener() {
                String s = "";
                
                public void focusGained(final FocusEvent focusEvent) {
                    this.s = array3[n].getText();
                }
                
                public void focusLost(final FocusEvent focusEvent) {
                    if (!array3[n].getText().equals(this.s)) {
                        ETLogParam.this.setState(ETLogParam.NOT_SAVED);
                    }
                }
            });
        }
    }
    
    public void addCopyPaste(final JTextComponent textComponent) {
        final JPopupMenu popupMenu = new JPopupMenu();
        final JMenuItem menuItem = new JMenuItem("Copy");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                ETLogParam.clipboard = textComponent.getText();
            }
        });
        final JMenuItem menuItem2 = new JMenuItem("Paste");
        menuItem2.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                textComponent.setText(ETLogParam.clipboard);
            }
        });
        popupMenu.add(menuItem);
        popupMenu.add(menuItem2);
        textComponent.addMouseListener(new MouseListener() {
            public void mouseClicked(final MouseEvent mouseEvent) {
            }
            
            public void mousePressed(final MouseEvent mouseEvent) {
                if ((mouseEvent.getModifiers() & 0x4) != 0x0 && mouseEvent.isPopupTrigger()) {
                    popupMenu.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
                }
            }
            
            public void mouseReleased(final MouseEvent mouseEvent) {
            }
            
            public void mouseEntered(final MouseEvent mouseEvent) {
            }
            
            public void mouseExited(final MouseEvent mouseEvent) {
            }
        });
    }
    
    public void addTo(final Container container) {
        this.setLayout(this.layout = new SpringLayout());
        final String s = "North";
        this.add(this.prefix);
        this.add(this.startNum);
        this.add(this.numLabel);
        this.add(this.stopNum);
        this.add(this.time);
        this.add(this.exp);
        this.add(this.source);
        this.add(this.target);
        this.add(this.iodine);
        this.add(this.fringes);
        this.add(this.comments);
        this.add(this.okbutton);
        this.layout.putConstraint("West", this.prefix, 0, "West", this);
        this.layout.putConstraint("North", this.prefix, 0, s, this);
        this.layout.putConstraint("West", this.startNum, 5, "East", this.prefix);
        this.layout.putConstraint("North", this.startNum, 0, s, this.prefix);
        this.layout.putConstraint("West", this.numLabel, 5, "East", this.startNum);
        this.layout.putConstraint("North", this.numLabel, 0, s, this.prefix);
        this.layout.putConstraint("West", this.stopNum, 5, "East", this.numLabel);
        this.layout.putConstraint("North", this.stopNum, 0, s, this.prefix);
        this.layout.putConstraint("West", this.time, 15, "East", this.stopNum);
        this.layout.putConstraint("North", this.time, 0, s, this.prefix);
        this.layout.putConstraint("West", this.exp, 15, "East", this.time);
        this.layout.putConstraint("North", this.exp, 0, s, this.prefix);
        this.layout.putConstraint("West", this.source, 15, "East", this.exp);
        this.layout.putConstraint("North", this.source, 0, s, this.prefix);
        this.layout.putConstraint("West", this.target, 15, "East", this.source);
        this.layout.putConstraint("North", this.target, 0, s, this.prefix);
        this.layout.putConstraint("West", this.iodine, 35, "East", this.target);
        this.layout.putConstraint("North", this.iodine, 0, s, this.prefix);
        this.layout.putConstraint("West", this.fringes, 35, "East", this.iodine);
        this.layout.putConstraint("North", this.fringes, 0, s, this.prefix);
        this.layout.putConstraint("West", this.comments, 35, "East", this.fringes);
        this.layout.putConstraint("North", this.comments, 0, s, this.prefix);
        this.layout.putConstraint("West", this.okbutton, 15, "East", this.comments);
        this.layout.putConstraint("North", this.okbutton, 0, s, this.prefix);
        this.layout.putConstraint("East", this, 10, "East", this.okbutton);
        this.layout.putConstraint("South", this, 15, "South", this.comments);
        container.add(this);
    }
    
    public String getParamValue() {
        final String string = (String)this.prefix.getSelectedItem() + "\t" + this.startNum.getText() + "\t" + this.stopNum.getText() + "\t" + this.time.getText() + "\t" + this.exp.getText() + "\t" + (String)this.source.getSelectedItem() + "\t" + (String)this.target.getSelectedItem();
        String s;
        if (this.iodine.isSelected()) {
            s = string + "\ttrue";
        }
        else {
            s = string + "\tfalse";
        }
        String str;
        if (this.fringes.isSelected()) {
            str = s + "\ttrue";
        }
        else {
            str = s + "\tfalse";
        }
        return str + "\t" + this.comments.getText().replaceAll("\n", " :: ");
    }
    
    public void setParamValue(final String s) {
        final String[] split = s.split("\t");
        this.prefix.setSelectedItem(split[0]);
        this.startNum.setText(split[1]);
        this.stopNum.setText(split[2]);
        this.time.setText(split[3]);
        this.exp.setText(split[4]);
        this.source.setSelectedItem(split[5]);
        this.target.setSelectedItem(split[6]);
        this.iodine.setSelected(Boolean.parseBoolean(split[7]));
        this.fringes.setSelected(Boolean.parseBoolean(split[8]));
        if (split.length > 9) {
            this.comments.setText(split[9].replaceAll(" :: ", "\n"));
        }
    }
    
    public void reset() {
        this.prefix.removeAllItems();
        this.prefix.addItem("Prefix");
        this.prefix.setSelectedIndex(0);
        this.startNum.setText("");
        this.stopNum.setText("");
        this.time.setText("");
        this.exp.setText("");
        this.source.setSelectedIndex(0);
        this.target.removeAllItems();
        this.target.addItem("Select a target");
        this.target.addItem("None");
        this.target.setSelectedIndex(0);
        this.iodine.setSelected(false);
        this.fringes.setSelected(false);
        this.comments.setText("");
        this.setState(ETLogParam.NOT_SAVED);
    }
    
    public String[] getParamCSV() {
        if (((String)this.source.getSelectedItem()).equals("Comment")) {
            return new String[] { "COMMENT: " + this.comments.getText().replaceAll(",", ";") };
        }
        final int int1 = Integer.parseInt(this.startNum.getText());
        final int int2 = Integer.parseInt(this.stopNum.getText());
        if (int1 > int2) {
            this.error("Stop index must be greater than start index!");
            return new String[1];
        }
        final String[] array = new String[int2 - int1 + 1];
        for (int i = 0; i < array.length; ++i) {
            final int n = int1 + i;
            array[i] = (String)this.prefix.getSelectedItem();
            final StringBuilder sb = new StringBuilder();
            final String[] array2 = array;
            final int n2 = i;
            array2[n2] = sb.append(array2[n2]).append("0000".substring(0, 4 - ("" + n).length())).append(n).toString();
            final StringBuilder sb2 = new StringBuilder();
            final String[] array3 = array;
            final int n3 = i;
            array3[n3] = sb2.append(array3[n3]).append(",").append(this.time.getText()).toString();
            final StringBuilder sb3 = new StringBuilder();
            final String[] array4 = array;
            final int n4 = i;
            array4[n4] = sb3.append(array4[n4]).append(",").append(this.exp.getText()).toString();
            final StringBuilder sb4 = new StringBuilder();
            final String[] array5 = array;
            final int n5 = i;
            array5[n5] = sb4.append(array5[n5]).append(",").append((String)this.source.getSelectedItem()).toString();
            final StringBuilder sb5 = new StringBuilder();
            final String[] array6 = array;
            final int n6 = i;
            array6[n6] = sb5.append(array6[n6]).append(",").append((String)this.target.getSelectedItem()).toString();
            if (this.iodine.isSelected()) {
                final StringBuilder sb6 = new StringBuilder();
                final String[] array7 = array;
                final int n7 = i;
                array7[n7] = sb6.append(array7[n7]).append(",true").toString();
            }
            else {
                final StringBuilder sb7 = new StringBuilder();
                final String[] array8 = array;
                final int n8 = i;
                array8[n8] = sb7.append(array8[n8]).append(",false").toString();
            }
            if (this.fringes.isSelected()) {
                final StringBuilder sb8 = new StringBuilder();
                final String[] array9 = array;
                final int n9 = i;
                array9[n9] = sb8.append(array9[n9]).append(",true").toString();
            }
            else {
                final StringBuilder sb9 = new StringBuilder();
                final String[] array10 = array;
                final int n10 = i;
                array10[n10] = sb9.append(array10[n10]).append(",false").toString();
            }
            final StringBuilder sb10 = new StringBuilder();
            final String[] array11 = array;
            final int n11 = i;
            array11[n11] = sb10.append(array11[n11]).append(",").append(this.comments.getText().replaceAll(",", ";").replaceAll("\n", " :: ")).toString();
        }
        return array;
    }
    
    public void setParamCSV(final String s) {
        if (s.startsWith("COMMENT: ")) {
            this.comments.setText(s.substring(9));
            return;
        }
        final String[] split = s.split(",");
        final int beginIndex = split[0].length() - 4;
        this.prefix.setSelectedItem(split[0].substring(0, beginIndex));
        this.startNum.setText(split[0].substring(beginIndex));
        this.stopNum.setText(split[0].substring(beginIndex));
        this.time.setText(split[1]);
        this.exp.setText(split[2]);
        this.source.setSelectedItem(split[3]);
        this.target.setSelectedItem(split[4]);
        this.iodine.setSelected(Boolean.parseBoolean(split[5]));
        this.fringes.setSelected(Boolean.parseBoolean(split[6]));
        if (split.length > 7) {
            this.comments.setText(split[7].replaceAll(" :: ", "\n"));
        }
    }
    
    public String[] getParamForPrint() {
        if (((String)this.source.getSelectedItem()).equals("Comment")) {
            return new String[] { this.comments.getText().replaceAll(",", ";") };
        }
        final String[] array = new String[10];
        final int int1 = Integer.parseInt(this.startNum.getText());
        final int int2 = Integer.parseInt(this.stopNum.getText());
        if (int1 > int2) {
            this.error("Stop index must be greater than start index!");
            return new String[1];
        }
        array[0] = (String)this.prefix.getSelectedItem();
        array[1] = "0000".substring(0, 4 - ("" + int1).length()) + int1;
        array[2] = "0000".substring(0, 4 - ("" + int2).length()) + int2;
        array[3] = this.time.getText();
        array[4] = this.exp.getText();
        array[5] = (String)this.source.getSelectedItem();
        array[6] = (String)this.target.getSelectedItem();
        if (this.iodine.isSelected()) {
            array[7] = "Y";
        }
        else {
            array[7] = "N";
        }
        if (this.fringes.isSelected()) {
            array[8] = "Y";
        }
        else {
            array[8] = "N";
        }
        array[9] = this.comments.getText().replaceAll(",", ";");
        return array;
    }
    
    public void addTarget(final String item) {
        this.target.addItem(item);
    }
    
    public void addPrefix(final String s) {
        this.prefix.addItem(s);
        if (!this.isSet) {
            this.prefix.setSelectedItem(s);
        }
    }
    
    public void removePrefix(final String anObject) {
        this.prefix.removeItem(anObject);
    }
    
    public void removeTarget(final String anObject) {
        this.target.removeItem(anObject);
    }
    
    public void warning(final String message) {
        JOptionPane.showMessageDialog(null, message, "Warning", 2);
    }
    
    public void error(final String message) {
        JOptionPane.showMessageDialog(null, message, "Error", 0);
    }
    
    public boolean validateParam() {
        final String s = (String)this.source.getSelectedItem();
        if (this.prefix.getSelectedIndex() == 0 && !s.equals("Comment")) {
            this.error("Prefix incorrect!");
            return false;
        }
        try {
            if (!s.equals("Comment")) {
                final int int1 = Integer.parseInt(this.startNum.getText());
                if (this.stopNum.getText().equals("")) {
                    this.stopNum.setText(this.startNum.getText());
                }
                if (int1 > Integer.parseInt(this.stopNum.getText())) {
                    this.error("Stop index must be greater than start index!");
                    return false;
                }
            }
        }
        catch (Exception ex) {
            this.error("Start and/or stop #: " + ex.toString());
            return false;
        }
        if (this.time.getText().equals("") && !s.equals("Comment")) {
            String s2 = new Date().toString();
            for (int i = 0; i < 3; ++i) {
                s2 = s2.substring(s2.indexOf(" ") + 1);
            }
            if (this.autoDateTime) {
                this.time.setText(s2.substring(0, s2.indexOf(" ")));
            }
        }
        if (this.exp.getText().equals("") && !s.equals("Comment")) {
            this.error("Exposure time missing!");
            return false;
        }
        if (s.startsWith("--")) {
            this.error("Source incorrect!");
            return false;
        }
        if (this.target.getSelectedIndex() == 0) {
            if (s.equals("Target Template") || s.equals("Target")) {
                this.error("Target incorrect!");
                return false;
            }
            this.target.setSelectedIndex(1);
        }
        if (((String)this.prefix.getSelectedItem()).indexOf(";") != -1) {
            this.warning("<html>Prefixes with a ; are not compatible with the pipeline.  Suggest changing.</html>");
        }
        if (((String)this.target.getSelectedItem()).indexOf(";") != -1) {
            this.warning("<html>Target names with a ; are not compatible with the pipeline.  Suggest changing.</html>");
        }
        return true;
    }
    
    public void setState(final int n) {
        Color color;
        Color background;
        if (n == ETLogParam.IS_SAVED) {
            color = Color.GREEN;
            background = Color.GREEN;
            this.isSet = true;
        }
        else if (n == ETLogParam.AUTO_SAVED) {
            color = Color.CYAN;
            background = Color.CYAN;
            this.isSet = true;
        }
        else {
            color = this.defaultColor;
            background = Color.WHITE;
            this.isSet = false;
        }
        this.prefix.setBackground(color);
        this.startNum.setBackground(background);
        this.stopNum.setBackground(background);
        this.time.setBackground(background);
        this.exp.setBackground(background);
        this.source.setBackground(color);
        this.target.setBackground(color);
        this.iodine.setBackground(color);
        this.fringes.setBackground(color);
        this.comments.setBackground(background);
        this.okbutton.setBackground(color);
    }
    
    public void addOKAction(final ETLogGUI etLogGUI) {
        this.okbutton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                ETLogParam.this.setState(ETLogParam.NOT_SAVED);
                if (!ETLogParam.this.validateParam()) {
                    return;
                }
                if (!ETLogParam.this.validateFilenames(etLogGUI)) {
                    return;
                }
                ETLogParam.this.isSet = true;
                try {
                    final PrintWriter printWriter = new PrintWriter(new FileOutputStream("autolog.dat"));
                    printWriter.println("#ET Observation Log -- Auto Save");
                    printWriter.println("#Telescope: " + etLogGUI.tTelescope.getText());
                    printWriter.println("#Run ID: " + etLogGUI.tRun.getText());
                    printWriter.println("#Date: " + etLogGUI.tDate.getText());
                    printWriter.println("#Observers: " + etLogGUI.tObservers.getText());
                    final String[] split = etLogGUI.tWeather.getText().split("\n");
                    for (int i = 0; i < split.length; ++i) {
                        printWriter.println("#Weather: " + split[i]);
                    }
                    final String[] split2 = etLogGUI.tTopComments.getText().split("\n");
                    for (int j = 0; j < split2.length; ++j) {
                        printWriter.println("#Comments: " + split2[j]);
                    }
                    for (int k = 0; k < etLogGUI.params.length; ++k) {
                        if (etLogGUI.params[k].isSet) {
                            printWriter.println(etLogGUI.params[k].getParamValue());
                        }
                    }
                    printWriter.close();
                }
                catch (IOException ex) {
                    ETLogParam.this.error(ex.toString());
                    return;
                }
                ETLogParam.this.setState(ETLogParam.AUTO_SAVED);
            }
        });
    }
    
    public boolean validateFilenames(final ETLogGUI etLogGUI) {
        if (!((String)this.source.getSelectedItem()).equals("Comment")) {
            final int selectedIndex = this.prefix.getSelectedIndex();
            try {
                final int int1 = Integer.parseInt(this.startNum.getText());
                for (int int2 = Integer.parseInt(this.stopNum.getText()), i = int1; i <= int2; ++i) {
                    for (int j = 0; j < etLogGUI.size; ++j) {
                        if (etLogGUI.params[j] != this) {
                            try {
                                final int int3 = Integer.parseInt(etLogGUI.params[j].startNum.getText());
                                final int int4 = Integer.parseInt(etLogGUI.params[j].stopNum.getText());
                                if (etLogGUI.params[j].prefix.getSelectedIndex() == selectedIndex && i >= int3 && i <= int4) {
                                    this.error("File " + (String)this.prefix.getSelectedItem() + i + " has already been used.");
                                    return false;
                                }
                            }
                            catch (NumberFormatException ex2) {}
                        }
                    }
                }
            }
            catch (NumberFormatException ex) {
                this.error("Start and/or stop #: " + ex.toString());
                return false;
            }
        }
        return true;
    }
    
    public void setAutoDateTime(final boolean autoDateTime) {
        this.autoDateTime = autoDateTime;
    }
    
    public void setConstraints(final SpringLayout springLayout, final int pad, final int pad2, final JComponent c2, final JComponent c3, final boolean b) {
        String e2;
        if (b) {
            e2 = "North";
        }
        else {
            e2 = "South";
        }
        springLayout.putConstraint("West", this, pad2, "West", c2);
        springLayout.putConstraint("North", this, pad, e2, c3);
    }
    
    public void addHeaders() {
        final JLabel label = new JLabel("Prefix");
        final JLabel c1 = new JLabel("Start #");
        final JLabel c2 = new JLabel("Stop #");
        final JLabel c3 = new JLabel("Time");
        final JLabel c4 = new JLabel("Exposure");
        final JLabel c5 = new JLabel("Source");
        final JLabel c6 = new JLabel("Target");
        final JLabel c7 = new JLabel("Iodine");
        final JLabel c8 = new JLabel("ThAr");
        final JLabel c9 = new JLabel("Comments");
        this.add(label);
        this.add(c1);
        this.add(c2);
        this.add(c3);
        this.add(c4);
        this.add(c5);
        this.add(c6);
        this.add(c7);
        this.add(c8);
        this.add(c9);
        this.layout.putConstraint("West", label, 10, "West", this);
        this.layout.putConstraint("North", label, 0, "North", this);
        this.layout.putConstraint("North", this.prefix, 5, "South", label);
        final JComponent[] array = { this.startNum, this.numLabel, this.stopNum, this.time, this.exp, this.source, this.target, this.iodine, this.fringes, this.comments, this.okbutton };
        for (int i = 0; i < array.length; ++i) {
            this.layout.putConstraint("North", array[i], 5, "South", label);
        }
        this.layout.putConstraint("West", c1, 5, "East", this.prefix);
        this.layout.putConstraint("North", c1, 0, "North", label);
        this.layout.putConstraint("West", c2, 5, "East", this.numLabel);
        this.layout.putConstraint("North", c2, 0, "North", label);
        this.layout.putConstraint("West", c3, 15, "East", this.stopNum);
        this.layout.putConstraint("North", c3, 0, "North", label);
        this.layout.putConstraint("West", c4, 15, "East", this.time);
        this.layout.putConstraint("North", c4, 0, "North", label);
        this.layout.putConstraint("West", c5, 15, "East", this.exp);
        this.layout.putConstraint("North", c5, 0, "North", label);
        this.layout.putConstraint("West", c6, 15, "East", this.source);
        this.layout.putConstraint("North", c6, 0, "North", label);
        this.layout.putConstraint("West", c7, 25, "East", this.target);
        this.layout.putConstraint("North", c7, 0, "North", label);
        this.layout.putConstraint("West", c8, 25, "East", this.iodine);
        this.layout.putConstraint("North", c8, 0, "North", label);
        this.layout.putConstraint("West", c9, 35, "East", this.fringes);
        this.layout.putConstraint("North", c9, 0, "North", label);
    }
    
    static {
        ETLogParam.NOT_SAVED = 0;
        ETLogParam.AUTO_SAVED = 1;
        ETLogParam.IS_SAVED = 2;
    }
}
