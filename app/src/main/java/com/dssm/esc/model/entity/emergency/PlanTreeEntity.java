package com.dssm.esc.model.entity.emergency;

import java.io.Serializable;
import java.util.List;

public class PlanTreeEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String treeId;// 预案id
    private String name;// 预案名称
    private List<GroupEntity> emeGroups;// 应急分组列表

    public String getTreeId() {
        return treeId;
    }

    public void setTreeId(String treeId) {
        this.treeId = treeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GroupEntity> getEmeGroups() {
        return emeGroups;
    }

    public void setEmeGroups(List<GroupEntity> emeGroups) {
        this.emeGroups = emeGroups;
    }
}
