using UnityEngine;

namespace MyTimer
{
    public class ColorLerp : ILerp<Color>
    {
        public Color Value(Color origin, Color target,float percent, float time, float duration)
        {
            return Color.Lerp(origin, target, percent);
        }
    }
}