package com.assoft.DroidDevelop;

import android.app.Activity;
import android.content.pm.*;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class NewPrjActivity extends Activity
{
  //private TextView tv;
  
//===================================================================
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
//===================================================================
  {
    setContentView(R.layout.prjopt);
    //tv = (TextView)findViewById(R.id.vwInfo);
    //fnShowInfos();
    super.onCreate(savedInstanceState);
  }
} // InfoActivity
