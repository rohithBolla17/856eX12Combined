package org.example;

import java.io.*;
import java.nio.file.Files;

public class X12FileProcessor {
    public static void main(String[] args) {
        String inputDirPath = "D:\\data\\apps\\MLSCPortal\\856E_X12\\Output";
        String outputFilePath = "D:\\data\\apps\\MLSCPortal\\856E_X12\\CombinedOutput.856E";

        try {
            File inputDir = new File(inputDirPath);
            File[] files = inputDir.listFiles((dir, name) -> name.endsWith(".856E"));

            if (files == null || files.length == 0) {
                System.out.println("No files ending with '.856E' found in the directory.");
                return;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
                for (File file : files) {
                    processFile(file, writer);
                }
            }

            System.out.println("Processing completed. Output written to: " + outputFilePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void processFile(File file, BufferedWriter writer) throws IOException {
        boolean isInSegment = false;
        StringBuilder segmentBuffer = new StringBuilder();

        String content = new String(Files.readAllBytes(file.toPath()));
        String[] segments = content.split("~");

        for (String segment : segments) {
            if (segment.startsWith("ST")) {
                isInSegment = true;
                segmentBuffer.setLength(0);
            }

            if (isInSegment) {
                //segmentBuffer.append(segment).append("~").append(System.lineSeparator());
                segmentBuffer.append(segment).append("~");
               //segmentBuffer.append(segment).append(System.lineSeparator());
            }

            if (segment.startsWith("SE")) {
                isInSegment = false;
                // Write the captured segment to the output file
                writer.write(segmentBuffer.toString());
                //writer.write(System.lineSeparator());
            }
        }
    }
}
