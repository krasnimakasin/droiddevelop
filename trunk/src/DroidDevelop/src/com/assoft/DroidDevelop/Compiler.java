package com.assoft.DroidDevelop;

import android.app.Activity;
import android.content.Intent;
import android.content.ComponentName;
import android.os.Bundle;
import android.content.DialogInterface;
import android.widget.Toast;
import android.content.pm.*;
//import android.net.Uri;

import kn.gui.MessageBox;
import kn.gui.Dialogs;
import kn.outside.GetClassRef;
import kn.utils.MarketUtils;

//import com.assoft.DroidDevelop.Editor;

public class Compiler
{
   private Activity parent;
   private BaseEditor editor;
   
   private String lastCompilationText = "";
   private int lastCompilationRes = 0;
   
   public Compiler(Activity aParent, BaseEditor anEditor)
   {
      parent = aParent;
      editor = anEditor;
   }
   
   private void processRefItem(String item)
   {
      String[] arr = {"Insert to Editor",
                               "Cancel"};
      MessageBox msg = new MessageBox(parent, null)
      {
          public void selfBtnListen(MessageBox msg, int which)
          {
	       if (which == 0)
                editor.insertTextToCurrent(msg.strParam);
          }    
        };
        msg.strParam = item;
        msg.choose("Select action", arr);     
   }
   
   private void doGetClassRef(String className)
   {
      String[] res = GetClassRef.main(className);
      new MessageBox(parent, null)
      {
          public void selfBtnListen(MessageBox msg, int which)
          {
	       processRefItem(msg.items[which]);
           }    
        }.choose(className, res);  
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
   
   public Boolean compilationSuccess(Intent intent)
   {
	   Bundle extras = intent.getExtras();
	   return ((extras != null) && (extras.getInt("android.intent.extra.ScriptResultCode", -1) == 0));
   }
   
    public void processResult(Intent intent)
    {
        try
        {
	   Bundle extras = intent.getExtras();
	   if (extras != null)
	   {
	      lastCompilationRes = extras.getInt("android.intent.extra.ScriptResultCode", -1);
	      lastCompilationText = extras.getString("android.intent.extra.ResultText");	    
	      openLastCompilationMessages();
	   } 
        }
        catch (Exception e)
        {
           new MessageBox(parent, null).ShowEmpty("Fail on JavaIdeDroid running. Try to reinstall it."); 
        }
    }
    
    public void processNativeLibResult (Intent intent)
    {
        try
        {          
           String s = intent.getData().toString();
           Dialogs.showText(parent, s, compResCaption(), false);
        } 
        catch (Exception e)
        {
           new MessageBox(parent, null).ShowEmpty("Fail on PascalDevelop running. Try to reinstall it."); 
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
       if (compRes == 1)
          return "Resource compilation fail";
       if (compRes == 2)
          return "Script fail";
       else
          return Integer.toString(compRes);
    }
    
    private Boolean intentAvail(Intent intent)
    {
       PackageManager pm = parent.getPackageManager();
       return !(pm.queryIntentActivities(intent, 0).isEmpty());    
    }
      
    private Intent doCreateIntent(Boolean forPro)
    {
       ComponentName cn;
       if (forPro)
          cn = new ComponentName("com.t_arn.JavaIDEdroidPRO", "com.t_arn.JavaIDEdroidPRO.MainActivity");       
       else
          cn = new ComponentName("com.t_arn.JavaIDEdroid", "com.t_arn.JavaIDEdroid.MainActivity");
       Intent intent = new Intent("android.intent.action.SEND");
       intent.setComponent(cn);
       return intent;       
    }
    
    private Intent createIntent()    
    {
       Intent res = doCreateIntent(true);
       if (intentAvail(res))
          return res;
       res = doCreateIntent(false);
       if (intentAvail(res))
          return res;    
       return null;
    }
   
    public void doRunCompile(String fileName, int Act)
    {       
       if (parent == null)
         return;
       Intent intent = createIntent();
       if (intent == null)
       {
         MarketUtils.askToInstall(parent, "JavaIDEDroid", "com.t_arn.JavaIDEdroid");
         //new MessageBox(parent, null).ShowEmpty("Please install JavaIDEDroid");
         return;
       }
       intent.putExtra("android.intent.extra.ScriptPath", fileName);
       intent.putExtra("android.intent.extra.ScriptAutoRun", true);
       intent.putExtra("android.intent.extra.ScriptAutoExit", true);
       intent.putExtra("android.intent.extra.WantResultText", true);
       try
       {
         parent.startActivityForResult(intent, Act);
       }
       catch (Exception e)
       {
          new MessageBox(parent, null).ShowEmpty("Please install JavaIDEDroid");
       }
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