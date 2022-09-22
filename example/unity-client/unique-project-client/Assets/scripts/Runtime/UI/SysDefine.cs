using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace UI
{
    public class SysDefine
    {
        /* 路径常量 */
        public const string SYS_PATH_CANVAS = "Canvas";
        public const string SYS_PATH_UIFORMS_CONFIG_INFO = "UIFormsConfigInfo";
        public const string SYS_PATH_CONFIG_INFO = "SysConfigInfo";

        /* 标签常量 */
        public const string SYS_TAG_CANVAS = "_TagCanvas";
        /* 节点常量 */
        public const string SYS_NORMAL_NODE = "Normal";
        public const string SYS_FIXED_NODE = "Fixed";
        public const string SYS_POPUP_NODE = "PopUp";
        public const string SYS_SCRIPTMANAGER_NODE = "_ScriptMgr";
        /* 遮罩管理器中，透明度常量 */
        public const float SYS_UIMASK_LUCENCY_COLOR_RGB = 255 / 255F;
        public const float SYS_UIMASK_LUCENCY_COLOR_RGB_A = 0F / 255F;

        public const float SYS_UIMASK_TRANS_LUCENCY_COLOR_RGB = 220 / 255F;
        public const float SYS_UIMASK_TRANS_LUCENCY_COLOR_RGB_A = 50F / 255F;

        public const float SYS_UIMASK_IMPENETRABLE_COLOR_RGB = 50 / 255F;
        public const float SYS_UIMASK_IMPENETRABLE_COLOR_RGB_A = 200F / 255F;

        /* 摄像机层深的常量 */

        /* 全局性的方法 */
        //Todo...

        /* 委托的定义 */
        //Todo....

    }
}
