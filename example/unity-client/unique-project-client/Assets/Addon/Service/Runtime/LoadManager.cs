using Managers;
 
using Assets.scripts.Utils;
using NetWork;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UnityEngine;
/// <summary>
/// GameLogicLoginService
/// 
/// @Author 贾超博
/// 
/// @Date 2022/4/30
/// </summary>
/// 
using Services;
namespace Assets.scripts
{
    public class LoadManager :Service
    {
        protected internal override void AfterInitailize()
        {
            base.AfterInitailize();

            DontDestroyOnLoad(this);

            NetGameClient.GetInstance().Init();
            NetBattleClient.GetInstance().Init();

            UIManager.GetInstance().ShowUIForms("UIMain");
            UIManager.GetInstance().ShowUIForms("GameOverPanel");
        }

        public bool Close() {
            try
            {
                NetGameClient.GetInstance().Close();
                NetBattleClient.GetInstance().Close();
                //LocalStorageUtil.RemoveItem(LocalStorageUtil.allFrameHandlesKey);
                return true;
            }
            catch (Exception ex) { return false; }
        }
    }
}
