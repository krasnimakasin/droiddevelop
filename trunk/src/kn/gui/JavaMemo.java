package kn.gui;

import android.widget.EditText;
import android.widget.TextView;
//import android.util.AttributeSet;
//import android.content.Context;
import android.graphics.Rect;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.KeyEvent;
import android.app.Activity;
import android.text.style.*;
//import android.text.Editable;

//import java.util.Timer;
//import java.util.TimerTask;

//import kn.data.StringUtils;
import  kn.gui.MessageBox;
import kn.gui.EditTextUtils;
import kn.utils.HighLight;
import kn.utils.PascalHighLight;

public class JavaMemo extends EditText
{
    public static int stJava = 0;
    public static int stPascal = 1;
    
    public static Boolean highLight = false;
    public static Boolean forceHighlight = false;
    public int syntax = stJava;
    private Rect mRect; 
    private Paint mPaint;     
    private static int leftPadding = 25;
    private Activity parent;
    
    public JavaMemo(Activity aParent) 
    { 
       super(aParent);  
       parent = aParent;
       setTextSize(12);
       mRect = new Rect(); 
       mPaint = new Paint(); 
       mPaint.setStyle(Paint.Style.STROKE); 
       mPaint.setColor(0x800000FF); 
       
       setPadding(leftPadding, 0, 0, 0);
       setHorizontallyScrolling (true);
       //setSingleLine(true);
       //setVerticalScrollBarEnabled (true);
       //setVerticalFadingEdgeEnabled (true);
       setVerticalScrollBarEnabled(true);
    }

  private void appendEmptySpaces()
  {
     EditTextUtils.appendEmptySpaces(this);
  }
  
  public void setBackGroundHighlight(int color, int start, int end)
  {
     CharacterStyle span = new BackgroundColorSpan(color);
     //int i = Integer.parseInt(getText().toString().substring(0, 3), 34);
     getText().setSpan (span, start, end, 33);//SPAN_INCLUSIVE_INCLUSIVE
  }
  
  public void removeAllBackGroundHighlight()
  {
       Class<BackgroundColorSpan> c = BackgroundColorSpan.class;//.getClass();
       BackgroundColorSpan[] list = getText().getSpans (0, getText().length(), c) ;           
       for (int j = list.length - 1; j >= 0 ; j--)
          getText().removeSpan((BackgroundColorSpan)list[j]);         
  }
      
  @Override
  public boolean onKeyDown (int keyCode, KeyEvent event)
  {
     boolean res = super.onKeyDown (keyCode, event);
     //new MessageBox(parent, null).ShowEmpty(Integer.toString(keyCode));
     if (keyCode == 66) //Enter
        appendEmptySpaces();
     return res;
  }
  
/*  
   private void doGotoLine(int line)
   {
      int linePos = StringUtils.lineStartPos(getText().toString(), line);
      if (linePos >= 0)
        setSelection(linePos);
   }
*/
    
   @Override
   protected void onSelectionChanged (int selStart, int selEnd)
   {
      super.onSelectionChanged(selStart, selEnd);
//      if (selStart == selEnd)
//         doGotoLine(selStart);
      //setHeight(100);      
   }
   
   @Override
   public boolean onTouchEvent (MotionEvent event)
   {
      
      //append("q");
      //moveCursorToVisibleOffset();
      boolean res = super.onTouchEvent(event);     
      computeScroll();
      return res;
   }
 
   @Override
   protected void onDraw(Canvas canvas) 
   { 
      int count = getLineCount(); 
      Rect r = mRect; 
      Paint paint = mPaint;
      for (int i = 0; i < count; i++) 
      { 
         getLineBounds(i, r); 
         String text = Integer.toString(i + 1);
         canvas.drawText(text, r.left - leftPadding + 3, r.top + r.height() - 3, paint); 
      }  
      super.onDraw(canvas); 
   }
   
   //private Timer timer = new Timer();
   private Boolean inHighLight = false;
   
   private void reHighLight(int start, int before, int after)
   {
      if (inHighLight)
        return;
      inHighLight = true;
      try
      {        
         if (highLight || forceHighlight)
         {
            //clearSpans();
            //newHighLight().highlight(getText(), spans);            
            //new MessageBox(parent, null).ShowEmpty(Integer.toString(start) + " " + Integer.toString(before) + " " + Integer.toString(after));
            /*String s = */newHighLight().highlightChanged(getText(), start, before, after);
            //parent.setTitle(s);
         }          
       }
       catch (Exception e)
       {
           new MessageBox(parent, null).ShowEmpty("Error " + e.getMessage());
       }
       inHighLight = false;   
   }

   @Override    
   protected void onTextChanged (CharSequence text, int start, int before, int after)
   { 
      super.onTextChanged (text, start, before, after);
   /*
      timer.cancel();
      TimerTask t = new TimerTask()
      {
         public void run()
         {
          */
             reHighLight(start, before, after);             
          /*
         }
      };
      timer.schedule (t, 1000);           
      */
   }
   
   private HighLight newHighLight()
   {
     if (syntax == stPascal)
       return new PascalHighLight();
     return new HighLight();
   }
   
   @Override 
   public void setText (CharSequence text, TextView.BufferType type)
   {
      inHighLight = true;
      super.setText (text, type);
      try
     {
        if (highLight || forceHighlight)
           newHighLight().highlight(getText());
     }
     catch (Exception e)
     {
          new MessageBox(parent, null).ShowEmpty("Error " + e.getMessage());
     }
     inHighLight = false;
   }
};