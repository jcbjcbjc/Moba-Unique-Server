namespace MyTimer
{
    /// <summary>
    /// 基本的往复变化
    /// </summary>
    public class Circulation<TValue,TLerp> : Timer<TValue, TLerp> where TLerp : ILerp<TValue>, new()
    {
        public Circulation()
        {
            OnComplete += MyOnComplete;
        }

        private void MyOnComplete(TValue _)
        {
            TValue temp = Origin;
            Origin = Target;
            Target = temp;
            Restart(true);
        }
    }

    /// <summary>
    /// 基本的反复变化
    /// </summary>
    public class Repeataion<TValue, TLerp> : Timer<TValue, TLerp> where TLerp : ILerp<TValue>, new()
    {
        public Repeataion()
        {
            OnComplete += MyOnComplete;
        }

        private void MyOnComplete(TValue _)
        {
            Restart(true);
        }
    }

    /// <summary>
    /// 不使用值，仅周期性调用方法
    /// </summary>
    public class Metronome : Repeataion<float,DefaultValue<float>>
    {
        public virtual void Initialize(float duration, bool start = true)
        {
            base.Initialize(0f, 0f, duration, start);
        }
    }

    /// <summary>
    /// 仅计时，不调用方法
    /// </summary>
    public class TimerOnly : Timer<float, CurrentTime>
    {
        public void Initialize(float duration, bool start = true)
        {
            base.Initialize(0f, duration, duration, start);
        }
    }
}