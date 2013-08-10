package kn.data;

import android.content.Intent;
import android.app.Activity;
import android.net.Uri;

import java.io.*;

import kn.data.TextFiles;
import kn.utils.MarketUtils;

public class FileUtils
{

  public static void forceDirectory(String path)
  {
     File f = new File(path);
     f.mkdirs();
  }
  
  public static Boolean fileExists(String filename)
  {
     File f = new File(filename);
     return f.exists();     
  }
  
  public static String tempPath()
  {       
    try
    {
       File f = File.createTempFile("", "");
       return f.getAbsolutePath();
    }
    catch (Exception e) 
    {
       return "";
    }
  } 
  
  public static void deleteFile(String fileName)
  {
     File f = new File(fileName);
     f.delete();
  }
  
   public static String pathToSDCardOld()
   {
         if (fileExists("/mnt/storage/sdcard"))
           return "/mnt/storage/sdcard/";
         else if  (fileExists("/mnt/sdcard"))
           return "/mnt/sdcard/";
         else
           return "/mnt/";
   }
  
   public static String _pathToSDCard()
   {
         if (fileExists("/mnt/storage/sdcard"))
           return "/mnt/storage/sdcard/";
         if (fileExists("/storage/sdcard"))
           return "/storage/sdcard/";           
         else if  (fileExists("/mnt/sdcard"))
           return "/mnt/sdcard/";
         else if  (fileExists("/sdcard"))
           return "/sdcard/";           
         else if  (fileExists("/mnt/"))
           return "/mnt/";
         else
           return tempPath() + "/";
   }

  public static String pathToSDCard()
  {  
        if (fileExists("/mnt/sdcard/external_sdcard"))
             return "/mnt/sdcard/external_sdcard/";
        return _pathToSDCard();
  }
   
   public static String AssoftRoot()
   {
     String res = pathToSDCardOld() + "assoft/";
     if  (fileExists(res))
        return res;
     res = _pathToSDCard() + "assoft/";
     if  (fileExists(res))
        return res;
     res = pathToSDCard() + "assoft/";     
     forceDirectory(res);
     return res;     
   }

  public static void chooseFileForOpen(Activity parent, int Act, String lastFile)
  {
    try
    {
       Intent intent = new Intent("org.openintents.action.PICK_FILE"); 
       intent.setData(Uri.parse("file://" + lastFile));
       parent.startActivityForResult(intent, Act);  
     }
     catch (Exception e)
     {
        MarketUtils.askToInstall(parent, "OI File Manager", "org.openintents.filemanager");
     }
  }
  
  public static String extractFilePath(String fileName)
  {
     int n = fileName.lastIndexOf("/");
     if (n < 0)
       return "";
     return fileName.substring(0, n);
  }
  
  public static String extractFileName(String fileName)
  {
     int n = fileName.lastIndexOf("/");
     if (n < 0)
       return "";
     return fileName.substring(n + 1, fileName.length());  
  }
  
  public static String removeExtention(String fileName)
  {
     int n = fileName.lastIndexOf(".");
     if (n < 0)
       return fileName;
     return fileName.substring(0, n);  
  }
  
  public static String extractExtention(String fileName)
  {
     int n = fileName.lastIndexOf(".");
     if (n < 0)
       return "";
     return fileName.substring(n + 1, fileName.length());  
  }
  
  public static String FileNameFromIntent(Intent intent)
  {
      try
      {
	   String path = intent.getData().toString();
	   return TextFiles.toAbsPath(path);
      }
      catch (Exception e)
      {
          return "";
      }
  }
   
   public static void copyFileFromRaw(Activity parent, int resID, String resFile)
   {
       File f = new File(resFile); 
       copyFileFromRaw(parent, resID, f);
   }
   
   public static void copyFileFromRaw(Activity parent, int resID, File resFile)
   {
        try {
            InputStream s = parent.getResources().openRawResource(resID);
            if ((s == null) || (s.available() <= 0))
               return;
            final byte[] bytes = new byte[s.available()];                  
            OutputStream out = new BufferedOutputStream(new FileOutputStream(resFile));
            s.read(bytes); 
            out.write(bytes);
            out.close();
         }
         catch (Exception e) {
         }
   }

  
  //public void chooseAndOpenFileWithOI(Activity A, int Act)
  //{
  //}
  }