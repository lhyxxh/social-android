package com.eastcom.social.pos.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.eastcom.social.pos.R;
import com.eastcom.social.pos.core.orm.dao.Dao;
import com.eastcom.social.pos.util.ToastUtil;

public class FSExplorerActivity extends Activity implements OnItemClickListener {
	ListView itemlist = null;
	String path = File.separator;
	List<Map<String, Object>> list;

	public void sendPathToActivity(String path) {
		File file = new File(path);
		
		if (file.getName().equals(Dao.DB_NAME)) // 判断扩展名
		{
			Intent intent = new Intent();
			intent.putExtra("path", path);
			setResult(1001, intent);
			finish();
		} else {
			ToastUtil.show(FSExplorerActivity.this, "选择的文件类型不正确，无法备份", 3000);
		}

	}

	private List<String> lstFile = new ArrayList<String>(); // 结果 List

	public void GetFiles(String Path, String Extension, boolean IsIterative) // 搜索目录，扩展名，是否进入子文件夹
	{
		File[] files = new File(Path).listFiles();

		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isFile()) {
				if (f.getPath()
						.substring(f.getPath().length() - Extension.length())
						.equals(Extension)) // 判断扩展名
					lstFile.add(f.getPath());

				if (!IsIterative)
					break;
			} else if (f.isDirectory() && f.getPath().indexOf("/.") == -1) // 忽略点文件（隐藏文件/文件夹）
				GetFiles(f.getPath(), Extension, IsIterative);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.files);
		setTitle("文件浏览器");
		itemlist = (ListView) findViewById(R.id.itemlist);
		refreshListItems(path);
	}

	/* 根据path更新路径列表 */
	private void refreshListItems(String path) {
		setTitle("文件浏览器 > " + path);
		list = buildListForSimpleAdapter(path);
		SimpleAdapter notes = new SimpleAdapter(this, list, R.layout.file_row,
				new String[] { "name", "path", "img" }, new int[] { R.id.name,
						R.id.desc, R.id.img });
		itemlist.setAdapter(notes);
		itemlist.setOnItemClickListener(this);
		itemlist.setSelection(0);
	}

	/* 根据路径生成一个包含路径的列表 */
	private List<Map<String, Object>> buildListForSimpleAdapter(String path) {
		File[] files = new File(path).listFiles();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(
				files.length);
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("name", "/");
		root.put("img", R.drawable.file_root);
		root.put("path", "go to root directory");
		list.add(root);
		Map<String, Object> pmap = new HashMap<String, Object>();
		pmap.put("name", "..");
		pmap.put("img", R.drawable.file_paranet);
		pmap.put("path", "go to paranet Directory");
		list.add(pmap);
		for (File file : files) {
			Map<String, Object> map = new HashMap<String, Object>();
			if (file.isDirectory()) {
				map.put("img", R.drawable.directory);
			} else {
				map.put("img", R.drawable.file_doc);
			}
			map.put("name", file.getName());
			map.put("path", file.getPath());
			list.add(map);
		}
		return list;
	}

	/* 跳转到上一层 */
	private void goToParent() {
		File file = new File(path);
		File str_pa = file.getParentFile();
		if (str_pa == null) {
			Toast.makeText(FSExplorerActivity.this, "已经是根目录",
					Toast.LENGTH_SHORT).show();
			refreshListItems(path);
		} else {
			path = str_pa.getAbsolutePath();
			refreshListItems(path);
		}
	}

	/* 实现OnItemClickListener接口 */
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		if (position == 0) {
			path = "/";
			refreshListItems(path);
		} else if (position == 1) {
			goToParent();
		} else {
			path = (String) list.get(position).get("path");
			File file = new File(path);
			if (file.isDirectory())
				refreshListItems(path);
			else {
				sendPathToActivity(path);
			}

		}

	}

}
