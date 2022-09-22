using System;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;



namespace UI
{
	public class RoomItem : BaseUIForm
	{

		public Text username;
		public Text maxcount;
		public Text count;
		public Text gamepattern;
		public Button _joinRoomButton;

		void Start()
		{

		}

		public void OnJoinRoomClick()
		{

		}



		string _userName = "";
		string _count = "";
		string _maxcount = "";
		int game = -1;
		void Update()
		{

			if (_userName != "" && _count != "" && _maxcount != "")
			{
				username.text = _userName;
				maxcount.text = "/" + _maxcount;
				//_totalCountText.text.Replace ("\n", "\\n");
				count.text = _count;
				gamepattern.text = "";
			}
		}




		
	}
}
