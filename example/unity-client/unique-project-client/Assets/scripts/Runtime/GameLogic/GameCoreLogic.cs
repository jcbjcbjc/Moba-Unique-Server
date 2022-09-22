using System;
using System.Collections.Generic;

using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Managers;
using C2BNet;
using UnityEngine;
using static Assets.scripts.Utils.enums.BattleModeEnum;

namespace GameLogic
{
    public class GameCoreLogic
    {
        CharacterManager characterManager_;
        EntityManager entityManager_;

        public void Init(CharacterManager characterManager, EntityManager entityManager) { 
            this.characterManager_ = characterManager;
            this.entityManager_ = entityManager;
        }

        public void update(IList<FrameHandle> frameHandles)
        {
            updateLogic(frameHandles);
        }
        private void updateLogic(IList<FrameHandle> frameHandles)
        {
            recordLastPos();

            var characterList = characterManager_.GetCharacterList();

            //foreach (FrameHandle fh in frameHandles)
            //{
            //    if (fh.UserId == User.Instance.user.Id)
            //    {
            //        foreach (FrameHandle frameHandle in GameLogicManager.PredictedInput) {
            //            if (frameHandle.OpretionId == fh.OpretionId) {
            //                GameLogicManager.PredictedInput.Remove(frameHandle);
            //            }
            //        }
            //    }
            //}
            foreach (var character in characterList)
            {
                foreach (FrameHandle fh in frameHandles)
                {
                    if (fh.UserId == character.user.Id)
                    {
                        character.update(fh);
                    }
                }
            }
        }

        private void recordLastPos()
        {

        }
    }
}
