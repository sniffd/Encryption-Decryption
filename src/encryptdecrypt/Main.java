package encryptdecrypt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

interface Strategy {

}

interface EncryptStrategy extends Strategy {

    void encrypt(char[] arrayChar, int eNumber);
}

interface DecryptStrategy extends Strategy {

    void decrypt(char[] arrayChar, int eNumber);
}

class ShiftEncryptStrategy implements EncryptStrategy {

    @Override
    public void encrypt(char[] arrayChar, int eNumber) {
        for (int i = 0; i < arrayChar.length; i++) {
            if (arrayChar[i] >= 'a' && arrayChar[i] <= 'z') {
                arrayChar[i] = (char) ((arrayChar[i] - 'a' + eNumber) % 26 + 'a');
            } else if (arrayChar[i] >= 'A' && arrayChar[i] <= 'Z') {
                arrayChar[i] = (char) ((arrayChar[i] - 'A' + eNumber) % 26 + 'A');
            }
        }
    }
}

class UnicodeEncryptStrategy implements EncryptStrategy {

    @Override
    public void encrypt(char[] arrayChar, int eNumber) {
        for (int i = 0; i < arrayChar.length; i++) {
            arrayChar[i] = (char) (arrayChar[i] + eNumber);
        }
    }
}

class ShiftDecryptStrategy implements DecryptStrategy {

    @Override
    public void decrypt(char[] arrayChar, int eNumber) {
        eNumber = 26 - eNumber % 26;
        for (int i = 0; i < arrayChar.length; i++) {
            if (arrayChar[i] >= 'a' && arrayChar[i] <= 'z') {
                arrayChar[i] = (char) ((arrayChar[i] - 'a' + eNumber) % 26 + 'a');
            } else if (arrayChar[i] >= 'A' && arrayChar[i] <= 'Z') {
                arrayChar[i] = (char) ((arrayChar[i] - 'A' + eNumber) % 26 + 'A');
            }
        }
    }
}

class UnicodeDecryptStrategy implements DecryptStrategy {

    @Override
    public void decrypt(char[] arrayChar, int eNumber) {
        for (int i = 0; i < arrayChar.length; i++) {
            arrayChar[i] = (char) (arrayChar[i] - eNumber);
        }
    }
}

public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        String actionType = null;
        String message = new String("");
        Integer eNumber = null;
        File fileNameRead = null;
        File fileNameWrite = null;
        String outPutOption = "terminal";
        String alg = "shift";
        Strategy strategy = null;

        for (int i = 0; i < args.length; i += 2) {
            switch (args[i]) {
                case "-mode":
                    actionType = args[i + 1];
                    if (!(actionType.equalsIgnoreCase("enc") || actionType.equalsIgnoreCase("dec"))) {
                        System.out.println("Unknown operation");
                        break;
                    }
                    break;
                case "-key":
                    eNumber = Integer.parseInt(args[i + 1]);
                    break;
                case "-data":
                    message = args[i + 1];
                    break;
                case "-in":
                    fileNameRead = new File(args[i + 1]);
                    break;
                case "-out":
                    fileNameWrite = new File(args[i + 1]);
                    outPutOption = "file";
                    break;
                case "-alg":
                    alg = args[i + 1];
                    break;
                default:
                    if (actionType.isBlank()) {
                        actionType = "enc";
                    }
                    if (eNumber == null) {
                        eNumber = 0;
                    }
            }
        }

        if ("enc".equals(actionType)) {
            if ("shift".equals(alg)) {
                strategy = new ShiftEncryptStrategy();
            } else if ("unicode".equals(alg)) {
                strategy = new UnicodeEncryptStrategy();
            }
        } else if ("dec".equals(actionType)) {
            if ("shift".equals(alg)) {
                strategy = new ShiftDecryptStrategy();
            } else if ("unicode".equals(alg)) {
                strategy = new UnicodeDecryptStrategy();
            }
        }

        switch (actionType) {
            case "enc":
                if(!("".equalsIgnoreCase(message))) {
                    main.encrypt(message, eNumber, fileNameWrite, outPutOption, (EncryptStrategy) strategy);
                } else if (fileNameRead.exists()){
                    main.encrypt(fileNameRead, eNumber, fileNameWrite,outPutOption, (EncryptStrategy) strategy);
                } else {
                    main.encrypt(message, eNumber, fileNameWrite,outPutOption, (EncryptStrategy) strategy);
                }
                break;
            case "dec":
                if(!("".equalsIgnoreCase(message))) {
                    main.decrypt(message, eNumber, fileNameWrite,outPutOption, (DecryptStrategy) strategy);
                } else if (fileNameRead.exists()){
                    main.decrypt(fileNameRead, eNumber, fileNameWrite,outPutOption, (DecryptStrategy) strategy);
                } else {
                    main.decrypt(message, eNumber, fileNameWrite,outPutOption, (DecryptStrategy) strategy);
                }
                break;
            default:
                System.out.println("Incorrect type");
        }
    }


    public void encrypt(String message, int eNumber, File fileNameWrite, String outPutOption, EncryptStrategy strategy) {
        char[] arrayChar = message.toCharArray();

        strategy.encrypt(arrayChar, eNumber);

        if(outPutOption.equalsIgnoreCase("file")){
            try {
                FileWriter fileWriter = new FileWriter(fileNameWrite);
                fileWriter.write(arrayChar);
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e){
                System.out.println("Error :" + e.getMessage());
            }
        } else {
            System.out.println(arrayChar);
        }

    }

    public void encrypt(File fileNameRead, int eNumber, File fileNameWrite, String outPutOption, EncryptStrategy strategy) {
        try{
            Scanner scannedFile = new Scanner(fileNameRead);
            while(scannedFile.hasNext()){
                char[] arrayChar = scannedFile.nextLine().toCharArray();

                strategy.encrypt(arrayChar, eNumber);

                if(outPutOption.equalsIgnoreCase("file")){
                    try {
                        FileWriter fileWriter = new FileWriter(fileNameWrite);
                        fileWriter.write(arrayChar);
                        fileWriter.flush();
                        fileWriter.close();

                    } catch (IOException e){
                        System.out.println("Error :" + e.getMessage());
                    }
                } else {
                    System.out.println(arrayChar);
                }
            }
            scannedFile.close();
        } catch (FileNotFoundException e){
            System.out.println("Error :" + e.getMessage());
        }

    }


    public void decrypt(String message, int eNumber, File fileNameWrite, String outPutOption, DecryptStrategy strategy) {
        char[] arrayChar = message.toCharArray();

        strategy.decrypt(arrayChar, eNumber);

        if(outPutOption.equalsIgnoreCase("file")){
            try {
                FileWriter fileWriter = new FileWriter(fileNameWrite);
                fileWriter.write(arrayChar);
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e){
                System.out.println("Error :" + e.getMessage());
            }
        } else {
            System.out.println(arrayChar);
        }
    }


    public void decrypt(File fileNameRead, int eNumber, File fileNameWrite, String outPutOption, DecryptStrategy strategy) {
        try{
            Scanner scannedFile = new Scanner(fileNameRead);
            while(scannedFile.hasNext()){
                char[] arrayChar = scannedFile.nextLine().toCharArray();

                strategy.decrypt(arrayChar, eNumber);

                if(outPutOption.equalsIgnoreCase("file")){
                    try {
                        FileWriter fileWriter = new FileWriter(fileNameWrite);
                        fileWriter.write(arrayChar);
                        fileWriter.flush();
                        fileWriter.close();

                    } catch (IOException e){
                        System.out.println("Error :" + e.getMessage());
                    }
                } else {
                    System.out.println(arrayChar);
                }
            }
            scannedFile.close();
        } catch (FileNotFoundException e){
            System.out.println("Error :" + e.getMessage());
        }
    }

}

