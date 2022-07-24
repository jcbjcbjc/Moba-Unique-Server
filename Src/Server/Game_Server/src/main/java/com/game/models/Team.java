/*package com.game.models;

import com.game.entity.Character;
import com.game.proto.Message;
import com.game.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;
// 队伍模板
public class Team {
    public int id;
    public Character leander;
    // 全部公会成员
    public List<Character> members = new ArrayList<>(5);
    public int timeStamp;

    // 增加一个 队员到 队伍，首个队员 默认队长
    public void addMember(Character member) {
        if (members.size() == 0) {
            leander = member;
        } else {
            for (Character c : members) {
                if (c.getId() == member.getId()) {
                    return;
                }
            }
        }
        members.add(member);
        member.team = this;
        this.timeStamp = TimeUtil.getTimeStamp();
    }

    public void removeMember(Character member) {
        if (members.contains(member)) {
            member.team = null;
            members.remove(member);

            if (leander.getId() == member.getId()) {
                // 队长离队，需要判断当前队伍人数是否 大于0
                if (members.size() > 0) {
                    leander = members.get(0);
                }
            }

            timeStamp = TimeUtil.getTimeStamp();
        }
    }

    public Message.NTeamInfo teamToInfo() {
        if (members.size() > 0) {
            Message.NTeamInfo.Builder builder = Message.NTeamInfo.newBuilder();
            builder.setId(id).setLeader(leander.getId());
            for (int i = 0; i < members.size(); i++) {
                builder.addMembers(members.get(i).getBaseInfo());
            }
            return builder.build();
        }
        return null;
    }

}
*/