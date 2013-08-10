package kn.utils;

public class NativeSettings
{
  public NativeSettings()
  {
    System.loadLibrary("NativeSettings");
  }
  native public int openSettings(String fileName);
  native public String getValue(int hSettings, String name);
  native public int setValue(int hSettings, String name, String value);
  native public int release(int hSettings, String fileName);
}