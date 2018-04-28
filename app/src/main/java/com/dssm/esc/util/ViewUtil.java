package com.dssm.esc.util;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.dssm.esc.R;


public class ViewUtil {

	public static RadioButton createTabRadioButton(TabTypeEnum tabType,
			Context context) {
		RadioButton rb = (RadioButton) LayoutInflater.from(context).inflate(
				R.layout.tab_radiobutton, null);
		rb.setText(tabType.getTitle());
		rb.setId(tabType.getId());
		return rb;
	}

	public static TabTypeEnum getTabTypeEnum(RadioButton rb) {
		int id = rb.getId();
		for (TabTypeEnum em : TabTypeEnum.values()) {
			if (em.getId() == id) {
				return em;
			}
		}
		return null;
	}
	
	/**
     * 遍历布局，并禁用所有子控件
     * 
     * @param viewGroup
     *            布局对象
     */ 
    public static void setSubControlsEnabled(ViewGroup viewGroup,boolean isEnabled) { 
        for (int i = 0; i < viewGroup.getChildCount(); i++) { 
            View v = viewGroup.getChildAt(i); 
            if (v instanceof ViewGroup) { 
                if (v instanceof Spinner) { 
                    Spinner spinner = (Spinner) v; 
                    //spinner.setClickable(isEnabled); 
                    spinner.setEnabled(isEnabled); 
 
                } else if (v instanceof ListView) { 
                    //((ListView) v).setClickable(isEnabled); 
                    ((ListView) v).setEnabled(isEnabled); 
 
                } else { 
                	setSubControlsEnabled((ViewGroup) v,isEnabled); 
                } 
            } else if (v instanceof EditText) { 
                ((EditText) v).setEnabled(isEnabled); 
                //((EditText) v).setClickable(isEnabled); 
 
            } else if (v instanceof Button) { 
                //((Button) v).setEnabled(isEnabled); 
 
	        } else if (v instanceof CheckBox) { 
	        	//((CheckBox) v).setClickable(true); 
	            ((CheckBox) v).setEnabled(isEnabled); 
	
	        } 
        } 
    } 
    	
}
