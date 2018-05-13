package com.elastic.support.diagnostics.commands;

import com.elastic.support.diagnostics.InputParams;
import com.elastic.support.util.SystemProperties;
import com.elastic.support.diagnostics.chain.DiagnosticContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class GenerateManifestCmd extends AbstractDiagnosticCmd {

   public boolean execute(DiagnosticContext context) {

      logger.info("Writing diagnostic manifest.");
      try {
         ObjectMapper mapper = new ObjectMapper();
         mapper.enable(SerializationFeature.INDENT_OUTPUT);
         Map<String, Object> manifest = new HashMap<>();
         manifest.put("diagToolVersion", getToolVersion());
         manifest.put("collectionDate", SystemProperties.getUtcDateString());
         InputParams params = context.getInputParams();
         logger.info("GenerateManifestCmd.execute(...) A: {}", params);
         params.setCtx(context);
         logger.info("GenerateManifestCmd.execute(...) B: {}", params);
         manifest.put("inputs", params.toString());
         logger.info("manifest.get(\"inputs\"): {}", manifest.get("inputs"));

         File manifestFile = new File(context.getTempDir() + SystemProperties.fileSeparator + "manifest.json");
         mapper.writeValue(manifestFile, manifest);

      } catch (Exception e) {
         logger.error("Error creating the manifest file\n", e);
      }

      return true;
   }

   public String getToolVersion() {
      String ver = GenerateManifestCmd.class.getPackage().getImplementationVersion();
      return (ver != null) ? ver : "Debug";
   }

   public String getIsoDate() {
      TimeZone tz = TimeZone.getTimeZone("UTC");
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
      df.setTimeZone(tz);
      String nowAsISO = df.format(new Date());
      return nowAsISO;
   }
}
