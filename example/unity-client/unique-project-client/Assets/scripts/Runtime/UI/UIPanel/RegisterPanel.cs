
using System;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

namespace UI
{
	public class RegisterPanel : BaseUIForm
	{
		Button _registerButton;
		Button _closeButton;
		InputField _nameInput;
		InputField _pwdInput;
		InputField _repeatPwdInput;

		private void Awake()
		{

		}
		void Start()
		{
			_registerButton = transform.Find("RegisterButton").GetComponent<Button>();
			_closeButton = transform.Find("CloseButton").GetComponent<Button>();
			_nameInput = transform.Find("NameInput").GetComponent<InputField>();
			_pwdInput = transform.Find("PwdInput").GetComponent<InputField>();
			_repeatPwdInput = transform.Find("RepeatPwdInput").GetComponent<InputField>();

			_registerButton.onClick.AddListener(() =>
			{
				OnRegisterClick();
			});
			_closeButton.onClick.AddListener(() =>
			{
				OnCloseClick();
			});
		}

		void OnRegisterClick()
		{
			if (_nameInput.text == "")
			{
				//Game.Instance.ShowMessage ("用户名不能为空");
				return;
			}
			if (_pwdInput.text == "")
			{
				//Game.Instance.ShowMessage ("密码不能为空");
				return;
			}
			if (_pwdInput.text != _repeatPwdInput.text)
			{
				//Game.Instance.ShowMessage ("两次密码输入不一致");
				return;
			}
			//NetConn.Instance.Send (ProConst.REGISTER_MESSAGE, new LoginData(_nameInput.text, _pwdInput.text));
		}

		void OnCloseClick()
		{

			CloseUIForm();
		}
		
	}
}