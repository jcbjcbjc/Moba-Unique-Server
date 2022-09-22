using UnityEngine;

namespace MyTimer
{
    public class Sine : ILerp<float>
    {
        public float Value(float origin, float target, float percent, float time, float duration)
        {
            float a = (target - origin) / 2;
            float phase = percent * 2f * Mathf.PI;
            float mid = (target + origin) / 2;
            return a * Mathf.Sin(phase) + mid;
        }
    }
}