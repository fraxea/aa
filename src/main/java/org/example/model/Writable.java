package org.example.model;

import java.io.File;
import java.util.List;

public interface Writable<T> {
    
    void writeDataInFile();
    
    File getFile();
    
    List<T> readFromFile();
    
}
