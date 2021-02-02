import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.BevelBorder;
import javax.swing.JList;
import java.awt.Container;
import javax.swing.JComboBox;
import java.awt.Dimension;
import javax.swing.JEditorPane;
import java.util.Date;
import java.awt.Color;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import javax.swing.JSeparator;
import javax.swing.JFileChooser;
import javax.swing.Icon;
import java.awt.Component;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.LayoutManager;
import javax.swing.BoxLayout;
import java.io.File;
import java.awt.Font;
import javax.swing.SpringLayout;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JFrame;

// 
// Decompiled by Procyon v0.5.36
// 

public class ETLogGUI extends JFrame
{
    int xdim;
    int ydim;
    int size;
    JPanel topPanel;
    JPanel bottomPanel;
    JMenuBar jmb;
    JMenu fileMenu;
    JMenu printMenu;
    JMenu helpMenu;
    JMenu targetMenu;
    JMenu optMenu;
    JMenuItem fileClear;
    JMenuItem fileLoad;
    JMenuItem fileRestore;
    JMenuItem fileSave;
    JMenuItem fileSaveAs;
    JMenuItem fileExit;
    JMenuItem printToFile;
    JMenuItem printToPrinter;
    JMenuItem helpAbout;
    JMenuItem helpHelp;
    JMenuItem helpKPNO;
    JMenuItem targetLoad;
    JMenuItem targetSave;
    JMenuItem optDateTime;
    String filename;
    JScrollPane topScrollPane;
    JScrollPane bottomScrollPane;
    JLabel logLabel;
    JLabel lDate;
    JLabel lObservers;
    JLabel lWeather;
    JLabel lTopComments;
    JLabel lFile;
    JLabel lPrefix;
    JLabel lStartNum;
    JLabel lStopNum;
    JLabel lTime;
    JLabel lExp;
    JLabel lSource;
    JLabel lTarget;
    JLabel lIodine;
    JLabel lFringes;
    JLabel lComments;
    JLabel lTelescope;
    JLabel lRun;
    JTextField tDate;
    JTextField tObservers;
    JTextField tTelescope;
    JTextField tRun;
    JTextArea tWeather;
    JTextArea tTopComments;
    JButton bPrefix;
    JButton bTarget;
    JButton bAddLines;
    ETLogParam[] params;
    SpringLayout topLayout;
    SpringLayout bottomLayout;
    Font labelFont;
    File defaultDir;
    boolean autoDateTime;
    static String clipboard;
    
    public ETLogGUI() {
        this(1180, 768, 100);
    }
    
    public ETLogGUI(final int xdim, final int ydim, final int size) {
        super("PARAS Electronic Log System");
        this.filename = null;
        this.labelFont = new Font("Dialog", 1, 16);
        this.defaultDir = new File(".");
        this.autoDateTime = true;
        this.xdim = xdim;
        this.ydim = ydim;
        this.size = size;
        this.setSize(this.xdim, this.ydim);
        final Container contentPane = this.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, 1));
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent windowEvent) {
                ETLogGUI.this.dispose();
            }
        });
        this.jmb = new JMenuBar();
        this.fileMenu = new JMenu("File");
        (this.fileClear = new JMenuItem("Clear")).addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                final Object[] options = { "Clear", "Cancel" };
                if (JOptionPane.showOptionDialog(ETLogGUI.this, "Are you sure you want clear all fields?", "Clear all fields?", 0, 3, null, options, options[1]) == 0) {
                    for (int i = 0; i < ETLogGUI.this.params.length; ++i) {
                        ETLogGUI.this.params[i].reset();
                    }
                    ETLogGUI.this.filename = null;
                    ETLogGUI.this.lFile.setText("<html>File: Not Saved<br>0 records</html>");
                }
            }
        });
        this.fileMenu.add(this.fileClear);
        (this.fileLoad = new JMenuItem("Load")).addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                final JFileChooser fileChooser = new JFileChooser(ETLogGUI.this.defaultDir);
                if (fileChooser.showOpenDialog((Component)actionEvent.getSource()) == 0) {
                    ETLogGUI.this.filename = fileChooser.getSelectedFile().getAbsolutePath();
                    ETLogGUI.this.defaultDir = fileChooser.getCurrentDirectory();
                    ETLogGUI.this.loadLog(ETLogGUI.this.filename);
                    if (ETLogGUI.this.filename.indexOf(".") != -1) {
                        ETLogGUI.this.filename = ETLogGUI.this.filename.substring(0, ETLogGUI.this.filename.lastIndexOf("."));
                    }
                }
            }
        });
        this.fileMenu.add(this.fileLoad);
        (this.fileRestore = new JMenuItem("Restore")).addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                if (new File("autolog.dat").exists()) {
                    ETLogGUI.this.loadLog("autolog.dat");
                    ETLogGUI.this.filename = null;
                    ETLogGUI.this.lFile.setText("<html>File: Not Saved<br>0 records</html>");
                    for (int i = 0; i < ETLogGUI.this.params.length; ++i) {
                        if (ETLogGUI.this.params[i].isSet) {
                            ETLogGUI.this.params[i].setState(ETLogParam.AUTO_SAVED);
                        }
                    }
                    return;
                }
                ETLogGUI.this.error("File autolog.dat does not exist!");
            }
        });
        this.fileMenu.add(this.fileRestore);
        (this.fileSave = new JMenuItem("Save")).addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                if (ETLogGUI.this.filename != null) {
                    ETLogGUI.this.saveLog(ETLogGUI.this.filename);
                }
                else {
                    final JFileChooser fileChooser = new JFileChooser(ETLogGUI.this.defaultDir);
                    if (fileChooser.showSaveDialog((Component)actionEvent.getSource()) == 0) {
                        ETLogGUI.this.filename = fileChooser.getSelectedFile().getAbsolutePath();
                        if (ETLogGUI.this.filename.indexOf(".") != -1) {
                            ETLogGUI.this.filename = ETLogGUI.this.filename.substring(0, ETLogGUI.this.filename.lastIndexOf("."));
                        }
                        if (new File(ETLogGUI.this.filename + ".dat").exists() || new File(ETLogGUI.this.filename + ".csv").exists()) {
                            final Object[] options = { "Overwrite", "Cancel" };
                            if (JOptionPane.showOptionDialog(ETLogGUI.this, ETLogGUI.this.filename + " already exists.", "File exists!", 0, 2, null, options, options[1]) == 1) {
                                return;
                            }
                        }
                        ETLogGUI.this.defaultDir = fileChooser.getCurrentDirectory();
                        ETLogGUI.this.saveLog(ETLogGUI.this.filename);
                    }
                }
            }
        });
        this.fileMenu.add(this.fileSave);
        (this.fileSaveAs = new JMenuItem("Save As")).addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                final JFileChooser fileChooser = new JFileChooser(ETLogGUI.this.defaultDir);
                if (fileChooser.showSaveDialog((Component)actionEvent.getSource()) == 0) {
                    ETLogGUI.this.filename = fileChooser.getSelectedFile().getAbsolutePath();
                    if (ETLogGUI.this.filename.indexOf(".") != -1) {
                        ETLogGUI.this.filename = ETLogGUI.this.filename.substring(0, ETLogGUI.this.filename.lastIndexOf("."));
                    }
                    if (new File(ETLogGUI.this.filename + ".dat").exists() || new File(ETLogGUI.this.filename + ".csv").exists()) {
                        final Object[] options = { "Overwrite", "Cancel" };
                        if (JOptionPane.showOptionDialog(ETLogGUI.this, ETLogGUI.this.filename + " already exists.", "File exists!", 0, 2, null, options, options[1]) == 1) {
                            return;
                        }
                    }
                    ETLogGUI.this.defaultDir = fileChooser.getCurrentDirectory();
                    ETLogGUI.this.saveLog(ETLogGUI.this.filename);
                }
            }
        });
        this.fileMenu.add(this.fileSaveAs);
        this.fileMenu.add(new JSeparator());
        (this.fileExit = new JMenuItem("Exit")).addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                final Object[] options = { "Exit", "Save and Exit", "Cancel" };
                final int showOptionDialog = JOptionPane.showOptionDialog(ETLogGUI.this, "Are you sure you want to quit?", "Exit ET Log?", 0, 2, null, options, options[1]);
                if (showOptionDialog == 0) {
                    System.exit(0);
                }
                else if (showOptionDialog == 1) {
                    ETLogGUI.this.fileSave.doClick();
                    JOptionPane.showMessageDialog(ETLogGUI.this, "File " + ETLogGUI.this.filename + " has been saved.", "File Saved", 1);
                    System.exit(0);
                }
            }
        });
        this.fileMenu.add(this.fileExit);
        this.printMenu = new JMenu("Print");
        (this.printToFile = new JMenuItem("To ASCII File")).addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                final JFileChooser fileChooser = new JFileChooser(ETLogGUI.this.defaultDir);
                if (fileChooser.showSaveDialog((Component)actionEvent.getSource()) == 0) {
                    final String absolutePath = fileChooser.getSelectedFile().getAbsolutePath();
                    if (new File(absolutePath).exists()) {
                        final Object[] options = { "Overwrite", "Cancel" };
                        if (JOptionPane.showOptionDialog(ETLogGUI.this, absolutePath + " already exists.", "File exists!", 0, 2, null, options, options[1]) == 1) {
                            return;
                        }
                    }
                    ETLogGUI.this.defaultDir = fileChooser.getCurrentDirectory();
                    ETLogGUI.this.printLog(absolutePath);
                }
            }
        });
        this.printMenu.add(this.printToFile);
        (this.printToPrinter = new JMenuItem("To Printer")).addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                ETLogGUI.this.printLog("temp.dat");
                final Vector<String> command = new Vector<String>();
                command.add("enscript");
                command.add("-r");
                command.add("-o");
                command.add("temp.ps");
                command.add("temp.dat");
                final ProcessBuilder processBuilder = new ProcessBuilder(command);
                try {
                    final Process start = processBuilder.start();
                    try {
                        start.waitFor();
                    }
                    catch (InterruptedException ex) {}
                    final Vector<String> command2 = new Vector<String>();
                    command2.add("lpr");
                    command2.add("temp.ps");
                    new ProcessBuilder(command2).start();
                    JOptionPane.showMessageDialog(ETLogGUI.this, "Print successful", "Print successful", 1);
                }
                catch (IOException ex2) {
                    JOptionPane.showMessageDialog(ETLogGUI.this, "Print failed", "Print failed", 1);
                }
            }
        });
        this.printMenu.add(this.printToPrinter);
        this.targetMenu = new JMenu("Targets");
        (this.targetLoad = new JMenuItem("Load Targets")).addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                final JFileChooser fileChooser = new JFileChooser(ETLogGUI.this.defaultDir);
                if (fileChooser.showOpenDialog((Component)actionEvent.getSource()) == 0) {
                    final String absolutePath = fileChooser.getSelectedFile().getAbsolutePath();
                    ETLogGUI.this.defaultDir = fileChooser.getCurrentDirectory();
                    try {
                        final BufferedReader bufferedReader = new BufferedReader(new FileReader(absolutePath));
                        String line = " ";
                        while (line != null) {
                            line = bufferedReader.readLine();
                            if (line == null) {
                                break;
                            }
                            boolean b = true;
                            for (int i = 0; i < ETLogGUI.this.params.length; ++i) {
                                for (int j = 0; j < ETLogGUI.this.params[i].target.getItemCount(); ++j) {
                                    if (line.trim().equals(ETLogGUI.this.params[i].target.getItemAt(j))) {
                                        ETLogGUI.this.error("<html>Target: " + line.trim() + " already exists!</html>");
                                        b = false;
                                        break;
                                    }
                                }
                                if (!b) {
                                    break;
                                }
                                if (!line.trim().equals("")) {
                                    ETLogGUI.this.params[i].addTarget(line.trim());
                                }
                            }
                        }
                        bufferedReader.close();
                    }
                    catch (IOException ex) {
                        ETLogGUI.this.error("<html>Error reading file: " + absolutePath + "<br>" + ex.toString() + "</html>");
                    }
                }
            }
        });
        this.targetMenu.add(this.targetLoad);
        (this.targetSave = new JMenuItem("Save Targets")).addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                final JFileChooser fileChooser = new JFileChooser(ETLogGUI.this.defaultDir);
                if (fileChooser.showSaveDialog((Component)actionEvent.getSource()) == 0) {
                    final String absolutePath = fileChooser.getSelectedFile().getAbsolutePath();
                    boolean append = false;
                    final Vector<String> vector = new Vector<String>();
                    if (new File(absolutePath).exists()) {
                        final Object[] options = { "Overwrite", "Append", "Cancel" };
                        final int showOptionDialog = JOptionPane.showOptionDialog(ETLogGUI.this, absolutePath + " already exists.", "File exists!", 0, 2, null, options, options[0]);
                        if (showOptionDialog == 2) {
                            return;
                        }
                        if (showOptionDialog == 1) {
                            append = true;
                            try {
                                final BufferedReader bufferedReader = new BufferedReader(new FileReader(absolutePath));
                                String line = " ";
                                while (line != null) {
                                    line = bufferedReader.readLine();
                                    if (line == null) {
                                        break;
                                    }
                                    vector.add(line);
                                }
                            }
                            catch (IOException ex2) {}
                        }
                    }
                    ETLogGUI.this.defaultDir = fileChooser.getCurrentDirectory();
                    try {
                        final PrintWriter printWriter = new PrintWriter(new FileOutputStream(absolutePath, append));
                        for (int i = 2; i < ETLogGUI.this.params[0].target.getItemCount(); ++i) {
                            if (vector.indexOf(ETLogGUI.this.params[0].target.getItemAt(i)) == -1) {
                                printWriter.println(ETLogGUI.this.params[0].target.getItemAt(i));
                            }
                        }
                        printWriter.close();
                    }
                    catch (IOException ex) {
                        ETLogGUI.this.error("<html>Error writing to file: " + absolutePath + "<br>" + ex.toString() + "</html>");
                    }
                }
            }
        });
        this.targetMenu.add(this.targetSave);
        this.optMenu = new JMenu("Options");
        (this.optDateTime = new JMenuItem("Turn Off Auto Date/Time")).setBackground(Color.GREEN);
        this.optDateTime.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                if (ETLogGUI.this.autoDateTime) {
                    ETLogGUI.this.autoDateTime = false;
                    ETLogGUI.this.optDateTime.setText("Turn On Auto Date/Time");
                    ETLogGUI.this.optDateTime.setBackground(Color.RED);
                    ETLogGUI.this.tDate.setText("");
                    for (int i = 0; i < ETLogGUI.this.params.length; ++i) {
                        ETLogGUI.this.params[i].setAutoDateTime(false);
                    }
                }
                else {
                    ETLogGUI.this.autoDateTime = true;
                    ETLogGUI.this.optDateTime.setText("Turn Off Auto Date/Time");
                    ETLogGUI.this.optDateTime.setBackground(Color.GREEN);
                    ETLogGUI.this.tDate.setText(new Date().toString());
                    for (int j = 0; j < ETLogGUI.this.params.length; ++j) {
                        ETLogGUI.this.params[j].setAutoDateTime(true);
                    }
                }
            }
        });
        this.optMenu.add(this.optDateTime);
        this.helpMenu = new JMenu("Help");
        (this.helpAbout = new JMenuItem("About")).addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                final JFrame frame = new JFrame("About ET Log");
                try {
                    final JEditorPane view = new JEditorPane(ETLogGUI.class.getResource("about.html"));
                    view.setContentType("text/html;");
                    view.setEditable(false);
                    final JScrollPane comp = new JScrollPane(view);
                    comp.setPreferredSize(new Dimension(1024, 830));
                    frame.getContentPane().add(comp);
                    frame.pack();
                    frame.setVisible(true);
                }
                catch (IOException ex) {
                    ETLogGUI.this.error("<html>Could not find help file!</html>");
                }
            }
        });
        this.helpMenu.add(this.helpAbout);
        (this.helpKPNO = new JMenuItem("KPNO Help")).addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                final JFrame frame = new JFrame("ET Log KPNO Help");
                try {
                    final JEditorPane view = new JEditorPane(ETLogGUI.class.getResource("KPNOhelp.html"));
                    view.setContentType("text/html;");
                    view.setEditable(false);
                    final JScrollPane comp = new JScrollPane(view);
                    comp.setPreferredSize(new Dimension(1024, 830));
                    frame.getContentPane().add(comp);
                    frame.pack();
                    frame.setVisible(true);
                }
                catch (IOException ex) {
                    ETLogGUI.this.error("<html>Could not find help file!</html>");
                }
            }
        });
        this.helpMenu.add(this.helpKPNO);
        this.jmb.add(this.fileMenu);
        this.jmb.add(this.printMenu);
        this.jmb.add(this.targetMenu);
        this.jmb.add(this.optMenu);
        this.jmb.add(this.helpMenu);
        this.setJMenuBar(this.jmb);
        (this.topPanel = new JPanel()).setPreferredSize(new Dimension(this.xdim, 180));
        this.topPanel.setBackground(Color.white);
        this.topLayout = new SpringLayout();
        this.topPanel.setLayout(this.topLayout);
        (this.logLabel = new JLabel("PARAS Observation Log")).setFont(new Font("Dialog", 1, 24));
        this.logLabel.setForeground(Color.BLUE);
        this.topPanel.add(this.logLabel);
        this.topLayout.putConstraint("West", this.logLabel, 0, "West", this.topPanel);
        this.topLayout.putConstraint("North", this.logLabel, 0, "North", this.topPanel);
        (this.lTelescope = new JLabel("Telescope:")).setFont(this.labelFont);
        this.topPanel.add(this.lTelescope);
        this.topLayout.putConstraint("West", this.lTelescope, 0, "West", this.topPanel);
        this.topLayout.putConstraint("North", this.lTelescope, 10, "South", this.logLabel);
        this.tTelescope = new JTextField(25);
        this.topPanel.add(this.tTelescope);
        this.topLayout.putConstraint("West", this.tTelescope, 100, "West", this.topPanel);
        this.topLayout.putConstraint("North", this.tTelescope, 10, "South", this.logLabel);
        (this.lDate = new JLabel("Date:")).setFont(this.labelFont);
        this.topPanel.add(this.lDate);
        this.topLayout.putConstraint("West", this.lDate, 0, "West", this.topPanel);
        this.topLayout.putConstraint("North", this.lDate, 8, "South", this.lTelescope);
        (this.tDate = new JTextField(25)).setText(new Date().toString());
        this.topPanel.add(this.tDate);
        this.topLayout.putConstraint("West", this.tDate, 100, "West", this.topPanel);
        this.topLayout.putConstraint("North", this.tDate, 8, "South", this.lTelescope);
        (this.lObservers = new JLabel("Observers:")).setFont(this.labelFont);
        this.topPanel.add(this.lObservers);
        this.topLayout.putConstraint("West", this.lObservers, 0, "West", this.topPanel);
        this.topLayout.putConstraint("North", this.lObservers, 8, "South", this.lDate);
        this.tObservers = new JTextField(25);
        this.topPanel.add(this.tObservers);
        this.topLayout.putConstraint("West", this.tObservers, 100, "West", this.topPanel);
        this.topLayout.putConstraint("North", this.tObservers, 8, "South", this.lDate);
        (this.lWeather = new JLabel("Weather:")).setFont(this.labelFont);
        this.topPanel.add(this.lWeather);
        this.topLayout.putConstraint("West", this.lWeather, 0, "West", this.topPanel);
        this.topLayout.putConstraint("North", this.lWeather, 8, "South", this.lObservers);
        this.tWeather = new JTextArea(3, 25);
        final JScrollPane c1 = new JScrollPane(this.tWeather);
        this.topPanel.add(c1);
        this.topLayout.putConstraint("West", c1, 100, "West", this.topPanel);
        this.topLayout.putConstraint("North", c1, 8, "South", this.lObservers);
        (this.lRun = new JLabel("Run ID:")).setFont(this.labelFont);
        this.topPanel.add(this.lRun);
        this.topLayout.putConstraint("West", this.lRun, 425, "West", this.topPanel);
        this.topLayout.putConstraint("North", this.lRun, 10, "South", this.logLabel);
        this.tRun = new JTextField(25);
        this.topPanel.add(this.tRun);
        this.topLayout.putConstraint("West", this.tRun, 525, "West", this.topPanel);
        this.topLayout.putConstraint("North", this.tRun, 10, "South", this.logLabel);
        (this.lTopComments = new JLabel("Comments:")).setFont(this.labelFont);
        this.topPanel.add(this.lTopComments);
        this.topLayout.putConstraint("West", this.lTopComments, 425, "West", this.topPanel);
        this.topLayout.putConstraint("North", this.lTopComments, 8, "South", this.lRun);
        this.tTopComments = new JTextArea(7, 25);
        final JScrollPane c2 = new JScrollPane(this.tTopComments);
        this.topPanel.add(c2);
        this.topLayout.putConstraint("West", c2, 525, "West", this.topPanel);
        this.topLayout.putConstraint("North", c2, 8, "South", this.lRun);
        (this.lFile = new JLabel("<html>File: Not Saved<br>0 records</html>")).setFont(this.labelFont);
        this.topPanel.add(this.lFile);
        this.topLayout.putConstraint("West", this.lFile, 850, "West", this.topPanel);
        this.topLayout.putConstraint("North", this.lFile, 15, "South", this.logLabel);
        (this.bPrefix = new JButton("Add/Delete Prefix")).addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                final JFrame frame = new JFrame("Add/Delete Prefix");
                final Container contentPane = frame.getContentPane();
                final SpringLayout layout = new SpringLayout();
                contentPane.setLayout(layout);
                final JLabel c2 = new JLabel("New prefix:");
                final JTextField textField = new JTextField(14);
                final JButton button = new JButton("Add");
                final JButton c3 = new JButton("Cancel");
                contentPane.add(c2);
                layout.putConstraint("West", c2, 10, "West", contentPane);
                layout.putConstraint("North", c2, 10, "North", contentPane);
                contentPane.add(textField);
                layout.putConstraint("West", textField, 10, "East", c2);
                layout.putConstraint("North", textField, 10, "North", contentPane);
                contentPane.add(button);
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(final ActionEvent actionEvent) {
                        final String text = textField.getText();
                        if (text.indexOf(";") != -1) {
                            JOptionPane.showMessageDialog(null, "<html>Prefixes with a ; are not compatible with the pipeline.  Suggest changing.</html>", "Warning", 2);
                        }
                        for (int i = 0; i < ETLogGUI.this.params.length; ++i) {
                            for (int j = 0; j < ETLogGUI.this.params[i].prefix.getItemCount(); ++j) {
                                if (text.equals(ETLogGUI.this.params[i].prefix.getItemAt(j))) {
                                    ETLogGUI.this.error("<html>Prefix: " + text + " already exists!</html>");
                                    frame.dispose();
                                    return;
                                }
                            }
                        }
                        for (int k = 0; k < ETLogGUI.this.params.length; ++k) {
                            ETLogGUI.this.params[k].addPrefix(textField.getText());
                        }
                        frame.dispose();
                    }
                });
                layout.putConstraint("West", button, 10, "East", c2);
                layout.putConstraint("North", button, 15, "South", textField);
                contentPane.add(c3);
                c3.addActionListener(new ActionListener() {
                    public void actionPerformed(final ActionEvent actionEvent) {
                        frame.dispose();
                    }
                });
                layout.putConstraint("West", c3, 10, "East", button);
                layout.putConstraint("North", c3, 15, "South", textField);
                final JLabel c4 = new JLabel("Delete prefix:");
                final int itemCount = ETLogGUI.this.params[0].prefix.getItemCount();
                final String[] items = new String[itemCount];
                for (int i = 0; i < itemCount; ++i) {
                    items[i] = (String)ETLogGUI.this.params[0].prefix.getItemAt(i);
                }
                final JComboBox c5 = new JComboBox(items);
                final JButton button2 = new JButton("Delete");
                final JButton button3 = new JButton("Cancel");
                contentPane.add(c4);
                layout.putConstraint("West", c4, 10, "West", contentPane);
                layout.putConstraint("North", c4, 80, "South", ETLogGUI.this.bPrefix);
                contentPane.add(c5);
                layout.putConstraint("West", c5, 10, "East", c4);
                layout.putConstraint("North", c5, 80, "South", ETLogGUI.this.bPrefix);
                contentPane.add(button2);
                button2.addActionListener(new ActionListener() {
                    public void actionPerformed(final ActionEvent actionEvent) {
                        final String str = (String)c5.getSelectedItem();
                        for (int i = 0; i < ETLogGUI.this.params.length; ++i) {
                            if (ETLogGUI.this.params[i].isSet && str.equals(ETLogGUI.this.params[i].prefix.getSelectedItem())) {
                                ETLogGUI.this.error("<html>Error deleting prefix: " + str + "<br>Prefix in use at line " + (i + 1) + "</html>");
                                frame.dispose();
                                return;
                            }
                        }
                        for (int j = 0; j < ETLogGUI.this.params.length; ++j) {
                            ETLogGUI.this.params[j].removePrefix((String)c5.getSelectedItem());
                        }
                        frame.dispose();
                    }
                });
                layout.putConstraint("West", button2, 10, "East", c4);
                layout.putConstraint("North", button2, 15, "South", c5);
                contentPane.add(button3);
                button3.addActionListener(new ActionListener() {
                    public void actionPerformed(final ActionEvent actionEvent) {
                        frame.dispose();
                    }
                });
                layout.putConstraint("West", button3, 10, "East", button2);
                layout.putConstraint("North", button3, 15, "South", c5);
                layout.putConstraint("East", contentPane, 30, "East", textField);
                layout.putConstraint("South", contentPane, 10, "South", button3);
                frame.pack();
                frame.setVisible(true);
            }
        });
        this.topPanel.add(this.bPrefix);
        this.topLayout.putConstraint("West", this.bPrefix, 850, "West", this.topPanel);
        this.topLayout.putConstraint("North", this.bPrefix, 15, "South", this.lFile);
        (this.bTarget = new JButton("Add/Delete Target(s)")).addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                final JFrame frame = new JFrame("Add/Delete Target(s)");
                final Container contentPane = frame.getContentPane();
                final SpringLayout layout = new SpringLayout();
                contentPane.setLayout(layout);
                final JLabel c2 = new JLabel("Target:");
                final JTextArea view = new JTextArea(5, 25);
                final JScrollPane c3 = new JScrollPane(view);
                final JButton button = new JButton("Add");
                final JButton button2 = new JButton("Add from File");
                final JButton c4 = new JButton("Cancel");
                contentPane.add(c2);
                layout.putConstraint("West", c2, 10, "West", contentPane);
                layout.putConstraint("North", c2, 10, "North", contentPane);
                contentPane.add(c3);
                layout.putConstraint("West", c3, 10, "East", c2);
                layout.putConstraint("North", c3, 10, "North", contentPane);
                contentPane.add(button);
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(final ActionEvent actionEvent) {
                        final String[] split = view.getText().split("\n");
                        for (int i = 0; i < split.length; ++i) {
                            if (split[i].indexOf(";") != -1) {
                                JOptionPane.showMessageDialog(null, "<html>Target names with a ; are not compatible with the pipeline.  Suggest changing.</html>", "Warning", 2);
                            }
                            boolean b = true;
                            for (int j = 0; j < ETLogGUI.this.params.length; ++j) {
                                for (int k = 0; k < ETLogGUI.this.params[j].target.getItemCount(); ++k) {
                                    if (split[i].equals(ETLogGUI.this.params[j].target.getItemAt(k))) {
                                        ETLogGUI.this.error("<html>Target: " + split[i] + " already exists!</html>");
                                        frame.dispose();
                                        b = false;
                                        break;
                                    }
                                }
                                if (!b) {
                                    break;
                                }
                            }
                            if (b) {
                                for (int l = 0; l < ETLogGUI.this.params.length; ++l) {
                                    if (!split[i].trim().equals("")) {
                                        ETLogGUI.this.params[l].addTarget(split[i].trim());
                                    }
                                }
                            }
                        }
                        frame.dispose();
                    }
                });
                layout.putConstraint("West", button, 10, "East", c2);
                layout.putConstraint("North", button, 15, "South", c3);
                contentPane.add(button2);
                button2.addActionListener(new ActionListener() {
                    public void actionPerformed(final ActionEvent actionEvent) {
                        final JFileChooser fileChooser = new JFileChooser(ETLogGUI.this.defaultDir);
                        if (fileChooser.showOpenDialog((Component)actionEvent.getSource()) == 0) {
                            final String absolutePath = fileChooser.getSelectedFile().getAbsolutePath();
                            ETLogGUI.this.defaultDir = fileChooser.getCurrentDirectory();
                            try {
                                final BufferedReader bufferedReader = new BufferedReader(new FileReader(absolutePath));
                                String line = " ";
                                while (line != null) {
                                    line = bufferedReader.readLine();
                                    if (line == null) {
                                        break;
                                    }
                                    boolean b = true;
                                    for (int i = 0; i < ETLogGUI.this.params.length; ++i) {
                                        for (int j = 0; j < ETLogGUI.this.params[i].target.getItemCount(); ++j) {
                                            if (line.trim().equals(ETLogGUI.this.params[i].target.getItemAt(j))) {
                                                ETLogGUI.this.error("<html>Target: " + line.trim() + " already exists!</html>");
                                                frame.dispose();
                                                b = false;
                                                break;
                                            }
                                        }
                                        if (!b) {
                                            break;
                                        }
                                        if (!line.trim().equals("")) {
                                            ETLogGUI.this.params[i].addTarget(line.trim());
                                        }
                                    }
                                }
                                bufferedReader.close();
                            }
                            catch (IOException ex) {
                                ETLogGUI.this.error("<html>Error reading file: " + absolutePath + "<br>" + ex.toString() + "</html>");
                            }
                        }
                        frame.dispose();
                    }
                });
                layout.putConstraint("West", button2, 10, "East", button);
                layout.putConstraint("North", button2, 15, "South", c3);
                contentPane.add(c4);
                c4.addActionListener(new ActionListener() {
                    public void actionPerformed(final ActionEvent actionEvent) {
                        frame.dispose();
                    }
                });
                layout.putConstraint("West", c4, 10, "East", button2);
                layout.putConstraint("North", c4, 15, "South", c3);
                final JLabel c5 = new JLabel("Delete Target:");
                final int itemCount = ETLogGUI.this.params[0].target.getItemCount();
                final String[] listData = new String[itemCount];
                for (int i = 2; i < itemCount; ++i) {
                    listData[i] = (String)ETLogGUI.this.params[0].target.getItemAt(i);
                }
                final JList list = new JList(listData);
                list.setBorder(new BevelBorder(1));
                final JScrollPane scrollPane = new JScrollPane(list);
                final JButton button3 = new JButton("Delete");
                final JButton button4 = new JButton("Cancel");
                contentPane.add(c5);
                layout.putConstraint("West", c5, 10, "West", contentPane);
                layout.putConstraint("North", c5, 80, "South", button);
                contentPane.add(list);
                layout.putConstraint("West", list, 10, "East", c5);
                layout.putConstraint("North", list, 80, "South", button);
                contentPane.add(button3);
                button3.addActionListener(new ActionListener() {
                    public void actionPerformed(final ActionEvent actionEvent) {
                        final Object[] selectedValues = list.getSelectedValues();
                        final String[] array = new String[selectedValues.length];
                        for (int i = 0; i < selectedValues.length; ++i) {
                            array[i] = (String)selectedValues[i];
                        }
                        for (int j = 0; j < array.length; ++j) {
                            boolean b = false;
                            for (int k = 0; k < ETLogGUI.this.params.length; ++k) {
                                if (ETLogGUI.this.params[k].isSet && array[j].equals(ETLogGUI.this.params[k].target.getSelectedItem())) {
                                    ETLogGUI.this.error("<html>Error deleting target: " + array[j] + "<br>Target in use at line " + (k + 1) + "</html>");
                                    b = true;
                                }
                            }
                            if (!b) {
                                for (int l = 0; l < ETLogGUI.this.params.length; ++l) {
                                    ETLogGUI.this.params[l].removeTarget(array[j]);
                                }
                            }
                        }
                        frame.dispose();
                    }
                });
                layout.putConstraint("West", button3, 10, "East", c5);
                layout.putConstraint("North", button3, 15, "South", list);
                contentPane.add(button4);
                button4.addActionListener(new ActionListener() {
                    public void actionPerformed(final ActionEvent actionEvent) {
                        frame.dispose();
                    }
                });
                layout.putConstraint("West", button4, 10, "East", button3);
                layout.putConstraint("North", button4, 15, "South", list);
                layout.putConstraint("East", contentPane, 30, "East", c3);
                layout.putConstraint("South", contentPane, 10, "South", button4);
                frame.pack();
                frame.setVisible(true);
            }
        });
        this.topPanel.add(this.bTarget);
        this.topLayout.putConstraint("West", this.bTarget, 15, "East", this.bPrefix);
        this.topLayout.putConstraint("North", this.bTarget, 15, "South", this.lFile);
        (this.bAddLines = new JButton("Add 25 Lines")).addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                final ETLogParam[] params = new ETLogParam[ETLogGUI.this.size + 25];
                for (int i = 0; i < ETLogGUI.this.size; ++i) {
                    params[i] = ETLogGUI.this.params[i];
                }
                ETLogGUI.this.params = params;
                for (int j = ETLogGUI.this.size; j < ETLogGUI.this.size + 25; ++j) {
                    (ETLogGUI.this.params[j] = new ETLogParam(ETLogGUI.clipboard)).addTo(ETLogGUI.this.bottomPanel);
                    for (int k = 1; k < ETLogGUI.this.params[0].prefix.getItemCount(); ++k) {
                        ETLogGUI.this.params[j].prefix.addItem(ETLogGUI.this.params[0].prefix.getItemAt(k));
                        ETLogGUI.this.params[j].prefix.setSelectedIndex(k);
                    }
                    for (int l = 2; l < ETLogGUI.this.params[0].target.getItemCount(); ++l) {
                        ETLogGUI.this.params[j].target.addItem(ETLogGUI.this.params[0].target.getItemAt(l));
                    }
                    ETLogGUI.this.params[j].addOKAction(ETLogGUI.this);
                    ETLogGUI.this.params[j].setConstraints(ETLogGUI.this.bottomLayout, 5, 10, ETLogGUI.this.bottomPanel, ETLogGUI.this.params[j - 1], false);
                }
                final ETLogGUI this$0 = ETLogGUI.this;
                this$0.size += 25;
                ETLogGUI.this.bottomLayout.putConstraint("East", ETLogGUI.this.bottomPanel, 1160, "West", ETLogGUI.this.bottomPanel);
                ETLogGUI.this.bottomLayout.putConstraint("South", ETLogGUI.this.bottomPanel, 5, "South", ETLogGUI.this.params[ETLogGUI.this.size - 1]);
                ETLogGUI.this.bottomPanel.validate();
                ETLogGUI.this.bottomScrollPane.validate();
            }
        });
        this.topPanel.add(this.bAddLines);
        this.topLayout.putConstraint("West", this.bAddLines, 850, "West", this.topPanel);
        this.topLayout.putConstraint("North", this.bAddLines, 15, "South", this.bPrefix);
        this.topLayout.putConstraint("South", this.topPanel, 50, "South", this.lWeather);
        (this.topScrollPane = new JScrollPane(this.topPanel)).setMinimumSize(new Dimension(500, 190));
        this.bottomPanel = new JPanel();
        this.bottomLayout = new SpringLayout();
        this.bottomPanel.setLayout(this.bottomLayout);
        this.params = new ETLogParam[100];
        for (int i = 0; i < this.size; ++i) {
            (this.params[i] = new ETLogParam(ETLogGUI.clipboard)).addTo(this.bottomPanel);
            this.params[i].addOKAction(this);
            if (i == 0) {
                this.params[0].setConstraints(this.bottomLayout, 15, 10, this.bottomPanel, this.bottomPanel, true);
            }
            else {
                this.params[i].setConstraints(this.bottomLayout, 5, 10, this.bottomPanel, this.params[i - 1], false);
            }
        }
        this.params[0].addHeaders();
        this.bottomLayout.putConstraint("East", this.bottomPanel, 1160, "West", this.bottomPanel);
        this.bottomLayout.putConstraint("South", this.bottomPanel, 5, "South", this.params[this.size - 1]);
        (this.bottomScrollPane = new JScrollPane(this.bottomPanel)).setPreferredSize(new Dimension(this.xdim, this.ydim - 180));
        contentPane.add(this.topScrollPane, 0);
        contentPane.add(this.bottomScrollPane, 1);
        this.pack();
        this.setVisible(true);
    }
    
    public void saveLog(final String s) {
        int i = 0;
        try {
            final PrintWriter printWriter = new PrintWriter(new FileOutputStream(s + ".dat"));
            printWriter.println("#ET Observation Log");
            printWriter.println("#Telescope: " + this.tTelescope.getText());
            printWriter.println("#Run ID: " + this.tRun.getText());
            printWriter.println("#Date: " + this.tDate.getText());
            printWriter.println("#Observers: " + this.tObservers.getText());
            final String[] split = this.tWeather.getText().split("\n");
            for (int j = 0; j < split.length; ++j) {
                printWriter.println("#Weather: " + split[j]);
            }
            final String[] split2 = this.tTopComments.getText().split("\n");
            if (split2.length == 0) {
                printWriter.println("#Comments: ");
            }
            for (int k = 0; k < split2.length; ++k) {
                printWriter.println("#Comments: " + split2[k]);
            }
            for (int l = 0; l < this.params.length; ++l) {
                if (this.params[l].isSet) {
                    printWriter.println(this.params[l].getParamValue());
                }
            }
            printWriter.close();
            for (int n = 0; n < this.params.length; ++n) {
                if (this.params[n].isSet) {
                    this.params[n].setState(ETLogParam.IS_SAVED);
                    ++i;
                }
            }
        }
        catch (IOException ex) {
            this.error("<html>Error writing file: " + s.substring(s.lastIndexOf("/") + 1) + ".dat<br>" + ex.toString() + "</html>");
        }
        try {
            final PrintWriter printWriter2 = new PrintWriter(new FileOutputStream(s + ".csv"));
            printWriter2.println("#ET Observation Log");
            printWriter2.println("#Date: " + this.tDate.getText());
            printWriter2.println("#Telescope: " + this.tTelescope.getText());
            printWriter2.println("#Run ID: " + this.tRun.getText());
            printWriter2.println("#Observers: " + this.tObservers.getText());
            final String[] split3 = this.tWeather.getText().split("\n");
            for (int n2 = 0; n2 < split3.length; ++n2) {
                printWriter2.println("#Weather: " + split3[n2]);
            }
            final String[] split4 = this.tTopComments.getText().split("\n");
            if (split4.length == 0) {
                printWriter2.println("#Comments: ");
            }
            for (int n3 = 0; n3 < split4.length; ++n3) {
                printWriter2.println("#Comments: " + split4[n3]);
            }
            for (int n4 = 0; n4 < this.params.length; ++n4) {
                if (this.params[n4].isSet) {
                    final String[] paramCSV = this.params[n4].getParamCSV();
                    for (int n5 = 0; n5 < paramCSV.length; ++n5) {
                        printWriter2.println(paramCSV[n5]);
                    }
                }
            }
            printWriter2.close();
        }
        catch (IOException ex2) {
            this.error("<html>Error writing file: " + s.substring(s.lastIndexOf("/") + 1) + ".csv<br>" + ex2.toString() + "</html>");
        }
        this.lFile.setText("<html>File: " + s.substring(s.lastIndexOf("/") + 1) + "<br>" + i + " records</html>");
    }
    
    public void printLog(final String s) {
        try {
            final PrintWriter printWriter = new PrintWriter(new FileOutputStream(s));
            printWriter.println("#ET Observation Log");
            printWriter.println("#Date: " + this.tDate.getText());
            printWriter.println("#Telescope: " + this.tTelescope.getText());
            printWriter.println("#Run ID: " + this.tRun.getText());
            printWriter.println("#Observers: " + this.tObservers.getText());
            final String[] split = this.tWeather.getText().split("\n");
            for (int i = 0; i < split.length; ++i) {
                printWriter.println("#Weather: " + split[i]);
            }
            final String[] split2 = this.tTopComments.getText().split("\n");
            if (split2.length == 0) {
                printWriter.println("#Comments: ");
            }
            for (int j = 0; j < split2.length; ++j) {
                printWriter.println("#Comments: " + split2[j]);
            }
            int max = 4;
            int max2 = 4;
            int max3 = 3;
            int max4 = 6;
            int max5 = 6;
            for (int k = 0; k < this.params.length; ++k) {
                if (this.params[k].isSet) {
                    final String[] paramForPrint = this.params[k].getParamForPrint();
                    if (paramForPrint.length != 1) {
                        max = Math.max(max, paramForPrint[0].length());
                        max2 = Math.max(max2, paramForPrint[3].length());
                        max3 = Math.max(max3, paramForPrint[4].length());
                        max4 = Math.max(max4, paramForPrint[5].length());
                        max5 = Math.max(max5, paramForPrint[6].length());
                    }
                }
            }
            final int n = 2;
            String string = "Pfix";
            for (int l = 4; l < max + n; ++l) {
                string += " ";
            }
            String s2 = string + "Files    ";
            for (int n2 = 0; n2 < n; ++n2) {
                s2 += " ";
            }
            String s3 = s2 + "Time";
            for (int n3 = 4; n3 < max2 + n; ++n3) {
                s3 += " ";
            }
            String s4 = s3 + "Exp";
            for (int n4 = 3; n4 < max3 + n; ++n4) {
                s4 += " ";
            }
            String s5 = s4 + "Source";
            for (int n5 = 6; n5 < max4 + n; ++n5) {
                s5 += " ";
            }
            String s6 = s5 + "Target";
            for (int n6 = 6; n6 < max5 + n; ++n6) {
                s6 += " ";
            }
            String s7 = s6 + "Iod";
            for (int n7 = 0; n7 < n; ++n7) {
                s7 += " ";
            }
            String s8 = s7 + "Fng";
            for (int n8 = 0; n8 < n; ++n8) {
                s8 += " ";
            }
            printWriter.println(s8 + "Comments");
            for (int n9 = 0; n9 < this.params.length; ++n9) {
                if (this.params[n9].isSet) {
                    final String[] paramForPrint2 = this.params[n9].getParamForPrint();
                    String x;
                    if (paramForPrint2.length == 1) {
                        x = "---" + paramForPrint2[0] + "---";
                    }
                    else {
                        String string2 = paramForPrint2[0];
                        for (int length = paramForPrint2[0].length(); length < max + n; ++length) {
                            string2 += " ";
                        }
                        String s9 = string2 + paramForPrint2[1] + "-" + paramForPrint2[2];
                        for (int n10 = 0; n10 < n; ++n10) {
                            s9 += " ";
                        }
                        String s10 = s9 + paramForPrint2[3];
                        for (int length2 = paramForPrint2[3].length(); length2 < max2 + n; ++length2) {
                            s10 += " ";
                        }
                        String s11 = s10 + paramForPrint2[4];
                        for (int length3 = paramForPrint2[4].length(); length3 < max3 + n; ++length3) {
                            s11 += " ";
                        }
                        String s12 = s11 + paramForPrint2[5];
                        for (int length4 = paramForPrint2[5].length(); length4 < max4 + n; ++length4) {
                            s12 += " ";
                        }
                        String s13 = s12 + paramForPrint2[6];
                        for (int length5 = paramForPrint2[6].length(); length5 < max5 + n; ++length5) {
                            s13 += " ";
                        }
                        String s14 = s13 + " " + paramForPrint2[7] + " ";
                        for (int n11 = 0; n11 < n; ++n11) {
                            s14 += " ";
                        }
                        String s15 = s14 + " " + paramForPrint2[8] + " ";
                        for (int n12 = 0; n12 < n; ++n12) {
                            s15 += " ";
                        }
                        x = s15 + paramForPrint2[9].replaceAll("\n", "\n\t\t\t\t\t\t\t\t\t");
                    }
                    printWriter.println(x);
                }
            }
            printWriter.close();
        }
        catch (IOException ex) {
            this.error("<html>Error writing file: " + s + "<br>" + ex.toString() + "</html>");
        }
    }
    
    public void loadLog(final String s) {
        final boolean endsWith = s.endsWith(".csv");
        int n = 0;
        for (int i = 0; i < this.params.length; ++i) {
            this.params[i].reset();
        }
        try {
            final BufferedReader bufferedReader = new BufferedReader(new FileReader(s));
            String line = " ";
            int n2 = 1;
            int n3 = 1;
            this.tDate.setText("");
            this.tObservers.setText("");
            this.tTelescope.setText("");
            this.tRun.setText("");
            this.tWeather.setText("");
            this.tTopComments.setText("");
            while (line != null) {
                if (n >= this.params.length) {
                    final ETLogParam[] params = new ETLogParam[this.size + 25];
                    for (int j = 0; j < this.size; ++j) {
                        params[j] = this.params[j];
                    }
                    this.params = params;
                    for (int k = this.size; k < this.size + 25; ++k) {
                        (this.params[k] = new ETLogParam(ETLogGUI.clipboard)).addTo(this.bottomPanel);
                        for (int l = 1; l < this.params[0].prefix.getItemCount(); ++l) {
                            this.params[k].prefix.addItem(this.params[0].prefix.getItemAt(l));
                            this.params[k].prefix.setSelectedIndex(l);
                        }
                        for (int index = 2; index < this.params[0].target.getItemCount(); ++index) {
                            this.params[k].target.addItem(this.params[0].target.getItemAt(index));
                        }
                        this.params[k].addOKAction(this);
                        this.params[k].setConstraints(this.bottomLayout, 5, 10, this.bottomPanel, this.params[k - 1], false);
                    }
                    this.size += 25;
                    this.bottomLayout.putConstraint("East", this.bottomPanel, 1160, "West", this.bottomPanel);
                    this.bottomLayout.putConstraint("South", this.bottomPanel, 5, "South", this.params[this.size - 1]);
                    this.bottomPanel.validate();
                    this.bottomScrollPane.validate();
                }
                line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                if (line.startsWith("#Date: ")) {
                    this.tDate.setText(line.substring(7));
                }
                else if (line.startsWith("#Observers: ")) {
                    this.tObservers.setText(line.substring(12));
                }
                else if (line.startsWith("#Telescope: ")) {
                    this.tTelescope.setText(line.substring(12));
                }
                else if (line.startsWith("#Run ID: ")) {
                    this.tRun.setText(line.substring(9));
                }
                else if (line.startsWith("#Weather: ")) {
                    if (n3 != 0) {
                        this.tWeather.setText(line.substring(10) + "\n");
                        n3 = 0;
                    }
                    else {
                        this.tWeather.append(line.substring(10) + "\n");
                    }
                }
                else if (line.startsWith("#Comments: ")) {
                    if (n2 != 0) {
                        this.tTopComments.setText(line.substring(11) + "\n");
                        n2 = 0;
                    }
                    else {
                        this.tTopComments.append(line.substring(11) + "\n");
                    }
                }
                else {
                    if (line.startsWith("#")) {
                        continue;
                    }
                    if (endsWith) {
                        final String[] split = line.split(",");
                        this.params[n].setParamCSV(line);
                        if (line.startsWith("COMMENT: ")) {
                            this.params[n].setState(ETLogParam.IS_SAVED);
                            ++n;
                            continue;
                        }
                        if (!this.params[n].prefix.getSelectedItem().equals(split[0].substring(0, split[0].length() - 4))) {
                            for (int n4 = 0; n4 < this.params.length; ++n4) {
                                this.params[n4].addPrefix(split[0].substring(0, split[0].length() - 4));
                            }
                        }
                        if (!this.params[n].target.getSelectedItem().equals(split[4])) {
                            for (int n5 = 0; n5 < this.params.length; ++n5) {
                                this.params[n5].addTarget(split[4]);
                            }
                            this.params[n].setParamCSV(line);
                        }
                        this.params[n].setState(ETLogParam.IS_SAVED);
                    }
                    else {
                        final String[] split2 = line.split("\t");
                        this.params[n].setParamValue(line);
                        if (!this.params[n].prefix.getSelectedItem().equals(split2[0])) {
                            for (int n6 = 0; n6 < this.params.length; ++n6) {
                                this.params[n6].addPrefix(split2[0]);
                            }
                        }
                        if (!this.params[n].target.getSelectedItem().equals(split2[6])) {
                            for (int n7 = 0; n7 < this.params.length; ++n7) {
                                this.params[n7].addTarget(split2[6]);
                            }
                            this.params[n].setParamValue(line);
                        }
                        this.params[n].setState(ETLogParam.IS_SAVED);
                    }
                    ++n;
                }
            }
            bufferedReader.close();
        }
        catch (IOException ex) {
            this.error("<html>Error reading file: " + s + "<br>" + ex.toString() + "</html>");
        }
        this.lFile.setText("<html>File: " + s.substring(s.lastIndexOf("/") + 1) + "<br>" + n + " records</html>");
        this.size = n;
    }
    
    public void error(final String message) {
        JOptionPane.showMessageDialog(null, message, "Error", 0);
    }
    
    @Override
    protected void processWindowEvent(final WindowEvent e) {
        if (e.getID() == 201) {
            final Object[] options = { "Exit", "Save and Exit", "Cancel" };
            final int showOptionDialog = JOptionPane.showOptionDialog(this, "Are you sure you want to quit?", "Exit ET Log?", 0, 2, null, options, options[1]);
            if (showOptionDialog == 0) {
                System.exit(0);
            }
            else if (showOptionDialog == 1) {
                this.fileSave.doClick();
                JOptionPane.showMessageDialog(this, "File " + this.filename + " has been saved.", "File Saved", 1);
                System.exit(0);
            }
        }
        else {
            super.processWindowEvent(e);
        }
    }
    
    public static void main(final String[] array) {
        final ETLogGUI etLogGUI = new ETLogGUI();
    }
    
    static {
        ETLogGUI.clipboard = "";
    }
}
