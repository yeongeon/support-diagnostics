package com.elastic.support.diagnostics.commands;

import com.elastic.support.diagnostics.Constants;
import com.elastic.support.diagnostics.InputParams;
import com.elastic.support.util.SystemProperties;
import com.elastic.support.diagnostics.chain.DiagnosticContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

         List<String> aliases = context.getAliases();
         if(context.getInputParams().useAliases() && aliases.size()>0){
            logger.error( String.format(">>>>> %s", aliases ));
         }
         Pattern pattern = Pattern.compile(Constants.MATCH_REGEX);
         Matcher match = pattern.matcher(params.toString());

         if(match.find()){
            logger.error("match.group(0): {}", match.group());
         }

          manifest.put("inputs", params.toString());

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
