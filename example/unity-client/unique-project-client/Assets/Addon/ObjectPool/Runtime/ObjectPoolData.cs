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
    /// ������������������
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

