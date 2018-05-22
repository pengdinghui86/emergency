package com.dssm.esc.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 检查权限的工具类
 */

public class PermissionsChecker {
    private final Context mContext;

    public PermissionsChecker(Context context) {
        mContext = context.getApplicationContext();
    }

    // 判断权限集合
    public List<String> lacksPermissions(String... permissions) {
        List<String> result = new ArrayList<>();
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                result.add(permission);
            }
        }
        return result;
    }

    // 判断是否缺少权限
    private boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(mContext, permission) ==
                PackageManager.PERMISSION_DENIED;
    }
}
