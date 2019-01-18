/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easemob.chatuidemo.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dssm.esc.R;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.PermissionsChecker;
import com.dssm.esc.util.VoiceRecorderUtil;
import com.dssm.esc.view.activity.MainActivity;
import com.dssm.esc.view.activity.PermissionsActivity;
import com.easemob.chatuidemo.adapter.ExpressionAdapter;
import com.easemob.chatuidemo.adapter.ExpressionPagerAdapter;
import com.easemob.chatuidemo.adapter.MessageAdapter;
import com.easemob.chatuidemo.adapter.VoicePlayClickListener;
import com.easemob.chatuidemo.utils.CommonUtils;
import com.easemob.chatuidemo.utils.SmileUtils;
import com.easemob.chatuidemo.widget.ExpandGridView;
import com.dssm.esc.util.event.mainEvent;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.options.MessageSendingOptions;
import cn.jpush.im.api.BasicCallback;
import de.greenrobot.event.EventBus;

/**
 * 聊天页面
 */
public class ChatActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = "ChatActivity";
    private static final int REQUEST_CODE_EMPTY_HISTORY = 2;
    public static final int REQUEST_CODE_CONTEXT_MENU = 3;
    private static final int REQUEST_CODE_MAP = 4;
    public static final int REQUEST_CODE_TEXT = 5;
    public static final int REQUEST_CODE_VOICE = 6;
    public static final int REQUEST_CODE_PICTURE = 7;
    public static final int REQUEST_CODE_LOCATION = 8;
    public static final int REQUEST_CODE_NET_DISK = 9;
    public static final int REQUEST_CODE_FILE = 10;
    public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
    public static final int REQUEST_CODE_PICK_VIDEO = 12;
    public static final int REQUEST_CODE_DOWNLOAD_VIDEO = 13;
    public static final int REQUEST_CODE_VIDEO = 14;
    public static final int REQUEST_CODE_DOWNLOAD_VOICE = 15;
    public static final int REQUEST_CODE_SELECT_USER_CARD = 16;
    public static final int REQUEST_CODE_SEND_USER_CARD = 17;
    public static final int REQUEST_CODE_CAMERA = 18;
    public static final int REQUEST_CODE_LOCAL = 19;
    public static final int REQUEST_CODE_CLICK_DESTORY_IMG = 20;
    public static final int REQUEST_CODE_GROUP_DETAIL = 21;
    public static final int REQUEST_CODE_SELECT_VIDEO = 23;
    public static final int REQUEST_CODE_SELECT_FILE = 24;
    public static final int REQUEST_CODE_ADD_TO_BLACKLIST = 25;

    public static final int RESULT_CODE_COPY = 1;
    public static final int RESULT_CODE_DELETE = 2;
    public static final int RESULT_CODE_FORWARD = 3;
    public static final int RESULT_CODE_OPEN = 4;
    public static final int RESULT_CODE_DWONLOAD = 5;
    public static final int RESULT_CODE_TO_CLOUD = 6;
    public static final int RESULT_CODE_EXIT_GROUP = 7;

    public static final int CHATTYPE_SINGLE = 1;
    public static final int CHATTYPE_GROUP = 2;
    public static final int CHATTYPE_CHATROOM = 3;

    public static final String COPY_IMAGE = "EASEMOBIMG";
    private View recordingContainer;
    private ImageView micImage;
    private TextView recordingHint;
    private ListView listView;
    private EditText mEditTextContent;
    private View buttonSetModeKeyboard;
    private View buttonSetModeVoice;
    private View buttonSend;
    private View buttonPressToSpeak;
    private LinearLayout emojiIconContainer;
    private ImageView locationImgview;
    private View more;
    private ClipboardManager clipboard;
    private ViewPager expressionViewpager;
    private InputMethodManager manager;
    private List<String> reslist;
    private Drawable[] micImages;
    private int chatType;
    private Conversation conversation;
    public ChatActivity activityInstance = null;
    // 给谁发送消息
    private String toChatUsername;
    // 显示esc的用户名
    public static String name;
    private VoiceRecorderUtil voiceRecorder;
    private MessageAdapter adapter;
    static int resendPos;
    private ImageView iv_emoticons_normal;
    private ImageView iv_emoticons_checked;
    private RelativeLayout edittext_layout;
    private ProgressBar loadmorePB;
    private boolean isloading;
    private final int pagesize = 20;
    private boolean haveMoreData = true;
    // private Button btnMore;
    public String playMsgId;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Handler micImageHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            // 切换msg切换图片
            micImage.setImageDrawable(micImages[msg.what]);
        }
    };
    public boolean isRobot;

    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private static final int REQUEST_AUDIO_RECODE_CODE = 0; // 请求录音

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        activityInstance = this;
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
        initView();
        setUpView();
        mPermissionsChecker = new PermissionsChecker(this);
    }

    /***
     * 打开EventBus开关
     */
    protected boolean useEventBus() {
        return true;
    }

    /**
     * initView
     */
    protected void initView() {
        recordingContainer = findViewById(R.id.recording_container);
        micImage = (ImageView) findViewById(R.id.mic_image);
        recordingHint = (TextView) findViewById(R.id.recording_hint);
        listView = (ListView) findViewById(R.id.list);
        mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
        buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
        edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
        buttonSetModeVoice = findViewById(R.id.btn_set_mode_voice);
        buttonSend = findViewById(R.id.btn_send);
        buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
        expressionViewpager = (ViewPager) findViewById(R.id.vPager);
        emojiIconContainer = (LinearLayout) findViewById(R.id.ll_face_container);
        // btnContainer = (LinearLayout) findViewById(R.id.ll_btn_container);
        locationImgview = (ImageView) findViewById(R.id.btn_location);
        iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
        iv_emoticons_checked = (ImageView) findViewById(R.id.iv_emoticons_checked);
        loadmorePB = (ProgressBar) findViewById(R.id.pb_load_more);
        // btnMore = (Button) findViewById(R.id.btn_more);
        iv_emoticons_normal.setVisibility(View.VISIBLE);
        iv_emoticons_checked.setVisibility(View.INVISIBLE);
        more = findViewById(R.id.more);
        edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);
        voiceCallBtn = (ImageView) findViewById(R.id.btn_voice_call);
        videoCallBtn = (ImageView) findViewById(R.id.btn_video_call);

        // 动画资源文件,用于录制语音时
        micImages = new Drawable[]{
                getResources().getDrawable(R.drawable.record_animate_01),
                getResources().getDrawable(R.drawable.record_animate_02),
                getResources().getDrawable(R.drawable.record_animate_03),
                getResources().getDrawable(R.drawable.record_animate_04),
                getResources().getDrawable(R.drawable.record_animate_05),
                getResources().getDrawable(R.drawable.record_animate_06),
                getResources().getDrawable(R.drawable.record_animate_07),
                getResources().getDrawable(R.drawable.record_animate_08),
                getResources().getDrawable(R.drawable.record_animate_09),
                getResources().getDrawable(R.drawable.record_animate_10),
                getResources().getDrawable(R.drawable.record_animate_11),
                getResources().getDrawable(R.drawable.record_animate_12),
                getResources().getDrawable(R.drawable.record_animate_13),
                getResources().getDrawable(R.drawable.record_animate_14)};

        // 表情list
        reslist = getExpressionRes(35);
        // 初始化表情viewpager
        List<View> views = new ArrayList<View>();
        View gv1 = getGridChildView(1);
        View gv2 = getGridChildView(2);
        views.add(gv1);
        views.add(gv2);
        expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));
        edittext_layout.requestFocus();
        voiceRecorder = new VoiceRecorderUtil(micImageHandler);
        buttonPressToSpeak.setOnTouchListener(new PressToSpeakListen());
        mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
                } else {
                    edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);
                }

            }
        });
        mEditTextContent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
                more.setVisibility(View.GONE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                emojiIconContainer.setVisibility(View.GONE);
                // btnContainer.setVisibility(View.GONE);
            }
        });
        // 监听文字框
        mEditTextContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!TextUtils.isEmpty(s)) {
                    // btnMore.setVisibility(View.GONE);
                    buttonSend.setVisibility(View.VISIBLE);
                } else {
                    // btnMore.setVisibility(View.VISIBLE);
                    buttonSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.chat_swipe_layout);

        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (listView.getFirstVisiblePosition() == 0
                                && !isloading && haveMoreData) {
                            List<Message> messages = new ArrayList<>();
                            try {
                                if (chatType == CHATTYPE_SINGLE) {
                                    messages = conversation.getAllMessage();
                                }
                            } catch (Exception e1) {
                                swipeRefreshLayout.setRefreshing(false);
                                return;
                            }

                            if (messages.size() > 0) {
                                adapter.notifyDataSetChanged();
                                adapter.refreshSeekTo(messages.size() - 1);
                                if (messages.size() != pagesize) {
                                    haveMoreData = false;
                                }
                            } else {
                                haveMoreData = false;
                            }

                            isloading = false;

                        } else {
                            Toast.makeText(
                                    ChatActivity.this,
                                    getResources().getString(
                                            R.string.no_more_messages),
                                    Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }

    private void setUpView() {
        iv_emoticons_normal.setOnClickListener(this);
        iv_emoticons_checked.setOnClickListener(this);
        // position = getIntent().getIntExtra("position", -1);
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
                .newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
        // 判断单聊还是群聊
        chatType = getIntent().getIntExtra("chatType", CHATTYPE_SINGLE);

        if (chatType == CHATTYPE_SINGLE) { // 单聊
            toChatUsername = getIntent().getStringExtra("userId");
            name = getIntent().getStringExtra("name");
        } else {
            // 群聊
            findViewById(R.id.container_to_group).setVisibility(View.VISIBLE);
            findViewById(R.id.container_remove).setVisibility(View.GONE);
            findViewById(R.id.container_voice_call).setVisibility(View.GONE);
            findViewById(R.id.container_video_call).setVisibility(View.GONE);
            toChatUsername = getIntent().getStringExtra("groupId");

            if (chatType == CHATTYPE_GROUP) {
                onGroupViewCreation();
            } else {
                onChatRoomViewCreation();
            }
        }

        // for chatroom type, we only init conversation and create view adapter
        // on success
        if (chatType != CHATTYPE_CHATROOM) {
            onConversationInit();

            onListViewCreation();

            // show forward message if the message is not null
            String forward_msg_id = getIntent()
                    .getStringExtra("forward_msg_id");
            if (forward_msg_id != null) {
                // 显示发送要转发的消息
                forwardMessage(forward_msg_id);
            }
        }
    }

    protected void onConversationInit() {
        conversation = JMessageClient.getSingleConversation(toChatUsername);
        EventBus.getDefault().post(new mainEvent("count"));
        if (conversation != null) {
            // 把此会话的未读数置为0
            conversation.setUnReadMessageCnt(0);
        }
        else
            conversation = Conversation.createSingleConversation(toChatUsername);
    }

    protected void onListViewCreation() {
        adapter = new MessageAdapter(ChatActivity.this, toChatUsername,
                chatType);
        // 显示消息
        listView.setAdapter(adapter);

        listView.setOnScrollListener(new ListScrollListener());
        adapter.refreshSelectLast();

        listView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                more.setVisibility(View.GONE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                emojiIconContainer.setVisibility(View.GONE);
                // btnContainer.setVisibility(View.GONE);
                return false;
            }
        });
    }

    protected void onGroupViewCreation() {

    }

    protected void onChatRoomViewCreation() {
        findViewById(R.id.container_to_group).setVisibility(View.GONE);

        final ProgressDialog pd = ProgressDialog
                .show(this, "", "Joining......");
    }

    /**
     * onActivityResult
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CODE_EXIT_GROUP) {
            setResult(RESULT_OK);
            finish();
            return;
        }
        if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
            switch (resultCode) {
                case RESULT_CODE_COPY: // 复制消息

                    break;
                case RESULT_CODE_DELETE: // 删除消息

                    break;

                case RESULT_CODE_FORWARD: // 转发消息
//				EMMessage forwardMsg = (EMMessage) adapter.getItem(data
//						.getIntExtra("position", 0));
//				Intent intent = new Intent(this, ForwardMessageActivity.class);
//				intent.putExtra("forward_msg_id", forwardMsg.getMsgId());
//				startActivity(intent);

                    break;

                default:
                    break;
            }
        }
        if (resultCode == RESULT_OK) { // 清空消息
            if (requestCode == REQUEST_CODE_EMPTY_HISTORY) {
                // 清空会话
                adapter.refresh();
            } else if (requestCode == REQUEST_CODE_LOCAL) { // 发送本地图片
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        sendPicByUri(selectedImage);
                    }
                }
            } else if (requestCode == REQUEST_CODE_SELECT_FILE) { // 发送选择的文件
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        sendFile(uri);
                    }
                }

            } else if (requestCode == REQUEST_CODE_MAP) { // 地图
                double latitude = data.getDoubleExtra("latitude", 0);
                double longitude = data.getDoubleExtra("longitude", 0);
                String locationAddress = data.getStringExtra("address");
                if (locationAddress != null && !locationAddress.equals("")) {
                    toggleMore(more);
                    sendLocationMsg(latitude, longitude, "", locationAddress);
                } else {
                    String st = getResources().getString(
                            R.string.unable_to_get_loaction);
                    Toast.makeText(this, st, Toast.LENGTH_SHORT).show();
                }
                // 重发消息
            } else if (requestCode == REQUEST_CODE_TEXT
                    || requestCode == REQUEST_CODE_VOICE
                    || requestCode == REQUEST_CODE_PICTURE
                    || requestCode == REQUEST_CODE_LOCATION
                    || requestCode == REQUEST_CODE_VIDEO
                    || requestCode == REQUEST_CODE_FILE) {
                resendMessage();
            } else if (requestCode == REQUEST_CODE_COPY_AND_PASTE) {
                // 粘贴
                if (!TextUtils.isEmpty(clipboard.getText())) {
                    String pasteText = clipboard.getText().toString();
                    if (pasteText.startsWith(COPY_IMAGE)) {
                        // 把图片前缀去掉，还原成正常的path
                        sendPicture(pasteText.replace(COPY_IMAGE, ""));
                    }

                }
            } else if (conversation.getAllMessage().size() > 0) {
                adapter.refresh();
                setResult(RESULT_OK);
            } else if (requestCode == REQUEST_CODE_GROUP_DETAIL) {
                adapter.refresh();
            }
        }
        // 缺少权限
        if (requestCode == REQUEST_AUDIO_RECODE_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            showMissingPermissionDialog();
        }
    }

    // 显示缺失权限提示
    private void showMissingPermissionDialog() {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ChatActivity.this);
        builder.setTitle(R.string.tip);
        builder.setMessage(R.string.permission_granted_audio_tip);

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                startAppSettings();
            }
        });

        builder.show();
    }

    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    /**
     * 消息图标点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        String st1 = getResources().getString(R.string.not_connect_to_server);
        int id = view.getId();
        if (id == R.id.btn_send) {// 点击发送按钮(发文字和表情)
            String s = mEditTextContent.getText().toString();
            sendText(s);
        }
        else if (id == R.id.iv_emoticons_normal) { // 点击显示表情框
            more.setVisibility(View.VISIBLE);
            iv_emoticons_normal.setVisibility(View.INVISIBLE);
            iv_emoticons_checked.setVisibility(View.VISIBLE);
            // btnContainer.setVisibility(View.GONE);
            emojiIconContainer.setVisibility(View.VISIBLE);
            hideKeyboard();
        } else if (id == R.id.iv_emoticons_checked) { // 点击隐藏表情框
            iv_emoticons_normal.setVisibility(View.VISIBLE);
            iv_emoticons_checked.setVisibility(View.INVISIBLE);
            // btnContainer.setVisibility(View.VISIBLE);
            emojiIconContainer.setVisibility(View.GONE);
            more.setVisibility(View.GONE);

        }
    }

    private void refreshUIWithNewMessage() {
        if (adapter == null) {
            return;
        }

        runOnUiThread(new Runnable() {
            public void run() {
                adapter.refreshSelectLast();
            }
        });
    }

    private void refreshUI() {
        if (adapter == null) {
            return;
        }

        runOnUiThread(new Runnable() {
            public void run() {
                adapter.refresh();
            }
        });
    }

    /**
     * 选择文件
     */
    private void selectFileFromLocal() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }

    /**
     * 从图库获取图片
     */
    public void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }

    /**
     * 发送文本消息
     */
    public void sendText(String content) {

        if (content.length() > 0) {
            //对方的username
            String name = toChatUsername;
            //对方所属应用的appkey，空默认为本应用
            String appkey = "";
            //是否保存离线
            boolean retainOfflineMsg = true;
            //是否显示通知
            boolean showNotification = true;
            //是否开启自定义接收方通知栏
            boolean enableCustomNotify = true;
            //是否需要对方发送已读回执
            boolean needReadReceipt = false;

            //通过username和appkey拿到会话对象，通过指定appkey可以创建一个和跨应用用户的会话对象，从而实现跨应用的消息发送
            Conversation mConversation = JMessageClient.getSingleConversation(name, appkey);
            if (mConversation == null) {
                mConversation = Conversation.createSingleConversation(name, appkey);
            }

            //构造message content对象
            TextContent textContent = new TextContent(content);
            //设置自定义的extra参数
            textContent.setStringExtra("", "");

            //创建message实体，设置消息发送回调。
            Message message = mConversation.createSendMessage(textContent, "");
            message.setOnSendCompleteCallback(new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    if (i == 0) {
                        Log.i(TAG, "JMessageClient.createSingleTextMessage" + ", responseCode = " + i + " ; LoginDesc = " + s);
                        Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i(TAG, "JMessageClient.createSingleTextMessage" + ", responseCode = " + i + " ; LoginDesc = " + s);
                        Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();
                    }
                    adapter.refresh();
                }
            });

            //设置消息发送时的一些控制参数
            MessageSendingOptions options = new MessageSendingOptions();
            options.setNeedReadReceipt(needReadReceipt);//是否需要对方用户发送消息已读回执
            options.setRetainOffline(retainOfflineMsg);//是否当对方用户不在线时让后台服务区保存这条消息的离线消息
            options.setShowNotification(showNotification);//是否让对方展示sdk默认的通知栏通知
            options.setCustomNotificationEnabled(enableCustomNotify);//是否需要自定义对方收到这条消息时sdk默认展示的通知栏中的文字
            if (enableCustomNotify) {
                options.setNotificationTitle(MySharePreferencesService.getInstance(getApplicationContext()).getcontectName("name"));//自定义对方收到消息时通知栏展示的title
                options.setNotificationAtPrefix("");//自定义对方收到消息时通知栏展示的@信息的前缀
                options.setNotificationText(content);//自定义对方收到消息时通知栏展示的text
            }
            //发送消息
            JMessageClient.sendMessage(message, options);

            // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
            adapter.refreshSelectLast();
            mEditTextContent.setText("");

            setResult(RESULT_OK);

        }
    }

    /**
     * 发送语音
     *
     * @param filePath
     * @param fileName
     * @param length
     * @param isResend
     */
    private void sendVoice(String filePath, String fileName, String length,
                           boolean isResend) {
        if (!(new File(filePath).exists())) {
            return;
        }
        try {
            adapter.refreshSelectLast();
            setResult(RESULT_OK);
            // send file
            // sendVoiceSub(filePath, fileName, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送图片
     *
     * @param filePath
     */
    private void sendPicture(final String filePath) {
        String to = toChatUsername;
        listView.setAdapter(adapter);
        adapter.refreshSelectLast();
        setResult(RESULT_OK);
        // more(more);
    }

    /**
     * 发送视频消息
     */
    private void sendVideo(final String filePath, final String thumbPath,
                           final int length) {
        final File videoFile = new File(filePath);
        if (!videoFile.exists()) {
            return;
        }
        try {
            listView.setAdapter(adapter);
            adapter.refreshSelectLast();
            setResult(RESULT_OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据图库图片uri发送图片
     *
     * @param selectedImage
     */
    private void sendPicByUri(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        String st8 = getResources().getString(R.string.cant_find_pictures);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(this, st8, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            sendPicture(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(this, st8, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }
            sendPicture(file.getAbsolutePath());
        }

    }

    /**
     * 发送位置信息
     *
     * @param latitude
     * @param longitude
     * @param imagePath
     * @param locationAddress
     */
    private void sendLocationMsg(double latitude, double longitude,
                                 String imagePath, String locationAddress) {

        listView.setAdapter(adapter);
        adapter.refreshSelectLast();
        setResult(RESULT_OK);

    }

    /**
     * 发送文件
     *
     * @param uri
     */
    private void sendFile(Uri uri) {
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = getContentResolver().query(uri, projection, null,
                        null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        File file = new File(filePath);
        if (file == null || !file.exists()) {
            String st7 = getResources().getString(R.string.File_does_not_exist);
            Toast.makeText(getApplicationContext(), st7, Toast.LENGTH_SHORT).show();
            return;
        }
        if (file.length() > 10 * 1024 * 1024) {
            String st6 = getResources().getString(
                    R.string.The_file_is_not_greater_than_10_m);
            Toast.makeText(getApplicationContext(), st6, Toast.LENGTH_SHORT).show();
            return;
        }

        listView.setAdapter(adapter);
        adapter.refreshSelectLast();
        setResult(RESULT_OK);
    }

    /**
     * 重发消息
     */
    private void resendMessage() {
        adapter.refreshSeekTo(resendPos);
    }

    /**
     * 显示语音图标按钮
     *
     * @param view
     */
    public void setModeVoice(View view) {
        hideKeyboard();
        edittext_layout.setVisibility(View.GONE);
        more.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        buttonSetModeKeyboard.setVisibility(View.VISIBLE);
        buttonSend.setVisibility(View.GONE);
        // btnMore.setVisibility(View.VISIBLE);
        buttonPressToSpeak.setVisibility(View.VISIBLE);
        iv_emoticons_normal.setVisibility(View.VISIBLE);
        iv_emoticons_checked.setVisibility(View.INVISIBLE);
        // btnContainer.setVisibility(View.VISIBLE);
        emojiIconContainer.setVisibility(View.GONE);

    }

    /**
     * 显示键盘图标
     *
     * @param view
     */
    public void setModeKeyboard(View view) {
        // mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener()
        // {
        // @Override
        // public void onFocusChange(View v, boolean hasFocus) {
        // if(hasFocus){
        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        // }
        // }
        // });
        edittext_layout.setVisibility(View.VISIBLE);
        more.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        buttonSetModeVoice.setVisibility(View.VISIBLE);
        // mEditTextContent.setVisibility(View.VISIBLE);
        mEditTextContent.requestFocus();
        // buttonSend.setVisibility(View.VISIBLE);
        buttonPressToSpeak.setVisibility(View.GONE);
        if (TextUtils.isEmpty(mEditTextContent.getText())) {
            // btnMore.setVisibility(View.VISIBLE);
            buttonSend.setVisibility(View.GONE);
        } else {
            // btnMore.setVisibility(View.GONE);
            buttonSend.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 点击清空聊天记录
     *
     * @param view
     */
    public void emptyHistory(View view) {
        String st5 = getResources().getString(
                R.string.Whether_to_empty_all_chats);
        startActivityForResult(
                new Intent(this, AlertDialog.class)
                        .putExtra("titleIsCancel", true).putExtra("msg", st5)
                        .putExtra("cancel", true), REQUEST_CODE_EMPTY_HISTORY);
    }

    /**
     * 点击进入群组详情
     *
     * @param view
     */
    public void toGroupDetails(View view) {

    }

    /**
     * 显示或隐藏图标按钮页
     *
     * @param view
     */
    public void toggleMore(View view) {
        if (more.getVisibility() == View.GONE) {
            hideKeyboard();
            more.setVisibility(View.VISIBLE);
            // btnContainer.setVisibility(View.VISIBLE);
            emojiIconContainer.setVisibility(View.GONE);
        } else {
            if (emojiIconContainer.getVisibility() == View.VISIBLE) {
                emojiIconContainer.setVisibility(View.GONE);
                // btnContainer.setVisibility(View.VISIBLE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
            } else {
                more.setVisibility(View.GONE);
            }

        }

    }

    /**
     * 点击文字输入框
     *
     * @param v
     */
    public void editClick(View v) {
        listView.setSelection(listView.getCount() - 1);
        if (more.getVisibility() == View.VISIBLE) {
            more.setVisibility(View.GONE);
            iv_emoticons_normal.setVisibility(View.VISIBLE);
            iv_emoticons_checked.setVisibility(View.INVISIBLE);
        }

    }

    private PowerManager.WakeLock wakeLock;
    private ImageView voiceCallBtn;
    private ImageView videoCallBtn;

    /**
     * 按住说话listener
     */
    class PressToSpeakListen implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!CommonUtils.isExitsSdcard()) {
                        String st4 = getResources().getString(
                                R.string.Send_voice_need_sdcard_support);
                        Toast.makeText(ChatActivity.this, st4, Toast.LENGTH_SHORT)
                                .show();
                        return false;
                    }
                    try {
                        // 缺少权限时, 进入权限配置页面
                        final String[] PERMISSIONS = new String[]{
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        };
                        List<String> permissionList = mPermissionsChecker.lacksPermissions(PERMISSIONS);
                        if (permissionList.size() > 0) {
                            startPermissionsActivity(permissionList.toArray(new String[permissionList.size()]), REQUEST_AUDIO_RECODE_CODE);
                        return false;
                        }
                        else {
                            v.setPressed(true);
                            wakeLock.acquire();
                            recordingContainer.setVisibility(View.VISIBLE);
                            recordingHint
                                    .setText(getString(R.string.move_up_to_cancel));
                            recordingHint.setBackgroundColor(Color.TRANSPARENT);
                            voiceRecorder.startRecording(null, toChatUsername,
                                    getApplicationContext());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        v.setPressed(false);
                        if (wakeLock.isHeld())
                            wakeLock.release();
                        if (voiceRecorder != null)
                            voiceRecorder.discardRecording();
                        recordingContainer.setVisibility(View.INVISIBLE);
                        Toast.makeText(ChatActivity.this, R.string.recoding_fail,
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    return true;
                case MotionEvent.ACTION_MOVE: {
                    if (event.getY() < 0) {
                        recordingHint
                                .setText(getString(R.string.release_to_cancel));
                        recordingHint
                                .setBackgroundResource(R.drawable.recording_text_hint_bg);
                    } else {
                        recordingHint
                                .setText(getString(R.string.move_up_to_cancel));
                        recordingHint.setBackgroundColor(Color.TRANSPARENT);
                    }
                    return true;
                }
                case MotionEvent.ACTION_UP:
                    v.setPressed(false);
                    recordingContainer.setVisibility(View.INVISIBLE);
                    if (wakeLock.isHeld())
                        wakeLock.release();
                    if (event.getY() < 0) {
                        // discard the recorded audio.
                        voiceRecorder.discardRecording();

                    } else {
                        // stop recording and send voice file
                        String st1 = getResources().getString(
                                R.string.Recording_without_permission);
                        String st2 = getResources().getString(
                                R.string.The_recording_time_is_too_short);
                        String st3 = getResources().getString(
                                R.string.send_failure_please);
                        try {
//                            if(!voiceRecorder.isRecording()) {
//                                voiceRecorder.discardRecording();
//                                return false;
//                            }
                            int length = voiceRecorder.stopRecoding();
                            if (length > 0) {
                                sendVoice(voiceRecorder.getVoiceFilePath(),
                                        voiceRecorder
                                                .getVoiceFileName(toChatUsername),
                                        Integer.toString(length), false);
                            } else {
                                Toast.makeText(getApplicationContext(), st2,
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ChatActivity.this, st3,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                    return true;
                default:
                    recordingContainer.setVisibility(View.INVISIBLE);
                    if (voiceRecorder != null)
                        voiceRecorder.discardRecording();
                    return false;
            }
        }
    }

    private void startPermissionsActivity(String[] permissions, int requestCode) {
        PermissionsActivity.startActivityForResult(this, requestCode, permissions);
    }

    /**
     * 获取表情的gridview的子view
     *
     * @param i
     * @return
     */
    private View getGridChildView(int i) {
        View view = View.inflate(this, R.layout.expression_gridview, null);
        ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
        List<String> list = new ArrayList<String>();
        if (i == 1) {
            List<String> list1 = reslist.subList(0, 20);
            list.addAll(list1);
        } else if (i == 2) {
            list.addAll(reslist.subList(20, reslist.size()));
        }
        list.add("delete_expression");
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this,
                1, list);
        gv.setAdapter(expressionAdapter);
        gv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String filename = expressionAdapter.getItem(position);
                try {
                    // 文字输入框可见时，才可输入表情
                    // 按住说话可见，不让输入表情
                    if (buttonSetModeKeyboard.getVisibility() != View.VISIBLE) {

                        if (filename != "delete_expression") { // 不是删除键，显示表情
                            // 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
                            Class clz = Class.forName("com.easemob.chatuidemo.utils.SmileUtils");
                            Field field = clz.getField(filename);
                            mEditTextContent.append(SmileUtils.getSmiledText(ChatActivity.this, (String) field.get(null)));
                        } else { // 删除文字或者表情
                            if (!TextUtils.isEmpty(mEditTextContent.getText())) {

                                int selectionStart = mEditTextContent.getSelectionStart();// 获取光标的位置
                                if (selectionStart > 0) {
                                    String body = mEditTextContent.getText().toString();
                                    String tempStr = body.substring(0, selectionStart);
                                    int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                    if (i != -1) {
                                        CharSequence cs = tempStr.substring(i, selectionStart);
                                        if (SmileUtils.containsKey(cs.toString()))
                                            mEditTextContent.getEditableText().delete(i, selectionStart);
                                        else
                                            mEditTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
                                    } else {
                                        mEditTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
                                    }
                                }
                            }

                        }
                    }
                } catch (Exception e) {
                }

            }
        });
        return view;
    }

    public List<String> getExpressionRes(int getSum) {
        List<String> reslist = new ArrayList<String>();
        for (int x = 1; x <= getSum; x++) {
            String filename = "ee_" + x;

            reslist.add(filename);

        }
        return reslist;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
        if (useEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void onEvent(mainEvent data) {
        if (data.getData().equals("im")) {
            adapter.refresh();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        voiceCallBtn.setEnabled(true);
        videoCallBtn.setEnabled(true);

        if (adapter != null) {
            adapter.refresh();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wakeLock.isHeld())
            wakeLock.release();
        if (VoicePlayClickListener.isPlaying
                && VoicePlayClickListener.currentPlayListener != null) {
            // 停止语音播放
        }

        try {
            // 停止录音
            if (voiceRecorder.isRecording()) {
                voiceRecorder.discardRecording();
                recordingContainer.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 加入到黑名单
     *
     * @param username
     */
    private void addUserToBlacklist(final String username) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.Is_moved_into_blacklist));
        pd.setCanceledOnTouchOutside(false);
        pd.show();
    }

    /**
     * 返回
     *
     * @param view
     */
    public void back(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 覆盖手机返回键
     */
    @Override
    public void onBackPressed() {
        if (more.getVisibility() == View.VISIBLE) {
            more.setVisibility(View.GONE);
            iv_emoticons_normal.setVisibility(View.VISIBLE);
            iv_emoticons_checked.setVisibility(View.INVISIBLE);
        } else {
            super.onBackPressed();
            if (chatType == CHATTYPE_CHATROOM) {

            }
        }
    }

    /**
     * listview滑动监听listener
     */
    private class ListScrollListener implements OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                case OnScrollListener.SCROLL_STATE_IDLE:
                /*
                 * if (view.getFirstVisiblePosition() == 0 && !isloading &&
				 * haveMoreData && conversation.getAllMessages().size() != 0) {
				 * isloading = true; loadmorePB.setVisibility(View.VISIBLE); //
				 * sdk初始化加载的聊天记录为20条，到顶时去db里获取更多 List<EMMessage> messages;
				 * EMMessage firstMsg = conversation.getAllMessages().get(0);
				 * try { // 获取更多messges，调用此方法的时候从db获取的messages //
				 * sdk会自动存入到此conversation中 if (chatType == CHATTYPE_SINGLE)
				 * messages =
				 * conversation.loadMoreMsgFromDB(firstMsg.getMsgId(),
				 * pagesize); else messages =
				 * conversation.loadMoreGroupMsgFromDB(firstMsg.getMsgId(),
				 * pagesize); } catch (Exception e1) {
				 * loadmorePB.setVisibility(View.GONE); return; } try {
				 * Thread.sleep(300); } catch (InterruptedException e) { } if
				 * (messages.size() != 0) { // 刷新ui if (messages.size() > 0) {
				 * adapter.refreshSeekTo(messages.size() - 1); }
				 *
				 * if (messages.size() != pagesize) haveMoreData = false; } else
				 * { haveMoreData = false; }
				 * loadmorePB.setVisibility(View.GONE); isloading = false;
				 *
				 * }
				 */
                    break;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {

        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        // 点击notification bar进入聊天页面，保证只有一个聊天页面
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }

    /**
     * 转发消息
     *
     * @param forward_msg_id
     */
    protected void forwardMessage(String forward_msg_id) {

    }

    public String getToChatUsername() {
        return toChatUsername;
    }

    public ListView getListView() {
        return listView;
    }

}
