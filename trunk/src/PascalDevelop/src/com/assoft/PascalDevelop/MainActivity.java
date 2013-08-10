//com.android.internal.os.ZygoteInit$MethodAndArgsCaller
//org.eclipse.debug.internal.core.LaunchManager
package com.assoft.PascalDevelop;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.content.DialogInterface;
import android.os.Bundle;
import android.net.Uri;

import kn.gui.MessageBox;
import kn.gui.CommonEditor;
import kn.gui.Dialogs;
import kn.data.FileUtils;
import kn.utils.Task;
import kn.utils.Ads;
import kn.ide.IDEActivity;
import kn.ide.BaseProject;
import kn.ide.ResUtils;
import kn.ide.EditorActions;

import com.assoft.PascalDevelop.R;
import com.assoft.PascalDevelop.Memo;

import java.io.*;
import java.util.zip.*;
//import java.util.concurrent.TimeUnit;

import com.google.ads.AdView;

public class MainActivity extends IDEActivity
{
  private CommonEditor editor;
  private BaseProject project;
  
  private Boolean needAds()
  {
     return true;
  }
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    try
    {
      super.onCreate(savedInstanceState);
      tv = new Memo(this, R.styleable.View);  
      setContentView(R.layout.main);
      if (needAds())
         addAds("a1508ff456df5d1");
      ViewGroup scr = (ViewGroup)findViewById(R.id.vwEdit);
      Memo.highLight = true;          
      editor = new CommonEditor(this, tv, scr, recentFiles);
      project = new BaseProject(this, prefs, recentFiles, editor);
      project.settings.initFirst(false, 12, false);      
      initRes();
      if (processIntent())
        finish();
    }
    catch (Exception e)
    {
       new MessageBox(this, null).ShowEmpty(e.getMessage());
    }
  }
  
  public Boolean processIntent()
  {
    Intent I = getIntent();
    if (I == null)
       return false;

    if (I.getAction().contentEquals("com.assoft.PascalDevelop.compilePDProject"))
    {
       String file = I.getStringExtra("android.intent.extra.srcFile");
       String resFile = I.getStringExtra("android.intent.extra.resFile");
       String params = file + " -Mdelphi -vi -XX -Fu" + unitsPath() + " -o" + resFile;
       String res = doRunFPC(params);
       I.setData(Uri.parse(res)); 
       setResult(RESULT_OK, I);
       return true;
    }
    return false;
  }
   
  @Override 
  protected CommonEditor peekEditor()
  {  
    return editor;
  }
  
  @Override
  protected BaseProject peekProject()
  {
    return project;
  }
  
  private  AdView adView;
	
  private void addAds(String pubId)
  {
       ViewGroup g = (ViewGroup)findViewById(R.id.vwAds);
       adView = Ads.addAds(this, g, pubId);
  }
  
   public void onDestroy() 
	{ 
		adView.stopLoading(); 
		super.onDestroy(); 
	} 
  
  private void initRes()
  {
     ResUtils.resIcon = R.drawable.assoft;
     ResUtils.resBtnControls = R.id.vwButtons;
     //ResUtils.resAndroidManifest = R.raw.androidmanifest;
     //ResUtils.resBuildApk = R.raw.buildapk;
     //ResUtils.resMain = R.raw.main;
     //ResUtils.resMainActivity = R.raw.mainactivity;     
  }
  
  public String stProgramName()
  {
     return "PascalDevelop";
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
        }
        else if (which == DialogInterface.BUTTON_NEUTRAL)
          FileUtils.chooseFileForOpen(msg.parent, intParam, fileName);
  }
  
  private File copyExecutable(String Name, int resId)
  {
     File res = getDir("files", 1);//getExternalFilesDir(null);
     File destFile = new File(res, Name);
     if (destFile.exists())
        return destFile;   
     FileUtils.copyFileFromRaw(this, resId, destFile);
     destFile.setExecutable(true, false);
     return destFile;     
  }
  
  private void copyUnits()
  {
    try {
    String destPath = unitsPath(); 
    InputStream source = getResources().openRawResource(R.raw.units);
    ZipInputStream zis = new ZipInputStream(source);
    try
    {
      ZipEntry ze;
      new File(destPath).mkdirs();
      while ((ze = zis.getNextEntry()) != null)
      {
         String filename = ze.getName(); 
         File resFile = new File(destPath, filename);
         if (resFile.exists())
         {
           continue;
         }
         ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
         byte[] buffer = new byte[1024]; 
         int count; 
         while ((count = zis.read(buffer)) != -1) 
         {
            baos.write(buffer, 0, count); 
         }        
         byte[] bytes = baos.toByteArray();
         OutputStream out = new BufferedOutputStream(new FileOutputStream(resFile));
         out.write(bytes);
         out.close();         
      }     
    }
    finally { zis.close(); }   
    }
    catch (Exception e) {
    }
  }
   
  private File copyFPC()
  {
     copyUnits();
     copyExecutable("as", R.raw.as);
     copyExecutable("ld", R.raw.ld);
     //copyExecutable("libc.a");
     return copyExecutable("ppcarm", R.raw.ppcarm);     
  }
  
  private String doRunFPC(String params)
  {
    try 
    {
        File file = copyFPC();
        Process p = Runtime.getRuntime().exec(file.getPath() + " " + params);
        InputStream resStrm = p.getInputStream();
        p.waitFor();
        return readText(resStrm);
     }
     catch (Exception e) {
       return "error\n" + e.getMessage();
     }       
  }
  
  private void runFPC(String params)
  {
      String s = doRunFPC(params);
      showText(s);
  }
  
  private String readText(InputStream resStrm)
  {
     String text = "";
     if (resStrm != null)
     {
       try {
          final byte[] bytes = new byte[resStrm.available()];
           resStrm.read(bytes);
           text = new String(bytes);
        
           text = "res:\n" + text;
        }
        catch (Exception e) {
           text = "error:\n" + e.getMessage();
        }
      }
      return text;
  }
  
  private void showText(String s)
  {
     Dialogs.showText(this, s, "res", false);  
  }
  
  private String unitsPath()
  {
    return FileUtils.AssoftRoot() + "PascalDevelop/units/";
  }
  
  private void runCompile(String file, int action)
  {
     String resFile = getDir("files", 1).getPath() + "/pasApp";
     runFPC(file + " -Mdelphi -vi -XX -Fu" + unitsPath() + " -o" + resFile);
  }
  
  private void doCompile()
  {
      editor.taskAskToSave(new Task()
                             {
                                public void execute()
                                {
                                   runCompile(editor.currentEditFile, EditorActions.compileScriptAct);
                                }
                             }); 
  }
  
  public void OnClickBtnCompile (final View view)
  {
     if (!(FileUtils.fileExists(editor.currentEditFile)))
       peekEditor().save(true);
     //get(500, TimeUnit.MILLISECONDS);
     if (!(FileUtils.fileExists(editor.currentEditFile)))
       return;
     /*
     {
       chooseAndOpenFile(EditorActions.chooseFileForOpenProjectAct);
       return;
     }
     */
     doCompile();
  }
  
  private void testApp(String file)
  {
     new PascalAppExecuter(this, file, needAds());
  }
  
  private void runProgram(String file)
  {
    String s = "";
    try 
    {
        File f = new File(file);
        f.setExecutable(true, false);
        testApp(file);
     }
     catch (Exception e) {
       s = "error\n" + e.getMessage();
       showText(s);
     }       
  }
  
  public void OnClickBtnRun (final View view)
  {
     String s = getDir("files", 1).getPath() + "/pasApp";
     if (FileUtils.fileExists(s))
        runProgram(s);
     else
        new MessageBox(this, null).ShowEmpty("Please compile at first");
        //chooseAndOpenFile(EditorActions.runProgramAct);
  }
  
  public void OnClickBtnOthers (final View view)
  {
     try
     {
        String[] arr = {"Search",
                              "Go to Line...",
                              "Char code..."};
        new MessageBox(this, null)
        {
           public void selfBtnListen(MessageBox msg, int which)
           {
             if (which == 0)
   	            editor.search();
   	         else if (which == 1)
   	            editor.gotoLine();
   	         else if (which == 2)
   	            editor.showCharCode();
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
     new MessageBox(this, null).ShowEmpty("Not available in current version");
     //refactPlugins.showDlg();  
  }
    
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent)
  {
     try
    {
	   if ((requestCode == EditorActions.chooseFileForOpenAct)  && (resultCode == RESULT_OK))
          editor.openFile(FileUtils.FileNameFromIntent(intent));
      if (requestCode == EditorActions.saveAsFileAct)
          editor.saveAs(FileUtils.FileNameFromIntent(intent));
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
  
  @Override
   public void newFile()
  {
     super.newFile();
  	  peekEditor().tv.append("program PascalApp;\n\nuses\n  pd_console;\n\nbegin\n\nend.");
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
        /*
      case R.id.opn_prj_new:
        newPrjPlugins.showDlg();  
        //new PrjDialog(this, project).show();
        return true;
      case R.id.opn_prj:
        chooseAndOpenFile(EditorActions.chooseFileForOpenProjectAct);
        return true;
        */
      case R.id.opt_infos:
        About.showInfo (this);
        return true;      
      case R.id.opt_prj_opt:
        project.prjOptionsDlg();
        return true;  
        /*  
      case R.id.prj_plugins:
        pluginsManager.showDlg();  
        return true;
        */
      default:
        return super.onOptionsItemSelected(item);
    }
  } 
} 
