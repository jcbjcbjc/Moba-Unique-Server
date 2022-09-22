using Managers;
using Assets.scripts.Utils.Tools;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UnityEngine;
namespace UI
{
    
    public abstract class BaseUIForm : MonoBehaviour
    {
        /*字段*/
        private UIType _CurrentUIType = new UIType();

        /* 属性*/
        //当前UI窗体类型
        public UIType CurrentUIType
        {
            get { return _CurrentUIType; }
            set { _CurrentUIType = value; }
        }


        #region  窗体的四种(生命周期)状态

        /// <summary>
        /// 显示状态
        /// </summary>
        public virtual void Display()
        {
            gameObject.SetActive(true);
            //设置模态窗体调用(必须是弹出窗体)
            if (_CurrentUIType.UIForms_Type == UIFormType.PopUp)
            {
                UIMaskMgr.GetInstance().SetMaskWindow(gameObject, _CurrentUIType.UIForm_LucencyType);
            }
        }

        /// <summary>
        /// 隐藏状态
        /// </summary>
        public virtual void Hiding()
        {
            gameObject.SetActive(false);
            //取消模态窗体调用
            if (_CurrentUIType.UIForms_Type == UIFormType.PopUp)
            {
                UIMaskMgr.GetInstance().CancelMaskWindow();
            }
        }

        /// <summary>
        /// 重新显示状态
        /// </summary>
        public virtual void Redisplay()
        {
            gameObject.SetActive(true);
            //设置模态窗体调用(必须是弹出窗体)
            if (_CurrentUIType.UIForms_Type == UIFormType.PopUp)
            {
                UIMaskMgr.GetInstance().SetMaskWindow(gameObject, _CurrentUIType.UIForm_LucencyType);
            }
        }

        /// <summary>
        /// 冻结状态
        /// </summary>
        public virtual void Freeze()
        {
            gameObject.SetActive(true);
        }


        #endregion

        #region 封装子类常用的方法

        /// <summary>
        /// 注册按钮事件
        /// </summary>
        /// <param name="buttonName">按钮节点名称</param>
        /// <param name="delHandle">委托：需要注册的方法</param>
        protected void RigisterButtonObjectEvent(string buttonName, EventTriggerListener.VoidDelegate delHandle)
        {
            GameObject goButton = UnityHelper.FindTheChildNode(this.gameObject, buttonName).gameObject;
            //给按钮注册事件方法
            if (goButton != null)
            {
                EventTriggerListener.Get(goButton).onClick = delHandle;
            }
        }

        /// <summary>
        /// 打开UI窗体
        /// </summary>
        /// <param name="uiFormName"></param>
        protected BaseUIForm OpenUIForm(string uiFormName)
        {
            return UIManager.GetInstance().ShowUIForms(uiFormName);
        }

        /// <summary>
        /// 关闭当前UI窗体
        /// </summary>
        protected internal virtual void CloseUIForm()
        {
            string strUIFromName = string.Empty;            //处理后的UIFrom 名称
            int intPosition = -1;

            strUIFromName = GetType().ToString();             //命名空间+类名
            intPosition = strUIFromName.IndexOf('.');
            if (intPosition != -1)
            {
                //剪切字符串中“.”之间的部分
                strUIFromName = strUIFromName.Substring(intPosition + 1);
            }
            
            UIManager.GetInstance().CloseUIForms(strUIFromName);
        }

        /// <summary>
        /// 发送消息
        /// </summary>
        /// <param name="msgType">消息的类型</param>
        /// <param name="msgName">消息名称</param>
        /// <param name="msgContent">消息内容</param>

        /// <summary>
        /// 显示语言
        /// </summary>
        /// <param name="id"></param>
        public string ShowLauguage(string id)
        {
            string strResult = string.Empty;

            strResult = LauguageMgr.GetInstance().ShowText(id);
            return strResult;
        }

        #endregion

    }
    
}
