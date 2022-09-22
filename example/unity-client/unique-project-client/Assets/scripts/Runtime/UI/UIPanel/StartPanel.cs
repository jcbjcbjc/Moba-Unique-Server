
using System;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
namespace UI {
	public class StartPanel : BaseUIForm
	{
		Button _loginButton;
		Button _tutorButton;
		Button _StartSetting;
		Button _startaudio;

		private void Awake()
		{

		}
		void Start()
		{
			_loginButton = GameObject.Find("StartButton").GetComponent<Button>();
			_tutorButton = GameObject.Find("TutorPlay").GetComponent<Button>();
			_StartSetting = GameObject.Find("StartSetting").GetComponent<Button>();
			_startaudio = GameObject.Find("StartAudio").GetComponent<Button>();

			_loginButton.onClick.AddListener(() => {
				OnStartClick();
			});
			_tutorButton.onClick.AddListener(() =>
			{
				OnStartTutor();
			});
			_StartSetting.onClick.AddListener(() =>
			{
				OnSetting();
			});
			_startaudio.onClick.AddListener(() =>
			{
				OnAudio();
			});
		}

		void OnStartClick()
		{

			CloseUIForm();
		}
		void OnStartTutor()
		{

		}
		void OnSetting()
		{

		}
		void OnAudio()
		{

		}

		
	}

}
