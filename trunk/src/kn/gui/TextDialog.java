package kn.gui;

import android.app.Activity;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.LinearLayout;
import android.app.Dialog;

//for wrap out parameters on Java
final class outObjCont
{
  public Object obj = null;
}

public class TextDialog
{
   public Dialog dlg;
   public TextView txt;
   public ScrollView scroll;
   public LinearLayout layout;
   
    private static TextView createTextView4ShowText(Activity parent, boolean readOnly)
    {
      if (readOnly)
        return new TextView(parent);
      else
        return new EditText(parent);  
    }
    
   public TextDialog(Activity parent, String title, boolean readOnly)
   {
      dlg = new Dialog(parent);
      txt = createTextView4ShowText(parent, readOnly);
      layout = new LinearLayout(parent);
      layout.setOrientation(1);
      scroll = new ScrollView(parent);
      layout.addView(scroll);
      scroll.addView(txt);
      dlg.setContentView(layout);
      dlg.setTitle(title);            
   }
}