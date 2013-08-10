package kn.ide;

import android.app.Activity;
import android.view.ViewGroup;

import kn.ide.DDSettings;
import kn.ide.ResUtils;
import kn.gui.CommonEditor;
import kn.gui.MessageBox;
import kn.data.RecentFiles;
import kn.data.PrefsStorage;
import kn.utils.Task;

public class BaseProject
{
   protected Activity parent;
   protected RecentFiles recentFiles;
   public CommonEditor editor;
   public DDSettings settings;
   
   private static int runFileStoreInd = 0;
   private static int lastOpenFileStoreInd = 1;   
   
  public BaseProject(Activity aParent, PrefsStorage aPrefs, RecentFiles aRecentFiles, CommonEditor anEditor)
   {
      settings = new DDSettings(aParent, aPrefs, anEditor.tv);
      parent = aParent;
      recentFiles = aRecentFiles;
      editor = anEditor;
   }

   private String showHideButtons()
   {
       ViewGroup scr = (ViewGroup)parent.findViewById(ResUtils.resBtnControls);   
       if (scr.getVisibility() == 8)
          return "Show Buttons";
       else
          return "Hide Buttons";
   }
   
   public void changeButtonsVisible()
   {
       ViewGroup scr = (ViewGroup)parent.findViewById(ResUtils.resBtnControls);   
       if (scr.getVisibility() == 8)
          scr.setVisibility(0); 
       else
          scr.setVisibility(8); //gone      
   }
   
   public void doRotateScreen()
   {
       if (parent.getRequestedOrientation() == 1)
           settings.setOrientation(0);
       else
           settings.setOrientation(1);
   }
   
   private void rotateScreen()   
   {
        editor.taskAskToSave(new Task()
                             {
                                public void execute()
                                {
                                   doRotateScreen();
                                }
                             });
   }
   
   private String wordWrapStr()
   {
     if (settings.getWordWrap())
       return "Don't Wordwrap";
    else
       return "Wordwrap";
   }
     
   public void prjOptionsDlg()
   {
        String[] arr = {"Clear File to Run...",
                                 showHideButtons(),
                                 "Rotate Screen",
                                 "Font Size",
                                 wordWrapStr()};
        new MessageBox(parent, null)
        {
           public void selfBtnListen(MessageBox msg, int which)
           {
	         if (which == 0) 
	            setRunFileName("");
               else if (which == 1)
                   changeButtonsVisible();
               else if (which == 2)
                   rotateScreen();
               else if (which == 3)
                   chooseFontSize("Choose Font Size...");
               else if (which == 4)
                   settings.setWordWrap(!(settings.getWordWrap()));
           }    
        }.choose("Project options", arr);   
   }
   
   public String[] fontSizeArr = {"Large",
                                                      "Middle",
                                                      "Small"};
   
  private void doSetFontSize(int which)
  {
      settings.setFontSize(fontSizeArr[which]);
  }    
  
  public void chooseFontSize(String caption)
  {
        new MessageBox(parent, null)
        {
           public void selfBtnListen(MessageBox msg, int which)
           {
              doSetFontSize(which);
           }    
        }.choose(caption, fontSizeArr);     
  }   
      
   public String getRunFileName()
   {
      return settings.getOption(runFileStoreInd);
   }
   
   public void setRunFileName(String value)
   {
      settings.setOption(value, runFileStoreInd);
   }
   
   public void readOptions(String fileName)
   {     
     try
     {   
        settings.readOptions(fileName);     
      }
      catch  (Exception e) 
      {
         new MessageBox(parent, null).ShowEmpty("Read project options error");
      }
   }  
   
   public String getLastOpenFile()
   {
      return settings.getOption(lastOpenFileStoreInd);
   }
   
   public void setLastOpenFile(String value)
   {
      settings.setOption(value, lastOpenFileStoreInd);
   }   
   
}