using UnityEngine;

namespace Services
{
    public class ServiceSettings : ScriptableObject
    {
        public float t_wait_init = 5f;
        public float t_wait = 2f;
        public int index_startGame = 1; //初始化完成后游戏开始时，要加载的场景号
    }
}