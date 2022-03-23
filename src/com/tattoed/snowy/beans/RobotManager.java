package com.tattoed.snowy.beans;

import com.tattoed.snowy.enums.RobotTask;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class RobotManager {
    public static final int MAX_ROBOT_COUNT = 30;
    private final ArrayList<Robot> listRobot = new ArrayList<>();
    private final LocalDateTime startTime;
    private final Warehouse warehouse;

    private RobotManager(int initialRobotCount) {
        startTime = LocalDateTime.now();
        warehouse = new Warehouse();
        for (int i = 0; i < initialRobotCount; i++) {
            listRobot.add(new Robot(generateNameForRobot(i), this, warehouse));
        }
    }

    public static RobotManager getInstance() {
        return LoadRobotManager.INSTANCE;
    }

    public ArrayList<Robot> getListRobot() {
        return listRobot;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    private String generateNameForRobot(int id) {
        return "R" + String.format("%02d", id);
    }

    synchronized RobotTask getNextBestTask() {
        updateStatus();
        if (listRobot.size() >= MAX_ROBOT_COUNT || existsRobot(RobotTask.BUY_ROBOT) && listRobot.size() >= MAX_ROBOT_COUNT - 1) {
            return RobotTask.STOP;
        } else if (warehouse.canBuyRobot() && !existsRobot(RobotTask.BUY_ROBOT)) {
            return RobotTask.BUY_ROBOT;
        } else if (warehouse.getSellableFoobar() > 0 && !existsRobot(RobotTask.SELL_FOOBAR)) {
            return RobotTask.SELL_FOOBAR;
        } else if (warehouse.canAssembleFoobar() && (countRobot(RobotTask.ASSEMBLE) * 2 < countRobot(RobotTask.MINE_BAR) + countRobot(RobotTask.MINE_FOO))) {
            return RobotTask.ASSEMBLE;
        } else if (warehouse.fooRequired() || (countRobot(RobotTask.MINE_BAR) >= countRobot(RobotTask.MINE_FOO))) {
            return RobotTask.MINE_FOO;
        } else {
            return RobotTask.MINE_BAR;
        }
    }

    synchronized void updateStatus() {
        Reporter.printManagerReport(this);
    }

    private boolean existsRobot(RobotTask task) {
        Robot robot = listRobot.stream()
                .filter(x -> task.equals(x.getMyTask()))
                .findAny()
                .orElse(null);
        return robot != null;
    }

    private long countRobot(RobotTask task) {
        return listRobot.stream()
                .filter(x -> task.equals(x.getMyTask()))
                .count();
    }

    void addNewRobot() {
        Robot robot = new Robot(generateNameForRobot(listRobot.size()), this, warehouse);
        listRobot.add(robot);
        robot.start();
    }

    public void work() throws InterruptedException {
        for (Robot robot : listRobot) {
            robot.start();
        }
    }


    private static class LoadRobotManager {
        static final RobotManager INSTANCE = new RobotManager(2);
    }
}
