package kn.gui;

//import android.widget.TextView;
import android.widget.EditText;
import android.app.Activity;
import android.content.DialogInterface;
import android.view.ViewGroup;
//import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import kn.data.TextFiles;
import kn.data.StringUtils;
import kn.utils.Task;
import kn.ide.EditorActions;
import kn.gui.MessageBox;
import kn.data.FileUtils;
import kn.data.RecentFiles;

public class CommonEditor
{
   public EditText tv;
   private ViewGroup place;
   protected LinearLayout panelsGroup;
   private String orgText = "";
   public String currentEditFile = "";
   public String ProgramName = "Edit file";
   protected Activity parent;
   
   private SearchGroup searchGroup;
   private RecentFiles recentFiles;
        
   public CommonEditor(Activity aparent, EditText atv, ViewGroup aPlace, RecentFiles aRecentFiles)
   {
      tv = atv;
      parent = aparent;
      place = aPlace;    
      panelsGroup = new LinearLayout(parent);
      panelsGroup.setOrientation(1); //VERTICAL
      searchGroup = new SearchGroup(parent, tv);   
      place.addView(panelsGroup);
      panelsGroup.addView(searchGroup);            
      place.addView(tv/*, new LayoutParams(-1, -1)*/);      
      recentFiles = aRecentFiles;
   }
   
   public void insertTextToCurrent(String text)
   {
      int start = tv.getSelectionStart();   
      tv.getEditableText().insert(start , text);      
   }
   
  public void setLastOpenFile(String fileName)
  {
  }
   
  public void openFile(String fileName)
  { 
     openFile(fileName, true);
  }  
   
  public void openFile(String fileName, Boolean showToast)
  {
    recentFiles.addRecent("RecentCodeFile", currentEditFile, tv.getSelectionStart());    
    String res = doOpenFile(fileName, showToast);
    setLastOpenFile(fileName);
    recentFiles.setLastOpenFile(EditorActions.chooseFileForOpenAct, fileName);
    recentFiles.addRecent("RecentCodeFile", fileName, 0);
    tv.setText(res);
  }
   
   public void search()
   {
      searchGroup.show();
   }
   
   public void showCharCode()
   {
      MessageBox msg = new MessageBox(parent, null)
      {
         public void selfBtnListen(MessageBox msg, int which)
         {               
            if ((msg.input != null) && (which == DialogInterface.BUTTON_POSITIVE))
            {
               String lineStr = msg.input.getText().toString();
               try
               {   
                  new MessageBox(parent, null).ShowEmpty(Integer.toString(lineStr.codePointAt(0)));
               }
               catch (Exception e)
               {
                  new MessageBox(parent, null).ShowEmpty("Error " + e.getMessage());
               }
            }
         }
      };
      msg.input("Input One Char", "");   
   }
   
   private void doGotoLine(int line)
   {
      int linePos = StringUtils.lineStartPos(tv.getText().toString(), line);
      if (linePos >= 0)
        tv.setSelection(linePos);
   }
   
   public void gotoLine()
   {
      MessageBox msg = new MessageBox(parent, null)
      {
         public void selfBtnListen(MessageBox msg, int which)
         {               
            if ((msg.input != null) && (which == DialogInterface.BUTTON_POSITIVE))
            {
               String lineStr = msg.input.getText().toString();
               try
               {   
                   int linePos = Integer.parseInt(lineStr);
                   doGotoLine(linePos);
               }
               catch (Exception e)
               {
                  new MessageBox(parent, null).ShowEmpty("Error " + e.getMessage());
               }
            }
         }
      };
      msg.input("Input Line Number", "");
   }
   
  private void doSaveAs(String text)
  {
    FileUtils.chooseFileForOpen(parent, EditorActions.saveAsFileAct, FileUtils.extractFilePath(recentFiles.getLastOpenFile(EditorActions.chooseFileForOpenAct))); //null/*recentFiles*/);
  }
  
  public void saveAs(String newFileName)
  {
      currentEditFile = newFileName;
      save(false);
  }
      
  public void save(Boolean canSaveAs)
  {
      try
      {
         TextFiles f = new TextFiles(parent);
         String s = tv.getText().toString();
         if (currentEditFile.contentEquals("") && canSaveAs)
            doSaveAs(s);
         else
         {
            f.saveFile(currentEditFile, s);      
            orgText = s;
         }
      }
      catch (Exception e)
      {  
         new MessageBox(parent, null).ShowEmpty("Error " + e.getMessage());    
      }
  }
  
  public void doCloseFile()
  {
    currentEditFile = "";
    tv.setText("");
    orgText = "";
  }  
  
  public void closeFile()
  {
     recentFiles.addRecent("RecentCodeFile", currentEditFile, tv.getSelectionStart());
     taskAskToSave(new Task()
                             {
                                public void execute()
                                {
                                   doCloseFile();
                                }
                             });
  }
   
  private String doOpenFile(String fileName, Boolean showToast)
  {
      TextFiles f = new TextFiles(parent);
      f.showToast = showToast;
      currentEditFile = fileName;
      parent.setTitle(ProgramName + " - " + fileName);
           
      orgText = f.loadFile(fileName);
      return orgText;
  }
    
  private boolean needAskToSave()
  {
    return (orgText.compareTo(tv.getText().toString()) != 0); 
  }
  
  public void openFileOnLine(String unit, int line)
  {
     Task t = new Task()
     {
         public void execute()
         {
             tv.setText(doOpenFile(strParam, true));
             doGotoLine(intParam);            
         }     
     };
     t.strParam = unit;
     t.intParam = line;
     taskAskToSave(t);
  }
   
  public void taskAskToSave(Task task)
  {
    if (task == null)
       return;
    if (!(needAskToSave()))
    {
       task.execute();
       return;
    }
    MessageBox msg = new MessageBox(parent, null)
    {
      public void selfBtnListen(MessageBox msg, int which)
      {    
        if (which == DialogInterface.BUTTON_POSITIVE)
          save(true);
        msg.task.execute();
      }
    };
    msg.task = task;
    msg.Ask("There are some changes. Do you want to save it?");
  }
  
  public void openRecent ()
  {
     recentFiles.addRecent("RecentCodeFile", currentEditFile, tv.getSelectionStart());
     new MessageBox(parent, null)
     {
        public void selfBtnListen(MessageBox msg, int which)
        {
             Task t = new Task()
                             {                              
                                public void execute()
                                {
                                   openFile(strParam);                                                                           
                                   try {
                                       tv.setSelection(intParam, intParam);
                                   }
                                catch (Exception e) {
                                }                                       
                                }
                             };                             
            t.strParam = recentFiles.getItem("RecentCodeFile", which);
            t.intParam = recentFiles.getItemLine("RecentCodeFile", which);
            taskAskToSave(t);
        }    
     }.choose("Recent files", recentFiles.getItems("RecentCodeFile"));
  }
}
