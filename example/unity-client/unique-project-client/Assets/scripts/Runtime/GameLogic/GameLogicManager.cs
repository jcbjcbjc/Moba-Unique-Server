
using NetWork;
using UI;
using Assets.scripts.Utils;
using Assets.scripts.Utils.enums;
using C2BNet;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UnityEngine;
using Managers;
using MyTimer;
using static Assets.scripts.Utils.enums.BattleModeEnum;
using static Assets.scripts.Utils.enums.GameStatusEnum;
using static Assets.scripts.Utils.enums.HandlerFrameResultEnum;

using static Assets.scripts.Utils.enums.OptTypeEnum;
using Services;


namespace GameLogic
{
    public class GameLogicManager:Service
    {
        EntityManager entityManager_;
        CharacterManager characterManager_;
        GameCoreLogic gameLogic_;



       
        private Metronome handleFrameTimer;
        private Metronome recordUserTimer;

        private EventSystem eventSystem;

        protected internal override void AfterInitailize()
        {
            base.AfterInitailize();
            eventSystem = ServiceLocator.Get<EventSystem>();
        }


        // public isRecProFlag:boolean = true; //是否恢复进度中

        public void Update()
        {
            updateLogic();
        }

        public void updateLogic()
        {
            if (GameData.isInGame) {
                HandlerFrameResult handlerFrameResult = HandleFrame();
                RepairFrameRequest(handlerFrameResult);
            }
        }
        public void Clear() {
            if(handleFrameTimer!=null) handleFrameTimer.Paused=true;
            if(recordUserTimer!=null) recordUserTimer.Paused = true;

            GameData.release();
           
            eventSystem.RemoveListener<FrameHandleResponse>(EEvent.OnFrameHandle, this.OnFrameHandle);
            eventSystem.RemoveListener<RepairFrameResponse>(EEvent.OnRepairFrame, this.OnRepairFrame);
            eventSystem.RemoveListener<LiveFrameResponse>(EEvent.OnLiveFrame, this.OnLiveFrame);
            eventSystem.RemoveListener<FrameHandle>(EEvent.OnAddOptClient, this.AddPlayerOpt);
        }
       
        public void init() {
            Clear();

            eventSystem.AddListener<FrameHandleResponse>(EEvent.OnFrameHandle, this.OnFrameHandle);
            eventSystem.AddListener<RepairFrameResponse>(EEvent.OnRepairFrame, this.OnRepairFrame);
            eventSystem.AddListener<LiveFrameResponse>(EEvent.OnLiveFrame, this.OnLiveFrame);
            eventSystem.AddListener<FrameHandle>(EEvent.OnAddOptClient, this.AddPlayerOpt);

            gameLogic_ = new GameCoreLogic();

            entityManager_ = new EntityManager();
            characterManager_=new CharacterManager();
            gameLogic_.Init(characterManager_, entityManager_);

            handleFrameTimer = new Metronome();
            recordUserTimer=new Metronome();

            ////////////////////////////////////////////////////////////////////////////////////////////

            UIGameLoadIn uIGameLoadIn= (UIGameLoadIn)UIManager.GetInstance().ShowUIForms("UIGameLoadIn");
            uIGameLoadIn.SetMsg("游戏拼命加载中...");
            
          
            characterManager_.CreateCharacter();

            // change the GameData
            GameData.gameStatus = GameStatus.GameIn;
            GameData.isInGame = true;


            eventSystem.Invoke(EEvent.OnBattleGameIn);

            //var allFrameHandlesStr = LocalStorageUtil.GetItem(LocalStorageUtil.allFrameHandlesKey);
            //if (allFrameHandlesStr!=null)
            //{  //恢复进度
            //    //console.log('恢复进度')

            //    //allFrameHandles = JSON.parse(allFrameHandlesStr).map || { };

            //    //    let this_=this;
            //    //    this.recProTimer=setInterval(async function(){
            //    //     await this_.IntervalProgressRecovery(this_);
            //    //   }, 0);  //2
            //}
            if (GameData.battleMode == BattleMode.Battle)
            {    //对局模式
                handleFrameTimer.OnComplete += CapturePlayerOpts;
                handleFrameTimer.Initialize(NetConfig.FrameTime/1000);
            }
            else if (GameData.battleMode == BattleMode.Live)
            {  //观看直播模式


            }

            //recordUserTimer = new TimerTask(1000, () => {
            //    if (GameData.handleFrameId >= 0)
            //    {
            //        recordUserTimer.Stop();
            //        // this_.isRecProFlag = false;
            //    }
            //    GameLogicService.GetInstance().SendRecordUser();
            //});
            
        }


        /*******************************************************************************************************
         * **                                On Message From BattleServer                                   ****
         * *****************************************************************************************************/


        /**
        * 帧操作响应
        */
        public void OnFrameHandle(FrameHandleResponse param)
        {
            //计算接收两帧之间的时间间隔
            float currentFrameTime = Time.time;
            if (GameData.lastReceiveFrameTime != 0 && currentFrameTime - GameData.lastCheckFrameTime > 3000)
            {  //每3秒抽查下
                var ms = currentFrameTime - GameData.lastReceiveFrameTime;

                //this.uiBattle.updateFrameTime(ms);

                GameData.lastCheckFrameTime = currentFrameTime;
            }
            GameData.lastReceiveFrameTime = currentFrameTime;


            var response = param;

            var frameId = response.Frame;

            GameData.newFrameId = frameId;

            if (GameData.newFrameId - 50 > GameData.handleFrameId)
            {
                //this.uiGameLoadIn.setMsg('游戏进度恢复中...');
                //this.uiGameLoadIn.show();
            }
            else
            {
                //this.uiGameLoadIn.hide();
            }

            //已经处理的帧
            if (frameId <= GameData.handleFrameId)
            {
                return;
            }
            if (!GameData.allFrameHandles.ContainsKey(frameId))
            {
                GameData.allFrameHandles.Add(frameId, response.FrameHandles);//收到帧保存起来
            }
        }

        /**
         * 直播帧响应
         * @param param 
         */
        public void OnLiveFrame(LiveFrameResponse param)
        {
            
            // let response = param[0] as LiveFrameResponse;
            var response = param;
            var liveFrames = response.LiveFrames;
            for (int i = 0; i < liveFrames.Count; i++)
            {
                var liveFrame = liveFrames[i];
                if (!GameData.allFrameHandles.ContainsKey(liveFrame.Frame)) {
                    GameData.allFrameHandles.Add(liveFrame.Frame, liveFrame.FrameHandles);
                }
                    
            }
            // this.liveNotExecuteFrameCount += liveFrames.length;
        }

        private void OnRepairFrame(RepairFrameResponse response)  {
            // console.log("OnRepairFrame:{0}", JSON.stringify(response.repairFrames));
            foreach (RepairFrame repairFrame in response.RepairFrames) {
                if (!GameData.allFrameHandles.ContainsKey(repairFrame.Frame)) {
                    GameData.allFrameHandles.Add(repairFrame.Frame, repairFrame.FrameHandles);
                }
            }
        }



        /*******************************************************************************************************
         * **                               Handle Frame                                                    ****
         * *****************************************************************************************************/



        private HandlerFrameResult HandleFrame()
        {
            var frameId = GameData.handleFrameId + 1;

            IList<FrameHandle> frameHandles;
            //获取帧操作集合
            GameData.allFrameHandles.TryGetValue(frameId, out frameHandles);

            if (frameHandles==null)
            {  //无帧数据
                return HandlerFrameResult.NoFrameData;
            }
            if (GameData.executeFrameId >= frameId)
            {
                //LogUtil.log("不能重复执行，已经执行的帧:" + this.executeFrameId);
                return HandlerFrameResult.NotRepeatFrame;
            }
            //update executeFrameId
            GameData.executeFrameId = frameId;



            //update
            gameLogic_.update(frameHandles);




            //update handleFrameId
            GameData.handleFrameId = frameId; 

            //store data
            if (frameId % 15 == 0)
            {
                //LocalStorageUtil.SetItem(LocalStorageUtil.allFrameHandlesKey, JSON.stringify(allFrameHandles));
            }
            return HandlerFrameResult.Success;
         }

        /**
         * 补帧效验
         * @param handlerFrameResult 
         * @return  是否补帧了
         */
        private void RepairFrameRequest(HandlerFrameResult handlerFrameResult) {
        if(handlerFrameResult == HandlerFrameResult.NoFrameData){
            if(GameData.currentRepairFrame <= 0){
            //补帧请求
            var start = GameData.handleFrameId + 1;
                    var end = this.GetEndFrameId(start);
            if ((end- start)<10)
            {
                return ;
            }
            //console.log('补帧请求 start=' + start + '，' + end + '，handleFrameId=' + this.handleFrameId)
                ServiceLocator.Get<GameLogicService>().SendRepairFrame(start, end);
                    GameData.currentRepairFrame = GameData.repairWaitFrameCount;
        }else{
                    GameData.currentRepairFrame--;
        }
            return;
        }
            GameData.currentRepairFrame =0;
            return;
        }
        /**
        * 获取补帧结束帧
        * @param startFrameId 起始帧
        */
        private int GetEndFrameId(int startFrameId)
        {
            if (GameData.allFrameHandles.Count != 0) {
                return GameData.allFrameHandles.Keys.Max();
            }
            return 0;
        }



        /*******************************************************************************************************
         * **                                collect and send player operation                              ****
         * *****************************************************************************************************/


        public void CapturePlayerOpts(float p){
            //无操作
            if(GameData.frameHandles.FrameHandles.Count==0){
               return;
            }

            GameData.frameHandles.UserId = ServiceLocator.Get<User>().user.Id ;
            // LogUtil.log(this.frameHandle);
            //发送操作

            ServiceLocator.Get<GameLogicService>().SendFrameHandle(GameData.frameHandles);

            GameData.frameHandles =new FrameHandlesFromClient();
        }

        public void AddPlayerOpt(FrameHandle frameHandle)
        {
            frameHandle.UserId = ServiceLocator.Get<User>().user.Id;
            frameHandle.OpretionId= GameData.NextOperationId++;
            GameData.PredictedInput.Add(frameHandle);
            GameData.frameHandles.FrameHandles.Add(frameHandle);
        }
    }
}
