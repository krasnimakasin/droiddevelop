package com.assoft.DDPluginsPack;

import android.app.Activity;
import android.content.Intent;

import kn.gui.MessageBox;
import kn.data.TextFiles;

public class LearnProgramming
{
   private Activity parent;
   
   public LearnProgramming(Activity aParent)
   {
     parent = aParent;
   }
   
   private void addCmd(Intent intent, String cmdName, String param1, String param2)
   {
      int commandsCount = intent.getIntExtra("android.intent.extra.CommandsCount", 0);        
      String suf = Integer.toString(commandsCount);
      commandsCount++;
      intent.putExtra("android.intent.extra.CommandsCount", commandsCount);
      intent.putExtra("android.intent.extra.CommandName" + suf, cmdName);
      intent.putExtra("android.intent.extra.CommandData" + suf, param1);
      intent.putExtra("android.intent.extra.CommandDataA" + suf, param2);      
   }
   
   private void helloWorld()
   {
      tryProcessStep("hello_world_0");      
   }
   
   private void copyTextFileFromRaw(int resID, String resFile)
   {
      TextFiles t = new TextFiles(parent);
      t.showToast = false;
      String str = TextFiles.readTextFileFromRaw(parent, resID);      
      t.saveFile(resFile, str);
   }   
   
   private void addPanel(Intent intent, int stepInd)
   {
          String s1 = Integer.toString(stepInd - 1);
          String s2 = Integer.toString(stepInd + 1);
          addCmd(intent, "RemovePanel", "Panel1", "");
          addCmd(intent, "AddPanel", "Panel1", "");          
          addCmd(intent, "AddLastPanelButton", "hello_world_" + s1, "Prev");          
          addCmd(intent, "AddLastPanelButton", "hello_world_" + s2, "Next");             
          addCmd(intent, "ShowPanel", "Panel1", "");                  
   }
   
   private void changeFile(Intent intent, int resID, String resFile)
   {
          copyTextFileFromRaw(resID, resFile); 
          addCmd(intent, "OpenFile", resFile, "");   
   }
   
   private void highlight(Intent intent, int start, int end)
   {
          addCmd(intent, "ClearHighlight", "", "");         
          addCmd(intent, "HighlightText", Integer.toString(start), Integer.toString(end));                        
   }
   
   public void tryProcessStep(String stepName)
   {
       Intent intent = parent.getIntent();
       if (stepName.indexOf("hello_world_") != 0)
         return;
       String stepStr = stepName.replace("hello_world_", "");
       int step = Integer.parseInt(stepStr);
       addPanel(intent, step);
       String path = intent.getStringExtra("android.intent.extra.ProjectPath");               
       if (step == 0)
       {  
          addCmd(intent, "ShowText", "This is lesson to create HelloWorld program\n" +
                                                             "On next step will created new empty project\n" +
                                                             "This is similar to 'New Project/New Empty' command from main menu\n\n" +
                                                             "Note: this step can be during some time. Please wait.", "");
       }    
       else if (step == 1)
       {
          addCmd(intent, "CreateProject", "HelloWorld", "");          
          addCmd(intent, "AddLastPanelText", "This is empty\nproject.", "");
       }
       else if (step == 2)
       {
          changeFile(intent, R.raw.hello_world_main_0, path + "/res/layout/main.xml");   
          addCmd(intent, "ShowText", "So. We want to create one button.\n" +
                                                             "When we press this button we will have message with 'Hello World' text\n" +
                                                             "You can add controls (including button) to special xml files.\n" +
                                                             "In the most of DroidDevelop example this file is '/res/layout/main.xml'\n\n" +
                                                             "Now we open this file.", "");          
       }
       else if (step == 3)
       {
          addCmd(intent, "ShowText", "Add button control to it\n" +                 
                                                              "You can compile and run this code, but on you press button, you will get fail\n" +
                                                              "We will fix this problem in future", "");                   
          changeFile(intent, R.raw.hello_world_main_1, path + "/res/layout/main.xml");
          highlight(intent, 407, 606);
       }
       else if (step == 4)
       {
          addCmd(intent, "ShowText", "This is unique control identifier.", "");                 
          changeFile(intent, R.raw.hello_world_main_1, path + "/res/layout/main.xml");
          highlight(intent, 420, 448);
       }      
       else if (step == 5)
       {
          addCmd(intent, "ShowText", "This is options for vertical and horizontal alignment.\n" +
                                                             "They are in 'wrap_content' value now\n" +
                                                             "You can change it to 'fill_parent' and see what happends", "");                 
          changeFile(intent, R.raw.hello_world_main_1, path + "/res/layout/main.xml");
          highlight(intent, 453, 531);
       }
       else if (step == 6)
       {
          addCmd(intent, "ShowText", "This is your button caption", "");                 
          changeFile(intent, R.raw.hello_world_main_1, path + "/res/layout/main.xml");
          highlight(intent, 536, 563);
       }       
       else if (step == 7)
       {
          addCmd(intent, "ShowText", "This is link to your on press function.\n" +     
                                                             "While we don't write this function, we will take fail on button press in result programm.\n" +
                                                             "To write it we need to open 'MainActivity.java' file", "");                      
          changeFile(intent, R.raw.hello_world_main_1, path + "/res/layout/main.xml");
          highlight(intent, 568, 602);
       }   
        else if (step == 8)
       {
          addCmd(intent, "ShowText", "This is your 'MainActivity.java file.\n" +     
                                                             "At first time most part of code you will write here.", "");                      
          changeFile(intent, R.raw.hello_world_mainactivity_0, path + "/src/com/assoft/HelloWorld/MainActivity.java");
       }  
        else if (step == 9)
       {
          addCmd(intent, "ShowText", "Add empty function for button click reaction.\n" +     
                                                             "You can compile and run program now.\n" +
                                                             "When you click button you don't fail, but don't have message too\n\n" +
                                                             "Note: when you use new external class, you need to use new module in import section\n" +
                                                             "We import class 'View' by 'imlort android.view.View' declaration", "");                      
          changeFile(intent, R.raw.hello_world_mainactivity_1, path + "/src/com/assoft/HelloWorld/MainActivity.java");
          highlight(intent, 309, 367);    
          addCmd(intent, "HighlightText", "87", "112");                                  
       }  
       else if (step == 10)
       {
          addCmd(intent, "ShowText", "Result code\n" +     
                                                             "You can compile and run it and see your message\n" +
                                                             "To see more comments, click next.", "");                       
          changeFile(intent, R.raw.hello_world_mainactivity_2, path + "/src/com/assoft/HelloWorld/MainActivity.java");
          highlight(intent, 113, 144);
          addCmd(intent, "HighlightText", "395", "459");                     
       }  
         else if (step == 11)
       {
          addCmd(intent, "ShowText", "New import for dialog", "");                       
          changeFile(intent, R.raw.hello_world_mainactivity_2, path + "/src/com/assoft/HelloWorld/MainActivity.java");
          highlight(intent, 113, 144);
       }      
         else if (step == 12)
       {
          addCmd(intent, "ShowText", "Dialog builder creation", "");                       
          changeFile(intent, R.raw.hello_world_mainactivity_2, path + "/src/com/assoft/HelloWorld/MainActivity.java");
          highlight(intent, 395, 424);
       }     
         else if (step == 13)
       {
          addCmd(intent, "ShowText", "Set message text", "");                       
          changeFile(intent, R.raw.hello_world_mainactivity_2, path + "/src/com/assoft/HelloWorld/MainActivity.java");
          highlight(intent, 424, 451);       
       }   
         else if (step == 14)
       {
          addCmd(intent, "ShowText", "Show dialog", "");                       
          changeFile(intent, R.raw.hello_world_mainactivity_2, path + "/src/com/assoft/HelloWorld/MainActivity.java");
          highlight(intent, 451, 459);
       }                  
       else 
       {
          parent.finish();
          return;         
       }
      parent.setResult(Activity.RESULT_OK, intent);
      parent.finish();                
   }
   
   public void process()
   {
        String[] arr = {"Hello World"};
        new MessageBox(parent, null)
        {
           public void selfBtnListen(MessageBox msg, int which)
           {
	         if (which == 0) 
	            helloWorld();
           }   
           public void OnBackPressed()
           {
                parent.finish();
           }            
        }.choose("", arr);     
   }   
}