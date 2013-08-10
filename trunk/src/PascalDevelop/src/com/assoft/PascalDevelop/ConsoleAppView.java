package com.assoft.PascalDevelop;

import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.text.style.ForegroundColorSpan;
import android.graphics.Color;
import android.text.Spannable;

import java.io.*;

import kn.gui.TextDialog;
//import kn.utils.ConsoleProxy;
import kn.utils.Ads;

import com.google.ads.AdView;

public class ConsoleAppView
{
  private TextDialog dlg;
  private OutputStream outStrm;
  private TextView input;
  private AdView adView;
  //private ConsoleProxy cp;
  
  public ConsoleAppView(Activity aParent, InputStream is, OutputStream os, Boolean needAds)
  {
      dlg = new TextDialog(aParent, "Pascal Program", true);
      dlg.txt.setText("", TextView.BufferType.SPANNABLE);
      outStrm = os;
      Button btn = new Button(aParent);
      btn.setText("Send"); 
      btn.setOnClickListener(new View.OnClickListener() 
                   { public void onClick(View v) 
                   {writeVal();} });
     input = new EditText(aParent);
     LinearLayout layout = new LinearLayout(aParent);
     layout.addView(input);
     if (needAds)
       adView = Ads.addAds(aParent, layout, "a1515211284bc04");
     layout.addView(btn);
     dlg.layout.addView(layout);
     dlg.dlg.show();      
  }

  private void writeVal()
  {
    String s = input.getText().toString() + "\n";
    input.setText("");
    byte[] bytes = s.getBytes();
    try {
      outStrm.write(bytes);   
      addText(s, true);
    } catch (Exception e) {
    }
  }
 
  private void addText(String text, Boolean useSpan)
  {
      ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#5050c0")) ;
      Spannable txt = (Spannable)dlg.txt.getText();
      int oldLen = txt.length();
      dlg.txt./*getEditableText().*/append(text);
      int len = txt.length();
      if ((len > oldLen) && useSpan) 
         txt.setSpan(span, oldLen, len - 1, 33); 
  }
  
  public Boolean readAgain(byte[] bytes, int bytesCount)
  {
        String text = "";           
        if (bytesCount > 0)
           text = new String(bytes, 0, bytesCount);
        addText(text, false); 
        return true;
  }
}