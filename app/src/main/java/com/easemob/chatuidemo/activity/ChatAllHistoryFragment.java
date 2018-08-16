package com.easemob.chatuidemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dssm.esc.R;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.DemoApplication;
import com.easemob.chatuidemo.adapter.ChatAllHistoryAdapter;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.event.mainEvent;
import com.dssm.esc.view.activity.MainActivity;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import de.greenrobot.event.EventBus;

import com.easemob.chatuidemo.db.InviteMessgeDao;

/**
 * 显示所有会话记录，比较简单的实现，更好的可能是把陌生人存入本地，这样取到的聊天记录是可控的
 */
public class ChatAllHistoryFragment extends Fragment implements
        View.OnClickListener {

    private InputMethodManager inputMethodManager;
    private ListView listView;
    private ChatAllHistoryAdapter adapter;
    private EditText query;
    private ImageButton clearSearch;
    public RelativeLayout errorItem;
    // 取本地存的用户信息
    private MySharePreferencesService service;
    public TextView errorText;
    private boolean hidden;
    private List<EMConversation> conversationList = new ArrayList<EMConversation>();
    private TextView emptytv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_conversation_history,
                container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null
                && savedInstanceState.getBoolean("isConflict", false))
            return;
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
        inputMethodManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        errorItem = (RelativeLayout) getView().findViewById(R.id.rl_error_item);
        errorText = (TextView) errorItem.findViewById(R.id.tv_connect_errormsg);
        service = MySharePreferencesService.getInstance(getActivity());
        conversationList.addAll(loadConversationsWithRecentChat());
        listView = (ListView) getView().findViewById(R.id.list);
        emptytv = (TextView) getView().findViewById(R.id.emptytv);
        adapter = new ChatAllHistoryAdapter(getActivity(), 1, conversationList);
        // 设置adapter
        listView.setAdapter(adapter);

        final String st2 = getResources().getString(
                R.string.Cant_chat_with_yourself);
        listView.setEmptyView(emptytv);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (((MainActivity) getActivity()).hxMsgCount > 0) {
                    ((MainActivity) getActivity()).hxMsgCount = 0;
                }

                EMConversation conversation = adapter.getItem(position);
                final String username = conversation.getUserName();
                if (username.equals(DemoApplication.getInstance().getUserName()))
                    Toast.makeText(getActivity(), st2, Toast.LENGTH_SHORT).show();
                else {
                    // 进入聊天页面
                    final Intent intent = new Intent(getActivity(),ChatActivity.class);
                    if (conversation.isGroup()) {
                        if (conversation.getType() == EMConversationType.ChatRoom) {
                            // it is group chat
                            intent.putExtra("chatType",
                                    ChatActivity.CHATTYPE_CHATROOM);
                            intent.putExtra("groupId", username);
                        } else {
                            // it is group chat
                            intent.putExtra("chatType",
                                    ChatActivity.CHATTYPE_GROUP);
                            intent.putExtra("groupId", username);
                        }

                    } else {
                        // it is single chat
                        intent.putExtra("userId", username);
                        intent.putExtra("name", service.getcontectName(username
                                .replace("_", "-")));
                    }
                    startActivity(intent);
                }
            }
        });

        // 注册上下文菜单，Android7.0以上menu会在view附近弹出，7.0以下在屏幕中间弹出
//        registerForContextMenu(listView);

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
                        EMConversation tobeDeleteCons = adapter.getItem(i);
                        // 删除此会话
                        EMChatManager.getInstance().deleteConversation(
                                tobeDeleteCons.getUserName(), tobeDeleteCons.isGroup(),
                                false);
                        InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
                        inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());
                        adapter.remove(tobeDeleteCons);
                        adapter.notifyDataSetChanged();
                        window.dismiss();
                    }
                });
                delete_message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EMConversation tobeDeleteCons = adapter.getItem(i);
                        // 删除此会话
                        EMChatManager.getInstance().deleteConversation(
                                tobeDeleteCons.getUserName(), tobeDeleteCons.isGroup(),
                                true);
                        InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
                        inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());
                        adapter.remove(tobeDeleteCons);
                        adapter.notifyDataSetChanged();
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

    void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity()
                                .getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // if(((AdapterContextMenuInfo)menuInfo).position > 0){ m,
        getActivity().getMenuInflater().inflate(R.menu.delete_message, menu);

        // }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean handled = false;
        boolean deleteMessage = false;
        if (item.getItemId() == R.id.delete_message) {
            deleteMessage = true;
            handled = true;
        } else if (item.getItemId() == R.id.delete_conversation) {
            deleteMessage = false;
            handled = true;
        }
        EMConversation tobeDeleteCons = adapter
                .getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
        // 删除此会话
        EMChatManager.getInstance().deleteConversation(
                tobeDeleteCons.getUserName(), tobeDeleteCons.isGroup(),
                deleteMessage);
        InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
        inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());
        adapter.remove(tobeDeleteCons);
        adapter.notifyDataSetChanged();

        // 更新消息未读数
        // ((MainActivity) getActivity()).updateUnreadLabel();
        return handled ? true : super.onContextItemSelected(item);
    }

    /**
     * 刷新页面
     */
    public void refresh() {
        conversationList.clear();
        conversationList.addAll(loadConversationsWithRecentChat());
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    /**
     * 获取所有会话
     *
     * @return +
     */
    private List<EMConversation> loadConversationsWithRecentChat() {
        // 获取所有会话，包括陌生人
        Hashtable<String, EMConversation> conversations = EMChatManager
                .getInstance().getAllConversations();
        // 过滤掉messages size为0的conversation
        /**
         * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
         * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
         */
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    // if(conversation.getType() !=
                    // EMConversationType.ChatRoom){
                    sortList.add(new Pair<Long, EMConversation>(conversation
                            .getLastMessage().getMsgTime(), conversation));
                    // }
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * 根据最后一条消息的时间排序
     */
    private void sortConversationByLastChatTime(
            List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList,
                new Comparator<Pair<Long, EMConversation>>() {
                    @Override
                    public int compare(final Pair<Long, EMConversation> con1,
                                       final Pair<Long, EMConversation> con2) {

                        if (con1.first == con2.first) {
                            return 0;
                        } else if (con2.first > con1.first) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }

                });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden) {
            refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden && !MainActivity.isConflict) {
            refresh();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (MainActivity.isConflict) {
            outState.putBoolean("isConflict", true);
        } else if (MainActivity.getCurrentAccountRemoved()) {
            outState.putBoolean(Constant.ACCOUNT_REMOVED, true);
        }
    }

    @Override
    public void onClick(View v) {
    }

    /***
     * 打开EventBus开关
     */
    protected boolean useEventBus() {
        return true;
    }

    /**
     * 接收推送过来的消息类型，向MessageFragment发送消息，要显示哪个界面
     *
     * @param data
     */

    public void onEvent(mainEvent data) {
        if (data.getData().equals("count") || data.getData().equals("userid")) {
            if (!hidden && !MainActivity.isConflict) {
                refresh();
            }
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (useEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
