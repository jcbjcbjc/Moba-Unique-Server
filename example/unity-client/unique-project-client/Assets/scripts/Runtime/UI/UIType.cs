using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace UI
{
    public class UIType
    {
        //是否清空“栈集合”
        public bool IsClearStack = false;
        //UI窗体（位置）类型
        public UIFormType UIForms_Type = UIFormType.Normal;
        //UI窗体显示类型
        public UIFormShowMode UIForms_ShowMode = UIFormShowMode.Normal;
        //UI窗体透明度类型
        public UIFormLucenyType UIForm_LucencyType = UIFormLucenyType.Lucency;

    }
    /// <summary>
    /// UI窗体（位置）类型
    /// </summary>
    public enum UIFormType
    {
        //普通窗体
        Normal,
        //固定窗体                              
        Fixed,
        //弹出窗体
        PopUp
    }

    /// <summary>
    /// UI窗体的显示类型
    /// </summary>
    public enum UIFormShowMode
    {
        //普通
        Normal,
        //反向切换
        ReverseChange,
        //隐藏其他
        HideOther
    }

    /// <summary>
    /// UI窗体透明度类型
    /// </summary>
    public enum UIFormLucenyType
    {
        //完全透明，不能穿透
        Lucency,
        //半透明，不能穿透
        Translucence,
        //低透明度，不能穿透
        ImPenetrable,
        //可以穿透
        Pentrate
    }

    public class UIForm {
        public static string UIEnterGameLoad = "UIEnterGameLoad";
        public static string UIGameLoadIn = "UIGameLoadIn";
        public static string UIMatchWait = "UIMatchWait";
        public static string UIMessageBox = "UIMessageBox";
    }
}
