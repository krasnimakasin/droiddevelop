package com.assoft.DroidDevelop;

import android.app.Activity;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.app.Dialog;
import android.view.View;

import kn.gui.MessageBox;
import kn.data.FileUtils;

public class PrjDialog
{
   private Dialog dlg;
   
   private EditText pathEdit;
   private EditText nameEdit;
   private EditText packageEdit;
   private Button okButton;
   
   private LinearLayout view;
   private Activity parent;
   private Project project;
   
   private EditText addInput(String labText,  String inpText)
   {
      TextView lab = new TextView(parent);
      lab.setText(labText);
      view.addView(lab);      
      EditText res = new EditText(parent);
      res.setText(inpText);
      view.addView(res);         
      return res;
   }
   
   private void doCreate()
   {
      project.newProject(pathEdit.getText().toString(), packageEdit.getText().toString(), nameEdit.getText().toString());   
      dlg.cancel();
   }
   
   public PrjDialog(Activity aParent, Project aProject)
   {
   try {
         parent = aParent;
         project = aProject;
         dlg = new Dialog(parent);      
         
         ScrollView scrollView = new ScrollView(parent);
         view = new LinearLayout(parent);
         scrollView.addView(view); 
         view.setOrientation(1);
  
         pathEdit = addInput("Project Path", FileUtils.pathToSDCard() + "myproject");                               
         nameEdit = addInput("Project Name", "MyProject");
         packageEdit  = addInput("Project Package", "com.MyCompany");
         okButton = new Button(parent);
         okButton.setText("Create"); 
         okButton.setOnClickListener(new View.OnClickListener() 
                     { public void onClick(View v) 
                     {doCreate();} });         
         view.addView(okButton);
         
         dlg.setContentView(scrollView);
         dlg.setTitle("Create New Project");            
      }
      catch (Exception e) {
        new MessageBox(parent, null).ShowEmpty(e.getMessage());
      }
   }
   
   public void show()
   {  
	 dlg.show();
   }
}