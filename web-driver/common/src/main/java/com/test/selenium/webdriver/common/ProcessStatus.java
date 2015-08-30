package com.test.selenium.webdriver.common;



public class ProcessStatus {

    /**
     * Sum of rss memory usage by process and all its child processes
     */
    private final int rss;

    /**
     * CPU usage in percent by process and all its child processes
     */
    private final double cpu;

    /**
     * Number of processes
     */
    private final int processes;

    /**
     * Time since driver created
     */
    private final int time;


    public ProcessStatus(int rss, double cpu, int processes, int time) {
        this.rss = rss;
        this.cpu = cpu;
        this.processes = processes;
        this.time = time;
    }


    public int getRss() {
        return rss;
    }

    public double getCpu() {
        return cpu;
    }

    public int getProcesses() {
        return processes;
    }

    public int getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "ProcessStatus{" +
                "rss=" + rss +
                ", cpu=" + cpu +
                ", processes=" + processes +
                ", time=" + time +
                '}';
    }
}