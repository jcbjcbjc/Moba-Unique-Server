//
// @brief: 公用数据类
// @version: 1.0.0
// @author helin
// @date: 03/7/2018
// 
// 
//


using C2BNet;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using static Assets.scripts.Utils.enums.BattleModeEnum;
using static Assets.scripts.Utils.enums.GameStatusEnum;

public class GameData {
    #region Data For Steplock
    //player frame operations
    public static FrameHandlesFromClient frameHandles = new FrameHandlesFromClient();

    //next operation Id
    public static int NextOperationId = 1;

    //已经处理的帧
    public static int handleFrameId = -1;

    //已经执行的帧
    public static int executeFrameId = -1;

    //all frame operations                         
    public static SortedDictionary<int, IList<FrameHandle>> allFrameHandles = new SortedDictionary<int, IList<FrameHandle>>();  //所有的帧操作

    //predicted Input for player
    public static List<FrameHandle> PredictedInput = new List<FrameHandle>();

    //补帧等待帧数
    public static int repairWaitFrameCount = 5 * 7;

    //当前执行补帧
    public static int currentRepairFrame = 0;  

    //最新帧
    public static int newFrameId = -1;

    //直播未执行帧数
    public static int liveNotExecuteFrameCount = 0;  

    //最后接收帧时间
    public static float lastReceiveFrameTime = 0;

    //最后抽查时间
    public static float lastCheckFrameTime = 0; 

    #endregion

    #region Data For GameStatus
    //Game status
    public static GameStatus gameStatus = GameStatus.None;

    //Game Control
    public static bool isInGame = false;

    #endregion

    #region Data For GameConfig
    //Game Mode
    public static BattleMode battleMode = BattleMode.None;
    #endregion
    

    

    public struct battleInfo
    {
        public int uGameFrame;
        public string sckeyEvent;
    }

    public static void release_data_for_steplock() {
        GameData.NextOperationId = 1;
        
         handleFrameId = -1;

         executeFrameId = -1;
      
         allFrameHandles = new SortedDictionary<int, IList<FrameHandle>>();  

         PredictedInput = new List<FrameHandle>();

         repairWaitFrameCount = 5 * 7;

         currentRepairFrame = 0;

         newFrameId = -1;

         liveNotExecuteFrameCount = 0;

         lastReceiveFrameTime = 0;

         lastCheckFrameTime = 0;
    }
    public static void release_data_for_gamestatus()
    {
        gameStatus = GameStatus.None;
        isInGame = false;
    }

    //- 释放资源
    // 
    // @return none
    public static void release() {

        release_data_for_steplock();
        release_data_for_gamestatus();
    }
}
