package kn.outside;

import java.lang.reflect.*;
//import java.io.*;

/*
final class StripQualifiers 
{
  private StreamTokenizer st;
  public StripQualifiers(String qualified) 
  {
    st = new StreamTokenizer(
      new StringReader(qualified));
    st.ordinaryChar(' '); // Хранит пробелы
  }
  public String getNext() 
  {  
    String s = null;
    try {
      int token = st.nextToken();
      if(token != StreamTokenizer.TT_EOF) 
      {
        switch(st.ttype) {
          case StreamTokenizer.TT_EOL:
            s = null;
            break;
          case StreamTokenizer.TT_NUMBER:
            s = Double.toString(st.nval);
            break;
          case StreamTokenizer.TT_WORD:
            s = new String(st.sval);
            break;
          default: // единичный символ в ttype
            s = String.valueOf((char)st.ttype);
        }
      }
    } 
    catch(IOException e) 
    {
      System.err.println("Error fetching token");
    }
    return s;
  }
  private static String strip(String qualified) 
  {
    StripQualifiers sq = new StripQualifiers(qualified);
    String s = "", si;
    while((si = sq.getNext()) != null) 
    {
      int lastDot = si.lastIndexOf('.');
      if(lastDot != -1)
        si = si.substring(lastDot + 1);
      s += si;
    }
    return s;
  }
} ///:~
*/

public class GetClassRef 
{
  public static String[] main(String className)
  {
    try 
    {
      Class<?> c = Class.forName(className);
      Method[] m = c.getMethods();
      Constructor<?>[] ctor = c.getConstructors();
      // Конвертирует в массив "очищенных" строк:
      String[] n = new String[m.length + ctor.length];
      for(int i = 0; i < m.length; i++) 
      {
        String s = m[i].toString();
        n[i] = s.replaceAll(className + ".", "");//StripQualifiers.strip(s);
      }
      for(int i = 0; i < ctor.length; i++) 
      {
        String s = ctor[i].toString();
        n[i + m.length] = s.replaceAll(className + ".", ""); //StripQualifiers.strip(s);
      }
      return n;
    } 
    catch(ClassNotFoundException e) 
    {
      return null;
    }
  }
} ///:~
