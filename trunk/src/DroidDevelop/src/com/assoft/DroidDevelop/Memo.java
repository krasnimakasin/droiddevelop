package com.assoft.DroidDevelop;

import android.app.Activity;
import android.content.res.TypedArray;

import kn.gui.JavaMemo;

public class Memo extends JavaMemo
{
    public Memo(Activity aParent, int[] attr) 
    { 
       super(aParent);  
       TypedArray a = aParent.obtainStyledAttributes(attr);
       initializeScrollbars(a);
       a.recycle();       
       setScrollContainer (true);       
    }
};