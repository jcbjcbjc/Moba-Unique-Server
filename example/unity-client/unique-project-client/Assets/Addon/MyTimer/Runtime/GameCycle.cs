using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Events;

namespace MyTimer
{
    public enum EInvokeMode
    {
        FixedUpdate,
        Update,
        LateUpdate,
    }

    public class GameCycle : MonoBehaviour
    {
        private static GameCycle instance;

        internal static GameCycle Instance
        {
            get
            {
                if (instance == null)
                {
                    GameObject obj = new GameObject("GameCycleForTimer");
                    instance = obj.AddComponent<GameCycle>();
                }
                return instance;
            }
        }

        private readonly Dictionary<EInvokeMode, UnityAction> cycle = new Dictionary<EInvokeMode, UnityAction>();
        private readonly Dictionary<EInvokeMode, UnityAction> temp = new Dictionary<EInvokeMode, UnityAction>();

        private void Awake()
        {
            DontDestroyOnLoad(gameObject);
            foreach (EInvokeMode mode in System.Enum.GetValues(typeof(EInvokeMode)))
            {
                cycle.Add(mode, null);
                temp.Add(mode, null);
            }
            StartCoroutine(DelayAttach());
        }

        public void AttachToGameCycle(EInvokeMode mode, UnityAction callBack)
        {
            temp[mode] += callBack;
        }

        public void RemoveFromGameCycle(EInvokeMode mode, UnityAction callBack)
        {
            cycle[mode] -= callBack;
            temp[mode] -= callBack;
        }

        private void Update()
        {
            cycle[EInvokeMode.Update]?.Invoke();
        }

        private void FixedUpdate()
        {
            cycle[EInvokeMode.FixedUpdate]?.Invoke();
        }

        private void LateUpdate()
        {
            cycle[EInvokeMode.LateUpdate]?.Invoke();
        }

        private IEnumerator DelayAttach()
        {
            for (; ; )
            {
                foreach (EInvokeMode mode in System.Enum.GetValues(typeof(EInvokeMode)))
                {
                    cycle[mode] += temp[mode];
                    temp[mode] = null;
                }
                yield return new WaitForEndOfFrame();
            }
        }
    }
}