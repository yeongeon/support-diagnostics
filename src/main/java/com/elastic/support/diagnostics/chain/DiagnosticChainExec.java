package com.elastic.support.diagnostics.chain;

import com.elastic.support.util.JsonYamlUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public class DiagnosticChainExec {

   private static Logger logger = LogManager.getLogger(DiagnosticChainExec.class);

   public void runDiagnostic(DiagnosticContext context) {

      try {
         Map<String, Object> aliases = JsonYamlUtils.readYamlFromClasspath("aliases.yml", false);
         if (context.getInputParams().useAliases() && aliases.containsKey("aliases") && ((List)aliases.get("aliases")).size()==0) {
            logger.error("Required config file aliases.yml was not found or empty.");
            throw new RuntimeException("Empty aliases.yml");
         }
         context.setAliases((List)aliases.get("aliases"));

         Map<String, Object> diags = JsonYamlUtils.readYamlFromClasspath("diags.yml", true);
         if (diags.size() == 0) {
            logger.error("Required config file diags.yml was not found. Exiting application.");
            throw new RuntimeException("Missing diags.yml");
         }

         context.setConfig(diags);

         Map<String, Object> chains = JsonYamlUtils.readYamlFromClasspath("chains.yml", false);
         if (chains.size() == 0) {
            logger.error("Required config file chains.yml was not found. Exiting application.");
            throw new RuntimeException("Missing chain.yml");
         }

         String diagType = context.getInputParams().getDiagType();
         List<String> chain = (List) chains.get(diagType);

         Chain diagnostic = new Chain(chain);
         diagnostic.execute(context);

      } catch (Exception e) {
         logger.error("Error encountered running diagnostic. See logs for additional information.  Exiting application.", e);
         throw new RuntimeException("Diagnostic runtime error", e);
      }

   }

}