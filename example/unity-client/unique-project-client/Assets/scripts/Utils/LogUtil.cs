using C2GNet;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UnityEngine;

namespace Assets.scripts.Utils
{
    public class LogUtil
    {
        public static void log(string v, object obj=null,object oo=null)
        {
            Debug.Log(v+obj+oo);
        }
    }
}
