using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Services;
using C2GNet;
/// <summary>
/// GameLogicLoginService
/// 
/// @Author �ֳ���
/// 
/// @Date 2022/4/30
/// </summary>
namespace NetWork
{
    public class User :Service
    {
        

        public NUser user=null;
        public bool isLogin = false;  //�Ƿ��¼
        public NRoom room = null;  //����

    }

}