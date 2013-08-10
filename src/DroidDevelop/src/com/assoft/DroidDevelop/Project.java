package com.assoft.DroidDevelop;

import kn.data.PrefsStorage;
import kn.data.FileUtils;
import kn.data.RecentFiles;
import kn.data.TextFiles;
import kn.ide.BaseProject;
import kn.ide.ResUtils;
import kn.ide.EditorActions;
import java.io.*;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Project extends BaseProject
{  
         
   public DDLearnProject curPrj = null;
   
   public String buildScriptFile = "";
   
   public void addNativeLib()
   {
      Native.addNativeLib(parent, buildScriptFile + ".nat");
   }
   
   private void copyTextFileFromRaw(int resID, String resFile, String projectPackage, String projectName)
   {
      String str = TextFiles.readTextFileFromRaw(parent, resID);
      
      String AndroidJarPath = FileUtils.AssoftRoot() + "android.jar";
      if (!(FileUtils.fileExists(AndroidJarPath)))
         AndroidJarPath = "";
      str = str.replace("%%androidJarPath%%", AndroidJarPath);
      
      str = str.replace("%%package%%", projectPackage).replace("%%programName%%", projectName);
      TextFiles t = new TextFiles(parent);
      t.showToast = false;
      t.saveFile(resFile, str);
   }

   private void copyLogoFile(String outFile)
   {
   try {
         Bitmap b = BitmapFactory.decodeResource(parent.getResources(), ResUtils.resIcon);
         File f = new File(outFile);       
         OutputStream out = new BufferedOutputStream(new FileOutputStream(f));       
         b.compress(Bitmap.CompressFormat.PNG, 100, out);
         out.flush();
         out.close();
      }
      catch (Exception e) {
      }
   }
   
   public void openNewProject(String aProjectPath, String projectPackage, String projectName)
   {
      String projectPath = aProjectPath + "/";
      String pkgPath = projectPackage.replace(".", "/") + "/";  
      openProject(projectPath + "buildApk.bsh");
      setRunFileName(projectPath + "out/test/" + projectName + "/" + projectName + ".apk");
      editor.openFile(projectPath + "src/" + pkgPath + projectName + "/MainActivity.java");
   }
   
   public void newProject(String aProjectPath, String projectPackage, String projectName)
   {
     ProgressDialog progressDialog = new ProgressDialog(parent);
     progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
     progressDialog.setTitle("New project creating");
     progressDialog.setIndeterminate(true);
     progressDialog.show();
     try
     {
       String projectPath = aProjectPath + "/";
       String pkgPath = projectPackage.replace(".", "/") + "/";
       FileUtils.forceDirectory(projectPath + "gen/" + pkgPath + projectName);
       FileUtils.forceDirectory(projectPath + "libs");
       FileUtils.forceDirectory(projectPath + "out/test/" + projectName);
       FileUtils.forceDirectory(projectPath + "res/drawable-hdpi"); 
       FileUtils.forceDirectory(projectPath + "res/drawable-ldpi"); 
       FileUtils.forceDirectory(projectPath + "res/drawable-mdpi"); 
       FileUtils.forceDirectory(projectPath + "res/layout"); 
       FileUtils.forceDirectory(projectPath + "res/menu"); 
       FileUtils.forceDirectory(projectPath + "res/values"); 
       FileUtils.forceDirectory(projectPath + "src/" + pkgPath + projectName);
       copyTextFileFromRaw(ResUtils.resAndroidManifest, projectPath + "AndroidManifest.xml", projectPackage, projectName);
       copyTextFileFromRaw(ResUtils.resBuildApk, projectPath + "buildApk.bsh", projectPackage, projectName);
       copyTextFileFromRaw(ResUtils.resMain, projectPath + "res/layout/main.xml", projectPackage, projectName);
       copyTextFileFromRaw(ResUtils.resMainActivity, projectPath + "src/" + pkgPath + projectName + "/MainActivity.java", projectPackage, projectName);
       copyLogoFile(projectPath + "res/drawable-hdpi/icon.png");
       //copyFileFromRaw(Resources.R.drawable.assoft, projectPath + "res/drawable-mdpi/icon.png");
       //copyFileFromRaw(Resources.R.drawable.assoft, projectPath + "res/drawable-ldpi/icon.png");
       openNewProject(aProjectPath, projectPackage, projectName);
     }
     finally
     {
       progressDialog.dismiss();
     }
   }
  
   public void runProgram(String fileName)
   {
      TextFiles t = new TextFiles(parent);
      recentFiles.setLastOpenFile(EditorActions.runProgramAct, fileName);
      setRunFileName(fileName);
      t.execute(fileName);
   }
   
  public Boolean openLearnProject(String fileName)
  {
    curPrj = null;
    DDLearnProject prj = null;
    if (!(FileUtils.extractExtention(fileName).contentEquals("ddl")))   
       return false;
    try
    {
        prj = new DDLearnProject(parent, fileName);
    }
    catch (Exception e) 
    {
        prj = null;
    } 
      if (prj !=  null)
      {
        String path = FileUtils.AssoftRoot() + prj.prjName;
        newProject(path, "com.assoft", prj.prjName);       
      }  
    curPrj = prj;   
    return (curPrj  != null);
  }
  
  public void openPrjFromRes(InputStream resStrm, String prjName)
  {
      DDLearnProject prj = new DDLearnProject(parent, resStrm, prjName);
      String path = FileUtils.AssoftRoot() + prjName;
      newProject(path, "com.assoft", prjName);   
      curPrj = prj;
  }
   
  public void openProject(String fileName)
  {
      if (fileName.compareTo("") == 0)
         return;
      if (openLearnProject(fileName))
         return;
      buildScriptFile = fileName;
      Toast.makeText(parent, "Opening " + fileName + " project", 2000).show();
      recentFiles.setLastOpenFile(EditorActions.chooseFileForOpenProjectAct, fileName);
      readOptions(fileName);
      String s = getLastOpenFile();
      if (FileUtils.fileExists(s))
        editor.openFile(s);
  }
  
   public Project(Activity aParent, PrefsStorage aPrefs, RecentFiles aRecentFiles, BaseEditor anEditor)
   {
      super(aParent, aPrefs, aRecentFiles, anEditor);
   }
}
