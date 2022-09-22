using UnityEngine;

/// <summary>
/// 由ObjectPool管理的游戏物体必须实现此接口
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