package com.kn.DroidDevelop;

import kn.data.StringUtils;

public class ErrorParser
{
   //ERROR in unitname (at line lineNumber)
   //WARNING in unitname (at line lineNumber)
   
   public static String parseErrorUnitName(String errText)
   {
      int n = errText.indexOf(" in ");
      int n1 = errText.indexOf(" (at line ");      
      if ((n < 1) || (n1 < n))
         return "";
      return errText.substring(n + 4, n1);
   }
    
   public static int parseErrorLine(String errText)
   {    
      int n = errText.indexOf(" (at line ");
      int n1 = errText.indexOf(")", n);
      if ((n < 1) || (n1 < n))
         return -1;
      String res = errText.substring(n + 10, n1);   
      return Integer.parseInt(res);
   }
   
   static int maxErr = 50;
   
   public static String[] parse(String compResult)
   {
      return StringUtils.toArray(compResult, "----------", maxErr);
   }
   
}

