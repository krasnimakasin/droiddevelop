package kn.ide;

import android.app.Activity;
import android.widget.EditText;
import android.view.View;

import kn.data.RecentFiles;
import kn.data.PrefsStorage;
import kn.gui.MessageBox;
import kn.gui.CommonEditor;
import kn.ide.BaseProject;
import kn.ide.EditorActions;
import kn.utils.Task;

public class IDEActivity extends Activity
{
  protected PrefsStorage prefs = new PrefsStorage(this);
  protected RecentFiles recentFiles = new RecentFiles(prefs);
  protected EditText tv;
  
  protected CommonEditor peekEditor()
  { 
     return null; 
  }
  
  protected BaseProject peekProject()
  {
    return null;
  }

  protected void doChooseAndOpenFile(MessageBox msg, int which, int intParam)
  {

  }

  protected void chooseAndOpenFile(int Act)
  {
    MessageBox msg = new MessageBox(this, null)
    {
      public void selfBtnListen(MessageBox msg, int which)
      {  
        doChooseAndOpenFile(msg, which, intParam);
      }    
    };
    msg.neutralBtn = "OI FileManager";
    msg.intParam = Act;
    String s = recentFiles.getLastOpenFile(Act);
    msg.input("Choose file", s);  
  }

  private void doOpenClick(final View view)
  {
     peekEditor().taskAskToSave(new Task()
                             {
                                public void execute()
                                {
                                   chooseAndOpenFile(EditorActions.chooseFileForOpenAct);
                                }
                             });
  }
  
  public void OnClickBtnHide(final View view)
  {
     peekProject().changeButtonsVisible(); 
  }
  
  public void newFile()
  {
  	  peekEditor().closeFile();
  }
  
  public void OnClickBtnOpen (final View view)
  {
     try
     {
        String[] arr = {"New",
                              "Open File...",
                              "Recent Files..."};
        new MessageBox(this, null)
        {
           public void selfBtnListen(MessageBox msg, int which)
           {
	         if (which == 0) 
                newFile();
   	         else if (which == 1)
   	            doOpenClick(view);
   	         else if (which == 2)
   	            peekEditor().openRecent();
           }    
        }.choose("Other actions", arr);
      }
      catch  (Exception e) 
      {
         new MessageBox(this, null).ShowEmpty("Unknown error 1");
      }
  }
  
  public void OnClickBtnSave (final View view)
  {
     peekEditor().save(true);
  }

  private void superonBackPressed()
  {
     super.onBackPressed();
  }
  
  public void onBackPressed()
  {
     peekEditor().taskAskToSave(new Task()
                             {
                                public void execute()
                                {
                                   superonBackPressed();
                                }
                             });  
  }

  public boolean onSearchRequested()
  {
     peekEditor().search();
     return super.onSearchRequested();     
  }

}