using UnityEngine.Events;

namespace MyStateMathine
{
    public class StateMathine
    {
        private readonly StateMapper mapper;
        public UnityAction<int> StateChange;

        private State state;
        private int stateIndex;
        public int StateIndex
        {
            get => stateIndex;
            set
            {
                if (stateIndex != value)
                {
                    //可以不依赖StateInitializer和State使用
                    if (mapper != null)
                    {
                        State newState = mapper.GetState(value);
                        state?.OnExit(stateIndex);
                        state = newState;
                        newState?.OnEnter(value);
                    }
                    StateChange?.Invoke(value);
                    stateIndex = value;
                }
            }
        }

        public StateMathine(StateMapper _mapper = null)
        {
            mapper = _mapper;
            stateIndex = -1;
        }
    }
}
