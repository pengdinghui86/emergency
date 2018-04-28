package com.dssm.esc.util;

import com.dssm.esc.model.entity.emergency.ChildEntity;

import java.util.Comparator;


public class PinyinComparator2 implements Comparator<ChildEntity>{

	

	@Override
	public int compare(ChildEntity name, ChildEntity sortLetters) {
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
