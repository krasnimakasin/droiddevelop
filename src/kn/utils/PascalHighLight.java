package kn.utils;

import android.text.Spannable;

public class PascalHighLight extends HighLight
{
String[] keyWords = {"begin","end", "class","implementation","raise","abstract",
               "interface","type","virtual","public","protected","private","repeat","until","result",
               "continue","break","exit","integer","longint","float","double","byte","char",
               "boolean","constructor","destructor","true","false","null","nil","while","for",
               "if","else","then","case","default","try","except","finally","string","procedure",
               "function","uses","var","inherited","to","do","in","as","is","array","cdecl",
               "exports","program","library","property", "override", "of", "and", "or", "not",
               "xor", "unit", "initialization", "finalization", "record", "packed"};

   @Override
   protected Boolean isKeyWord(String s)
   {
      for (int i = 0; i < keyWords.length; i++)
      {
         if (keyWords[i].compareTo(s.toLowerCase()) == 0)
            return true;
      }
      return false;
    }
    
  @Override 
 protected void highlightInt(int start, int end, Spannable text, Boolean startAtMultiLine)
   {
      curText = text;
      int lastChar = 0;
      String s = text.toString();
      for (int i = start; i < s.length(); i++)
      {
         if ((i > end) && (curState != stateMultiLineComment) && (curState != stateMultiLineCommentSimple) && (!(startAtMultiLine)))
            break;
         curIndex = i;
         int curChar = s.codePointAt(i);           
         if (curState == stateUnknown)
         {
            if (curChar == 39) //'
              changeState(stateInString);
            else if ((curChar == 47)&&(lastChar == 47)) ////
              changeState(stateComment);
            else if ((curChar == 36)&&(lastChar == 123)) //${
              changeState(stateDefine);
            else if (lastChar == 123) //{
                changeState(stateMultiLineCommentSimple);
            else if ((curChar == 42)&&(lastChar == 40)) //*(
              changeState(stateMultiLineComment);
            else if ((curChar == 10)||(curChar == 32)||(curChar == 59)||
                        (curChar == 40)||(curChar == 41)||(curChar == 47)||
                        (curChar == 46)||(curChar == 44)||(curChar ==125)||(curChar ==123))
               tryHighLightWord(s);
         }
         else if (curState == stateInString)
         {
            if (curChar == 39) //'
              changeState(stateUnknown);
            if (curChar == 10)
            {
              curState = stateUnknown;
              tryHighLightWord(s);
            }
         }
         else if ((curState == stateMultiLineCommentSimple) || (curState == stateMultiLineComment) || startAtMultiLine || (curState == stateDefine))
         {
           if (((curChar == 125)&&(curState == stateMultiLineCommentSimple)) || //}
              ((curChar == 41)&&(lastChar == 42)&&(curState == stateMultiLineComment)) || //)*
              ((curChar == 125)&&(curState == stateDefine))) //}
           {
              changeState(stateUnknown);
              curWordStart = curIndex + 1;
              startAtMultiLine = false;          
           }
         }
         else if (curState == stateComment)
         {
            if (curChar == 10)
            {
              changeState(stateUnknown);
              tryHighLightWord(s);
            }
         }
         lastChar = curChar;
      }
      changeState(stateUnknown);
      tryHighLightWord(s);
   }  
}