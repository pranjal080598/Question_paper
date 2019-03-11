package questionpaper;

import com.itextpdf.text.Chunk;
import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import java.io.IOException;
import java.nio.file.Paths;

import java.util.Date;

class CreatePaper 
{
    public void generate(ArrayList<String> send,int flag,String subj)  throws IOException
    {
        int[] chapter = new int[send.size()];
        int i=0;
        ArrayList<String> distinctCO = new ArrayList<>();
        ArrayList<String> questions = new ArrayList<>();
        for(String s:send)
        {
            String[] temp = s.split(" ");
            chapter[i++] = Integer.parseInt(temp[1]);
        }
            
        try
        {
            Connect c = new Connect();
            c.makeConnection();
            distinctCO = c.getDistinctCO(chapter,true);
            int eachCO = 10/distinctCO.size();
            c.setGlobalTT();
            ArrayList<String> temp = c.fetchQuestion34(distinctCO,eachCO);
            questions.addAll(temp);
            c.closeConnection();
            ArrayList<String> temp3 = new ArrayList<>();
            ArrayList<String> temp4 = new ArrayList<>();
            for(i=0;i<4;i++)
                temp3.add(temp.get(i));
            for(;i<10;i++)
                temp4.add(temp.get(i));
            Collections.shuffle(temp3);
            Collections.shuffle(temp4);
            
            String path = Paths.get(".").toAbsolutePath().normalize().toString();
            try 
            {
                OutputStream file = new FileOutputStream(new File(path+"\\questions.pdf"));

                Document document = new Document();
                PdfWriter.getInstance(document, file);

                Font font1 = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
                Font font2 = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL);
                Font font3 = new Font(Font.FontFamily.TIMES_ROMAN,12, Font.BOLD);
                document.open();
                Paragraph para = new Paragraph("Anjuman-i-Islam's\n");
                para.setAlignment(Element.ALIGN_CENTER);
                document.add(para);

                Chunk collegeName = new Chunk("M.H.SABOO SIDDIK POLYTECHNIC\n", font1);
                Paragraph paraColl = new Paragraph(collegeName);
                paraColl.setAlignment(Element.ALIGN_CENTER);
                document.add(paraColl);
                
                Chunk collegeAdd = new Chunk("8,Saboo Siddik Polytechnic Rd,Mumbai - 8\n", font2);
                Paragraph paraAdd = new Paragraph(collegeAdd);
                paraAdd.setAlignment(Element.ALIGN_CENTER);
                document.add(paraAdd);

                Paragraph paraStar = new Paragraph("**********\n\n");
                paraStar.setAlignment(Element.ALIGN_CENTER);
                document.add(paraStar);                
                
                Chunk exam = new Chunk("CLASS TEST\n", font1);
                Paragraph paraExam = new Paragraph(exam);
                paraExam.setAlignment(Element.ALIGN_CENTER);
                document.add(paraExam);
                
                Chunk year = new Chunk("Diploma in Computer Engineering\n", font3);
                Paragraph paraYear = new Paragraph(year);
                paraYear.setAlignment(Element.ALIGN_CENTER);
                document.add(paraYear);
                
                Chunk subject = new Chunk("Subject:"+subj+"\n", font3);
                Paragraph paraSub = new Paragraph(subject);
                paraSub.setAlignment(Element.ALIGN_CENTER);
                document.add(paraSub);
                
                DottedLineSeparator separator = new DottedLineSeparator();
                separator.setPercentage(590000f / 523f);
                Chunk linebreak = new Chunk(separator);
                document.add(linebreak);
                
                Chunk q1 = new Chunk("Q.1.  Attempt any Three Questions :                  (9)\n", font3);
                document.add(new Paragraph(q1));
                
                document.add(new Paragraph("     1.  "+temp3.get(0)+"\n"));
                document.add(new Paragraph("     2.  "+temp3.get(1)+"\n"));
                document.add(new Paragraph("     3.  "+temp3.get(2)+"\n"));                
                document.add(new Paragraph("     4.  "+temp3.get(3)+"\n\n"));

                Chunk q2 = new Chunk("Q.2.  Attempt any Two Questions :                    (8)\n", font3);
                document.add(new Paragraph(q2));
                document.add(new Paragraph("     1.  "+temp4.get(0)+"\n"));
                document.add(new Paragraph("     2.  "+temp4.get(1)+"\n"));
                document.add(new Paragraph("     3.  "+temp4.get(2)+"\n\n"));

                Chunk q3 = new Chunk("Q.3.  Attempt any Two Questions :                    (8)\n", font3);
                document.add(new Paragraph(q3));                                
                document.add(new Paragraph("     1.  "+temp4.get(3)+"\n"));             
                document.add(new Paragraph("     2.  "+temp4.get(4)+"\n"));
                document.add(new Paragraph("     3.  "+temp4.get(5)+"\n\n"));
                
                document.close();
                file.close();
                System.out.println("Success");
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
        }
        catch(Exception e){e.printStackTrace();}
    }

    public void generate(ArrayList<String> send)  throws IOException    {

        int[] chapter = new int[send.size()];
        int i=0;
        ArrayList<String> distinctCO = new ArrayList<>();
        ArrayList<String> questions = new ArrayList<>();
        for(String s:send)
        {
            String[] temp = s.split(" ");
            chapter[i++] = Integer.parseInt(temp[1]);
        }
            
        Connect c = new Connect();
        try
        {
            c.makeConnection();
            distinctCO = c.getDistinctCO(chapter);
            int eachCO = 25/distinctCO.size();
            c.setGlobal();
            for(String s:distinctCO)
            {
                ArrayList<String> temp = c.fetchQuestions(s,eachCO);
                questions.addAll(temp);
            }
            if(questions.size()<25)
                questions.addAll(c.fetchAdditionalQuestions(25-questions.size()));
            c.closeConnection();
            Collections.shuffle(questions);
            for(int q=0;q<questions.size();q++)
                System.out.println(q+1+"\t"+questions.get(q));
                
            String path = Paths.get(".").toAbsolutePath().normalize().toString();
            try 
            {
                OutputStream file = new FileOutputStream(new File(path+"\\questions.pdf"));

                Document document = new Document();
                PdfWriter.getInstance(document, file);

                document.open();
                for(i=0;i<questions.size();i++)
                    document.add(new Paragraph("Q."+(i+1)+" "+questions.get(i)+"\n\n"));


                document.close();
                file.close();
                System.out.println("Success");
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
        }
        catch(Exception e){}
    }
}
