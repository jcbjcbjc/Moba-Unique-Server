namespace MyTimer
{
    public class CurrentTime : ILerp<float>
    {
        public float Value(float origin, float target,float percent, float time, float duration)
        {
            return time;
        }
    }
}