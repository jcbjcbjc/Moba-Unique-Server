using System;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using Services;
using NetWork;

namespace UI
{
	public class GameOver : BaseUIForm
	{
		EventSystem eventSystem;

		public Button ReturnRoom;
		public Button QuitButton;

		private void Awake()
		{
			eventSystem = ServiceLocator.Get<EventSystem>();
		}

		void Start()
		{

		}

		public void OnClickReturn()
		{
			ServiceLocator.Get<GameLogicService>().SendGameOver();
			ServiceLocator.Get<RoomService>().SendGameOver2();
		}
		public void OnClickQuit()
		{

		}


		
	}
}