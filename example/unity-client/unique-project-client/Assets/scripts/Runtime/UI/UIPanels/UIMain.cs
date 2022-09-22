
using NetWork;
using Services;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace UI
{
    public class UIMain : BaseUIForm
    {
        private EventSystem eventSystem;

        private void Awake()
        {
            eventSystem = ServiceLocator.Get<EventSystem>();
            eventSystem.AddListener(EEvent.OnEnterGameProcess, () =>
            {
                CloseUIForm();
            });
        }
        private void Start()
        {
            RigisterButtonObjectEvent("MatchButton", Match);
        }

        public void setMsg(string msg)
        {
            //this.msgLabel.string = msg;
        }

        
        public void Match(GameObject go)
        {
            ServiceLocator.Get<MatchService>().SendStartMatch();
        }
    }
}

