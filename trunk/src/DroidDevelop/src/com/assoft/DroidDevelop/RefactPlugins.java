package com.assoft.DroidDevelop;

import android.app.Activity;
import android.content.Intent;
import android.widget.EditText;
import android.text.Editable;
import kn.gui.MessageBox;
import kn.utils.Formatter;
import kn.utils.Plugins;
import kn.ide.EditorActions;

public class RefactPlugins extends Plugins
{
   private EditText tv; 
      
   private String[] mainRefactItentNamesArray = {"Add try/catch Block for Selection"};       
   
  public RefactPlugins(Activity aParent, EditText aTv)
  {
     super(aParent);
     addArray(mainRefactItentNamesArray, null);
     tv = aTv;
  }
  
  public void processRefactCommandRes(Intent intent)
  {
     try 
     {
        String s = intent.getData().toString();
         tv.setText(s);         
        int start = intent.getIntExtra("android.intent.extra.SelectionStart", 0);
        int end = intent.getIntExtra("android.intent.extra.SelectionEnd", 0);         
         tv.setSelection(start, end);
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
	 Formatter.trycatch(parent, tv);     
     if (index < mainRefactItentNamesArray.length) 
        return;
     try 
     {
        Editable text = tv.getEditableText();
        int start = tv.getSelectionStart();
        int end = tv.getSelectionEnd();
        if (start > end)
        {
           int temp = start;
           start = end;
           end = temp;
        }
        Intent newIntent = new Intent().setAction("com.assoft.DroidDevelop.processRefactCommand");
        newIntent.setClassName(intent.getStringExtra(pkgName), intent.getStringExtra(clsName));
        newIntent.putExtra("android.intent.extra.Name", name);
        newIntent.putExtra("android.intent.extra.Text", text.toString());
        newIntent.putExtra("android.intent.extra.SelectionStart", start);
        newIntent.putExtra("android.intent.extra.SelectionEnd", end);
        parent.startActivityForResult(newIntent, EditorActions.execRefactItentAct);  
     }
     catch (Exception e) 
     {
        new MessageBox(parent, null).ShowEmpty("Error " + e.getMessage());
     }
  }
 
  @Override
  protected String getNamesItentName()
  {
     return "com.assoft.DroidDevelop.getRefactCommandsNames";
  }
 
  @Override
  protected int getNamesItentNameAct()
  {
     return EditorActions.getRefactItentNamesAct;
  }
}