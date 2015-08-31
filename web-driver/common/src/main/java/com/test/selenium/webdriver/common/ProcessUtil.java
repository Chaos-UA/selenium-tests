package com.test.selenium.webdriver.common;


import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        AtomicInteger memory = new AtomicInteger(0);
        class Methods {
            void calculate(int id) {
                Result result = execute(String.format("ps -p %s -o %%cpu", id));
                if (result.getExitCode() != 0) {
                    throw new RuntimeException(result.toString() + "\nid="+id);
                }
                String[] lines = result.getOutput().split("\n");
                if (lines.length != 2) {
                    throw new RuntimeException("Should contain 2 lines");
                }
                if (!lines[0].equals("%CPU")) {
                    throw new RuntimeException();
                }
                lines[1] = lines[1].trim();
                cpu.set(cpu.get() + Double.parseDouble(lines[1].trim()));
                memory.set(memory.get() + getPrivateMemory(id));
            }
        }
        Methods methods = new Methods();
        Set<Integer> ids = getAllProcessIds(processId);
        for (int id : ids) {
            methods.calculate(id);
        }

        //
        return new ProcessStatus(memory.get(), cpu.get(), ids.size(), time);
    }

    public static int getPrivateMemory(int pid) {
        int privateDirty = 0;
        Result result = execute(String.format("cat /proc/%s/smaps", pid));
        if (result.getExitCode() != 0) {
            throw new RuntimeException();
        }
        Pattern pattern = Pattern.compile("Private_Dirty: +(\\d+)");
        Matcher matcher = pattern.matcher(result.getOutput());
        while (matcher.find()) {
            privateDirty += Integer.parseInt(matcher.group(1));
        }
        return privateDirty;
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
