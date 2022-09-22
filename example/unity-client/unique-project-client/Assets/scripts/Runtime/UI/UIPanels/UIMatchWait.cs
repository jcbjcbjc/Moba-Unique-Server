using Services;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace UI
{
    public class UIMatchWait:BaseUIForm
    {
        private EventSystem eventSystem;

        private void Awake()
        {
            eventSystem = ServiceLocator.Get<EventSystem>();
        }
       
    }
}
