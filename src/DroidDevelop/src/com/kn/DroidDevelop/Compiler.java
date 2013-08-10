package com.kn.DroidDevelop;

import android.app.Activity;
import android.content.Intent;
import android.content.ComponentName;
import android.os.Bundle;
import android.content.DialogInterface;
import android.widget.Toast;

import kn.gui.MessageBox;
import kn.gui.Dialogs;
import kn.outside.GetClassRef;

public class Compiler
{
   private Activity parent;
   private Editor editor;
   
   private String lastCompilationText = "";
   private int lastCompilationRes = 0;
   
   public Compiler(Activity aParent, Editor anEditor)
   {
      parent = aParent;
      editor = anEditor;
   }
   
   private void doGetClassRef(String className)
   {
      String[] res = GetClassRef.main(className);
      new MessageBox(parent, null).choose(className, res);
   }
   
   public void getClassRef()
   {
      new MessageBox(parent, null)
     {
        public void selfBtnListen(MessageBox msg, int which)
        {
           if ((msg.input != null) && (which == DialogInterface.BUTTON_POSITIVE))
           {
              String className = msg.input.getText().toString();
              doGetClassRef(className);
           }
        }    
     }.input("Class name", "android.app.Activity");   
   }
   
    public void processResult(Intent intent)
    {
	   Bundle extras = intent.getExtras();
	   if (extras != null)
	   {
	      lastCompilationRes = extras.getInt("android.intent.extra.ScriptResultCode", -1);
	      lastCompilationText = extras.getString("android.intent.extra.ResultText");	    
	      openLastCompilationMessages();
	   } 
    }

    public void showLastCompilationMessages()
    {
        if (!(doShowLastCompilationMessages()))
   	       new MessageBox(parent, null).ShowEmpty("There are no any messages"); 
    }
    
    public void showLastCompilationText()
    {
	    Dialogs.showText(parent, lastCompilationText, compResCaption(), false);         
    }
    
    private String compResStr(int compRes)
    {
       if (compRes == 99)
          return "Script does not exist";
       if (compRes == 0)
          return "Script succes";
       if (compRes == 2)
          return "Script fail";
       else
          return Integer.toString(compRes);
    }
   
    public void doRunCompile(String fileName, int Act)
    {
       if (parent == null)
         return;
       ComponentName cn = new ComponentName("com.t_arn.JavaIDEdroid", "com.t_arn.JavaIDEdroid.MainActivity");
       Intent intent = new Intent("android.intent.action.SEND");
       intent.setComponent(cn);
       intent.putExtra("android.intent.extra.ScriptPath", fileName);
       intent.putExtra("android.intent.extra.ScriptAutoRun", true);
       intent.putExtra("android.intent.extra.ScriptAutoExit", true);
       intent.putExtra("android.intent.extra.WantResultText", true);
       parent.startActivityForResult(intent, Act);
   }

   public void runCompile(String fileName, int Act)   
   {
      try
      {
         Toast.makeText(parent, "Compiling " + fileName + " project", 2000).show();
         doRunCompile(fileName, Act);
      }     
      catch (Exception e)
      {
          new MessageBox(parent, null).ShowEmpty("Please install JavaIDEDroid");
      }
   }
   
   private String compResCaption()
   {
      return "Compilation Result " + compResStr(lastCompilationRes);
   }
   
   private void openItem(int ind)
   {
      try
      {
        String[] arr = ErrorParser.parse(lastCompilationText);
        String s = arr[ind];
        String unit = ErrorParser.parseErrorUnitName(s);
        int line = ErrorParser.parseErrorLine(s);
        editor.openFileOnLine(unit, line);
      }
      catch (Exception e)
      {
      }
   }
   
   private boolean doShowLastCompilationMessages()
   {      
      try
      {
   	     String[] arr = ErrorParser.parse(lastCompilationText);
   	     if (arr != null)
   	     {
	        new MessageBox(parent, null)
	        {
	             public void selfBtnListen(MessageBox msg, int which)
                  {
                      openItem(which);
                  }
             }.choose(compResCaption(), arr);   
	        return true;
	     }   
      }
      catch (Exception e)
      {
      }
      return false;
   }
   
   private void openLastCompilationMessages()
   {
      if (!(doShowLastCompilationMessages()))
	    Dialogs.showText(parent, lastCompilationText, compResCaption(), false);      
   }
}