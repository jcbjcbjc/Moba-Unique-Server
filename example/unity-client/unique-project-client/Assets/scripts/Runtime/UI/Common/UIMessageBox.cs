using Managers;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Services;
namespace UI
{
    public class UIMessageBox : BaseUIForm
    {

        EventSystem eventSystem;

        private void Awake()
        {
            eventSystem = ServiceLocator.Get<EventSystem>();

            base.CurrentUIType.UIForms_Type = UIFormType.PopUp;

            base.CurrentUIType.UIForm_LucencyType = UIFormLucenyType.Translucence/*.Pentrate*/;
        }
        public void update(string message, string title = "", MessageBoxType type = MessageBoxType.Information, string btnOK = "", string btnCancel = "")
        {



        }
        
        private void OnClickYes()
        {
            //console.log('OnClickYes')
            //SoundManager.Instance.PlaySound(SoundDefine.SFX_UI_Confirm);

            eventSystem.Invoke(EEvent.UIMessageBox_OnClickYes);

            CloseUIForm();
        }

        private void OnClickNo()
        {
            //console.log('OnClickNo'+this.uuid)
            //SoundManager.Instance.PlaySound(SoundDefine.SFX_UI_Win_Close);
            eventSystem.Invoke(EEvent.UIMessageBox_OnClickNo);
            CloseUIForm();
        }

        

    }

    public enum MessageBoxType
    {
        /// <summary>
        /// Information Dialog with OK button
        /// </summary>
        Information = 1,

        /// <summary>
        /// Confirm Dialog whit OK and Cancel buttons
        /// </summary>
        Confirm = 2,

        /// <summary>
        /// Error Dialog with OK buttons
        /// </summary>
        Error = 3
    }
}
