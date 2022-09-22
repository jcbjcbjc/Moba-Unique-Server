using Assets.scripts.Utils.Tools;
using System;
using System.Collections.Generic;

using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UnityEngine;

namespace Managers
{
    public class LauguageMgr
    {
        //本类实例
        public static LauguageMgr _Instance;
        //语言翻译的缓存集合
        private Dictionary<string, string> _DicLauguageCache;



        private LauguageMgr()
        {
            _DicLauguageCache = new Dictionary<string, string>();
            //初始化语言缓存集合
            InitLauguageCache();
        }

        /// <summary>
        /// 得到本类实例
        /// </summary>
        /// <returns></returns>
	    public static LauguageMgr GetInstance()
        {
            if (_Instance == null)
            {
                _Instance = new LauguageMgr();
            }
            return _Instance;
        }

        /// <summary>
        /// 到显示文本信息
        /// </summary>
        /// <param name="lauguageID">语言的ID</param>
        /// <returns></returns>
	    public string ShowText(string lauguageID)
        {
            string strQueryResult = string.Empty;           //查询结果

            //参数检查
            if (string.IsNullOrEmpty(lauguageID)) return null;

            //查询处理
            if (_DicLauguageCache != null && _DicLauguageCache.Count >= 1)
            {
                _DicLauguageCache.TryGetValue(lauguageID, out strQueryResult);
                if (!string.IsNullOrEmpty(strQueryResult))
                {
                    return strQueryResult;
                }
            }

            Debug.Log(GetType() + "/ShowText()/ Query is Null!  Parameter lauguageID: " + lauguageID);
            return null;
        }

        /// <summary>
        /// 初始化语言缓存集合
        /// </summary>
        private void InitLauguageCache()
        {
            IConfigManager config = new ConfigManagerByJson("LauguageJSONConfig");
            if (config != null)
            {
                _DicLauguageCache = config.AppSetting;
            }
        }


    }
}
