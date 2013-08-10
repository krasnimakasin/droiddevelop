package com.kn.DroidDevelop;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
//import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
//import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.content.DialogInterface;
import android.text.Html;
import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.net.Uri.Builder;
//import android.view.MotionEvent;

import kn.gui.MessageBox;
import kn.gui.Dialogs;
import kn.data.TextFiles;
import kn.data.PrefsStorage;
import kn.data.RecentFiles;
import kn.data.FileUtils;
//import kn.data.StringUtils;

import kn.utils.Task;
//import kn.outside.GetClassRef;


public class MainActivity extends Activity
{
  private Editor editor;
  private Compiler compiler;
  private Project project;
  private EditText tv;
  private PrefsStorage prefs = new PrefsStorage(this);
  private RecentFiles recentFiles = new RecentFiles(prefs);

  private Resources res;
  
  private String buildScriptFile = "";
  
  private static int chooseFileForOpenAct = 1;
  private static int compileScriptAct = 2;
  private static int chooseFileForOpenProjectAct = 3;
  private static int runProgramAct = 4;
  
  public static String stProgramName="DroidDevelop";
  
//===================================================================
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
//===================================================================
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    tv = (EditText)findViewById(R.id.vwEdit);
    editor = new Editor(this, tv);
    compiler = new Compiler(this, editor);
    project = new Project(this, prefs);
    res = getResources();
    openProject(recentFiles.getLastOpenFile(chooseFileForOpenProjectAct));
  }
  
  private void runProgram(String fileName)
  {
     TextFiles t = new TextFiles(this);
     recentFiles.setLastOpenFile(runProgramAct, fileName);
     project.setRunFileName(fileName);
     t.execute(fileName);
  }
  
  private String doOpenFile(String fileName)
  {    
    String res = editor.OpenFile(fileName);
    recentFiles.setLastOpenFile(chooseFileForOpenAct, fileName);
    recentFiles.addRecent("RecentCodeFile", fileName);
    return res;
  }
  
  private void openProject(String fileName)
  {
      if (fileName.compareTo("") == 0)
         return;
      buildScriptFile = fileName;
      Toast.makeText(this, "Opening " + fileName + " project", 2000).show();
      recentFiles.setLastOpenFile(chooseFileForOpenProjectAct, fileName);
      project.readOptions(fileName);
  }
  
  private void chooseAndOpenFile(int Act)
  {
    MessageBox msg = new MessageBox(this, null)
    {
      public void selfBtnListen(MessageBox msg, int which)
      {    
        if ((msg.input != null) && (which == DialogInterface.BUTTON_POSITIVE))
        {
          String fileName = msg.input.getText().toString();
                    
          if (intParam == chooseFileForOpenAct)
            tv.setText(doOpenFile(fileName));
          else if (intParam == chooseFileForOpenProjectAct)
            openProject(fileName);
          else if (intParam == runProgramAct)
            runProgram(fileName);
        }
        else if (which == DialogInterface.BUTTON_NEUTRAL)
          FileUtils.chooseFileForOpen(msg.parent, intParam);
      }    
    };
    msg.neutralBtn = "OI FileManager";
    msg.intParam = Act;
    String s = recentFiles.getLastOpenFile(Act);
    msg.input("Choose file", s);  
  }
  
  public void OnClickBtnOpen (final View view)
  {
     editor.taskAskToSave(new Task()
                             {
                                public void execute()
                                {
                                   chooseAndOpenFile(chooseFileForOpenAct);
                                }
                             });
  }
  
  public void OnClickBtnSave (final View view)
  {
     editor.save();
  }

  public void closeFile()
  {
     editor.taskAskToSave(new Task()
                             {
                                public void execute()
                                {
                                   editor.CloseFile();
                                }
                             });
  }

  public void OnClickBtnNew (final View view)
  {
     closeFile();
  }
  
  public void OnClickBtnCompile (final View view)
  {
       compiler.runCompile(buildScriptFile, compileScriptAct);
  }
  
  public void OnClickBtnRun (final View view)
  {
     String s = project.getRunFileName();
     if (s.compareTo("") != 0)
        runProgram(s);
     else
        chooseAndOpenFile(runProgramAct);
  }

  public void OnClickBtnRecent (final View view)
  {
     new MessageBox(this, null)
     {
        public void selfBtnListen(MessageBox msg, int which)
        {
             Task t = new Task()
                             {                              
                                public void execute()
                                {
                                   tv.setText(doOpenFile(strParam));
                                }
                             };
            t.strParam = recentFiles.getItems("RecentCodeFile")[which];
            editor.taskAskToSave(t);
            //tv.setText(doOpenFile(recentFiles.getItems("RecentCodeFile")[which]));
        }    
     }.choose("Recent files", recentFiles.getItems("RecentCodeFile"));
     //chooseRecentFile(runProgramAct);
  }
  
  public void OnClickBtnOthers (final View view)
  {
     try
     {
        String[] arr = {"Open last compilation messages...",
                              "Open last compilation log...",
                              "List of class methods...",
                              "Go to Line...",
                              "Char code..."};
        new MessageBox(this, null)
        {
           public void selfBtnListen(MessageBox msg, int which)
           {
	         if (which == 0) 
	            compiler.showLastCompilationMessages();
   	         else if (which == 1)
   	            compiler.showLastCompilationText();
   	         else if (which == 2)
   	            compiler.getClassRef();
   	         else if (which == 3)
   	            editor.gotoLine();
   	         else if (which == 4)
   	            editor.showCharCode();
           }    
        }.choose("Other actions", arr);
      }
      catch  (Exception e) 
      {
         new MessageBox(this, null).ShowEmpty("Unknown error 1");
      }
  }
  
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent)
  {
	if ((requestCode == chooseFileForOpenAct)  && (resultCode == RESULT_OK))
	{
	   String path = FileUtils.FileNameFromIntent(intent);
        tv.setText(doOpenFile(path));
	}
	if ((requestCode == chooseFileForOpenProjectAct) && (resultCode == RESULT_OK))
	{
	   buildScriptFile = FileUtils.FileNameFromIntent(intent);
	   openProject(buildScriptFile);
	}
	if ((requestCode == runProgramAct) && (resultCode == RESULT_OK))
	{
	   String path = FileUtils.FileNameFromIntent(intent);
	   runProgram(path);
	}
	if (requestCode == compileScriptAct)
	   compiler.processResult(intent);
  }
  
  public boolean onCreateOptionsMenu (Menu menu)
  {
    getMenuInflater().inflate(R.menu.mainmenu, menu);
    return super.onCreateOptionsMenu(menu);
  }
  
  private void showInfo()
  {
     Html.ImageGetter img = new Html.ImageGetter()
        {
           public Drawable getDrawable(String source)
           {            
             Drawable result = res.getDrawable(R.drawable.assoft);
             if (result != null) 
                result.setBounds(20, 0, 140, 100);
             return result;
           }
        };
     String s = "<div align=center>";
     s += "<a href=http://en.assoft.ru><img src=assoft.png alt=www.en.assoft.ru/></a>";
     s += "</div><br>";
     s += "<div align=center>";
     s += "<h2><a href=mailto:admin@assoft.ru> Make it Usability!</a></h2>";
     s += "<a href=https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&amp;hosted_button_id=U7K7ADWGBYRTY>Donate</a>";
     s += "</div><br>";
     //s += "<h2>Специальная версия для <a href=http://www.my-free-soft.ru>www.my-free-soft.ru</a></h2>";
     Dialogs.showHtml(this, s, "", true, img);
  }

  public boolean onOptionsItemSelected (MenuItem item)
  {
    switch (item.getItemId())
    {
      case R.id.opt_exit:
        finish();
        return true;
      case R.id.opn_prj:
        chooseAndOpenFile(chooseFileForOpenProjectAct);
        return true;
      case R.id.opt_infos:
        showInfo ();
        return true;
      case R.id.opt_prj_opt:
        project.prjOptionsDlg();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  } 
  
  //private void fnInfos ()
  //{
  //  final Intent i;
  //  try
  //  {
  //    i = new Intent (this, NewPrjActivity.class);
      //Log.i(stProgramName, "Starting info activity");
  //    startActivity(i);
  //  }
  //  catch (Exception e)
   // { 
      //tv.append(e.getMessage());
  //  }
  //}
} // MainActivity