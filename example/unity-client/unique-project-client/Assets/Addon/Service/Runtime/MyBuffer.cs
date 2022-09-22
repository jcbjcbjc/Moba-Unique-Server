using Services;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using MyTimer;

//定期回收超过一定时间未使用的资源
public class MyBuffer<T> where T : class
{
    private readonly float garbageTime;
    private readonly Metronome metronome;
    private readonly Dictionary<string, T> buffer;
    private readonly Dictionary<string, float> timer;
    private readonly GlobalGameCycle gameCycle;

    public MyBuffer(float time = 600)
    {
        if (time < 600)
            Debug.LogWarning("垃圾回收时间过短");
        garbageTime = time;
        gameCycle = ServiceLocator.Get<GlobalGameCycle>();
        buffer = new Dictionary<string, T>();
        timer = new Dictionary<string, float>();
        metronome = new Metronome();
        metronome.OnComplete += StartGC;
        metronome.Initialize(garbageTime);
    }

    public void Write(string name, T obj)
    {
        if (buffer.ContainsKey(name))
            buffer[name] = obj;
        else
            buffer.Add(name, obj);
        timer[name] = Time.time;
    }

    public T Read(string name)
    {
        if (buffer.ContainsKey(name))
        {
            timer[name] = Time.time;
            return buffer[name];
        }
        return null;
    }

    public IEnumerator GarbageCollect()
    {
        int countPerFrame = Mathf.Max(100, buffer.Count / 10);
        int count = 0;
        foreach (string name in timer.Keys)
        {
            if (Time.time - timer[name] > garbageTime)
            {
                timer.Remove(name);
                buffer.Remove(name);
            }
            count++;
            if (count == countPerFrame)
                yield return null;
        }
        yield return null;
        Resources.UnloadUnusedAssets();
    }

    private void StartGC(float _)
        => gameCycle.StartCoroutine(GarbageCollect());
}
