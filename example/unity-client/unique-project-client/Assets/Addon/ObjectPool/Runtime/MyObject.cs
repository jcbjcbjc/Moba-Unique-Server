using UnityEngine;
using UnityEngine.Events;

namespace ObjectPool
{
    /// <summary>
    /// 此类脱离ObjectManager也可以使用
    /// </summary>
    public sealed class MyObject : MonoBehaviour, IMyObject
    {
        public event UnityAction OnRecycle;
        public event UnityAction OnActivate;

        internal bool b_createdByPool;
        internal ObjectPool objectPoolAttached;

        public Transform Transform => transform;

        [SerializeField]
        internal bool active;
        public bool Active
        {
            get => active;
            internal set
            {
                if (value == active)
                    return;
                active = value;
                gameObject.SetActive(value);
            }
        }

        /// <summary>
        /// 激活物体
        /// </summary>
        public void Activate(Vector3 pos, Vector3 eulerAngles, Transform parent = null)
        {
            if (parent != null)
                transform.SetParent(parent);
            transform.position = pos;
            transform.eulerAngles = eulerAngles;
            Active = true;
            OnActivate?.Invoke();
        }
        /// <summary>
        /// 回收物体，如果不是由对象池创建，改为销毁物体
        /// </summary>
        public void Recycle()
        {
            if (b_createdByPool && objectPoolAttached != null)
            {
                OnRecycle?.Invoke();
                Active = false;
                objectPoolAttached.Recycle(this);
                transform.SetParent(objectPoolAttached.transform, false);
            }
            else
                Destroy(gameObject);
        }
    }
}
