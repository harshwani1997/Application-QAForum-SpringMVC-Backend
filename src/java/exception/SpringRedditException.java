package com.example.springredditclone.exception;

import java.util.function.Supplier;

public class SpringRedditException extends RuntimeException {
    public SpringRedditException(String exMessage, Exception e) {
        super(exMessage);
    }

   public SpringRedditException(String exMessage)
   {
       super(exMessage);
   }
}
