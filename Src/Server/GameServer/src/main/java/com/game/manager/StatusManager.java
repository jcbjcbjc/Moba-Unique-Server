package com.game.manager;

import com.game.entity.Character;
import com.game.entity.User;
import com.game.proto.C2GNet;
import com.game.proto.C2GNet.NStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// 金币，物品，变动状态 管理
public class StatusManager {
    public User owner;
    public List<NStatus> statusList = Collections.synchronizedList(new ArrayList<>());

    public boolean hasStatus() {
        return statusList.size() > 0;
    }

    public StatusManager(User user) {
        owner = user;
    }

    // 增加一种 状态，StatusType 类型。id Itemid，value 数量，StatusAction增加|减少
    public void addStatus(C2GNet.StatusType type, int id, long value, C2GNet.StatusAction action) {
        NStatus.Builder builder = NStatus.newBuilder();
        NStatus build = builder.setType(type).setId(id).setValue(value).setAction(action).build();
        statusList.add(build);
    }

    // 增加|减少 gold
    public void addGoldChange(long goldDelta) {
        if (goldDelta > 0) {
            addStatus(C2GNet.StatusType.MONEY, 0, goldDelta, C2GNet.StatusAction.ADD);
        }
        if (goldDelta < 0) {
            addStatus(C2GNet.StatusType.MONEY, 0, -goldDelta, C2GNet.StatusAction.DELETE);
        }
    }

    // 增加|减少 装备
    public void addItemChange(int itemId, int count, C2GNet.StatusAction action) {
        addStatus(C2GNet.StatusType.ITEM, itemId, count, action);
    }

    public C2GNet.StatusNotify getStatusList() {
        C2GNet.StatusNotify.Builder builder = C2GNet.StatusNotify.newBuilder();

        for (int i = 0; i < statusList.size(); i++) {
            builder.addStatus(i,statusList.get(i));
        }
        statusList.clear();
        return builder.build();

    }
}
