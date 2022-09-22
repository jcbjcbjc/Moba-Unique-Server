using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace ObjectPool
{
    public class ObjectPool : MonoBehaviour
    {
        private GameObject prefab;
        private Queue<IMyObject> myObjects;  //�����ϵĽű�

        internal void Initialize(GameObject _prefab, int _size)
        {
            myObjects = new Queue<IMyObject>(_size);
            prefab = _prefab;
            if (prefab.GetComponent<IMyObject>() == null)
            {
                Debug.LogError("������е�����δʵ��IMyObject");
                return;
            }
            StartCoroutine(GenerateObject(_size));
        }

        private IEnumerator GenerateObject(int num)
        {
            MyObject temp;
            for (; myObjects.Count < num - 10;)
            {
                //ÿ֡����10������
                for (int i = 0; i < 10; i++)
                {
                    temp = ObjectPoolUtility.Create(prefab, true, this);
                    temp.transform.SetParent(transform, false);
                    myObjects.Enqueue(temp);
                }
                yield return null;
            }
            for (; myObjects.Count < num;)
            {
                temp = ObjectPoolUtility.Create(prefab, true, this);
                temp.transform.SetParent(transform, false);
                myObjects.Enqueue(temp);
            }
            GameLauncher.Instance.Count_Incomplete--;
        }

        internal IMyObject Activate(Vector3 position, Vector3 eulerAngles, Transform parent = null)
        {
            IMyObject ret;
            if (myObjects.Count > 0)
                ret = myObjects.Dequeue();
            else
            {
                Debug.LogWarning(gameObject.name + "�еĶ���������");
                ret = ObjectPoolUtility.Create(prefab, true, this);
                ret.Transform.parent = transform;
            }
            ret.Activate(position, eulerAngles, parent);
            return ret;
        }

        internal void Recycle(MyObject myObject)
        {
            myObjects.Enqueue(myObject);
        }
    }
}