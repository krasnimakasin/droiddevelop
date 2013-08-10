package com.assoft.PascalDevelop;

import android.app.Activity;
import android.os.AsyncTask;

import java.util.concurrent.TimeUnit;
import java.io.*;

public class PascalAppExecuter
{
   private Process process;
   private ConsoleAppView app;
   private InputStream is;
   private OutputStream os;
   private Activity parent;
   private Boolean needAds;
   private Boolean appChoosen;
   
   public PascalAppExecuter(Activity aParent, String aFile, Boolean aNeedAds)
   {
      try
      {
        process = Runtime.getRuntime().exec(aFile);
      }
      catch (Exception e) {
      }
      is = process.getInputStream();
      os = process.getOutputStream();
      parent = aParent;
      needAds = aNeedAds;
      doProcessAsync();     
   }
   
  private void doProcessAsync()
  {
    new AsyncTask< PascalAppExecuter , PascalAppExecuter , Integer> ()
    { 
       @Override
       protected Integer doInBackground( PascalAppExecuter ... params)
       {
          appChoosen = false;
          while (true)
          {
             try {
                get(100, TimeUnit.MILLISECONDS);
             } catch(Exception e){};
            publishProgress(params[0]);       
          }
       }
       @Override
       protected void onProgressUpdate( PascalAppExecuter ... values) 
       {
          values[0].readAgain();   
       }
     }.execute(this);
  }
  
  public Boolean chooseApp()
  { 
      byte[] bytes;
      try 
      {
         if (is.available() < 7)
            return true;      
         bytes = new byte[7];
         is.read(bytes);
      } 
      catch (Exception e) 
      {
         return true;
      }   
      if (new String(bytes).compareTo("console") == 0)
      {
        app = new ConsoleAppView(parent, is, os, needAds);
        appChoosen = true;
      } 
      return true;
  }
  
  public Boolean readAgain()
  {   
    //if (cp == null)
    //  cp = new ConsoleProxy(fileName);  
       if (!appChoosen)
         return chooseApp();
       try {
          byte[] bytes = new byte[is.available()];
          int n = is.read(bytes);
          return app.readAgain(bytes, n);
       }
       catch (Exception e) {
         return false;
       }
  }
   
}