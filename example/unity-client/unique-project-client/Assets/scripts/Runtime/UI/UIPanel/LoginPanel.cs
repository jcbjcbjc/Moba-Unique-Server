
using System;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
namespace UI
{
	public class LoginPanel : BaseUIForm
	{
		Button _loginButton;
		Button _registerButton;
		Button _closeButton;
		InputField _nameInput;
		InputField _pwdInput;

		void Awake()
		{

		}

		void Start()
		{
			RigisterButtonObjectEvent("LoginButton", OnLoginClick);
			RigisterButtonObjectEvent("RegisterButton", OnRegisterClick);
			RigisterButtonObjectEvent("CloseButton", OnCloseClick);


			_nameInput = transform.Find("NameInput").GetComponent<InputField>();
			_pwdInput = transform.Find("PwdInput").GetComponent<InputField>();


		}

		void OnLoginClick(GameObject go)
		{
			if (_nameInput.text == "")
			{
				string[] strArray = new string[] { "用户名不能为空", "2" };
				//SendMessage(ProConst.SHOW_MESSAGE_MESSAGE, "", strArray);
				return;
			}
			if (_pwdInput.text == "")
			{
				string[] strArray = new string[] { "密码不能为空", "2" };
				//SendMessage(ProConst.SHOW_MESSAGE_MESSAGE, "", strArray);
				return;
			}

		}

		void OnRegisterClick(GameObject go)
		{

			CloseUIForm();
		}

		void OnCloseClick(GameObject go)
		{

			CloseUIForm();
		}

		
	}
}