package kn.utils;

import kn.gui.MessageBox;

import android.app.Activity;
import java.io.*;
import java.net.*;

public class InternetUtils
{
   private static HttpURLConnection createConnection(Activity parent, String anUrl)
   {
       try {
           URL url = new URL(anUrl);
           HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    
           urlConnection.setRequestMethod("GET");
           urlConnection.setDoOutput(true);
           urlConnection.connect();   
           return urlConnection;
        }
        catch (Exception e) {
          error(parent, e.getMessage());
          return null;
        }
   }
   
   private static void error(Activity parent, String msg)
   {
       MessageBox.showEmptyOk(parent, "Fail ''android.jar'' Downloading", msg);                 
   }
   
   private static void publishProgress(int cur, int total)
   {
   
   }
   
   public static Boolean downloadFileToFolder(Activity parent, String url, String destFileName)
   {
      HttpURLConnection urlConnection = createConnection(parent, url);
      if (urlConnection == null)
         return false;
      try 
      {
         File f = new File(destFileName);   
         FileOutputStream fos = new FileOutputStream(f);
         InputStream inputStream = urlConnection.getInputStream();         
    
         int totalSize = urlConnection.getContentLength();
         int downloadedSize = 0;
    
         byte[] buffer = new byte[1024];
         int bufferLength = 0;
    
         while ((bufferLength = inputStream.read(buffer)) > 0) 
         {
             fos.write(buffer, 0, bufferLength);
             downloadedSize += bufferLength;
             publishProgress(downloadedSize, totalSize);
         }
    
         fos.close();
         inputStream.close();      
      return true;
      }
      catch (Exception e) {
         error(parent, e.getMessage());
         return false;
      }
   }
   
   /*
    protected File doInBackground(String... params) {
      URL url;
      HttpURLConnection urlConnection;
      InputStream inputStream;
      int totalSize;
      int downloadedSize;
      byte[] buffer;
      int bufferLength;
 
      File file = null;
      FileOutputStream fos = null;
 
      try {
 
        file = File.createTempFile("tmp", "download");
        fos = new FileOutputStream(file);
        inputStream = urlConnection.getInputStream();
 
        totalSize = urlConnection.getContentLength();
        downloadedSize = 0;
 
        buffer = new byte[1024];
        bufferLength = 0;
 
        while ((bufferLength = inputStream.read(buffer)) &gt; 0) {
          fos.write(buffer, 0, bufferLength);
          downloadedSize += bufferLength;
          publishProgress(downloadedSize, totalSize);
        }
 
        fos.close();
        inputStream.close();
 
        return file;
      }
      catch (MalformedURLException e) {
        e.printStackTrace();
        m_error = e;
      }
      catch (IOException e) {
        e.printStackTrace();
        m_error = e;
      }
 
      return null;
    }
    */   
}