using UnityEngine;

namespace ObjectPool
{
    public enum EObject
    {
        Effect_AffectRange,
        Effect_CastRange,
        StatusBar,
        IndicatorPoint,
        SlowArea,
        Trap,
        FlashLight,
    }

    /// <summary>
    /// 创建对象池所需的数据
    /// </summary>
    [System.Serializable]
    public class ObjectPoolData
    {
        [SerializeField]
        internal EObject eObject;
        [SerializeField]
        internal int size;
        [SerializeField]
        internal GameObject prefab;
    }
}

