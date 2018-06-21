package com.elastic.support.diagnostics.commands;

import com.elastic.support.diagnostics.chain.DiagnosticContext;
import com.elastic.support.util.JsonYamlUtils;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScrubLogsCmd extends AbstractDiagnosticCmd {

   // To do , Hash and IP placeholders for specialized sub.

   public boolean execute(DiagnosticContext context) {

      if (! context.isLocalAddressLocated() || ! context.getInputParams().isScrubFiles()) {
         logger.info("Log scrubbing not configured.");
         return true;
      }

      List<String> tempFileDirs = (List<String>)context.getAttribute("tempFileDirs");

      logger.info("Scrubbing elasticsearch logs using scrub.yml.");

      try {
         Map<String, Object> dictionary = JsonYamlUtils.readYamlFromClasspath("scrub.yml", false);
         if (dictionary.size() == 0){
            logger.warn("Scrubbing was enabled but no substitutions were defined. Bypassing log file processing.");
            return true;
         }

         for(String logdir: tempFileDirs) {
            Collection<File> files = FileUtils.listFiles(new File(logdir), new String[]{"log"}, true);
            for (File fl : files) {
               scrubFile(fl, dictionary);
            }
         }

      } catch (Exception e) {
         logger.error("Error scrubbing log files.", e);
         logger.error("Password removal failed - please examine archive to ensure sensitive information is removed.");
      }

      logger.info("Finished scrubbing logs.");

      return true;
   }

   private  void scrubFile(File file, Map<String, Object> dictionary) throws Exception{
      String filepath = file.getAbsolutePath();
      String filename = file.getName();
      String scrubbedFilename = filepath.replaceAll(filename, "scrubbed-"+ filename);

      Set<Map.Entry<String, Object>> entries = dictionary.entrySet();

      BufferedReader br = new BufferedReader(new FileReader(file));
      BufferedWriter bw = new BufferedWriter((
         new FileWriter(scrubbedFilename)));

      String line = br.readLine();
      while(line != null){
         for(Map.Entry entry: entries ){
            line = line.replaceAll(entry.getKey().toString(), entry.getValue().toString());
         }
         bw.write(line);
         bw.newLine();
         line = br.readLine();
      }

      br.close();
      bw.flush();
      bw.close();

      FileUtils.deleteQuietly(file);

   }

}