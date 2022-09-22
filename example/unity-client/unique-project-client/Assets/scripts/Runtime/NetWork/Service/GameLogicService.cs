using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Services;
using Assets.scripts.Utils;
using C2BNet;
/// <summary>
/// GameLogicLoginService
/// 
/// @Author 贾超博
/// 
/// @Date 2022/4/30
/// </summary>

namespace NetWork
{
    public class GameLogicService :Service
    {
        
        EventSystem eventSystem;
        protected internal override void AfterInitailize()
        {
            base.AfterInitailize();
            eventSystem = ServiceLocator.Get<EventSystem>();
        }

        public void SendFrameHandle(FrameHandlesFromClient frameHandles)
        {
            //LogUtil.log("SendFrameHandle",frameHandle);
            var userId = ServiceLocator.Get<User>().user.Id;

            frameHandles.UserId=userId ;

            var Net = new C2BNetMessage
            {
                Request = new C2BNetMessageRequest
                {
                    UserId = userId,
                    FrameHandles = frameHandles
                }
            };

            NetBattleClient.GetInstance().SendMessage(Net);
        }


        /**
         * 发送进度转发
         */
        public void SendPercentForward(int percent)
        {
            var userId = ServiceLocator.Get<User>().user.Id;
            var Net = new C2BNetMessage
            {
                Request = new C2BNetMessageRequest
                {
                    UserId = userId,
                    PercentForward = new PercentForward
                    {
                        UserId = userId,
                        Percent = percent
                    }
                }
            };

            NetBattleClient.GetInstance().SendMessage(Net);

        }


        /**
        * 发送游戏结束
        */
        public void SendGameOver()
        {
            LogUtil.log("SendGameOver");
            var userId = ServiceLocator.Get<User>().user.Id;
            var Net = new C2BNetMessage
            {
                Request = new C2BNetMessageRequest
                {
                    UserId = userId,
                    GameOverReq = new GameOverRequest()
                }
            };

            NetBattleClient.GetInstance().SendMessage(Net);
        }

        /**
            * 发送补帧请求
            */
        public void SendRepairFrame(int startFrame, int endFrame)
        {
            // LogUtil.log("SendRepairFrame");
            var userId = ServiceLocator.Get<User>().user.Id;

            var Net = new C2BNetMessage
            {
                Request = new C2BNetMessageRequest
                {
                    UserId = userId,
                    RepairFrameReq = new RepairFrameRequest
                    {
                        StartFrame = startFrame,
                        EndFrame = endFrame
                    }
                }
            };

            NetBattleClient.GetInstance().SendMessage(Net);
        }

        /**
         * 记录用户请求
         */
        public void SendRecordUser()
        {
            var userId = ServiceLocator.Get<User>().user.Id;
            //console.log('SendRecordUser')
            var Net = new C2BNetMessage
            {
                Request = new C2BNetMessageRequest
                {
                    UserId = userId
                }
            };

            NetBattleClient.GetInstance().SendMessage(Net);

        }

    }
}
