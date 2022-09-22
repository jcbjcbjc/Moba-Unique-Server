using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using NetWork;

using C2BNet;
using C2GNet;
using System;
using Services;
using Assets.scripts.Utils;

public class MessageDispatcher : Service
{
    EventSystem eventSystem;
    public static List<NetMessage> msgList = new List<NetMessage>();


    protected internal override void AfterInitailize()
    {
        base.AfterInitailize();
        eventSystem = ServiceLocator.Get<EventSystem>();
    }

    // Update is called once per frame
    void Update()
    {
        //每帧处理消息
        for (int i = 0; i < NetConfig.MessageDispatchSpeed; i++)
        {
            if (msgList.Count > 0)
            {
                Dispatch(msgList[0]);

                lock (msgList)
                {
                    msgList.RemoveAt(0);
                }
            }
            else
            {
                break;
            }
        }
    }
    private bool Dispatch(NetMessage msg)
    {
        try
        {
            switch (msg.Net_TypeBorG)
            {
                case NetTypeBorG.FromB:
                    C2BNetMessageResponse message = msg.C2B_NetMessage.Response;

                    //战斗服
                    
                    if (message.RepairFrameRes != null)
                    {
                        eventSystem.Invoke(EEvent.OnRepairFrame, message.RepairFrameRes);
                    }
                    if (message.FrameHandleRes != null)
                    {
                        
                        eventSystem.Invoke(EEvent.OnFrameHandle, message.FrameHandleRes);
                    }
                    if (message.PercentForwardRes != null)
                    {

                        eventSystem.Invoke(EEvent.OnPercentForward, message.PercentForwardRes);

                    }
                    if (message.LiveFrameRes != null)
                    {

                        eventSystem.Invoke(EEvent.OnLiveFrame, message.LiveFrameRes);

                    }
                    break;
                case NetTypeBorG.FromG:
                    NetMessageResponse messag = msg.C2G_NetMessage.Response;
                    //大厅服
                    if (messag.UserRegister != null)
                    {
                        eventSystem.Invoke(EEvent.OnUserRegister, messag.UserRegister);
                    }
                    if (messag.UserLogin != null)
                    {
                        eventSystem.Invoke(EEvent.OnUserLogin, messag.UserLogin);
                    }
                    if (messag.UnLockRes != null)
                    {
                        eventSystem.Invoke(EEvent.OnUnLock, messag.UnLockRes);
                    }
                    if (messag.StatusNotify != null)
                    {
                        eventSystem.Invoke(EEvent.OnStatusNotify, messag.StatusNotify);
                    }
                    if (messag.CharacterDetail != null)
                    {
                        eventSystem.Invoke(EEvent.OnCharacterDetail, messag.CharacterDetail);
                    }
                    if (messag.SwitchCharacterRes != null)
                    {
                        eventSystem.Invoke(EEvent.OnSwitchCharacter, messag.SwitchCharacterRes);
                    }
                    if (messag.GameOver2Res != null) {
                        eventSystem.Invoke(EEvent.OnGameOver2);
                    }
                    //if (messag.AttrPromote != null)
                    //{
                    //    MessageCenter.dispatch(MessageType.OnAttrPromoteInfo, message.attrPromote);
                    //}
                    //if (messag.BagHandleRes != null)
                    //{
                    //    MessageCenter.dispatch(MessageType.OnBagHandle, message.bagHandleRes);
                    //}
                    //if (messag.ItemBuy != null)
                    //{
                    //    MessageCenter.dispatch(MessageType.OnItemBuy, message.itemBuy);
                    //}
                    //if (messag.CombatPowerRanking != null)
                    //{
                    //    MessageCenter.dispatch(MessageType.OnCombatPowerRanking, message.combatPowerRanking);
                    //}
                    if (messag.FollowRes != null)
                    {
                        eventSystem.Invoke(EEvent.OnFollowRes, messag.FollowRes);
                    }
                    if (messag.UserStatusChangeRes != null)
                    {
                        eventSystem.Invoke(EEvent.OnUserStatusChange, messag.UserStatusChangeRes);
                    }
                    if (messag.HeartBeatRes != null)
                    {
                        eventSystem.Invoke(EEvent.OnHeartBeat, messag.HeartBeatRes);
                    }
                    if (messag.TipsRes != null)
                    {
                        eventSystem.Invoke(EEvent.OnTips, messag.TipsRes);
                    }
                    if (messag.MyRoomRes != null)
                    {
                        eventSystem.Invoke(EEvent.OnMyRoom, messag.MyRoomRes);
                    }
                    if (messag.InviteRes != null)
                    {
                        eventSystem.Invoke(EEvent.OnInviteResponse, messag.InviteRes);
                    }
                    if (messag.InviteReq != null)
                    {
                        eventSystem.Invoke(EEvent.OnInviteRequest, messag.InviteReq);
                    }
                    if (messag.KickOutRes != null)
                    {
                        eventSystem.Invoke(EEvent.OnKickOut, messag.KickOutRes);
                    }
                    if (messag.RoomStartGameRes != null)
                    {
                        eventSystem.Invoke(EEvent.OnRoomStartGame, messag.RoomStartGameRes);
                    }
                    if (messag.NickNameSearchRes != null)
                    {
                        eventSystem.Invoke(EEvent.OnNickNameSearch, messag.NickNameSearchRes);
                    }
                    if (messag.AddRoomReq != null)
                    {
                        eventSystem.Invoke(EEvent.OnAddRoomRequest, messag.AddRoomReq);
                    }
                    if (messag.AddRoomRes != null)
                    {
                        eventSystem.Invoke(EEvent.OnAddRoomResponse, messag.AddRoomRes);
                    }
                    if (messag.OutRoomRes != null)
                    {
                        eventSystem.Invoke(EEvent.OnOutRoom, messag.OutRoomRes);
                    }
                    if (messag.FollowListRes != null)
                    {
                        eventSystem.Invoke(EEvent.OnFollowList, messag.FollowListRes);
                    }
                    if (messag.ChatRes != null)
                    {
                        eventSystem.Invoke(EEvent.OnChat, messag.ChatRes);
                    }
                    if (messag.UserStatusQueryRes != null)
                    {
                        eventSystem.Invoke(EEvent.OnUserStatusQuery, messag.UserStatusQueryRes);
                    }
                    if (messag.StartMatchRes != null)
                    {
                        eventSystem.Invoke(EEvent.OnStartMatch, messag.StartMatchRes);
                    }
                    if (messag.MatchRes != null)
                    {
                        eventSystem.Invoke(EEvent.OnMatchResponse, messag.MatchRes);
                    }
                    if (messag.AddLiveRes != null)
                    {
                        eventSystem.Invoke(EEvent.OnAddLiveResponse, messag.AddLiveRes);
                    }
                    if (messag.ValidateOpenRoomRes != null)
                    {
                        eventSystem.Invoke(EEvent.OnValidateOpenRoom, messag.ValidateOpenRoomRes);
                    }
                    break;
            }
        }
        catch (Exception ex)
        {

            Debug.Log(ex.Message);

        }
        return true;
    }
    public static bool AddTask(NetMessage msg)
    {
        lock (msgList)
        {
            msgList.Add(msg);
        }
        return true;
    }


    public enum NetTypeBorG
    {
        FromB = 0,
        FromG
    }
    public class NetMessage
    {
        NetTypeBorG netTypeBorG;
        C2GNetMessage c2GNetMessage;
        C2BNetMessage c2BNetMessage;
        public NetTypeBorG Net_TypeBorG
        {
            get { return netTypeBorG; }
            set { netTypeBorG = value; }
        }
        public C2GNetMessage C2G_NetMessage
        {
            get { return c2GNetMessage; }
            set { c2GNetMessage = value; }
        }
        public C2BNetMessage C2B_NetMessage
        {
            get { return c2BNetMessage; }
            set { c2BNetMessage = value; }
        }

        public NetMessage(C2GNetMessage _c2GNetMessage)
        {
            this.netTypeBorG = NetTypeBorG.FromG;
            this.c2GNetMessage = _c2GNetMessage;
        }
        public NetMessage(C2BNetMessage _c2BNetMessage)
        {
            this.netTypeBorG = NetTypeBorG.FromB;
            this.c2BNetMessage = _c2BNetMessage;
        }
    }
}
