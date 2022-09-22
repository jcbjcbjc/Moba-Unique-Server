using UI;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI;

namespace Managers
{
    public class TipsManager
    {
        static TipsManager _instance;
        public static TipsManager Instance
        {
            get
            {
                if (_instance == null)
                {
                    _instance = new TipsManager();
                }
                return _instance;
            }
        }
        public void showTips(string msg) { 
            //TODO 


        }

        public Task<UIMessageBox> Show(string message, string title = "", MessageBoxType type = MessageBoxType.Information, string btnOK = "", string btnCancel = "")
        {
            var task = Task.Run(() => {
                UIMessageBox baseUIForm = null;
                baseUIForm = (UIMessageBox)UIManager.GetInstance().ShowUIForms("UIMessageBox");
                baseUIForm.update(message, title, type, btnOK, btnCancel);
                return baseUIForm;
            });
            return task;
        }
    }
}
