package com.school.management.dto;

import java.util.List;

public class PermissionUpdateRequest {
    private List<String> add;
    private List<String> remove;

    public PermissionUpdateRequest() {}

    public List<String> getAdd() { return add; }
    public void setAdd(List<String> add) { this.add = add; }
    public List<String> getRemove() { return remove; }
    public void setRemove(List<String> remove) { this.remove = remove; }
}
