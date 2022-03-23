package com.tattoed.snowy.beans;

import com.tattoed.snowy.enums.RobotState;
import com.tattoed.snowy.enums.RobotTask;

import java.util.Random;

public class Robot extends Thread {
    public static final int DURATION_MOVING = 5000;
    public static final int DURATION_BAR_MAX = 2000;
    public static final int DURATION_BAR_MIN = 500;
    public static final int DURATION_FOO = 1000;
    public static final int DURATION_ASSEMBLE = 2000;
    public static final int DURATION_SELLING = 10000;
    private final RobotManager manager;
    private final Warehouse warehouse;
    private final String name;
    private Thread t;
    private RobotState state;
    private RobotTask task;

    Robot(String name, RobotManager manager, Warehouse warehouse) {
        this.name = name;
        this.manager = manager;
        this.warehouse = warehouse;
        this.state = RobotState.GETTING_NEW_TASK;
    }

    public String getMyName() {
        return name;
    }

    public RobotTask getMyTask() {
        return task;
    }

    public RobotState getMyState() {
        return state;
    }

    private boolean executeTask(RobotTask task) throws Exception {
        return switch (task) {
            case MINE_BAR -> mineBar();
            case MINE_FOO -> mineFoo();
            case ASSEMBLE -> assemble();
            case SELL_FOOBAR -> sellFooBar();
            case BUY_ROBOT -> buyRobot();
            default -> throw new Exception("Bad command or file name :) ");
        };
    }

    private void move() throws InterruptedException {
        state = RobotState.MOVING;
        manager.updateStatus();
        Thread.sleep(DURATION_MOVING);
    }

    private boolean mineBar() throws InterruptedException {
        move();
        state = RobotState.MINING_BAR;
        manager.updateStatus();
        Random r = new Random();
        Thread.sleep(r.nextInt(DURATION_BAR_MAX - DURATION_BAR_MIN) + DURATION_BAR_MIN);
        return warehouse.mineBar(name);
    }

    private boolean mineFoo() throws InterruptedException {
        move();
        state = RobotState.MINING_FOO;
        manager.updateStatus();
        Thread.sleep(DURATION_FOO);
        return warehouse.mineFoo(name);
    }

    private boolean assemble() throws InterruptedException {
        move();
        state = RobotState.ASSEMBLING;
        manager.updateStatus();
        Thread.sleep(DURATION_ASSEMBLE);
        return warehouse.assembleFooBar(name);
    }

    private boolean sellFooBar() throws InterruptedException {
        move();
        state = RobotState.SELLING_FOOBAR;
        manager.updateStatus();
        Thread.sleep(DURATION_SELLING);
        return warehouse.sellFoobar();
    }

    private boolean buyRobot() throws InterruptedException {
        move();
        state = RobotState.BUYING_ROBOT;
        manager.updateStatus();
        boolean result = warehouse.buyRobot();
        if (result) {
            manager.addNewRobot();
        }
        return result;
    }

    public void run() {
        try {
            task = manager.getNextBestTask();
            while (task != RobotTask.STOP) {
                executeTask(task);
                task = null;
                state = RobotState.GETTING_NEW_TASK;
                task = manager.getNextBestTask();
            }
            state = RobotState.STOPPED;
        } catch (Exception e) {
            System.out.println("Robot " + name + " is interrupted. The reason is: " + e.getMessage());
        }
        manager.updateStatus();

    }

    public void start() {
        if (t == null) {
            t = new Thread(this, name);
            t.start();
        }
    }
}
