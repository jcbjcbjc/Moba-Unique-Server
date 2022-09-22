using UnityEditor;
using UnityEditor.SceneManagement;
using UnityEngine;

public static class MenuTools
{
    [MenuItem("Tools/Scene/Scene0 _`")]
    public static void OpenScene0()
    {
        EditorSceneManager.OpenScene("Assets/Scenes/0.unity");
        SceneAsset asset = AssetDatabase.LoadAssetAtPath<SceneAsset>("Assets/Scenes/0.unity");
        ProjectWindowUtil.ShowCreatedAsset(asset);
    }

}