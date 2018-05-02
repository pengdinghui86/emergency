package com.dssm.esc.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.UserSevice;
import com.dssm.esc.model.analytical.implSevice.UserSeviceImpl;
import com.dssm.esc.model.entity.user.UserEntity;
import com.easemob.chatuidemo.DemoApplication;
import com.easemob.chatuidemo.activity.LoginActivity;

import net.tsz.afinal.FinalHttp;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("InflateParams")
@SuppressWarnings("serial")
public class Utils implements Serializable {

    private static class SingletonHolder {
        /**
         * 单例对象实例
         */
        static final Utils INSTANCE = new Utils();

    }

    public static Utils getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * private的构造函数用于避免外界直接使用new来实例化对象
     */
    private Utils() {
    }

    /**
     * readResolve方法应对单例对象被序列化时候
     */
    private Object readResolve() {
        return getInstance();
    }

    /**
     * afinal框架访问网络对象；
     */
    private FinalHttp finalHttp = null;
    private String reg;
    private Toast toast = null;
    private Handler mHandler = new Handler();
    private Runnable r = new Runnable() {
        public void run() {
            toast.cancel();
        }
    };

    /**
     * 获取系统当前时间
     *
     * @param format
     * @return
     * @version 1.0
     * @createTime 2015-9-7,上午10:00:10
     * @updateTime 2015-9-7,上午10:00:10
     * @createAuthor Zsj
     * @updateAuthor
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    public String getCurrentTime(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }

    public String getCurrentTime() {
        return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
    }

    /**
     * 电话号码验证
     *
     * @param mobiles
     * @return
     * @version 1.0
     * @createTime 2015-9-7,上午9:59:58
     * @updateTime 2015-9-7,上午9:59:58
     * @createAuthor Zsj
     * @updateAuthor
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    public boolean isMobileNo(String mobiles) {
        Pattern p = Pattern
                .compile("^((1[3-5&7-8][0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        Log.i("dianhua", "dianhua" + m.matches());
        return m.matches();
    }

    /**
     * 邮箱验证
     *
     * @param email
     * @return
     * @version 1.0
     * @createTime 2015-9-7,上午9:59:44
     * @updateTime 2015-9-7,上午9:59:44
     * @createAuthor Zsj
     * @updateAuthor
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    public boolean isEmail(String email) {
        if (null == email || "".equals(email))
            return false;
        // Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern
                .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");// 复杂匹配
        Matcher m = p.matcher(email);
        Log.i("youxiang", "youxiang" + m.matches());
        return m.matches();
    }

    public boolean startCheck(String reg, String string) {
        boolean tem = false;

        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(string);

        tem = matcher.matches();
        return tem;
    }

    /**
     * 身份证号码验证
     *
     * @param idNr
     * @return
     * @version 1.0
     * @createTime 2015-9-7,上午9:54:59
     * @updateTime 2015-9-7,上午9:54:59
     * @createAuthor Zsj
     * @updateAuthor
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    public boolean checkIdCard(String idNr) {
        // String reg="^[1-9](\\d{14}|\\d{17})";
        if (idNr.length() == 15) {
            reg = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
        } else if (idNr.length() == 18) {
            // reg =
            // "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$";这里不包括判断最后一位是x的情况
            reg = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$";

        } else {
            reg = "^[1-9](\\d{14}|\\d{17})";
        }

        return startCheck(reg, idNr);
    }

    /**
     * 判断网络是否可用
     *
     * @param context
     * @return
     * @version 1.0
     * @createTime 2015-9-7,上午9:54:41
     * @updateTime 2015-9-7,上午9:54:41
     * @createAuthor Zsj
     * @updateAuthor
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    public boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取FinalHttp对象发送请求
     *
     * @return
     */
    public FinalHttp getFinalHttp() {
        if (finalHttp != null) {
            return finalHttp;
        } else {
            finalHttp = new FinalHttp();
        }
        finalHttp.configTimeout(300 * 1000);// 超时时间
//		finalHttp.configRequestExecutionRetryCount(1);// 请求错误重试次数
        return finalHttp;
    }

    /**
     * 判断sdcard是否被挂载
     */
    public boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @author text 保存用户输入的内容到文件
     */
    public boolean save(Context context, String content, String fileName) {

        // String content = editText.getText().toString();
        try {
            /*
			 * 根据用户提供的文件名，以及文件的应用模式，打开一个输出流.文件不存系统会为你创建一个的，
			 * 至于为什么这个地方还有FileNotFoundException抛出，我也比较纳闷。在Context中是这样定义的 public
			 * abstract FileOutputStream openFileOutput(String name, int mode)
			 * throws FileNotFoundException; openFileOutput(String name, int
			 * mode); 第一个参数，代表文件名称，注意这里的文件名称不能包括任何的/或者/这种分隔符，只能是文件名
			 * 该文件会被保存在/data/data/应用名称/files/chenzheng_java.txt 第二个参数，代表文件的操作模式
			 * MODE_PRIVATE 私有（只能创建它的应用访问） 重复写入时会文件覆盖 MODE_APPEND 私有
			 * 重复写入时会在文件的末尾进行追加，而不是覆盖掉原来的文件 MODE_WORLD_READABLE 公用 可读
			 * MODE_WORLD_WRITEABLE 公用 可读写
			 */
            FileOutputStream outputStream = context.openFileOutput(fileName,
                    Activity.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.flush();
            outputStream.close();
            Log.i("text", "文件保存成功");
            return true;
            // Toast.makeText(MainActivity.this, "保存成功",
            // Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @author chenzheng_java 读取文件中用户保存的内容
     */
    public String read(Context context, String fileName) {
        try {
            FileInputStream inputStream = context.openFileInput(fileName);
            byte[] bytes = new byte[1024];
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            while (inputStream.read(bytes) != -1) {
                arrayOutputStream.write(bytes, 0, bytes.length);
            }
            inputStream.close();
            arrayOutputStream.close();
            String content = new String(arrayOutputStream.toByteArray());
            // showTextView.setText(content);
            return content;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String setTtimeline(String dateNow, String dateStar) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = df.parse(dateNow);
            Date d2 = df.parse(dateStar);
            long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24))
                    / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
                    * (1000 * 60 * 60))
                    / (1000 * 60);
            long miao = (diff - days * (1000 * 60 * 60 * 24) - hours
                    * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000;
            System.out.println("" + days + "天" + hours + "小时" + minutes + "分"
                    + miao + "秒");
            if (days == 0 && hours > 0 && minutes > 0 && miao > 0) {
                return hours + "小时" + minutes + "分" + miao + "秒";
            } else if (days == 0 && hours == 0 && minutes > 0 && miao > 0) {

                return minutes + "分" + miao + "秒";
            } else if (days == 0 && hours == 0 && minutes == 0 && miao >= 0) {

                return miao + "秒";
            } else {

//                return "" + days + "天" + hours + "小时" + minutes + "分" + miao
//                        + "秒";
                //2018.4.28修改
                return "" + (days * 24 + hours) + "小时" + minutes + "分" + miao
                        + "秒";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * 计算已用时间
     */
    public String getOverTime(String nowTime, String subTime) {
        String strDate2 = subTime.replace("-", "/");
        long time = 0;
        if (nowTime.contains("-")) {// 2015-11-18
            long strDate = Date.parse(nowTime.replace("-", "/"));
            time = strDate - Date.parse(strDate2);
        } else {// 微秒
            time = Long.parseLong(nowTime) - Date.parse(strDate2);
        }
        MyDate myDate2 = new MyDate();
        String userTime = myDate2.formatSeconds(time / 1000);
        Log.i("userTime", userTime + "");
        return userTime;
    }

    /**
     * 获取系统当前时间
     *
     * @return
     */
    public String getSystemTime() {
        // SimpleDateFormat sDateFormat = new SimpleDateFormat(
        // "yyyy-MM-dd    hh:mm:ss");//12小时
        SimpleDateFormat sDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd    HH:mm:ss");// 24小时
        return sDateFormat.format(new Date());

    }

    public ProgressDialog progressDialog;

    /*
     * 提示加载
     */
    public void showProgressDialog(Context context, String title, String message) {
        if (progressDialog == null) {

            progressDialog = ProgressDialog.show(context, title, message, true,
                    false);
        } else if (progressDialog.isShowing()) {
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
        }

        progressDialog.show();

    }

    /*
     * 隐藏提示加载
     */
    public void hideProgressDialog() {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }

    }

    protected MySharePreferencesService service = new MySharePreferencesService();
    protected Map<String, String> map;
    protected String reloginString = "";
    protected UserSevice usevice;

    /**
     * 重新登录
     */
    public void relogin() {
        map = service.getPreferences();
        if (usevice == null) {
            usevice = Control.getinstance().getUserSevice();
            ToastUtil.showLongToast(DemoApplication.applicationContext,
                    "登录超时,自动重新登陆");
        }
        usevice.relogin(map.get("loginName"), map.get("password"),
                map.get("selectedRolem"), new UserSeviceImpl.UserSeviceImplListListenser() {

                    @Override
                    public void setUserSeviceImplListListenser(Object object,
                                                               String stRerror, String Exceptionerror) {
                        // TODO Auto-generated method stub
                        String str = null;
                        String string = "";
                        // 若登陆成功，直接进入主界面
                        if (object != null) {
                            UserEntity userEntity = (UserEntity) object;
                            if (userEntity.getSuccess().equals("true")) {
                                str = "登陆成功";
                                ToastUtil
                                        .showLongToast(
                                                DemoApplication.applicationContext,
                                                str);
                                // netListener.initNetData();
                            } else {
                                str = "密码已失效,请重新登陆";
                                ToastUtil
                                        .showLongToast(
                                                DemoApplication.applicationContext,
                                                str);
                                Intent intent = new Intent(
                                        DemoApplication.applicationContext,
                                        LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                DemoApplication.applicationContext
                                        .startActivity(intent);
                            }

                        } else if (stRerror != null) {
                            str = stRerror;
                            ToastUtil.showLongToast(
                                    DemoApplication.applicationContext, str);
                            Intent intent = new Intent(
                                    DemoApplication.applicationContext,
                                    LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            DemoApplication.applicationContext
                                    .startActivity(intent);
                        } else if (Exceptionerror != null) {
                            str = Const.NETWORKERROR + Exceptionerror;
                            ToastUtil.showLongToast(
                                    DemoApplication.applicationContext, str);
                            Intent intent = new Intent(
                                    DemoApplication.applicationContext,
                                    LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            DemoApplication.applicationContext
                                    .startActivity(intent);
                        }
                    }
                });
    }

    /**
     * list去重复
     *
     * @param list
     * @return
     */
    public List<String> removeDuplicate(List<String> list) {
        Set set = new LinkedHashSet<String>();
        set.addAll(list);
        list.clear();
        list.addAll(set);
        return list;
    }

    /**
     * 将字符串用","切割成数组
     *
     * @param str
     * @return
     */
    private String[] convertStrToArray(String str) {
        String[] strArray = null;
        strArray = str.split(","); // 拆分字符为"," ,然后把结果交给数组strArray
        return strArray;
    }

    /**
     * @Description:把数组转换为一个用逗号分隔的字符串 ，以便于用in+String 查询
     */
    public static String converToString(String[] ig) {
        String str = "";
        if (ig != null && ig.length > 0) {
            for (int i = 0; i < ig.length; i++) {
                str += ig[i] + ",";
            }
        }
        str = str.substring(0, str.length() - 1);
        return str;
    }

    /**
     * @Description:把list转换为一个用逗号分隔的字符串
     */
    public static String listToString(List list) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    sb.append(list.get(i) + ",");
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString();
    }

    /**
     * 随机生成四位数字加字母
     *
     * @return
     */
    public String code2() {
        // 生成验证码
        char[] bytes = new char[4];
        Random ran = new Random();
        for (int i = 0; i < bytes.length; i++) {
            byte bit = (byte) ran.nextInt(62);
            if (bit < 10)
                bytes[i] = (char) (bit + 48);
            else if (bit < 36)
                bytes[i] = (char) (bit + 55);
            else
                bytes[i] = (char) (61 + bit);
        }
        // 验证码
        return new String(bytes);
    }

    /**
     * 随机生成四位数字
     *
     * @return
     */
    public String code() {
        Random random = new Random();
        int num = 0;
        for (int i = 0; i <= 3; i++) {
            num = num * 10 + random.nextInt(9);
        }
        return String.valueOf(num);
    }
}
