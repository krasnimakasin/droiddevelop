package kn.data;

import java.io.*;
import java.net.URI;

import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.webkit.MimeTypeMap;
import android.net.Uri;
import android.app.Activity;

public class TextFiles
{
    private Context context;
    public Boolean showToast = true;
    public TextFiles(Context aContext)
    {
      context = aContext;
    }
    
  
   public static String readTextFileFromRaw(Activity parent, int resID)
   {
   try {
         InputStream s = parent.getResources().openRawResource(resID);
         if ((s == null) || (s.available() <= 0))
            return "";
         final byte[] bytes = new byte[s.available()];
         s.read(bytes);
         return new String(bytes);
      }
      catch (Exception e) {
        return "";
      }
   }      
    
    private void info(String s)
    {      
        Toast.makeText(context, s, 2000).show();
    }
    
    public static String toAbsPath(String path)
    {
       return (new File(URI.create(path))).getAbsolutePath();
    }
    
    public static String extractFileExt(String fileName)
    {
       int lastdot = fileName.lastIndexOf(".");
       if(lastdot > 0)
          return fileName.substring(lastdot + 1);
       return "";
    }

    public static String mimeTypeByExt(String ext)
    {
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = mime.getMimeTypeFromExtension(ext);
        if(type != null) 
           return type;
       return "application/octet-stream";      
    }

   public void execute(String fileName)
   {
       String mime = mimeTypeByExt(extractFileExt(fileName));
       Intent intent = new Intent();
       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       intent.setAction(android.content.Intent.ACTION_VIEW);
       intent.setDataAndType(Uri.fromFile(new File(fileName)), mime);
       try
       {
          context.startActivity(intent);
       }
       catch (Exception e)
       {
          info("Couldn't open: unknown file type");
       }       
    }
    
    public String loadFile(String path) 
    {   
        if (showToast)  
           info("Loading file '" + path + "'");   
        File f = new File(path);
        final long length = f.length();
        if (!f.exists()) 
        {
            info("File doesnt exists: " + path);
            return "";
        }
        // typecast to int is safe as long as MAXFILESIZE < MAXINT
        byte[] array = new byte[(int)length];
        
        try {
            InputStream is = new FileInputStream(f);
            is.read(array);
            is.close();
        } catch (FileNotFoundException ex) 
        {
            // Checked for file existance already, so this should not happen
            info("Failed to access file: " + path);
            return "";
        } catch (IOException ex) {
            // read or close failed
            info("Failed to access file: " + path);
            return  "";
        }
        //setTitle("DroidDevelop - " + path);
        return new String(array);
    }
    
    public void saveFile(String path, String text) 
    {    
        if (showToast) 
          info("Saving file '" + path + "'");   
        File f = new File(path);
       
        OutputStream out = null;
        byte[] array = text.getBytes();
        try 
        { 
          try
          {
            out = new BufferedOutputStream(new FileOutputStream(f)); 
            out.write(array);
          }
          finally 
          { 
            if (out != null) 
              out.close(); 
          }
        }
        catch (Exception e)
        {
           info("Error during save file");// '" + e.message() + "'");
        }   
      }          
    }