//com.android.internal.os.ZygoteInit$MethodAndArgsCaller
//org.eclipse.debug.internal.core.LaunchManager
package com.assoft.DroidDevelop;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.content.DialogInterface;

import kn.gui.MessageBox;
import kn.data.FileUtils;
import kn.utils.Task;
import kn.ide.IDEActivity;
import kn.gui.CommonEditor;
import kn.ide.EditorActions;
import kn.ide.ResUtils;

import com.assoft.DroidDevelop.Editor;
import com.assoft.DroidDevelop.Project;
import com.assoft.DroidDevelop.R;
import com.assoft.DroidDevelop.Memo;
import com.assoft.DroidDevelop.Compiler;

public class DDActivity extends IDEActivity
{
  private Editor editor;
  private Compiler compiler;
  private Project project;
  private RefactPlugins refactPlugins;
  private NewPrjPlugins newPrjPlugins;
  private PluginsManager pluginsManager;
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    try
    {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
      tv = new Memo(this, R.styleable.View);  
   
      ViewGroup scr = (ViewGroup)findViewById(R.id.vwEdit);
      Memo.highLight = highLightSupport();          
      editor = new Editor(this, tv, scr, recentFiles);
      refactPlugins = new RefactPlugins(this, tv);
      editor.ProgramName = stProgramName();
      compiler = new Compiler(this, editor);
      project = new Project(this, prefs, recentFiles, editor);
      project.settings.initFirst(false, 12, false);
      editor.setProject(project);
      newPrjPlugins = new NewPrjPlugins(this, project);
      pluginsManager = new PluginsManager(this, project);
      project.openProject(recentFiles.getLastOpenFile(EditorActions.chooseFileForOpenProjectAct));      
      initRes();
      setEvents();
    }
    catch (Exception e)
    {
       new MessageBox(this, null).ShowEmpty(e.getMessage());
    }
  }
  
  public void doLongClick()
  {
       String[] arr = {"Compile project",
                                 "Compile Native Library..."};
        new MessageBox(this, null)
        {
           public void selfBtnListen(MessageBox msg, int which)
           {
	         if (which == 0) 
	            OnClickBtnCompile(null) ;
   	         else if (which == 1)
   	            compileNative();
           }    
        }.choose("Compile", arr);
  }
  
  public void compileNative()
  {
      String[] arr = Native.chooseNativeLib(this, project.buildScriptFile + ".nat");  
      new MessageBox(this, null)
      {
           public void selfBtnListen(MessageBox msg, int which)
           {
              doCompileBinary(msg.items[which]);
           }    
      }.choose("Select Library...", arr);     
  }
  
  private String nativeLibResFile(String libFile)
  {
     String prjPath = FileUtils.extractFilePath(project.buildScriptFile);
     String destPath = prjPath + "/libs/armeabi/";
     String resLib = FileUtils.extractFileName(libFile);
     String resFile = destPath + "lib" + FileUtils.removeExtention(resLib) + ".so";
     FileUtils.forceDirectory(destPath);
     return resFile;
  }
  
  public void doCompileBinary(String fileName)
  {
     try {
         _doCompileBinary(fileName);
     }
     catch (Exception e) {
        new MessageBox(this, null).ShowEmpty(e.getMessage());
     }
  }
  
  public void _doCompileBinary(String fileName)
  {
      if (!(FileUtils.fileExists(fileName)))
      {
         new MessageBox(this, null).ShowEmpty("Can not find file '" + fileName + "'");
         return;
      } 
      Intent newIntent = new Intent().setAction("com.assoft.PascalDevelop.compilePDProject");
      //newIntent.setClassName("com.assoft.PascalDevelop", "MainActivity");
      newIntent.putExtra("android.intent.extra.srcFile", fileName); 
      newIntent.putExtra("android.intent.extra.resFile", nativeLibResFile(fileName));    
      startActivityForResult(newIntent, EditorActions.compileNativePascalLibrary);
  }
  
  private void setEvents()
  {
      View.OnLongClickListener  buttonListener = new View.OnLongClickListener() 
      {
        public boolean onLongClick(View view)
        {
          doLongClick();
          return true;
        }
      };
      View btn = findViewById(R.id.btnCompile);
      btn.setOnLongClickListener(buttonListener);
    }
    
  private void initRes()
  {
     ResUtils.resIcon = R.drawable.assoft;
     ResUtils.resBtnControls = R.id.vwButtons;
     ResUtils.resAndroidManifest = R.raw.androidmanifest;
     ResUtils.resBuildApk = R.raw.buildapk;
     ResUtils.resMain = R.raw.main;
     ResUtils.resMainActivity = R.raw.mainactivity;     
  }
  
    public Boolean highLightSupport()
  {
      return false;
  }
  
  public String stProgramName()
  {
     return "DroidDevelop";
  }
  
  @Override
  protected CommonEditor peekEditor()
  { 
     return editor; 
  }

  @Override
  protected void doChooseAndOpenFile(MessageBox msg, int which, int intParam)
  {
        String fileName = "";
        if (msg.input != null)
           fileName = msg.input.getText().toString();  
        if (which == DialogInterface.BUTTON_POSITIVE)
        {                              
          if (intParam == EditorActions.chooseFileForOpenAct)
            editor.openFile(fileName);
          else if (intParam == EditorActions.chooseFileForOpenProjectAct)
            project.openProject(fileName);
          else if (intParam == EditorActions.runProgramAct)
            project.runProgram(fileName);
        }
        else if (which == DialogInterface.BUTTON_NEUTRAL)
          FileUtils.chooseFileForOpen(msg.parent, intParam, fileName);
  }
    
  private void doCompile()
  {
      editor.taskAskToSave(new Task()
                             {
                                public void execute()
                                {
                                   compiler.runCompile(project.buildScriptFile, EditorActions.compileScriptAct);
                                }
                             }); 
  }

  public void OnClickBtnCompile (final View view)
  {
     if (!(FileUtils.fileExists(project.buildScriptFile)))
     {
       chooseAndOpenFile(EditorActions.chooseFileForOpenProjectAct);
       return;
     }
     doCompile();
  }
  
  public void OnClickBtnRun (final View view)
  {
     String s = project.getRunFileName();
     if (FileUtils.fileExists(s))
        project.runProgram(s);
     else
        chooseAndOpenFile(EditorActions.runProgramAct);
  }
  
  public void OnClickBtnOthers (final View view)
  {
     try
     {
        String[] arr = {"Open last compilation messages...",
                              "Open last compilation log...",
                              "List of class methods...",
                              "Search",
                              "Go to Line...",
                              "Char code...",
                              "Add Native Library..."};
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
   	            editor.search();
   	         else if (which == 4)
   	            editor.gotoLine();
   	         else if (which == 5)
   	            editor.showCharCode();
            else if (which == 6)
               project.addNativeLib();
           }    
        }.choose("Other actions", arr);
      }
      catch  (Exception e) 
      {
         new MessageBox(this, null).ShowEmpty("Unknown error 1");
      }
  }
  
  public void OnClickBtnRefact(final View view)
  {
     refactPlugins.showDlg();  
  }
    
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent)
  {
     try
    {
	if ((requestCode == EditorActions.chooseFileForOpenAct)  && (resultCode == RESULT_OK))
          editor.openFile(FileUtils.FileNameFromIntent(intent));
	if ((requestCode == EditorActions.chooseFileForOpenProjectAct) && (resultCode == RESULT_OK))
	   project.openProject(FileUtils.FileNameFromIntent(intent));
	if ((requestCode == EditorActions.runProgramAct) && (resultCode == RESULT_OK))
	   project.runProgram(FileUtils.FileNameFromIntent(intent));
	if (requestCode == EditorActions.compileScriptAct)
	   compiler.processResult(intent);
      if (requestCode == EditorActions.saveAsFileAct)
          editor.saveAs(FileUtils.FileNameFromIntent(intent));
      if ((requestCode == EditorActions.getRefactItentNamesAct) && (resultCode == RESULT_OK))
         refactPlugins.fillItentNamesArray(intent);
      if ((requestCode == EditorActions.execRefactItentAct) && (resultCode == RESULT_OK))
         refactPlugins.processRefactCommandRes(intent);
      if ((requestCode == EditorActions.getNewProjectItentNamesAct) && (resultCode == RESULT_OK))
         newPrjPlugins.fillItentNamesArray(intent);
      if ((requestCode == EditorActions.execNewProjectItentAct) && (resultCode == RESULT_OK))
         newPrjPlugins.processRefactCommandRes(intent);
      if ((requestCode == EditorActions.getCommonPluginsItentNamesAct) && (resultCode == RESULT_OK))
         pluginsManager.fillItentNamesArray(intent);         
      if ((requestCode == EditorActions.execCommonPluginItentAct) && (resultCode == RESULT_OK))
         pluginsManager.processRefactCommandRes(intent);         
      if ((requestCode == EditorActions.compileNativePascalLibrary)&&(resultCode == RESULT_OK))
         compiler.processNativeLibResult(intent);
     }
     catch (Exception e)
     {
        new MessageBox(this, null).ShowEmpty(e.getMessage());
     }
  }
 
  public boolean onCreateOptionsMenu (Menu menu)
  {
    getMenuInflater().inflate(R.menu.mainmenu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  public boolean onOptionsItemSelected (MenuItem item)
  {
    switch (item.getItemId())
    {
      case R.id.opt_exit:
           editor.taskAskToSave(new Task()
                             {
                                public void execute()
                                {
                                    finish();
                                }
                             }); 
        return true;
      case R.id.opn_prj_new:
        newPrjPlugins.showDlg();  
        //new PrjDialog(this, project).show();
        return true;
      case R.id.opn_prj:
        chooseAndOpenFile(EditorActions.chooseFileForOpenProjectAct);
        return true;
      case R.id.opt_infos:
        About.showInfo (this);
        return true;
      case R.id.opt_prj_opt:
        project.prjOptionsDlg();
        return true;
      case R.id.prj_plugins:
        pluginsManager.showDlg();  
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  } 
} 
