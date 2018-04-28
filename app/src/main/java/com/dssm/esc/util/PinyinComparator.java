package com.dssm.esc.util;

import com.dssm.esc.model.entity.emergency.GroupEntity;

import java.util.Comparator;


public class PinyinComparator implements Comparator<GroupEntity>
		 {


	@Override
	public int compare(GroupEntity name, GroupEntity sortLetters) {
		// TODO Auto-generated method stub
		if (name.getSortLetters().equals("@")
				|| sortLetters.getSortLetters().equals("#")) {
			return -1;
		} else if (name.getSortLetters().equals("#")
				|| sortLetters.getSortLetters().equals("@")) {
			return 1;
		} else {
			return name.getSortLetters()
					.compareTo(sortLetters.getSortLetters());
		}
	}

	

}
