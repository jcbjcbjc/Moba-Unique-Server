using UnityEngine;

namespace MyTimer
{
    public class BezierCurve : Timer<Vector3, BeizierLerp>
    {
        public void Initialize(Vector3[] points, float duration, bool start = true)
        {
            base.Initialize(points[0], points[points.Length - 1], duration, start);
            (Lerp as BeizierLerp).points = points;
        }
    }
}