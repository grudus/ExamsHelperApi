package com.grudus.examshelper.exceptions;


public class SubjectNotFoundException  extends NotFoundException {
    public SubjectNotFoundException() {
        super("Cannot find subject");
    }
}
