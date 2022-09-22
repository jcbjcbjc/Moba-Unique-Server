using UnityEngine;
using UnityEngine.Events;

namespace MyTimer
{
    [System.Serializable]
    /// <summary>
    /// 描述一段随时间推进发生的变化
    /// </summary>
    public class Timer<TValue,TLerp> where TLerp : ILerp<TValue>,new()
    {
        private readonly GameCycle gameCycle;

        [SerializeField]
        protected bool paused;

        /// <summary>
        /// 是否暂停，被创建时默认暂停,弃用Timer前，一定要确保其Paused==true
        /// </summary>
        public bool Paused
        {
            get => paused;
            set
            {
                if (paused != value)
                {
                    paused = value;
                    if (value)
                    {
                        OnPause?.Invoke(Current);
                        gameCycle.RemoveFromGameCycle(EInvokeMode.Update, Update);
                    }
                    else
                    {
                        OnUnpause?.Invoke(Current);
                        gameCycle.AttachToGameCycle(EInvokeMode.Update, Update);
                    }
                }
            }
        }

        [SerializeField]
        protected bool completed;
        /// <summary>
        /// 是否完成
        /// </summary>
        public bool Completed
        {
            get => completed;
            protected set
            {
                if (completed != value)
                {
                    completed = value;
                    if (value)
                    {
                        OnComplete?.Invoke(Current);
                    }
                }
            }
        }

        /// <summary>
        /// 经过的时间
        /// </summary>
        public float Time { get; protected set; }
        /// <summary>
        /// 到达的百分比（0～1)
        /// </summary>
        public float Percent => Mathf.Clamp01(Time / Duration);
        /// <summary>
        /// 总时间
        /// </summary>
        public float Duration { get; protected set; }
        /// <summary>
        /// 初值
        /// </summary>
        public TValue Origin { get; protected set; }
        /// <summary>
        /// 终值
        /// </summary>
        public TValue Target { get; protected set; }

        public ILerp<TValue> Lerp { get; protected set; }
        /// <summary>
        /// 当前值
        /// </summary>
        public TValue Current => Lerp.Value(Origin, Target, Percent, Time, Duration);
        
        public event UnityAction<TValue> OnPause;
        public event UnityAction<TValue> OnUnpause;
        public event UnityAction<TValue> OnComplete;
        public event UnityAction<TValue> OnUpdate;

        public Timer()
        {
            Lerp = new TLerp();
            gameCycle = GameCycle.Instance;
            paused = true;
        }

        /// <summary>
        /// 为MyTimer设置初始属性及是否启动（默认启动）
        /// </summary>
        public virtual void Initialize(TValue origin, TValue target, float duration, bool start = true)
        {
            Duration = duration;
            Origin = origin;
            Target = target;
            if (start)
                Restart();
        }

        protected void Update()
        {
            Time += UnityEngine.Time.deltaTime;
            if (Time >= Duration)
            {
                Paused = true;
                Completed = true;
            }
            OnUpdate?.Invoke(Current);
        }

        /// <summary>
        /// 重新开始计时
        /// </summary>
        public void Restart(bool fixedTime = false)
        {
            if (fixedTime)
                Time -= Duration;
            else
                Time = 0;
            Paused = false;
            Completed = false;
        }

        /// <summary>
        /// 强制快进到完成
        /// </summary>
        public void Complete()
        {
            Time = Duration;
            Paused = true;
            Completed = true;
        }

        public override string ToString()
        {
            return $"Paused:{Paused},Completed:{Completed},Origin:{Origin},Target:{Target},Duration:{Duration}";
        }
    }
}