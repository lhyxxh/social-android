package com.eastcom.social.pos.fragment;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.eastcom.social.pos.R;
import com.eastcom.social.pos.activity.AppDetailActivity;
import com.eastcom.social.pos.activity.AppStoreActivity;
import com.eastcom.social.pos.adapter.AppStoreAdapter;
import com.eastcom.social.pos.adapter.AppStoreAdapter.Callback;
import com.eastcom.social.pos.adapter.MyUsualAppAdapter;
import com.eastcom.social.pos.adapter.MyUsualAppAdapter.MyUsualCallback;
import com.eastcom.social.pos.component.PageControlView;
import com.eastcom.social.pos.component.ScrollLayout;
import com.eastcom.social.pos.component.ScrollLayout.OnScreenChangeListenerDataLoad;
import com.eastcom.social.pos.database.DatabaseService;
import com.eastcom.social.pos.entity.AppListEntity;
import com.eastcom.social.pos.entity.DeviceAppInfoEntity;
import com.eastcom.social.pos.service.UpdateManager;
import com.eastcom.social.pos.util.QueryDeviceAppInfo;
import com.eastcom.social.pos.util.ToastUtil;





 public class MyAppFragment extends Fragment implements Callback,MyUsualCallback{
	 
	private static final String TAG = "MyAppFragment";
	public AppStoreActivity mActivity;
	private Context context;
	
	private SharedPreferences share;
	
	private EditText et_search_myapp;
	private RelativeLayout search_myapp;
	
	private ArrayList<AppListEntity> appList,usualAppList;
	private AppStoreAdapter adapter;
	private MyUsualAppAdapter usualAdapter;
	
	
	private UpdateManager updateManager;
	// 查看设备各APP信息版本号
	private ArrayList<DeviceAppInfoEntity> mlistAppInfo = null;// 本地APP信息
	
	private ScrollLayout scroll_layout;  
    private static final int APP_PAGE_SIZE = 6;  
    private PageControlView page_control; 
    private DataLoading dataLoad; 

	private GridView myapp_usual_gridView;
    
    private unInstallReceiver unInstallReceiver;
    private String pkgName="", appId = "", appName="", appRemark="", versionName="";
    private int versionCode, uploadLogo;
	 
	 @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	 
	 @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_my_app,container, false);
		
		initView(view);
		initData();
		registerunInstallReceiver();
		
//		List<RunningTaskInfo> arr = QueryRecentlyApp.getTaskList(context);
//		Log.e(TAG, "RecentTaskInfo-1---"+arr.size());
//		Log.e(TAG, "RecentTaskInfo-6---"+arr.get(0).baseActivity.getPackageName());
//		
//		
//		String arr2 = QueryRecentlyApp.getTopAppInfoPackageName(context);
//		Log.e(TAG, "RecentTaskInfo--arr2--"+arr2);
		
//		QueryRecentlyApp.getPkgUsageStats();
		
		return view;
	}
	
	private void registerunInstallReceiver() {
		unInstallReceiver = new unInstallReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        getActivity().registerReceiver(unInstallReceiver, filter);
	}
	
	
	
	private void initView(View view) {
		String hint = getResources().getString(R.string.search_hint); 
		et_search_myapp = (EditText) view.findViewById(R.id.et_search_myapp);
		search_myapp = (RelativeLayout) view.findViewById(R.id.search_myapp);
		search_myapp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String tableName = "social_myapp";
				String search = et_search_myapp.getText().toString().replaceAll(" ", "");
				
				if(!"".equals(search)&&search!=null){
					appList = DatabaseService.getSearchAppFromDatabase(context, 
							tableName, search);
					if(appList.size()!=0){
						pageReflesh();
					}else{
						ToastUtil.show(context, getResources()
								.getString(R.string.search_error), 5000);
					}
				}else{//若搜索时，搜索框为空，则获取表中所有数据
					appList = DatabaseService.getMyAppFromDatabase(context, tableName);
					if(appList.size()!=0){
						pageReflesh();
					}
				}
			}
		});
		
		scroll_layout = (ScrollLayout)view.findViewById(R.id.scroll_layout); 
		page_control = (PageControlView) view.findViewById(R.id.page_control);  
		dataLoad = new DataLoading(); 
		
		
		myapp_usual_gridView = (GridView) view
				.findViewById(R.id.myapp_usual_gridView);
		myapp_usual_gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				 Intent detailIntent = new Intent(getActivity(),AppDetailActivity.class);
				 detailIntent.putExtra("appId", appList.get(position).getId());
				 detailIntent.putExtra("appName", appList.get(position).getAppName());
				 detailIntent.putExtra("appRemark", appList.get(position).getAppRemark());
				 startActivity(detailIntent);
			}
		});

		context = this.getActivity().getApplicationContext();
		mlistAppInfo = QueryDeviceAppInfo.queryDeviceAppInfo(getActivity(),"1");
		usualAppList = new ArrayList<AppListEntity>();
		usualAdapter = new MyUsualAppAdapter(context,usualAppList,mlistAppInfo,this);
		myapp_usual_gridView.setAdapter(usualAdapter);
	}

	 
	private void initData() {

		share = context.getSharedPreferences("social_app_info", 0);
		updateManager = new UpdateManager(getActivity());
		appList = new ArrayList<AppListEntity>();
		
		boolean isSaveAppStoreTable = share.getBoolean("isSaveMyAppTable", false);
		if(isSaveAppStoreTable){//若已经存储了  应用市场 到数据库，则先获取本地数据库展示界面
			String tableName = "social_myApp";
			if(DatabaseService.isTableExist(context,tableName)){
				appList = DatabaseService.getMyAppFromDatabase(context, tableName);
				if(appList.size()!=0){
					Log.e(TAG, "initData  本地数据库  appList.size()--"+appList.size());
					pageReflesh();
				}
			}
		}
		
		updateUsualApp();
		
	}

	
	private void updateUsualApp() {
		boolean isSaveAppStoreTable = share.getBoolean("isSaveMyAppTable", false);
		if(isSaveAppStoreTable){//若已经存储了  应用市场 到数据库，则先获取本地数据库展示界面
			String tableName = "social_myApp";
			appList = DatabaseService.getMyAppFromDatabase(context, tableName);
			if(appList.size()!=0){
				ArrayList<AppListEntity> list_temp;
				list_temp = new ArrayList<AppListEntity>();
				if (appList.size() > 3) {
					for (int i = 0; i < 3; i++) {
								list_temp.add(appList.get(i));
					}
					usualAppList = list_temp;
				}else{
					usualAppList = appList;
				}
				usualAdapter.changeValue(usualAppList);
			}
		}
	}

	private void pageReflesh() {
        try {
			int pageNo = (int)Math.ceil( appList.size()/APP_PAGE_SIZE)+1; 
			//由于每次 安装 或者 卸载 之后，本地的mlistAppInfo 发生了改变 ，更新该列表以刷新界面
			mlistAppInfo = QueryDeviceAppInfo.queryDeviceAppInfo(getActivity(),"1");  
			scroll_layout.removeAllViewsInLayout();//先置空，以免再次加入
			page_control.removeAllViews();
			for(int i =0; i<pageNo; i++){
			    GridView appPage = new GridView(context);  
				adapter = new AppStoreAdapter(context,appList, mlistAppInfo, this,i,APP_PAGE_SIZE);
				appPage.setAdapter(adapter);
			    appPage.setNumColumns(3);  //列数
			    appPage.setVerticalSpacing(10);
			    appPage.setHorizontalSpacing(11);
			    appPage.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						 Intent detailIntent = new Intent(getActivity(),AppDetailActivity.class);
						 detailIntent.putExtra("appId", appList.get(position).getId());
						 detailIntent.putExtra("appName", appList.get(position).getAppName());
						 detailIntent.putExtra("appRemark", appList.get(position).getAppRemark());
						 startActivity(detailIntent);
					}
				});  
			    scroll_layout.addView(appPage);  
			}  
			//加载分页  
			page_control.bindScrollViewGroup(scroll_layout);
			//加载分页数据  
            dataLoad.bindScrollViewGroup(scroll_layout);
			adapter.changeValue(appList);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG,"pageReflesh 错误--"+e.toString());
		}
		
	}
	@Override
	public void itemInnerClick(View v) {
		appId = appList.get((Integer)v.getTag()).getId();
		pkgName = appList.get((Integer)v.getTag()).getPackageName();
		appName = appList.get((Integer)v.getTag()).getAppName();
		appRemark = appList.get((Integer)v.getTag()).getAppRemark();
		versionName = appList.get((Integer)v.getTag()).getVersionName();
		versionCode = appList.get((Integer)v.getTag()).getVersionCode();
		uploadLogo = appList.get((Integer)v.getTag()).getUploadLogo();
		String flag = "apk";
		switch (v.getId()) {
		case R.id.appstore_update3:
			updateManager.checkUpdateInfo(appId,flag,appName);
			break;
		case R.id.appstore_open3:
			updateManager.openApp(pkgName);
			break;
		case R.id.appstore_uninstall3:
			updateManager.unInstallApk(pkgName);
			break;
		case R.id.appstore_open2:
			updateManager.openApp(pkgName);
			break;
		case R.id.appstore_uninstall2:
			updateManager.unInstallApk(pkgName);
			break;
		}
	}
	

	//分页数据  
    class DataLoading {  
        private int count;  
        public void bindScrollViewGroup(ScrollLayout scrollViewGroup) {  
            this.count=scrollViewGroup.getChildCount();  
            scrollViewGroup.setOnScreenChangeListenerDataLoad(new OnScreenChangeListenerDataLoad() {  
                public void onScreenChange(int currentIndex) {  
                    //generatePageControl(currentIndex);  
                }  
            });  
        }  
        private void generatePageControl(int currentIndex){  
            if(count==currentIndex+1){  
            }  
        }  
    }  
	 
    
    private class unInstallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
        if (pkgName != null && ("package:" + pkgName).equals(intent.getDataString())) {
    		String myAppTableName = "social_myApp";
            //收到卸载完成后的广播才删除该条记录，并刷新列表
			DatabaseService.deleteData(context, myAppTableName, new String[] { appId });
			appList = DatabaseService.getMyAppFromDatabase(context, myAppTableName);
			Log.e(TAG, "unInstallReceiver---appList.size()--"+appList.size());
			if(appList.size()!=0){
				pageReflesh();
			}
			updateUsualApp();
        }
      }
    }
    
    
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(unInstallReceiver);
	}
}
