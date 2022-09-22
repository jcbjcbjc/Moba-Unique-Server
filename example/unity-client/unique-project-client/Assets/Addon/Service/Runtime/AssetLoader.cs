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
    /// ������Դ
    /// </summary>
    /// <param name="path">·��</param>
    /// <param name="callBack">�ص����������غõ���Դ��Ϊ�������أ�</param>
    /// <param name="async">�Ƿ��첽</param>
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
            Debug.LogWarning($"�޷�������Դ��·��Ϊ{path}");
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
