package com.assoft.DroidDevelop;

import kn.utils.NativeSettings;
import kn.utils.MarketUtils;
import kn.gui.MessageBox;

import android.app.Activity;
import android.content.DialogInterface;

public class Native
{
   public static String[] chooseNativeLib(Activity parent, String settingsFile)
   {
     String[] arr = new String[0];
     NativeSettings ns = new NativeSettings();
     int set = ns.openSettings(settingsFile);
     try
     {
       int i = 0;
       while (ns.getValue(set, "natLibName" + Integer.toString(i)).compareTo("") != 0)
         i++;
       arr = new String[i];
       for (int j = 0; j < i; j++)
         arr[j] = ns.getValue(set, "natLibName" + Integer.toString(j));
     }
     finally
     {
       ns.release(set, settingsFile);
     }      
     return arr;
     //return MessageBox.chooseSync(parent, "Choose Natibe Library", arr);
   }
   
   private static void doAddNativeLib(String libName, String settingsFile)
   {
     NativeSettings ns = new NativeSettings();
     int set = ns.openSettings(settingsFile);
     try
     {
       int i = 0;
       while (ns.getValue(set, "natLibName" + Integer.toString(i)).compareTo("") != 0)
         i++;
       ns.setValue(set, "natLibName" + Integer.toString(i), libName);
     }
     finally
     {
       ns.release(set, settingsFile);
     }
   }
   
   public static void addNativeLib(Activity parent, String settingsFile)
   {
     if (!(MarketUtils.isInstalled(parent, "com.assoft.PascalDevelop", "MainActivity")))
     {
        MarketUtils.askToInstall(parent, "PascalDevelop", "com.assoft.PascalDevelop");
        return;
     }
      MessageBox msg = new MessageBox(parent, null)
      {
         public void selfBtnListen(MessageBox msg, int which)
         {               
            if ((msg.input != null) && (which == DialogInterface.BUTTON_POSITIVE))
            {
               String lineStr = msg.input.getText().toString();
               doAddNativeLib(lineStr, strParam);
            }
         }
      };
      msg.strParam = settingsFile;
      msg.input("Input source '*.pas' file full path", "");   
   }
}