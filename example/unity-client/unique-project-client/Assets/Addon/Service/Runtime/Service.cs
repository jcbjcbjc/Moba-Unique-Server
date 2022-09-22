using System;
using System.Reflection;
using UnityEngine;

namespace Services
{
    /// <summary>
    /// 可通过Get获取继承此类的子类对象，每个类的对象应当是唯一的
    /// </summary>
    public class Service : MonoBehaviour
    {
        public static T Get<T>() where T : Service
        {
            return ServiceLocator.Get<T>();
        }

        protected virtual void Awake()
        {
            ServiceLocator.Register(this);
        }

        /// <summary>
        /// 服务初始化完成后调用此方法
        /// </summary>
        protected internal virtual void AfterInitailize()
        {
            
        }

        // 禁止重写此方法
        protected void Start() {
            GetOtherService();
            AfterInitailize();
        }

        internal void GetOtherService()
        {
            static T GetAttribute<T>(MemberInfo member, bool inherit = false) where T : Attribute
            {
                object[] ret = member.GetCustomAttributes(typeof(T), inherit);
                return ret.Length > 0 ? ret[0] as T : null;
            }

            FieldInfo[] infos = GetType().GetFields(BindingFlags.Instance | BindingFlags.Public | BindingFlags.NonPublic);
            foreach (FieldInfo info in infos)
            {
                Type type = info.FieldType;
                OtherAttribute attribute = GetAttribute<OtherAttribute>(info);
                if (attribute != null && type.IsSubclassOf(typeof(Service)))
                {
                    if (attribute.type != null && attribute.type.IsSubclassOf(type))
                        type = attribute.type;
                    info.SetValue(this, ServiceLocator.Get(type));
                }
            }
        }
    }

    /// <summary>
    ///自动获取其他服务，仅用于Service的子类，仅用于服务初始化阶段
    /// </summary>
    [AttributeUsage(AttributeTargets.Field)]
    internal class OtherAttribute : Attribute
    {
        internal Type type;

        /// <param name="_type">获取B类实例并赋值给A类时（B类继承A类），需要指定B类的类型为参数</param>
        public OtherAttribute(Type _type = null)
        {
            type = _type;
        }
    }
}