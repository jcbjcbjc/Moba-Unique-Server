using System;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;


namespace UI
{
	public class RoomPanel : BaseUIForm
	{


		Text _oneUserNameText;
		Text _oneTotalCountText;
		Text _oneWinCountText;

		Text _twoUserNameText;
		Text _twoTotalCountText;
		Text _twoWinCountText;

		public Button _startGameButton;
		Button _leaveRoomButton;
		public Button _readyButton;
		public Text Pattern;
		private void Awake()
		{

		}

		void Start()
		{
			_oneUserNameText = transform.Find("OnePanel/UserNameText").GetComponent<Text>();
			_oneTotalCountText = transform.Find("OnePanel/TotalCountText").GetComponent<Text>();
			_oneWinCountText = transform.Find("OnePanel/WinCountText").GetComponent<Text>();

			_twoUserNameText = transform.Find("TwoPanel/UserNameText").GetComponent<Text>();
			_twoTotalCountText = transform.Find("TwoPanel/TotalCountText").GetComponent<Text>();
			_twoWinCountText = transform.Find("TwoPanel/WinCountText").GetComponent<Text>();

			_startGameButton = transform.Find("StartGameButton").GetComponent<Button>();
			_leaveRoomButton = transform.Find("LeaveRoomButton").GetComponent<Button>();
			_readyButton = transform.Find("ReadyGame").GetComponent<Button>();

			_startGameButton.onClick.AddListener(() =>
			{
				OnStartGameClick();
			});
			_leaveRoomButton.onClick.AddListener(() =>
			{
				OnLeaveRoomClick();
			});
			_readyButton.onClick.AddListener(() =>
			{
				OnReadyClick();
			});
		}

		void OnStartGameClick()
		{

		}

		void OnLeaveRoomClick()
		{

		}
		void OnReadyClick()
		{

		}
		


	}
}