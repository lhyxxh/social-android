package com.eastcom.social.pos.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.eastcom.social.pos.R;

public class DbBackUp extends Activity implements OnClickListener{
	private Button button1;
	private Button button2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_db_backup);
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			dataBackup();
			break;
		case R.id.button2:
			dataRecover();
			break;

		default:
			break;
		}
	}

	// 数据恢复
	private void dataRecover() {
		new BackupTask(this).execute("restroeDatabase");
	}

	// 数据备份
	private void dataBackup() {
		File file = dbOk("social_pos");
		if (file!=null) {
			new BackupTask(this).execute("backupDatabase");
		}
		
	}
	
	  /**
     * 数据库文件是否存在，并可以使用
     * 
     * @return
     */
    private File dbOk(String dbName) {
        String sp = File.separator;
        String absPath = Environment.getDataDirectory().getAbsolutePath();
        String pakName = getPackageName();
        String dbPath = absPath + sp + "data" + sp + pakName + sp + "databases"
                + sp + dbName;
        File file = new File(dbPath);
        if (file.exists()) {
            return file;
        } else {
            return null;
        }
    }

	public class BackupTask extends AsyncTask<String, Void, Integer> {
		private static final String COMMAND_BACKUP = "backupDatabase";
		public static final String COMMAND_RESTORE = "restroeDatabase";
		private Context mContext;

		public BackupTask(Context context) {
			this.mContext = context;
		}

		@Override
		protected Integer doInBackground(String... params) {
			// 获得正在使用的数据库路径，我的是 sdcard 目录下的 /dlion/db_dlion.db
			// 默认路径是 /data/data/(包名)/databases/*.db
			File dbFile = dbOk("social_pos");
			File exportDir = new File(
					Environment.getExternalStorageDirectory(), "dlionBackup");
			if (!exportDir.exists()) {
				exportDir.mkdirs();
			}
			File backup = new File(exportDir, dbFile.getName());
			String command = params[0];
			if (command.equals(COMMAND_BACKUP)) {
				try {
					backup.createNewFile();
					fileCopy(dbFile, backup);
					return Log.d("backup", "ok");
				} catch (Exception e) {
					e.printStackTrace();
					return Log.d("backup", "fail");
				}
			} else if (command.equals(COMMAND_RESTORE)) {
				try {
					fileCopy(backup, dbFile);
					return Log.d("restore", "success");
				} catch (Exception e) {
					e.printStackTrace();
					return Log.d("restore", "fail");
				}
			} else {
				return null;
			}
		}

		private void fileCopy(File dbFile, File backup) throws IOException {
			FileChannel inChannel = new FileInputStream(dbFile).getChannel();
			FileChannel outChannel = new FileOutputStream(backup).getChannel();
			try {
				inChannel.transferTo(0, inChannel.size(), outChannel);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (inChannel != null) {
					inChannel.close();
				}
				if (outChannel != null) {
					outChannel.close();
				}
			}
		}
	}

}
