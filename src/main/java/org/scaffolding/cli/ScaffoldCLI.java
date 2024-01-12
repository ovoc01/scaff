package org.scaffolding.cli;

import lombok.Data;
import org.apache.commons.cli.*;
import org.scaffolding.database.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

@Data
public class ScaffoldCLI {
    private Options options;
    private String args[];
    private static final String ASCII_ART = "\n" + //
            "███████╗ ██████╗ █████╗ ███████╗███████╗ ██████╗ ██╗     ██████╗ \n" + //
            "██╔════╝██╔════╝██╔══██╗██╔════╝██╔════╝██╔═══██╗██║     ██╔══██╗\n" + //
            "███████╗██║     ███████║█████╗  █████╗  ██║   ██║██║     ██║  ██║\n" + //
            "╚════██║██║     ██╔══██║██╔══╝  ██╔══╝  ██║   ██║██║     ██║  ██║\n" + //
            "███████║╚██████╗██║  ██║██║     ██║     ╚██████╔╝███████╗██████╔╝\n" + //
            "╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝     ╚═╝      ╚═════╝ ╚══════╝╚═════╝ \n" + //
            "                                                                 \n" //
            ;


    public ScaffoldCLI(String[] args) {
        options = new Options();
        addOptions();
        this.args = args;
    }

    private void addOptions() {
        options.addOption("i", "init", false,
                "Create scaffold.config.json where to store the parameter of were scaffolding");
        options.addOption("c", "config", true, "Path to scaffold.config.json");
        options.addOption("e", "entity", false, "Generate entity");
        options.addOption("r", "restcontroller", false, "Generate restcontroller");
        options.addOption("p", "repository", false, "Generate repository");
        options.addOption("s", "service", false, "Generate service");
        options.addOption("dot", "dotnet", false, "Generate  dotnet ");
        options.addOption("j", "java", true, "Default options of scaffolding");
        options.addOption("t", "templates", true, "Class creation template , only work for java");
        options.addOption("sch", "schemas", true, "Class creation template , only work for java");
    }

    public void run() throws SQLException {
        String url = prompt("JDBC_URL");
        String user = prompt("USER");
        String password = prompt("PASSWORD");
        Connection c = DriverManager.getConnection(url, user, password);
        Database database = new Database();
        database.initAllTableSchemas(c);
        //database.showTables();

        while (true) {
            String newLine = promptBuild();
            String[] arguments = newLine.split(" ");

            if (arguments[0].equals("exit")) {
                break;
            } else if (arguments[0].equals("show")) {
                database.showTables();
                continue;
            } else if (arguments[0].equals("build")) {
                cli(arguments);
            } else {
                System.out.println("\u001B[31mError " + "Command not found" + "\u001B[0m");
            }
        }
    }

    public void cli(String[] args) {
        CommandLineParser parser = new org.apache.commons.cli.DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            for (Option opt: cmd.getOptions()){
                System.out.println(opt.getOpt());
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private String prompt(String prompt) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[").append(prompt).append("]: ");
        System.out.print(stringBuilder.toString());
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private String promptBuild() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("> ");
        System.out.print(stringBuilder.toString());
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }


}
