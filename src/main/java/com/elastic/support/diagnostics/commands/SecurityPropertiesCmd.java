package com.elastic.support.diagnostics.commands;

import com.elastic.support.diagnostics.chain.DiagnosticContext;
import com.elastic.support.util.SystemUtils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Security;
import java.util.*;

public final class SecurityPropertiesCmd extends AbstractDiagnosticCmd {

   @Override
   public boolean execute(final DiagnosticContext context) {

      Properties securityProperties = new Properties();
      Map diagProps = (Map)context.getConfig().get("security-settings");
      Set<String> keys = diagProps.keySet();
      for (String key: keys){
         String value = SystemUtils.safeToString(Security.getProperty(key), "");
         securityProperties.put(key, value);
      }

      final Path path = Paths.get(context.getTempDir(), "security.properties");
      try {
         OutputStream outpuStream = new FileOutputStream(path.toFile());
         BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outpuStream));
         securityProperties.store(writer, "");
      } catch (final Exception e) {
         logger.error("failed opening " + path.toString(), e);
         return false;
      }
      return true;
   }


}
