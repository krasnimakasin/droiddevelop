package com.assoft.DDPluginsPack;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.DialogInterface;
import android.net.Uri;

import kn.gui.Dialogs;
import kn.gui.TextDialog;
import kn.data.TextFiles;

public class MainActivity extends Activity
{
  private DDIntegration integration = new DDIntegration(this);  
  private LearnProgramming learnProgramming = new LearnProgramming(this);
  String[] names = {"Notes", "DroidDevelop Integration...", "Learn Programming..."};
  
  private Intent intent;
  
  private String names2Query()
  {
     String res = "";
     for (int i = 0; i < names.length; i++)
     {
       res += names[i];
       if (i < names.length - 1)
         res += "|";
     }
     return res;
  }
  
  TextDialog textDlg = null;
  
  private void processRefactCommand()
  {
     String name = intent.getStringExtra("android.intent.extra.Name");        
     String path = intent.getStringExtra("android.intent.extra.ProjectPath");        
     if (name.compareTo(names[0]) == 0)
     {
        String file = path + "/todo.txt";
        String s;
        try {
          s = new TextFiles(this).loadFile(file);
        }
        catch (Exception e) {
          s = "";
        }
        textDlg = Dialogs.showText(this, s, "Notes", false);  
        textDlg.dlg.setOnCancelListener(new DialogInterface.OnCancelListener()
                                                                 {
                                                                    public void onCancel(DialogInterface dialog)
                                                                    {
                                                                       BackPressed();
                                                                    }
                                                                 });
     }
     else if (name.compareTo(names[1]) == 0)     
     {
        integration.process();
     }
     else if (name.compareTo(names[2]) == 0)     
     {
        learnProgramming.process();
     } 
     else 
        learnProgramming.tryProcessStep(name); 
  }
  
  public void BackPressed()
  {
     String name = intent.getStringExtra("android.intent.extra.Name");        
     String path = intent.getStringExtra("android.intent.extra.ProjectPath");        
     if (name.compareTo(names[0]) == 0)
     {
         String file = path + "/todo.txt";     
         String text = textDlg.txt.getText().toString();
         new TextFiles(this).saveFile(file, text);
     }
     super.onBackPressed();
  }
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    intent = getIntent();
    if (intent == null)
    {
       finishActivity(RESULT_CANCELED);
       return;
    }
    if (intent.getAction().contentEquals("com.assoft.DroidDevelop.getCommonPluginCommandsNames"))
    {
       intent.setData(Uri.parse(names2Query())); 
       setResult(RESULT_OK, intent);       
       finish();
    }
    if (intent.getAction().contentEquals("com.assoft.DroidDevelop.processCommonPluginCommand"))
       processRefactCommand();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent aIntent)
  {
	if ((requestCode == 0) || (resultCode != RESULT_OK))
          finish();
      if (requestCode == DDIntegration.ActPeakColor)
      {
         intent = getIntent();
         int color = aIntent.getIntExtra("org.openintents.extra.COLOR", 0);
         intent.putExtra("android.intent.extra.CommandsCount", 1);
         intent.putExtra("android.intent.extra.CommandName0", "InsertText");
         intent.putExtra("android.intent.extra.CommandData0", Integer.toString(color));
         setResult(RESULT_OK, intent);
         finish();
      }
  }
}
