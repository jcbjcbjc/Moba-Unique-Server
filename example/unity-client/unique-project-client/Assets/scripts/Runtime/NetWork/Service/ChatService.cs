using Managers;



using C2GNet;
using Services;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
/// <summary>
/// ChatService
/// 
/// @Author 贾超博
/// 
/// @Date 2022/4/30
/// </summary>
/// 

namespace NetWork
{
    public class ChatService:Service
    {
        EventSystem eventSystem;
        protected internal override void AfterInitailize()
        {
            base.AfterInitailize();
            eventSystem = ServiceLocator.Get<EventSystem>();
            eventSystem.AddListener<ChatResponse>(EEvent.OnChat, this.OnChat);
        }
        /**
     * 发送聊天
     */
        public void SendChat(ChatChannel chatChannel, string msg, ChatRoomType chatRoomType, int toId, string toName, int toLevel, int toCCharacterId) {
            //LogUtil.log("SendChat");
            var Net = new C2GNetMessage
            {
                Request = new NetMessageRequest
                {
                    ChatReq = new ChatRequest {
                        ChatMessage = new ChatMessage {
                            ChatChannel = chatChannel,
                            FromId = ServiceLocator.Get<User>().user.Id,
                           
                            ToId = toId,
                            ToName = toName,
                            ToLevel = toLevel,
                            ToCCharacterId = toCCharacterId,
                            FromName = ServiceLocator.Get<User>().user.Nickname,
                            //FromLevel = User.Instance.user.Character.Level,
                            //FromCCharacterId = User.Instance.user.Character.Cid,
                            ChatRoomType = chatRoomType,
                            Msg = msg,

                        }
                    }
                }
            };

            NetGameClient.GetInstance().SendMessage(Net);
        }


        /** 
         * 聊天响应
         */
        private void OnChat(object param) {
            var response = param as ChatResponse;
            //LogUtil.log("OnChat:{0}", response.result,response.errormsg);
            if (response.Result == Result.Success) {
                //ChatManager.Instance.AddMessages(ChatChannel.Comp, response.CompMessagesList);
                //ChatManager.Instance.AddMessages(ChatChannel.Private, response.PrivateMessagesList);
                //ChatManager.Instance.AddMessages(ChatChannel.RoomChat, response.RoomMessagesList);
            } else
            {
                TipsManager.Instance.showTips(response.Errormsg);
            }
        }

    }
}
