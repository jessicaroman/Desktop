/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktop;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author jessi
 */
public class JFrameMain extends javax.swing.JFrame {

    ClassMain cm = new ClassMain();
    Conexion cx = new Conexion();
    String[] conexion = null;
    String[] config = null;
    String[] filters = null;
    String[] parklots = null;
    String[] id_parklots = null;
    String[] namemixs = null;
    DefaultListModel<String> mix = new DefaultListModel<String>();
    DefaultListModel<String> model = new DefaultListModel<String>();
    String beforeNameFilter = "";
    String URL = "";
    String dir = "";
    int Threads = 0;
    int State = 0;

    /**
     * Creates new form Conect
     */
    public JFrameMain() {
        initComponents();
        this.setLocationRelativeTo(null);
        try {
            conexion = cm.getConexion();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void Init() {

        try {

            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));

            String ip = in.readLine(); //you get the IP as a String
            this.Conexion_MyIP.setText(ip);
            this.Conexion_MyIP2.setText(ip);

            String passRemote = (conexion[5].equals("NONE")) ? "" : conexion[5];
            String passLocal = (conexion[11].equals("NONE")) ? "" : conexion[11];
            this.Remote_IP.setText(conexion[1]);
            this.Remote_Puerto.setText(conexion[2]);
            this.Remote_Name.setText(conexion[3]);
            this.Remote_User.setText(conexion[4]);
            this.Remote_Pass.setText(passRemote);
            this.Local_IP.setText(conexion[7]);
            this.Local_Puerto.setText(conexion[8]);
            this.Local_Name.setText(conexion[9]);
            this.Local_User.setText(conexion[10]);
            this.Local_Pass.setText(passLocal);

        } catch (UnknownHostException ex) {
            Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            String sql = "SELECT `id_Parklot`,`name_Parklot` FROM `parklot`";
            int n = 2;
            String[] temp = cx.select(sql, n, 1);
            parklots = new String[temp.length];
            id_parklots = new String[temp.length];
            this.CreateModel_Parkings.removeAllItems();
            this.Classifiers_Parkings.removeAllItems();

            for (int i = 0; i < temp.length; i++) {
                String[] substring = temp[i].split(" columns ");
                id_parklots[i] = substring[0];
                parklots[i] = substring[1];
                this.CreateModel_Parkings.addItem(parklots[i]);
                this.Classifiers_Parkings.addItem(parklots[i]);
            }

        } catch (SQLException ex) {
            Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            String sql = "SELECT `url`,`path` ,`threads` FROM `settings`";
            int n = 3;
            String[] temp = cx.select(sql, n, 2);
            String[] substring = null;
            for (int i = 0; i < temp.length; i++) {
                substring = temp[i].split(" columns ");
            }
            URL = substring[0];
            dir = substring[1];
            Threads = Integer.valueOf(substring[2]);
            this.Settings_Url.setText(substring[0]);
            this.Settings_PathImagesTemp.setText(substring[1]);
            this.Settings_Threads.setSelectedItem(substring[2]);
        } catch (SQLException ex) {
            Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            //Obtiene tamaño para las imagenes del ARFF
            String sql = "SELECT * FROM `settings_arff` WHERE `id`=1";
            int n = 5;
            String[] temp = cx.select(sql, n, 2);

            String[] substring = temp[0].split(" columns ");
            this.SettingsARFF_Width.setText(substring[1]);
            this.SettingsARFF_Height.setText(substring[2]);

            sql = "SELECT * FROM `settings_arff` WHERE `id`=2";
            n = 5;
            temp = cx.select(sql, n, 2);

            substring = temp[0].split(" columns ");

            this.ClassifiersSettings_Width.setText(substring[1]);
            this.ClassifiersSettings_Height.setText(substring[2]);

            String m = substring[3];
            String arff = substring[4];

            this.ClassifiersSettings_PathModel.setText(m.replace("/", "\\"));
            this.ClassifiersSettings_PathArff.setText(arff.replace("/", "\\"));

        } catch (SQLException ex) {
            Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            String sql = "SELECT `name` FROM `filters`";
            int n = 1;
            String[] temp = cx.select(sql, n, 2);
            filters = new String[(temp.length) + 1];
            this.EditFilters_JComboFilters.removeAllItems();
            this.CreateFilters_JComboFilters.removeAllItems();
            filters[0] = "";
            this.EditFilters_JComboFilters.addItem(filters[0]);
            this.CreateFilters_JComboFilters.addItem(filters[0]);
            for (int i = 0; i < temp.length; i++) {
                String[] substring = temp[i].split(" columns ");
                filters[i + 1] = substring[0];
                this.EditFilters_JComboFilters.addItem(filters[i + 1]);
                this.CreateFilters_JComboFilters.addItem(filters[i + 1]);
            }
            sql = "SELECT `name` FROM `filters` WHERE `use_gui`='false'";
            n = 1;
            temp = cx.select(sql, n, 2);
            filters = new String[(temp.length) + 1];
            this.CreateMixFilters_ListFilters.removeAll();
            for (int i = 0; i < temp.length; i++) {
                String[] substring = temp[i].split(" columns ");
                filters[i] = substring[0];
            }
            this.CreateMixFilters_ListFilters.setListData(filters);
            this.EditMixFilters_Filters.setListData(filters);

            sql = "SELECT `name` FROM `mix`";
            n = 1;
            temp = cx.select(sql, n, 2);
            namemixs = new String[(temp.length) + 1];
            this.EditMixFilters_ListMix.removeAll();
            for (int i = 0; i < temp.length; i++) {
                String[] substring = temp[i].split(" columns ");
                namemixs[i] = substring[0];
            }
            this.EditMixFilters_ListMix.setListData(namemixs);

        } catch (SQLException ex) {
            Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        TabClassifiers = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        Classifiers_Start = new javax.swing.JButton();
        jLabel53 = new javax.swing.JLabel();
        Classifiers_Parkings = new javax.swing.JComboBox<>();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        Classifiers_Stop = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        ClassifiersSettings_Save = new javax.swing.JButton();
        jLabel54 = new javax.swing.JLabel();
        ClassifiersSettings_Width = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        ClassifiersSettings_Height = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel14 = new javax.swing.JLabel();
        ClassifiersSettings_PathModel = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        ClassifiersSettings_PathArff = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        TabModelARFF = new javax.swing.JTabbedPane();
        TabCreateModel = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        CreateModel_Start = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        CreateModel_Parkings = new javax.swing.JComboBox<>();
        jLabel52 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        SettingsARFF_Save = new javax.swing.JButton();
        jLabel47 = new javax.swing.JLabel();
        SettingsARFF_Width = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        SettingsARFF_Height = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        TabFilters = new javax.swing.JTabbedPane();
        TabCreateFilter = new javax.swing.JPanel();
        btnSaveFilters = new javax.swing.JButton();
        CreateFilter_UseGui = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        CreateFilter_Name = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        CreateFilter_Init = new javax.swing.JTextArea();
        jScrollPane8 = new javax.swing.JScrollPane();
        CreateFilter_Gui = new javax.swing.JTextArea();
        jLabel32 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        CreateFilter_Apply = new javax.swing.JTextArea();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        CreateFilter_Generate = new javax.swing.JButton();
        jScrollPane18 = new javax.swing.JScrollPane();
        CreateFilter_Code = new javax.swing.JTextArea();
        CreateFilters_Copy = new javax.swing.JCheckBox();
        CreateFilters_JComboFilters = new javax.swing.JComboBox<>();
        TabEditFilter = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        EditFilters_JComboFilters = new javax.swing.JComboBox<>();
        jLabel41 = new javax.swing.JLabel();
        EditFilter_UseGui = new javax.swing.JComboBox<>();
        jLabel42 = new javax.swing.JLabel();
        jScrollPane14 = new javax.swing.JScrollPane();
        EditFilter_Init = new javax.swing.JTextArea();
        jLabel43 = new javax.swing.JLabel();
        jScrollPane15 = new javax.swing.JScrollPane();
        EditFilter_Gui = new javax.swing.JTextArea();
        jLabel44 = new javax.swing.JLabel();
        jScrollPane16 = new javax.swing.JScrollPane();
        EditFilter_Apply = new javax.swing.JTextArea();
        jLabel45 = new javax.swing.JLabel();
        UpdateFilters_Simulation = new javax.swing.JButton();
        UpdateFilters_Save = new javax.swing.JButton();
        jScrollPane19 = new javax.swing.JScrollPane();
        EditFilter_Code = new javax.swing.JTextArea();
        TabMixFilter = new javax.swing.JPanel();
        TabCreateMix = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        CreateMixFilters_ListFilters = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        CreateMixFilters_ListMix = new javax.swing.JList<>();
        CreateMixFilters_Name = new javax.swing.JTextField();
        CreateMixFilters_Save = new javax.swing.JButton();
        jLabel36 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        EditMixFilters_ListMix = new javax.swing.JList<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        EditMixFilters_ListFilters = new javax.swing.JList<>();
        EditMixFilters_Name = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        EditMixFilters_Save = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        EditMixFilters_Filters = new javax.swing.JList<>();
        jLabel13 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel40 = new javax.swing.JLabel();
        TabSettings = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        Settings_PathImagesTemp = new javax.swing.JTextField();
        Settings_Save = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        Settings_Url = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel46 = new javax.swing.JLabel();
        Settings_Threads = new javax.swing.JComboBox<>();
        jLabel38 = new javax.swing.JLabel();
        TabConexion = new javax.swing.JTabbedPane();
        TabLocalConexion = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        Local_IP = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        Local_Puerto = new javax.swing.JTextField();
        Local_Name = new javax.swing.JTextField();
        Local_Save = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        Local_User = new javax.swing.JTextField();
        Local_Pass = new javax.swing.JPasswordField();
        Conexion_MyIP = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        TabRemoteConexion = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        Remote_IP = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        Remote_Puerto = new javax.swing.JTextField();
        Remote_Name = new javax.swing.JTextField();
        Remote_Save = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        Remote_User = new javax.swing.JTextField();
        Remote_Pass = new javax.swing.JPasswordField();
        jLabel37 = new javax.swing.JLabel();
        Conexion_MyIP2 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setForeground(new java.awt.Color(38, 91, 145));
        jTabbedPane1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        TabClassifiers.setForeground(new java.awt.Color(38, 91, 145));
        TabClassifiers.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(38, 91, 145), new java.awt.Color(38, 91, 145)));
        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Classifiers_Start.setBackground(new java.awt.Color(255, 255, 255));
        Classifiers_Start.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        Classifiers_Start.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/play_icon.png"))); // NOI18N
        Classifiers_Start.setText("Iniciar");
        Classifiers_Start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Classifiers_StartActionPerformed(evt);
            }
        });
        jPanel15.add(Classifiers_Start, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 260, 120, -1));

        jLabel53.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(74, 173, 82));
        jLabel53.setText("Parqueo");
        jPanel15.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 80, 40));

        Classifiers_Parkings.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel15.add(Classifiers_Parkings, new org.netbeans.lib.awtextra.AbsoluteConstraints(121, 25, 250, 30));
        jPanel15.add(jTabbedPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -20, -1, -1));

        Classifiers_Stop.setBackground(new java.awt.Color(255, 255, 255));
        Classifiers_Stop.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        Classifiers_Stop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/stop_icon.png"))); // NOI18N
        Classifiers_Stop.setText("Parar");
        Classifiers_Stop.setEnabled(false);
        Classifiers_Stop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Classifiers_StopActionPerformed(evt);
            }
        });
        jPanel15.add(Classifiers_Stop, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 330, 120, -1));

        jPanel1.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 13, 430, 410));

        TabClassifiers.addTab("Proceso", jPanel1);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(38, 91, 145), new java.awt.Color(38, 91, 145)));
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ClassifiersSettings_Save.setBackground(new java.awt.Color(255, 255, 255));
        ClassifiersSettings_Save.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ClassifiersSettings_Save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save_icon.png"))); // NOI18N
        ClassifiersSettings_Save.setText("Guardar");
        ClassifiersSettings_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClassifiersSettings_SaveActionPerformed(evt);
            }
        });
        jPanel17.add(ClassifiersSettings_Save, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 340, -1, -1));

        jLabel54.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(74, 173, 82));
        jLabel54.setText("Imágenes");
        jPanel17.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, -1, 40));

        ClassifiersSettings_Width.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ClassifiersSettings_Width.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ClassifiersSettings_WidthKeyTyped(evt);
            }
        });
        jPanel17.add(ClassifiersSettings_Width, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 210, 190, 40));

        jLabel55.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(74, 173, 82));
        jLabel55.setText("ANCHO ");
        jPanel17.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, -1, 40));
        jPanel17.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, 360, 20));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(38, 91, 145));
        jLabel3.setText("px");
        jPanel17.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 210, 40, 40));

        jLabel56.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(74, 173, 82));
        jLabel56.setText("ALTO");
        jPanel17.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 260, 50, 40));

        ClassifiersSettings_Height.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ClassifiersSettings_Height.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ClassifiersSettings_HeightKeyTyped(evt);
            }
        });
        jPanel17.add(ClassifiersSettings_Height, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 260, 190, 40));

        jLabel57.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(38, 91, 145));
        jLabel57.setText("px");
        jPanel17.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 260, 40, 40));

        jLabel58.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(74, 173, 82));
        jLabel58.setText("Weka");
        jPanel17.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, 40));
        jPanel17.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 770, 20));

        jLabel14.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(74, 173, 82));
        jLabel14.setText("PATH Model");
        jPanel17.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 200, 20));

        ClassifiersSettings_PathModel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ClassifiersSettings_PathModel.setText("C:\\");
            jPanel17.add(ClassifiersSettings_PathModel, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 60, 540, -1));

            jButton3.setBackground(new java.awt.Color(255, 255, 255));
            jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/folder_icon.png"))); // NOI18N
            jButton3.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton3ActionPerformed(evt);
                }
            });
            jPanel17.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 50, 60, -1));

            jLabel17.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
            jLabel17.setForeground(new java.awt.Color(74, 173, 82));
            jLabel17.setText("PATH ARFF");
            jPanel17.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 200, 20));

            ClassifiersSettings_PathArff.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
            ClassifiersSettings_PathArff.setText("C:\\");
                jPanel17.add(ClassifiersSettings_PathArff, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 120, 540, -1));

                jButton4.setBackground(new java.awt.Color(255, 255, 255));
                jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/folder_icon.png"))); // NOI18N
                jButton4.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton4ActionPerformed(evt);
                    }
                });
                jPanel17.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 110, 60, -1));

                jPanel7.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 820, 410));

                TabClassifiers.addTab("Configuración", jPanel7);

                jTabbedPane1.addTab("Clasificador", new javax.swing.ImageIcon(getClass().getResource("/images/car_icon.png")), TabClassifiers); // NOI18N

                TabModelARFF.setBackground(new java.awt.Color(255, 255, 255));
                TabModelARFF.setForeground(new java.awt.Color(74, 173, 82));
                TabModelARFF.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

                TabCreateModel.setBackground(new java.awt.Color(255, 255, 255));
                TabCreateModel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                jPanel8.setBackground(new java.awt.Color(255, 255, 255));
                jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(38, 91, 145), new java.awt.Color(38, 91, 145)));
                jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                CreateModel_Start.setBackground(new java.awt.Color(255, 255, 255));
                CreateModel_Start.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                CreateModel_Start.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/play_icon.png"))); // NOI18N
                CreateModel_Start.setText("Iniciar");
                CreateModel_Start.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        CreateModel_StartActionPerformed(evt);
                    }
                });
                jPanel8.add(CreateModel_Start, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 330, -1, -1));

                jLabel4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jLabel4.setForeground(new java.awt.Color(74, 173, 82));
                jLabel4.setText("Parqueo");
                jPanel8.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 80, 40));

                CreateModel_Parkings.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                jPanel8.add(CreateModel_Parkings, new org.netbeans.lib.awtextra.AbsoluteConstraints(121, 25, 250, 30));

                TabCreateModel.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 430, 410));

                jLabel52.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ARFF_logo.png"))); // NOI18N
                TabCreateModel.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 260, -1, -1));

                TabModelARFF.addTab("Crear", TabCreateModel);

                jPanel2.setBackground(new java.awt.Color(255, 255, 255));
                jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                jPanel9.setBackground(new java.awt.Color(255, 255, 255));
                jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(38, 91, 145), new java.awt.Color(38, 91, 145)));
                jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                SettingsARFF_Save.setBackground(new java.awt.Color(255, 255, 255));
                SettingsARFF_Save.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                SettingsARFF_Save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save_icon.png"))); // NOI18N
                SettingsARFF_Save.setText("Guardar");
                SettingsARFF_Save.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        SettingsARFF_SaveActionPerformed(evt);
                    }
                });
                jPanel9.add(SettingsARFF_Save, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 330, -1, -1));

                jLabel47.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jLabel47.setForeground(new java.awt.Color(74, 173, 82));
                jLabel47.setText("Imágenes");
                jPanel9.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, 40));

                SettingsARFF_Width.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                SettingsARFF_Width.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyTyped(java.awt.event.KeyEvent evt) {
                        SettingsARFF_WidthKeyTyped(evt);
                    }
                });
                jPanel9.add(SettingsARFF_Width, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 60, 190, 40));

                jLabel48.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jLabel48.setForeground(new java.awt.Color(74, 173, 82));
                jLabel48.setText("ANCHO ");
                jPanel9.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, -1, 40));
                jPanel9.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 360, 20));

                jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jLabel2.setForeground(new java.awt.Color(38, 91, 145));
                jLabel2.setText("px");
                jPanel9.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 60, 40, 40));

                jLabel49.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jLabel49.setForeground(new java.awt.Color(74, 173, 82));
                jLabel49.setText("ALTO");
                jPanel9.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 110, 50, 40));

                SettingsARFF_Height.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                SettingsARFF_Height.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyTyped(java.awt.event.KeyEvent evt) {
                        SettingsARFF_HeightKeyTyped(evt);
                    }
                });
                jPanel9.add(SettingsARFF_Height, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 110, 190, 40));

                jLabel50.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jLabel50.setForeground(new java.awt.Color(38, 91, 145));
                jLabel50.setText("px");
                jPanel9.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 110, 40, 40));

                jPanel2.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 470, 410));

                jLabel51.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ARFF_logo.png"))); // NOI18N
                jPanel2.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 260, -1, -1));

                TabModelARFF.addTab("Configuración ", jPanel2);

                jTabbedPane1.addTab("ARFF", new javax.swing.ImageIcon(getClass().getResource("/images/arff_icon.png")), TabModelARFF); // NOI18N

                TabFilters.setBackground(new java.awt.Color(255, 255, 255));
                TabFilters.setForeground(new java.awt.Color(74, 173, 82));
                TabFilters.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

                TabCreateFilter.setBackground(new java.awt.Color(255, 255, 255));
                TabCreateFilter.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                btnSaveFilters.setBackground(new java.awt.Color(255, 255, 255));
                btnSaveFilters.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                btnSaveFilters.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save_icon.png"))); // NOI18N
                btnSaveFilters.setText("Guardar");
                btnSaveFilters.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        btnSaveFiltersActionPerformed(evt);
                    }
                });
                TabCreateFilter.add(btnSaveFilters, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 360, -1, -1));

                CreateFilter_UseGui.setEditable(true);
                CreateFilter_UseGui.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                CreateFilter_UseGui.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "true", "false" }));
                TabCreateFilter.add(CreateFilter_UseGui, new org.netbeans.lib.awtextra.AbsoluteConstraints(103, 51, 167, 28));

                jLabel10.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jLabel10.setForeground(new java.awt.Color(74, 173, 82));
                jLabel10.setText("NOMBRE DEL FILTRO");
                TabCreateFilter.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 18, 170, 18));

                CreateFilter_Name.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                CreateFilter_Name.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyTyped(java.awt.event.KeyEvent evt) {
                        CreateFilter_NameKeyTyped(evt);
                    }
                });
                TabCreateFilter.add(CreateFilter_Name, new org.netbeans.lib.awtextra.AbsoluteConstraints(182, 15, 230, 26));

                jLabel30.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jLabel30.setForeground(new java.awt.Color(74, 173, 82));
                jLabel30.setText("Código");
                TabCreateFilter.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 10, 114, 30));

                jLabel31.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jLabel31.setForeground(new java.awt.Color(74, 173, 82));
                jLabel31.setText("init");
                TabCreateFilter.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 40, 18));

                CreateFilter_Init.setColumns(20);
                CreateFilter_Init.setFont(new java.awt.Font("Consolas", 0, 13)); // NOI18N
                CreateFilter_Init.setRows(5);
                CreateFilter_Init.setText("function(self) {}");
                CreateFilter_Init.setMargin(new java.awt.Insets(5, 5, 5, 5));
                jScrollPane7.setViewportView(CreateFilter_Init);

                TabCreateFilter.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, 410, 84));

                CreateFilter_Gui.setColumns(20);
                CreateFilter_Gui.setFont(new java.awt.Font("Consolas", 0, 13)); // NOI18N
                CreateFilter_Gui.setRows(5);
                CreateFilter_Gui.setText("function(self) {}");
                CreateFilter_Gui.setMargin(new java.awt.Insets(5, 5, 5, 5));
                jScrollPane8.setViewportView(CreateFilter_Gui);

                TabCreateFilter.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, 410, 81));

                jLabel32.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jLabel32.setForeground(new java.awt.Color(74, 173, 82));
                jLabel32.setText("gui");
                TabCreateFilter.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 50, 18));

                CreateFilter_Apply.setColumns(20);
                CreateFilter_Apply.setFont(new java.awt.Font("Consolas", 0, 13)); // NOI18N
                CreateFilter_Apply.setRows(5);
                CreateFilter_Apply.setText("function(self) {\n          TestCanvas.apply(\"Nombre_del_Filtro\", []);\n        }");
                CreateFilter_Apply.setMargin(new java.awt.Insets(5, 5, 5, 5));
                CreateFilter_Apply.setMinimumSize(new java.awt.Dimension(129, 26));
                jScrollPane9.setViewportView(CreateFilter_Apply);

                TabCreateFilter.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 300, 410, 84));

                jLabel33.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jLabel33.setForeground(new java.awt.Color(74, 173, 82));
                jLabel33.setText("apply");
                TabCreateFilter.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 50, 18));

                jLabel34.setBackground(new java.awt.Color(255, 255, 255));
                jLabel34.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jLabel34.setForeground(new java.awt.Color(74, 173, 82));
                jLabel34.setText("USE_GUI");
                TabCreateFilter.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 56, -1, 18));

                CreateFilter_Generate.setBackground(new java.awt.Color(255, 255, 255));
                CreateFilter_Generate.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                CreateFilter_Generate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/generate_icon.png"))); // NOI18N
                CreateFilter_Generate.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        CreateFilter_GenerateActionPerformed(evt);
                    }
                });
                TabCreateFilter.add(CreateFilter_Generate, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 10, 60, -1));

                CreateFilter_Code.setColumns(20);
                CreateFilter_Code.setFont(new java.awt.Font("Consolas", 0, 13)); // NOI18N
                CreateFilter_Code.setRows(5);
                CreateFilter_Code.setText("ImageFilters.Nombre_del_Filtro = function(srcImageData) {\n  var srcPixels = srcImageData.data,\n    srcWidth = srcImageData.width,\n    srcHeight = srcImageData.height,\n    srcLength = srcPixels.length,\n    dstImageData = this.utils.createImageData(srcWidth, srcHeight),\n    dstPixels = dstImageData.data;\n\n  return dstImageData;\n};");
                CreateFilter_Code.setMargin(new java.awt.Insets(5, 5, 5, 5));
                jScrollPane18.setViewportView(CreateFilter_Code);

                TabCreateFilter.add(jScrollPane18, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 50, 630, 300));

                CreateFilters_Copy.setBackground(new java.awt.Color(255, 255, 255));
                CreateFilters_Copy.setText("Copiar Filtro");
                CreateFilters_Copy.addChangeListener(new javax.swing.event.ChangeListener() {
                    public void stateChanged(javax.swing.event.ChangeEvent evt) {
                        CreateFilters_CopyStateChanged(evt);
                    }
                });
                TabCreateFilter.add(CreateFilters_Copy, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 370, 140, 30));

                CreateFilters_JComboFilters.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                CreateFilters_JComboFilters.setEnabled(false);
                CreateFilters_JComboFilters.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        CreateFilters_JComboFiltersItemStateChanged(evt);
                    }
                });
                TabCreateFilter.add(CreateFilters_JComboFilters, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 370, 191, 30));

                TabFilters.addTab("Crear", TabCreateFilter);

                TabEditFilter.setBackground(new java.awt.Color(255, 255, 255));
                TabEditFilter.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                jLabel35.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jLabel35.setForeground(new java.awt.Color(38, 91, 145));
                jLabel35.setText("FILTROS");
                TabEditFilter.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 78, 30));

                EditFilters_JComboFilters.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                EditFilters_JComboFilters.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        EditFilters_JComboFiltersItemStateChanged(evt);
                    }
                });
                TabEditFilter.add(EditFilters_JComboFilters, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 191, 30));

                jLabel41.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jLabel41.setForeground(new java.awt.Color(74, 173, 82));
                jLabel41.setText("USE_GUI");
                TabEditFilter.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 10, 87, 18));

                EditFilter_UseGui.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                EditFilter_UseGui.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "true", "false" }));
                TabEditFilter.add(EditFilter_UseGui, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 30, 167, 28));

                jLabel42.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jLabel42.setForeground(new java.awt.Color(74, 173, 82));
                jLabel42.setText("init");
                TabEditFilter.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 83, 40, 18));

                EditFilter_Init.setColumns(20);
                EditFilter_Init.setFont(new java.awt.Font("Consolas", 0, 13)); // NOI18N
                EditFilter_Init.setRows(5);
                EditFilter_Init.setMargin(new java.awt.Insets(5, 5, 5, 5));
                jScrollPane14.setViewportView(EditFilter_Init);

                TabEditFilter.add(jScrollPane14, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 80, 410, 89));

                jLabel43.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jLabel43.setForeground(new java.awt.Color(74, 173, 82));
                jLabel43.setText("gui");
                TabEditFilter.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 185, 50, 18));

                EditFilter_Gui.setColumns(20);
                EditFilter_Gui.setFont(new java.awt.Font("Consolas", 0, 13)); // NOI18N
                EditFilter_Gui.setRows(5);
                EditFilter_Gui.setMargin(new java.awt.Insets(5, 5, 5, 5));
                jScrollPane15.setViewportView(EditFilter_Gui);

                TabEditFilter.add(jScrollPane15, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 180, 410, 87));

                jLabel44.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jLabel44.setForeground(new java.awt.Color(74, 173, 82));
                jLabel44.setText("apply");
                TabEditFilter.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 296, 50, 18));

                EditFilter_Apply.setColumns(20);
                EditFilter_Apply.setFont(new java.awt.Font("Consolas", 0, 13)); // NOI18N
                EditFilter_Apply.setRows(5);
                EditFilter_Apply.setMargin(new java.awt.Insets(5, 5, 5, 5));
                jScrollPane16.setViewportView(EditFilter_Apply);

                TabEditFilter.add(jScrollPane16, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 280, 410, 84));

                jLabel45.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jLabel45.setForeground(new java.awt.Color(74, 173, 82));
                jLabel45.setText("Código");
                TabEditFilter.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 10, 114, 30));

                UpdateFilters_Simulation.setBackground(new java.awt.Color(255, 255, 255));
                UpdateFilters_Simulation.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                UpdateFilters_Simulation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/play_icon.png"))); // NOI18N
                UpdateFilters_Simulation.setText("Simular");
                UpdateFilters_Simulation.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        UpdateFilters_SimulationActionPerformed(evt);
                    }
                });
                TabEditFilter.add(UpdateFilters_Simulation, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 360, -1, -1));

                UpdateFilters_Save.setBackground(new java.awt.Color(255, 255, 255));
                UpdateFilters_Save.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                UpdateFilters_Save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save_icon.png"))); // NOI18N
                UpdateFilters_Save.setText("Guardar");
                UpdateFilters_Save.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        UpdateFilters_SaveActionPerformed(evt);
                    }
                });
                TabEditFilter.add(UpdateFilters_Save, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 360, -1, -1));

                EditFilter_Code.setColumns(20);
                EditFilter_Code.setFont(new java.awt.Font("Consolas", 0, 13)); // NOI18N
                EditFilter_Code.setRows(5);
                EditFilter_Code.setMargin(new java.awt.Insets(5, 5, 5, 5));
                jScrollPane19.setViewportView(EditFilter_Code);

                TabEditFilter.add(jScrollPane19, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 50, 630, 300));

                TabFilters.addTab("Editar", TabEditFilter);

                TabCreateMix.setBackground(new java.awt.Color(255, 255, 255));
                TabCreateMix.setForeground(new java.awt.Color(38, 91, 145));
                TabCreateMix.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

                jPanel5.setBackground(new java.awt.Color(255, 255, 255));

                jPanel6.setBackground(new java.awt.Color(255, 255, 255));
                jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                jLabel6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jLabel6.setForeground(new java.awt.Color(74, 173, 82));
                jLabel6.setText("FILTROS");
                jPanel6.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 150, -1));

                jLabel7.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jLabel7.setForeground(new java.awt.Color(74, 173, 82));
                jLabel7.setText("Nombre Combinación");
                jPanel6.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 10, -1, 26));

                CreateMixFilters_ListFilters.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                CreateMixFilters_ListFilters.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        CreateMixFilters_ListFiltersMouseClicked(evt);
                    }
                });
                jScrollPane1.setViewportView(CreateMixFilters_ListFilters);

                jPanel6.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 190, 320));

                CreateMixFilters_ListMix.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                CreateMixFilters_ListMix.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        CreateMixFilters_ListMixMouseClicked(evt);
                    }
                });
                jScrollPane3.setViewportView(CreateMixFilters_ListMix);

                jPanel6.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 50, 190, 320));

                CreateMixFilters_Name.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                CreateMixFilters_Name.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyTyped(java.awt.event.KeyEvent evt) {
                        CreateMixFilters_NameKeyTyped(evt);
                    }
                });
                jPanel6.add(CreateMixFilters_Name, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 10, 190, -1));

                CreateMixFilters_Save.setBackground(new java.awt.Color(255, 255, 255));
                CreateMixFilters_Save.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                CreateMixFilters_Save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save_icon.png"))); // NOI18N
                CreateMixFilters_Save.setText("Guardar");
                CreateMixFilters_Save.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        CreateMixFilters_SaveActionPerformed(evt);
                    }
                });
                jPanel6.add(CreateMixFilters_Save, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 330, -1, -1));

                jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/next_icon.png"))); // NOI18N
                jPanel6.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 180, 42, 42));

                jLabel39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/camera_logo.png"))); // NOI18N
                jPanel6.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 150, -1, -1));

                javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
                jPanel5.setLayout(jPanel5Layout);
                jPanel5Layout.setHorizontalGroup(
                    jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                );
                jPanel5Layout.setVerticalGroup(
                    jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                );

                TabCreateMix.addTab("Crear Combinación", jPanel5);

                jPanel4.setBackground(new java.awt.Color(255, 255, 255));
                jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                EditMixFilters_ListMix.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                EditMixFilters_ListMix.setToolTipText("");
                EditMixFilters_ListMix.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        EditMixFilters_ListMixMouseClicked(evt);
                    }
                });
                jScrollPane5.setViewportView(EditMixFilters_ListMix);

                jPanel4.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 180, 330));

                jLabel9.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jLabel9.setForeground(new java.awt.Color(74, 173, 82));
                jLabel9.setText("MIX FILTROS");
                jPanel4.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 150, 20));

                jLabel11.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
                jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/next_icon.png"))); // NOI18N
                jPanel4.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 180, 42, 42));

                EditMixFilters_ListFilters.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                EditMixFilters_ListFilters.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        EditMixFilters_ListFiltersMouseClicked(evt);
                    }
                });
                jScrollPane6.setViewportView(EditMixFilters_ListFilters);

                jPanel4.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 50, 190, 330));

                EditMixFilters_Name.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                EditMixFilters_Name.setEnabled(false);
                jPanel4.add(EditMixFilters_Name, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 20, 190, -1));

                jLabel12.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jLabel12.setForeground(new java.awt.Color(74, 173, 82));
                jLabel12.setText("Nombre Combinación");
                jPanel4.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 20, 160, 26));

                EditMixFilters_Save.setBackground(new java.awt.Color(255, 255, 255));
                EditMixFilters_Save.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                EditMixFilters_Save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save_icon.png"))); // NOI18N
                EditMixFilters_Save.setText("Guardar");
                EditMixFilters_Save.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        EditMixFilters_SaveActionPerformed(evt);
                    }
                });
                jPanel4.add(EditMixFilters_Save, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 330, -1, -1));

                EditMixFilters_Filters.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                EditMixFilters_Filters.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        EditMixFilters_FiltersMouseClicked(evt);
                    }
                });
                jScrollPane10.setViewportView(EditMixFilters_Filters);

                jPanel4.add(jScrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 50, 190, 330));

                jLabel13.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jLabel13.setForeground(new java.awt.Color(74, 173, 82));
                jLabel13.setText("FILTROS");
                jPanel4.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 20, 150, 20));

                jSeparator1.setForeground(new java.awt.Color(38, 91, 145));
                jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
                jPanel4.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 10, 30, 370));

                jLabel40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/camera_logo.png"))); // NOI18N
                jPanel4.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 150, -1, -1));

                TabCreateMix.addTab("Editar Combinación", jPanel4);

                javax.swing.GroupLayout TabMixFilterLayout = new javax.swing.GroupLayout(TabMixFilter);
                TabMixFilter.setLayout(TabMixFilterLayout);
                TabMixFilterLayout.setHorizontalGroup(
                    TabMixFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TabCreateMix)
                );
                TabMixFilterLayout.setVerticalGroup(
                    TabMixFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(TabMixFilterLayout.createSequentialGroup()
                        .addComponent(TabCreateMix, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                );

                TabFilters.addTab("Combinación de Filtros", new javax.swing.ImageIcon(getClass().getResource("/images/camera_icon.png")), TabMixFilter); // NOI18N

                jTabbedPane1.addTab("Filtros", new javax.swing.ImageIcon(getClass().getResource("/images/filters_icon.png")), TabFilters); // NOI18N

                TabSettings.setBackground(new java.awt.Color(255, 255, 255));
                TabSettings.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                jPanel10.setBackground(new java.awt.Color(255, 255, 255));
                jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(38, 91, 145), new java.awt.Color(38, 91, 145)));
                jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jLabel5.setForeground(new java.awt.Color(74, 173, 82));
                jLabel5.setText("PATH Imagenés Temporales ");
                jPanel10.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 200, 20));

                Settings_PathImagesTemp.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                Settings_PathImagesTemp.setText("C:\\");
                    jPanel10.add(Settings_PathImagesTemp, new org.netbeans.lib.awtextra.AbsoluteConstraints(228, 60, 380, -1));

                    Settings_Save.setBackground(new java.awt.Color(255, 255, 255));
                    Settings_Save.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                    Settings_Save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save_icon.png"))); // NOI18N
                    Settings_Save.setText("Guardar");
                    Settings_Save.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            Settings_SaveActionPerformed(evt);
                        }
                    });
                    jPanel10.add(Settings_Save, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 320, -1, -1));

                    jLabel8.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                    jLabel8.setForeground(new java.awt.Color(74, 173, 82));
                    jLabel8.setText("NÚMERO DE HILOS");
                    jPanel10.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 170, 20));

                    Settings_Url.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                    Settings_Url.setText("http://localhost:8080/Servidor/");
                    jPanel10.add(Settings_Url, new org.netbeans.lib.awtextra.AbsoluteConstraints(228, 20, 380, -1));

                    jButton2.setBackground(new java.awt.Color(255, 255, 255));
                    jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/folder_icon.png"))); // NOI18N
                    jButton2.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            jButton2ActionPerformed(evt);
                        }
                    });
                    jPanel10.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 60, 60, -1));

                    jLabel46.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                    jLabel46.setForeground(new java.awt.Color(74, 173, 82));
                    jLabel46.setText("URL");
                    jPanel10.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 93, 20));

                    Settings_Threads.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                    Settings_Threads.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" }));
                    Settings_Threads.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            Settings_ThreadsActionPerformed(evt);
                        }
                    });
                    jPanel10.add(Settings_Threads, new org.netbeans.lib.awtextra.AbsoluteConstraints(228, 100, 70, -1));

                    TabSettings.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 24, 700, 393));

                    jLabel38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/settings_logo.png"))); // NOI18N
                    TabSettings.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 260, -1, -1));

                    jTabbedPane1.addTab("Configuración", new javax.swing.ImageIcon(getClass().getResource("/images/settings_icon.png")), TabSettings); // NOI18N

                    TabConexion.setBackground(new java.awt.Color(255, 255, 255));
                    TabConexion.setForeground(new java.awt.Color(74, 173, 82));
                    TabConexion.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

                    TabLocalConexion.setBackground(new java.awt.Color(255, 255, 255));
                    TabLocalConexion.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                    jPanel11.setBackground(new java.awt.Color(255, 255, 255));
                    jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(38, 91, 145), new java.awt.Color(38, 91, 145)));
                    jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                    jLabel22.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                    jLabel22.setForeground(new java.awt.Color(74, 173, 82));
                    jLabel22.setText("Nombre de la Base de Datos");
                    jLabel22.setToolTipText("");
                    jPanel11.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 86, -1, 20));

                    jLabel19.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                    jLabel19.setForeground(new java.awt.Color(74, 173, 82));
                    jLabel19.setText("IP");
                    jLabel19.setToolTipText("");
                    jPanel11.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 18, 135, -1));

                    Local_IP.setText("166.62.78.1");
                    jPanel11.add(Local_IP, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 15, 201, -1));

                    jLabel20.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                    jLabel20.setForeground(new java.awt.Color(74, 173, 82));
                    jLabel20.setText("Puerto");
                    jLabel20.setToolTipText("");
                    jPanel11.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 53, -1, -1));

                    Local_Puerto.setText("3306");
                    jPanel11.add(Local_Puerto, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 50, 200, -1));

                    Local_Name.setText("server-administrator");
                    jPanel11.add(Local_Name, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 85, 201, -1));

                    Local_Save.setBackground(new java.awt.Color(255, 255, 255));
                    Local_Save.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                    Local_Save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save_icon.png"))); // NOI18N
                    Local_Save.setText("Guardar");
                    Local_Save.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            Local_SaveActionPerformed(evt);
                        }
                    });
                    jPanel11.add(Local_Save, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 260, -1, -1));

                    jPanel12.setBackground(new java.awt.Color(255, 255, 255));
                    jPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(38, 91, 145), new java.awt.Color(38, 91, 145)));
                    jPanel12.setForeground(new java.awt.Color(74, 173, 82));
                    jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                    jLabel21.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                    jLabel21.setForeground(new java.awt.Color(74, 173, 82));
                    jLabel21.setText("USERNAME");
                    jLabel21.setToolTipText("");
                    jPanel12.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 18, -1, -1));

                    jLabel23.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                    jLabel23.setForeground(new java.awt.Color(74, 173, 82));
                    jLabel23.setText("PASSWORD");
                    jLabel23.setToolTipText("");
                    jPanel12.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 73, -1, -1));

                    Local_User.setText("jessicaroman");
                    jPanel12.add(Local_User, new org.netbeans.lib.awtextra.AbsoluteConstraints(193, 15, 142, -1));

                    Local_Pass.setText("12345");
                    jPanel12.add(Local_Pass, new org.netbeans.lib.awtextra.AbsoluteConstraints(193, 70, 142, -1));

                    jPanel11.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 125, 360, 120));

                    TabLocalConexion.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 13, 450, 330));
                    TabLocalConexion.add(Conexion_MyIP, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 360, 250, 39));

                    jLabel29.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                    jLabel29.setForeground(new java.awt.Color(74, 173, 82));
                    jLabel29.setText("IP EXTERNA");
                    jLabel29.setToolTipText("");
                    TabLocalConexion.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, 170, 39));

                    jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/conexion_logo.png"))); // NOI18N
                    TabLocalConexion.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 260, -1, -1));

                    TabConexion.addTab("Programa Interno", TabLocalConexion);

                    jPanel13.setBackground(new java.awt.Color(255, 255, 255));
                    jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                    jPanel14.setBackground(new java.awt.Color(255, 255, 255));
                    jPanel14.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(38, 91, 145), new java.awt.Color(38, 91, 145)));
                    jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                    jLabel24.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                    jLabel24.setForeground(new java.awt.Color(74, 173, 82));
                    jLabel24.setText("Nombre de la Base de Datos");
                    jPanel14.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 86, -1, 20));

                    jLabel25.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                    jLabel25.setForeground(new java.awt.Color(74, 173, 82));
                    jLabel25.setText("IP");
                    jPanel14.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 18, 135, -1));

                    Remote_IP.setText("127.0.0.1");
                    jPanel14.add(Remote_IP, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 15, 201, -1));

                    jLabel26.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                    jLabel26.setForeground(new java.awt.Color(74, 173, 82));
                    jLabel26.setText("Puerto");
                    jPanel14.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 53, -1, -1));

                    Remote_Puerto.setText("3306");
                    jPanel14.add(Remote_Puerto, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 50, 201, -1));

                    Remote_Name.setText("parkingdb");
                    jPanel14.add(Remote_Name, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 85, 201, -1));

                    Remote_Save.setBackground(new java.awt.Color(255, 255, 255));
                    Remote_Save.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                    Remote_Save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save_icon.png"))); // NOI18N
                    Remote_Save.setText("Guardar");
                    Remote_Save.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            Remote_SaveActionPerformed(evt);
                        }
                    });
                    jPanel14.add(Remote_Save, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 260, -1, -1));

                    jPanel16.setBackground(new java.awt.Color(255, 255, 255));
                    jPanel16.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(38, 91, 145), new java.awt.Color(38, 91, 145)));
                    jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                    jLabel27.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                    jLabel27.setForeground(new java.awt.Color(74, 173, 82));
                    jLabel27.setText("USERNAME");
                    jPanel16.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 18, -1, -1));

                    jLabel28.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                    jLabel28.setForeground(new java.awt.Color(74, 173, 82));
                    jLabel28.setText("PASSWORD");
                    jPanel16.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 73, -1, -1));

                    Remote_User.setText("root");
                    jPanel16.add(Remote_User, new org.netbeans.lib.awtextra.AbsoluteConstraints(193, 15, 142, -1));
                    jPanel16.add(Remote_Pass, new org.netbeans.lib.awtextra.AbsoluteConstraints(193, 70, 142, -1));

                    jPanel14.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 125, 360, 120));

                    jPanel13.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 13, 450, 330));

                    jLabel37.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                    jLabel37.setForeground(new java.awt.Color(74, 173, 82));
                    jLabel37.setText("IP EXTERNA");
                    jPanel13.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, 170, 39));
                    jPanel13.add(Conexion_MyIP2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 360, 250, 39));

                    jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/conexion_logo.png"))); // NOI18N
                    jLabel15.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
                    jPanel13.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 260, -1, -1));

                    javax.swing.GroupLayout TabRemoteConexionLayout = new javax.swing.GroupLayout(TabRemoteConexion);
                    TabRemoteConexion.setLayout(TabRemoteConexionLayout);
                    TabRemoteConexionLayout.setHorizontalGroup(
                        TabRemoteConexionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(TabRemoteConexionLayout.createSequentialGroup()
                            .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 1173, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 8, Short.MAX_VALUE))
                    );
                    TabRemoteConexionLayout.setVerticalGroup(
                        TabRemoteConexionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(TabRemoteConexionLayout.createSequentialGroup()
                            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(0, 0, 0))
                    );

                    TabConexion.addTab("Programa Externo", TabRemoteConexion);

                    jTabbedPane1.addTab("Conexión", new javax.swing.ImageIcon(getClass().getResource("/images/conexion_icon.png")), TabConexion); // NOI18N

                    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                    getContentPane().setLayout(layout);
                    layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1191, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap())
                    );
                    layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 523, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap())
                    );

                    pack();
                }// </editor-fold>//GEN-END:initComponents

    private void Settings_ThreadsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Settings_ThreadsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Settings_ThreadsActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        JFileChooser chooser = new JFileChooser();

        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("URL Imagenés Temporales");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //
        // disable the "All files" option.
        //
        chooser.setAcceptAllFileFilterUsed(false);
        //
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            this.Settings_PathImagesTemp.setText(chooser.getSelectedFile().toString());
            //            System.out.println("getCurrentDirectory(): "
            //                    + chooser.getCurrentDirectory());
            //            System.out.println("getSelectedFile() : "
            //                    + chooser.getSelectedFile());
        } else {
            System.out.println("No Selection ");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void Settings_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Settings_SaveActionPerformed
        URL = this.Settings_Url.getText();
        String path = this.Settings_PathImagesTemp.getText();
        String thread = this.Settings_Threads.getSelectedItem().toString();
        String res = path.replace("\\", "/");
        String sql = "UPDATE `settings` SET `url`='" + URL + "', `path`='" + res + "', `threads`='" + thread + "'WHERE `id`=1;";
        String msg = "la configuración general";
        cx.update(sql, msg, 2);
    }//GEN-LAST:event_Settings_SaveActionPerformed

    private void Remote_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Remote_SaveActionPerformed
        String ruta = "./config/conexion.txt";
        String passRemote = (String.valueOf(this.Remote_Pass.getPassword()).equals("")) ? "NONE" : String.valueOf(this.Remote_Pass.getPassword());
        String passLocal = (String.valueOf(this.Local_Pass.getPassword()).equals("")) ? "NONE" : String.valueOf(this.Local_Pass.getPassword());
        cm.Write("Remote,"
                + this.Remote_IP.getText() + ","
                + this.Remote_Puerto.getText() + ","
                + this.Remote_Name.getText() + ","
                + this.Remote_User.getText() + ","
                + passRemote + ","
                + "Local,"
                + this.Local_IP.getText() + ","
                + this.Local_Puerto.getText() + ","
                + this.Local_Name.getText() + ","
                + this.Local_User.getText() + ","
                + passLocal, ruta, false);

        String sql = "UPDATE `config` SET "
                + "`ip`='" + this.Remote_IP.getText() + "', "
                + "`puerto`='" + this.Remote_Puerto.getText() + "', "
                + "`name_db`='" + this.Remote_Name.getText() + "', "
                + "`username`='" + this.Remote_User.getText() + "', "
                + "`password`='" + passRemote
                + "'WHERE `type`='externo'";
        String msg = "la configuración de base de datos externa";
        cx.update(sql, msg, 2);
    }//GEN-LAST:event_Remote_SaveActionPerformed

    private void Local_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Local_SaveActionPerformed
        String ruta = "./config/conexion.txt";
        String passRemote = (String.valueOf(this.Remote_Pass.getPassword()).equals("")) ? "NONE" : String.valueOf(this.Remote_Pass.getPassword());
        String passLocal = (String.valueOf(this.Local_Pass.getPassword()).equals("")) ? "NONE" : String.valueOf(this.Local_Pass.getPassword());
        cm.Write("Remote,"
                + this.Remote_IP.getText() + ","
                + this.Remote_Puerto.getText() + ","
                + this.Remote_Name.getText() + ","
                + this.Remote_User.getText() + ","
                + passRemote + ","
                + "Local,"
                + this.Local_IP.getText() + ","
                + this.Local_Puerto.getText() + ","
                + this.Local_Name.getText() + ","
                + this.Local_User.getText() + ","
                + passLocal, ruta, false);

        String sql = "UPDATE `config` SET "
                + "`ip`='" + this.Local_IP.getText() + "', "
                + "`puerto`='" + this.Local_Puerto.getText() + "', "
                + "`name_db`='" + this.Local_Name.getText() + "', "
                + "`username`='" + this.Local_User.getText() + "', "
                + "`password`='" + passLocal
                + "'WHERE `type`='interno'";
        String msg = "la configuración de base de datos interna";
        cx.update(sql, msg, 2);
    }//GEN-LAST:event_Local_SaveActionPerformed

    private void EditMixFilters_FiltersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EditMixFilters_FiltersMouseClicked
        JList list = (JList) evt.getSource();
        if (evt.getClickCount() == 2) {
            int index = list.locationToIndex(evt.getPoint());
            mix.addElement(this.EditMixFilters_Filters.getSelectedValue());
            //Test
            //            System.out.println(model.size());
            this.CreateMixFilters_ListMix.setModel(mix);
        }
    }//GEN-LAST:event_EditMixFilters_FiltersMouseClicked

    private void EditMixFilters_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditMixFilters_SaveActionPerformed
        try {
            String sql = "SELECT `id` FROM `mix` WHERE `name`='" + this.EditMixFilters_Name.getText() + "'";
            String temp[] = cx.select(sql, 1, 2);
            if (this.EditMixFilters_ListFilters.getModel().getSize() <= 1) {
                JOptionPane.showMessageDialog(null, "Llenar Todos Los Campos ", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                String[] id = temp[0].split(" columns ");
                sql = "DELETE FROM mix_filters WHERE mixID=" + id[0] + ";";
                cx.delete(sql, " ", 2);
                for (int i = 0; i < this.EditMixFilters_ListFilters.getModel().getSize(); i++) {
                    sql = "SELECT `id` FROM `filters` WHERE `name`='" + this.EditMixFilters_ListFilters.getModel().getElementAt(i) + "'";
                    temp = cx.select(sql, 1, 2);
                    String[] subs = temp[0].split(" columns ");
                    sql = "INSERT INTO `mix_filters`(`mixID`,`filterID`) VALUES (?,?);";
                    String[] c = {
                        id[0],
                        subs[0]
                    };
                    String msg = " ";
                    cx.insert(sql, c, msg, 2);
                }
                JOptionPane.showMessageDialog(null, "Se ha actualizado la combinación de filtro", "Aviso", JOptionPane.INFORMATION_MESSAGE);

            }
        } catch (SQLException ex) {
            Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_EditMixFilters_SaveActionPerformed

    private void EditMixFilters_ListFiltersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EditMixFilters_ListFiltersMouseClicked
        JList list = (JList) evt.getSource();
        if (evt.getClickCount() == 2) {
            mix.removeElementAt(this.EditMixFilters_ListFilters.getSelectedIndex());
            //Test
            //            System.out.println(model.size());
            this.EditMixFilters_ListFilters.setModel(mix);
        }
    }//GEN-LAST:event_EditMixFilters_ListFiltersMouseClicked

    private void EditMixFilters_ListMixMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EditMixFilters_ListMixMouseClicked
        JList list = (JList) evt.getSource();
        if (evt.getClickCount() == 1) {
            try {
                mix.clear();
                int index = list.locationToIndex(evt.getPoint());
                String name = this.EditMixFilters_ListMix.getSelectedValue();
                this.EditMixFilters_Name.setText(name);
                String sql = "SELECT filters.name FROM filters JOIN mix_filters ON filters.id=mix_filters.filterID JOIN mix ON mix.id=mix_filters.mixID WHERE mix.name='" + name + "';";
                //Test
                //            System.out.println(model.size());
                String[] temp = cx.select(sql, 1, 2);
                for (int i = 0; i < temp.length; i++) {
                    String[] subs = temp[i].split(" columns ");
                    mix.addElement(subs[0]);
                }
                this.EditMixFilters_ListFilters.setModel(mix);
            } catch (SQLException ex) {
                Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_EditMixFilters_ListMixMouseClicked

    private void CreateMixFilters_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreateMixFilters_SaveActionPerformed
        try {

            String sql = "SELECT `id` FROM `mix` WHERE `name`='" + this.CreateMixFilters_Name.getText() + "'";
            String temp[] = cx.select(sql, 1, 2);

            //Test
            //            System.out.println(temp[0]);
            //            System.out.println(this.CreateMixFilters_Name.getText().length());
            if (temp[0].isEmpty() && this.CreateMixFilters_Name.getText().length() != 0) {
                if (cm.isEmpty(this.CreateMixFilters_Name.getText())
                        && this.CreateMixFilters_ListMix.getModel().getSize() <= 1) {
                    JOptionPane.showMessageDialog(null, "Llenar Todos Los Campos ", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    sql = "INSERT INTO `mix`(`name`) VALUES (?);";
                    String[] columns = {
                        this.CreateMixFilters_Name.getText()
                    };
                    String msg = "la nueva combinacion de filtro";
                    cx.insert(sql, columns, msg, 2);
                    sql = "SELECT MAX(`id`) FROM `mix`";
                    temp = cx.select(sql, 1, 2);
                    String[] max = temp[0].split(" columns ");

                    for (int i = 0; i < this.CreateMixFilters_ListMix.getModel().getSize(); i++) {
                        sql = "SELECT `id` FROM `filters` WHERE `name`='" + this.CreateMixFilters_ListMix.getModel().getElementAt(i) + "'";
                        temp = cx.select(sql, 1, 2);
                        String[] subs = temp[0].split(" columns ");
                        sql = "INSERT INTO `mix_filters`(`mixID`,`filterID`) VALUES (?,?);";
                        String[] c = {
                            max[0],
                            subs[0]
                        };
                        msg = " ";
                        cx.insert(sql, c, msg, 2);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "El nombre de la combinación ya existe ó no es permitido", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_CreateMixFilters_SaveActionPerformed

    private void CreateMixFilters_NameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CreateMixFilters_NameKeyTyped
        this.CreateMixFilters_Name.setText(this.CreateMixFilters_Name.getText().trim());
    }//GEN-LAST:event_CreateMixFilters_NameKeyTyped

    private void CreateMixFilters_ListMixMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CreateMixFilters_ListMixMouseClicked
        JList list = (JList) evt.getSource();
        if (evt.getClickCount() == 2) {
            int index = list.locationToIndex(evt.getPoint());
            model.removeElementAt(this.CreateMixFilters_ListMix.getSelectedIndex());
            //Test
            //            System.out.println(model.size());
            this.CreateMixFilters_ListMix.setModel(model);
        }
    }//GEN-LAST:event_CreateMixFilters_ListMixMouseClicked

    private void CreateMixFilters_ListFiltersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CreateMixFilters_ListFiltersMouseClicked
        JList list = (JList) evt.getSource();
        if (evt.getClickCount() == 2) {
            int index = list.locationToIndex(evt.getPoint());
            model.addElement(this.CreateMixFilters_ListFilters.getSelectedValue());
            //Test
            //            System.out.println(model.size());
            this.CreateMixFilters_ListMix.setModel(model);
        }
    }//GEN-LAST:event_CreateMixFilters_ListFiltersMouseClicked

    private void UpdateFilters_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateFilters_SaveActionPerformed
        String sql = "UPDATE `filters` SET "
                + "`use_gui`='" + this.EditFilter_UseGui.getItemAt(this.EditFilter_UseGui.getSelectedIndex()) + "', "
                + "`init`='" + this.EditFilter_Init.getText() + "', "
                + "`gui`='" + this.EditFilter_Gui.getText() + "', "
                + "`apply`='" + this.EditFilter_Apply.getText() + "', "
                + "`imagefilters_function`='" + this.EditFilter_Code.getText() + "' "
                + "WHERE `name`='" + this.EditFilters_JComboFilters.getItemAt(this.EditFilters_JComboFilters.getSelectedIndex()) + "'";
        String msg = "el filtro";
        //        System.out.println(sql);
        cx.update(sql, msg, 2);
    }//GEN-LAST:event_UpdateFilters_SaveActionPerformed

    private void UpdateFilters_SimulationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateFilters_SimulationActionPerformed

        JFrameTest.http = this.Settings_Url.getText();
        JFrameTest jt = new JFrameTest();
        jt.setDefaultCloseOperation(JFrameTest.DISPOSE_ON_CLOSE);
        jt.setVisible(true);
    }//GEN-LAST:event_UpdateFilters_SimulationActionPerformed

    private void EditFilters_JComboFiltersItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EditFilters_JComboFiltersItemStateChanged
        String nameFilter = this.EditFilters_JComboFilters.getItemAt(this.EditFilters_JComboFilters.getSelectedIndex());
        String sql = "SELECT * FROM `filters` WHERE `name`='" + nameFilter + "';";
        try {
            String temp[] = cx.select(sql, 7, 2);
            String[] substring = temp[0].split(" columns ");
            //Test
            //                System.out.println(temp.length);
            //                System.out.println(substring.length);
            if (substring.length > 1) {
                this.EditFilter_UseGui.setSelectedIndex(substring[2].equals("true") ? 0 : 1);
                this.EditFilter_Init.setText(substring[3]);
                this.EditFilter_Gui.setText(substring[4]);
                this.EditFilter_Apply.setText(substring[5]);
                this.EditFilter_Code.setText(substring[6]);
            }

        } catch (SQLException ex) {
            Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_EditFilters_JComboFiltersItemStateChanged

    private void CreateFilter_GenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreateFilter_GenerateActionPerformed
        if (this.CreateFilter_Name.getText().length() > 3) {
            if (!this.beforeNameFilter.equals("")) {
                String apply = this.CreateFilter_Apply.getText();
                String code = this.CreateFilter_Code.getText();
                String name = this.CreateFilter_Name.getText();
                String[] temp = apply.split(this.beforeNameFilter);
                temp[0] = temp[0] + name;
                String new_apply = temp[0];
                for (int i = 1; i < temp.length; i++) {
                    new_apply += temp[i];
                }
                this.CreateFilter_Apply.setText(" ");
                this.CreateFilter_Apply.setText(new_apply);

                temp = null;
                temp = code.split(this.beforeNameFilter);
                temp[0] = temp[0] + name;
                String new_code = temp[0];
                for (int i = 1; i < temp.length; i++) {
                    new_code += temp[i];
                }
                this.CreateFilter_Code.setText(" ");
                this.CreateFilter_Code.setText(new_code);
                this.beforeNameFilter = name;
            } else {
                String apply = this.CreateFilter_Apply.getText();
                String code = this.CreateFilter_Code.getText();
                String name = this.CreateFilter_Name.getText();
                String[] temp = apply.split("Nombre_del_Filtro");
                temp[0] = temp[0] + name;
                String new_apply = temp[0];
                for (int i = 1; i < temp.length; i++) {
                    new_apply += temp[i];
                }
                this.CreateFilter_Apply.setText(" ");
                this.CreateFilter_Apply.setText(new_apply);

                temp = null;
                temp = code.split("Nombre_del_Filtro");
                temp[0] = temp[0] + name;
                String new_code = temp[0];
                for (int i = 1; i < temp.length; i++) {
                    new_code += temp[i];
                }
                this.CreateFilter_Code.setText(" ");
                this.CreateFilter_Code.setText(new_code);
                this.beforeNameFilter = name;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Se debe llenar el Campo 'Nombre de Filtro' con mínimo 4 caractéres", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_CreateFilter_GenerateActionPerformed

    private void CreateFilter_NameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CreateFilter_NameKeyTyped
        this.CreateFilter_Name.setText(this.CreateFilter_Name.getText().trim());
    }//GEN-LAST:event_CreateFilter_NameKeyTyped

    private void btnSaveFiltersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveFiltersActionPerformed
        try {
            String sql = "SELECT `id` FROM `filters` WHERE `name`='" + this.CreateFilter_Name.getText() + "'";
            String temp[] = cx.select(sql, 1, 2);

            //Test
            //            System.out.println(temp[0]);
            if (temp[0].isEmpty()) {
                sql = "INSERT INTO `filters`(`name`, `use_gui`, `init`, `gui`, `apply`, `imagefilters_function`) VALUES (?,?,?,?,?,?);";
                if (cm.isEmpty(this.CreateFilter_Name.getText())
                        || cm.isEmpty(this.CreateFilter_UseGui.getItemAt(this.CreateFilter_UseGui.getSelectedIndex()))
                        || cm.isEmpty(this.CreateFilter_Init.getText())
                        || cm.isEmpty(this.CreateFilter_Gui.getText())
                        || cm.isEmpty(this.CreateFilter_Apply.getText())
                        || cm.isEmpty(this.CreateFilter_Code.getText())) {
                    JOptionPane.showMessageDialog(null, "Llenar Todos Los Campos ", "Error", JOptionPane.ERROR_MESSAGE);
                } else {

                    String[] columns = {
                        this.CreateFilter_Name.getText(),
                        this.CreateFilter_UseGui.getItemAt(this.CreateFilter_UseGui.getSelectedIndex()),
                        this.CreateFilter_Init.getText(),
                        this.CreateFilter_Gui.getText(),
                        this.CreateFilter_Apply.getText(),
                        this.CreateFilter_Code.getText()
                    };
                    String msg = "el nuevo filtro";
                    cx.insert(sql, columns, msg, 2);

                }
            } else {
                JOptionPane.showMessageDialog(null, "El nombre de filtro ya existe", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSaveFiltersActionPerformed

    private void SettingsARFF_HeightKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SettingsARFF_HeightKeyTyped
        char ch = evt.getKeyChar();
        if (!cm.isNumber(ch) && !cm.isValidSignal(ch, this.SettingsARFF_Height.getText()) && !cm.validatePoint(ch, this.SettingsARFF_Height.getText()) && ch != '\b') {
            evt.consume();
        }
    }//GEN-LAST:event_SettingsARFF_HeightKeyTyped

    private void SettingsARFF_WidthKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SettingsARFF_WidthKeyTyped
        char ch = evt.getKeyChar();
        if (!cm.isNumber(ch) && !cm.isValidSignal(ch, this.SettingsARFF_Width.getText()) && !cm.validatePoint(ch, this.SettingsARFF_Width.getText()) && ch != '\b') {
            evt.consume();
        }
    }//GEN-LAST:event_SettingsARFF_WidthKeyTyped

    private void SettingsARFF_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SettingsARFF_SaveActionPerformed
        String w = this.SettingsARFF_Width.getText();
        String h = this.SettingsARFF_Height.getText();
        String sql = "UPDATE `settings_arff` SET `width`='" + w + "', `height`='" + h + "'WHERE `id`=1;";
        String msg = "la configuración del archivo ARFF";
        cx.update(sql, msg, 2);
    }//GEN-LAST:event_SettingsARFF_SaveActionPerformed

    private void CreateModel_StartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreateModel_StartActionPerformed
         
        ARFFfile.Width = Integer.valueOf(this.SettingsARFF_Width.getText());
        ARFFfile.Height = Integer.valueOf(this.SettingsARFF_Height.getText());
        ARFFfile ar = new ARFFfile();
        Binary.URL=URL;
        String id = this.id_parklots[this.CreateModel_Parkings.getSelectedIndex()];
        JFrameWebView.id = id;
        JFrameWebView.http = this.Settings_Url.getText();
        JFrameWebView s = new JFrameWebView();
        s.setVisible(true);
    }//GEN-LAST:event_CreateModel_StartActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        this.Init();
    }//GEN-LAST:event_formWindowOpened

    private void Classifiers_StartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Classifiers_StartActionPerformed
        try {

            
            ClassExecutorsWhile.state = 10000000;

            String id = this.id_parklots[this.Classifiers_Parkings.getSelectedIndex()];
            
            String sql = "DELETE FROM `detailparklot` WHERE `id_park` ='"+id+"'";
            cx.delete(sql, " ", 1);

            sql = "SELECT pathImg_Parklot FROM `parklot` WHERE id_Parklot ='" + id + "'";
            String[] temp = cx.select(sql, 1, 1);
            String[] sub = temp[0].split(".jpg columns");
            String path_parklot = sub[0].substring(0, sub[0].length() - 19);

            Clasificador.PATH = path_parklot;

            ClassExecutorsWhile cew = new ClassExecutorsWhile();
            cew.RUN(id, Threads, URL, dir);
            this.Classifiers_Start.setEnabled(false);
            this.Classifiers_Stop.setEnabled(true);

        } catch (IOException ex) {
            Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_Classifiers_StartActionPerformed

    private void ClassifiersSettings_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClassifiersSettings_SaveActionPerformed
        String w = this.ClassifiersSettings_Width.getText();
        String h = this.ClassifiersSettings_Height.getText();
        String model = this.ClassifiersSettings_PathModel.getText();
        String arff = this.ClassifiersSettings_PathArff.getText();

        String res_model = model.replace("\\", "/");
        String res_arff = arff.replace("\\", "/");

        String sql = "UPDATE `settings_arff` SET `width`='" + w + "', `height`='" + h + "', `path_model`='" + res_model + "',`path_arff`='" + res_arff + "' WHERE `id`=2;";
        String msg = "la configuración del clasificador";
        cx.update(sql, msg, 2);
    }//GEN-LAST:event_ClassifiersSettings_SaveActionPerformed

    private void ClassifiersSettings_WidthKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ClassifiersSettings_WidthKeyTyped
        char ch = evt.getKeyChar();
        if (!cm.isNumber(ch) && !cm.isValidSignal(ch, this.ClassifiersSettings_Width.getText()) && !cm.validatePoint(ch, this.ClassifiersSettings_Width.getText()) && ch != '\b') {
            evt.consume();
        }
    }//GEN-LAST:event_ClassifiersSettings_WidthKeyTyped

    private void ClassifiersSettings_HeightKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ClassifiersSettings_HeightKeyTyped
        char ch = evt.getKeyChar();
        if (!cm.isNumber(ch) && !cm.isValidSignal(ch, this.ClassifiersSettings_Height.getText()) && !cm.validatePoint(ch, this.ClassifiersSettings_Height.getText()) && ch != '\b') {
            evt.consume();
        }
    }//GEN-LAST:event_ClassifiersSettings_HeightKeyTyped

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        JFileChooser chooser = new JFileChooser();

        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("PATH Modelo");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        chooser.setSelectedFile(new File("*.model"));
        FileFilter filter = new FileNameExtensionFilter("Archivo Model", "model");
        chooser.setFileFilter(filter);
        //
        // disable the "All files" option.
        //
        //chooser.setAcceptAllFileFilterUsed(false);
        //
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            this.ClassifiersSettings_PathModel.setText(chooser.getSelectedFile().toString());
            //            System.out.println("getCurrentDirectory(): "
            //                    + chooser.getCurrentDirectory());
            //            System.out.println("getSelectedFile() : "
            //                    + chooser.getSelectedFile());
        } else {
            System.out.println("No Selection ");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        JFileChooser chooser = new JFileChooser();

        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("PATH Archivo ARFF");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setSelectedFile(new File("*.arff"));
        FileFilter filter = new FileNameExtensionFilter("Archivo ARFF", "arff");
        chooser.setFileFilter(filter);
        //
        // disable the "All files" option.
        //
        //chooser.setAcceptAllFileFilterUsed(false);
        //
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            this.ClassifiersSettings_PathArff.setText(chooser.getSelectedFile().toString());
            //            System.out.println("getCurrentDirectory(): "
            //                    + chooser.getCurrentDirectory());
            //            System.out.println("getSelectedFile() : "
            //                    + chooser.getSelectedFile());
        } else {
            System.out.println("No Selection ");
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void Classifiers_StopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Classifiers_StopActionPerformed
        ClassExecutorsWhile cew = new ClassExecutorsWhile();
        cew.stop();
        this.Classifiers_Start.setEnabled(true);
        this.Classifiers_Stop.setEnabled(false);
    }//GEN-LAST:event_Classifiers_StopActionPerformed

    private void CreateFilters_CopyStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_CreateFilters_CopyStateChanged
        if (this.CreateFilters_Copy.isSelected()) {
            this.CreateFilters_JComboFilters.setEnabled(true);
        } else {
            this.CreateFilters_JComboFilters.setEnabled(false);
            this.CreateFilter_UseGui.setSelectedIndex(0);
            this.CreateFilter_Init.setText("function(self) {}");
            this.CreateFilter_Gui.setText("function(self) {}");
            this.CreateFilter_Apply.setText("function(self) {\n"
                    + "          TestCanvas.apply(\"Nombre_del_Filtro\", []);\n"
                    + "        }");
            this.CreateFilter_Code.setText("ImageFilters.Nombre_del_Filtro = function(srcImageData) {\n"
                    + "  var srcPixels = srcImageData.data,\n"
                    + "    srcWidth = srcImageData.width,\n"
                    + "    srcHeight = srcImageData.height,\n"
                    + "    srcLength = srcPixels.length,\n"
                    + "    dstImageData = this.utils.createImageData(srcWidth, srcHeight),\n"
                    + "    dstPixels = dstImageData.data;\n"
                    + "\n"
                    + "  return dstImageData;\n"
                    + "};");
        }
    }//GEN-LAST:event_CreateFilters_CopyStateChanged

    private void CreateFilters_JComboFiltersItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_CreateFilters_JComboFiltersItemStateChanged
        String nameFilter = this.CreateFilters_JComboFilters.getItemAt(this.CreateFilters_JComboFilters.getSelectedIndex());
        String sql = "SELECT * FROM `filters` WHERE `name`='" + nameFilter + "';";
        try {
            String temp[] = cx.select(sql, 7, 2);
            String[] substring = temp[0].split(" columns ");
            //Test
//                            System.out.println(temp.length);
//                            System.out.println(substring.length);
            if (substring.length > 1) {
                this.CreateFilter_UseGui.setSelectedIndex(substring[2].equals("true") ? 0 : 1);
                this.CreateFilter_Init.setText(substring[3]);
                this.CreateFilter_Gui.setText(substring[4]);
                this.CreateFilter_Apply.setText(substring[5]);
                this.CreateFilter_Code.setText(substring[6]);
            }

            if (substring.length == 1) {

                this.CreateFilter_UseGui.setSelectedIndex(0);
                this.CreateFilter_Init.setText("function(self) {}");
                this.CreateFilter_Gui.setText("function(self) {}");
                this.CreateFilter_Apply.setText("function(self) {\n"
                        + "          TestCanvas.apply(\"Nombre_del_Filtro\", []);\n"
                        + "        }");
                this.CreateFilter_Code.setText("ImageFilters.Nombre_del_Filtro = function(srcImageData) {\n"
                        + "  var srcPixels = srcImageData.data,\n"
                        + "    srcWidth = srcImageData.width,\n"
                        + "    srcHeight = srcImageData.height,\n"
                        + "    srcLength = srcPixels.length,\n"
                        + "    dstImageData = this.utils.createImageData(srcWidth, srcHeight),\n"
                        + "    dstPixels = dstImageData.data;\n"
                        + "\n"
                        + "  return dstImageData;\n"
                        + "};");

            }

        } catch (SQLException ex) {
            this.CreateFilter_UseGui.setSelectedIndex(0);
            this.CreateFilter_Init.setText("function(self) {}");
            this.CreateFilter_Gui.setText("function(self) {}");
            this.CreateFilter_Apply.setText("function(self) {\n"
                    + "          TestCanvas.apply(\"Nombre_del_Filtro\", []);\n"
                    + "        }");
            this.CreateFilter_Code.setText("ImageFilters.Nombre_del_Filtro = function(srcImageData) {\n"
                    + "  var srcPixels = srcImageData.data,\n"
                    + "    srcWidth = srcImageData.width,\n"
                    + "    srcHeight = srcImageData.height,\n"
                    + "    srcLength = srcPixels.length,\n"
                    + "    dstImageData = this.utils.createImageData(srcWidth, srcHeight),\n"
                    + "    dstPixels = dstImageData.data;\n"
                    + "\n"
                    + "  return dstImageData;\n"
                    + "};");
        }
    }//GEN-LAST:event_CreateFilters_JComboFiltersItemStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* Set the Nimbus look and feel */
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrameMain().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField ClassifiersSettings_Height;
    private javax.swing.JTextField ClassifiersSettings_PathArff;
    private javax.swing.JTextField ClassifiersSettings_PathModel;
    private javax.swing.JButton ClassifiersSettings_Save;
    private javax.swing.JTextField ClassifiersSettings_Width;
    private javax.swing.JComboBox<String> Classifiers_Parkings;
    private javax.swing.JButton Classifiers_Start;
    private javax.swing.JButton Classifiers_Stop;
    private javax.swing.JTextField Conexion_MyIP;
    private javax.swing.JTextField Conexion_MyIP2;
    private javax.swing.JTextArea CreateFilter_Apply;
    private javax.swing.JTextArea CreateFilter_Code;
    private javax.swing.JButton CreateFilter_Generate;
    private javax.swing.JTextArea CreateFilter_Gui;
    private javax.swing.JTextArea CreateFilter_Init;
    private javax.swing.JTextField CreateFilter_Name;
    private javax.swing.JComboBox<String> CreateFilter_UseGui;
    private javax.swing.JCheckBox CreateFilters_Copy;
    private javax.swing.JComboBox<String> CreateFilters_JComboFilters;
    private javax.swing.JList<String> CreateMixFilters_ListFilters;
    private javax.swing.JList<String> CreateMixFilters_ListMix;
    private javax.swing.JTextField CreateMixFilters_Name;
    private javax.swing.JButton CreateMixFilters_Save;
    private javax.swing.JComboBox<String> CreateModel_Parkings;
    private javax.swing.JButton CreateModel_Start;
    private javax.swing.JTextArea EditFilter_Apply;
    private javax.swing.JTextArea EditFilter_Code;
    private javax.swing.JTextArea EditFilter_Gui;
    private javax.swing.JTextArea EditFilter_Init;
    private javax.swing.JComboBox<String> EditFilter_UseGui;
    private javax.swing.JComboBox<String> EditFilters_JComboFilters;
    private javax.swing.JList<String> EditMixFilters_Filters;
    private javax.swing.JList<String> EditMixFilters_ListFilters;
    private javax.swing.JList<String> EditMixFilters_ListMix;
    private javax.swing.JTextField EditMixFilters_Name;
    private javax.swing.JButton EditMixFilters_Save;
    private javax.swing.JTextField Local_IP;
    private javax.swing.JTextField Local_Name;
    private javax.swing.JPasswordField Local_Pass;
    private javax.swing.JTextField Local_Puerto;
    private javax.swing.JButton Local_Save;
    private javax.swing.JTextField Local_User;
    private javax.swing.JTextField Remote_IP;
    private javax.swing.JTextField Remote_Name;
    private javax.swing.JPasswordField Remote_Pass;
    private javax.swing.JTextField Remote_Puerto;
    private javax.swing.JButton Remote_Save;
    private javax.swing.JTextField Remote_User;
    private javax.swing.JTextField SettingsARFF_Height;
    private javax.swing.JButton SettingsARFF_Save;
    private javax.swing.JTextField SettingsARFF_Width;
    private javax.swing.JTextField Settings_PathImagesTemp;
    private javax.swing.JButton Settings_Save;
    private javax.swing.JComboBox<String> Settings_Threads;
    private javax.swing.JTextField Settings_Url;
    private javax.swing.JTabbedPane TabClassifiers;
    private javax.swing.JTabbedPane TabConexion;
    private javax.swing.JPanel TabCreateFilter;
    private javax.swing.JTabbedPane TabCreateMix;
    private javax.swing.JPanel TabCreateModel;
    private javax.swing.JPanel TabEditFilter;
    private javax.swing.JTabbedPane TabFilters;
    private javax.swing.JPanel TabLocalConexion;
    private javax.swing.JPanel TabMixFilter;
    private javax.swing.JTabbedPane TabModelARFF;
    private javax.swing.JPanel TabRemoteConexion;
    private javax.swing.JPanel TabSettings;
    private javax.swing.JButton UpdateFilters_Save;
    private javax.swing.JButton UpdateFilters_Simulation;
    private javax.swing.JButton btnSaveFilters;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    // End of variables declaration//GEN-END:variables
}
