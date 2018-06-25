package com.elastic.support.diagnostics.commands;

import com.elastic.support.diagnostics.chain.DiagnosticContext;
import com.elastic.support.util.SystemProperties;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.*;

public abstract class BaseSystemCallsCmd extends AbstractDiagnosticCmd {


      protected void processCalls(DiagnosticContext context, Map<String, String> osCmds){

         ProcessBuilder pb = getProcessBuilder();

         try {
            Iterator<Map.Entry<String, String>> iter = osCmds.entrySet().iterator();
            while (iter.hasNext()) {
               Map.Entry<String, String> entry = iter.next();
               String cmdLabel = entry.getKey();
               String cmdText = entry.getValue();
               runCommand(context, cmdLabel, cmdText, pb);
            }

         } catch (Exception e) {
            logger.error("Error executing one or more system calls.", e);
         }
      }


   protected String checkOS() {
      String osName = SystemProperties.osName.toLowerCase();
      if (osName.contains("windows")) {
         return "winOS";
      } else if (osName.contains("linux")) {
         return "linuxOS";
      } else if (osName.contains("darwin") || osName.contains("mac os x")) {
         return "macOS";
      } else {
         logger.error("Failed to detect operating system!");
         throw new RuntimeException("Unsupported OS");
      }
   }

   protected Map<String, String> processArgs(Map<String, String> osCmds, String pid, String javaHome) {

      HashMap revMap = new HashMap();
      Iterator<Map.Entry<String, String>> iter = osCmds.entrySet().iterator();
      while (iter.hasNext()) {
         Map.Entry<String, String> entry = iter.next();
         String cmdKey = entry.getKey();
         String cmdText = entry.getValue();
         if (cmdText.contains("PID")) {
            cmdText = cmdText.replace("PID", pid);
         }

         if (cmdText.contains("JAVA_HOME")) {
            cmdText = cmdText.replace("JAVA_HOME", javaHome);
         }

         revMap.put(cmdKey, cmdText);

      }
      return revMap;
    }

   protected void runCommand(DiagnosticContext context,
                           String cmdLabel,
                           String cmdText,
                           ProcessBuilder pb) {

      final List<String> cmds = new ArrayList<>();

      try {
         StringTokenizer st = new StringTokenizer(cmdText, " ");
         while (st.hasMoreTokens()) {
            cmds.add(st.nextToken());
         }
         logger.info("Running: " + cmdText);
         pb.redirectOutput(new File(context.getTempDir() + SystemProperties.fileSeparator + cmdLabel + ".txt"));
         pb.command(cmds);
         Process pr = pb.start();
         pr.waitFor();
      } catch (Exception e) {
         logger.error("Error processing system command:" + cmdLabel);
         try {
            FileOutputStream fos = new FileOutputStream(new File(context.getTempDir() + SystemProperties.fileSeparator + cmdLabel + ".txt"), true);
            PrintStream ps = new PrintStream(fos);
            e.printStackTrace(ps);
         } catch (Exception ie) {
            logger.error("Error processing system command", ie);
         }
      } finally {
         cmds.clear();
      }
   }

   protected ProcessBuilder getProcessBuilder() {
      ProcessBuilder pb = new ProcessBuilder();
      pb.redirectErrorStream(true);
      return pb;
   }

   protected boolean isJdkPresent() {
      try {
         File jdk = Paths.get(SystemProperties.javaHome, "bin", "javac").toFile();
         if (jdk.exists()) {
            return true;
         }
      } catch (Exception e) {
         logger.debug("Error checking for JDK", e);
      }

      logger.info("JDK not found, assuming only JRE present.");
      return false;
   }

   protected boolean isProcessPresent(String pid) {
      if (pid.equals("0")) {
         return false;
      }
      return true;
   }
}
