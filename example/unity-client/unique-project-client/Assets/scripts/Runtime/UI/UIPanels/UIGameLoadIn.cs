using Services;
namespace UI 
{
    public class UIGameLoadIn : BaseUIForm
    {
        private EventSystem eventSystem;

        private void Awake()
        {
            eventSystem = ServiceLocator.Get<EventSystem>();
        }

        public override void Display()
        {
            base.Display();
            eventSystem.AddListener<string>(EEvent.GameLoadIn, SetMsg);
        }

        public void SetMsg(string msg)
        {
            //this.msgLabel.string = msg;
        }

        
        protected internal override void CloseUIForm()
        {
            eventSystem.RemoveListener<string>(EEvent.GameLoadIn, SetMsg);
            base.CloseUIForm();
        }
    }
}
