package kn.utils;

import android.text.Spannable;
import android.graphics.Color;
import android.text.style.ForegroundColorSpan;
//import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;

  
  final class SyntaxHighlightSpan extends ForegroundColorSpan
  {
     public int color;
     public int state;
     public SyntaxHighlightSpan(int aColor, int aState)
     {
        super(aColor);
        color = aColor;
        state = aState;
     }
  }

public class HighLight
{
   protected static int stateUnknown                               = 0;
   protected static int stateComment                               = 1;
   protected static int stateMultiLineComment              = 2;
   protected static int stateInString                                  = 3;
   protected static int stateKeyWord                                = 4;
   protected static int stateMultiLineCommentSimple = 5;
   protected static int stateDefine                                     = 6;

   protected Spannable curText;
   
   String[] keyWords = {"package","import", "extends","implements","throws","abstract",
               "interface","enum","class","public","protected","private","static","final","return",
               "synchronized","volatile","void","int","long","float","double","byte","char",
               "Boolean","new","return","true","false","null","break","while","for",
               "if","else","switch","case","default","throw","try","catch","finally", "String",
               "native", "this"};

   protected Boolean isKeyWord(String s)
   {
      for (int i = 0; i < keyWords.length; i++)
      {
         if (keyWords[i].compareTo(s) == 0)
            return true;
      }
      return false;
   } 
   
   protected void tryHighLightWord(String text)
   {
      try
      {
         if (isKeyWord(text.substring(curWordStart, curIndex)))
            addSpan(curWordStart, curIndex - 1, stateKeyWord);
         curWordStart = curIndex + 1;
      }
      catch (Exception e)
     {
     }
   }

   private int getColorByState(int state)
   {
      if ((state == stateComment)||(state == stateMultiLineComment)||(state == stateMultiLineCommentSimple))
        return Color.parseColor("#50c050");
      else if (state == stateInString)
        return Color.parseColor("#800050");
      else if (state == stateDefine)
        return Color.parseColor("#FF0000");
      else
        return Color.parseColor("#5050c0");
   }

  protected Boolean getNeedDecStart(int state)
  {
     return ((state == stateComment)||(state == stateMultiLineComment)||
                  (state == stateDefine)||(state == stateMultiLineCommentSimple));
  }

   private void addSpan(int start, int end, int state)
   {
       if (state == stateUnknown)
          return;
       int color = getColorByState(state);
       CharacterStyle span = new SyntaxHighlightSpan(color, state);
       if (getNeedDecStart(state))
          curText.setSpan (span, start - 1, end + 1, 33);//SPAN_INCLUSIVE_INCLUSIVE
       else
          curText.setSpan (span, start, end + 1, 33);
   }

   protected int curState = stateUnknown;
   protected int curStateStart = 0;
   protected int curWordStart = 0;
   protected int curIndex = 0;

   protected void changeState(int newState)
   {
      addSpan(curStateStart, curIndex, curState);
      curState = newState;
      curStateStart = curIndex;
   }
   
   protected void highlightInt(int start, int end, Spannable text, Boolean startAtMultiLine)
   {
      curText = text;
      int lastChar = 0;
      String s = text.toString();
      for (int i = start; i < s.length(); i++)
      {
         if ((i > end) && (curState != stateMultiLineComment) && (curState != stateMultiLineCommentSimple)  && (!(startAtMultiLine)))
            break;
         curIndex = i;
         int curChar = s.codePointAt(i);
         if (curState == stateUnknown)
         {
            if (curChar == 34) //"
              changeState(stateInString);
            else if ((curChar == 47)&&(lastChar == 47)) ////
              changeState(stateComment);
            else if ((curChar == 42)&&(lastChar == 47)) //*/
              changeState(stateMultiLineComment);
            else if ((curChar == 10)||(curChar == 32)||(curChar == 59)||
                        (curChar == 40)||(curChar == 41)||(curChar == 47)||
                        (curChar == 46)||(curChar == 44)||(curChar ==125)||(curChar ==123))
               tryHighLightWord(s);
         }
         else if (curState == stateInString)
         {
            if (curChar == 34) //"
              changeState(stateUnknown);
            if (curChar == 10)
            {
              curState = stateUnknown;
              tryHighLightWord(s);
            }
         }
         else if ((curState == stateMultiLineComment) || startAtMultiLine)
         {
            if ((curChar == 47)&&(lastChar == 42)) ///*
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

   public void highlight(Spannable text)
   {
      highlightInt(0, text.toString().length() - 1, text, false);
   }
   
   public String highlightChanged(Spannable text, int aStart, int before, int after)
   {
       String s = text.toString();                     

       int start;
       int end;
                    
       for (start = aStart - before; start > 0; start--)
           if ((s.codePointAt(start) == 10) && (start != aStart - before))
              break;              
        if (start < 0)
           start = 0;      
        for (end = aStart + after; end < s.length(); end++)
           if (s.codePointAt(end) == 10)
              break;
       if (end > s.length() - 1)
           end = s.length() - 1;

       curState = stateUnknown;
       curStateStart = start;
       Boolean startAtMultiLine = false;
      
       String debug = Integer.toString(aStart) + " " + Integer.toString(before) + " " + Integer.toString(after) + " " + Integer.toString(start) + " " + Integer.toString(end) + " ";
       Class<SyntaxHighlightSpan> c = SyntaxHighlightSpan.class;//.getClass();
       SyntaxHighlightSpan[] list = text.getSpans (start, end, c) ;         
       for (int i = 0; i < list.length; i++)
       {
           SyntaxHighlightSpan span = list[i];
           int spanStart = text.getSpanStart(span) + 1;
           int spanEnd = text.getSpanEnd(span);
           //if ((spanStart >= start) && (spanEnd <= start))
           if ((span.state == stateMultiLineComment) || (span.state == stateMultiLineCommentSimple))
           {
              startAtMultiLine = true;
           }
           
            if (/*(span.state == stateMultiLineComment)&&*/(spanStart < start))
            {
               debug += Integer.toString(spanStart) + " " + Integer.toString(spanEnd) + " ";
               curState = span.state;   
               curStateStart = spanStart;
               if ((span.state == stateMultiLineComment) || (span.state == stateMultiLineCommentSimple))
                  startAtMultiLine = true;
            }
       }
              
       for (int j = list.length - 1; j >= 0 ; j--)
       {
          SyntaxHighlightSpan span = (SyntaxHighlightSpan)list[j];
          //if ((span.start >= start) && (span.end <= end))
             text.removeSpan(span);
       }       
       
       highlightInt(start, end, text, startAtMultiLine);
       return debug;
   }
}