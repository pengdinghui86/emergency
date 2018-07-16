package com.dssm.esc.util.acra;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

public class CrashSender implements ReportSender {
    @Override
    public void send(@NonNull Context context, @NonNull CrashReportData crashReportData) throws ReportSenderException {
        //发送邮件
        Log.i("CrashSender", "send: " + crashReportData.toJSON());
    }
}
