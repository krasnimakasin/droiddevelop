package kn.utils;

import android.app.Activity;
import android.view.ViewGroup;

import kn.gui.MessageBox;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class Ads
{	
	public static AdView addAds(Activity parent, ViewGroup vg, String pubId)
	{
       AdView adView  = new AdView(parent, AdSize.BANNER/*MY_BANNER_UNIT_ID*/, pubId);
		try
		{                
          vg.addView(adView);
			AdRequest request = new AdRequest();
			//adView.setAdListener(this);
			//request.setTesting(true);
			adView.loadAd(request);              
		}
		catch (Exception e)
		{
			new MessageBox(parent, null).ShowEmpty(e.getMessage());
		}
       return adView;
	}
}