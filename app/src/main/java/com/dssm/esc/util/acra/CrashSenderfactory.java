package com.dssm.esc.util.acra;

import android.content.Context;
import android.support.annotation.NonNull;

import org.acra.config.ACRAConfiguration;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderFactory;

public class CrashSenderfactory implements ReportSenderFactory {
    /***
     * 注意这里必须要是空的构造方法
     */
    public CrashSenderfactory() {
    }

    @NonNull
    @Override
    public ReportSender create(@NonNull Context context, @NonNull ACRAConfiguration acraConfiguration) {
        return new CrashSender();
    }
}
