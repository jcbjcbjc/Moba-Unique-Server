using UnityEngine;

namespace ObjectPool
{
    public static class ObjectPoolUtility
    {
        /// <summary>
        /// 用此方法生成MyObject挂载的游戏物体，而不是Object.Instantiate
        /// </summary>
        /// <param name="prefab">要克隆的游戏物体</param>
        /// <param name="byPool">是否由对象池生成</param>
        /// <param name="pool">所属的对象池</param>
        /// <returns>生成的游戏物体的MyObject脚本</returns>
        public static MyObject Create(GameObject prefab, bool byPool = false, ObjectPool pool = null)
        {
            GameObject obj = GameObject.Instantiate(prefab);
            MyObject myObject = obj.GetComponent<MyObject>();
            myObject.b_createdByPool = byPool;
            if (byPool)
            {
                if (pool == null)
                    Debug.LogWarning("未分配对象池");
                myObject.active = true;
                myObject.Active = false;
                myObject.objectPoolAttached = pool;
            }
            return myObject;
        }

        /// <summary>
        /// 回收一个游戏物体所有子物体中的MyObject组件
        /// </summary>
        public static void RecycleMyObjects(GameObject gameObject)
        {
            MyObject[] myObjects = gameObject.GetComponentsInChildren<MyObject>();
            foreach (MyObject obj in myObjects)
            {
                obj.Recycle();
            }
        }
    }
}