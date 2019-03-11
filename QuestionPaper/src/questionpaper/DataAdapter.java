package questionpaper;

public final class DataAdapter 
{
    private int id,semNo,chapterNo,marks;
    private String courseObjectiveNumber,subjectName,branchName,question;
    
    public DataAdapter(int id,int semNo,int chapterNo,int marks,String courseObjectiveNumber,String subjectName,String branchName,String question)
    {
        setId(id);
        setSemNo(semNo);
        setChapterNo(chapterNo);
        setMarks(marks);
        setCourseObjectiveNumber(courseObjectiveNumber);
        setSubjectName(subjectName);
        setBranchName(branchName);
        setQuestion(question);
    }
    
    public int getId()                      {return id;}
    public int getSemNo()                   {return semNo;}
    public int getChapterNo()               {return chapterNo;}
    public int getMarks()                   {return marks;}
    public String getCourseObjectiveNumber(){return courseObjectiveNumber;}
    public String getSubjectName()          {return subjectName;}
    public String getBranchName()           {return branchName;}
    public String getQuestion()             {return question;}
    
    public void setId(int id)       {this.id = id;}
    public void setSemNo(int semNo) {this.semNo = semNo;}
    public void setChapterNo(int chapterNo) {this.chapterNo = chapterNo;}
    public void setMarks(int marks) {this.marks = marks;}
    public void setCourseObjectiveNumber(String courseObjectiveNumber) {this.courseObjectiveNumber = courseObjectiveNumber;}
    public void setSubjectName(String subjectName) {this.subjectName = subjectName;}
    public void setBranchName(String branchName) {this.branchName = branchName;}
    public void setQuestion(String question) {this.question = question;}
}
