package com.eastcom.social.pos.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;

import com.eastcom.social.pos.application.MyApplicationLike;
import com.eastcom.social.pos.config.Constance;
import com.eastcom.social.pos.core.socket.client.SoClient;
import com.eastcom.social.pos.core.socket.listener.ActivityCallBackListener;
import com.eastcom.social.pos.core.socket.message.SoFollowCommad;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.UpdateIncrement.UpdateIncrementMessage;
import com.eastcom.social.pos.core.socket.message.UpdateIncrement.UpdateIncrementRespMessage;
import com.eastcom.social.pos.core.socket.message.checkincreamentversion.CheckIncreamentVersionMessage;
import com.eastcom.social.pos.core.socket.message.checkincreamentversion.CheckIncreamentVersionRespMessage;
import com.eastcom.social.pos.core.socket.message.checkversion.CheckVersionMessage;
import com.eastcom.social.pos.core.socket.message.checkversion.CheckVersionRespMessage;
import com.eastcom.social.pos.core.socket.message.confirm.ConfirmMssage;
import com.eastcom.social.pos.core.socket.message.updatesoft.UpdateSoftMessage;
import com.eastcom.social.pos.core.socket.message.updatesoft.UpdateSoftRespMessage;
import com.eastcom.social.pos.listener.MySoClient;
import com.eastcom.social.pos.util.FileUtils;
import com.eastcom.social.pos.util.MyLog;
import com.lidroid.xutils.util.LogUtils;
import com.tencent.tinker.lib.tinker.TinkerInstaller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UploadIncreamentVersionService extends Service {

    private GetDataTask getDataTask;
    private int operateType = 0;
    private LocalDataFactory localDataFactory;

    private SoClient client;
    private int mDownloadinNo;

    @Override
    public void onCreate() {
        MyLog.i("UpdateSoftService", "oncreate");
        operateType = 0;
        localDataFactory = LocalDataFactory.newInstance(MyApplicationLike
                .getContext());
        client = MySoClient.newInstance().getClient();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startGetDataTask();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void startGetDataTask() {
        if (null == getDataTask) {
            getDataTask = new GetDataTask();
            getDataTask.execute((Void) null);
        }
    }

    public class GetDataTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {

                if (operateType == 0) {
                    checkIncrementVersion();
                } else if (operateType == 1) {
//                    int versionMainUpdate_local = localDataFactory.getInt(
//                            LocalDataFactory.INCREMENT_MAIN_UPDATE, 0);
//                    int versionSubUpdate_local = localDataFactory.getInt(
//                            LocalDataFactory.INCREMENT_SUB_UPDATE, 0);
                    int versionMainUpdate_local = LocalDataFactory.LocalMainVersion;
                    int versionSubUpdate_local = localDataFactory.LocalSubVersion;
                    int verionUpdateSize = localDataFactory.getInt(
                            LocalDataFactory.INCREMENT_UPDATE_SIZE, 0);
                    MyLog.i("UpdateIncreamentVersionService", "UpdateIncreamentVersionMessage send");
                    mDownloadinNo = verionUpdateSize;
                    updateIncrement(versionMainUpdate_local, versionSubUpdate_local,
                            verionUpdateSize);
                }

            } catch (Exception e) {
                MyLog.i("UpdateIncreamentVersionService", "UpdateIncreamentVersionMessage Exception");
                getDataTask = null;
                e.printStackTrace();
                return false;
            }
            return true;
        }

        private void updateIncrement(final int versionMain, final int versionSub,
                            final int packetNo) {
            int versionMainUpdate_local = LocalDataFactory.LocalMainVersion;
            int versionSubUpdate_local = localDataFactory.LocalSubVersion;
            UpdateIncrementMessage updateIncrementMessage = new UpdateIncrementMessage(
                    versionMainUpdate_local, versionSubUpdate_local, packetNo);
            client.sendMessage(updateIncrementMessage,
                    new ActivityCallBackListener() {

                        @Override
                        public void doTimeOut() {
                            MyLog.i("updateIncrementService",
                                    "updateIncrementMessage doTimeOut");
                            stopSelf();
                        }

                        @Override
                        public void callBack(SoMessage message) {
                            try {
                                UpdateIncrementRespMessage updateIncrementRespMessage = new UpdateIncrementRespMessage(
                                        message);
                                int versionSize = updateIncrementRespMessage
                                        .getVersionSize();
                                int versionFlag = updateIncrementRespMessage
                                        .getVersionFlag();
                                byte[] data = updateIncrementRespMessage.getData();
//                                String path = versionMain + "_" + versionSub
//                                        + "/";
                                String path = LocalDataFactory.INCREMENT_MAIN_UPDATE + "_" +
                                        LocalDataFactory.INCREMENT_SUB_UPDATE + "/";
                                String tempFileName = "" + versionFlag;
                                File word = new File(Constance.ApkIncrementFile);
                                if (!word.exists()) {
                                    MyLog.i("UpdateIncrementService",
                                            "ApkIncrementFile makedir");
                                    word.mkdir();
                                }
                                File dir = new File(Constance.ApkIncrementFile
                                        + path);
                                if (!dir.exists()) {
                                    MyLog.i("UpdateIncrementService", "path makedir");
                                    dir.mkdir();
                                }
                                File file = new File(Constance.ApkIncrementFile
                                        + path, tempFileName);
                                FileUtils.createFileWithByte(file, data);
                                if (versionFlag + 1 == versionSize) {
                                    MyLog.i("UpdateIncrementService",
                                            "update last flag is done");
                                    localDataFactory
                                            .putInt(LocalDataFactory.INCREMENT_UPDATE_SIZE,
                                                    -1);
                                    mDownloadinNo = -1;
                                    String fileName = versionMain + "_"
                                            + versionSub + ".apk";
                                    mergeFile(fileName, versionSize, path);
                                    String realFilePath = Constance.ApkIncrementFile
                                            + versionMain
                                            + "_"
                                            + versionSub
                                            + ".apk";
                                    File apkfile = new File(realFilePath);
                                    if (!apkfile.exists()) {
                                        MyLog.i("UpdateIncrementService",
                                                "download file not exist");
                                        // 安装文件不存在
                                        localDataFactory
                                                .putInt(LocalDataFactory.INCREMENT_UPDATE_SIZE,
                                                        0);
                                        mDownloadinNo = 0;
                                    } else {
//                                        if (mDownloadinNo == versionFlag) {
                                            sendOpenBroadcast();
//                                            installApk(realFilePath);
                                        installIncrement(realFilePath);
                                            // 删除临时文件夹
                                            FileUtils
                                                    .deleteDirectory(Constance.ApkIncrementFile
                                                            + path);
//                                        } else {
//                                            MyLog.i("UpdateIncrementService",
//                                                    "mDownloadinNo = "
//                                                            + mDownloadinNo
//                                                            + ", versionFlag = "
//                                                            + versionFlag);
//                                        }
                                    }
                                    stopSelf();
                                } else {
                                    localDataFactory
                                            .putInt(LocalDataFactory.INCREMENT_UPDATE_SIZE,
                                                    versionFlag);
                                    updateIncrement(versionMain, versionSub,
                                            versionFlag + 1);
                                }
                            } catch (Exception e) {
                                MyLog.i("UpdateIncrementService", "update Exception");
                                stopSelf();
                            }
                        }
                    });

        }

        private void mergeFile(String fileName, int versionSize, String path) {
            ArrayList<File> tempFiles = new ArrayList<File>();
            for (int i = 0; i < versionSize; i++) {
                File tempFile = new File(Constance.ApkIncrementFile + path, ""
                        + i);
                tempFiles.add(tempFile);
            }
            String realFilePath = Constance.ApkIncrementFile + fileName;
            File realFile = new File(realFilePath);
            FileUtils.mergeFiles(realFile, tempFiles);
        }

        private void checkIncrementVersion() {
            try {
                MyLog.i("UpdateIncrementService", "CheckIncrementVersionMessage send");
                String versionName = getPackageManager().getPackageInfo(
                        getPackageName(), 0).versionName;
                final int versionMain = Integer.valueOf(versionName
                        .split("\\.")[0]);
                final int versionSub = Integer
                        .valueOf(versionName.split("\\.")[1]);

                //调试
                CheckIncreamentVersionMessage checkIncreamentVersionMessage = new CheckIncreamentVersionMessage(
                        versionMain, versionSub);
//                CheckIncreamentVersionMessage checkIncreamentVersionMessage = new CheckIncreamentVersionMessage(
//                        9, 1);

                client.sendMessage(checkIncreamentVersionMessage,
                        new ActivityCallBackListener() {

                            @Override
                            public void doTimeOut() {
                                MyLog.i("UpdateIncrementService",
                                        "CheckIncrementVersionMessage doTimeOut");
                                stopSelf();
                            }

                            @Override
                            public void callBack(SoMessage message) {
                                MyLog.e("UpdateIncrementService",
                                        "CheckIncrementVersionMessage callBack");
                                CheckIncreamentVersionRespMessage checkIncreamentVersionRespMessage = new CheckIncreamentVersionRespMessage(
                                        message);
                                int versionMainUpdate = checkIncreamentVersionRespMessage
                                        .getVersionMainUpdate();
                                int versionSubUpdate = checkIncreamentVersionRespMessage
                                        .getVersionSubUpdate();
                                int hasIncrePacket = checkIncreamentVersionRespMessage   //是否有增量包
                                        .getHasIncrePacket();
                                if (hasIncrePacket == 1){
                                    int versionMainUpdate_local = localDataFactory
                                            .getInt(LocalDataFactory.INCREMENT_MAIN_UPDATE,
                                                    0);
                                    int versionSubUpdate_local = localDataFactory
                                            .getInt(LocalDataFactory.INCREMENT_SUB_UPDATE,
                                                    0);
                                    localDataFactory.putInt(
                                            LocalDataFactory.INCREMENT_MAIN_UPDATE,
                                            versionMainUpdate);
                                    localDataFactory.putInt(
                                            LocalDataFactory.INCREMENT_SUB_UPDATE,
                                            versionSubUpdate);
                                    MyLog.i("UpdateIncrementService", "hasIncrePacket:"
                                            + hasIncrePacket);
                                    MyLog.i("UpdateIncrementService",
                                            "versionMainUpdate_local:"
                                                    + versionMainUpdate_local
                                                    + "versionSubUpdate_local:"
                                                    + versionSubUpdate_local);
                                    if (versionMain == versionMainUpdate
                                            && versionSub == versionSubUpdate) {
                                        // 不需要更新 当前版本号与最细版本一致
                                        MyLog.e("UpdateIncrementService",
                                                "ConfirmMssage send");

                                        stopSelf();
                                    } else {
                                        if (versionMainUpdate == versionMainUpdate_local
                                                && versionSubUpdate == versionSubUpdate_local) {
                                            // 未完成的下载计划，继续下载
                                            int verionUpdateSize = localDataFactory
                                                    .getInt(LocalDataFactory.INCREMENT_UPDATE_SIZE,
                                                            0);
                                            MyLog.i("UpdateIncrementService",
                                                    "verionUpdateSize:"
                                                            + verionUpdateSize);
                                            if (verionUpdateSize == -1) {
                                                // 下载完成 安装
                                                String realFilePath = Constance.ApkIncrementFile
                                                        + versionMainUpdate
                                                        + "_"
                                                        + versionSubUpdate
                                                        + ".apk";
                                                File apkfile = new File(
                                                        realFilePath);
                                                if (!apkfile.exists()) {
                                                    MyLog.i("UpdateIncrementService",
                                                            "apkfile is not exist");
                                                    // 安装文件不存在
                                                    localDataFactory
                                                            .putInt(LocalDataFactory.INCREMENT_UPDATE_SIZE,
                                                                    0);
                                                } else {
                                                    if (isAppOnForeground()) {
                                                        sendOpenBroadcast();
//                                                        installApk(realFilePath);
                                                        installIncrement(realFilePath);
                                                    }
                                                }
                                                stopSelf();
                                            } else {
                                                operateType = 1;
                                                startGetDataTask();
                                            }
                                        } else {
                                            // 新完成的下载计划，重新下载
                                            localDataFactory
                                                    .putInt(LocalDataFactory.INCREMENT_UPDATE_SIZE,
                                                            0);
                                            operateType = 1;
                                            startGetDataTask();
                                        }
                                    }
                                }else { //没有增量包更新就检查固件包更新
                                        if (operateType == 0) {
                                            checkVersion();
                                        } else if (operateType == 1) {
                                            int versionMainUpdate_local = localDataFactory.getInt(
                                                    LocalDataFactory.VERSION_MAIN_UPDATE, 0);
                                            int versionSubUpdate_local = localDataFactory.getInt(
                                                    LocalDataFactory.VERSION_SUB_UPDATE, 0);
                                            int verionUpdateSize = localDataFactory.getInt(
                                                    LocalDataFactory.VERSION_UPDATE_SIZE, 0);
                                            MyLog.i("UpdateSoftService", "UpdateSoftMessage send");
                                            mDownloadinNo = verionUpdateSize;
                                            update(versionMainUpdate_local, versionSubUpdate_local,
                                                    verionUpdateSize);
                                        }
                                }
                            }
                        }, 10);
            } catch (Exception e) {
                MyLog.e("UpdateSoftService", "checkVersion:exception");
                stopSelf();
            }

        }

        @Override
        protected void onPostExecute(Boolean result) {
            getDataTask = null;
            try {
                if (result) {
                    if (operateType == 0) {

                    } else if (operateType == 1) {

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void checkVersion() {
            try {
                MyLog.i("UpdateSoftService", "CheckVersionMessage send");
                String versionName = getPackageManager().getPackageInfo(
                        getPackageName(), 0).versionName;
                final int versionMain = Integer.valueOf(versionName
                        .split("\\.")[0]);
                final int versionSub = Integer
                        .valueOf(versionName.split("\\.")[1]);
                CheckVersionMessage checkVersionMessage = new CheckVersionMessage(
                        versionMain, versionSub);
                client.sendMessage(checkVersionMessage,
                        new ActivityCallBackListener() {

                            @Override
                            public void doTimeOut() {
                                MyLog.i("UpdateSoftService",
                                        "CheckVersionMessage doTimeOut");
                                stopSelf();
                            }

                            @Override
                            public void callBack(SoMessage message) {
                                MyLog.e("UpdateSoftService",
                                        "CheckVersionMessage callBack");
                                CheckVersionRespMessage checkVersionRespMessage = new CheckVersionRespMessage(
                                        message);
                                int versionMainUpdate = checkVersionRespMessage
                                        .getVersionMainUpdate();
                                int versionSubUpdate = checkVersionRespMessage
                                        .getVersionSubUpdate();
                                int allowUpdate = checkVersionRespMessage
                                        .getAllowUpdate();
                                int versionMainUpdate_local = localDataFactory
                                        .getInt(LocalDataFactory.VERSION_MAIN_UPDATE,
                                                0);
                                int versionSubUpdate_local = localDataFactory
                                        .getInt(LocalDataFactory.VERSION_SUB_UPDATE,
                                                0);
                                localDataFactory.putInt(
                                        LocalDataFactory.VERSION_MAIN_UPDATE,
                                        versionMainUpdate);
                                localDataFactory.putInt(
                                        LocalDataFactory.VERSION_SUB_UPDATE,
                                        versionSubUpdate);
                                MyLog.i("UpdateSoftService", "allowUpdate:"
                                        + allowUpdate);
                                MyLog.i("UpdateSoftService",
                                        "versionMainUpdate_local:"
                                                + versionMainUpdate_local
                                                + "versionSubUpdate_local:"
                                                + versionSubUpdate_local);
                                if (versionMain == versionMainUpdate
                                        && versionSub == versionSubUpdate) {
                                    // 不需要更新 当前版本号与最细版本一致
                                    MyLog.e("UpdateSoftService",
                                            "ConfirmMssage send");
                                    client.sendMessage(new ConfirmMssage(
                                                    SoFollowCommad.请求更新固件, 1),
                                            new ActivityCallBackListener() {

                                                @Override
                                                public void doTimeOut() {
                                                    MyLog.e("UpdateSoftService",
                                                            "ConfirmMssage doTimeOut");
                                                }

                                                @Override
                                                public void callBack(
                                                        SoMessage message) {
                                                    MyLog.e("UpdateSoftService",
                                                            "ConfirmMssage callback");
                                                }
                                            });
                                    stopSelf();
                                } else {
                                    if (allowUpdate == 1) {
                                        if (versionMainUpdate == versionMainUpdate_local
                                                && versionSubUpdate == versionSubUpdate_local) {
                                            // 未完成的下载计划，继续下载
                                            int verionUpdateSize = localDataFactory
                                                    .getInt(LocalDataFactory.VERSION_UPDATE_SIZE,
                                                            0);
                                            MyLog.i("UpdateSoftService",
                                                    "verionUpdateSize:"
                                                            + verionUpdateSize);
                                            if (verionUpdateSize == -1) {
                                                // 下载完成 安装
                                                String realFilePath = Constance.ApkDownloadFile
                                                        + versionMainUpdate
                                                        + "_"
                                                        + versionSubUpdate
                                                        + ".apk";
                                                File apkfile = new File(
                                                        realFilePath);
                                                if (!apkfile.exists()) {
                                                    MyLog.i("UpdateSoftService",
                                                            "apkfile is not exist");
                                                    // 安装文件不存在
                                                    localDataFactory
                                                            .putInt(LocalDataFactory.VERSION_UPDATE_SIZE,
                                                                    0);
                                                } else {
                                                    if (isAppOnForeground()) {
                                                        sendOpenBroadcast();
                                                        installApk(realFilePath);
                                                    }
                                                }
                                                stopSelf();
                                            } else {
                                                operateType = 1;
                                                startGetDataTask();
                                            }
                                        } else {
                                            // 新完成的下载计划，重新下载
                                            localDataFactory
                                                    .putInt(LocalDataFactory.VERSION_UPDATE_SIZE,
                                                            0);
                                            operateType = 1;
                                            startGetDataTask();
                                        }
                                    } else {
                                        stopSelf();
                                    }
                                }
                            }
                        }, 10);
            } catch (Exception e) {
                MyLog.e("UpdateSoftService", "checkVersion:exception");
                stopSelf();
            }

        }

        private void update(final int versionMain, final int versionSub,
                            final int packetNo) {
            UpdateSoftMessage updateSoftMessage = new UpdateSoftMessage(
                    versionMain, versionSub, packetNo);
            client.sendMessage(updateSoftMessage,
                    new ActivityCallBackListener() {

                        @Override
                        public void doTimeOut() {
                            MyLog.i("UpdateSoftService",
                                    "UpdateSoftMessage doTimeOut");
                            stopSelf();
                        }

                        @Override
                        public void callBack(SoMessage message) {
                            try {
                                UpdateSoftRespMessage updateSoftRespMessage = new UpdateSoftRespMessage(
                                        message);
                                int versionSize = updateSoftRespMessage
                                        .getVersionSize();
                                int versionFlag = updateSoftRespMessage
                                        .getVersionFlag();
                                byte[] data = updateSoftRespMessage.getData();
                                String path = versionMain + "_" + versionSub
                                        + "/";
                                String tempFileName = "" + versionFlag;
                                File word = new File(Constance.ApkDownloadFile);
                                if (!word.exists()) {
                                    MyLog.i("UpdateSoftService",
                                            "ApkDownloadFile makedir");
                                    word.mkdir();
                                }
                                File dir = new File(Constance.ApkDownloadFile
                                        + path);
                                if (!dir.exists()) {
                                    MyLog.i("UpdateSoftService", "path makedir");
                                    dir.mkdir();
                                }
                                File file = new File(Constance.ApkDownloadFile
                                        + path, tempFileName);
                                FileUtils.createFileWithByte(file, data);
                                if (versionFlag + 1 == versionSize) {
                                    MyLog.i("UpdateSoftService",
                                            "update last flag is done");
                                    localDataFactory
                                            .putInt(LocalDataFactory.VERSION_UPDATE_SIZE,
                                                    -1);
                                    mDownloadinNo = -1;
                                    String fileName = versionMain + "_"
                                            + versionSub + ".apk";
                                    mergeFile(fileName, versionSize, path);
                                    String realFilePath = Constance.ApkDownloadFile
                                            + versionMain
                                            + "_"
                                            + versionSub
                                            + ".apk";
                                    File apkfile = new File(realFilePath);
                                    if (!apkfile.exists()) {
                                        MyLog.i("UpdateSoftService",
                                                "download file not exist");
                                        // 安装文件不存在
                                        localDataFactory
                                                .putInt(LocalDataFactory.VERSION_UPDATE_SIZE,
                                                        0);
                                        mDownloadinNo = 0;
                                    } else {
                                        //mDownloadinNo不是已经设置成-1了吗
//                                        if (mDownloadinNo == versionFlag) {
                                            sendOpenBroadcast();
                                            installApk(realFilePath);
                                            // 删除临时文件夹
                                            FileUtils
                                                    .deleteDirectory(Constance.ApkDownloadFile
                                                            + path);
//                                        } else {
//                                            MyLog.i("UpdateSoftService",
//                                                    "mDownloadinNo = "
//                                                            + mDownloadinNo
//                                                            + ", versionFlag = "
//                                                            + versionFlag);
//                                        }
                                    }
                                    stopSelf();
                                } else {
                                    localDataFactory
                                            .putInt(LocalDataFactory.VERSION_UPDATE_SIZE,
                                                    versionFlag);
                                    update(versionMain, versionSub,
                                            versionFlag + 1);
                                }
                            } catch (Exception e) {
                                MyLog.i("UpdateSoftService", "update Exception");
                                stopSelf();
                            }
                        }
                    });

        }

    }



    /**
     * 安装apk
     *
     * @param saveFileName
     */
    private void installApk(String saveFileName) {
        File apkfile = new File(saveFileName);

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        startActivity(i);
    }

    /**
     * 发送 打开未知来源选项广播
     */
    private void sendOpenBroadcast() {
        Intent intent = new Intent();
        intent.setAction("android.intent.for.install.y");
        sendBroadcast(intent);
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    private void installIncrement(String realFilePath){
        try {
            TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), realFilePath);
        }catch (Exception e){
            LogUtils.e("Tinker更新失败" + e.toString());
        }

    }
}
