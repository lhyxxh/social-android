package com.eastcom.social.pos.application;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tencent.tinker.lib.reporter.DefaultLoadReporter;

import java.io.File;

public class MyLoadReporter extends DefaultLoadReporter {
    Context context;
    public MyLoadReporter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onLoadResult(File patchDirectory, int loadCode, long cost) {
        Log.i("Tinker.MyLoadReporter", "热更新完成");
        Intent intent = new Intent();
        intent.setAction("BROADCAST_ACTION");
        context.sendBroadcast(intent);
        super.onLoadResult(patchDirectory, loadCode, cost);
    }

    @Override
    public void onLoadPatchVersionChanged(String oldVersion, String newVersion, File patchDirectoryFile, String currentPatchName) {
        Log.i("Tinker.MyLoadReporter", "热更新成功，与原包有不一样");
        super.onLoadPatchVersionChanged(oldVersion, newVersion, patchDirectoryFile, currentPatchName);
    }

    @Override
    public void onLoadPatchListenerReceiveFail(File patchFile, int errorCode) {
        Log.i("Tinker.MyLoadReporter", "更新失败");
        super.onLoadPatchListenerReceiveFail(patchFile, errorCode);
    }
}
