using Assets.scripts.Utils.Tools;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UnityEngine;
using UnityEngine.UI;

namespace UI
{
    public class UIMaskMgr : MonoBehaviour
    {
        /*  字段 */
        //本脚本私有单例
        private static UIMaskMgr _Instance = null;
        //UI根节点对象
        private GameObject _GoCanvasRoot = null;
        //UI脚本节点对象
        private Transform _TraUIScriptsNode = null;
        //顶层面板
        private GameObject _GoTopPanel;
        //遮罩面板
        private GameObject _GoMaskPanel;
        //UI摄像机
        private Camera _UICamera;
        //UI摄像机原始的“层深”
        private float _OriginalUICameralDepth;

        //得到实例
        public static UIMaskMgr GetInstance()
        {
            if (_Instance == null)
            {
                _Instance = new GameObject("_UIMaskMgr").AddComponent<UIMaskMgr>();
            }
            return _Instance;
        }




        void Awake()
        {
            //得到UI根节点对象、脚本节点对象
            _GoCanvasRoot = GameObject.FindGameObjectWithTag(SysDefine.SYS_TAG_CANVAS);
            _TraUIScriptsNode = UnityHelper.FindTheChildNode(_GoCanvasRoot, SysDefine.SYS_SCRIPTMANAGER_NODE);
            //把本脚本实例，作为“脚本节点对象”的子节点。
            UnityHelper.AddChildNodeToParentNode(_TraUIScriptsNode, this.gameObject.transform);
            //得到“顶层面板”、“遮罩面板”
            _GoTopPanel = _GoCanvasRoot;
            _GoMaskPanel = UnityHelper.FindTheChildNode(_GoCanvasRoot, "_UIMaskPanel").gameObject;
            //得到UI摄像机原始的“层深”
            _UICamera = GameObject.FindGameObjectWithTag("_TagUICamera").GetComponent<Camera>();
            if (_UICamera != null)
            {
                //得到UI摄像机原始“层深”
                _OriginalUICameralDepth = _UICamera.depth;
            }
            else
            {
                Debug.Log(GetType() + "/Start()/UI_Camera is Null!,Please Check! ");
            }
        }

        /// <summary>
        /// 设置遮罩状态
        /// </summary>
        /// <param name="goDisplayUIForms">需要显示的UI窗体</param>
        /// <param name="lucenyType">显示透明度属性</param>
	    public void SetMaskWindow(GameObject goDisplayUIForms, UIFormLucenyType lucenyType = UIFormLucenyType.Lucency)
        {
            //顶层窗体下移
            _GoTopPanel.transform.SetAsLastSibling();
            //启用遮罩窗体以及设置透明度
            switch (lucenyType)
            {
                //完全透明，不能穿透
                case UIFormLucenyType.Lucency:
                    _GoMaskPanel.SetActive(true);
                    Color newColor1 = new Color(SysDefine.SYS_UIMASK_LUCENCY_COLOR_RGB, SysDefine.SYS_UIMASK_LUCENCY_COLOR_RGB, SysDefine.SYS_UIMASK_LUCENCY_COLOR_RGB, SysDefine.SYS_UIMASK_LUCENCY_COLOR_RGB_A);
                    _GoMaskPanel.GetComponent<Image>().color = newColor1;
                    break;
                //半透明，不能穿透
                case UIFormLucenyType.Translucence:
                    _GoMaskPanel.SetActive(true);
                    Color newColor2 = new Color(SysDefine.SYS_UIMASK_TRANS_LUCENCY_COLOR_RGB, SysDefine.SYS_UIMASK_TRANS_LUCENCY_COLOR_RGB, SysDefine.SYS_UIMASK_TRANS_LUCENCY_COLOR_RGB, SysDefine.SYS_UIMASK_TRANS_LUCENCY_COLOR_RGB_A);
                    _GoMaskPanel.GetComponent<Image>().color = newColor2;
                    break;
                //低透明，不能穿透
                case UIFormLucenyType.ImPenetrable:
                    _GoMaskPanel.SetActive(true);
                    Color newColor3 = new Color(SysDefine.SYS_UIMASK_IMPENETRABLE_COLOR_RGB, SysDefine.SYS_UIMASK_IMPENETRABLE_COLOR_RGB, SysDefine.SYS_UIMASK_IMPENETRABLE_COLOR_RGB, SysDefine.SYS_UIMASK_IMPENETRABLE_COLOR_RGB_A);
                    _GoMaskPanel.GetComponent<Image>().color = newColor3;
                    break;
                //可以穿透
                case UIFormLucenyType.Pentrate:
                    if (_GoMaskPanel.activeInHierarchy)
                    {
                        _GoMaskPanel.SetActive(false);
                    }
                    break;

                default:
                    break;
            }



            //遮罩窗体下移
            _GoMaskPanel.transform.SetAsLastSibling();
            //显示窗体的下移
            goDisplayUIForms.transform.SetAsLastSibling();
            //增加当前UI摄像机的层深（保证当前摄像机为最前显示）
            if (_UICamera != null)
            {
                _UICamera.depth = _UICamera.depth + 100;    //增加层深
            }
        }

        /// <summary>
        /// 取消遮罩状态
        /// </summary>
	    public void CancelMaskWindow()
        {
            //顶层窗体上移
            _GoTopPanel.transform.SetAsFirstSibling();
            //禁用遮罩窗体
            if (_GoMaskPanel.activeInHierarchy)
            {
                //隐藏
                _GoMaskPanel.SetActive(false);
            }

            //恢复当前UI摄像机的层深 
            if (_UICamera != null)
            {
                _UICamera.depth = _OriginalUICameralDepth;  //恢复层深
            }
        }


    }
}
