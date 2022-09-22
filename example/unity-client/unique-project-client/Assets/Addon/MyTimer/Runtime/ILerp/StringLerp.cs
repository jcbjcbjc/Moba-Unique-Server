using UnityEngine;

namespace MyTimer
{
    public class StringLerp : ILerp<string>
    {
        public string Value(string origin, string target, float percent, float time, float duration)
        {
            return target.Substring(0, Mathf.FloorToInt(percent * target.Length));
        }
    }
}