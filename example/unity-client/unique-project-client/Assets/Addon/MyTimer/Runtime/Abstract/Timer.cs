using UnityEngine;
using UnityEngine.Events;

namespace MyTimer
{
    [System.Serializable]
    /// <summary>
    /// ����һ����ʱ���ƽ������ı仯
    /// </summary>
    public class Timer<TValue,TLerp> where TLerp : ILerp<TValue>,new()
    {
        private readonly GameCycle gameCycle;

        [SerializeField]
        protected bool paused;

        /// <summary>
        /// �Ƿ���ͣ��������ʱĬ����ͣ,����Timerǰ��һ��Ҫȷ����Paused==true
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
        /// �Ƿ����
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
        /// ������ʱ��
        /// </summary>
        public float Time { get; protected set; }
        /// <summary>
        /// ����İٷֱȣ�0��1)
        /// </summary>
        public float Percent => Mathf.Clamp01(Time / Duration);
        /// <summary>
        /// ��ʱ��
        /// </summary>
        public float Duration { get; protected set; }
        /// <summary>
        /// ��ֵ
        /// </summary>
        public TValue Origin { get; protected set; }
        /// <summary>
        /// ��ֵ
        /// </summary>
        public TValue Target { get; protected set; }

        public ILerp<TValue> Lerp { get; protected set; }
        /// <summary>
        /// ��ǰֵ
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
        /// ΪMyTimer���ó�ʼ���Լ��Ƿ�������Ĭ��������
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
        /// ���¿�ʼ��ʱ
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
        /// ǿ�ƿ�������
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