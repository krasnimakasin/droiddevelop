package com.assoft.DroidDevelop;

import android.app.Activity;
import android.content.Intent;
import kn.gui.MessageBox;
import kn.utils.Plugins;
import kn.ide.EditorActions;

public class NewPrjPlugins extends Plugins
{      
  private String[] mainRefactItentNamesArray = {"New Empty"};       
  
  private Project project;
   
  public NewPrjPlugins(Activity aParent, Project aProject)
  {
     super(aParent);
     addArray(mainRefactItentNamesArray, null);
     project = aProject;
  }
  
  public void processRefactCommandRes(Intent intent)
  {
     try 
     {        
        String path = intent.getStringExtra("android.intent.extra.ProjectPath");
        String pack = intent.getStringExtra("android.intent.extra.ProjectPackage");         
        String name = intent.getStringExtra("android.intent.extra.ProjectName");         
        project.openNewProject(path, pack, name);   
      }
      catch (Exception e) 
      {
         new MessageBox(parent, null).ShowEmpty("Error " + e.getMessage());
      }
  }

  @Override
  public void processRefactItemQuery(int index, String name, Intent intent)
  {
     if (index == 0) 
	 new PrjDialog(parent, project).show();
     if (index < mainRefactItentNamesArray.length) 
        return;
     try 
     {
        Intent newIntent = new Intent().setAction("com.assoft.DroidDevelop.processNewProjectCommand");
        newIntent.setClassName(intent.getStringExtra(pkgName), intent.getStringExtra(clsName));
        newIntent.putExtra("android.intent.extra.Name", name);
        parent.startActivityForResult(newIntent, EditorActions.execNewProjectItentAct);  
     }
     catch (Exception e) 
     {
        new MessageBox(parent, null).ShowEmpty("Error " + e.getMessage());
     }
  }
 
  @Override
  protected String getNamesItentName()
  {
     return "com.assoft.DroidDevelop.getNewProjectCommandsNames";
  }
 
  @Override
  protected int getNamesItentNameAct()
  {
     return EditorActions.getNewProjectItentNamesAct;
  }
}