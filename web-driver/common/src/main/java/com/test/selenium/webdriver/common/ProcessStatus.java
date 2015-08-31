package com.test.selenium.webdriver.common;



public class ProcessStatus {

    /**
     * Sum of private process memory by process and all its child processes
     */
    private final int uss;

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


    public ProcessStatus(int uss, double cpu, int processes, int time) {
        this.uss = uss;
        this.cpu = cpu;
        this.processes = processes;
        this.time = time;
    }


    public int getUss() {
        return uss;
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
                "uss=" + uss +
                ", cpu=" + cpu +
                ", processes=" + processes +
                ", time=" + time +
                '}';
    }
}