package kn.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.*;
import android.net.Uri;
import android.content.DialogInterface;

import kn.gui.MessageBox;

public class MarketUtils
{
    public static Boolean isInstalled(Activity parent, String pack, String aClass)
    {
        Intent intent = new Intent();  
        intent.setClassName(pack, pack + "." + aClass);
        return isInstalledIntent(parent, intent);
    }
    
    public static Boolean isInstalledIntent(Activity parent, Intent intent)
    {
        PackageManager pm = parent.getPackageManager();
        return (pm.resolveActivity(intent, 0) != null);
    }    
   
    public static void askToInstall(Activity parent, String programName, String pack)
    {
      MessageBox msg = new MessageBox(parent, null)
      {
         public void selfBtnListen(MessageBox msg, int which)
         {    
            if (which == DialogInterface.BUTTON_POSITIVE)
            {
                Intent intent = new Intent("android.intent.action.VIEW");                   
                intent.setData(Uri.parse("market://details?id=" + msg.strParam));
                try {                                
                  parent.startActivityForResult(intent, 0);
                }
                catch (Exception e) {
                  new MessageBox(parent, null).ShowEmpty("Error: " + e.getMessage());
                }
            }
         }
      };
      msg.strParam = pack;
      msg.Ask("Please install " + programName + ". Do you want to do it now from market?");    
    }
}