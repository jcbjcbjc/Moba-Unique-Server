using UnityEngine;
using UnityEngine.Events;

namespace ObjectPool
{
    /// <summary>
    /// ��������ObjectManagerҲ����ʹ��
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
        /// ��������
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
        /// �������壬��������ɶ���ش�������Ϊ��������
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
