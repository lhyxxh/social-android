package com.eastcom.social.pos.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.os.Build;
import android.util.Log;

public class QueryRecentlyApp {
	private static final String TAG = "QueryRecentlyApp";
 	/** 
     * api的版本,返回一个整形的数值 
     */  
    public static final int apiVersion = Build.VERSION.SDK_INT; 
    
	/**
	 * 查询当前设备最近使用的应用程序，并获取到包名
	 * 
	 */
	
	public static List<RunningTaskInfo> getTaskList(Context context){
		ActivityManager am=(ActivityManager)context.getSystemService
				(Context.ACTIVITY_SERVICE);
		return am.getRunningTasks(1);
		}

	/** 
     * 获取app的名称 
     *  
     * @param context 
     * @return 
     */  
    public static String getAppName(Context context) {  
        return context.getApplicationInfo().loadLabel(context.getPackageManager()) + "";  
    }  
  
    /** 
     * 这个方法获取最近运行任何中最上面的一个应用的包名,<br> 
     * 进行了api版本的判断,然后利用不同的方法获取包名,具有兼容性 
     *  
     * @param context 
     *            上下文对象 
     * @return 返回包名,如果出现异常或者获取失败返回"" 
     */  
    public static String getTopAppInfoPackageName(Context context) {  
        if (apiVersion < 21) { // 如果版本低于22  
            // 获取到activity的管理的类  
            ActivityManager m = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);  
            // 获取最近的一个运行的任务的信息  
            List<RunningTaskInfo> tasks = m.getRunningTasks(1);  
  
            if (tasks != null && tasks.size() > 0) { // 如果集合不是空的  
  
                // 返回任务栈中最上面的一个  
                RunningTaskInfo info = m.getRunningTasks(1).get(0);  
  
                // 获取到应用的包名  
                // String packageName =  
                // info.topActivity.getPackageName();  
                return info.baseActivity.getPackageName();  
            } else {  
                return "";  
            }  
        } else {  
  
            final int PROCESS_STATE_TOP = 2;  
            try {  
                // 获取正在运行的进程应用的信息实体中的一个字段,通过反射获取出来  
                Field processStateField = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");  
                // 获取所有的正在运行的进程应用信息实体对象  
                List<ActivityManager.RunningAppProcessInfo> processes = ((ActivityManager) context  
                        .getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses();  
                // 循环所有的进程,检测某一个进程的状态是最上面,也是就最近运行的一个应用的状态的时候,就返回这个应用的包名  
                for (ActivityManager.RunningAppProcessInfo process : processes) {  
                    if (process.importance <= ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND  
                            && process.importanceReasonCode == 0) {  
                        int state = processStateField.getInt(process);  
                        if (state == PROCESS_STATE_TOP) { // 如果这个实体对象的状态为最近的运行应用  
                            String[] packname = process.pkgList;  
                            // 返回应用的包名  
                            return packname[0];  
                        }  
                    }  
                }  
            } catch (Exception e) {  
            }  
            return "";  
        }  
    }  
    
    
    /** 
     * Use reflect to get Package Usage Statistics data.<br> 
     */  
    public static void getPkgUsageStats() {  
        Log.i(TAG, "[getPkgUsageStats]");  
        try {  
            Class<?> cServiceManager = Class  
                    .forName("android.os.ServiceManager");  
            Method mGetService = cServiceManager.getMethod("getService",  
                    java.lang.String.class);  
            Object oRemoteService = mGetService.invoke(null, "usagestats");  
      
            // IUsageStats oIUsageStats =  
            // IUsageStats.Stub.asInterface(oRemoteService)  
            Class<?> cStub = Class  
                    .forName("com.android.internal.app.IUsageStats$Stub");  
            Method mUsageStatsService = cStub.getMethod("asInterface",  
                    android.os.IBinder.class);  
            Object oIUsageStats = mUsageStatsService.invoke(null,  
                    oRemoteService);  
      
            // PkgUsageStats[] oPkgUsageStatsArray =  
            // mUsageStatsService.getAllPkgUsageStats();  
            Class<?> cIUsageStatus = Class  
                    .forName("com.android.internal.app.IUsageStats");  
            Method mGetAllPkgUsageStats = cIUsageStatus.getMethod(  
                    "getAllPkgUsageStats", (Class[]) null);  
            Object[] oPkgUsageStatsArray = (Object[]) mGetAllPkgUsageStats  
                    .invoke(oIUsageStats, (Object[]) null);  
            Log.i(TAG, "[getPkgUsageStats] oPkgUsageStatsArray = "+oPkgUsageStatsArray);  
      
            Class<?> cPkgUsageStats = Class  
                    .forName("com.android.internal.os.PkgUsageStats");  
      
            StringBuffer sb = new StringBuffer();  
            sb.append("nerver used : ");  
            for (Object pkgUsageStats : oPkgUsageStatsArray) {  
                // get pkgUsageStats.packageName, pkgUsageStats.launchCount,  
                // pkgUsageStats.usageTime  
                String packageName = (String) cPkgUsageStats.getDeclaredField(  
                        "packageName").get(pkgUsageStats);  
                int launchCount = cPkgUsageStats  
                        .getDeclaredField("launchCount").getInt(pkgUsageStats);  
                long usageTime = cPkgUsageStats.getDeclaredField("usageTime")  
                        .getLong(pkgUsageStats);  
                if (launchCount > 0)  
                    Log.i(TAG, "[getPkgUsageStats] "+ packageName + "  count: "  
                            + launchCount + "  time:  " + usageTime);  
                else {  
                    sb.append(packageName + "; ");  
                }  
            }  
            Log.i(TAG, "[getPkgUsageStats] " + sb.toString());  
        } catch (IllegalArgumentException e) {  
            e.printStackTrace();  
        } catch (IllegalAccessException e) {  
            e.printStackTrace();  
        } catch (InvocationTargetException e) {  
            e.printStackTrace();  
        } catch (NoSuchFieldException e) {  
            e.printStackTrace();  
        } catch (ClassNotFoundException e) {  
            e.printStackTrace();  
        } catch (NoSuchMethodException e) {  
            e.printStackTrace();  
        }  
    }
}