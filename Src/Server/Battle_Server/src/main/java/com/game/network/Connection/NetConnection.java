package com.game.network.Connection;

import com.game.network.proto.C2BNet;

public interface NetConnection {
     C2BNet.C2BNetMessageResponse.Builder getResponse() ;
    void sendFrameHandleRes(C2BNet.C2BNetMessageResponse.Builder message2) ;
    void sendLiveFrameRes(C2BNet.C2BNetMessageResponse.Builder message2);
    void send();
    void send(C2BNet.C2BNetMessage.Builder message2);
}
