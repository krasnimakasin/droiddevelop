package kn.gui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.widget.EditText;

import kn.utils.Task;

public class MessageBox
{  
  public Activity parent;
  private DialogInterface.OnClickListener buttonListener;
  public int icon;
  public int intParam;
  public Object objParam;
  public String strParam;
  public Boolean boolParam;
  public EditText input = null;
  public String neutralBtn = "";
  public Task task = null;
  public String[] items;
    
  public void selfBtnListen(MessageBox msg, int which)
  {
    
  }
  
  public void OnBackPressed()
  {
  
  }

  private Dialog  createDialog(AlertDialog.Builder builder)
  {
         Dialog dlg = builder.create();
         dlg.setOnCancelListener(new DialogInterface.OnCancelListener()
                                                                 {
                                                                    public void onCancel(DialogInterface dialog)
                                                                    {
                                                                       OnBackPressed();
                                                                    }
                                                                 });
         return dlg;
    }
                                                                 
    
  public void doSelfBtnListen(int which)
  {
     selfBtnListen(this, which);
  }
  
  public MessageBox(Activity aParent, DialogInterface.OnClickListener aButtonListener)
  {
    parent = aParent;
    buttonListener = aButtonListener;
    
    if (buttonListener == null)
    {
      buttonListener = new DialogInterface.OnClickListener() 
      {
        public void onClick(DialogInterface dialog, int which)
        {
          doSelfBtnListen(which);
        }
      };
    }
  }
  
  public Activity getParent()
  {
    return parent;
  }
    
  private AlertDialog.Builder CreateEmpty(String caption)
  {  
    AlertDialog.Builder builder = new AlertDialog.Builder(parent);
    if (caption != "")
      builder.setTitle(caption);
    if (icon != 0) 
      builder.setIcon(icon);
    if (neutralBtn != "")    
      builder.setNeutralButton(neutralBtn, buttonListener);     
    return builder;
  }
  
  public void ShowEmpty(String caption)
  {
    AlertDialog.Builder dlg = CreateEmpty(caption);
    createDialog(dlg).show();
  }
  
  public static void showEmptyOk(Activity parent, String caption, String message)
  {
    MessageBox msg = new MessageBox(parent, null);
    AlertDialog.Builder dlg = msg.CreateEmpty(caption);
    dlg.setMessage(message);
    dlg.setNegativeButton("Ok", msg.buttonListener);
    msg.createDialog(dlg).show();
  }
    
  public void Ask(String caption)
  {  
    AlertDialog.Builder builder = CreateEmpty(caption); 
    builder.setPositiveButton("Yes", buttonListener);
    builder.setNegativeButton("No", buttonListener);
    createDialog(builder).show();    
  }
  
  public void choose(String caption, String[] s)
  {
    try
    {
      AlertDialog.Builder builder = CreateEmpty(caption);
      builder.setItems(s, buttonListener);
      items = s;
      createDialog(builder).show();  
    }
    catch (Exception e)
    {
    
    }    
  }
  
  public void input(String caption, String def)
  {
    AlertDialog.Builder builder = CreateEmpty(caption);
    input = new EditText(parent);
    input.setText(def);
    input.setSelection(0, def.length());
    builder.setView(input);    
    builder.setPositiveButton("Ok", buttonListener);
    builder.setNegativeButton("Cancel", buttonListener);
    createDialog(builder).show();    
  }
}

