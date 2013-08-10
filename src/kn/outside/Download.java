package kn.outside;

import android.app.Activity;
import java.io.*;
import java.net.*;

import android.os.AsyncTask;
import android.app.ProgressDialog;

import kn.utils.Task;
import kn.gui.MessageBox;

public class Download
{
public static Exception m_error = null;
public static void downloadFile(Activity parent, String url, String destFile, String caption, Task task) 
{   
   try
   {
     doDownloadFile(parent, url, destFile, caption, task);
   }
   catch (Exception e)
   {
     m_error = e;
   }
   if (m_error != null)
         MessageBox.showEmptyOk(parent, "Downloading fail", m_error.getMessage());                
}

public static void doDownloadFile(Activity aParent, String url, String destFile, String caption, Task task) 
{
  final Task t = task;
  final Activity parent = aParent;
  final ProgressDialog progressDialog = new ProgressDialog(parent);
   
  new AsyncTask<String, Integer, File>() {    
    String caption;
 
    @Override
    protected void onPreExecute() {
      progressDialog.setMessage("Downloading " + "''" + caption + "''" + "...");
      progressDialog.setCancelable(false);
      progressDialog.setMax(100);
      progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
 
      progressDialog.show();
    }
 
    @Override
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
        url = new URL(params[0]);
        
        caption = params[2];
        progressDialog.setMessage("Downloading " + "''" + caption + "''" + "...");
        
        urlConnection = (HttpURLConnection) url.openConnection();
 
        urlConnection.setRequestMethod("GET");
        urlConnection.setDoOutput(true);
        urlConnection.connect();
 
        file = new File(params[1]);
        fos = new FileOutputStream(file);
        inputStream = urlConnection.getInputStream();
 
        totalSize = urlConnection.getContentLength();
        downloadedSize = 0;
 
        buffer = new byte[1024];
        bufferLength = 0;
 
        while ((bufferLength = inputStream.read(buffer)) > 0) {
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
 
    protected void onProgressUpdate(Integer... values) {
      progressDialog.setProgress((int) ((values[0] / (float) values[1]) * 100));
    };
 
    @Override
    protected void onPostExecute(File file) {
      if (m_error != null) {
        m_error.printStackTrace();
        progressDialog.hide();
        MessageBox.showEmptyOk(parent, "Downloading fail", m_error.getMessage());               
        return;
      }
      progressDialog.hide();
      if (t != null)
        t.execute();
      //file.delete();
    }
  }.execute(url, destFile, caption);
}
}