namespace MyStateMathine
{
    public class State
    {
        protected GameCycle gameCycle;
        protected StateMathine stateMathine;
        protected internal int enumIndex;

        public State()
        {
            gameCycle = GameCycle.Instance;
        }

        public void Initialize(StateMathine mathine)
        {
            stateMathine = mathine;
        }

        protected internal virtual void OnEnter(int enumIndex)
        {
            gameCycle.AttachToGameCycle(EInvokeMode.Update, Update);
            gameCycle.AttachToGameCycle(EInvokeMode.FixedUpdate, FixedUpdate);
            gameCycle.AttachToGameCycle(EInvokeMode.LateUpdate, LateUpdate);
        }

        protected internal virtual void OnExit(int enumIndex)
        {
            gameCycle.RemoveFromGameCycle(EInvokeMode.Update, Update);
            gameCycle.RemoveFromGameCycle(EInvokeMode.FixedUpdate, FixedUpdate);
            gameCycle.RemoveFromGameCycle(EInvokeMode.LateUpdate, LateUpdate);
        }

        protected virtual void Update() { }
        protected virtual void FixedUpdate() { }
        protected virtual void LateUpdate() { }
    }
}