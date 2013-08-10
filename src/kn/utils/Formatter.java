package kn.utils; 

import android.app.Activity;
import android.text.Editable;
import android.widget.EditText;

import kn.gui.MessageBox;
import kn.gui.EditTextUtils;

public class Formatter
{ 
  private static int step = 2;
  private static void appendSpaces(Editable text, int index, int count)
  {
    String s = "";
    for (int i = count; i > 0; i--)
    s += " ";
    if (count > 0)
    text.insert(index, s);
    else if (count < 0)
    text.delete(index + count, index);
  }
  
  private static Boolean hasAddDeptWord(String s, int Index)
  {
    return ((s.indexOf("if", Index) == 1) || (s.indexOf("else", Index) == 2) ||
                  (s.indexOf("for", Index) == 0));
  }
  
  private static int textLen(Editable text)
  {
     return text.toString().length();
  }
  
  private static void addLine(EditText t, int start, int oldLen)
  {
     int[] arr = {10};
     Editable text = t.getEditableText();
     text.insert(start + textLen(text) - oldLen, new String(arr, 0, 1));
     t.setSelection(start + textLen(text) - oldLen, start + textLen(text) - oldLen);    
     EditTextUtils.appendEmptySpaces(t);  
  }
  
  private static void addText(EditText t, int start, int oldLen, String textStr)
  {
     Editable text = t.getEditableText();
     text.insert(start + textLen(text) - oldLen, textStr);
  }
  
  public static void trycatch(Activity parent, EditText t)
  {
     Editable text = t.getEditableText();
     int start = t.getSelectionStart();
     int end = t.getSelectionEnd();
     if (start > end)
     {
        int temp = start;
        start = end;
        end = temp;
     }
     t.setSelection(start, start);
     int oldLen = textLen(text);
     //int[] arr = {32};
     
     EditTextUtils.appendEmptySpaces(t);
     addText(t, start, oldLen,  "try {");     
     addLine(t, start, oldLen);
     int n = textLen(t.getEditableText()) - oldLen;
     int bodyStart = start + n;
     int bodyEnd = end;

     addLine(t, end, oldLen);
     addText(t, end, oldLen, "}");
     addLine(t, end, oldLen);
     addText(t, end, oldLen, "catch (Exception e) {");
     addLine(t, end, oldLen);
     addText(t, end, oldLen, "}");
     
     for (int i = bodyEnd; i > bodyStart + n; i--)
     {
       int code = t.getEditableText().toString().codePointAt(i);      
       if (code == 10)
         t.getEditableText().insert(i + 1, "   ");
     }
  }
  
  private static void doFormatAsDelphi(Activity parent, Editable text)
  {
    int dept = 0;
    int delta = 0;
    int spaces = 0;
    int ignores = 6;
    Boolean collectSpaces = false;
    Boolean needAddDept = false;
    String s = text.toString();
    for (int i = 0; i < s.length(); i++)
    {
      int code = s.codePointAt(i);      
      if (collectSpaces)
      {
        if (code == 32)
        spaces++;
        else
        {
          collectSpaces = false;
          int adept;
          if (code == 125)
          adept = dept - 1;          
          else
          adept = dept;
          if (needAddDept)
          adept++;        
          needAddDept = hasAddDeptWord(s, i - 2);         
          int d = step * adept - spaces;
          if (step * adept > spaces - ignores)
          {
            appendSpaces(text, i + delta - 1, d);
            delta += d;
          }
        }
      }
      
      if (code == 123)
      {
        dept++;
        needAddDept = false;
      }
      else if (code == 125)
      dept--;
      else if (code == 10)
      {
        collectSpaces = true;
        spaces = 0;
      }
    }
  }
  
  private static void formatAsDelphi(Activity parent, Editable text)
  {
    try
    {
      doFormatAsDelphi(parent, text);
    }
    catch (Exception e)
    {
      new MessageBox(parent, null).ShowEmpty("Error: " + e.getMessage());
    }
  }
  
  private static void formatAsCpp(Activity parent, Editable text)
  {
    
  }
  
  public static void format(Activity parent, Editable text)
  {
    String[] arr = {"Delphi style",
                             "C++ style"};
    MessageBox msg = new MessageBox(parent, null)
    {
      public void selfBtnListen(MessageBox msg, int which)            
      {
        
        Editable text = (Editable)msg.objParam;                                                                                                                                                                                                                                                        
	 if (which == 0)                                                                                                                                                                                                                                                         
	    Formatter.formatAsDelphi(msg.parent, text);
        else if (which == 1)
        Formatter.formatAsCpp(msg.parent, text);
      }    
    };
    msg.objParam = text;
    msg.choose("Other actions", arr);
  }
}