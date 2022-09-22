namespace MyTimer
{
    /// <summary>
    /// 打字机效果
    /// </summary>
    public class TypeWriter : Timer<string,StringLerp>
    {
        /// <param name="text">文本</param>
        /// <param name="letterPerSecond">每秒打的字符数</param>
        /// <param name="start">是否启动计时器</param>
        public void Initialize(string text, float letterPerSecond, bool start = true)
        {
            base.Initialize("", text, text.Length / letterPerSecond, start);
        }
    }

    /// <summary>
    /// 为TypeWriter提供句子结束后停顿的功能
    /// </summary>
    public class TypeWriterExtend
    {
        public const string SentenceSeparator = "?!。！？…";

        private TypeWriter typeWriter;
        private string separator;
        private float interval;
        private readonly TimerOnly timer;

        private string target;

        private string text;
        private string Text
        {
            set
            {
                if (text != value)
                {
                    text = value;
                    if (value.Length > 1 && separator.IndexOf(target[value.Length - 1]) != -1)
                    {
                        //避免重复分隔符多次延时
                        if (target.Length > value.Length)
                        {
                            if (separator.IndexOf(target[value.Length]) == -1)
                            {
                                typeWriter.Paused = true;
                                timer.Initialize(interval);
                            }
                        }
                        else
                        {
                            typeWriter.Paused = true;
                            timer.Initialize(interval);
                        }
                    }
                }
            }
        }

        public TypeWriterExtend()
        {
            timer = new TimerOnly();
            timer.OnComplete += AfterDelay;
        }

        /// <param name="_typeWriter">要控制的TypeWriter</param>
        /// <param name="_interval">每句话结束后的停留时间</param>
        /// <param name="_separator">所有标志句子结束的标志，为空则使用默认的结束标志</param>
        public void Initialize(TypeWriter _typeWriter, float _interval = 0.5f, string _separator = null)
        {
            Dispose();
            typeWriter = _typeWriter;
            separator = _separator ?? SentenceSeparator;
            interval = _interval;
            typeWriter.OnUpdate += OnUpdate;
            typeWriter.OnUnpause += OnUnpause;
        }

        private void OnUnpause(string _)
        {
            target = typeWriter.Target;
        }

        private void OnUpdate(string current)
        {
            Text = typeWriter.Current;
        }

        private void Dispose()
        {
            if (typeWriter != null)
            {
                typeWriter.Paused = true;
                typeWriter.OnUpdate -= OnUpdate;
            }
        }

        private void AfterDelay(float _)
        {
            typeWriter.Paused = false;
        }
    }
}
