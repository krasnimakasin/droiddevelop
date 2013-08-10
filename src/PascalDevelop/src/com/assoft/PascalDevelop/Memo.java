package com.assoft.PascalDevelop;

import android.app.Activity;
import android.content.res.TypedArray;

import kn.gui.JavaMemo;

public class Memo extends JavaMemo
{
    public Memo(Activity aParent, int[] attr) 
    { 
       super(aParent);
       syntax = stPascal;  
       TypedArray a = aParent.obtainStyledAttributes(attr);
       initializeScrollbars(a);
       a.recycle();       
       setScrollContainer (true);       
    }
};