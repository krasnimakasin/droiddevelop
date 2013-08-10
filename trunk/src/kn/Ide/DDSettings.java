package kn.ide;

import kn.data.PrefsStorage;
import kn.data.StringUtils;

import android.app.Activity;
import android.widget.TextView;

public class DDSettings
{
  private PrefsStorage prefs;
  private Activity parent;
  private TextView tv;
  
  private String prjName = "";  
  
  private String[] projectOptions = null;
  
  public DDSettings(Activity aParent, PrefsStorage aPrefs, TextView aTv)
  {
     parent = aParent;
     prefs = aPrefs;
     tv = aTv;
  }
  
   public void setOrientation(int value)
   {
       if (value == 1)
         prefs.setPref("interfaceOrientation", "1");   
       else
         prefs.setPref("interfaceOrientation", "0");      
       parent.setRequestedOrientation(value);
   }  
   public void firstSetOrientation(Boolean defVert)
   {
      if (defVert)
      {
       if (prefs.getPref("interfaceOrientation").contentEquals("0"))
          parent.setRequestedOrientation(0);
       else
          parent.setRequestedOrientation(1);      
      }
      else
      {
       if (prefs.getPref("interfaceOrientation").contentEquals("1"))
          parent.setRequestedOrientation(1);
       else
          parent.setRequestedOrientation(0);
       }
   }  
   
        
   public void firstSetSize(int defSize)
   {
     if (prefs.getPref("fontSize").contentEquals("Large"))
        tv.setTextSize(24);
     else if (prefs.getPref("fontSize").contentEquals("Middle"))
        tv.setTextSize(18);
     else if (prefs.getPref("fontSize").contentEquals("Small"))
        tv.setTextSize(12);        
     else 
        tv.setTextSize(defSize);        
   }
   
   public void setFontSize(String sizeProfName)
   {
      prefs.setPref("fontSize", sizeProfName);      
      firstSetSize(18);
   }  
   
   public void initFirst(Boolean defVert, int fontSize, Boolean wordWrap)
   {
      firstSetOrientation(defVert);
      firstSetSize(fontSize);
      firstSetWordWrap(wordWrap);     
   }
   
   public Boolean getWordWrap()
   {
      return prefs.getPref("wordWrap") == "1";
   }
   
   public void setWordWrap(Boolean b)
   {
     if (b)
       prefs.setPref("wordWrap", "1");      
     else
       prefs.setPref("wordWrap", "0");      
     tv.setHorizontallyScrolling (!(b));
   }   
   
   public void firstSetWordWrap(Boolean b)
   {
      if (!(prefs.hasPref("wordWrap")))
        setWordWrap(b);
     else
        tv.setHorizontallyScrolling (!(getWordWrap()));
   }   
   
   
   public void setBackgroundHighLight(int color)
   {
      //new MessageBox(parent, null).ShowEmpty(Integer.toString(color));      
      prefs.setPref("backgroundHighlight", Integer.toString(color));        
   }
  
   public int defBackgroundHighlightColor = -3806209;//-1717315;
   
   public int getBackgroundHighLight()
   {
      if (!(prefs.hasPref("backgroundHighlight")))
         return defBackgroundHighlightColor;
      try
      {
         String s = prefs.getPref("backgroundHighlight");
         if (s.contentEquals("")) 
           return defBackgroundHighlightColor;
         return Integer.parseInt(s);
      }
      catch (Exception e)
      {
        return defBackgroundHighlightColor;
      }      
   }   
      
   private boolean validOptInd(int ind)
   {
      return (projectOptions != null) && (projectOptions.length > ind);
   }   
   
   
   public String getOption(int index)
   {
      if (validOptInd(index))
         return projectOptions[index];
      return "";   
   }
   
   private void checkOptions(int maxInd)
   {
      if (!(validOptInd(maxInd)))
      {
         String[] arr = new String[maxInd + 1];
         if (projectOptions != null) 
         {
            for (int i = 0; i < projectOptions.length; i++)
               arr[i] = projectOptions[i];
         }
         projectOptions = arr;
      }   
   }
   
   public void setOption(String value, int index)
   {
      checkOptions(index);
      projectOptions[index] = value;
      flushOptions();   
   }  
      
   public void readOptions(String fileName)
   {      
        prjName = fileName;          
        String prjOptStr = prefs.getPref("prjOpt" + fileName);
        projectOptions = StringUtils.toArray(prjOptStr, ";", 100);
   }   
   
   public void flushOptions()
   {
      if (prjName.compareTo("") == 0)
         return;      
      String s = "qqq;";
      for (int i = 0; i < projectOptions.length; i++)
         s += projectOptions[i] + ";";
      prefs.setPref("prjOpt" + prjName, s);
   }    
}