package com.assoft.DroidDevelop;

import android.app.Activity;
//import android.util.Log;

import java.io.DataInputStream;
import java.io.*;

import kn.gui.MessageBox;
import kn.data.FileUtils;

public class DDLearnProject
{   
   private Activity parent;
   public Step[] lesson;
   public String charSet = "windows-1251";
   public String pubId;
   public String prjName;
   private static int curVersion = 2;
   
   public class Step
   {
      public Highlight[] highlights = null;
      public String file = "";
      public String text = "";
      public String code = "";
   }
   
   public class Highlight
   {
     public int start = 0;
     public int end = 0;
   }      
   
   private void error(String mess)
   {
     new MessageBox(parent, null).ShowEmpty(mess); 
   }
   
   private String readString(DataInputStream strm)
   {
      try 
      {
           int size = readInt(strm);
           byte buf[] = new byte[size];           
           
           //for (int i = 0; i < buf.length; i++)
           // buf[i] = strm.readByte();
           strm.read(buf, 0, size);
           
           //error(new String(buf));
           //Log.w("StartJava", "Str:" + new String(buf));
           
           return new String(buf, charSet);   
        }
        catch (Exception e) 
        {
           error(e.getMessage());
           return "";
        }
   }
   
   private int readInt(DataInputStream strm)
   {
      try 
      {
         int res = Integer.reverseBytes(strm.readInt());
         //Log.w("StartJava", "Int:" + Integer.toString(res));
         return res;
      }
      catch (Exception e) 
      {
         error(e.getMessage());
         return -1;
      }
   }   
   
   private void loadStep(DataInputStream strm, int stepInd)
   {
      try 
      {        
        lesson[stepInd] = new Step();
        
        lesson[stepInd].text = readString(strm);
        lesson[stepInd].file = readString(strm);
        lesson[stepInd].code = readString(strm);
        
        int size = readInt(strm);
        lesson[stepInd].highlights = new Highlight[size];
        for (int i = 0; i < lesson[stepInd].highlights.length; i++)
        {
          lesson[stepInd].highlights[i] = new Highlight();
          lesson[stepInd].highlights[i].start = readInt(strm);
        }
         readInt(strm);
        for (int i = 0; i < lesson[stepInd].highlights.length; i++)
          lesson[stepInd].highlights[i].end = readInt(strm);         
     }
     catch (Exception e) 
     {
        error(e.getMessage());
        return;
     }
   }
   
   private void readProject(InputStream resStrm)   
   {
      readProjectFromStream(resStrm);
            
   }
   
   private void readProjectFromStream(InputStream s)
   {
      try 
      {
         //error("1");
         //String path = "/mnt/storage/sdcard/progr/java/_my/StartJava/HelloWorld.ddl";
         //InputStream s = new FileInputStream(new File(path));
         if ((s == null) || (s.available() <= 0))
            return;
         DataInputStream strm = new DataInputStream(s);
         int version = readInt(strm);
     
         if (version > curVersion)
           error("Incorrect program version. Try to update.");
         if (version > 1)
         {
           charSet = readString(strm);
           pubId = readString(strm);
         }
         int stepsCount = readInt(strm);
         lesson = new Step[stepsCount];
         for (int i = 0; i < lesson.length; i++)
            loadStep(strm, i);   
            
      }
      catch (Exception e) 
      {
        error(e.getMessage());
      }   
   }
   
   public String getFileForStep(int step)
   {
       return FileUtils.AssoftRoot() + prjName + "/" + lesson[step].file;         
   }
   
   public DDLearnProject(Activity aParent, InputStream resStrm, String aPrjName)
   {
      parent = aParent;
      prjName = aPrjName;      
      readProject(resStrm);
   }   
   
   public DDLearnProject(Activity aParent, String fileName)
   {
      parent = aParent;
      try
      {
        readProjectFromStream(new FileInputStream(new File(fileName)));
      }
      catch (Exception e)
      {
         error("Can not load ''" + fileName + "'' project");
      }
      prjName = FileUtils.removeExtention(FileUtils.extractFileName(fileName));
   }
}