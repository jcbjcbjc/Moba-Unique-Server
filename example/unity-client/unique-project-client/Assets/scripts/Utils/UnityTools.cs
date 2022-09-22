//
// @brief: unity相关功能封装
// @version: 1.0.0
// @author helin
// @date: 03/7/2018
// 
// 
//

#if _CLIENTLOGIC_
using UnityEngine;
#endif
using System.Collections;
using UnityEngine;

public class UnityTools {
    public static string playerPrefsGetString(string key)
    {

		return PlayerPrefs.GetString(key);
        
    }

    public static void playerPrefsSetString(string key, string value)
    {
		
		PlayerPrefs.SetString(key, value);
		
    }

    public static void setTimeScale(float value)
    {
		
		Time.timeScale = value;
		
    }

    public static void Log(object message)
    { 
		UnityEngine.Debug.Log(message);

		System.Console.WriteLine (message);

    }

    public static void LogError(object message)
    {
		
		Debug.LogError(message);
		
    }
    
}
