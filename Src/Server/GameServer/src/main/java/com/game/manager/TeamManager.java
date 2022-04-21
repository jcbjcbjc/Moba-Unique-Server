/*package com.game.manager;

import com.game.entity.Character;
import com.game.models.Team;

import java.util.HashMap;
import java.util.Map;

public class TeamManager {
    private static TeamManager teamManager = new TeamManager();

    Map<Integer, Team> teams = new HashMap<>();

    public static TeamManager instance() {
        return teamManager;
    }

    public void addTeamMember(Character leader, Character member) {
        if (leader.team == null) {
            Team team = createTeam();
            team.addMember(leader);
            team.leander = leader;
            if (member != null) {
                team.addMember(member);
            }
        } else {
            if(member == null){
                return;
            }
            leader.team.addMember(member);
            member.team = leader.team;
        }
    }

    public void leave(Character member) {
        member.team.removeMember(member);
    }

    // 踢出
    public void teamExpel(Character leader, int memberId) {
        if (leader.team.leander.getId() == leader.getId()) {
            for (int i = 0; i < leader.team.members.size(); i++) {
                if(leader.team.members.get(i).getId() == memberId){
                    leave(leader.team.members.get(i));
                    return;
                }
            }
        }
    }


    Team createTeam() {
        for (Integer in : teams.keySet()) {
            if (teams.get(in).members.size() == 0) {
                return teams.get(in);
            }
        }
        Team team = new Team();
        team.id = teams.size();
        teams.put(team.id, team);
        return team;
    }


}
*/