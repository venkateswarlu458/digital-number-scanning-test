package com.digital.number.scanner.service;

import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class DigitalNumberScannerService {

    static Logger logger = Logger.getLogger(DigitalNumberScannerService.class);

    //Used below seven segment to derive the actual digit.
    /*
         _0_
       5|   |1
         _6_
       4|   |2
         _3_
    */
    static Map<Integer,Integer> segmentMap = new HashMap<>();
    static
    {
        segmentMap.put(63,0);
        segmentMap.put(6,1);
        segmentMap.put(91,2);
        segmentMap.put(79,3);
        segmentMap.put(102,4);
        segmentMap.put(109,5);
        segmentMap.put(125,6);
        segmentMap.put(7,7);
        segmentMap.put(127,8);
        segmentMap.put(111,9);
    };

    /** This method used to scan all files from a given folder and generate the number based on file content.
     * @Param inputFilePath where system can lookup a folder and process all files one by one.
     * @Return provide the list of numbers generated based on provided files.
     * @Exception - IOException if file not available.
     * */
    public List<String> performScanning(String inputFilePath) throws IOException {

        AtomicInteger lineCount= new AtomicInteger(0);
        List<String>  finalNumberList = new ArrayList<>();
        List<String>  linesList = new ArrayList<>();

        try(Stream<String> lines = Files.lines(Paths.get(inputFilePath))){
            lines.filter(line->line!=null && line.trim().length()>0)
                    .forEach(line -> {
                            if(lineCount.get()%3==0 && linesList.size()==3){
                                finalNumberList.add(getFinalNumber(linesList));
                                linesList.clear();
                                linesList.add(line);
                            }else{
                                linesList.add(line);
                            }
                            lineCount.incrementAndGet();

                         if(line.length()!=27 && line.trim().length()>0){
                           logger.error("Incorrect line length {"+line+"}");
                          }
                    });
        }catch (FileNotFoundException exp){
            throw new IOException("File not found Exception:"+exp.getMessage());
        };

        //Below code will be executed only for last 3 digital lines
        if(lineCount.get()%3==0){
            finalNumberList.add(getFinalNumber(linesList));
            linesList.clear();
        }
        return finalNumberList;
    }

    /** Generate the numeric numbers from Digital format.
     * @Param listofLines lines from files.
     * "Return complete number, which is generated out from 3 lines of a file.
     * */
    public String getFinalNumber(List<String> list) {

                StringBuffer sb = new StringBuffer();

                List<String> line1List = Arrays.asList(list.get(0).split("(?<=\\G.{" + 3 + "})"));
                List<String> line2List = Arrays.asList(list.get(1).split("(?<=\\G.{" + 3 + "})"));
                List<String> line3List = Arrays.asList(list.get(2).split("(?<=\\G.{" + 3 + "})"));

                for (int count = 0; count < line1List.size(); count++) {
                 try {
                    char charArray[][] = {line1List.get(count).toCharArray(), line2List.get(count).toCharArray(), line3List.get(count).toCharArray()};
                     StringBuffer segment = new StringBuffer();
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                           try {
                               if (i == 0 && charArray[i][j] == '_' && j == 1) {
                                   segment.append("0");
                               } else if (i == 1 && charArray[i][j] == '|' && j == 0) {
                                   segment.append("5");
                               } else if (i == 1 && charArray[i][j] == '_' && j == 1) {
                                   segment.append("6");
                               } else if (i == 1 && charArray[i][j] == '|' && j == 2) {
                                   segment.append("1");
                               } else if (i == 2 && charArray[i][j] == '|' && j == 0) {
                                   segment.append("4");
                               } else if (i == 2 && charArray[i][j] == '_' && j == 1) {
                                   segment.append("3");
                               } else if (i == 2 && charArray[i][j] == '|' && j == 2) {
                                   segment.append("2");
                               } else if (charArray[i][j] != ' ') {
                                   segment.append('8');
                               }
                           }catch(ArrayIndexOutOfBoundsException exp){
                               segment.append('8');
                               logger.error("Incorrect digital number or null values supplied at segment line :" + count);
                           }
                        }
                    }
                     sb.append(digitalToNumeric(segment.toString()));
                    }catch(Exception exp){
                        logger.error("IException while generating fina number:" + exp.getMessage());
                    }
                }
                if(list.get(0).length()!=27 || list.get(1).length()!=27 || list.get(2).length()!=27) {
                    logger.error("Digital number line length not equals to 27 char length\n"+list.get(0)+"\n"+list.get(1)+"\n"+list.get(2));
                }
            String finalNumberContent =  sb.toString().contains("?")?sb.append("ILL").toString():sb.toString();
            logger.info("Generated Number: "+finalNumberContent);
            return  finalNumberContent;
    }

    private String digitalToNumeric(String str){
        int pow=0;
        for(int i=0; i<str.length(); i++){
            pow+=Math.pow(2,Character.getNumericValue(str.charAt(i)));
        }
        return segmentMap.containsKey(pow)? Integer.toString(segmentMap.get(pow)):"?";
    }
}