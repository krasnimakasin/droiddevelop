package kn.data;

public class StringUtils
{
   public static int lineStartPos(String s, int lineInd)
   {
      int n = lineInd;
      int k = -1;
      while (n > 0)
      {
         k = s.indexOf(10, k + 1);
         if (k < 0)
            return k;
         n--;
      }
      return k;
   }
   
   public static String[] doToArray(String val, String delim, int maxCount)
   {
      if (val.compareTo("") == 0)
         return null;
      int n1 = 0;
      int k = 0;
      int n = val.indexOf(delim);
      if (n < 1)
        return null;
      String[] res = new String[maxCount];//val.split("----------");
      for (int i = 0; i < maxCount; i++)
         res[i] = "";
      for (int i = 0; i < maxCount; i++)
      {
         n1 = val.indexOf(delim, n + delim.length());
         if (n1 < n)
            return res;
         res[k] = (val.substring(n + delim.length(), n1));
         k++;
         n = n1;
      }
      return res;
   }
   
   private static int nonEmptyCnt(String[] arr, int maxCount)
   {
      int n = 0;
      for (int i = 0; i < arr.length; i++)
      {
         if (arr[i].compareTo("") == 0)
            return n;
         n++;
      }
      return n;
   }
   
   public static String[] toArray(String val, String delim, int maxCount)
   {
      String[] arr = doToArray(val, delim, maxCount);
      if (arr == null)
         return null;
      int n = nonEmptyCnt(arr, maxCount);
      if (n == 0)
         return null;
      String[] res = new String[n];
      for (int i = 0; i < n; i++)
         res[i] = arr[i];
      return res;      
   }
   
   public static int wordCount(String val, String delim)
   {
      int res = 1;
      int n = val.indexOf(delim);   
      while (n >= 0)
      {
         res++;
         n = val.indexOf(delim, n + 1);
      }
      return res;
   }
   
   public static String extractWord(String val, String delim, int index)
   {
      int n1 = 0;
      int n2 = val.indexOf(delim);
      if (n2 < 0) 
         return val;
      for (int i = 0; i < index; i++)
      {
         n1 = n2 + 1;
         n2 = val.indexOf(delim, n2 + 1);
         if (n2 < 0)
            return val.substring(n1, val.length());
      }
      return val.substring(n1, n2);
   }
}