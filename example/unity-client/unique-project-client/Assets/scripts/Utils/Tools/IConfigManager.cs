using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Assets.scripts.Utils.Tools
{
    public interface IConfigManager
    {

        /// <summary>
        /// 只读属性： 应用设置
        /// 功能： 得到键值对集合数据
        /// </summary>
	    Dictionary<string, string> AppSetting { get; }

        /// <summary>
        /// 得到配置文件（AppSeting）最大的数量
        /// </summary>
        /// <returns></returns>
	    int GetAppSettingMaxNumber();

    }

    [Serializable]
    internal class KeyValuesInfo
    {
        //配置信息
        public List<KeyValuesNode> ConfigInfo = null;
    }

    [Serializable]
    internal class KeyValuesNode
    {
        //键
        public string Key = null;
        //值
        public string Value = null;
    }
}
