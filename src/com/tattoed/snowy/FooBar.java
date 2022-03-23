package com.tattoed.snowy;

import com.tattoed.snowy.beans.RobotManager;

public class FooBar {

    public static void main(String[] args) throws InterruptedException {
        RobotManager manager = new RobotManager(2);
        manager.work();
    }
}
