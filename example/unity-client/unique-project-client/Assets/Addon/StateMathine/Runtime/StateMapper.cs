using System;
using System.Collections.Generic;
using UnityEngine;

namespace MyStateMathine
{
    //����ö��ֵ��״̬���ӳ�䣬����ʼ��״̬
    public class StateMapper
    {
        protected Dictionary<int, State> stateDict = new Dictionary<int, State>();
        protected StateMathine stateMathine;

        public void Initialize(StateMathine mathine)
        {
            stateMathine = mathine;
        }

        /// <summary>
        /// ͨ��State���ͺ�ö�����Ϳ��ٴ���״̬����������״̬��Ϊ��ͬ����;����
        /// </summary>
        public void AddSameStates<T, E>() where T : State where E : Enum
        {
            foreach (E value in Enum.GetValues(typeof(E)))
            {
                T state = Activator.CreateInstance<T>();
                state.enumIndex = Convert.ToInt32(value);
                AddState(state);
            }
        }

        public State GetState(int enumIndex)
        {
            if (stateDict.ContainsKey(enumIndex))
                return stateDict[enumIndex];
            Debug.LogWarning($"δ����{enumIndex}״̬");
            return null;
        }
        public void AddState(State state)
        {
            if (!stateDict.ContainsKey(state.enumIndex))
            {
                stateDict.Add(state.enumIndex, state);
                InitializeState(state);
            }
        }
        public void AddStates(params State[] states)
        {
            foreach (State state in states)
            {
                AddState(state);
            }
        }
        protected virtual void InitializeState(State state)
        {
            state.Initialize(stateMathine);
        }
    }
}