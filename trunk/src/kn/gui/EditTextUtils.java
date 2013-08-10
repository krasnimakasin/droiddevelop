package kn.gui;

import android.widget.EditText;

public class EditTextUtils
{
  private static String genSpaces(int start, String s)
  {
     int SpaceCount = 0;
     for (int i = start - 2; i > -1; i--)
     {
        int code = s.codePointAt(i);
        if (code == 10)
           break;
        if (code == 32)
          SpaceCount++;
        else
          SpaceCount = 0;
     }
    String res = "";
    for (int j = 0; j < SpaceCount; j++)
       res += " ";
    return res;
  } 

  public static void appendEmptySpaces(EditText t)
  {     
     try
     {
        int start = t.getSelectionStart();
        t.getEditableText().insert(start, genSpaces(start, t.getText().toString()));
     }
     catch (Exception e)
    {
      //new MessageBox(parent, null).ShowEmpty(e.getMessage());
    }
  }
}