using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Services;
using C2GNet;
/// <summary>
/// GameLogicLoginService
/// 
/// @Author ¼Ö³¬²©
/// 
/// @Date 2022/4/30
/// </summary>
namespace NetWork
{
    public class User :Service
    {
        

        public NUser user=null;
        public bool isLogin = false;  //ÊÇ·ñµÇÂ¼
        public NRoom room = null;  //·¿¼ä

    }

}