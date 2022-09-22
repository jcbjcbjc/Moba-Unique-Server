using System;
using UnityEngine;

[DefaultExecutionOrder(-100)]
/// <summary>
/// ���ô��������Դʱ��·��ΪResources�ļ����ڲ���·�����Ҳ��ܰ�����չ��
/// </summary>
public class AssetLoader_Resoureces : AssetLoader
{
    private class Coupling<T> where T : UnityEngine.Object
    {
        private readonly ResourceRequest request;
        private readonly Action<T> callBack;

        public Coupling(ResourceRequest _request, Action<T> _callBack)
        {
            request = _request;
            callBack = _callBack;
        }

        public void OnCompleteOperation(AsyncOperation asyncOperation)
        {
            T asset = request.asset as T;
            if (asset == null)
                Debug.LogWarning("������Դʧ��");
            callBack?.Invoke(asset);
        }
    }

    protected override void Load<T>(string path, Action<T> callBack)
    {
        T asset = Resources.Load<T>(path);
        callBack?.Invoke(asset);
    }

    protected override void LoadAsync<T>(string path, Action<T> callBack)
    {
        ResourceRequest request = Resources.LoadAsync<T>(path);
        Coupling<T> intermediary = new Coupling<T>(request, callBack);
        request.completed += intermediary.OnCompleteOperation;
    }

    public override void UnLoadAsset<T>(T t)
    {
        Resources.UnloadAsset(t);
    }

    public override string FormatPath(string path)
    {
        int first = path.IndexOf("Resources/");
        int lastRight = path.LastIndexOf('.');
        if (first == -1)
            first = 0;
        else
            first += "Resources/".Length;
        if (lastRight == -1)
            lastRight = path.Length;
        return path.Substring(first, lastRight - first);
    }
}
