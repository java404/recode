package smartmon.oracle;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

@Slf4j
public class SmartMonOraCli {
  private static final Options options;
  private static final CommandLineParser parser;

  static  {
    options = new Options();
    options.addOption("c", "command", true, "command name");
    parser = new DefaultParser();
  }

  public static void main(String[] args) {
    try {
      final OracleCommands commands = new OracleCommands();
      final CommandLine cmd = parser.parse(options, args);

      final OracleCommand command = commands.getCommand(cmd.getOptionValue("c"));
      if (command == null) {
        log.error("Invalid command line option");
        return;
      }
      command.exec();
    } catch (ParseException error) {
      log.error("Cannot Parse command line: ", error);
    }
  }
}

