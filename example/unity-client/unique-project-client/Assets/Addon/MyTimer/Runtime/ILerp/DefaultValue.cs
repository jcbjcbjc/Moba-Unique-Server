namespace MyTimer
{
    public class DefaultValue<T> : ILerp<T>
    {
        public T Value(T origin, T target, float percent, float time, float duration)
        {
            return default;
        }
    }
}