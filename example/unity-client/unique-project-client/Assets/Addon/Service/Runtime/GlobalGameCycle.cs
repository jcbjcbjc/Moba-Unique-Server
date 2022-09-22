using Services;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Events;

public enum EInvokeMode
{
    /// <summary>
    /// 每次FixedUpdate调用
    /// </summary>
    FixedUpdate,
    /// <summary>
    /// 下次FixedUpdate调用
    /// </summary>
    NextFixedUpdate,
    /// <summary>
    /// 每次Update调用
    /// </summary>
    Update,
    /// <summary>
    /// 下次Update调用
    /// </summary>
    NextUpdate,
    /// <summary>
    /// 每次LateUpdate调用
    /// </summary>
    LateUpdate,
    /// <summary>
    /// 下次LateUpdate调用
    /// </summary>
    NextLateUpdate,
}

[DefaultExecutionOrder(-100)]
public class GlobalGameCycle : Service
{
    private readonly Dictionary<EInvokeMode, UnityAction> cycle = new Dictionary<EInvokeMode, UnityAction>();
    private readonly Dictionary<EInvokeMode, UnityAction> temp = new Dictionary<EInvokeMode, UnityAction>();

    protected override void Awake()
    {
        base.Awake();
        foreach (EInvokeMode mode in System.Enum.GetValues(typeof(EInvokeMode)))
        {
            cycle.Add(mode, null);
            temp.Add(mode, null);
        }
        StartCoroutine(DelayAttach());
    }

    /// <summary>
    /// 用于将非Monobehavior方法加入游戏循环，加入的方法下一帧开始才会被调用
    /// </summary>
    public void AttachToGameCycle(EInvokeMode mode, UnityAction callBack)
    {
        temp[mode] += callBack;
    }

    public void RemoveFromGameCycle(EInvokeMode mode, UnityAction callBack)
    {
        cycle[mode] -= callBack;
        temp[mode] -= callBack;
    }

    private void Update()
    {
        cycle[EInvokeMode.Update]?.Invoke();
        cycle[EInvokeMode.NextUpdate]?.Invoke();
        cycle[EInvokeMode.NextUpdate] = null;
    }

    private void FixedUpdate()
    {
        cycle[EInvokeMode.FixedUpdate]?.Invoke();
        cycle[EInvokeMode.NextFixedUpdate]?.Invoke();
        cycle[EInvokeMode.NextFixedUpdate] = null;
    }

    private void LateUpdate()
    {
        cycle[EInvokeMode.LateUpdate]?.Invoke();
        cycle[EInvokeMode.NextLateUpdate]?.Invoke();
        cycle[EInvokeMode.NextLateUpdate] = null;
    }

    private IEnumerator DelayAttach()
    {
        for (; ; )
        {
            foreach (EInvokeMode mode in System.Enum.GetValues(typeof(EInvokeMode)))
            {
                cycle[mode] += temp[mode];
                temp[mode] = null;
            }
            yield return new WaitForEndOfFrame();
        }
    }
}