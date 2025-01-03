package SMT1B;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class SMT1B {

    private ArrayList<String> ReadFile(int number) {
        if (number < 0 || number > 99) throw new IllegalArgumentException("Number must be between 0 and 99");
        String path = String.format("src/SMT1B/formulas/%02d.limboole", number);
        ArrayList<String> lines = new ArrayList<>();
        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                lines.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return lines;
    }

    private ArrayList<ArrayList<String>> FormatContent(ArrayList<String> content) {
        ArrayList<ArrayList<String>> formattedContent = new ArrayList<>();
        ArrayList<String> currentContent = new ArrayList<>();
        for (String line : content) {
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == '!') {
                    currentContent.add(String.valueOf(line.charAt(i)) + line.charAt(i + 1));
                    i += 2;
                }
                if (Character.isLetter(line.charAt(i))) {
                    currentContent.add(String.valueOf(line.charAt(i)));
                }
            }
            formattedContent.add(currentContent);
            currentContent = new ArrayList<>();
        }
        return formattedContent;
    }

    private ArrayList<String> CreateOutput(ArrayList<ArrayList<String>> content) {
        ArrayList<String> output = new ArrayList<>();
        StringBuilder firstLine = new StringBuilder("(");
        for (int i = 0; i < content.size(); i++) {
            if (i == 0) {
                firstLine.append("l").append(i + 1).append(" |");
                continue;
            }
            if (i == content.size() - 1) {
                firstLine.append(" l").append(i + 1);
            } else {
                firstLine.append(" l").append(i + 1).append(" |");
            }
        }
        firstLine.append(") &");
        output.add(firstLine.toString());


        for (int i = 0; i < content.size(); i++) {
            StringBuilder currentLine = new StringBuilder("(");
            for (String character : content.get(i)) {
                currentLine.append("!l").append(i + 1).append(" | ");
                currentLine.append(character);
                currentLine.append(") & (");
            }


            for (String character : content.get(i)) {
                if (character.contains("!")) {
                    currentLine.append(character.charAt(1));
                } else {
                    currentLine.append("!").append(character);
                }
                currentLine.append(" | ");
            }
            currentLine.append("l").append(i + 1).append(") &");
            if (i == content.size() - 1) {
                currentLine = new StringBuilder(currentLine.substring(0, currentLine.length() - 2));
            }
            output.add(currentLine.toString());
        }


        return output;
    }

    private void processInput(int number) {
        SMT1B smt1b = new SMT1B();
        ArrayList<String> content = smt1b.ReadFile(number);
        ArrayList<ArrayList<String>> formattedContent = smt1b.FormatContent(content);
        ArrayList<String> output = smt1b.CreateOutput(formattedContent);
        String path = String.format("src/SMT1B/results/%02d.txt", number);
        try {
            File myObj = new File(path);
            myObj.createNewFile();
            java.io.FileWriter myWriter = new java.io.FileWriter(path);
            for (String line : output) {
                myWriter.write(line + "\n");
            }
            myWriter.close();
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    public void execute() {
        File directory = new File("src/SMT1B/results");
        if (!directory.exists()) {
            directory.mkdir();
        } else {
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                file.delete();
            }
        }
        for (int i = 0; i < 100; i++) {
            processInput(i);
        }
    }

}
