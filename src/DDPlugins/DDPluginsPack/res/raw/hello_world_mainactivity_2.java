package com.assoft.HelloWorld;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.app.AlertDialog;

public class MainActivity extends Activity
{
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
  }
  
  public void OnClickBtnHello(final View view)
  {
     new AlertDialog.Builder(this).setTitle("Hello World!!!").show();
  }  
  
} 
