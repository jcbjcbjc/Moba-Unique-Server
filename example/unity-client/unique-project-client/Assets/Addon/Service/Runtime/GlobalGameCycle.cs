using Services;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Events;

public enum EInvokeMode
{
    /// <summary>
    /// ÿ��FixedUpdate����
    /// </summary>
    FixedUpdate,
    /// <summary>
    /// �´�FixedUpdate����
    /// </summary>
    NextFixedUpdate,
    /// <summary>
    /// ÿ��Update����
    /// </summary>
    Update,
    /// <summary>
    /// �´�Update����
    /// </summary>
    NextUpdate,
    /// <summary>
    /// ÿ��LateUpdate����
    /// </summary>
    LateUpdate,
    /// <summary>
    /// �´�LateUpdate����
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
    /// ���ڽ���Monobehavior����������Ϸѭ��������ķ�����һ֡��ʼ�Żᱻ����
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