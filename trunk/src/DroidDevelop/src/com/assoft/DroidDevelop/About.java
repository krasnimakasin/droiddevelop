package com.assoft.DroidDevelop;

import android.text.Html;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.content.res.Resources;

//import java.io.*;

import kn.gui.Dialogs;
import kn.gui.MessageBox;
import kn.data.TextFiles;
//import kn.gui.TextDialog;

public class About
{
  public static Resources res = null;
  public static void showInfo(Activity aParent)
  {
        String[] arr = {"Help...",
                              "About..."};
        new MessageBox(aParent, null)
        {
           public void selfBtnListen(MessageBox msg, int which)
           {
	         if (which == 0) 
	            showHelp(parent);
   	         else if (which == 1)
   	            showAbout(parent);
           }    
        }.choose("", arr);  
  }
  
  public static void showAbout(Activity aParent)
  {
     if (res == null)
        res = aParent.getResources();
     Html.ImageGetter img = new Html.ImageGetter()
        {
           public Drawable getDrawable(String source)
           {            
             Drawable result = res.getDrawable(R.drawable.assoft);
             if (result != null) 
                result.setBounds(0, 0, 120, 100);
             return result;
           }
        };
     String s = "<div align=center>";
     s += "<a href=http://en.assoft.ru><img src=assoft.png alt=www.en.assoft.ru/></a>";
     s += "</div><br>";
     s += "<div align=center>";
     s += "<h2><a href=mailto:admin@assoft.ru> Make it Usability!</a></h2>";
     s += "<a href=https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&amp;hosted_button_id=U7K7ADWGBYRTY>Donate</a>";
     s += "</div><br>";
     //s += "<h2>Специальная версия для <a href=http://www.my-free-soft.ru>www.my-free-soft.ru</a></h2>";
     Dialogs.showHtml(aParent, s, "", true, img, 300);
  }
  
  public static void showHelp(Activity parent)
  {
     String h = TextFiles.readTextFileFromRaw(parent, R.raw.help);
     Dialogs.showText(parent, h, "Help", true);
  }
}