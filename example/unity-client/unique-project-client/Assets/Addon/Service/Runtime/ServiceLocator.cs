using System;
using System.Collections.Generic;
using UnityEngine;

namespace Services
{
    public static class ServiceLocator
    {
        internal static readonly Dictionary<Type, Service> serviceDict = new Dictionary<Type, Service>();

        /// <summary>
        /// ��ȡһ������
        /// </summary>
        /// <typeparam name="T">�ڶ��������ѡ��һ��ʱ��TӦָ��Ϊһ����������ǹ�ͬ�ĸ���</typeparam>
        internal static T Get<T>() where T : Service
            => Get(typeof(T)) as T;

        internal static Service Get(Type type)
        {
            if (!serviceDict.ContainsKey(type))
            {
                Debug.LogWarning($"���񲻴��ڣ���������Ϊ{type}");
                return null;
            }
            return serviceDict[type];
        }

        internal static void Register(Service service)
        {
            
            Type type = service.GetType();
            if (serviceDict.ContainsKey(type))
            {
                Debug.LogWarning($"�������õĽű����޸��ˣ���������Ϊ{type}");
                serviceDict[type] = service;
            }
            else
                serviceDict.Add(type, service);
        }
    }
}