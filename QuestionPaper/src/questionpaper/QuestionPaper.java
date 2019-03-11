package questionpaper;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.Subject;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class QuestionPaper
{      
    private int lkl=0;
    private JFrame mainFrame;
    private JPanel controlPanel,temp;
    private JButton homeButton,addButton,genButton,delButton;
    private boolean flag=false;
    private JScrollPane scrollPane;
    
    public static void main(String[] args)  {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new QuestionPaper().showCardLayout();
            }
        });
    }   
    
    private void showCardLayout()   {
        mainFrame = new JFrame("Question Paper Generator");
        mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainFrame.setSize(600,600);
        mainFrame.setResizable(false);
                
        controlPanel = new JPanel();
        controlPanel.setLayout(null);
        
        mainFrame.add(controlPanel);
        mainFrame.setVisible(true);
        final JPanel panel = new JPanel();
        panel.setSize(600,500);

        CardLayout layout = new CardLayout();
        layout.setHgap(5);layout.setVgap(5);
        panel.setLayout(layout);

        JPanel homePanel  = getHomePanel();   panel.add("Home",homePanel);
        JPanel addPanel   = getAddPanel();    panel.add("Add" ,addPanel);
        JPanel genPanel   = getGenPanel();    panel.add("Gen" ,genPanel);
        JPanel delPanel   = getDelPanel();    panel.add("Del" ,delPanel);

        homeButton = new JButton("Home");                homeButton.setBounds(20,510,80,35);
        addButton  = new JButton("Add/View Questions");  addButton.setBounds(110,510,150,35);
        genButton  = new JButton("Generate Questions");  genButton.setBounds(270,510,160,35);
        delButton  = new JButton("Delete Questions");    delButton.setBounds(440,510,140,35);
      
        homeButton.addActionListener((ActionEvent e) -> {
            CardLayout cardLayout = (CardLayout)(panel.getLayout());
            cardLayout.show(panel,"Home");
            homeButton.setEnabled(false);genButton.setEnabled(true);
            addButton.setEnabled(true);delButton.setEnabled(true);
        });
        addButton.addActionListener((ActionEvent e) -> {
            CardLayout cardLayout = (CardLayout)(panel.getLayout());
            cardLayout.show(panel,"Add");
            
            homeButton.setEnabled(true);genButton.setEnabled(true);
            addButton.setEnabled(false);delButton.setEnabled(true);
        });
        genButton.addActionListener((ActionEvent e) -> {
            CardLayout cardLayout = (CardLayout)(panel.getLayout());
            cardLayout.show(panel,"Gen");
            homeButton.setEnabled(true);genButton.setEnabled(false);
             addButton.setEnabled(true);delButton.setEnabled(true);
        });
        delButton.addActionListener((ActionEvent e) -> {
            CardLayout cardLayout = (CardLayout)(panel.getLayout());
            cardLayout.show(panel,"Del");
            homeButton.setEnabled(true);genButton.setEnabled(true);
             addButton.setEnabled(true);delButton.setEnabled(false);
        });

        controlPanel.add(homeButton);
        controlPanel.add(addButton);
        controlPanel.add(genButton);
        controlPanel.add(delButton);

        controlPanel.add(panel);
    }
        
    private JPanel getHomePanel()   {
        JPanel homePanel = new JPanel();
        homePanel.setLayout(new GridLayout(2,1));
        
        //add path of image here
        //Image size strictly 550*250px
        ImageIcon image = new ImageIcon("C:\\Users\\Ronak Shah\\Desktop\\circle.jpg");
        JLabel imageLabel = new JLabel(image);
        homePanel.add(imageLabel);      
            
//        homePanel.add(new JLabel("Image comes here"));
        
        JLabel projectTitle = new JLabel( "Project : Question Paper Generator ",JLabel.CENTER );
        projectTitle.setFont(new java.awt.Font("Arial", Font.ITALIC, 28));
        projectTitle.setOpaque(true);
        projectTitle.setForeground(Color.RED);
        homePanel.add(projectTitle);
        
        return homePanel;
    } 
    
    private JPanel getAddPanel()    {
       
        JPanel addPanel = new JPanel();
        addPanel.setLayout(null);
        
        JLabel branchLabel = new JLabel("Branch : ");           branchLabel.setBounds(10,20,50,30);
        String[] branches = new String[] {"Computer","Information Technology","Electronics","Mechanical"};
        JComboBox<String> branch = new JComboBox<>(branches);   branch.setBounds(65,20,140,30);

        JLabel semesterLabel = new JLabel("Semester : ");   semesterLabel.setBounds(215,20,70,30);
        String[] sems = new String[] {"1","2","3","4","5","6","7","8"};
        JComboBox<String> sem = new JComboBox<>(sems);      sem.setBounds(295,20,45,30);

        JLabel subjectLabel = new JLabel("Subject : ");     subjectLabel.setBounds(350,20,60,30);
        JTextField subject = new JTextField("");            subject.setBounds(420,20,150,30);
        
        JLabel formatLabel = new JLabel("Paper format : "); formatLabel.setBounds(10,80,95,30);
        JRadioButton mcq = new JRadioButton("MCQ's",true);  mcq.setBounds(115,80,65,30);    
        JRadioButton tt  = new JRadioButton("Term Test");   tt.setBounds(190,80,85,30); 
        ButtonGroup group = new ButtonGroup();  group.add(mcq); group.add(tt);
        
        JButton newButton = new JButton("Add New");newButton.setBounds(310,80,100,30);
        JButton searchButton = new JButton("Search");searchButton.setBounds(430,80,100,30);
              
        JLabel defText = new JLabel("No Data Found");
        defText.setBounds(100,200,100,40);
        defText.setVisible(true);

        newButton.addActionListener((ActionEvent e) -> {
            String subj = subject.getText();
            if(subj.isEmpty())
            {
                JOptionPane.showMessageDialog(mainFrame," Please Enter Subject ");
                return;
            }

            int sems1 = Integer.parseInt(sem.getSelectedItem().toString());

            String bran = branch.getSelectedItem().toString();
            String format = "mcqs";
            if(tt.isSelected())
                format="tt";
                        
            
            JPanel input = new JPanel(new GridLayout(8,8,10,10));
            
            JLabel sn,bn,sna,c,co,q,fo,m;
            JTextField tsn,tbn,tsna,tc,tco,tq,tfo,tm;
            
            sn = new JLabel("Sem Number : ");    
            bn = new JLabel("Branch Name : ");
            sna = new JLabel("Subject Name : ");
            fo = new JLabel("Format ");
            c = new JLabel("Enter Chapter Number : ");
            co = new JLabel("Enter Course Objective : ");
            q = new JLabel("Enter Question : ");
            m = new JLabel("Enter Marks : ");
            
            tsn = new JTextField(Integer.toString(sems1));  tsn.setEditable(false);
            tbn = new JTextField(bran);                      tbn.setEditable(false);
            tsna = new JTextField(subj);                     tsna.setEditable(false);
            tfo = new JTextField(format);                   tfo.setEditable(false);
            tc = new JTextField();
            tco = new JTextField();
            tq = new JTextField();
            tm = new JTextField();
            if(format.equals("mcqs"))
            {
                tm.setText("1");
                tm.setEditable(false);
            }

            input.add(sn);input.add(tsn);
            input.add(bn);input.add(tbn);
            input.add(sna);input.add(tsna);
            input.add(fo);input.add(tfo);
            input.add(c);input.add(tc);
            input.add(co);input.add(tco);
            input.add(q);input.add(tq);
            input.add(m);input.add(tm);
            
            JOptionPane jp = new JOptionPane();
            jp.setSize(400,400);
            
            int form = JOptionPane.showConfirmDialog
                       (mainFrame,input, "Question Form : ", JOptionPane.OK_CANCEL_OPTION
                                                           , JOptionPane.PLAIN_MESSAGE);
            if (form == JOptionPane.OK_OPTION) 
            {
                try 
                {
                    Connect con = new Connect();
                    con.makeConnection();
                    String query = "INSERT INTO paperdatabase."+format+" (sem_no,chapter_no,marks,course_objective,subject_name,branch_name,question) VALUES (";
                    query += sems1 + "," + Integer.parseInt(tc.getText()) + ","+Integer.parseInt(tm.getText())+",'" + tco.getText() + "','" + tsna.getText() + "','" + bran + "','" + tq.getText() + "')";
                    System.out.println(query);
                    con.insertData(query);
                    JOptionPane.showMessageDialog(mainFrame," Question Added Successfully ");
                    con.closeConnection();
                }   
                catch(SQLException exception){}
            }
        });
                
        searchButton.addActionListener((ActionEvent e) -> {
            Component[] comp = addPanel.getComponents();
            for(Component c : comp)
                if(c.equals(scrollPane))
                {
                    Component[] x = scrollPane.getComponents();
                    for(Component y:x)
                        scrollPane.remove(y);
                    addPanel.remove(scrollPane);
                    break;
                }
            int sems1 = Integer.parseInt(sem.getSelectedItem().toString());
            String subj = subject.getText();
            String bran = branch.getSelectedItem().toString();
            String format = "mcqs";
            if(tt.isSelected())
                format="tt";
            if(subj.isEmpty())
            {
                JOptionPane.showMessageDialog(mainFrame," Please Enter Subject ");
                return;
            }

            try 
            {
                Connect con = new Connect();
                con.makeConnection();
                ResultSet rs;
                JTable table;
                if(format=="mcqs")
                {
                    rs = con.fetchData(sems1, subj, bran);
                    table = new JTable(buildTableModel1(rs));
                }
                else
                {
                    rs = con.fetchData(sems1, subj, bran, format);
                    table = new JTable(buildTableModel1(rs));
                }
                table.getColumnModel().getColumn(0).setMaxWidth(60);
                table.getColumnModel().getColumn(1).setMaxWidth(40);
                table.getColumnModel().getColumn(2).setMaxWidth(40);
                scrollPane = new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                addPanel.add(scrollPane);
                scrollPane.setBounds(20,130,550,350);
                if(table.getRowCount()==0)
                {
                    scrollPane.setVisible(false);
                    defText.setVisible(true);
                }
                
                defText.setVisible(false);
                con.closeConnection();
            }
            catch (SQLException ex) {}
            
        });
        
        addPanel.add(branchLabel);      addPanel.add(branch);
        addPanel.add(semesterLabel);    addPanel.add(sem);
        addPanel.add(subjectLabel);     addPanel.add(subject);
        addPanel.add(defText);          addPanel.add(formatLabel);
        addPanel.add(mcq);              addPanel.add(tt);
        addPanel.add(newButton);        addPanel.add(searchButton);
        
        return addPanel;
    }
    
    private static DefaultTableModel buildTableModel1(ResultSet rs) throws SQLException  {
        ResultSetMetaData metaData = rs.getMetaData();
        // names of columns
        Vector<String> columnNames;
        columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();

        columnNames.add("Chap");
        columnNames.add("CO");
        columnNames.add("Marks");
        columnNames.add("Question");

        // data of the table
        Vector<Vector<Object>> data;
        data = new Vector<>();
        while (rs.next()) 
        {
            Vector<Object> vector;
            vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) 
                vector.add(rs.getObject(columnIndex));
            data.add(vector);
        }
        return new DefaultTableModel(data, columnNames);
    }   
    
    private JPanel getGenPanel()    {
        JPanel genPanel = new JPanel();
        genPanel.setLayout(null);
        
        JLabel branchLabel = new JLabel("Branch : ");           branchLabel.setBounds(10,20,50,30);
        String[] branches = new String[] {"Computer","Information Technology","Electronics","Mechanical"};
        JComboBox<String> branch = new JComboBox<>(branches);   branch.setBounds(65,20,140,30);

        JLabel semesterLabel = new JLabel("Semester : ");   semesterLabel.setBounds(215,20,70,30);
        String[] sems = new String[] {"1","2","3","4","5","6","7","8"};
        JComboBox<String> sem = new JComboBox<>(sems);      sem.setBounds(295,20,45,30);

        JLabel subjectLabel = new JLabel("Subject : ");     subjectLabel.setBounds(350,20,60,30);
        JTextField subject = new JTextField("");            subject.setBounds(420,20,150,30);
        
        JLabel formatLabel = new JLabel("Paper format : "); formatLabel.setBounds(10,80,95,30);
        JRadioButton mcq = new JRadioButton("MCQ's",true);  mcq.setBounds(115,80,65,30);    
        JRadioButton tt  = new JRadioButton("Term Test");   tt.setBounds(190,80,85,30); 
        ButtonGroup group = new ButtonGroup();  group.add(mcq); group.add(tt);
        
        JLabel defText = new JLabel("No Data Found");
        defText.setBounds(100,200,100,40);
        defText.setVisible(true);

        JButton submitButton = new JButton("Submit");   submitButton.setBounds(430,80,100,30);
        JButton gener =new JButton("Create Paper");     gener.setBounds(50,400,200,30); 
        gener.setVisible(false);

        JCheckBox[] checkBoxes = new JCheckBox[100];
        
        submitButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {

                String subj = subject.getText();
                if(subj.isEmpty())
                {
                    JOptionPane.showMessageDialog(mainFrame," Please Enter Subject ");
                    return;
                }

                gener.setVisible(true);
                defText.setVisible(false);

                int sems1 = Integer.parseInt(sem.getSelectedItem().toString());
                String bran = branch.getSelectedItem().toString();
                String format = "mcqs";
                flag=false;
                if(tt.isSelected())
                {
                    format="tt";
                    flag=true;
                }

                try
                {
                    for(int i=0;i<lkl;i++)
                        temp.remove(checkBoxes[i]);
                    mainFrame.repaint();
                    Connect c = new Connect();
                    c.makeConnection();
                    for(int i=0;i<lkl;i++)
                        checkBoxes[i]=null;
                   
                    ArrayList<String> chaps = new ArrayList<>();
                    if(format=="mcqs")
                        chaps = c.fetchChapterData(sems1, subj, bran);
                    else
                        chaps = c.fetchChapterData(sems1,subj,bran,format);

                    c.closeConnection();
                    lkl = chaps.size();
                    temp = new JPanel(new GridLayout(3,chaps.size()/3));

                    for(int i=0;i<chaps.size();i++)
                    {
                        checkBoxes[i] = new JCheckBox(chaps.get(i));
                        temp.add(checkBoxes[i]);
                    }
                    temp.setBounds(20,130,550,250);
                    temp.setVisible(true);
                    genPanel.add(temp);
                    mainFrame.invalidate();
                    mainFrame.validate();
                }
                catch(SQLException e1){}
            }
        });
        ArrayList<String> send = new ArrayList<>();

        gener.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                for(int i=0;i<lkl;i++)
                    if(checkBoxes[i].isSelected())
                        send.add(checkBoxes[i].getText());
                String subj = subject.getText();
                CreatePaper createPaper = new CreatePaper();
                try
                {
                    if(flag==false)
                        createPaper.generate(send);
                    else
                    {
                        createPaper.generate(send,0,subj);
                        JOptionPane.showMessageDialog(mainFrame," Paper Created Successfully  ");
                    }
                        
                } 
                catch (IOException ex) {}
            }
        });        
        
        genPanel.add(branchLabel);      genPanel.add(branch);
        genPanel.add(semesterLabel);    genPanel.add(sem);
        genPanel.add(subjectLabel);     genPanel.add(subject);
        genPanel.add(defText);          genPanel.add(formatLabel);
        genPanel.add(mcq);              genPanel.add(tt);
        genPanel.add(submitButton);     genPanel.add(gener);         

            return genPanel;
        
    }    

    private JPanel getDelPanel()    {
        JPanel delPanel = new JPanel();
        delPanel.setLayout(null);
        
        JLabel branchLabel = new JLabel("Branch : ");           branchLabel.setBounds(10,20,50,30);
        String[] branches = new String[] {"Computer","Information Technology","Electronics","Mechanical"};
        JComboBox<String> branch = new JComboBox<>(branches);   branch.setBounds(65,20,140,30);

        JLabel semesterLabel = new JLabel("Semester : ");   semesterLabel.setBounds(215,20,70,30);
        String[] sems = new String[] {"1","2","3","4","5","6","7","8"};
        JComboBox<String> sem = new JComboBox<>(sems);      sem.setBounds(295,20,45,30);

        JLabel subjectLabel = new JLabel("Subject : ");     subjectLabel.setBounds(350,20,60,30);
        JTextField subject = new JTextField("");            subject.setBounds(420,20,150,30);
        
        JLabel formatLabel = new JLabel("Paper format : "); formatLabel.setBounds(10,80,95,30);
        JRadioButton mcq = new JRadioButton("MCQ's",true);  mcq.setBounds(115,80,65,30);    
        JRadioButton tt  = new JRadioButton("Term Test");   tt.setBounds(190,80,85,30); 
        ButtonGroup group = new ButtonGroup();  group.add(mcq); group.add(tt);
        
        JButton submitButton = new JButton("Submit");submitButton.setBounds(430,80,100,30);
              
        JLabel defText = new JLabel("No Data Found");
        defText.setBounds(100,200,100,40);
        defText.setVisible(true);

        submitButton.addActionListener((ActionEvent e) -> {
            Component[] comp = delPanel.getComponents();
            for(Component c : comp)
                if(c.equals(scrollPane))
                {
                    Component[] x = scrollPane.getComponents();
                    for(Component y:x)
                        scrollPane.remove(y);
                    delPanel.remove(scrollPane);
                    break;
                }
            int sems1 = Integer.parseInt(sem.getSelectedItem().toString());
            String subj = subject.getText();
            String bran = branch.getSelectedItem().toString();
            String format = "mcqs";
            if(tt.isSelected())
                format="tt";
            if(subj.isEmpty())
            {
                JOptionPane.showMessageDialog(mainFrame," Please Enter Subject ");
                return;
            }

            try 
            {
                Connect con = new Connect();
                con.makeConnection();
                ResultSet rs;
                JTable table;
                if(format=="mcqs")
                {
                    rs = con.fetchData(sems1, subj, bran);
                    table = new JTable(buildTableModel1(rs));
                }
                else
                {
                    rs = con.fetchData(sems1, subj, bran, format);                    
                    table = new JTable(buildTableModel2(rs));

                }
                table.getColumnModel().getColumn(2).setMaxWidth(40);
                table.getColumnModel().getColumn(0).setMaxWidth(60);
                table.getColumnModel().getColumn(1).setMaxWidth(40);
                JPopupMenu popup = new JPopupMenu();
                JMenuItem menuItem = new JMenuItem("Delete Question");
                menuItem.addActionListener(new ActionListener() 
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        Component c = (Component)e.getSource();
                        JPopupMenu popup = (JPopupMenu)c.getParent();
                        JTable table = (JTable)popup.getInvoker();
                        int row =table.getSelectedRow();
                        String cp = table.getModel().getValueAt(row,0).toString();
                        String co = table.getModel().getValueAt(row,1).toString();
                        String ma = table.getModel().getValueAt(row,2).toString();
                        String qu = table.getModel().getValueAt(row,3).toString();
                        Connect c1 = new Connect();
                        try
                        {
                            c1.makeConnection();
                            if(mcq.isSelected()==true)
                                c1.deleteMCQ(cp,co,ma,qu);
                            else
                                c1.deleteTT(cp,co,ma,qu);
                            c1.closeConnection();
                        }
                        catch(Exception e2){}
                    }
                });
                popup.add( menuItem );

                table.addMouseListener( new MouseAdapter()
                {
                    public void mouseReleased(MouseEvent e)
                    {
                        if(e.isPopupTrigger())
                        {
                            JTable source = (JTable)e.getSource();
                            int row = source.rowAtPoint( e.getPoint() );
                            int column = source.columnAtPoint( e.getPoint() );
                            if (! source.isRowSelected(row))
                                source.changeSelection(row, column, false, false);
                            popup.show(e.getComponent(), e.getX(), e.getY());
                        }
                    }
                });
                
                scrollPane = new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                delPanel.add(scrollPane);
                scrollPane.setBounds(20,130,550,350);
                if(table.getRowCount()==0)
                {
                    scrollPane.setVisible(false);
                    defText.setVisible(true);
                }
                
                defText.setVisible(false);
                con.closeConnection();
            }
            catch (SQLException ex) {}
        });
        
        delPanel.add(branchLabel);      delPanel.add(branch);
        delPanel.add(semesterLabel);    delPanel.add(sem);
        delPanel.add(subjectLabel);     delPanel.add(subject);
        delPanel.add(defText);          delPanel.add(formatLabel);
        delPanel.add(mcq);              delPanel.add(tt);
        delPanel.add(submitButton);
        
        return delPanel; 
    }
    private static DefaultTableModel buildTableModel2(ResultSet rs) throws SQLException  {
        ResultSetMetaData metaData = rs.getMetaData();
        // names of columns
        Vector<String> columnNames;
        columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();

        columnNames.add("Chap");
        columnNames.add("CO");
        columnNames.add("Marks");
        columnNames.add("Question");

        // data of the table
        Vector<Vector<Object>> data;
        data = new Vector<>();
        while (rs.next()) 
        {
            Vector<Object> vector;
            vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) 
                vector.add(rs.getObject(columnIndex));
            
            data.add(vector);
        }
        return new DefaultTableModel(data, columnNames);
    }

}

////private static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException  {
//        ResultSetMetaData metaData = rs.getMetaData();
//        // names of columns
//        Vector<String> columnNames;
//        columnNames = new Vector<>();
//        int columnCount = metaData.getColumnCount();
//
//        columnNames.add("Chap");
//        columnNames.add("CO");
//        columnNames.add("Question");
//        
//        // data of the table
//        Vector<Vector<Object>> data;
//        data = new Vector<>();
//        while (rs.next()) 
//        {
//            Vector<Object> vector;
//            vector = new Vector<>();
//            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) 
//                vector.add(rs.getObject(columnIndex));
//            data.add(vector);
//        }
//        return new DefaultTableModel(data, columnNames);
//    }   





