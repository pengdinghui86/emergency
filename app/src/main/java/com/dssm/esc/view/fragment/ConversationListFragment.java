package com.dssm.esc.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dssm.esc.R;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.mainEvent;
import com.dssm.esc.view.activity.MainActivity;
import com.dssm.esc.view.adapter.ConversationListAdapter;
import com.dssm.esc.view.widget.AutoListView;
import com.dssm.esc.view.widget.ClearEditText;
import com.easemob.chatuidemo.activity.ChatActivity;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * 显示所有会话记录
 */
public class ConversationListFragment extends BaseFragment implements
        AutoListView.OnRefreshListener, AutoListView.OnLoadListener, MainActivity.onInitNetListener{

    private InputMethodManager inputMethodManager;
    private AutoListView listView;
    // 取本地存的用户信息
    private MySharePreferencesService service;
    /**
     * 当前页展示list
     */
    private List<Conversation> list = new ArrayList<Conversation>();
    /**
     * 总list
     */
    private List<Conversation> totalList = new ArrayList<Conversation>();
    /**
     * 搜索过滤后list
     */
    private List<Conversation> searchList = new ArrayList<Conversation>();
    /**
     * 搜索输入框
     */
    private ClearEditText filter_edit;
    /**
     * 页面尾部灰色背景区域
     */
    private View message_listview_v_end;
    /**
     * 无数据显示布局
     */
    private LinearLayout ll_no_data_page;
    /**
     * 适配器
     */
    private ConversationListAdapter adapter;
    /**
     * 当前页数
     */
    private int i = 0;

    /**
     * 当前数据数量
     */
    private int limt = 0;
    /**
     * 搜索字符串
     */
    public String queryStr = "";

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            @SuppressWarnings("unchecked")
            List<Conversation> result = (ArrayList<Conversation>) msg.obj;
            switch (msg.what) {
                case AutoListView.REFRESH:
                    Log.i("44", "刷新");
                    listView.onRefreshComplete();
                    /** 总集合清理 */
                    list.clear();
                    /** 总集合添加 */
                    list.addAll(result);
                    if(result.size() > 0) {
                        ll_no_data_page.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        message_listview_v_end.setVisibility(View.VISIBLE);
                    } else {
                        ll_no_data_page.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                        message_listview_v_end.setVisibility(View.GONE);
                    }
                    break;
                case 2:// 第一次加载
                    listView.onRefreshComplete();
                    /** 总集合清理 */
                    list.clear();
                    list.addAll(result);
                    if(result.size() > 0) {
                        ll_no_data_page.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        message_listview_v_end.setVisibility(View.VISIBLE);
                    } else {
                        ll_no_data_page.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                        message_listview_v_end.setVisibility(View.GONE);
                    }
                    break;
                case AutoListView.LOAD:
                    listView.onLoadComplete();
                    list.addAll(result);
                    break;
            }
            listView.setResultSize(result.size(), i);
            adapter.notifyDataSetChanged();
            Utils.getInstance().hideProgressDialog();
        }
    };

    public ConversationListFragment() {

    }

    @Override
    protected View getViews() {
        return view_Parent = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_conversation_history, null);
    }

    @Override
    protected void findViews() {
        service = MySharePreferencesService.getInstance(getActivity());
        inputMethodManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        listView = (AutoListView) view_Parent
                .findViewById(R.id.list);
        filter_edit = (ClearEditText) view_Parent.findViewById(R.id.filter_edit);
        message_listview_v_end = (View) view_Parent.findViewById(R.id.message_listview_v_end);
        ll_no_data_page = (LinearLayout) view_Parent.findViewById(R.id.ll_no_data_page);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (((MainActivity) getActivity()).imMsgCount > 0) {
                    ((MainActivity) getActivity()).imMsgCount = 0;
                }

                Conversation conversation = adapter.getItem(position - 1);
                cn.jpush.im.android.api.model.Message message = conversation.getLatestMessage();
                final String username;
                if(message.getDirect() == MessageDirect.send)
                {
                    username = ((UserInfo) conversation.getTargetInfo()).getUserName();
                }
                else {
                    username = message.getFromUser().getUserName();
                }
                if (username.equals(service.getcontectName("userId")))
                    Toast.makeText(getActivity(), "不能给自己发消息", Toast.LENGTH_SHORT).show();
                else {
                    // 进入聊天页面
                    final Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("userId", username);
                    intent.putExtra("name", service.getcontectName(username
                            .replace("_", "-")));
                    startActivity(intent);
                }
            }
        });
        // 注册上下文菜单，Android7.0以上menu会在view附近弹出，7.0以下在屏幕中间弹出
        registerForContextMenu(listView);

        listView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 隐藏软键盘
                hideSoftKeyboard();
                return false;
            }

        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                // 用于PopupWindow的View
                View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.delete_message, null, false);
                final TextView delete_conversation = (TextView) contentView.findViewById(R.id.delete_conversation);
                final TextView delete_message = (TextView) contentView.findViewById(R.id.delete_message);
                // 创建PopupWindow对象，其中：
                // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
                // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
                final PopupWindow window = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                // 设置PopupWindow的背景
                window.setBackgroundDrawable(new BitmapDrawable());
                // 设置PopupWindow是否能响应外部点击事件
                window.setOutsideTouchable(true);
                // 设置PopupWindow是否能响应点击事件
                window.setTouchable(true);
                window.setFocusable(true);
                window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        //关闭窗口时恢复activity背景颜色
                        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                        lp.alpha = 1f;
                        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        getActivity().getWindow().setAttributes(lp);
                    }
                });

                delete_conversation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Conversation tobeDeleteCons = adapter.getItem(i);
                        // 删除此会话
                        JMessageClient.deleteSingleConversation(tobeDeleteCons.getLatestMessage().getFromUser().getUserName());
                        totalList.remove(tobeDeleteCons);
                        initData();
                        window.dismiss();
                    }
                });
                delete_message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Conversation tobeDeleteCons = adapter.getItem(i);
                        // 删除此会话
                        JMessageClient.deleteSingleConversation(tobeDeleteCons.getLatestMessage().getFromUser().getUserName());
                        totalList.remove(tobeDeleteCons);
                        initData();
                        window.dismiss();
                    }
                });
                // 显示PopupWindow，其中：
                // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
//                window.showAsDropDown(anchor, xoff, yoff);
                // 或者也可以调用此方法显示PopupWindow，其中：
                // 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
                // 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
                window.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                //弹出窗口时设置activity背景为灰色
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 0.7f;
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getActivity().getWindow().setAttributes(lp);
                return true;
            }
        });
    }

    @Override
    protected void widgetListener() {
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
    }

    @Override
    protected void init() {
        adapter = new ConversationListAdapter(getActivity(), list);
        listView.setAdapter(adapter);
        filter_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                filterData(s.toString());
            }
        });
    }

    @Override
    public void initGetData() {

    }

    public void initData() {
        i = 1;
        limt = 0;
        //获取所有会话列表
        totalList = JMessageClient.getConversationList();
        List<Conversation> tempList = new ArrayList<>(totalList);
        for(Conversation conversation : tempList)
        {
            if(conversation.getAllMessage().size() == 0)
                totalList.remove(conversation);
        }
        searchData();
        onLoadDta(2);
    }

    void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity()
                                .getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v,
//                                    ContextMenuInfo menuInfo) {
////        super.onCreateContextMenu(menu, v, menuInfo);
//        // if(((AdapterContextMenuInfo)menuInfo).position > 0){ m,
//        getActivity().getMenuInflater().inflate(R.menu.delete_message, menu);
//
//        // }
//    }

//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        boolean handled = false;
//        boolean deleteMessage = false;
//        if (item.getItemId() == R.id.delete_message) {
//            deleteMessage = true;
//            handled = true;
//        } else if (item.getItemId() == R.id.delete_conversation) {
//            deleteMessage = false;
//            handled = true;
//        }
//        EMConversation tobeDeleteCons = adapter
//                .getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
//        // 删除此会话
//        EMChatManager.getInstance().deleteConversation(
//                tobeDeleteCons.getUserName(), tobeDeleteCons.isGroup(),
//                deleteMessage);
//        InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
//        inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());
//        adapter.remove(tobeDeleteCons);
//        adapter.notifyDataSetChanged();
//
//        // 更新消息未读数
//        // ((MainActivity) getActivity()).updateUnreadLabel();
//        return handled ? true : super.onContextItemSelected(item);
//    }

    //    /**
//     * 刷新页面
//     */
//    public void refresh() {
//        conversationList.clear();
//        conversationList.addAll(loadConversationsWithRecentChat());
//        if (adapter != null)
//            adapter.notifyDataSetChanged();
//    }
//
//    /**
//     * 获取所有会话
//     *
//     * @return +
//     */
//    private List<EMConversation> loadConversationsWithRecentChat() {
//        // 获取所有会话，包括陌生人
//        Hashtable<String, EMConversation> conversations = EMChatManager
//                .getInstance().getAllConversations();
//        // 过滤掉messages size为0的conversation
//        /**
//         * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
//         * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
//         */
//        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
//        synchronized (conversations) {
//            for (EMConversation conversation : conversations.values()) {
//                if (conversation.getAllMessages().size() != 0) {
//                    // if(conversation.getType() !=
//                    // EMConversationType.ChatRoom){
//                    sortList.add(new Pair<Long, EMConversation>(conversation
//                            .getLastMessage().getMsgTime(), conversation));
//                    // }
//                }
//            }
//        }
//        try {
//            // Internal is TimSort algorithm, has bug
//            sortConversationByLastChatTime(sortList);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        List<EMConversation> list = new ArrayList<EMConversation>();
//        for (Pair<Long, EMConversation> sortItem : sortList) {
//            list.add(sortItem.second);
//        }
//        return list;
//    }
//
//    /**
//     * 根据最后一条消息的时间排序
//     */
//    private void sortConversationByLastChatTime(
//            List<Pair<Long, EMConversation>> conversationList) {
//        Collections.sort(conversationList,
//                new Comparator<Pair<Long, EMConversation>>() {
//                    @Override
//                    public int compare(final Pair<Long, EMConversation> con1,
//                                       final Pair<Long, EMConversation> con2) {
//
//                        if (con1.first == con2.first) {
//                            return 0;
//                        } else if (con2.first > con1.first) {
//                            return 1;
//                        } else {
//                            return -1;
//                        }
//                    }
//
//                });
//    }
//
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onRefresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        if (MainActivity.isConflict) {
//            outState.putBoolean("isConflict", true);
//        } else if (MainActivity.getCurrentAccountRemoved()) {
//            outState.putBoolean(Constant.ACCOUNT_REMOVED, true);
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//    }
//
    /***
     * 打开EventBus开关
     */
    protected boolean useEventBus() {
        return true;
    }

    /**
     * 过滤数据
     */
    private void searchData() {
        searchList.clear();
        if("".equals(queryStr))
            searchList = totalList;
        else {
            for (Conversation conversation : totalList) {
                TextContent textContent = (TextContent) conversation.getLatestMessage().getContent();
                if (queryStr.equals(conversation.getLatestMessage().getFromUser().getUserName())
                        || queryStr.equals(textContent.getText())) {
                    searchList.add(conversation);
                }
            }
        }
    }

    /**
     * 上拉加载更多数据
     *
     * @param what
     */
    private void onLoadDta(int what) {
        List<Conversation> dataList = new ArrayList<>();
        Message msg = handler.obtainMessage();
        msg.what = what;
        //已是最新的数据
        if (limt >= searchList.size()) {
            listView.onRefreshComplete();
        }
        else if (limt + 20 > searchList.size()) {
            dataList = new ArrayList(searchList.subList(limt, searchList.size()));
            limt = searchList.size();
        } else {
            dataList = new ArrayList(searchList.subList(limt, limt + 20));
            limt = limt + 20;
        }
        msg.obj = dataList;
        handler.sendMessage(msg);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initNetData() {
        initData();
    }

    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void onLoad() {
        onLoadDta(1);
    }

    /**
     * 接收到推送消息，刷新界面
     *
     * @param data
     */
    public void onEvent(mainEvent data) {
        if (data.getData().equals("refresh")) {
            initData();
        }
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     */
    private void filterData(String filterStr) {
        queryStr = filterStr;
        initData();
    }
}