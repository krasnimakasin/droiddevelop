package kn.utils;

import android.app.Activity;
import android.content.pm.*;
import java.util.List;
import android.content.Intent;

import kn.data.StringUtils;
import kn.gui.MessageBox;

public class Plugins
{
  protected Activity parent;
  
  private Intent[] intentsList = null;
  private String[] fullRefactItentNamesArray = null;

  public static String pkgName = "android.intent.extra.PackageName";
  public static String clsName = "android.intent.extra.ClassName";
  
  public Plugins(Activity aParent)
  {
    parent = aParent;
     try 
     {
         getRefactItentNamesQuery();
     }
     catch (Exception e) 
     {
     }
  }
  
  public void processRefactItemQuery(int index, String name, Intent intent)
  {
  
  }
    
  public void showDlg()
  {
     try
     {
        String[] arr = fullRefactItentNamesArray; 
        new MessageBox(parent, null)
        {
           public void selfBtnListen(MessageBox msg, int which)
           {
               if (which < fullRefactItentNamesArray.length)
                   processRefactItemQuery(which, fullRefactItentNamesArray[which], intentsList[which]);
           }    
        }.choose("Other actions", arr);
      }
      catch  (Exception e) 
      {
         new MessageBox(parent, null).ShowEmpty("Unknown error 3");
      }  
  }
  
  public void fillItentNamesArray(Intent intent)
  { 
    addArray(null, intent);
  }

  protected String getNamesItentName()
  {
     return "";
  }

  protected int getNamesItentNameAct()
  {
     return -1;
  }

    public void getRefactItentNamesQuery()
    {
      List<ResolveInfo> list = getActivitiesForAction(getNamesItentName());
      if (list != null)
      {
         for (ResolveInfo ri : list)
         {
            Intent intent = new Intent().setAction(getNamesItentName());  
            intent.setClassName(ri.activityInfo.packageName, ri.activityInfo.name);
            intent.putExtra(pkgName, ri.activityInfo.packageName);
            intent.putExtra(clsName, ri.activityInfo.name);
            parent.startActivityForResult(intent, getNamesItentNameAct());  
         }
      }
   } 
    
  protected List<ResolveInfo> getActivitiesForAction(String act)
  {
     Intent intent = new Intent().setAction(act);
     PackageManager pm = parent.getPackageManager();
     return pm.queryIntentActivities(intent, 0);
  }
  
  protected void addArray(String[] arr, Intent intent)
  {
    if (intent == null)
      fullRefactItentNamesArray = doFillItentNamesArrayByArr(fullRefactItentNamesArray, arr, intent);
    else
      fullRefactItentNamesArray = doFillItentNamesArray(fullRefactItentNamesArray, intent);
  }
  
  private String[] doFillItentNamesArrayByArr(String[] addArr, String[] arr, Intent intent)  
  {
     if (arr == null)
       return addArr;
     if (addArr == null)
     {
        intentsList = new Intent[arr.length];
        for (int k = 0; k < arr.length; k++)
          intentsList[k] = intent;
        return arr;
     }
     Intent[] intentArr = new Intent[addArr.length + arr.length];
     String[] res = new String[addArr.length + arr.length];
     int n = 0;
     for (int i = 0; i < addArr.length; i++)
     {
       res[n] = addArr[i];
       intentArr[n] = intentsList[i];
       n++;
     }
     for (int j = 0; j < arr.length; j++)
     {
       res[n] = arr[j]; 
       intentArr[n] = intent;
       n++;
     }
     intentsList = intentArr;
     return res;  
  }
  
  private String[] doFillItentNamesArray(String[] addArr, Intent intent)
  {
     String[] arr = parseRefactItentNames(intent);
     return doFillItentNamesArrayByArr(addArr, arr, intent);
  }
  
  public String[] parseRefactItentNames(Intent I)
  {
     String s = I.getData().toString();
     int n = StringUtils.wordCount(s, "|");
     String[] res = new String[n];
     for (int i = 0; i < n; i++)
       res[i] = StringUtils.extractWord(s, "|", i);
     return res;
  }
}