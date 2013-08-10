package com.assoft.DroidDevelop;

import android.content.Intent;
import android.app.Activity;
import android.widget.EditText;
import android.view.ViewGroup;

import kn.data.RecentFiles;

//import com.assoft.DroidDevelop.Actions;

public class Editor extends BaseEditor
{
   public Editor(Activity aparent, EditText atv, ViewGroup aPlace, RecentFiles aRecentFiles)
   {
     super(aparent, atv, aPlace, aRecentFiles);
   }
   
   public PluginPanel addPanel(Intent anIntent, String panelName)
   {
     PluginPanel panel = new PluginPanel(parent, anIntent, panelName, project);
     panelsGroup.addView(panel);
     return panel;
   }
   
   public void removePanel(PluginPanel panel)
   {
      panelsGroup.removeView(panel);
   }
   
}