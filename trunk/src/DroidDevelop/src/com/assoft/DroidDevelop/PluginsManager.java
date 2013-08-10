package com.assoft.DroidDevelop;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.content.DialogInterface;

import kn.gui.MessageBox;
import kn.utils.Plugins;
import kn.utils.MarketUtils;
import kn.data.FileUtils;
import kn.gui.Dialogs;
import kn.data.TextFiles;
import kn.gui.JavaMemo;
import kn.ide.EditorActions;

public class PluginsManager extends Plugins
{     
   private Project project;
   private String[] mainRefactItentNamesArray = {"Plugins Manager..."};       
   
   public PluginsManager(Activity aParent, Project aProject)
   {
      super(aParent);
      project = aProject;
      addArray(mainRefactItentNamesArray, null);
   }
  
   private static String[] pluginNames = {
       "DDPluginsPack",
       "DDRefuctoringPluginExample",       
       "DDNewProjectPluginExample"
       };
       
    private static String[] packageNames = {
       "com.assoft.DDPluginsPack",
       "com.assoft.DDRefactoringPluginDemo",
       "com.assoft.DDNewProjectPluginExample"
       };

/*       
    private static String[] packageClasses = {
       "DDRefactoringPluginDemo",
       "DDNewProjectPluginExample"
       };       
*/

    public static void askToInstall(Activity parent, String programName, String pack)
    {
       MarketUtils.askToInstall(parent, programName, pack);
    }
       
   private static Boolean pluginInstalled(Activity parent, String pluginPackage, String pluginClass)
   {
        return MarketUtils.isInstalled(parent, pluginPackage, "MainActivity");
   }
   
   private static void removeApp(Activity parent, String appPackage, String programName)
   {
      MessageBox msg = new MessageBox(parent, null)
      {
         public void selfBtnListen(MessageBox msg, int which)
         {    
            if (which == DialogInterface.BUTTON_POSITIVE)
               try {
                   Intent intent = new Intent(Intent.ACTION_DELETE);                
                   intent.setData(Uri.parse("package:" + msg.strParam));                   
                   parent.startActivity(intent);
               }
               catch (Exception e) {
                 new MessageBox(parent, null).ShowEmpty("Error: " + e.getMessage());
               }
         }
      };
      msg.strParam = appPackage;
      msg.Ask("Do you want to remove " + programName + "?");       
   }
   
   private static String appPackageByPluginName(String pluginName)
   {
      for (int i = 0; i < pluginNames.length; i++)
        if (pluginName.compareTo(pluginNames[i]) == 0)
          return packageNames[i];
      return "";
   }
   
   private static void showPluginsList(Activity parent, Boolean isInstalled)
   {
      Boolean[] installed  = new Boolean[pluginNames.length];
      for (int i = 0; i < pluginNames.length; i++)
         installed[i] = pluginInstalled(parent, packageNames[i], "");//packageClasses[i]);
      int n = 0;
      for (int j = 0; j < pluginNames.length; j++)
         if (isInstalled == installed[j])
            n++;
      String[] names = new String[n];
      n = 0;
      for (int k = 0; k < pluginNames.length; k++)
      {
         if (isInstalled == installed[k])
         {
            names[n] = pluginNames[k];
            n++;
         }
      }
      MessageBox msg = new MessageBox(parent, null)
      {
         public void selfBtnListen(MessageBox msg, int which)
         {
            if (boolParam)
              removeApp(parent, appPackageByPluginName(items[which]), items[which]);
            else
              askToInstall(parent, items[which], appPackageByPluginName(items[which]));
         }    
      };
      msg.boolParam = isInstalled;
      if (isInstalled)
        msg.choose("Installed plugins", names);           
      else
        msg.choose("Available plugins", names);           
   }
   
   private Editor editor()
   {
     return (Editor)project.editor;
   }
   
   private PluginPanel [] pluginPanels = new PluginPanel[0];
   
   private void addPanel(Intent intent, String panelName)
   {  
     PluginPanel [] panels = new PluginPanel[pluginPanels.length + 1];     
     PluginPanel panel = editor().addPanel(intent, panelName);
     panels[0] = panel;
     for (int i = 0; i < pluginPanels.length; i++)
        panels[i + 1] = pluginPanels[i];
     pluginPanels = panels;
   }
   
   private PluginPanel getPanelByName(String panelName)
   {
      for (int i = 0; i < pluginPanels.length; i++)
        if (pluginPanels[i].panelName.compareTo(panelName) == 0)
           return pluginPanels[i];
      new MessageBox(parent, null).ShowEmpty("Error: can not found panel ''" + panelName + "''");       
      return null;
   }
   
   private void removePanel(String panelName)
   {
      int n = 0;
      for (int i = 0; i < pluginPanels.length; i++)
        if (pluginPanels[i].panelName.compareTo(panelName) != 0)      
           n++;
      PluginPanel [] panels = new PluginPanel[n];         
      n = 0;        
      for (int i = 0; i < pluginPanels.length; i++)
      {
        if (pluginPanels[i].panelName.compareTo(panelName) != 0)      
        {
           panels[n] = pluginPanels[i];
           n++;           
        }
        else           
           editor().removePanel(pluginPanels[i]);
      }
      pluginPanels = panels;
   }
   
   private void showPanel(String panelName)
   {
      PluginPanel panel = getPanelByName(panelName);
      if (panel != null)
        panel.show();        
   }
   
   private void addPanelButton(String btnName, String btnCaption)
   {
      if (pluginPanels.length == 0)
        return;
      PluginPanel panel = pluginPanels[0];
      if (panel != null)
        panel.addPluginButton(btnName, btnCaption);
   }
   
   private void addPanelText(String aText)
   {
      if (pluginPanels.length == 0)
        return;
      PluginPanel panel = pluginPanels[0];
      if (panel != null)
        panel.addPluginText(aText);   
   }
   
  private void processCommand(String commandName, String commandData, String commandData2, Intent intent)
  {
     JavaMemo memo = (JavaMemo)project.editor.tv;
     if (commandName.compareTo("InsertText") == 0)
        project.editor.insertTextToCurrent(commandData);
     else if (commandName.compareTo("HighlightText") == 0)
        memo.setBackGroundHighlight(-5111901, Integer.parseInt(commandData), Integer.parseInt(commandData2));
     else if (commandName.compareTo("ClearHighlight") == 0)
        memo.removeAllBackGroundHighlight();        
     else if (commandName.compareTo("AddPanel") == 0)
       addPanel(intent, commandData);
     else if (commandName.compareTo("ShowPanel") == 0)
       showPanel(commandData);
     else if (commandName.compareTo("RemovePanel") == 0)
       removePanel(commandData);       
     else if (commandName.compareTo("AddLastPanelButton") == 0)
       addPanelButton(commandData, commandData2);
     else if (commandName.compareTo("AddLastPanelText") == 0)
       addPanelText(commandData);      
     else if (commandName.compareTo("ShowText") == 0)
       //Dialogs.showText(parent, commandData, "Plug-in message", true);    
       MessageBox.showEmptyOk(parent, "Plug-in Message", commandData);
     else if (commandName.compareTo("OpenFile") == 0)
       project.editor.openFile(commandData, false);           
     else if (commandName.compareTo("CreateProject") == 0)
       project.newProject(FileUtils.pathToSDCard() + "assoft/" + commandData, "com.assoft", commandData);               
  }
  
/*  
  public void removeAllBackGroundHighlight()
  */
  
  public void processRefactCommandRes(Intent intent)
  {
     try 
     {   
         int commandsCount = intent.getIntExtra("android.intent.extra.CommandsCount", 0);     
         for (int i = 0; i < commandsCount; i++)
         {
            String commandName = intent.getStringExtra("android.intent.extra.CommandName" + Integer.toString(i));
            String commandData = intent.getStringExtra("android.intent.extra.CommandData" + Integer.toString(i));
            String commandData2 = intent.getStringExtra("android.intent.extra.CommandDataA" + Integer.toString(i));
            processCommand(commandName, commandData, commandData2, intent);
         }
      }
      catch (Exception e) 
      {
         new MessageBox(parent, null).ShowEmpty("Error" + e.getMessage());
      }
  }
  
    private void showPluginsHelp()
    {
       String h = TextFiles.readTextFileFromRaw(parent, R.raw.plugins_help);
       Dialogs.showText(parent, h, "Plugins Help", true);    
    }
  
   private void managePlugins()
   {
        String[] arr = {"Installed...",
                                 "Available...",
                                 "Help..."};
        new MessageBox(parent, null)
        {
           public void selfBtnListen(MessageBox msg, int which)
           {
               if (which == 0) 
	            showPluginsList(parent, true);
               else if (which == 1)
                   showPluginsList(parent, false);  
   	         else if (which == 2)
   	            showPluginsHelp();
           }    
        }.choose("Plugins Manager", arr);
   }
   
   public static void execute(String cmdName, String packageName, String className, Project aProject, Activity aParent)
   {
     try 
     {
        Intent newIntent = new Intent().setAction("com.assoft.DroidDevelop.processCommonPluginCommand");
        newIntent.setClassName(packageName, className);
        newIntent.putExtra("android.intent.extra.Name", cmdName);
        newIntent.putExtra("android.intent.extra.BuildFile", aProject.buildScriptFile);
        newIntent.putExtra("android.intent.extra.ProjectPath", FileUtils.extractFilePath(aProject.buildScriptFile));
        newIntent.putExtra("android.intent.extra.CurrentEditFile", aProject.editor.currentEditFile);

        int start = aProject.editor.tv.getSelectionStart();
        int end = aProject.editor.tv.getSelectionEnd();
        if (start > end)
        {
           int temp = start;
           start = end;
           end = temp;
        }        
        newIntent.putExtra("android.intent.extra.SelectionStart", start);
        newIntent.putExtra("android.intent.extra.SelectionEnd", end);
        
        aParent.startActivityForResult(newIntent, EditorActions.execCommonPluginItentAct);  
     }
     catch (Exception e) 
     {
        new MessageBox(aParent, null).ShowEmpty("Error " + e.getMessage());
     }   
   }

  @Override
  public void processRefactItemQuery(int index, String name, Intent intent)
  {
      if (index == 0) 
	   managePlugins();
     if (index < mainRefactItentNamesArray.length) 
        return;
     execute(name, intent.getStringExtra(pkgName), intent.getStringExtra(clsName), project, parent);
  }
 
  @Override
  protected String getNamesItentName()
  {
     return "com.assoft.DroidDevelop.getCommonPluginCommandsNames";
  }
 
  @Override
  protected int getNamesItentNameAct()
  {
     return EditorActions.getCommonPluginsItentNamesAct;
  }
}