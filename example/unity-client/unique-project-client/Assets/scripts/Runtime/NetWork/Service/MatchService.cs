using GameLogic;
using Managers;



using UI;
using Assets.scripts.Utils;
using C2GNet;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using static Assets.scripts.Utils.enums.BattleModeEnum;
using Services;

namespace NetWork
{
    public class MatchService :Service
    {

        EventSystem eventSystem;
        protected internal override void AfterInitailize()
        {
            base.AfterInitailize();
            eventSystem = ServiceLocator.Get<EventSystem>();
            eventSystem.AddListener<StartMatchResponse>(EEvent.OnStartMatch, this.OnStartMatch);
            eventSystem.AddListener<MatchResponse>(EEvent.OnMatchResponse, this.OnMatchResponse);
        }

        BaseUIForm uiMatchWait = null;
       

        /**
     * 开始匹配请求
     */
        public void SendStartMatch()
        {
            LogUtil.log("SendStartMatch");
            var Net = new C2GNetMessage
            {
                Request = new NetMessageRequest
                {
                    StartMatchReq = new StartMatchRequest(),
                }
            };

            NetGameClient.GetInstance().SendMessage(Net);

        }


        /** 
         * 开始匹配响应
         */
        private /*async*/ void OnStartMatch(StartMatchResponse response)
        {   
            LogUtil.log("OnStartMatch:{0}", response.Result,response.Errormsg);
            if (response.Result == Result.Success)
            {
                uiMatchWait = UIManager.GetInstance().ShowUIForms("UIMatchWait") as UIMatchWait;  //打开匹配弹窗
            }
            else
            {
                TipsManager.Instance.showTips(response.Errormsg);
            }
        }

        /**
         * 匹配响应
         */
        public void OnMatchResponse(MatchResponse response)
        {
            LogUtil.log("OnMatchResponse:{0}", response.Result, response.Errormsg);
            TipsManager.Instance.showTips(response.Errormsg);
            if (this.uiMatchWait)
            {   //关闭匹配弹窗 
                this.uiMatchWait.CloseUIForm();
                this.uiMatchWait = null;
            }
            if (response.Result == Result.Success)
            {  //匹配成功
               // LocalStorageUtil.RemoveItem(LocalStorageUtil.allFrameHandlesKey);  //清除上一次的帧操作
                GameData.battleMode = BattleMode.Battle;  //设置为对局模式

                ServiceLocator.Get<User>().room = response.Room;
                RandomUtil.seed = response.Room.RandomSeed;   //设置战斗随机数种子
                UIManager.GetInstance().ShowUIForms("UIEnterGameLoad");                                            //director.loadScene('EnterGameLoad');
                eventSystem.Invoke(EEvent.OnEnterGameProcess);                                              //SoundManager.Instance.PlayMusic(SoundDefine.Music_Select);
            }
        }
    }
}
