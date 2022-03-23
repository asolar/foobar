package com.tattoed.snowy.beans;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Reporter {

    static void printWarehouseReport(Warehouse warehouse) {
        String leftAlignFormat = "| %-10s | %-8d |%n";

        System.out.format("+------------+----------+%n");
        System.out.format("| Item       | Count    |%n");
        System.out.format("+------------+----------+%n");
        System.out.format(leftAlignFormat, "Foo", warehouse.getFoo().size());
        System.out.format(leftAlignFormat, "Bar", warehouse.getBar().size());
        System.out.format(leftAlignFormat, "FooBar", warehouse.getFoobar().size());
        System.out.format(leftAlignFormat, "Euro", warehouse.getEuro());
        System.out.format("+------------+----------+%n");


        System.out.println("--> Sold Foobars count: " + warehouse.getSoldFooBar().size());
        for (String foobar : warehouse.getSoldFooBar()) {
            System.out.print(foobar + ", ");
        }
        System.out.println();
    }

    private static void printTimeStats(RobotManager manager) {
        LocalDateTime fromDateTime = manager.getStartTime();
        LocalDateTime toDateTime = LocalDateTime.now();

        long minutes = fromDateTime.until(toDateTime, ChronoUnit.MINUTES);
        long seconds = fromDateTime.plusMinutes(minutes).until(toDateTime, ChronoUnit.SECONDS);

        System.out.println("Running for: " + minutes + " minutes " + seconds + " seconds.");
    }

    private static void printRobotReport(RobotManager manager) {
        String leftAlignFormat = "| %-10s | %-15s | %-15s |%n";

        System.out.format("+------------+-----------------+-----------------+%n");
        System.out.format("| Robot      | Task            | State           |%n");
        System.out.format("+------------+-----------------+-----------------+%n");
        for (Robot robot : manager.getListRobot()) {
            System.out.format(leftAlignFormat, robot.getMyName(), robot.getMyTask(), robot.getMyState());
        }
        System.out.format("+------------+-----------------+-----------------+%n");
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.print("");
        System.out.flush();
    }

    static void printManagerReport(RobotManager manager) {
        clearScreen();
        printTimeStats(manager);
        printRobotReport(manager);
        Reporter.printWarehouseReport(manager.getWarehouse());
    }
}
