
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
using GameLogic;
using Services;

namespace NetWork
{
    public class RoomService : Service
    {
        EventSystem eventSystem;

        protected internal override void AfterInitailize() {
            base.AfterInitailize();
            eventSystem =ServiceLocator.Get<EventSystem>();
            eventSystem.AddListener<MyRoomResponse>(EEvent.OnMyRoom, this.OnMyRoom);
            eventSystem.AddListener<InviteResponse>(EEvent.OnInviteResponse, this.OnInviteResponse);
            eventSystem.AddListener<InviteRequest>(EEvent.OnInviteRequest, this.OnInviteRequest);
            eventSystem.AddListener<KickOutResponse>(EEvent.OnKickOut, this.OnKickOut);
            eventSystem.AddListener<RoomStartGameResponse>(EEvent.OnRoomStartGame, this.OnRoomStartGame);
            eventSystem.AddListener<NickNameSearchResponse>(EEvent.OnNickNameSearch, this.OnNickNameSearch);
            eventSystem.AddListener<AddRoomRequest>(EEvent.OnAddRoomRequest, this.OnAddRoomRequest);
            eventSystem.AddListener<AddRoomResponse>(EEvent.OnAddRoomResponse, this.OnAddRoomResponse);
            eventSystem.AddListener<OutRoomResponse>(EEvent.OnOutRoom, this.OnOutRoom);
            eventSystem.AddListener<AddLiveResponse>(EEvent.OnAddLiveResponse, this.OnAddLiveResponse);
            eventSystem.AddListener<ValidateOpenRoomResponse>(EEvent.OnValidateOpenRoom, this.OnValidateOpenRoom);
            eventSystem.AddListener(EEvent.OnGameOver2,this.OnGameOver2);
        }
        /**
        * 请求我的房间
        */
        public void SendMyRoom()
        {
            LogUtil.log("SendMyRoom");
            var Net = new C2GNetMessage
            {
                Request = new NetMessageRequest
                {
                    MyRoomReq = new MyRoomRequest(),
                }
            };

            NetGameClient.GetInstance().SendMessage(Net);
        }

        /**
     * 我的房间响应
     */
        private void OnMyRoom(MyRoomResponse response)
        {
            LogUtil.log("OnMyRoom:{0}", response.Room);
            eventSystem.Invoke(EEvent.OnMyRoom_UI, response.Room);
            
        }

        /**
     * 发送邀请请求
     */
        public void SendInviteRequest(int toUserId, string toNickName, int teamId)
        {
            LogUtil.log("SendInviteRequest");
            var Net = new C2GNetMessage
            {
                Request = new NetMessageRequest
                {
                    InviteReq = new InviteRequest
                    {
                        FromUserId = ServiceLocator.Get<User>().user.Id,
                        FromNickName = ServiceLocator.Get<User>().user.Nickname,
                        ToUserId = toUserId,
                        ToNickName = toNickName,
                        TeamId = teamId

                    }

                }
            };

            NetGameClient.GetInstance().SendMessage(Net);

        }

        /**
         * 发送邀请响应
         */
        public void SendInviteResponse(bool accept, InviteRequest inviteRequest)
        {
            var Net = new C2GNetMessage
            {
                Request = new NetMessageRequest
                {
                    InviteRes = new InviteResponse
                    {


                        Resultmsg = accept ? Result.Success : Result.Failed,
                        Errormsg = "",
                        InviteRequest = inviteRequest
                    }
                }
            };

            NetGameClient.GetInstance().SendMessage(Net);

        }
        /**
    * 收到邀请请求
    */
        private async void OnInviteRequest(InviteRequest request)
        {
           
            LogUtil.log("OnInviteRequest", request);
            var confirmObj = await TipsManager.Instance.Show(request.FromNickName + "邀请你加入房间？", "邀请请求", MessageBoxType.Confirm, "接受", "拒绝");
            var this_ = this;
            eventSystem.AddListener(EEvent.UIMessageBox_OnClickYes, () => { SendInviteResponse(true, request); });
            eventSystem.AddListener(EEvent.UIMessageBox_OnClickNo, () =>
            {
                this_.SendInviteResponse(false, request);
            });
        }

        /**
         * 收到邀请响应
         */
        private void OnInviteResponse(InviteResponse response)
        {
         
            LogUtil.log("OnInviteResponse:{0}{1}", response.Resultmsg,response.Errormsg);
            
            TipsManager.Instance.showTips(response.Errormsg);
            
            if (response.Resultmsg == Result.Success)
            {
                //被邀请者是当前用户
                if (response.InviteRequest.ToUserId == ServiceLocator.Get<User>().user.Id)
                {
                    /**************************
                   director.loadScene('Room');
                    */
                }
                else
                {
                    eventSystem.Invoke(EEvent.OnMyRoom_RefieshUI);
                }
            }
        }

        /**
     * 踢出响应
     */
        private void OnKickOut(KickOutResponse response)
        {
            
            LogUtil.log("OnKickOut:{0}{1}", response.Result,response.Errormsg);
            eventSystem.Invoke(EEvent.OnKickOut_UI, response);
            eventSystem.Invoke(EEvent.OnMyRoom_RefieshUI);
            
            TipsManager.Instance.showTips(response.Errormsg);
        }

        /**
         * 开始游戏请求
         */
        public void SendRoomStartGame()
        {
            LogUtil.log("SendRoomStartGame");
            var Net = new C2GNetMessage
            {
                Request = new NetMessageRequest
                {
                    RoomStartGameReq = new RoomStartGameRequest(),
                }
            };

            NetGameClient.GetInstance().SendMessage(Net);


        }

        /**
         * 开始游戏响应
         */
        public void OnRoomStartGame(RoomStartGameResponse response)
        {
            LogUtil.log("OnRoomStartGame{0}{1}：", response.Result, response.Errormsg);
            eventSystem.Invoke(EEvent.OnRoomStartGame_UI, response);
            TipsManager.Instance.showTips(response.Errormsg);
        }

        /**
         * 昵称搜索请求
         */
        public void SendNickNameSearch(string nickName)
        {
            var Net = new C2GNetMessage
            {
                Request = new NetMessageRequest
                {
                    NickNameSearchReq = new NickNameSearchRequest
                    {
                        NickName = nickName
                    },
                }
            };

            NetGameClient.GetInstance().SendMessage(Net);

        }

        /**
        * 昵称搜索响应
*/
        public void OnNickNameSearch(object param)
        {
            var response = param as NickNameSearchResponse;
            LogUtil.log("OnNickNameSearch");
            eventSystem.Invoke(EEvent.OnNickNameSearch_UI, response.RoomUser);
        }

        /**
         * 发送加入房间请求
         */
        public void SendAddRoomRequest(int roomId)
        {
            var Net = new C2GNetMessage
            {
                Request = new NetMessageRequest
                {
                    AddRoomReq = new AddRoomRequest
                    {
                        RoomId = roomId,
                        FromUserId = ServiceLocator.Get<User>().user.Id,
                        FromNickName = ServiceLocator.Get<User>().user.Nickname

                    },
                }
            };

            NetGameClient.GetInstance().SendMessage(Net);

        }

        /**
         * 发送加入房间响应
         */
        public void SendAddRoomResponse(bool accept, int teamId, AddRoomRequest addRoomRequest)
        {
            var Net = new C2GNetMessage
            {
                Request = new NetMessageRequest
                {
                    AddRoomRes = new AddRoomResponse
                    {
                        Result = accept ? Result.Success : Result.Failed,
                        Errormsg = "",
                        TeamId = teamId,
                        AddRoomRequest = addRoomRequest
                    },
                }
            };

            NetGameClient.GetInstance().SendMessage(Net);
            LogUtil.log("SendAddRoomResponse");

        }

        /**
         * 收到加入房间请求
         */
        private async void OnAddRoomRequest(object param)
        {
            var request = param as AddRoomRequest;
            LogUtil.log("OnAddRoomRequest", request);
            var confirmObj = await TipsManager.Instance.Show(request.FromNickName + "加入房间？", "加入房间", MessageBoxType.Confirm, "接受", "拒绝");
            var this_ = this;
            eventSystem.AddListener(EEvent.UIMessageBox_OnClickYes, async () =>
            {
                var teamConfirmObj = await TipsManager.Instance.Show("请选择" + request.FromNickName + "加入队伍！", "选择队伍", MessageBoxType.Confirm, "友队", "敌队");
                eventSystem.AddListener(EEvent.UIMessageBox_OnClickYes, () =>
                {
                    this_.SendAddRoomResponse(true, 0, request);
                });
                eventSystem.AddListener(EEvent.UIMessageBox_OnClickNo, () =>
                {
                    this_.SendAddRoomResponse(true, 1, request);
                });
            });
            eventSystem.AddListener(EEvent.UIMessageBox_OnClickNo, /*async */() =>
            {
                this_.SendAddRoomResponse(false, 0, request);
            });
        }


        /**
         * 收到加入房间响应
         */
        private void OnAddRoomResponse(object param)
        {
            var response = param as AddRoomResponse;
            LogUtil.log("OnAddRoomResponse:{0}{1}", response.Result, response.Errormsg);
            TipsManager.Instance.showTips(response.Errormsg);
            if (response.Result == Result.Success)
            {
                //加入者是当前用户
                if (response.AddRoomRequest.FromUserId == ServiceLocator.Get<User>().user.Id)
                {
                    /*******************************
                    //director.loadScene('Room');
                    ******************************************/
                }
                else
                {
                    eventSystem.Invoke(EEvent.OnMyRoom_RefieshUI);
                }
            }
        }

        /**
         * 退出房间请求
         */
        public void SendOutRoom()
        {
            var Net = new C2GNetMessage
            {
                Request = new NetMessageRequest
                {
                    OutRoomReq = new OutRoomRequest(),
                }
            };

            NetGameClient.GetInstance().SendMessage(Net);
            LogUtil.log("SendOutRoom");

        }

        /**
         * 退出房间响应
         */
        public void OnOutRoom(object param)
        {
            var response = param as OutRoomResponse;
            LogUtil.log("OnOutRoom{0}{1}：", response.Result, response.Errormsg);
            TipsManager.Instance.showTips(response.Errormsg);
        }

        /**
         * 请求游戏结束
         */
        public void SendGameOver2()
        {
            var Net = new C2GNetMessage
            {
                Request = new NetMessageRequest
                {
                    GameOver2Req = new GameOver2Request
                    {
                        IpPortStr = ServiceLocator.Get<User>().room.IpPortStr
                    },
                }
            };

            NetGameClient.GetInstance().SendMessage(Net);
            LogUtil.log("SendGameOver2");
        }

        public void OnGameOver2() {
            NetBattleClient.GetInstance().Close();
            LogUtil.log("GameOver2");
        }

        /**
         * 上传比分请求
         */
        public void SendUploadBiFen(string biFen)
        {
            LogUtil.log("SendUploadBiFen");
            var Net = new C2GNetMessage
            {
                Request = new NetMessageRequest
                {
                    UploadBiFenReq = new UploadBiFenRequest
                    {
                        BiFen = biFen
                    },
                }
            };

            NetGameClient.GetInstance().SendMessage(Net);
        }

        /**
         * 进入直播请求
         */
        public void SendAddLive(int targetUserId)
        {
            LogUtil.log("SendAddLive");
            GameLogicGlobal.targetLiveUserId = targetUserId;
            var Net = new C2GNetMessage
            {
                Request = new NetMessageRequest
                {
                    AddLiveReq = new AddLiveRequest
                    {
                        UserId = targetUserId
                    },
                }
            };

            NetGameClient.GetInstance().SendMessage(Net);
        }

        /**
         * 收到进入直播响应
         */
        private void OnAddLiveResponse(object param)
        {
            var response = param as AddLiveResponse;
            LogUtil.log("OnAddLiveResponse:{0}{1}", response.Result, response.Errormsg);
            TipsManager.Instance.showTips(response.Errormsg);
            if (response.Result == Result.Success)
            {  //进入直播房间
                //LocalStorageUtil.RemoveItem(LocalStorageUtil.allFrameHandlesKey);
                GameData.battleMode = BattleMode.Live;

                ServiceLocator.Get<User>().room = response.Room;
                RandomUtil.seed = response.Room.RandomSeed;   //设置战斗随机数种子

                //director.loadScene('EnterGameLoad');
            }
        }

        /**
         * 请求效验是否可以开房间
         */
        public void SendValidateOpenRoom()
        {
            LogUtil.log("SendValidateOpenRoom");
            var Net = new C2GNetMessage
            {
                Request = new NetMessageRequest
                {
                    ValidateOpenRoomReq = new ValidateOpenRoomRequest(),
                }
            };

            NetGameClient.GetInstance().SendMessage(Net);

        }

        /**
         * 效验是否可以开房间响应
         */
        private void OnValidateOpenRoom(object param)
        {
            var response = param as ValidateOpenRoomResponse;
            LogUtil.log("OnValidateOpenRoom:{0}", response);
            if (response.Result == Result.Success)
            {
                //director.loadScene('Room');
            }
            else
            {
                TipsManager.Instance.showTips(response.Errormsg);
            }
        }

    }
}
