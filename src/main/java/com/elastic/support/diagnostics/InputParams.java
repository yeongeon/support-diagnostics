package com.elastic.support.diagnostics;

import com.beust.jcommander.*;
import com.elastic.support.diagnostics.chain.DiagnosticContext;
import com.elastic.support.util.AliasUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InputParams {
   protected final Logger logger = LogManager.getLogger();

   @Parameter(names = {"-?", "--help"}, description = "Help contents.", help = true)
   private boolean help;

   @Parameter(names = {"-o", "--out", "--output", "--outputDir"}, description = "Fully qualified path to output directory or c for current working directory.")
   private String outputDir = "cwd";

   @Parameter(names = {"-h", "--host",}, required = true, description = "Required field.  Hostname, IP Address, or localhost.  HTTP access must be enabled.")
   private String host = "";

   @Parameter(names = {"--port"}, description = "HTTP or HTTPS listening port. Defaults to 9200.")
   private int port = 9200;

   @Parameter(names = {"-u", "--user"}, description = "Username.")
   private String username;

   @Parameter(names = {"-p", "--password"}, description = "Password", password = true)
   private String password;

   @Parameter(names = {"-s", "--ssl", "--https"}, description = "Use SSL?  No value required, only the option.")
   private boolean isSsl = false;

   @Parameter(names = {"--type"}, description ="Diagnostic type to run. Enter standard, remote, logstash. Default is standard. Using remote will suppress retrieval of logs, configuration and system command info.")
   private String diagType = "diagnostics";

   @Parameter(names = {"--ptp"}, description = "Insecure plain text password - allows you to input the password as a plain text argument for scripts. WARNING: Exposes passwords in clear text. Inherently insecure.")
   private String plainTextPassword = "";

   @Parameter(names = {"--reps"}, description = "Number of times to execute the diagnostic. Use to create multiple runs at timed intervals.")
   private int reps = 1;

   @Parameter(names = {"--interval"}, description = "Elapsed time in seconds between diagnostic runs when in repeating mode.")
   private long interval = 30;

   @Parameter(names = {"--archivedLogs"}, description = "Get archived logs in addition to current ones if present - No value required, only the option.")
   private boolean archivedLogs=false;

   @Parameter(names = {"--scrub"}, description = "Set to true to use the scrub.yml dictionary to scrub logs and config files.  See README for more info.")
   private boolean scrubFiles = false;

   @Parameter(names = {"--noVerify"}, description = "Use this option to bypass hostname verification for certificate. This is inherently unsafe and NOT recommended.")
   private boolean skipVerification = false;

   @Parameter(names= {"--keystore"}, description = "Keystore for client certificate.")
   private String keystore;

   @Parameter(names= {"--keystorePass"}, description = "Keystore password for client certificate.", password = true)
   private String keystorePass;

   @Parameter(names = {"--noLogs"}, description = "Use this option to suppress log collection.")
   private boolean skipLogs = false;

   @Parameter(names = {"--accessLogs"}, description = "Use this option to collect access logs as well.")
   private boolean accessLogs = false;

   @Parameter(names = {"--threads"}, description = "Collect only hot threads.")
   private boolean hotThreads = false;

   @Parameter(names = {"--bypassDiagVerify"}, description = "Don't check the diagnostic version by querying Github.")
   private boolean bypassDiagVerify = false;

   @Parameter(names= {"--proxyHost"}, description = "HTTP Proxy host.")
   private String proxyHost;

   @Parameter(names= {"--proxyPort"}, description = "HTTP Proxy port.")
   private int proxyPort;

   @Parameter(names= {"--proxyPass"}, description = "HTTP Proxy password.", password = true)
   private String proxyPass;

   @Parameter(names = {"--aliases"}, description = "Use alias instead of real hostnames or ip addresses.")
   private boolean aliases = false;

   private boolean secured = false;
   private boolean wasPortSet = false;

   private DiagnosticContext ctx;

   public DiagnosticContext getCtx() {
      return ctx;
   }

   public void setCtx(DiagnosticContext ctx) {
      this.ctx = ctx;
   }

   public boolean useAliases() {
      return aliases;
   }
   public void setAliases(boolean useAliases) {
      this.aliases = useAliases;
   }

   public boolean isHotThreads() {
      return hotThreads;
   }

   public void setHotThreads(boolean hotThreads) {
      this.hotThreads = hotThreads;
   }

   public boolean isSkipLogs() {
      return skipLogs;
   }

   public void setSkipLogs(boolean skipLogs) {
      this.skipLogs = skipLogs;
   }

   public String getKeystore() {
      return keystore;
   }

   public void setKeystore(String keystore) {
      this.keystore = keystore;
   }

   public String getKeystorePass() {
      return keystorePass;
   }

   public void setKeystorePass(String keystorePass) {
      this.keystorePass = keystorePass;
   }

   public boolean isSkipVerification() {
      return skipVerification;
   }

   public void setSkipVerification(boolean skipVerification) {
      this.skipVerification = skipVerification;
   }

   public boolean isScrubFiles() {
      return scrubFiles;
   }

   public void setScrubFiles(boolean scrubFiles){
      this.scrubFiles = scrubFiles;
   }

   public String getHost() {
      return host;
   }

   public void setHost(String host) {
      this.host = host;
   }

   public String getDiagType() {
      return diagType;
   }

   public void setDiagType(String diagType) {
      this.diagType = diagType;
   }

   public int getPort() {
      if(diagType.equalsIgnoreCase("logstash")){
         if( this.port == 9200 ){
            return Constants.LOGSTASH_PORT;
         }
      }
      return port;
   }

   public void setPort(int port) {
      this.port = port;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getOutputDir() {
      return outputDir;
   }

   public void setOutputDir(String outputDir) {
      this.outputDir = outputDir;
   }

   public boolean isSecured() {
      return (this.username != null && this.password != null);
   }

   public boolean isSsl() {
      return isSsl;
   }

   public void setIsSsl(boolean isSsl) {
      this.isSsl = isSsl;
   }

   public void setSecured(boolean secured) {
      this.secured = secured;
   }

   public boolean isHelp() {
      return help;
   }

   public void setHelp(boolean help) {
      this.help = help;
   }

   public int getReps() {
      return reps;
   }

   public void setReps(int reps) {
      if (reps < 1) {
         throw new IllegalArgumentException("Number of repetitions must be at least 1.");
      }
      this.reps = reps;
   }

   public long getInterval() {
      return interval;
   }

   public void setInterval(long interval) {
      this.interval = interval;
   }

   public String getPlainTextPassword() {
      return plainTextPassword;
   }

   public void setPlainTextPassword(String plainTextPassword) {
      this.plainTextPassword = plainTextPassword;
   }

   public boolean isArchivedLogs() {
      return archivedLogs;
   }

   public void setArchivedLogs(boolean archivedLogs) {
      this.archivedLogs = archivedLogs;
   }

   public boolean isAccessLogs() {
      return accessLogs;
   }

   public void setAccessLogs(boolean accessLogs) {
      this.accessLogs = accessLogs;
   }

   public String getUrl() {
      return getProtocol() + "://" + getHost() + ":" + getPort();
   }

   public String getProtocol(){
      if (this.isSsl) {
         return  "https";
      } else {
         return "http";
      }
   }

   @Override
   public String toString() {
      return "InputParams{" +
         "help=" + help +
         ", outputDir='" + outputDir + '\'' +
         ", host='" + (AliasUtil.alias(this.ctx, host)) + '\'' +
         ", port=" + port +
         ", isSsl=" + isSsl +
         ", diagType='" + diagType + '\'' +
         ", reps=" + reps +
         ", interval=" + interval +
         ", archivedLogs=" + archivedLogs +
         ", scrubFiles=" + scrubFiles +
         ", skipVerification=" + skipVerification +
         ", keystore='" + keystore + '\'' +
         ", skipLogs=" + skipLogs +
         ", skipAccessLogs=" + accessLogs +
         ", secured=" + secured +
         ", wasPortSet=" + wasPortSet +
         ", aliases=" + aliases +
         '}';
   }
}
