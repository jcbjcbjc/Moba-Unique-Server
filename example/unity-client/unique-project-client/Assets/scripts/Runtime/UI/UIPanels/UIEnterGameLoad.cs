using GameLogic;
using Managers;


using NetWork;
using Assets.scripts.Utils;
using C2BNet;
using C2GNet;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using static Assets.scripts.Utils.enums.BattleModeEnum;
using MyTimer;
using Services;

namespace UI
{
    public class UIEnterGameLoad:BaseUIForm
    {
        private Metronome timer;
        private EventSystem eventSystem;

        private int percent_ = 0;   //加载进度百分比
        private bool isGoToBattleScene = false; //是否已跳转战斗场景
       
        public void Awake()
        {
            eventSystem= ServiceLocator.Get<EventSystem>();
            timer =new Metronome();
            eventSystem.AddListener(EEvent.OnEnterGameProcess, OnEnterGame);
        }
        public void Start()
        {
            
        }

        public void OnEnterGame()
        {
            this.InitTeamUser(ServiceLocator.Get<User>().room);

            //连接到战斗服务器
            var ipPortArr = ServiceLocator.Get<User>().room.IpPortStr.Split(":");
            NetConfig.UdpIp = ipPortArr[0];
            NetConfig.UdpPort = int.Parse(ipPortArr[2]);

            LogUtil.log("战斗服务器地址：" + NetConfig.UdpIp + NetConfig.UdpPort);

            NetBattleClient.GetInstance().connectToServer(NetConfig.UdpIp, NetConfig.UdpPort);
            eventSystem.AddListener<PercentForwardResponse>(EEvent.OnPercentForward, OnPercentForward);

            if (GameData.battleMode == BattleMode.Battle)
            {  //对局模式
               //上传加载进度，需要等所有用户资源都加载完成
                timer.OnComplete += (x) =>
                {
                    //console.log('uploadProgress percent_=' + this_.percent_)
                    ServiceLocator.Get<GameLogicService>().SendPercentForward(this.percent_);
                     
                     if (this.percent_ < 100)
                     {
                         this.percent_ += 20;
                     }
                };
                timer.Initialize(0.5f);
            }
            else if (GameData.battleMode == BattleMode.Live)
            {  //旁观模式
               //加载资源，只需要当前用户的资源加载完成即可
               //跳转战斗场景

               

            }
        }

        private void OnPercentForward(PercentForwardResponse response)
        {
            //LogUtil.log("OnPercentForward:{0} [{1}]", response.PercentForward ,response.AllUserLoadSucess);
            var userId = response.PercentForward.UserId;
            var percent = response.PercentForward.Percent;

            this.UpdateTeamUserPercent(userId, percent);

            //全部用户资源加载成功
            if (response.AllUserLoadSucess && !this.isGoToBattleScene)
            {
                this.isGoToBattleScene = true;

                LogUtil.log("跳转战斗场景");
                ServiceLocator.Get<GameLogicManager>().init();

                CloseUIForm();
            }
        }
        /**
        * 初始化队伍信息
        * @param roomUserList 
        * @param teamType 
        */
        private void InitTeamUser(NRoom roomUserList)
        {
            
        }

        /**
         * 更新队伍用户加载进度
         * @param userId 
         * @param percent 
         * @param teamType 
         */
        private void UpdateTeamUserPercent(int userId, int percent)
        {

            foreach (AllTeam allTeam in ServiceLocator.Get<User>().room.AllTeam) {
                foreach (RoomUser roomUser in allTeam.Team) {
                    if (roomUser.UserId == userId)
                    {
                        //this[percentName + (i + 1)].string= percent + '%';
                    }
                }
            }
        }
        protected internal override void CloseUIForm() {
            if (timer != null)
            {
                timer.Paused = true;
            }
            percent_ = 0;
            isGoToBattleScene = false;
            base.CloseUIForm();

        }
    }
}
