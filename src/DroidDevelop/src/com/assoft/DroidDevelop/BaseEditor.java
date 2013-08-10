package com.assoft.DroidDevelop;

//import android.widget.TextView;
import android.widget.EditText;
import android.app.Activity;
import android.view.ViewGroup;

import kn.gui.CommonEditor;
import kn.data.FileUtils;
import kn.data.RecentFiles;

//import com.assoft.DroidDevelop.Actions;

public class BaseEditor extends CommonEditor
{
   protected Project project;
   
   public BaseEditor(Activity aparent, EditText atv, ViewGroup aPlace, RecentFiles aRecentFiles)
   {
      super(aparent, atv, aPlace, aRecentFiles);
   }

   public void setProject(Project aProject)
   {
      project = aProject;   
   }

  @Override
  public void setLastOpenFile(String fileName)
  {
     project.setLastOpenFile(fileName);
     Memo.forceHighlight = FileUtils.extractFileName(fileName).contentEquals("MainActivity.java");    
  }

@Override
 public void doCloseFile() 
{
    Memo.forceHighlight = false;
    super.doCloseFile();
 }
}
