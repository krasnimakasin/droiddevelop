package com.kn.DroidDevelop;

import android.widget.TextView;
import android.widget.EditText;
import android.app.Activity;
import android.content.DialogInterface;

import java.util.Timer;
import java.util.TimerTask;

import kn.data.TextFiles;
import kn.data.StringUtils;
import kn.utils.Task;
import kn.gui.MessageBox;

public class Editor
{
   public EditText tv;
   private String orgText = "";
   private String currentEditFile = "";
   private Activity parent;
   private Timer timer;
      
   public Editor(Activity aparent, EditText atv)
   {
      tv = atv;
      parent = aparent;
      timer = new Timer();
      TimerTask t = new TimerTask()
      {
         public void run()
         {
            try
            {
               tv.moveCursorToVisibleOffset();
            }
            catch (Exception e)
            {
            }
         }
      };
      //timer.scheduleAtFixedRate (t, 100, 100);
   }
   
   public void showCharCode()
   {
      MessageBox msg = new MessageBox(parent, null)
      {
         public void selfBtnListen(MessageBox msg, int which)
         {               
            if ((msg.input != null) && (which == DialogInterface.BUTTON_POSITIVE))
            {
               String lineStr = msg.input.getText().toString();
               try
               {   
                  new MessageBox(parent, null).ShowEmpty(Integer.toString(lineStr.codePointAt(0)));
               }
               catch (Exception e)
               {
                  new MessageBox(parent, null).ShowEmpty("Error " + e.getMessage());
               }
            }
         }
      };
      msg.input("Input One Char", "");   
   }
   
   private void doGotoLine(int line)
   {
      int linePos = StringUtils.lineStartPos(tv.getText().toString(), line);
      if (linePos >= 0)
        tv.setSelection(linePos);
   }
   
   public void gotoLine()
   {
      MessageBox msg = new MessageBox(parent, null)
      {
         public void selfBtnListen(MessageBox msg, int which)
         {               
            if ((msg.input != null) && (which == DialogInterface.BUTTON_POSITIVE))
            {
               String lineStr = msg.input.getText().toString();
               try
               {   
                   int linePos = Integer.parseInt(lineStr);
                   doGotoLine(linePos);
               }
               catch (Exception e)
               {
                  new MessageBox(parent, null).ShowEmpty("Error " + e.getMessage());
               }
            }
         }
      };
      msg.input("Input Line Number", "");
   }
      
  public void save()
  {
      TextFiles f = new TextFiles(parent);
      String s = tv.getText().toString();
      f.saveFile(currentEditFile, s);
      orgText = s;
  }
  
  public void CloseFile()
  {
    currentEditFile = "";
    tv.setText("");
    orgText = "";
  }
   
  public String OpenFile(String fileName)
  {
      TextFiles f = new TextFiles(parent);
      currentEditFile = fileName;
      parent.setTitle("DroidDevelop - " + fileName);
      orgText = f.loadFile(fileName);
      return orgText;
  }
    
  private boolean needAskToSave()
  {
    return (orgText.compareTo(tv.getText().toString()) != 0); 
  }
  
  public void openFileOnLine(String unit, int line)
  {
     Task t = new Task()
     {
         public void execute()
         {
             tv.setText(OpenFile(strParam));
             doGotoLine(intParam);            
         }     
     };
     t.strParam = unit;
     t.intParam = line;
     taskAskToSave(t);
  }
   
  public void taskAskToSave(Task task)
  {
    if (task == null)
       return;
    if (!(needAskToSave()))
    {
       task.execute();
       return;
    }
    MessageBox msg = new MessageBox(parent, null)
    {
      public void selfBtnListen(MessageBox msg, int which)
      {    
        if (which == DialogInterface.BUTTON_POSITIVE)
          save();
        msg.task.execute();
      }
    };
    msg.task = task;
    msg.Ask("There are some changes. Do you want to save it?");
  }
}