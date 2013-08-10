package com.assoft.DroidDevelop;

import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ScrollView;
import android.app.Activity;
import android.view.View;
import android.content.Intent;

import kn.gui.MessageBox;
//import kn.utils.Plugins;
//import kn.data.FileUtils;

public class PluginPanel extends LinearLayout
{
   private Button close;
   private Activity parent;
   private Intent intent;
   private Project project;
   public String panelName;
   
   private Boolean closeAdded = false;
   
   private class PluginPanelButton extends Button
   {
      public String name;
      
      public PluginPanelButton(Activity aParent, String aName)
      {
         super(aParent);
         name = aName;
      }
   }

   public PluginPanel(Activity aParent, Intent anIntent, String aPanelName, Project aProject)
   {
      super(aParent);        
      parent = aParent;             
      intent = anIntent;  
      panelName = aPanelName;
      project = aProject;
                   
      close = new Button(aParent);
      close.setText("Close"); 
      close.setOnClickListener(new View.OnClickListener() 
                   { public void onClick(View v) 
                   {hide();} });                                 
      hide();
   }
   
   private void processButtonCmd(String btnName)
   {
     try 
     {  
        PluginsManager.execute(btnName, intent.getComponent().getPackageName(), 
                   intent.getComponent().getClassName(), project, parent);
     }
     catch (Exception e) 
     {
        new MessageBox(parent, null).ShowEmpty("Error btn: " + e.getMessage());
     }   
   }
   
   public void addPluginButton(String aName, String aCaption)
   {
      PluginPanelButton btn = new PluginPanelButton(parent, aName);
      btn.setOnClickListener(new View.OnClickListener() 
                   { public void onClick(View v) 
                   {processButtonCmd(((PluginPanelButton)v).name);} });                  
      btn.setText(aCaption);                     
      addView(btn);
   }
   
   public void addPluginText(String aText)
   {
      TextView t = new TextView(parent);
      ScrollView s = new ScrollView(parent);
      t.setText(aText);
      s.addView(t);
      addView(s);
   }

   public void hide()
   {
       setVisibility(8); //gone   
   }   
   
   public void show()
   {
      if (!(closeAdded))
      {
         closeAdded = true;
         addView(close);      
      }
      setVisibility(0); //visible           
   }
}