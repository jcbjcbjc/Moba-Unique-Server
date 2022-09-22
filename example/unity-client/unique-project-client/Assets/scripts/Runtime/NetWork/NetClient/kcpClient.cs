using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net.Sockets;
//using System.Net;
//using UnityEngine;
//using C2BNet;
//using cocosocket4unity;
//using static MessageDispatcher;
//using System.Threading;
//using Assets.scripts.Utils;
//using MyTimer;
//using Google.Protobuf;
//using Services;
///// <summary>
///// GameLogicLoginService
///// 
///// @Author 贾超博
///// 
///// @Date 2022/4/30
///// </summary>
//namespace NetWork
//{
//    public class NetBattleClient:KcpClient
//    {
//        private EventSystem eventSystem=  ServiceLocator.Get<EventSystem>();

//        private static NetBattleClient _instance = new NetBattleClient();


//        private NetBattleClient()
//        {
//        }


//        public static NetBattleClient GetInstance()
//        {
//            return _instance;
//        }


//        Metronome timerTask1;
//        Metronome timerTask2;

//        protected override void HandleReceive(ByteBuf buf)
//        {
//            int length = buf.ReadableBytes();
            
//            C2BNetMessage msg = C2BNetMessage.Parser.ParseFrom(buf.GetRaw(),0, length);

//            LogUtil.log(msg.ToString());

//            MessageDispatcher.AddTask(new NetMessage( msg));
//        }
//        /// <summary>
//        /// 异常
//        /// </summary>
//        /// <param name="ex"></param>
//        protected override void HandleException(Exception ex)
//        {
//            base.HandleException(ex);
//        }
//        /// <summary>
//        /// 超时
//        /// </summary>
//        protected override void HandleTimeout()
//        {
//            StopHeartBeat();
//            base.HandleTimeout();
//        }
//        public void connectToServer(string ip,int port) {
//            KcpClient client = _instance; /*new NetBattleClient();*/
//            client.NoDelay(1, 10, 2, 1);//fast
//            client.WndSize(64, 64);
//            client.Timeout(10 * 1000);
//            client.SetMtu(512);
//            client.SetMinRto(10);
//            client.SetConv(121106);
            
//            client.Connect(ip, port);
//            client.Start();

//            StartHeartBeat();
//        }

//        public void Init()
//        {
//            timerTask1 = new Metronome();
//            timerTask2 = new Metronome();
//        }

//        private void StartHeartBeat()
//        {
//            timerTask1.OnComplete += (p) => {ServiceLocator.Get<UserService>().SendBattleHeartBeat(); };
//            timerTask1.Initialize(1);
             
//        }
//        private void StopHeartBeat()
//        {
//            if (timerTask1 != null)
//            {
//                timerTask1.Paused=true;
//            }

//        }

//        public int SendMessage(C2BNetMessage msg)
//        {
//            try
//            {
//                byte[] buffer = msg.ToByteArray();
               
//                ByteBuf bb = new ByteBuf(buffer);
//                this.Send(bb);
//            }
//            catch (Exception ex)
//            {
//                Debug.Log( ex.Message);
                
//            }
//            return -1;
//        }

//        public void Close() {
//            try
//            {
                
//                StopHeartBeat();
//            }
//            catch (Exception ex)
//            {
//                Debug.Log("无法关闭连接：" + ex.Message);

//            }
//        }

//        //private void reconnect(int time) {
//        //    timerTask2 = new TimerTask(1000, () => {
//        //        _instance = new NetBattleClient();
//        //        connect();
//        //    });
//        //}
//    }
//}
