using UnityEngine;

/// <summary>
/// ��ObjectPool�������Ϸ�������ʵ�ִ˽ӿ�
/// </summary>
namespace ObjectPool
{
    public interface IMyObject
    {
        Transform Transform { get; }
        bool Active { get; }
        void Activate(Vector3 pos, Vector3 eulerAngles, Transform parent = null);
        void Recycle();
    }
}