using Services;
using System;
using UnityEngine;

public abstract class AssetLoader : Service
{
    private class BufferUpdate
    {
        private readonly MyBuffer<UnityEngine.Object> myBuffer;
        private readonly string path;

        public BufferUpdate(MyBuffer<UnityEngine.Object> buffer, string _path)
        {
            myBuffer = buffer;
            path = _path;
        }

        public void AfterLoadAsset(UnityEngine.Object asset)
        {
            myBuffer.Write(path, asset);
        }
    }

    private MyBuffer<UnityEngine.Object> myBuffer;

    protected override void Awake()
    {
        base.Awake();
        myBuffer = new MyBuffer<UnityEngine.Object>();
    }

    /// <summary>
    /// 加载资源
    /// </summary>
    /// <param name="path">路径</param>
    /// <param name="callBack">回调函数（加载好的资源作为参数返回）</param>
    /// <param name="async">是否异步</param>
    public void LoadAsset<T>(string path, Action<T> callBack, bool async = true) where T : UnityEngine.Object
    {
        if (string.IsNullOrEmpty(path))
            return;
        path = FormatPath(path);
        try
        {
            T obj = myBuffer.Read(path) as T;
            if (obj != null)
                callBack?.Invoke(obj);
            else
            {
                BufferUpdate request = new BufferUpdate(myBuffer, path);
                callBack += request.AfterLoadAsset;
                if (async)
                    LoadAsync<T>(path, callBack);
                else
                    Load<T>(path, callBack);
            }
        }
        catch (Exception e)
        {
            Debug.LogError(e);
            Debug.LogWarning($"无法加载资源，路径为{path}");
        }
    }

    protected abstract void Load<T>(string path, Action<T> callBack) where T : UnityEngine.Object;

    protected abstract void LoadAsync<T>(string path, Action<T> callBack) where T : UnityEngine.Object;

    public abstract void UnLoadAsset<T>(T asset) where T : UnityEngine.Object;

    public virtual string FormatPath(string path)
    {
        return path;
    }
}
