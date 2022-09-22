using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Assets.scripts.Utils.enums
{
    public class HandlerFrameResultEnum
    {
        public enum HandlerFrameResult
        {
            NoFrameData = 0,  //无帧数据
            NotRepeatFrame,  //不能重复执行帧
            Success   //执行成功
        }
    }
}
