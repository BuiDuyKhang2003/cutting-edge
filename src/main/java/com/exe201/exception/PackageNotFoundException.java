package com.exe201.exception;

public class PackageNotFoundException extends RuntimeException{

    public PackageNotFoundException(String message){
        super(message);
    }
}
