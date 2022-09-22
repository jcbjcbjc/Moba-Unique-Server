using Services;
using System.Collections;
using UnityEngine;
using UnityEngine.Events;
using UnityEngine.SceneManagement;

public class SceneControllerBase : Service
{
    [Other]
    protected EventSystem eventSystem;
    
    public static int Index => SceneManager.GetActiveScene().buildIndex;
    public static string SceneName => SceneManager.GetActiveScene().name;

    /// <summary>
    /// �Ƿ��첽���س���
    /// </summary>
    public bool async;

    /// <summary>
    /// ��ʼ�첽���س���ʱ�������첽����
    /// </summary>
    public event UnityAction<AsyncOperation> AsyncLoadScene;

    public void LoadScene(string name)
    {
        StartCoroutine(LoadSceneProcess(name));
    }
    public void LoadScene(int index)
    {
        StartCoroutine(LoadSceneProcess(index));
    }
    public void LoadNextScene()
    {
        StartCoroutine(LoadSceneProcess(Index + 1));
    }
    public void Quit()
    {
#if UNITY_EDITOR
        UnityEditor.EditorApplication.isPlaying = false;
#else
        Application.Quit();
#endif
    }

    /// <param name="confirm">���ص�90%ʱ�Ƿ���Ҫȷ��</param>    
    private IEnumerator LoadSceneProcess(int index, bool confirm = false)
    {
        eventSystem.Invoke(EEvent.BeforeLoadScene, index);
        if (async)
        {
            AsyncOperation operation = SceneManager.LoadSceneAsync(index);
            AsyncLoadScene?.Invoke(operation);
            operation.allowSceneActivation = !confirm;
            yield return operation;
        }
        else
        {
            SceneManager.LoadScene(index);
        }
        yield return null;
        eventSystem.Invoke(EEvent.AfterLoadScene, Index);
    }

    /// <param name="confirm">���ص�90%ʱ�Ƿ���Ҫȷ��</param>    
    private IEnumerator LoadSceneProcess(string name, bool confirm = false)
    {
        eventSystem.Invoke(EEvent.BeforeLoadScene, Index);
        if (async)
        {
            AsyncOperation operation = SceneManager.LoadSceneAsync(name);
            AsyncLoadScene?.Invoke(operation);
            operation.allowSceneActivation = !confirm;
            yield return operation;
        }
        else
        {
            SceneManager.LoadScene(name);
        }
        yield return null;
        eventSystem.Invoke(EEvent.AfterLoadScene, Index);
    }
}
