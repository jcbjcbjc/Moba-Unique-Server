using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace ObjectPool
{
    public class ObjectPool : MonoBehaviour
    {
        private GameObject prefab;
        private Queue<IMyObject> myObjects;  //对象上的脚本

        internal void Initialize(GameObject _prefab, int _size)
        {
            myObjects = new Queue<IMyObject>(_size);
            prefab = _prefab;
            if (prefab.GetComponent<IMyObject>() == null)
            {
                Debug.LogError("对象池中的物体未实现IMyObject");
                return;
            }
            StartCoroutine(GenerateObject(_size));
        }

        private IEnumerator GenerateObject(int num)
        {
            MyObject temp;
            for (; myObjects.Count < num - 10;)
            {
                //每帧生成10个物体
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
                Debug.LogWarning(gameObject.name + "中的对象用完了");
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