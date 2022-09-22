using C2GNet;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Assets.scripts.Utils;
using C2BNet;

namespace GameLogic
{
    public  class Character:Entity
    {
        public NUser user;
        int userid;
        string nickname;
        int teamid;
        int cCharacterId;

        public int Userid
        {
            get { return userid; }
            set { userid = value; }
        }
        public string Nickname
        {
            get { return nickname; }
            set { nickname = value; }
        }
        public int Teamid
        {
            get { return teamid; }
            set { teamid = value; }
        }
        public int CCharacterId
        {
            get { return cCharacterId; }
            set { cCharacterId = value; }
        }
        public void update(FrameHandle fh) {
            LogUtil.log("update");
        }
    }
}
