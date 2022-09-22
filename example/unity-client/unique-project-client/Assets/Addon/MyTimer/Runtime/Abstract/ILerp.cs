namespace MyTimer
{
    public interface ILerp<T>
    {
        public T Value(T origin, T target, float percent, float time, float duration);
    }
}