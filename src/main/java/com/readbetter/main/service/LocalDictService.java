package com.readbetter.main.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;

@Service
public class LocalDictService implements ILocalDictService {

    @Override
    public String readFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("src\\demo.txt"));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }


}
