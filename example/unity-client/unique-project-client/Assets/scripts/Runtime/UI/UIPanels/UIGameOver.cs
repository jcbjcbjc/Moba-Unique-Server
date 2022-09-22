
using Services;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace UI
{
    public class UIGameOver:BaseUIForm
    {
        private EventSystem eventSystem;

        private void Awake()
        {
            eventSystem = ServiceLocator.Get<EventSystem>();
        }
    }
}
