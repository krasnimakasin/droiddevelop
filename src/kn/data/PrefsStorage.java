package kn.data;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PrefsStorage
{
  private Activity parent;
  
  public PrefsStorage(Activity aParent)
  {
    parent = aParent;
  }
  
  private SharedPreferences getPref()
  {
    return PreferenceManager.getDefaultSharedPreferences(parent);
  }
  
  public Boolean hasPref(String key)
  {
     SharedPreferences p = getPref();
     return p.getAll().get(key) != null;
  }
  
  public String getPref(String key)
  {
     SharedPreferences p = getPref();
     if (p.getAll().get(key) != null)
       return p.getAll().get(key).toString();
     else
       return "";
  }
  
  public void setPref(String key, String value)
  {
    SharedPreferences p = getPref();
    Editor e = p.edit();
    e.putString(key, value.toString());    
    e.commit();
  }  
}