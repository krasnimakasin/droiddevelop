package kn.gui;

import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.Button;
import android.app.Activity;
import android.view.View;

//import kn.gui.MessageBox;

public class SearchGroup extends LinearLayout
{
   private EditText searchInput = null;
   private EditText  tv;
   private Button close;
   private Button next;
   private Activity parent;

   public SearchGroup(Activity aParent, EditText aTv)
   {
      super(aParent);        
      parent = aParent;       
      tv = aTv;
      searchInput = new EditText(parent); 
      
      next = new Button(aParent);
      next.setText("Next"); 
      next.setOnClickListener(new View.OnClickListener() 
                   { public void onClick(View v) 
                   {toNext();} });
                   
      Button prev = new Button(aParent);
      prev.setText("Prev"); 
      prev.setOnClickListener(new View.OnClickListener() 
                   { public void onClick(View v) 
                   {toPrev();} });
                   
      close = new Button(aParent);
      close.setText("Close"); 
      close.setOnClickListener(new View.OnClickListener() 
                   { public void onClick(View v) 
                   {hide();} });              
             
      addView(next);       
      addView(prev);                        
      addView(close);
      addView(searchInput, new LayoutParams(-1, -2));
      //searchInput.setMinWidth(searchInput.getWidth() + aTv.getWidth() -this.getWidth());
      hide();    
   }
   
   private void Navigate(Boolean next)
   {
        tv.requestFocus();
        int start = tv.getSelectionStart();
        String query = searchInput.getText().toString();
        String text = tv.getText().toString();
        int n;
        if (next)
           n = text.indexOf(query, start + 1);
        else
           n = text.lastIndexOf(query, start - 1);        
        if (n < 0)
        {
           if (next)
              n = text.indexOf(query);
           else
              n = text.lastIndexOf(query);
        }
        if (n >= 0)
          tv.setSelection(n, n + query.length());   
   }
   
   public void toNext()
   {
       Navigate(true);
   }
   
   public void toPrev()
   {
        Navigate(false);
   }
   
   public void hide()
   {
       setVisibility(8); //gone   
   }   
   
   public void show()
   {
      setVisibility(0); //visible           
   }
}
