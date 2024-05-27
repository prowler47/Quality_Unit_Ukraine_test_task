
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataTool implements AbstractDataTool<String> {

    @Override
    public void processData(String inputData) {
        List<String[]> listOfCLines = new ArrayList<>();
        List<String> lines = Arrays.stream(inputData.split("\n")).toList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        int stringLengthDate = 10;
        int stringLengthRange = 12;
        int totalTime = 0;
        int count = 0;
        for (String line : lines) {
            if (line.startsWith("C")) {
                listOfCLines.add(line.split(" "));
            }
            if (line.startsWith("D")) {
                String[] splitDLine = line.split(" ");
                String serviceFromD = splitDLine[1];
                String questionFromD = splitDLine[2];
                String[] dates = processStringRangeOfDates(splitDLine[4], stringLengthRange).split("-");
                LocalDate startDate = LocalDate.parse(processStringDate(dates[0], stringLengthDate), formatter);
                LocalDate endDate = LocalDate.parse(processStringDate(dates[1], stringLengthDate), formatter);
                boolean isAnyMatch = false;

                for (String[] cLine : listOfCLines) {
                    String serviceFromC = cLine[1];
                    String questionFromC = cLine[2];
                    LocalDate dateFromC = LocalDate.parse(processStringDate(cLine[4], stringLengthDate), formatter);
                    int time = Integer.parseInt(cLine[5]);
                    if (checkService(serviceFromC, serviceFromD) && checkQuestion(questionFromC, questionFromD) && checkDate(startDate, endDate, dateFromC)) {
                        totalTime += time;
                        count++;
                        isAnyMatch = true;
                    }
                }
                if (!isAnyMatch) {
                    System.out.println("-");
                } else {
                    int avg = totalTime/count;
                    System.out.println(avg);
                    totalTime = 0;
                    count = 0;
                }
            }
        }
    }

    private String processStringDate(String date, int stringLengthDate) {
        if (date.length() != stringLengthDate) {
           return "0" + date;
        }
        return date;
    }

    private String processStringRangeOfDates(String dates, int stringLengthRange) {
        if (dates.length() < stringLengthRange) {
            return dates + "-" + dates;
        }
        return dates;
    }

    private boolean checkService(String serviceFromC, String serviceFromD) {
        if (serviceFromC.startsWith("*")) {
            return true;
        }
        return serviceFromC.substring(0, 1).equals(serviceFromD.substring(0, 1));
    }

    private boolean checkQuestion(String questionFromC, String questionFromD) {
        if (questionFromD.startsWith("*")) {
            return true;
        }
        return questionFromC.substring(0, 1).equals(questionFromD.substring(0, 1));
    }

    private boolean checkDate(LocalDate startDate, LocalDate endDate, LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
}
