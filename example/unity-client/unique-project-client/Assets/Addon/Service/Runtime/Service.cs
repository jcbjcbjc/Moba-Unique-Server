using System;
using System.Reflection;
using UnityEngine;

namespace Services
{
    /// <summary>
    /// ��ͨ��Get��ȡ�̳д�����������ÿ����Ķ���Ӧ����Ψһ��
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
        /// �����ʼ����ɺ���ô˷���
        /// </summary>
        protected internal virtual void AfterInitailize()
        {
            
        }

        // ��ֹ��д�˷���
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
    ///�Զ���ȡ�������񣬽�����Service�����࣬�����ڷ����ʼ���׶�
    /// </summary>
    [AttributeUsage(AttributeTargets.Field)]
    internal class OtherAttribute : Attribute
    {
        internal Type type;

        /// <param name="_type">��ȡB��ʵ������ֵ��A��ʱ��B��̳�A�ࣩ����Ҫָ��B�������Ϊ����</param>
        public OtherAttribute(Type _type = null)
        {
            type = _type;
        }
    }
}