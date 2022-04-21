package com.game.manager;

import com.game.entity.Character;
import com.game.entity.User;
import com.game.proto.Message.*;
import com.game.spring.DBUtil;
import com.game.spring.ScheduleTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author 贾超博
 *
 * Component in Manager
 *
 * UserManager Class
 *
 *
 */
// 只管理角色,当角色进入时,负责管理
public class UserManager {
    public static UserManager Instance = new UserManager();

    public Map<Integer, User> userMap = Collections.synchronizedMap(new HashMap<>());

    public void init() {

    }

    // 这里角色即将进入游戏世界
    public NUser addUser(User user) {
        updateUserStatusInfo(user.id, true); //更新用户在线状态
        // 将 数据库中的角色信息,构筑城 网络传输的NUser
        userMap.put(user.id, user);
        return user.info();
    }

    public void removeUser(int userId) {
        updateUserStatusInfo(userId, false); //更新用户离线状态
        userMap.remove(userId);
    }

    public User getUser(int userId) {
        User user = userMap.get(userId);
        if (user == null) {
            String lockUid = "getUser_" + userId;
            synchronized (lockUid.intern()) {
                user = userMap.get(userId);
                if (user != null) {
                    return user;
                }
                //TODO 加redis
                user = DBUtil.Instance.getUserDao().queryById(userId);
                if (user != null) {
                    userMap.put(userId, user);
                }
            }
        }
        return userMap.get(userId);
    }

    /**
     * 检测更新用户到数据库
     */
    public void updateObjDBInfo() {
        Collection<User> users = UserManager.Instance.userMap.values();
        for (User user : users) {
            user.updateToDB();
        }
    }

    /**
     * 在线、离线用户状态更新
     */
    public void updateUserStatusInfo(int userId, boolean status) {
        Collection<User> users = UserManager.Instance.userMap.values();
        for (User user : users) {
            if (user.offLineUserManager == null) {
                continue;
            }
            user.offLineUserManager.AddUserStatus(userId, status);
        }
    }

    /**
     * 根据状态查找用户
     */
    public List<User> queryByUserStatus(UserStatus userStatus) {
        List<User> resultList = new ArrayList<>();
        Collection<User> users = UserManager.Instance.userMap.values();
        for (User user : users) {
            if (user.getStatus() == userStatus) {
                resultList.add(user);
            }
        }
        return resultList;
    }
}
