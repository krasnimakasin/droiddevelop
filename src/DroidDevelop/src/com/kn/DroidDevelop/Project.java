package com.kn.DroidDevelop;

import kn.data.PrefsStorage;
import kn.gui.MessageBox;
import kn.data.StringUtils;

import android.app.Activity;

public class Project
{  
   private static int runFileStoreInd = 0;
   
   private PrefsStorage prefs;
   private String[] projectOptions = null;
   private String prjName = "";
   private Activity parent;
   
   public void prjOptionsDlg()
   {
        String[] arr = {"Clear File to Run..."};
        new MessageBox(parent, null)
        {
           public void selfBtnListen(MessageBox msg, int which)
           {
	         if (which == 0) 
	            setRunFileName("");
           }    
        }.choose("Project options", arr);   
   }
   
   private boolean validOptInd(int ind)
   {
      return (projectOptions != null) && (projectOptions.length > ind);
   }
   
   public String getRunFileName()
   {
      if (validOptInd(runFileStoreInd))
         return projectOptions[runFileStoreInd];
      return "";
   }
   
   public void setRunFileName(String value)
   {
      if (!(validOptInd(runFileStoreInd)))
      {
         String[] arr = new String[runFileStoreInd + 1];
         if (projectOptions != null) 
         {
            for (int i = 0; i < projectOptions.length; i++)
               arr[i] = projectOptions[i];
         }
         projectOptions = arr;
      }
      projectOptions[runFileStoreInd] = value;
      flushOptions();
   }
   
   public void readOptions(String fileName)
   {
     try
     {
        prjName = fileName;
        String prjOptStr = prefs.getPref("prjOpt" + prjName);
        projectOptions = StringUtils.toArray(prjOptStr, ";", 100);
      }
      catch  (Exception e) 
      {
         new MessageBox(parent, null).ShowEmpty("Read project options error");
      }
   }
   
   public void flushOptions()
   {
      if (prjName.compareTo("") == 0)
         return;      
      String s = "qqq;";
      for (int i = 0; i < projectOptions.length; i++)
         s += projectOptions[i] + ";";
      prefs.setPref("prjOpt" + prjName, s);
   }
   
   public Project(Activity aParent, PrefsStorage aPrefs)
   {
      prefs = aPrefs;
      parent = aParent;
   }
}