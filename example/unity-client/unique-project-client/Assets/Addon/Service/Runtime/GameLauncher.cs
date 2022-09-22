using Services;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Events;

//游戏开始时部分脚本执行顺序：
//①服务初始化
//②IInitailize初始化
//③对象池等脚本初始化

interface IInitialize
{
    void Initialize();
}

[DefaultExecutionOrder(-999)]
public class GameLauncher : MonoBehaviour
{
    public static GameLauncher Instance { private set; get; }

    public event UnityAction AfterInitializeScriptableObject;
    /// <summary>
    /// 要在游戏开始时初始化的脚本
    /// </summary>
    [SerializeField]
    private List<ScriptableObject> scripts;

    [SerializeField]
    private int count_incomplete;
    /// <summary>
    /// 剩余的初始化任务数
    /// </summary>
    public int Count_Incomplete
    {
        get => count_incomplete;
        set
        {
            if (value < 0 || value == count_incomplete)
                return;
            if (value == 0)
                StartGame();
            count_incomplete = value;
        }
    }

    private void Awake()
    {
        Instance = this;
       
        count_incomplete = 1;
        Invoke(nameof(Initialize),1f);
    }

    private void Initialize()
    {
        Random.InitState(System.DateTime.Now.Second);
        foreach (ScriptableObject obj in scripts)
        {
            (obj as IInitialize)?.Initialize();
        }
        AfterInitializeScriptableObject?.Invoke();
        Count_Incomplete--;
    }

    private void StartGame()
    {
        
        ServiceLocator.Get<SceneControllerBase>().LoadScene(1);
    }
}