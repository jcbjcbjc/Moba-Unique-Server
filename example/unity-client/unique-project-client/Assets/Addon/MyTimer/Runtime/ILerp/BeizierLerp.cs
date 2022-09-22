using UnityEngine;

namespace MyTimer
{
    public class BeizierLerp : ILerp<Vector3>
    {
        public Vector3[] points;

        public static Vector3 BezierLerp(Vector3[] points, float percent)
        {
            int count = points.Length;
            switch (count)
            {
                case 0:
                case 1:
                    throw new System.ArgumentException();
                case 2:
                    return Vector3.Lerp(points[0], points[1], percent);
                default:
                    Vector3[] newPoints = new Vector3[count - 1];
                    for (int i = 0; i < count - 1; i++)
                    {
                        newPoints[i] = Vector3.Lerp(points[i], points[i + 1], percent);
                    }
                    return BezierLerp(newPoints, percent);
            }
        }

        public Vector3 Value(Vector3 origin, Vector3 target, float percent, float time, float duration)
        {
            if (points == null)
                return default;
            int count = points.Length;
            switch (count)
            {
                case 0:
                case 1:
                    throw new System.ArgumentException();
                case 2:
                    return Vector3.Lerp(points[0], points[1], percent);
                default:
                    Vector3[] newPoints = new Vector3[count - 1];
                    for (int i = 0; i < count - 1; i++)
                    {
                        newPoints[i] = Vector3.Lerp(points[i], points[i + 1], percent);
                    }
                    return BezierLerp(newPoints, percent);
            }
        }
    }
}