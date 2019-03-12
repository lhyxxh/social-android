package com.eastcom.social.pos.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.eastcom.social.pos.R;
import com.eastcom.social.pos.util.FileUtils;

public class ConfigActivity extends Activity implements OnClickListener {
	private EditText et_userno;
	private EditText et_terminal;
	private EditText et_usercname;
	private EditText et_simplename;
	private Button btn_cancle;
	private Button btn_positive;
	@SuppressLint("SdCardPath")
	private final String fileName = "/mnt/sdcard/bank/SysCfg.dat";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);
		initView();
		initData();
	}

	private void initData() {
		btn_cancle.setOnClickListener(this);
		btn_positive.setOnClickListener(this);
	}

	private void initView() {
		et_userno = (EditText) findViewById(R.id.et_userno);
		et_terminal = (EditText) findViewById(R.id.et_terminal);
		et_usercname = (EditText) findViewById(R.id.et_usercname);
		et_simplename = (EditText) findViewById(R.id.et_simplename);
		btn_cancle = (Button) findViewById(R.id.btn_cancle);
		btn_positive = (Button) findViewById(R.id.btn_positive);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_positive:
			String userno = et_userno.getText().toString();
			String terminal = et_terminal.getText().toString();
			String usercname = et_usercname.getText().toString();
			String simplename = et_simplename.getText().toString();
			String bankSysString = FileUtils.getNewBankSys(userno, terminal,
					usercname, simplename);
			
			FileUtils.writeFileSdcardFile(fileName,bankSysString);
			break;
		case R.id.btn_cancle:
			finish();
			break;

		default:
			break;
		}
	}

}
