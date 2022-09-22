using Services;
using System;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Events;

[DefaultExecutionOrder(-100)]
public class EventSystem : Service
{
    private readonly Dictionary<EEvent, Type> typeDict = new Dictionary<EEvent, Type>();
    private readonly Dictionary<EEvent, Delegate> eventDict = new Dictionary<EEvent, Delegate>();

    private bool Check(EEvent eEvent, Type methodType)
    {
        if (!typeDict.ContainsKey(eEvent))
        {
            CreateEvent(eEvent, methodType);
            return true;
        }
        if (typeDict[eEvent] != methodType)
        {
            Debug.LogWarning($"��Ӧ���������Ͳ������¼���Ҫ�������,�¼���Ϊ{eEvent}");
            return false;
        }
        return true;
    }

    private void CreateEvent(EEvent eEvent, Type type)
    {
        if (!type.IsSubclassOf(typeof(Delegate)))
        {
            Debug.LogWarning($"{type}����Delegate������");
            return;
        }
        if (eventDict.ContainsKey(eEvent))
        {
            Debug.LogWarning($"��Ϊ{eEvent}���¼��Ѵ���");
            return;
        }
        typeDict.Add(eEvent, type);
        eventDict.Add(eEvent, null);
    }

    public void AddListener(EEvent eEvent, UnityAction callBack)
    {
        if (Check(eEvent, callBack.GetType()))
            eventDict[eEvent] = eventDict[eEvent] as UnityAction + callBack;
    }
    public void AddListener<T1>(EEvent eEvent, UnityAction<T1> callBack)
    {
        if (Check(eEvent, callBack.GetType()))
            eventDict[eEvent] = eventDict[eEvent] as UnityAction<T1> + callBack;
    }
    public void AddListener<T1, T2>(EEvent eEvent, UnityAction<T1, T2> callBack)
    {
        if (Check(eEvent, callBack.GetType()))
            eventDict[eEvent] = eventDict[eEvent] as UnityAction<T1, T2> + callBack;
    }
    public void AddListener<T1, T2, T3>(EEvent eEvent, UnityAction<T1, T2, T3> callBack)
    {
        if (Check(eEvent, callBack.GetType()))
            eventDict[eEvent] = eventDict[eEvent] as UnityAction<T1, T2, T3> + callBack;
    }

    public void RemoveListener(EEvent eEvent, UnityAction callBack)
    {
        if (Check(eEvent, callBack.GetType()))
            eventDict[eEvent] = eventDict[eEvent] as UnityAction - callBack;
    }
    public void RemoveListener<T1>(EEvent eEvent, UnityAction<T1> callBack)
    {
        if (Check(eEvent, callBack.GetType()))
            eventDict[eEvent] = eventDict[eEvent] as UnityAction<T1> - callBack;
    }
    public void RemoveListener<T1, T2>(EEvent eEvent, UnityAction<T1, T2> callBack)
    {
        if (Check(eEvent, callBack.GetType()))
            eventDict[eEvent] = eventDict[eEvent] as UnityAction<T1, T2> - callBack;
    }
    public void RemoveListener<T1, T2, T3>(EEvent eEvent, UnityAction<T1, T2, T3> callBack)
    {
        if (Check(eEvent, callBack.GetType()))
            eventDict[eEvent] = eventDict[eEvent] as UnityAction<T1, T2, T3> - callBack;
    }

    public void Invoke(EEvent eEvent)
    {
        if (Check(eEvent, typeof(UnityAction)))
            (eventDict[eEvent] as UnityAction)?.Invoke();
    }
    public void Invoke<T1>(EEvent eEvent, T1 arg1)
    {
        if (Check(eEvent, typeof(UnityAction<T1>)))
            (eventDict[eEvent] as UnityAction<T1>)?.Invoke(arg1);
    }
    public void Invoke<T1, T2>(EEvent eEvent, T1 arg1, T2 arg2)
    {
        if (Check(eEvent, typeof(UnityAction<T1, T2>)))
            (eventDict[eEvent] as UnityAction<T1, T2>)?.Invoke(arg1, arg2);
    }
    public void Invoke<T1, T2, T3>(EEvent eEvent, T1 arg1, T2 arg2, T3 arg3)
    {
        if (Check(eEvent, typeof(UnityAction<T1, T2, T3>)))
            (eventDict[eEvent] as UnityAction<T1, T2, T3>)?.Invoke(arg1, arg2, arg3);
    }
}
