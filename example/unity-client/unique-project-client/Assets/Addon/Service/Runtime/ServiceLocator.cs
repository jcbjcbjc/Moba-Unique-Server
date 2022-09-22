using System;
using System.Collections.Generic;
using UnityEngine;

namespace Services
{
    public static class ServiceLocator
    {
        internal static readonly Dictionary<Type, Service> serviceDict = new Dictionary<Type, Service>();

        /// <summary>
        /// 获取一个服务
        /// </summary>
        /// <typeparam name="T">在多个子类中选择一个时，T应指定为一个子类而不是共同的父类</typeparam>
        internal static T Get<T>() where T : Service
            => Get(typeof(T)) as T;

        internal static Service Get(Type type)
        {
            if (!serviceDict.ContainsKey(type))
            {
                Debug.LogWarning($"服务不存在，服务类型为{type}");
                return null;
            }
            return serviceDict[type];
        }

        internal static void Register(Service service)
        {
            
            Type type = service.GetType();
            if (serviceDict.ContainsKey(type))
            {
                Debug.LogWarning($"服务引用的脚本被修改了，服务类型为{type}");
                serviceDict[type] = service;
            }
            else
                serviceDict.Add(type, service);
        }
    }
}