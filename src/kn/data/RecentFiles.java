package kn.data;

import kn.data.PrefsStorage;

public class RecentFiles
{
  private static int maxCount = 20;
  private PrefsStorage prefs;
  
  public RecentFiles(PrefsStorage aPrefs)
  {
     prefs = aPrefs;
  }
  
  public String getLastOpenFile(int Act)
  {
     return prefs.getPref("LastOpenFile_" + Integer.toString(Act));
  }
  
  public void setLastOpenFile(int Act, String fileName)
  {
     prefs.setPref("LastOpenFile_" + Integer.toString(Act), fileName);
  }
  
  public String getItem(String prefix, int index)
  {
     if (prefs == null)
        return "";
     return prefs.getPref(prefix + Integer.toString(index));
  }
  
  private void setItem(String prefix, int index, String value, int line)
  {
     if (prefs == null)
        return;
     prefs.setPref(prefix + Integer.toString(index), value);  
     prefs.setPref(prefix + Integer.toString(index) + "_line", Integer.toString(line));  
  }
/*  
   public void addRecent(String prefix, String fileName)
  {
     addRecent(prefix, fileName, 0);
  }
*/  
  public void addRecent(String prefix, String fileName, int aLine)
  {
     String s = fileName;
     int line = aLine;
     int n = 0;
     for (int i = 0; i < maxCount; i++)
     {
        String ss = getItem(prefix, i);
        int ll = getItemLine(prefix, i);
        
        if ((fileName.compareTo(s) != 0) || (i == 0))
        {
          if (s.compareTo("") != 0)
          {
            setItem(prefix, n, s, line);
            n++;
          }
        } 
        s = ss;
        line = ll;
     }
     for (int i = n; i < maxCount; i++)
        setItem(prefix, n, "", 0);
  }
  
  public String[] getItems(String prefix)
  {
     String[] arr = new String[maxCount];
     for (int i = 0; i < maxCount; i++)
          arr[i] = getItem(prefix, i);
     return arr;
  }
  
  public int getItemLine(String prefix, int index)
  {     
     if (prefs == null)
        return 0;
        try 
        {
           return Integer.parseInt(prefs.getPref(prefix + Integer.toString(index) + "_line"));  
        }
        catch (Exception e) 
        {
           return 0;
        }
  }
}