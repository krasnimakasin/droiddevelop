package kn.gui;

import android.app.Activity;
import android.text.Html;
import android.text.method.LinkMovementMethod;

import kn.gui.TextDialog;

public class Dialogs
{       
    public static TextDialog showHtml(Activity parent, String text, String title, boolean readOnly, Html.ImageGetter img, int width)
    {  
      TextDialog d = new TextDialog(parent, title, readOnly);
      d.txt.setMovementMethod(LinkMovementMethod.getInstance());
      d.txt.setText(Html.fromHtml(text, img, null));
      d.txt.setWidth(width);
      d.txt.setGravity(0x01);
      d.dlg.show();
      return d;
    }
  
    public static TextDialog showText(Activity parent, String text, String title, boolean readOnly)
    {
      TextDialog d = new TextDialog(parent, title, readOnly);
      d.txt.setText(text);
      d.dlg.show();
      return d;
    }
}