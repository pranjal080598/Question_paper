package questionpaper;

import com.mysql.jdbc.Driver;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connect {
    ArrayList<GlobalData> globalquestion = new ArrayList<>();
    ArrayList<String> ch = new ArrayList<>();
    ArrayList<String> chtt = new ArrayList<>();
    
    private Connection con;
    public Connect(){}
    public void makeConnection() throws SQLException {
        if(con==null)
        {
            new Driver();
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/paperDatabase","root","pass@123");
        }
    }
    
    public void closeConnection() throws SQLException   {
        con.close();
    }
    
    public void insertData(String query) throws SQLException    {
        Statement stmt = con.createStatement();
        stmt.executeUpdate(query);
    }
    
    public ArrayList<String> fetchChapterData(int sem,String subject_name,String branchName) throws SQLException    {
        ArrayList<String> chaps = new ArrayList<String>();
        ResultSet rs =null;
        Statement stmt = con.createStatement();
        String sql = "SELECT DISTINCT(chapter_no) from mcqs WHERE branch_name = '"+ branchName + "' and sem_no = "+sem+
                " and subject_name = '"+subject_name+"' ORDER BY chapter_no ";
        try
        {
            rs = stmt.executeQuery(sql);
            while(rs.next())
                chaps.add("Chapter "+Integer.toString(rs.getInt("chapter_no")));
        }
        catch(Exception e){}
        return chaps;
    }    
    public ArrayList<String> fetchChapterData(int sem,String subject_name,String branchName,String format) throws SQLException    {
        ArrayList<String> chaps = new ArrayList<String>();
        ResultSet rs =null;
        Statement stmt = con.createStatement();
        String sql = "SELECT DISTINCT(chapter_no) from tt WHERE branch_name = '"+ branchName + "' and sem_no = "+sem+
                " and subject_name = '"+subject_name+"' ORDER BY chapter_no ";
        try
        {
            rs = stmt.executeQuery(sql);
            while(rs.next())
                chaps.add("Chapter "+Integer.toString(rs.getInt("chapter_no")));
        }
        catch(Exception e){}
        return chaps;
    }
    
    public ResultSet fetchData(int sem,String subject_name,String branchName) throws SQLException   {
        ResultSet rs=null;
        Statement stmt = con.createStatement();
        String sql = "SELECT chapter_no,course_objective,marks,question FROM mcqs WHERE branch_name = '"+ branchName + "' and sem_no = "+sem+
                " and subject_name = '"+subject_name+"' ORDER BY chapter_no ";
 
        try{
            rs = stmt.executeQuery(sql);
        }
        catch(Exception e)  {}   
        return rs;
    }
    public ResultSet fetchData(int sem,String subject_name,String branchName,String format) throws SQLException   {
        ResultSet rs=null;
        Statement stmt = con.createStatement();
        String sql = "SELECT chapter_no,course_objective,marks,question FROM tt WHERE branch_name = '"+ branchName + "' and sem_no = "+sem+
                " and subject_name = '"+subject_name+"' ORDER BY chapter_no ";
 
        try{
            rs = stmt.executeQuery(sql);
        }
        catch(Exception e)  {}   
        return rs;
    }
    
    public ArrayList<String> getDistinctCO(int[] chapter) throws SQLException   {
        for(int j=0;j<chapter.length;j++)
            ch.add(Integer.toString(chapter[j]));
        
        ArrayList<String> distinctCO = new ArrayList<>();
        int x=0;
        Statement stmt = con.createStatement();
        String query = "SELECT DISTINCT(course_objective) AS temp from mcqs WHERE";
        String t="";
        int i=0;
        for(i=0;i<chapter.length-1;i++)
            t += " chapter_no="+chapter[i] + " or";
        t+=" chapter_no="+chapter[i];
        query += t;
        ResultSet rs = stmt.executeQuery(query);
        
        while(rs.next())
            distinctCO.add(rs.getString("temp"));

        return distinctCO;
    }
    public ArrayList<String> getDistinctCO(int[] chapter,boolean flag) throws SQLException   {
        for(int j=0;j<chapter.length;j++)
            chtt.add(Integer.toString(chapter[j]));
        
        ArrayList<String> distinctCO = new ArrayList<>();
        int x=0;
        Statement stmt = con.createStatement();
        String query = "SELECT DISTINCT(course_objective) AS temp from tt WHERE";
        String t="";
        int i=0;
        for(i=0;i<chapter.length-1;i++)
            t += " chapter_no="+chapter[i] + " or";
        t+=" chapter_no="+chapter[i];
        query += t;
        ResultSet rs = stmt.executeQuery(query);
        
        while(rs.next())
            distinctCO.add(rs.getString("temp"));

        return distinctCO;
    }
    
    public void setGlobal() throws SQLException {
        Statement stmt = con.createStatement();
        String query = "SELECT chapter_no,marks,course_objective,question from mcqs WHERE";
        String clause="";
        int i=0;
        for(i=0;i<ch.size()-1;i++)
            clause += " chapter_no="+Integer.parseInt(ch.get(i)) + " or";
        clause +=" chapter_no="+ Integer.parseInt(ch.get(i));
        query += clause;
        ResultSet rs = stmt.executeQuery(query);
        while(rs.next())
            globalquestion.add(new GlobalData(rs.getString("question"),rs.getString("chapter_no"),rs.getString("course_objective"),Integer.parseInt(rs.getString("marks"))));        
    }
    public void setGlobalTT() throws SQLException   {
        Statement stmt = con.createStatement();
        String query = "SELECT chapter_no,marks,course_objective,question from tt WHERE";
        String clause="";
        int i=0;
        for(i=0;i<chtt.size()-1;i++)
            clause += " chapter_no="+Integer.parseInt(chtt.get(i)) + " or";
        clause +=" chapter_no="+ Integer.parseInt(chtt.get(i));
        query += clause;
        ResultSet rs = stmt.executeQuery(query);
        while(rs.next())
            globalquestion.add(new GlobalData(rs.getString("question"),rs.getString("chapter_no"),rs.getString("course_objective"),Integer.parseInt(rs.getString("marks"))));        
    } 
    
    public ArrayList<String> fetchQuestions(String CO,int eachCO) throws SQLException{
        ArrayList<String> ques = new ArrayList<>();
        ArrayList<Data> currentCO = new ArrayList<Data>();
        int eachChap = eachCO/ch.size();
        Statement stmt = con.createStatement();
        Random rand = new Random();
        for(GlobalData gD : globalquestion) {
            if(gD.getCo().equals(CO))
                currentCO.add((new Data(gD.getQue(),gD.getCha())));
        }
        for(String c:ch)    {
            ArrayList<String> currentChap = new ArrayList<>();
            for(Data d:currentCO)   {
                if(d.getCha().equals(c))
                    currentChap.add(d.getQue());
            }
            for(int i=0;i<eachChap;i++) {
                int n=0;
                if(currentChap.size() == 0)
                    break;
                n = rand.nextInt(100000)%currentChap.size();
                ques.add(currentChap.get(n));

                int j=0;
                for(j=0;j<currentCO.size();j++)
                {
                    Data d = currentCO.get(j);
                    if(d.getQue().equals(currentChap.get(n)) && d.getCha().equals(c))
                        break;
                }
                currentCO.remove(j);
                for(j=0;j<globalquestion.size();j++)    {
                    GlobalData gd = globalquestion.get(j);
                    if(gd.getQue().equals(currentChap.get(n)) && gd.getCha().equals(c) && gd.getCo().equals(CO))
                        break;
                }
                globalquestion.remove(j);
                currentChap.remove(currentChap.get(n));
            }
        }
        return ques;
    }    
    
    public ArrayList<String> fetchQuestion34(ArrayList<String> course,int eachCO)   {
        ArrayList<String> q34 = new ArrayList<>();
        
        ArrayList<GlobalData> tempQues3 = new ArrayList<>();
        ArrayList<GlobalData> tempQues4 = new ArrayList<>();
        ArrayList<String> tempChap = new ArrayList<>();
        tempChap.addAll(chtt);
        Map m = new HashMap();
        for(String s:course)
            m.put(s,eachCO);
        
        for(GlobalData gd:globalquestion)
        {
            if(gd.getMarks()==3)
                tempQues3.add(gd);
            else if(gd.getMarks()==4)
                tempQues4.add(gd);
        }
        // generate 4 questions of 3 marks
        
        ArrayList<String> removeIndex = new ArrayList<>();
        ArrayList<String> removeCo = new ArrayList<>();
        
        Collections.shuffle(tempQues3);
        int count = 0;
        for(int i=0;i<tempQues3.size();i++)
        {
            if(count==4 || tempChap.isEmpty())
                break;
            GlobalData t = tempQues3.get(i);
            if(tempChap.contains(t.getCha()))
            {
                if(m.containsKey(t.getCo()))
                {
                    int v = (int)m.get(t.getCo());
                    m.remove(t.getCo());
                    if(v!=0)
                    {
                        m.put(t.getCo(),v-1);
                        q34.add(t.getQue());
                        tempChap.remove(t.getCha());
                        removeIndex.add(Integer.toString(i));
                        count++;
                        if(v-1==0)
                        {
                            removeCo.add(t.getCo());                            
                        }
                    }
                    else
                    {
                        removeCo.add(t.getCo());
                    }
                }
            }
        }
        if(count!=4)
        {
            Collections.reverse(removeIndex);
            for(String rem : removeIndex)
                tempQues3.remove(Integer.parseInt(rem));
            removeIndex.clear();
            

            for(int i=0;i<tempQues3.size();i++)
                if(removeCo.contains(tempQues3.get(i).getCo()))
                    removeIndex.add(Integer.toString(i));
            
            Collections.reverse(removeIndex);
            for(String rem : removeIndex)
                tempQues3.remove(Integer.parseInt(rem));
            removeIndex.clear();
            Random rand = new Random();
            
            while(count!=4)
            {
                int z = rand.nextInt(100000)%tempQues3.size();
                GlobalData g = tempQues3.get(z);
                if(m.containsKey(g.getCo()))
                {
                    int v = (int)m.get(g.getCo());
                    m.remove(g.getCo());
                    if(v!=0)
                    {
                        m.put(g.getCo(),v-1);
                        q34.add(g.getQue());
                        tempQues3.remove(z);
                        count++;
                    }
                }
            }    
        }
        
        // generate 6 questions of 4 marks
        removeCo.clear();
        removeIndex.clear();
        for(int i=0;i<tempQues4.size();i++)
        {
            GlobalData g= tempQues4.get(i);
            if(q34.size()==10 || tempChap.size()==0)
                break;
            if(tempChap.contains(g.getCha()))
            {
                if(m.containsKey(g.getCo()))
                {
                    int v = (int)m.get(g.getCo());
                    m.remove(g.getCo());
                    if(v!=0)
                    {
                        m.put(g.getCo(),v-1);
                        q34.add(g.getQue());
                        tempChap.remove(g.getCha());
                        removeIndex.add(Integer.toString(i));
                        count++;
                        if(v-1==0)
                        {
                            removeCo.add(g.getCo());                            
                        }
                    }
                    else
                    {
                        removeCo.add(g.getCo());
                    }
                }
            }
        }
        Collections.reverse(removeIndex);
        for(String rem : removeIndex)
            tempQues4.remove(Integer.parseInt(rem));
        removeIndex.clear();


        for(int i=0;i<tempQues4.size();i++)
            if(removeCo.contains(tempQues4.get(i).getCo()))
                removeIndex.add(Integer.toString(i));

        Collections.reverse(removeIndex);
        for(String rem : removeIndex)
            tempQues4.remove(Integer.parseInt(rem));
        removeIndex.clear();
        removeCo.clear();
        Random rand= new Random();
        
        for(String c:course)
        {
            if(m.containsKey(c))
            {
                ArrayList<String> tempCo = new ArrayList<>();
                for(int i=0;i<tempQues4.size();i++)
                    if(tempQues4.get(i).getCo().equals(c))
                        tempCo.add(tempQues4.get(i).getQue());
                        
                int v = (int)m.get(c);
                while(v!=0)
                {
                    if(q34.size()==0 || tempCo.size()==0)
                        break;
                    int n= rand.nextInt(100000)%tempCo.size();
                    q34.add(tempCo.get(n));
                    int i=0;
                    for(i=0;i<tempQues4.size();i++)
                        if(tempQues4.get(i).getQue().equals(tempCo.get(n)))
                            break;
                    tempQues4.remove(i);
                    tempCo.remove(n);
                    v--;
                }
            }
        }
        while(q34.size()!=10)
        {
            int n= rand.nextInt(10000)%tempQues4.size();
            q34.add(tempQues4.get(n).getQue());
            tempQues4.remove(n);
        }
        
        for(String q3:q34)
            System.out.println(q3);

        return q34;
    }
    
    public ArrayList<String> fetchAdditionalQuestions(int count) throws SQLException    {
        ArrayList<String> ques = new ArrayList<>();
        Collections.shuffle(globalquestion);
        Random rand = new Random();
        for(int i=0;i<count;i++)
        {
            int n=0;
            if(globalquestion.size()==0)
                break;
            n = rand.nextInt(100000)%globalquestion.size();
            ques.add(globalquestion.get(n).getQue());
            globalquestion.remove(n);
        }
        return ques;
    }
    
    private static class Data {
        private String que,cha;
        public Data(String que,String cha) 
        {
            this.cha = cha;
            this.que = que;
        }
        public String getQue() {return que;}
        public String getCha() {return cha;}
    }
   
    private static class GlobalData {
        private String que,cha,co;
        int marks = 1;
        public GlobalData(String que,String cha,String co)  {
            this.cha = cha;
            this.que = que;
            this.co = co;
        }
        public GlobalData(String que,String cha,String co,int marks)  {
            this.cha = cha;
            this.que = que;
            this.co = co;
            this.marks = marks;
        }        
        public String getCo()  {return co;}
        public String getQue() {return que;}
        public String getCha() {return cha;}

        public int getMarks()  {return marks;}    
    }
    
    public void deleteMCQ(String cp,String co,String ma,String qu) throws SQLException  {
        ResultSet rs =null;
        try {
            Statement stmt = con.createStatement();
            String sql = "DELETE from mcqs where chapter_no="+cp+" and course_objective='"+co+"' and "
                    + "marks="+ma+" and question='"+qu+"'";
            System.out.println(sql);
            stmt.execute(sql);
        } catch (SQLException ex) {}    }
    public void deleteTT(String cp,String co,String ma,String qu) throws SQLException   {
        ResultSet rs =null;
        try {
            Statement stmt = con.createStatement();
            String sql = "DELETE from tt where chapter_no="+cp+" and course_objective='"+co+"' and "
                    + "marks="+ma+" and question='"+qu+"'";
            System.out.println(sql);
            stmt.execute(sql);
        } catch (SQLException ex) {}        
    }
}
