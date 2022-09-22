using UnityEngine;

namespace MyTimer
{
    public class Vector3Lerp : ILerp<Vector3>
    {
        public Vector3 Value(Vector3 origin, Vector3 target, float percent, float time, float duration)
        {
            return Vector3.Lerp(origin, target, percent);
        }
    }
}