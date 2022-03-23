package com.tattoed.snowy.beans;

import java.util.ArrayList;
import java.util.Random;

public class Warehouse {

    public static final int ROBOT_REQUIRED_FOO = 6;
    public static final int ROBOT_REQUIRED_EURO = 3;
    public static final int FOOBAR_MAX_SELL = 5;
    public static final int RATE_ASSEMBLE_SUCCESS = 60;
    private final ArrayList<String> foo;
    private final ArrayList<String> bar;
    private final ArrayList<String> foobar;
    private final ArrayList<String> soldFooBar;
    private int euro;
    private int fooLabelCounter;
    private int barLabelCounter;

    Warehouse() {
        foo = new ArrayList<>();
        bar = new ArrayList<>();
        foobar = new ArrayList<>();
        soldFooBar = new ArrayList<>();
        euro = 0;
        fooLabelCounter = 0;
        barLabelCounter = 0;
    }

    public ArrayList<String> getSoldFooBar() {
        return soldFooBar;
    }

    public ArrayList<String> getFoo() {
        return foo;
    }

    public ArrayList<String> getBar() {
        return bar;
    }

    public ArrayList<String> getFoobar() {
        return foobar;
    }

    public int getEuro() {
        return euro;
    }

    private String getLabel(String preLabel, int counter) {
        return preLabel + String.format("%04d", counter);
    }

    private void removeItems(ArrayList<String> list, int count) {
        if (count > 0) {
            list.subList(0, count).clear();
        }
    }

    private void transferFoobar(int count) {
        if (count > 0) {
            soldFooBar.addAll(foobar.subList(0, count));
            foobar.subList(0, count).clear();
        }
    }

    public boolean mineFoo(String robotName) {
        fooLabelCounter++;
        foo.add(robotName + getLabel("F", fooLabelCounter));
        return true;
    }

    public boolean mineBar(String robotName) {
        barLabelCounter++;
        bar.add(robotName + getLabel("B", barLabelCounter));
        return true;
    }

    synchronized public boolean assembleFooBar(String robotName) {
        if (canAssembleFoobar()) {
            Random r = new Random();
            boolean success = r.nextInt(100) <= RATE_ASSEMBLE_SUCCESS;
            if (success) {
                foobar.add(robotName + foo.get(0) + bar.get(0));
                foo.remove(0);
                bar.remove(0);
                return true;
            } else {
                removeItems(foo, 1);
                return false;
            }
        }
        return false;
    }

    synchronized public boolean sellFoobar() {
        int amountToSell = getSellableFoobar();
        if (amountToSell > 0) {
            transferFoobar(amountToSell);
            euro = euro + amountToSell;
            return true;
        }
        return false;
    }

    synchronized public boolean buyRobot() {
        if (canBuyRobot()) {
            removeItems(foo, ROBOT_REQUIRED_FOO);
            euro = euro - ROBOT_REQUIRED_EURO;
            return true;
        }
        return false;
    }

    boolean canBuyRobot() {
        return foo.size() >= ROBOT_REQUIRED_FOO && euro >= ROBOT_REQUIRED_EURO;
    }

    boolean canAssembleFoobar() {
        return foo.size() >= 7 && bar.size() >= 1;
    }

    int getSellableFoobar() {
        return (foobar.size() - FOOBAR_MAX_SELL) < 0 ? foobar.size() : 5;
    }

    boolean fooRequired() {
        return bar.size() + 6 > foo.size();
    }
}
