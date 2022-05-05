package com.game.netty;

public class CSKCPServer {
    public void bind(int port){
        CKCPServer s = new CKCPServer(port, 1);
        s.noDelay(1, 10, 2, 1);
        s.setMinRto(10);
        s.wndSize(64, 64);
        s.setTimeout(10 * 1000);
        s.setMtu(512);
        s.start();
        System.out.println("BattleServer start successfully");
    }
}
