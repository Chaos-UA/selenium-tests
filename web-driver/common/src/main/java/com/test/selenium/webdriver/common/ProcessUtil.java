package com.test.selenium.webdriver.common;


import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ProcessUtil {
    public static Result execute(String cmd) {
        try {
            CommandLine cmdLine = CommandLine.parse(cmd);
            DefaultExecutor executor = new DefaultExecutor();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayOutputStream errorOutputStream = new ByteArrayOutputStream();
            executor.setStreamHandler(new PumpStreamHandler(outputStream, errorOutputStream));
            executor.setExitValues(null);
            int resultCode = executor.execute(cmdLine);
            return new Result(resultCode, outputStream.toString(), errorOutputStream.toString());
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static ProcessStatus getProcessStatusWithSubprocesses(int processId, int time) {
        AtomicReference<Double> cpu = new AtomicReference<>(0D);
        AtomicInteger rss = new AtomicInteger(0);
        class Methods {
            void calculate(int id) {
                Result result = execute(String.format("ps -p %s -o %%cpu,rss", id));
                if (result.getExitCode() != 0) {
                    throw new RuntimeException(result.toString());
                }
                String[] lines = result.getOutput().split("\n");
                if (lines.length != 2) {
                    throw new RuntimeException("Should contain 2 lines");
                }
                if (!lines[0].equals("%CPU   RSS")) {
                    throw new RuntimeException();
                }
                lines[1] = lines[1].trim();
                String[] cols = lines[1].split("[ ]+");
                cpu.set(Double.parseDouble(cols[0]) + cpu.get());
                rss.set(rss.get() + Integer.parseInt(cols[1]));
            }
        }
        Methods methods = new Methods();
        Set<Integer> ids = getAllProcessIds(processId);
        for (int id : ids) {
            methods.calculate(id);
        }
        return new ProcessStatus(rss.get(), cpu.get(), ids.size(), time);
    }

    /**
     * @return process id and children ids
     */
    public static Set<Integer> getAllProcessIds(int processId) {
        HashSet<Integer> ids = new HashSet<>();
        ids.add(processId);
        Result result = execute(String.format("pgrep -P %s", processId));
        if (result.getExitCode() == 1) {
            return ids;
        }
        for (String id : result.getOutput().split("\n")) {
            ids.addAll(getAllProcessIds(Integer.parseInt(id)));
        }
        return ids;
    }

    public static class Result {
        private final int exitCode;
        private final String output;
        private final String errorOutput;

        public Result(int exitCode, String output, String errorOutput) {
            this.exitCode = exitCode;
            this.output = output;
            this.errorOutput = errorOutput;
        }

        public int getExitCode() {
            return exitCode;
        }

        public String getOutput() {
            return output;
        }

        public String getErrorOutput() {
            return errorOutput;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "exitCode=" + exitCode +
                    ", output='" + output + '\'' +
                    ", errorOutput='" + errorOutput + '\'' +
                    '}';
        }
    }
}
