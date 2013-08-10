package com.assoft.DDPluginsPack;

import android.app.Activity;
import android.content.Intent;
import android.content.ComponentName;
import android.content.DialogInterface;

//import java.net.URL;

import kn.gui.MessageBox;
import kn.data.PrefsStorage;
import kn.data.FileUtils;
import kn.utils.MarketUtils;

//import com.castillo.dd.Download;

public class DDIntegration
{
   public static int ActPeakColor = 1;
   
   private Activity parent;
   private PrefsStorage prefs;
   
   public DDIntegration(Activity aParent)
   {
      parent = aParent;
      prefs = new PrefsStorage(parent);
   }
   
   private void runTArnDiff(String file1, String file2)
   {
      ComponentName cn = new ComponentName("com.t_arn.taTextDiff", "com.t_arn.taTextDiff.MainActivity");    
      Intent intent = new Intent();
      intent.setComponent(cn);
      intent.putExtra("android.intent.extra.FilePath1", file1);   
      intent.putExtra("android.intent.extra.FilePath2", file2);   
      parent.startActivityForResult(intent, 0);
   }
   
   private void doShowDiff(String filePath2)
   {
     Intent intent = parent.getIntent();
     String fileName = intent.getStringExtra("android.intent.extra.CurrentEditFile");        
     String filePath1 = FileUtils.extractFilePath(fileName);
     prefs.setPref("showDiffPath_" + filePath1, filePath2);
     runTArnDiff(fileName, filePath2 + FileUtils.extractFileName(fileName));
   }
   
   private void showDiff()
   {
     if (!(MarketUtils.isInstalled(parent, "com.t_arn.taTextDiff", "MainActivity")))
     {
        MarketUtils.askToInstall(parent, "taTextDiff", "com.t_arn.taTextDiff");
        return;
     }
     Intent intent = parent.getIntent();
     String fileName = intent.getStringExtra("android.intent.extra.CurrentEditFile");        
     String filePath1 = FileUtils.extractFilePath(fileName);
     String filePath2 = prefs.getPref("showDiffPath_" + filePath1);
     new MessageBox(parent, null)
     {
           public void selfBtnListen(MessageBox msg, int which)
           {
	         if (which == DialogInterface.BUTTON_POSITIVE) 
	            doShowDiff(msg.input.getText().toString());
               else
                   parent.finish();
           } 
           public void OnBackPressed()
           {
                parent.finish();
           }                 
     }.input("Choose Backup Folder for\n" + fileName, filePath2);          
   }
   
   private void pickColor()
   {
     Intent intent = new Intent("org.openintents.action.PICK_COLOR");
     if (!(MarketUtils.isInstalledIntent(parent, intent)))
     {
        MarketUtils.askToInstall(parent, "OI Color Picker", "org.openintents.colorpicker");
        return;
     }
    parent.startActivityForResult(intent, ActPeakColor);
   }

/*   
   private void downloadJar()
   {
   try {
         Download dd = new Download(new URL("en.assoft.ru/android.jar"), 0);
         dd.setFileName("/mnt/storage/sdcard/android.jar");
         dd.run();
      }
      catch (Exception e) {
      }
      //dd.setFileName("
   }
   */
   
   public void process()
   {
        String[] arr = {"taTextDiff...",
                                 "OI Color Picker"/*,
                                 "Download Android"*/};
        new MessageBox(parent, null)
        {
           public void selfBtnListen(MessageBox msg, int which)
           {
	         if (which == 0) 
	            showDiff();
               else if (which == 1)
                  pickColor();
        /*       else if (which == 2)
                  downloadJar();  */
           }   
           public void OnBackPressed()
           {
                parent.finish();
           }            
        }.choose("", arr);     
   }
}