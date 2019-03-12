package com.eastcom.social.pos.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eastcom.social.pos.R;


public class LoadingDialog extends Dialog {
	private TextView tv;  
	private String title;
	  
    public LoadingDialog(Context context,String title) {  
        super(context, R.style.loadingDialogStyle);  
        this.title =title;
    }  
  
    private LoadingDialog(Context context, int theme) {  
        super(context, theme);  
    }  
   
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.view_loading);  
        tv = (TextView)this.findViewById(R.id.tv);  
        tv.setText(title);  
        LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.LinearLayout);  
        linearLayout.getBackground().setAlpha(210);  
    }  
    
//    public class Builder{
//    	private Context context;
//		private String title;
//
//		public Builder(Context context) {
//			this.context = context;
//		}
//		
//		public Builder setTitle(String title) {
//			this.title = title;
//			return this;
//		}
//    } 
}
