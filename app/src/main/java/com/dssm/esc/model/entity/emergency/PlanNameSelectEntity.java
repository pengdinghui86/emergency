package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;
import java.util.List;

/**
 * 预案名称选择的实体类
 * @Description TODO
 * @author Zsj
 * @date 2015-9-11
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co., Ltd. Inc. All rights reserved.
 */
public class PlanNameSelectEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	List<PlanNameRowEntity> rows;
	public List<PlanNameRowEntity> getRows() {
		return rows;
	}
	public void setRows(List<PlanNameRowEntity> rows) {
		this.rows = rows;
	}
	

}
